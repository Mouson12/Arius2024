package com.example.garage

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * Class for running test cases against the API endpoints.
 * Primarily used for debugging and verifying that API methods function as expected.
 *
 * @param repository An instance of `RepairRepository` to access API operations.
 * @param scope A `LifecycleCoroutineScope` for launching coroutines safely within a lifecycle-aware component.
 */
class ApiTestRunner(
    private val repository: RepairRepository,
    private val scope: LifecycleCoroutineScope
) {

    /**
     * Runs all available API tests in sequence.
     * Each test is responsible for logging its results or any errors encountered.
     */
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

    /**
     * Tests fetching the list of services from the workshop.
     * Logs the result or any errors encountered.
     */
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

    /**
     * Tests creating a new repair order.
     * Logs the result or any errors encountered.
     */
    private fun testCreateRepairOrder() {
        scope.launch {
            try {
                val response = repository.createRepairOrder(
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

    /**
     * Tests fetching all available appointments.
     * Logs the result or any errors encountered.
     */
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

    /**
     * Tests fetching appointments specific to the authenticated user.
     * Logs the result or any errors encountered.
     */
    private fun testGetUserAppointments() {
        scope.launch {
            try {
                val userAppointments = repository.getUserAppointments()
                Log.d("ApiTest", "User Appointments: $userAppointments")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching user appointments: ${e.message()}")
            }
        }
    }

    /**
     * Tests fetching the repair history for the authenticated user.
     * Logs the result or any errors encountered.
     */
    private fun testGetRepairHistory() {
        scope.launch {
            try {
                val history = repository.getUserRepairHistory()
                Log.d("ApiTest", "Repair history: $history")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching repair history: ${e.message()}")
            }
        }
    }

    /**
     * Tests submitting a rating for a workshop.
     * Logs the result or any errors encountered.
     */
    private fun testRateWorkshop() {
        scope.launch {
            try {
                val message = repository.rateWorkshop(
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

    /**
     * Tests fetching all workshop ratings.
     * Logs the result or any errors encountered.
     */
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

    /**
     * Tests fetching all repair orders created by the authenticated user.
     * Logs the result or any errors encountered.
     */
    private fun testGetRepairOrdersByUser() {
        scope.launch {
            try {
                val repairOrders = repository.getRepairOrdersByUser()
                Log.d("ApiTest", "Repair orders by user: $repairOrders")
            } catch (e: HttpException) {
                Log.e("ApiTest", "Error fetching repair orders by user: ${e.message()}")
            }
        }
    }
}