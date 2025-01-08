package com.example.garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class NewOrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_order, container, false)

        val vehicleModelEditText = view.findViewById<EditText>(R.id.editTextVehicleModel)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.buttonSubmit)

        submitButton.setOnClickListener {
            val vehicleModel = vehicleModelEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (vehicleModel.isNotEmpty() && description.isNotEmpty()) {
                Toast.makeText(requireContext(), "Zlecenie dodane!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
