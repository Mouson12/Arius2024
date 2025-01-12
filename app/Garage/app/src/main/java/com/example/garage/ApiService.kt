package com.example.garage

import retrofit2.Call
import retrofit2.http.*

// Represents the login request data (email and password)
data class LoginRequest(val email: String, val password: String)

// Represents the response for a successful login, containing an access token
data class LoginResponse(val access_token: String)

// Represents the registration request data for a new user
data class RegisterRequest(val username: String, val email: String, val password: String)

// Represents a general response from the server for update actions
data class UpdateResponse(val message: String)

// Interface defining the API methods used by the application
interface ApiService {

    /**
     * Logs in a user with the provided email and password.
     * @param request A `LoginRequest` object containing login credentials.
     * @return A `Call` object that returns a `LoginResponse` containing the access token.
     */
    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    /**
     * Registers a new user with the provided username, email, and password.
     * @param request A `RegisterRequest` object containing the registration details.
     * @return A `Call` object returning a void response.
     */
    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<Void>

    /**
     * Retrieves a list of services available at the workshop.
     * @return A list of `Service` objects representing available services.
     */
    @GET("/api/services")
    suspend fun getServices(): List<Service>

    /**
     * Creates a new repair order.
     * @param token The user's authorization token.
     * @param request A `RepairOrder` object containing details of the order.
     * @return A map containing the server response, such as the order ID.
     */
    @POST("/api/repair_orders")
    suspend fun createRepairOrder(
        @Header("Authorization") token: String,
        @Body request: RepairOrder
    ): Map<String, Any>

    /**
     * Retrieves a list of all available appointments.
     * @return A list of `Appointment` objects representing available time slots.
     */
    @GET("/api/appointments")
    suspend fun getAppointments(): List<Appointment>

    /**
     * Retrieves a list of appointments for the logged-in user.
     * @param token The user's authorization token.
     * @return A list of `Appointment` objects specific to the user.
     */
    @GET("/api/appointments/user")
    suspend fun getUserAppointments(
        @Header("Authorization") token: String
    ): List<Appointment>

    /**
     * Retrieves the repair history for the logged-in user.
     * @param token The user's authorization token.
     * @return A list of `RepairHistory` objects specific to the user.
     */
    @GET("/api/repair_history")
    suspend fun getUserRepairHistory(
        @Header("Authorization") token: String
    ): List<RepairHistory>

    /**
     * Submits a rating for a workshop.
     * @param token The user's authorization token.
     * @param request A `WorkshopRatingRequest` object containing rating details.
     * @return A server response as an `UpdateResponse` object.
     */
    @POST("/api/ratings")
    suspend fun rateWorkshop(
        @Header("Authorization") token: String,
        @Body request: WorkshopRatingRequest
    ): UpdateResponse

    /**
     * Retrieves a list of all ratings for workshops.
     * @return A list of `WorkshopRating` objects.
     */
    @GET("/api/ratings")
    suspend fun getRatings(): List<WorkshopRating>

    /**
     * Retrieves the repair orders created by the logged-in user.
     * @param token The user's authorization token.
     * @return A list of `RepairOrder` objects specific to the user.
     */
    @GET("/api/repair_orders/user")
    suspend fun getRepairOrdersByUser(
        @Header("Authorization") token: String
    ): List<RepairOrder>
}