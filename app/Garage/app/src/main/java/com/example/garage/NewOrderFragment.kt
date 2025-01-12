package com.example.garage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for creating a new repair order.
 * Allows users to:
 * - Select a date using a calendar view.
 * - Enter details about their vehicle and repair description.
 * - Submit the order to the backend.
 *
 * @param repository An instance of `RepairRepository` for interacting with the backend API.
 */
class NewOrderFragment(private val repository: RepairRepository) : Fragment() {

    // UI elements
    private lateinit var calendarView: com.applandeo.materialcalendarview.CalendarView
    private val appointmentCountByDate = mutableMapOf<String, Int>()
    private var selectedCalendarDay: CalendarDay? = null

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for the fragment
        val view = inflater.inflate(R.layout.fragment_new_order, container, false)

        setupUIToHideKeyboard(view)

        // Initialize UI components
        val vehicleModelEditText = view.findViewById<EditText>(R.id.editTextVehicleModel)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.buttonSubmit)
        calendarView = view.findViewById(R.id.calendarView)
        val selectedDateTextView = view.findViewById<TextView>(R.id.textViewSelectedDate)

        var selectedDate: String? = null

        // Fetch and populate existing appointments
        lifecycleScope.launch {
            try {
                val appointments = repository.getAppointments()
                populateAppointments(appointments)
            } catch (e: Exception) {
                Log.e("Appointments Error", "Error fetching appointments: ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Error fetching appointments.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Calendar day selection handling
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val clickedDayCalendar = calendarDay.calendar
                selectedDate = String.format(
                    "%04d-%02d-%02d",
                    clickedDayCalendar.get(Calendar.YEAR),
                    clickedDayCalendar.get(Calendar.MONTH) + 1,
                    clickedDayCalendar.get(Calendar.DAY_OF_MONTH)
                )

                val count = appointmentCountByDate[selectedDate] ?: 0
                if (count >= 10) {
                    selectedDate = null
                    showMaxAppointmentsDialog()
                } else {
                    selectedCalendarDay = calendarDay
                    updateCalendarSelection()
                    selectedDateTextView.text = "Selected Date: $selectedDate"
                }
            }
        })

        // Handle order submission
        submitButton.setOnClickListener {
            val vehicleModel = vehicleModelEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (vehicleModel.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter the vehicle model!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a description of the issue!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate == null) {
                Toast.makeText(requireContext(), "Please select a date for the appointment!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val appointmentDate = "${selectedDate}T10:00:00"
                    val message = repository.createRepairOrder(vehicleModel, description, appointmentDate)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("Create Order", "Error creating order: ${e.message}")
                    Toast.makeText(requireContext(), "Error creating order!", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    /**
     * Updates the calendar to reflect selected dates and appointment availability.
     */
    private fun updateCalendarSelection() {
        val events = mutableListOf<EventDay>()

        // Highlight selected day
        selectedCalendarDay?.let {
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(android.graphics.Color.parseColor("#6200EE")) // Highlight color
                setSize(64, 64)
            }
            events.add(EventDay(it.calendar, drawable))
        }

        // Add existing appointments
        for ((date, count) in appointmentCountByDate) {
            val (year, month, day) = date.split("-").map { it.toInt() }
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)

            if (selectedCalendarDay?.calendar != calendar) {
                val color = getColorForAppointments(count)
                val drawable = createCircleDrawable(color)
                events.add(EventDay(calendar, drawable))
            }
        }

        calendarView.setEvents(events)
    }

    /**
     * Populates the calendar with existing appointments.
     *
     * @param appointments List of appointments fetched from the backend.
     */
    private fun populateAppointments(appointments: List<Appointment>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        for (appointment in appointments) {
            val date = dateFormat.format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    .parse(appointment.appointment_date)!!
            )
            val count = appointmentCountByDate[date] ?: 0
            appointmentCountByDate[date] = count + 1
        }
        updateCalendarColors()
    }

    /**
     * Updates calendar colors based on the number of appointments on each date.
     */
    private fun updateCalendarColors() {
        val events = mutableListOf<EventDay>()
        for ((date, count) in appointmentCountByDate) {
            val (year, month, day) = date.split("-").map { it.toInt() }
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)
            val color = getColorForAppointments(count)
            val drawable = createCircleDrawable(color)
            events.add(EventDay(calendar, drawable))
        }
        calendarView.setEvents(events)
    }

    /**
     * Creates a circular drawable with the given color.
     *
     * @param color The color to apply to the drawable.
     * @return A `GradientDrawable` with the specified color.
     */
    private fun createCircleDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setSize(48, 48)
        }
    }

    /**
     * Determines the color intensity based on the number of appointments.
     *
     * @param count Number of appointments.
     * @return Color integer.
     */
    private fun getColorForAppointments(count: Int): Int {
        val colorIntensity = (255 * count / 10).coerceIn(0, 255)
        return android.graphics.Color.argb(255, 255, 255 - colorIntensity, 255 - colorIntensity)
    }

    /**
     * Shows a dialog informing the user when the maximum number of appointments for a day is reached.
     */
    private fun showMaxAppointmentsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("No Available Slots")
            .setMessage("The selected day already has 10 appointments. Please choose another day.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /**
     * Sets up UI interactions to hide the keyboard when tapping outside editable views.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupUIToHideKeyboard(view: View) {
        if (view !is EditText && view !is Button) {
            view.setOnTouchListener { _, _ ->
                requireActivity().hideKeyboard()
                view.clearFocus()
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUIToHideKeyboard(innerView)
            }
        }
    }
}