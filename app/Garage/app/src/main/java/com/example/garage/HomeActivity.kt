package com.example.garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class HomeActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var usernameTextView: TextView
    private lateinit var servicesRecyclerView: RecyclerView
    private val servicesAdapter = ServicesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        usernameTextView = findViewById(R.id.usernameTextView)
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView)

        servicesRecyclerView.layoutManager = LinearLayoutManager(this)
        servicesRecyclerView.adapter = servicesAdapter

        apiService = Retrofit.Builder()
            .baseUrl("http://157.90.162.7:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val username = intent.getStringExtra("USERNAME") ?: "Użytkownik"
        usernameTextView.text = "Witam, $username"

        loadServices()
    }

    private fun loadServices() {
        lifecycleScope.launch {
            try {
                val services = apiService.getServices()
                servicesAdapter.submitList(services)
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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

@Composable
fun ServiceItem(service: Service) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = service.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${service.price} zł",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}




