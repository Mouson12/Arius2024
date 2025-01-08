package com.example.garage

import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val access_token: String)
data class RegisterRequest(val username: String, val email: String, val password: String)
data class UpdateResponse(val message: String)
interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<Void>

    @GET("/api/services")
    suspend fun getServices(): List<Service>

    @POST("/api/repair_orders")
    suspend fun createRepairOrder(
        @Header("Authorization") token: String,
        @Body request: RepairOrder
    ): Map<String, Any>

    @GET("/api/appointments")
    suspend fun getAppointments(): List<Appointment>

    @GET("/api/appointments/user")
    suspend fun getUserAppointments(
        @Header("Authorization") token: String
    ): List<Appointment>

    @GET("/api/repair_history")
    suspend fun getUserRepairHistory(
        @Header("Authorization") token: String
    ): List<RepairHistory>

    @POST("/api/ratings")
    suspend fun rateWorkshop(
        @Header("Authorization") token: String,
        @Body request: WorkshopRatingRequest
    ): UpdateResponse

    @GET("/api/ratings")
    suspend fun getRatings(): List<WorkshopRating>

    @GET("/api/repair_orders/user")
    suspend fun getRepairOrdersByUser(
        @Header("Authorization") token: String
    ): List<RepairOrder>
}

