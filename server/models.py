from flask_sqlalchemy import SQLAlchemy
from datetime import datetime, timezone, timedelta
from werkzeug.security import generate_password_hash, check_password_hash
from flask_jwt_extended import JWTManager, create_access_token

db = SQLAlchemy()

class User(db.Model):
    """
    User model for storing user details.
    """
    __tablename__ = 'users'
    user_id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String, nullable=False)
    email = db.Column(db.String, unique=True, nullable=False)
    password_hash = db.Column(db.String, nullable=False)
    created_at = db.Column(db.DateTime(), default=datetime.utcnow, index=True)

    def set_password(self, password):
        """
        Hash the password when setting it.
        :param password: Plain text password
        """
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        """
        Check if the provided password matches the stored hash.
        :param password: Plain text password
        :return: Boolean indicating if the password matches
        """
        return check_password_hash(self.password_hash, password)

    def generate_jwt(self):
        """
        Generate JWT token for the user that expires after 10 days.
        :return: JWT token as a string
        """
        return create_access_token(identity=str(self.user_id), expires_delta=timedelta(days=20))

class Service(db.Model):
    """
    Service model for storing service details.
    """
    __tablename__ = 'services'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text, nullable=True)
    price = db.Column(db.Float, nullable=False)

    def __repr__(self):
        """
        String representation of the Service object.
        :return: String
        """
        return f'<Service {self.name}>'

class RepairOrder(db.Model):
    """
    RepairOrder model for storing repair order details.
    """
    __tablename__ = 'repair_orders'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.user_id'), nullable=False)  # Updated to 'users.user_id'
    vehicle_model = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text, nullable=False)
    status = db.Column(db.String(50), default='Pending')  # Status np. 'Pending', 'Completed'
    created_at = db.Column(db.DateTime, default=datetime.utcnow())
    appointment_date = db.Column(db.DateTime, nullable=False)

    user = db.relationship('User', backref=db.backref('repair_orders', lazy=True))

    def __repr__(self):
        """
        String representation of the RepairOrder object.
        :return: String
        """
        return f'<RepairOrder {self.id}>'


class RepairHistory(db.Model):
    """
    RepairHistory model for storing repair history details.
    """
    __tablename__ = 'repair_history'
    id = db.Column(db.Integer, primary_key=True)
    repair_order_id = db.Column(db.Integer, db.ForeignKey('repair_orders.id'), nullable=False)
    service_id = db.Column(db.Integer, db.ForeignKey('services.id'), nullable=False) # TODO: Add many services in RepairHistory
    report = db.Column(db.Text, nullable=False)
    completed_at = db.Column(db.DateTime, default=datetime.utcnow())

    repair_order = db.relationship('RepairOrder', backref=db.backref('repair_history', lazy=True))
    service = db.relationship('Service', backref=db.backref('repair_history', lazy=True))

    def __repr__(self):
        """
        String representation of the RepairHistory object.
        :return: String
        """
        return f'<RepairHistory {self.id}>'

class WorkshopRating(db.Model):
    """
    WorkshopRating model for storing workshop ratings.
    """
    __tablename__ = 'workshop_ratings'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.user_id'), nullable=False)  # Updated to 'users.user_id'
    repair_order_id = db.Column(db.Integer, db.ForeignKey('repair_orders.id'), nullable=False)
    rating = db.Column(db.Integer, nullable=False)  # Rate from 1 to 5
    comment = db.Column(db.Text, nullable=True)

    user = db.relationship('User', backref=db.backref('workshop_ratings', lazy=True))
    repair_order = db.relationship('RepairOrder', backref=db.backref('workshop_ratings', lazy=True))

    def __repr__(self):
        """
        String representation of the WorkshopRating object.
        :return: String
        """
        return f'<WorkshopRating {self.rating}>'
