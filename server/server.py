from flask import Flask, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager
from models import db
from config import Config
from endpoints import api, mail
from auth import auth
from sqlalchemy.exc import OperationalError

# Initialize Flask application
app = Flask(__name__)

try:
    # Load configuration from Config class
    app.config.from_object(Config)
    
    # Initialize Flask extensions
    db.init_app(app)  # Database connection
    mail.init_app(app)  # Email functionality
    
    # Create database tables within application context
    with app.app_context():
        db.create_all()
except OperationalError as e:
    # Handle database connection failures
    print("Database connection failed. Please ensure the database is running and accessible.")
    print(f"Error: {e}")
    exit(1)
except Exception as e:
    # Handle any other initialization errors
    print("An unexpected error occurred during app initialization.")
    print(f"Error: {e}")
    exit(1)

# Initialize JWT manager for handling authentication tokens
jwt = JWTManager(app)

# Register blueprints with URL prefixes
app.register_blueprint(api, url_prefix="/api")  # API routes
app.register_blueprint(auth, url_prefix="/auth")  # Authentication routes

@app.errorhandler(404)
def page_not_found(e):
    """
    Global 404 error handler for the application.
    Returns JSON response instead of default HTML.
    """
    return jsonify({"error": "Page not found"}), 404

# Application entry point
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)