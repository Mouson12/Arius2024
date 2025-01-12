package com.example.garage

/**
 * Repository for managing repair-related data and operations.
 * Handles API interactions, providing a layer of abstraction for accessing repair services,
 * repair orders, appointments, and user-specific repair data.
 *
 * @param api The `ApiService` instance for making network requests.
 * @param token The user's authentication token used for API authorization.
 */
class RepairRepository(private val api: ApiService, private val token: String) {

    /**
     * Retrieves a list of available services from the workshop.
     *
     * @return A list of `Service` objects.
     */
    suspend fun getServices(): List<Service> = api.getServices()

    /**
     * Creates a new repair order.
     *
     * @param vehicleModel The model of the vehicle requiring repair.
     * @param description A detailed description of the repair issue.
     * @param appointmentDate The scheduled date for the repair appointment in ISO 8601 format.
     * @return A success message returned from the API.
     */
    suspend fun createRepairOrder(
        vehicleModel: String,
        description: String,
        appointmentDate: String
    ): String {
        // Create a repair order request
        val request = RepairOrder(
            vehicle_model = vehicleModel,
            description = description,
            appointment_date = appointmentDate
        )

        // Send the request to the API
        val response = api.createRepairOrder("Bearer $token", request)

        // Return the message from the server response
        return response["message"] as String
    }

    /**
     * Fetches all booked appointments.
     *
     * @return A list of `Appointment` objects representing all appointments.
     */
    suspend fun getAppointments(): List<Appointment> = api.getAppointments()

    /**
     * Fetches appointments specific to the authenticated user.
     *
     * @return A list of `Appointment` objects belonging to the user.
     */
    suspend fun getUserAppointments(): List<Appointment> = api.getUserAppointments("Bearer $token")

    /**
     * Fetches the repair history for the authenticated user.
     *
     * @return A list of `RepairHistory` objects representing the user's repair history.
     */
    suspend fun getUserRepairHistory(): List<RepairHistory> = api.getUserRepairHistory("Bearer $token")

    /**
     * Submits a workshop rating for a specific repair order.
     *
     * @param repairOrderId The ID of the repair order being rated.
     * @param rating The rating value (e.g., 1-5).
     * @param comment An optional comment providing feedback.
     * @return A success message returned from the API.
     */
    suspend fun rateWorkshop(repairOrderId: Int, rating: Int, comment: String?): String {
        // Create a workshop rating request
        val request = WorkshopRatingRequest(
            repair_order_id = repairOrderId,
            rating = rating,
            comment = comment
        )

        // Send the request to the API
        return api.rateWorkshop("Bearer $token", request).message
    }

    /**
     * Retrieves all ratings for the workshop.
     *
     * @return A list of `WorkshopRating` objects representing all ratings.
     */
    suspend fun getRatings(): List<WorkshopRating> = api.getRatings()

    /**
     * Retrieves all repair orders created by the authenticated user.
     *
     * @return A list of `RepairOrder` objects belonging to the user.
     */
    suspend fun getRepairOrdersByUser(): List<RepairOrder> = api.getRepairOrdersByUser("Bearer $token")
}