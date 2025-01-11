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

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepairHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RepairHistoryAdapter(listOf()) { repairOrderId, workshopName ->
            showRatingPopup(repairOrderId, workshopName)
        }
        recyclerView.adapter = adapter

        fetchRepairHistory()

        return view
    }

    private fun fetchRepairHistory() {
        lifecycleScope.launch {
            val token = fetchToken() // Pobierz token
            if (token != null) {
                val bearerToken = "Bearer $token"
                try {
                    val repairHistory = RetrofitInstance.api.getUserRepairHistory(bearerToken)
                    adapter.updateData(repairHistory)
                } catch (e: Exception) {
                    Log.e("HistoryFragment", "Błąd podczas pobierania historii: ${e.message}")
                }
            } else {
                Log.e("HistoryFragment", "Token jest pusty, użytkownik musi się zalogować.")
            }
        }
    }

    private fun showRatingPopup(repairOrderId: Int, workshopName: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_rate_workshop, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.tvWorkshopName).text = "Oceń usługę"
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

    private fun submitRating(repairOrderId: Int, rating: Int, comment: String?) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitInstance.api // Pobierz instancję ApiService
                val token = fetchToken() // Pobierz token (upewnij się, że metoda fetchToken działa poprawnie)

                if (token.isNullOrEmpty()) {
                    Toast.makeText(context, "Authentication token is missing. Please log in.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val repairRepository = RepairRepository(apiService, token) // Przekaż token do repozytorium
                val message = repairRepository.rateWorkshop(repairOrderId, rating, comment)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to submit rating: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchToken(): String? {
        return try {
            val database = TokenDatabase.getInstance(requireContext())
            database.tokenDao().getToken()?.token
        } catch (e: Exception) {
            Log.e("HistoryFragment", "Błąd podczas pobierania tokena: ${e.message}")
            null
        }
    }


}