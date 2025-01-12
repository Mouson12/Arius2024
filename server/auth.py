from flask import Blueprint, jsonify, request
from models import db, User
from werkzeug.security import generate_password_hash, check_password_hash
from flask_jwt_extended import JWTManager, create_access_token

# Initialize Blueprint for authentication routes
# The name parameter should be '__name__' not '**name**' - this appears to be a typo
auth = Blueprint('auth', '__name__')

@auth.route('/register', methods=['POST'])
def register():
    """
    User registration endpoint.
    
    Expects JSON payload with:
    - username: String, required
    - email: String, required, must be unique
    - password: String, required
    
    Returns:
    - 201: User successfully created
    - 400: Missing required fields or user already exists
    """
    data = request.get_json()
    username = data.get('username')
    email = data.get('email')
    password = data.get('password')

    # Validate required fields
    if not username or not email or not password:
        return jsonify({"message": "Username, email, and password are required."}), 400

    # Check for existing user to prevent duplicates
    existing_user = User.query.filter((User.email == email)).first()
    if existing_user:
        return jsonify({"message": "Username or email already in use."}), 400

    # Create new user with hashed password
    new_user = User(username=username, email=email)
    new_user.set_password(password)
    
    # Persist user to database
    db.session.add(new_user)
    db.session.commit()

    return jsonify({"message": "User registered successfully."}), 201

