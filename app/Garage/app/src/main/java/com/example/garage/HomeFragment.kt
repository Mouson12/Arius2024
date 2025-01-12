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

/**
 * Fragment responsible for displaying the main home screen of the app.
 * It includes a list of available services and a logout button.
 */
class HomeFragment : Fragment() {

    // API service instance for making HTTP requests
    private lateinit var apiService: ApiService

    // RecyclerView to display a list of services
    private lateinit var servicesRecyclerView: RecyclerView

    // Button for logging out of the app
    private lateinit var logoutButton: Button

    // Adapter for managing and displaying services data
    private val servicesAdapter = ServicesAdapter()

    /**
     * Initializes the view for the HomeFragment.
     * Sets up RecyclerView, Logout Button, and API service configuration.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        // Setup Retrofit API service
        apiService = Retrofit.Builder()
            .baseUrl("http://157.90.162.7:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Load services data from the API
        loadServices()

        return rootView
    }

    /**
     * Fetches the list of services from the API and updates the RecyclerView adapter.
     * Displays an error message if the fetch fails.
     */
    private fun loadServices() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Make API call to fetch services
                val services = apiService.getServices()
                servicesAdapter.submitList(services)
            } catch (e: Exception) {
                // Show error message on failure
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Logs out the user by clearing the session data and navigating back to the login screen.
     */
    private fun performLogout() {
        // Clear SharedPreferences to remove saved user data
        val sharedPreferences =
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Navigate to LoginActivity with clear task flags
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

/**
 * Adapter for managing and displaying the list of services in the RecyclerView.
 */
class ServicesAdapter : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    // List of services to be displayed
    private val services = mutableListOf<Service>()

    /**
     * Updates the list of services and notifies the RecyclerView to refresh.
     *
     * @param newServices List of new services to display.
     */
    fun submitList(newServices: List<Service>) {
        services.clear()
        services.addAll(newServices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        // Inflate the layout for a single service item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        // Bind the service data to the ViewHolder
        holder.bind(services[position])
    }

    override fun getItemCount(): Int = services.size

    /**
     * ViewHolder class for binding service data to the item view.
     */
    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // TextView for displaying the service name
        private val serviceNameTextView: TextView = itemView.findViewById(R.id.serviceNameTextView)

        // TextView for displaying the service price
        private val servicePriceTextView: TextView = itemView.findViewById(R.id.servicePriceTextView)

        /**
         * Binds the service data to the views.
         *
         * @param service The service object containing name and price.
         */
        fun bind(service: Service) {
            serviceNameTextView.text = service.name
            servicePriceTextView.text = "${service.price} zł"
        }
    }
}