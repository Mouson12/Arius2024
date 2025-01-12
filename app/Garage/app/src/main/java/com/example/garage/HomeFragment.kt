package com.example.garage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var logoutButton: Button
    private val servicesAdapter = ServicesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_home, container, false)

        // Initialize RecyclerView
        servicesRecyclerView = rootView.findViewById(R.id.servicesRecyclerView)
        servicesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        servicesRecyclerView.adapter = servicesAdapter

        // Initialize Logout Button
        logoutButton = rootView.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            performLogout()
        }

        // Setup API service
        apiService = Retrofit.Builder()
            .baseUrl("http://157.90.162.7:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        loadServices()

        return rootView
    }

    private fun loadServices() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val services = apiService.getServices()
                servicesAdapter.submitList(services)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogout() {
        // Clear SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Navigate to LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

class ServicesAdapter : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    private val services = mutableListOf<Service>()

    fun submitList(newServices: List<Service>) {
        services.clear()
        services.addAll(newServices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int = services.size

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceNameTextView: TextView = itemView.findViewById(R.id.serviceNameTextView)
        private val servicePriceTextView: TextView = itemView.findViewById(R.id.servicePriceTextView)

        fun bind(service: Service) {
            serviceNameTextView.text = service.name
            servicePriceTextView.text = "${service.price} zł"
        }
    }
}