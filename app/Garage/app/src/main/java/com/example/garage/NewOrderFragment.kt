package com.example.garage

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewOrderFragment(private val repository: RepairRepository) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_order, container, false)

        setupUIToHideKeyboard(view)

        val vehicleModelEditText = view.findViewById<EditText>(R.id.editTextVehicleModel)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val dateButton = view.findViewById<Button>(R.id.buttonSelectDate)
        val submitButton = view.findViewById<Button>(R.id.buttonSubmit)
        val selectedDateTextView = view.findViewById<TextView>(R.id.textViewSelectedDate)

        var selectedDate: String? = null

        // Date Picker
        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val date = Calendar.getInstance()
                    date.set(year, month, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    selectedDate = format.format(date.time)
                    selectedDateTextView.text = "Wybrana data: $selectedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        submitButton.setOnClickListener {
            val vehicleModel = vehicleModelEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (vehicleModel.isEmpty()) {
                Toast.makeText(requireContext(), "Wprowadź model pojazdu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Wprowadź opis usterki!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate == null) {
                Toast.makeText(requireContext(), "Wybierz datę wizyty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send the repair order using repository
            lifecycleScope.launch() {
                try {
                    selectedDate = "${selectedDate}T10:00:00"
                    val message = repository.createRepairOrder(vehicleModel, description, selectedDate!!)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("Create New order", "Error creating new order: ${e.message}")
                    Toast.makeText(requireContext(), "Błąd podczas tworzenia zamówienia!", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUIToHideKeyboard(view: View) {
        // Jeśli kliknięto poza EditText, schowaj klawiaturę
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                requireActivity().hideKeyboard()  // Ukryj klawiaturę
                view.clearFocus()  // Usuń focus z EditText
                true
            }
        }

        // Dla wszystkich dzieci widoku
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUIToHideKeyboard(innerView)
            }
        }
    }
}
