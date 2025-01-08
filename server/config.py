import os
class Config:
    SQLALCHEMY_DATABASE_URI = 'postgresql://admin:admin@garage_db:5432/garage_db'
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    MAIL_SERVER = 'smtp.sendgrid.net'
    MAIL_PORT = 587
    MAIL_USE_TLS = True
    MAIL_USE_SSL = False
    MAIL_USERNAME = 'apikey'
    MAIL_PASSWORD = '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'
    MAIL_DEFAULT_SENDER = 'warsztat.arius@gmail.com'
    SECRET_KEY = 'Arius2024'