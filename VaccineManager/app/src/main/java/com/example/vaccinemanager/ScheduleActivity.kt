package com.example.vaccinemanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.type.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var etVaccineName: EditText
    private lateinit var selectedDate: Calendar
    private lateinit var selectedTime: Calendar
    private lateinit var btnSaveSchedule: Button
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        initViews()

        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        btnPickTime.setOnClickListener {
            showTimePicker()
        }

        btnSaveSchedule.setOnClickListener {
            saveSchedule()
        }

    }

    private fun initViews() {
        btnPickDate = findViewById(R.id.btnDatePicker)
        btnPickTime = findViewById(R.id.btnTimePicker)
        etVaccineName = findViewById(R.id.etVaccineName)
        selectedDate = Calendar.getInstance()
        selectedTime = Calendar.getInstance()
        btnSaveSchedule = findViewById(R.id.btnScheduleAppointment)
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                updateDateButtonText()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                updateTimeButtonText()
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun updateDateButtonText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        btnPickDate.text = dateFormat.format(selectedDate.time)
    }

    private fun updateTimeButtonText() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        btnPickTime.text = timeFormat.format(selectedTime.time)
    }

    private fun saveSchedule() {
        val vaccineName = etVaccineName.text.toString().trim()
        val date = selectedDate.time
        val time = selectedTime.time

        if (vaccineName.isNotEmpty()) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
            }
            val timestamp = calendar.time
            
            val appointmentDetails = hashMapOf(
                "vaccineName" to vaccineName,
                "timestamp" to timestamp,
            )

            db.collection("Appointments")
                .add(appointmentDetails)
                .addOnSuccessListener { documentReference ->
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Error adding document: $e")
                }
        } else {
            showToast("Please fill in all the details.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
