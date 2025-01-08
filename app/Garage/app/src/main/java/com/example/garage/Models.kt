package com.example.garage

// Model serwisu naprawczego (Service)
data class Service(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double
)

// Model zamówienia naprawy (RepairOrder)
data class RepairOrder(
    val vehicle_model: String,
    val description: String,
    val status: String = "Pending",
    val appointment_date: String
)

// Model historii napraw (RepairHistory)
data class RepairHistory(
    val repair_history_id: Int,
    val repair_order_id: Int,
    val service_id: Int,
    val report: String,
    val completed_at: String
)

// Model oceny warsztatu (WorkshopRating)
data class WorkshopRating(
    val id: Int,
    val repair_order_id: Int,
    val rating: Int,
    val comment: String?
)

// Model zapytania o ocenę warsztatu
data class WorkshopRatingRequest(
    val repair_order_id: Int,
    val rating: Int,
    val comment: String?
)

// Model terminu naprawy (Appointment)
data class Appointment(
    val appointment_date: String
)