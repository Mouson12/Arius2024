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

class NewOrderFragment(private val repository: RepairRepository) : Fragment() {

    private lateinit var calendarView: com.applandeo.materialcalendarview.CalendarView
    private val appointmentCountByDate = mutableMapOf<String, Int>()

    private var selectedCalendarDay: CalendarDay? = null


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_order, container, false)

        setupUIToHideKeyboard(view)

        val vehicleModelEditText = view.findViewById<EditText>(R.id.editTextVehicleModel)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.buttonSubmit)
        calendarView = view.findViewById(R.id.calendarView)
        val selectedDateTextView = view.findViewById<TextView>(R.id.textViewSelectedDate)

        var selectedDate: String? = null

        // Pobranie terminów podczas tworzenia widoku
        lifecycleScope.launch() {
            try {
                val appointments = repository.getAppointments()
                populateAppointments(appointments)
            } catch (e: Exception) {
                Log.e("Appointments Error", "Error fetching appointments: ${e.message}")
                Toast.makeText(requireContext(), "Błąd podczas pobierania wizyt", Toast.LENGTH_SHORT).show()
            }
        }

//        calendarView.setHeaderColor(resources.getColor(android.R.color.white))
//        calendarView.setHeaderLabelColor(resources.getColor(android.R.color.black))
//        calendarView.setForwardButtonImage(resources.getDrawable(1))


        // Obsługa wyboru daty w kalendarzu
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            @SuppressLint("DefaultLocale")
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
                    // Ustawienie zaznaczenia na kliknięty dzień
                    selectedCalendarDay = calendarDay
                    updateCalendarSelection()
                    selectedDateTextView.text = "Wybrana data: $selectedDate"
                }
            }
        })



        // Obsługa wysłania formularza
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

            lifecycleScope.launch() {
                try {
                    val appointmentDate = "${selectedDate}T10:00:00"
                    val message = repository.createRepairOrder(vehicleModel, description, appointmentDate)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("Create Order", "Error creating order: ${e.message}")
                    Toast.makeText(requireContext(), "Błąd podczas tworzenia zamówienia!", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    private fun updateCalendarSelection() {
        val events = mutableListOf<EventDay>()

        // Dodanie zaznaczonego dnia z wyróżnieniem
        selectedCalendarDay?.let {
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(android.graphics.Color.parseColor("#6200EE")) // Kolor zaznaczenia
                setSize(64, 64) // Rozmiar kółka dla wybranego dnia
            }
            events.add(EventDay(it.calendar, drawable))
        }

        // Dodanie istniejących wydarzeń z ich kolorami
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


    // Funkcja do wypełnienia danych o zajętych terminach
    private fun populateAppointments(appointments: List<Appointment>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        for (appointment in appointments) {
            val date = dateFormat.format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(appointment.appointment_date)!!)
            val count = if (appointmentCountByDate.containsKey(date)) appointmentCountByDate[date]!! else 0
            appointmentCountByDate[date] = count + 1
        }
        updateCalendarColors()
    }

    // Zaznaczenie kolorów na kalendarzu
    private fun updateCalendarColors() {
        val events = mutableListOf<EventDay>()
        for ((date, count) in appointmentCountByDate) {
            val (year, month, day) = date.split("-").map { it.toInt() }
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)

            // Pobranie koloru na podstawie liczby wizyt
            val color = getColorForAppointments(count)

            // Tworzenie okrągłego Drawable z dynamicznym kolorem
            val drawable = createCircleDrawable(color)

            events.add(EventDay(calendar, drawable))
        }
        calendarView.setEvents(events)
    }

    // Funkcja do tworzenia okrągłego Drawable z podanym kolorem
    private fun createCircleDrawable(color: Int): GradientDrawable {
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setSize(48, 48) // Rozmiar okręgu (można dopasować)
        }
        return shape
    }


    private fun getColorForAppointments(count: Int): Int {
        val colorIntensity = (255 * count / 10).coerceIn(0, 255)
        return android.graphics.Color.argb(255, 255, 255 - colorIntensity, 255 - colorIntensity)
    }

    private fun showMaxAppointmentsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Brak dostępnych miejsc")
            .setMessage("W tym dniu jest już zaplanowanych 10 wizyt. Wybierz inny dzień.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

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