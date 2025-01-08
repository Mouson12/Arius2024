package com.example.garage

import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val access_token: String)
data class RegisterRequest(val username: String, val email: String, val password: String)
data class Service(val id: Int, val name: String, val description: String, val price: Double)
data class RepairOrder(val user_id: Int, val vehicle_model: String, val description: String, val appointment_date: String)

interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<Void>

    @GET("/api/services")
    fun getServices(): Call<List<Service>>

    @POST("/api/repair_orders")
    fun createRepairOrder(@Body request: RepairOrder): Call<Void>
}
