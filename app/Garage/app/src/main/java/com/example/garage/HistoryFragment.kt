package com.example.garage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

/**
 * Fragment responsible for displaying the repair history of the user.
 * Provides functionality to view repair details and rate a workshop.
 */
class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepairHistoryAdapter

    /**
     * Creates and inflates the view for the fragment, setting up the RecyclerView and adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RepairHistoryAdapter(listOf()) { repairOrderId, workshopName ->
            showRatingPopup(repairOrderId, workshopName)
        }
        recyclerView.adapter = adapter

        // Fetch and display repair history
        fetchRepairHistory()

        return view
    }

    /**
     * Fetches the repair history data using the API and updates the RecyclerView adapter.
     * Logs errors if fetching fails.
     */
    private fun fetchRepairHistory() {
        lifecycleScope.launch {
            val token = fetchToken() // Retrieve authentication token
            if (token != null) {
                val bearerToken = "Bearer $token"
                try {
                    val repairHistory = RetrofitInstance.api.getUserRepairHistory(bearerToken)
                    adapter.updateData(repairHistory)
                } catch (e: Exception) {
                    Log.e("HistoryFragment", "Error fetching repair history: ${e.message}")
                }
            } else {
                Log.e("HistoryFragment", "Token is null, user needs to log in.")
            }
        }
    }

    /**
     * Displays a popup dialog for rating a workshop.
     *
     * @param repairOrderId ID of the repair order being rated.
     * @param workshopName Name of the workshop being rated.
     */
    private fun showRatingPopup(repairOrderId: Int, workshopName: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_rate_workshop, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Set up dialog components
        dialogView.findViewById<TextView>(R.id.tvWorkshopName).text = "Rate Workshop"
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val etComment = dialogView.findViewById<EditText>(R.id.etComment)

        dialogView.findViewById<Button>(R.id.btnSubmitRating).setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = etComment.text.toString()

            if (rating == 0) {
                Toast.makeText(context, "Please provide a rating", Toast.LENGTH_SHORT).show()
            } else {
                submitRating(repairOrderId, rating, comment)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    /**
     * Submits a rating for a repair order to the server.
     *
     * @param repairOrderId ID of the repair order being rated.
     * @param rating Rating value provided by the user.
     * @param comment Optional comment provided by the user.
     */
    private fun submitRating(repairOrderId: Int, rating: Int, comment: String?) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitInstance.api // API service instance
                val token = fetchToken() // Retrieve authentication token

                if (token.isNullOrEmpty()) {
                    Toast.makeText(context, "Authentication token is missing. Please log in.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val repairRepository = RepairRepository(apiService, token) // Create repository instance
                val message = repairRepository.rateWorkshop(repairOrderId, rating, comment)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to submit rating: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Retrieves the authentication token from the local database.
     *
     * @return The authentication token or null if not available.
     */
    private suspend fun fetchToken(): String? {
        return try {
            val database = TokenDatabase.getInstance(requireContext())
            database.tokenDao().getToken()?.token
        } catch (e: Exception) {
            Log.e("HistoryFragment", "Error fetching token: ${e.message}")
            null
        }
    }
}