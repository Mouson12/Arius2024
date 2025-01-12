package com.example.garage

/**
 * Represents a service offered by the repair workshop.
 *
 * @param id Unique identifier of the service.
 * @param name Name of the service.
 * @param description Optional description of the service.
 * @param price Cost of the service in double precision.
 */
data class Service(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double
)

/**
 * Represents a repair order submitted by a user.
 *
 * @param vehicle_model Model of the vehicle to be repaired.
 * @param description Detailed description of the repair issue.
 * @param status Current status of the repair order. Default is "Pending".
 * @param appointment_date Scheduled date for the repair appointment in string format.
 */
data class RepairOrder(
    val vehicle_model: String,
    val description: String,
    val status: String = "Pending",
    val appointment_date: String
)

/**
 * Represents the history of a completed repair.
 *
 * @param repair_history_id Unique identifier for the repair history record.
 * @param repair_order_id Identifier for the associated repair order.
 * @param service_id Identifier for the service provided during the repair.
 * @param report Detailed report of the repair work.
 * @param completed_at Timestamp when the repair was completed.
 */
data class RepairHistory(
    val repair_history_id: Int,
    val repair_order_id: Int,
    val service_id: Int,
    val report: String,
    val completed_at: String
)

/**
 * Represents a rating given to the workshop by a user.
 *
 * @param id Unique identifier for the rating.
 * @param repair_order_id Identifier for the associated repair order being rated.
 * @param rating Integer rating value (e.g., 1-5).
 * @param comment Optional textual feedback for the workshop.
 */
data class WorkshopRating(
    val id: Int,
    val repair_order_id: Int,
    val rating: Int,
    val comment: String?
)

/**
 * Represents the request payload to submit a workshop rating.
 *
 * @param repair_order_id Identifier for the associated repair order being rated.
 * @param rating Integer rating value to be submitted (e.g., 1-5).
 * @param comment Optional textual feedback for the workshop.
 */
data class WorkshopRatingRequest(
    val repair_order_id: Int,
    val rating: Int,
    val comment: String?
)

/**
 * Represents an appointment date for repair services.
 *
 * @param appointment_date Scheduled date of the appointment in string format.
 */
data class Appointment(
    val appointment_date: String
)