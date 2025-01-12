from flask import Blueprint, jsonify, request
from models import db, Service, RepairOrder, RepairHistory, WorkshopRating, User
from datetime import datetime
from flask_mail import Mail, Message
from flask_jwt_extended import jwt_required, get_jwt_identity

mail = Mail()

# Create a Blueprint for routes
api = Blueprint('api', __name__)

def get_user_by_jwt():
    user_id = get_jwt_identity()
    user = User.query.get(user_id)
    return user

# ********************************************* ZROBIONE *********************************************

@api.route('/user_data', methods=['GET'])
@jwt_required()
def get_user_data():
    """
    Endpoint to retrieve user data based on JWT identity.
    """
    user = get_user_by_jwt()
    if not user:
        return jsonify({"message": "User not found."}), 404
    
    return jsonify({
        "user_id": user.user_id,
        "username": user.username,
        "email": user.email,
        "created_at": user.created_at.isoformat()
    })

@api.route('/repair_orders', methods=['GET'])
def get_repair_orders():
    """
    Endpoint to retrieve all repair orders for admin.
    """
    repair_orders = RepairOrder.query.all()
    if not repair_orders:
        return jsonify({"message": "No orders found."}), 404
    return jsonify([{
        "order_id": ro.id,
        "vehicle_model": ro.vehicle_model,
        "description": ro.description,
        "status": ro.status,
        "appointment_date": ro.appointment_date.isoformat()
    } for ro in repair_orders])

@api.route('/services', methods=['GET'])
def get_services():
    """
    Endpoint to retrieve all available services (price list).
    """
    services = Service.query.all()
    return jsonify([{"id": s.id, "name": s.name, "description": s.description, "price": s.price} for s in services])

# ********************************************* ZROBIONE *********************************************

@api.route('/repair_orders', methods=['POST'])
@jwt_required()
def create_repair_order():
    """
    Endpoint to create a new repair order.
    """
    user = get_user_by_jwt()
    if not user:
        return jsonify({"message": "User not found."}), 404

    data = request.get_json()
    vehicle_model = data.get('vehicle_model')
    description = data.get('description')
    appointment_date = data.get('appointment_date')

    new_order = RepairOrder(
        user_id=user.user_id,
        vehicle_model=vehicle_model,
        description=description,
        appointment_date=datetime.strptime(appointment_date, '%Y-%m-%dT%H:%M:%S')
    )
    db.session.add(new_order)
    db.session.commit()

    return jsonify({"message": "Repair order created successfully.", "order_id": new_order.id}), 201

# ********************************************* ADMIN ZROBIONE *********************************************

@api.route('/appointments', methods=['GET'])
def get_appointments():
    """
    Endpoint to retrieve all taken appointment dates.
    """
    orders = RepairOrder.query.all()
    taken_dates = [{"appointment_date": o.appointment_date.isoformat()} for o in orders]
    return jsonify(taken_dates)


@api.route('/appointments/user', methods=['GET'])
@jwt_required()
def get_user_appointments():
    """
    Endpoint to retrieve all appointments for the authenticated user.
    """
    user = get_user_by_jwt()
    if not user:
        return jsonify({"message": "User not found."}), 404

    orders = RepairOrder.query.filter_by(user_id=user.user_id).all()
    if not orders:
        return jsonify({"message": "No appointments found for this user."}), 404
    user_appointments = [{"appointment_date": o.appointment_date.isoformat()} for o in orders]
    return jsonify(user_appointments)

# ********************************************* ADMIN *********************************************

@api.route('/repair_complete', methods=['POST'])
def complete_repair():
    """
    Endpoint to mark a repair as completed and add a repair history entry.
    Sends an email with the HTML report.
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


    user_email = repair_order.user.email
    username = repair_order.user.username
    vehicle_model = repair_order.vehicle_model

    # Wczytanie szablonu HTML
    with open('templates/emails/repair_complete.html', 'r') as file:
        html_template = file.read()

    html_body = html_template.replace("{{ username }}", username)\
                             .replace("{{ vehicle_model }}", vehicle_model)\
                             .replace("{{ report }}", report)

    # Wysyłanie e-maila z HTML
    # TODO: In email price, report of services
    msg = Message(
        subject="Repair Completed",
        recipients=[user_email],
        body=f"Hello {username},\nYour repair for {vehicle_model} has been completed.\n\nReport:\n{report}\nThank you!",
        html=html_body
    )
    try:
        mail.send(msg)
        return jsonify({"message": "Repair completed and report sent via email."}), 200
    except Exception as e:
        return jsonify({"message": f"Repair completed, but email sending failed: {str(e)}"}), 500

# ********************************************* ZROBIONE *********************************************

@api.route('/repair_history', methods=['GET'])
@jwt_required()
def get_user_repair_history():
    """
    Endpoint to retrieve the repair history of the authenticated user.
    """
    user = get_user_by_jwt()
    if not user:
        return jsonify({"message": "User not found."}), 404

    history = RepairHistory.query.join(RepairOrder).filter(RepairOrder.user_id == user.user_id).all()
    history_data = [{
        "repair_history_id": h.id,
        "repair_order_id": h.repair_order_id,
        "service_id": h.service_id,
        "report": h.report,
        "completed_at": h.completed_at.isoformat()
    } for h in history]
    return jsonify(history_data)

# ********************************************* ZROBINE *********************************************

@api.route('/ratings', methods=['POST'])
@jwt_required()
def rate_workshop():
    """
    Endpoint to submit a rating for the workshop.
    """
    user = get_user_by_jwt()
    if not user:
        return jsonify({"message": "User not found."}), 404

    data = request.get_json()
    repair_order_id = data.get('repair_order_id')
    rating = data.get('rating')
    comment = data.get('comment')

    new_rating = WorkshopRating(
        user_id=user.user_id,
        repair_order_id=repair_order_id,
        rating=rating,
        comment=comment
    )
    db.session.add(new_rating)
    db.session.commit()

    return jsonify({"message": "Rating submitted successfully."}), 201
    

# ********************************************* ZROBIONE *********************************************

@api.route('/ratings', methods=['GET'])
def get_ratings():
    """
    Endpoint to retrieve all workshop ratings.
    """
    ratings = WorkshopRating.query.all()
    return jsonify([{"id": r.id, "user_id": r.user_id, "repair_order_id": r.repair_order_id, "rating": r.rating, "comment": r.comment} for r in ratings])

# ********************************************* ZROBIONE *********************************************
@api.route('/repair_orders/user', methods=['GET'])
@jwt_required()
def get_repair_orders_by_user():
    """
    Endpoint to retrieve all repair orders for the authenticated user.
    """
    user = get_user_by_jwt()
    if not user:
        return jsonify({"message": "User not found."}), 404

    repair_orders = RepairOrder.query.filter_by(user_id=user.user_id).all()
    if not repair_orders:
        return jsonify({"message": "No orders found for this user."}), 404
    return jsonify([{
        "order_id": ro.id,
        "vehicle_model": ro.vehicle_model,
        "description": ro.description,
        "status": ro.status,
        "appointment_date": ro.appointment_date.isoformat()
    } for ro in repair_orders])
    
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

