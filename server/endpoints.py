from flask import Blueprint, jsonify, request
from models import db, Service, RepairOrder, RepairHistory, WorkshopRating
from datetime import datetime

# Create a Blueprint for routes
api = Blueprint('api', __name__)

@api.route('/services', methods=['GET'])
def get_services():
    """
    Endpoint to retrieve all available services (price list).
    """
    services = Service.query.all()
    return jsonify([{"id": s.id, "name": s.name, "description": s.description, "price": s.price} for s in services])

@api.route('/repair_orders', methods=['POST'])
def create_repair_order():
    """
    Endpoint to create a new repair order.
    """
    data = request.get_json()
    user_id = data.get('user_id')
    vehicle_model = data.get('vehicle_model')
    description = data.get('description')
    appointment_date = data.get('appointment_date')

    new_order = RepairOrder(
        user_id=user_id,
        vehicle_model=vehicle_model,
        description=description,
        appointment_date=datetime.strptime(appointment_date, '%Y-%m-%dT%H:%M:%S')
    )
    db.session.add(new_order)
    db.session.commit()

    return jsonify({"message": "Repair order created successfully.", "order_id": new_order.id}), 201

@api.route('/appointments', methods=['GET'])
def get_appointments():
    """
    Endpoint to retrieve all taken appointment dates.
    """
    orders = RepairOrder.query.all()
    taken_dates = [{"appointment_date": o.appointment_date.isoformat()} for o in orders]
    return jsonify(taken_dates)

@api.route('/repair_complete', methods=['POST'])
def complete_repair():
    """
    Endpoint to mark a repair as completed and add a repair history entry.
    """
    data = request.get_json()
    repair_order_id = data.get('repair_order_id')
    report = data.get('report')

    repair_order = RepairOrder.query.get(repair_order_id)
    if not repair_order:
        return jsonify({"message": "Repair order not found."}), 404

    repair_history = RepairHistory(
        repair_order_id=repair_order.id,
        service_id=data.get('service_id'),
        report=report,
        completed_at=datetime.utcnow()
    )
    repair_order.status = "Completed"
    db.session.add(repair_history)
    db.session.commit()

    # TODO: Send email with the report (email handling function)
    return jsonify({"message": "Repair completed and report sent."}), 200

@api.route('/repair_history/<int:user_id>', methods=['GET'])
def get_user_repair_history(user_id):
    """
    Endpoint to retrieve the repair history of a user.
    """
    history = RepairHistory.query.join(RepairOrder).filter(RepairOrder.user_id == user_id).all()
    history_data = [{
        "repair_history_id": h.id,
        "repair_order_id": h.repair_order_id,
        "service_id": h.service_id,
        "report": h.report,
        "completed_at": h.completed_at.isoformat()
    } for h in history]
    return jsonify(history_data)

@api.route('/ratings', methods=['POST'])
def rate_workshop():
    """
    Endpoint to submit a rating for the workshop.
    """
    data = request.get_json()
    user_id = data.get('user_id')
    repair_order_id = data.get('repair_order_id')
    rating = data.get('rating')
    comment = data.get('comment')

    new_rating = WorkshopRating(
        user_id=user_id,
        repair_order_id=repair_order_id,
        rating=rating,
        comment=comment
    )
    db.session.add(new_rating)
    db.session.commit()

    return jsonify({"message": "Rating submitted successfully."}), 201

@api.route('/ratings', methods=['GET'])
def get_ratings():
    """
    Endpoint to retrieve all workshop ratings.
    """
    ratings = WorkshopRating.query.all()
    return jsonify([{"id": r.id, "user_id": r.user_id, "repair_order_id": r.repair_order_id, "rating": r.rating, "comment": r.comment} for r in ratings])

@api.route('/repair_orders/<int:order_id>', methods=['GET'])
def get_repair_order(order_id):
    """
    Endpoint to retrieve a specific repair order by its ID.
    """
    repair_order = RepairOrder.query.get(order_id)
    if not repair_order:
        return jsonify({"message": "Order not found."}), 404
    return jsonify({
        "order_id": repair_order.id,
        "vehicle_model": repair_order.vehicle_model,
        "description": repair_order.description,
        "status": repair_order.status,
        "appointment_date": repair_order.appointment_date.isoformat()
    })
    
@api.route('/repair_orders/<int:order_id>', methods=['PUT'])
def update_repair_order(order_id):
    """
    Endpoint to update the status of a specific repair order by its ID.
    """
    data = request.get_json()
    repair_order = RepairOrder.query.get(order_id)
    if not repair_order:
        return jsonify({"message": "Order not found."}), 404

    repair_order.status = data.get('status', repair_order.status)
    db.session.commit()

    return jsonify({"message": "Repair order updated."}), 200

