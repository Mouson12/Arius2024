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

    // Pobranie listy usług warsztatu
    @GET("/api/services")
    suspend fun getServices(): List<Service>

    // Tworzenie zamówienia naprawy
    @POST("/api/repair_orders")
    suspend fun createRepairOrder(@Body request: RepairOrder): Map<String, Any>

    // Pobranie zajętych terminów
    @GET("/api/appointments")
    suspend fun getAppointments(): List<Appointment>

    // Pobranie terminów dla konkretnego użytkownika
    @GET("/api/appointments/user/{user_id}")
    suspend fun getUserAppointments(@Path("user_id") userId: Int): List<Appointment>


    // Pobranie historii napraw użytkownika
    @GET("/api/repair_history/{user_id}")
    suspend fun getUserRepairHistory(@Path("user_id") userId: Int): List<RepairHistory>

    // Dodanie oceny warsztatu
    @POST("/api/ratings")
    suspend fun rateWorkshop(@Body request: WorkshopRatingRequest): UpdateResponse

    // Pobranie wszystkich ocen warsztatu
    @GET("/api/ratings")
    suspend fun getRatings(): List<WorkshopRating>

    // Pobranie wszystkich zamówień użytkownika
    @GET("/api/repair_orders/user/{user_id}")
    suspend fun getRepairOrdersByUser(@Path("user_id") userId: Int): List<RepairOrder>

}
