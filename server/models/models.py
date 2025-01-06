from db_conf import db
from datetime import datetime

# Model czynności (katalog czynności z cennikiem)
class Service(db.Model):
    __tablename__ = 'services'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text, nullable=True)
    price = db.Column(db.Float, nullable=False)

    def __repr__(self):
        return f'<Service {self.name}>'

# Model zamówienia naprawy
class RepairOrder(db.Model):
    __tablename__ = 'repair_orders'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    vehicle_model = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text, nullable=False)
    status = db.Column(db.String(50), default='Pending')  # Status np. 'Pending', 'Completed'
    created_at = db.Column(db.DateTime, default=datetime.timezone.utc)
    appointment_date = db.Column(db.DateTime, nullable=False)

    user = db.relationship('User', backref=db.backref('repair_orders', lazy=True))

    def __repr__(self):
        return f'<RepairOrder {self.id}>'


# Model historii wykonanych napraw
class RepairHistory(db.Model):
    __tablename__ = 'repair_history'
    id = db.Column(db.Integer, primary_key=True)
    repair_order_id = db.Column(db.Integer, db.ForeignKey('repair_orders.id'), nullable=False)
    service_id = db.Column(db.Integer, db.ForeignKey('services.id'), nullable=False)
    report = db.Column(db.Text, nullable=False)
    completed_at = db.Column(db.DateTime, default=datetime.timezone.utc)

    repair_order = db.relationship('RepairOrder', backref=db.backref('repair_history', lazy=True))
    service = db.relationship('Service', backref=db.backref('repair_history', lazy=True))

    def __repr__(self):
        return f'<RepairHistory {self.id}>'

# Model oceny warsztatu
class WorkshopRating(db.Model):
    __tablename__ = 'workshop_ratings'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    repair_order_id = db.Column(db.Integer, db.ForeignKey('repair_orders.id'), nullable=False)
    rating = db.Column(db.Integer, nullable=False)  # Ocena od 1 do 5
    comment = db.Column(db.Text, nullable=True)

    user = db.relationship('User', backref=db.backref('workshop_ratings', lazy=True))
    repair_order = db.relationship('RepairOrder', backref=db.backref('workshop_ratings', lazy=True))

    def __repr__(self):
        return f'<WorkshopRating {self.rating}>'
