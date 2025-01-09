package com.example.garage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        adapter = RepairHistoryAdapter(listOf())
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