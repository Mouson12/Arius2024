package com.example.garage

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

//TODO: Only for test
class ApiTestRunner(private val repository: RepairRepository, private val scope: LifecycleCoroutineScope) {

    fun runAllTests() {
        testGetServices()
        testCreateRepairOrder()
        testGetAppointments()
        testGetUserAppointments()
        testGetRepairHistory()
        testRateWorkshop()
        testGetRatings()
        testGetRepairOrdersByUser()
    }

    private fun testGetServices() {
        scope.launch {
            try {
                val services = repository.getServices()
                Log.d("ApiTest", "Available services: $services")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching services: ${e.message()}")
            }
        }
    }

    private fun testCreateRepairOrder() {
        scope.launch {
            try {
                val response = repository.createRepairOrder(
                    userId = 1,
                    vehicleModel = "Toyota Corolla",
                    description = "Brakes issue",
                    appointmentDate = "2025-01-15T10:00:00"
                )
                Log.d("ApiTest", "Repair order created: $response")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error creating repair order: ${e.message()}")
            }
        }
    }

    private fun testGetAppointments() {
        scope.launch {
            try {
                val appointments = repository.getAppointments()
                Log.d("ApiTest", "Appointments: $appointments")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching appointments: ${e.message()}")
            }
        }
    }

    private fun testGetUserAppointments() {
        scope.launch {
            try {
                val userAppointments = repository.getUserAppointments(1)
                Log.d("ApiTest", "User Appointments: $userAppointments")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching user appointments: ${e.message()}")
            }
        }
    }

    private fun testGetRepairHistory() {
        scope.launch {
            try {
                val history = repository.getUserRepairHistory(1)
                Log.d("ApiTest", "Repair history: $history")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching repair history: ${e.message()}")
            }
        }
    }

    private fun testRateWorkshop() {
        scope.launch {
            try {
                val message = repository.rateWorkshop(
                    userId = 1,
                    repairOrderId = 39,
                    rating = 5,
                    comment = "Excellent service!"
                )
                Log.d("ApiTest", "Rating submitted: $message")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error submitting rating: ${e.message()}")
            }
        }
    }

    private fun testGetRatings() {
        scope.launch {
            try {
                val ratings = repository.getRatings()
                Log.d("ApiTest", "Workshop ratings: $ratings")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching ratings: ${e.message()}")
            }
        }
    }

    private fun testGetRepairOrdersByUser() {
        scope.launch {
            try {
                val repairOrders = repository.getRepairOrdersByUser(1)
                Log.d("ApiTest", "Repair orders by user: $repairOrders")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching repair orders by user: ${e.message()}")
            }
        }
    }
}
