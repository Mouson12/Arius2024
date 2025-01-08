package com.example.garage

class RepairRepository(private val api: ApiService) {

    // Pobranie listy dostępnych usług warsztatu
    suspend fun getServices(): List<Service> = api.getServices()

    // Tworzenie zamówienia naprawy
    suspend fun createRepairOrder(
        userId: Int,
        vehicleModel: String,
        description: String,
        appointmentDate: String
    ): String {
        val request = RepairOrder(
            user_id = userId,
            vehicle_model = vehicleModel,
            description = description,
            appointment_date = appointmentDate
        )
        val response = api.createRepairOrder(request)
        return response["message"] as String
    }

    // Pobranie zajętych terminów
    suspend fun getAppointments(): List<Appointment> = api.getAppointments()

    // Pobranie zajętych terminów dla użytkownika
    suspend fun getUserAppointments(userId: Int): List<Appointment> = api.getUserAppointments(userId)

    // Pobranie historii napraw użytkownika
    suspend fun getUserRepairHistory(userId: Int): List<RepairHistory> = api.getUserRepairHistory(userId)

    // Dodanie oceny warsztatu
    suspend fun rateWorkshop(userId: Int, repairOrderId: Int, rating: Int, comment: String?): String {
        val request = WorkshopRatingRequest(userId, repairOrderId, rating, comment)
        return api.rateWorkshop(request).message
    }

    // Pobranie wszystkich ocen warsztatu
    suspend fun getRatings(): List<WorkshopRating> = api.getRatings()

    // Pobranie wszystkich zamówień użytkownika
    suspend fun getRepairOrdersByUser(userId: Int): List<RepairOrder> = api.getRepairOrdersByUser(userId)
}
