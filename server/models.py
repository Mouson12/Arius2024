from flask_sqlalchemy import SQLAlchemy
from datetime import datetime, timezone, timedelta
from werkzeug.security import generate_password_hash, check_password_hash
from flask_jwt_extended import JWTManager, create_access_token
from datetime import timedelta

db = SQLAlchemy()


class User(db.Model):
    __tablename__ = 'users'
    user_id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String, nullable=False)
    email = db.Column(db.String, unique=True, nullable=False)
    password_hash = db.Column(db.String, nullable=False)
    created_at = db.Column(db.DateTime(), default=datetime.utcnow, index=True)

    def set_password(self, password):
        #Hash the password when setting it
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        #Check if the provided password matches the stored hash
        return check_password_hash(self.password_hash, password)

    def generate_jwt(self):
        #Generate JWT token for the user that expires after 10 days
        return create_access_token(identity=str(self.user_id), expires_delta=timedelta(days=10))

class Alarm(db.Model):
    __tablename__ = 'alarm'
    id = db.Column(db.Integer, primary_key=True)
    fall_detected = db.Column(db.Boolean, nullable=False, default=False)  # True if fall detected
    
    def __repr__(self):
        return f"<Alarm(id={self.id}, fall_detected={self.fall_detected})>"

class Medications(db.Model):
    __tablename__ = 'medications'
    id = db.Column(db.Integer, primary_key=True)
    last_dose = db.Column(db.DateTime, nullable=False, default=datetime.now() + timedelta(hours=1) )  # Timestamp of last dose
    next_dose = db.Column(db.DateTime, nullable=False)  # Timestamp of next dose
    delay_minutes = db.Column(db.Integer, nullable=False)  # Delay in minutes
    
    def __repr__(self):
        return f"<Medications(id={self.id}, last_dose={self.last_dose}, next_dose={self.next_dose}, delay_minutes={self.delay_minutes})>"
    

class Temperature(db.Model):
    __tablename__ = 'temperature'
    id = db.Column(db.Integer, primary_key=True)
    value = db.Column(db.Float, nullable=False)  # Temperature value in Celsius

    def __repr__(self):
        return f"<Temperature(id={self.id}, value={self.value}, timestamp={self.timestamp})>"

class PulseOximeter(db.Model):
    __tablename__ = 'pulse_oximeter'
    id = db.Column(db.Integer, primary_key=True)
    value = db.Column(db.Float, nullable=False)  # Oxygen saturation percentage (SpO2)

    def __repr__(self):
        return f"<PulseOximeter(id={self.id}, value={self.value}, pulse_rate={self.pulse_rate}, timestamp={self.timestamp})>"
    



class MedsTaken(db.Model):
    __tablename__ = 'meds_taken'
    id = db.Column(db.Integer, primary_key=True)
    meds_taken = db.Column(db.Boolean, nullable=False, default=False)
