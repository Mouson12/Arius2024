package com.example.garage

class RepairRepository(private val api: ApiService, private val token: String) {

    // Pobranie listy dostępnych usług warsztatu
    suspend fun getServices(): List<Service> = api.getServices()

    // Tworzenie zamówienia naprawy
    suspend fun createRepairOrder(
        vehicleModel: String,
        description: String,
        appointmentDate: String
    ): String {
        val request = RepairOrder(
            vehicle_model = vehicleModel,
            description = description,
            appointment_date = appointmentDate
        )
        val response = api.createRepairOrder("Bearer $token", request)
        return response["message"] as String
    }

    // Pobranie zajętych terminów
    suspend fun getAppointments(): List<Appointment> = api.getAppointments()

    // Pobranie zajętych terminów dla użytkownika
    suspend fun getUserAppointments(): List<Appointment> = api.getUserAppointments("Bearer $token")

    // Pobranie historii napraw użytkownika
    suspend fun getUserRepairHistory(): List<RepairHistory> = api.getUserRepairHistory("Bearer $token")

    // Dodanie oceny warsztatu
    suspend fun rateWorkshop(repairOrderId: Int, rating: Int, comment: String?): String {
        val request = WorkshopRatingRequest(repair_order_id = repairOrderId, rating = rating, comment = comment)
        return api.rateWorkshop("Bearer $token", request).message
    }

    // Pobranie wszystkich ocen warsztatu
    suspend fun getRatings(): List<WorkshopRating> = api.getRatings()

    // Pobranie wszystkich zamówień użytkownika
    suspend fun getRepairOrdersByUser(): List<RepairOrder> = api.getRepairOrdersByUser("Bearer $token")
}
