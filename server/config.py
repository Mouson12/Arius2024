import os

class Config:
    """
    Application configuration settings.
    
    WARNING: We know that in a production environment, these values should be:
    1. Loaded from environment variables
    2. Never committed to version control
    3. Use strong, randomly generated keys
    """
    
    # Database configuration
    SQLALCHEMY_DATABASE_URI = 'postgresql://admin:admin@garage_db:5432/garage_db'
    SQLALCHEMY_TRACK_MODIFICATIONS = False  # Disable FSAQLAlchemy modification tracking
    
    # Email configuration for SendGrid
    MAIL_SERVER = 'smtp.sendgrid.net'
    MAIL_PORT = 587
    MAIL_USE_TLS = True
    MAIL_USE_SSL = False
    MAIL_USERNAME = 'apikey'
    
    # WARNING: This should be stored as an environment variable
    MAIL_PASSWORD = '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'
    MAIL_DEFAULT_SENDER = 'warsztat.arius@gmail.com'
    
    # WARNING: This should be a strong, randomly generated key stored as an environment variable
    SECRET_KEY = 'Arius2024'

    