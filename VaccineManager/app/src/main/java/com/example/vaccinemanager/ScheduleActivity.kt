package com.example.vaccinemanager

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.Calendar


class ScheduleActivity : AppCompatActivity() {

    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var etVaccineName: EditText
    private lateinit var selectedDate: Calendar
    private lateinit var selectedTime: Calendar
    private lateinit var btnSaveSchedule: Button
    private lateinit var btnEnterAddress: Button
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        initViews()

        btnPickDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnPickTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnEnterAddress.setOnClickListener {
            showAddressEntryDialog()
        }

        btnSaveSchedule.setOnClickListener {
            saveSchedule()
            startActivity(
                Intent(this@ScheduleActivity,
                    HomeActivity::class.java)
            )
        }

    }

    private fun initViews() {
        btnPickDate = findViewById(R.id.btnDatePicker)
        btnPickTime = findViewById(R.id.btnTimePicker)
        etVaccineName = findViewById(R.id.etVaccineName)
        selectedDate = Calendar.getInstance()
        selectedTime = Calendar.getInstance()
        btnSaveSchedule = findViewById(R.id.btnScheduleAppointment)
        btnEnterAddress = findViewById(R.id.btnEnterAddress)
    }

    private fun showDatePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_date_picker)

        val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker)
        val btnSetDate = dialog.findViewById<Button>(R.id.btnSetDate)

        btnSetDate.setOnClickListener {
            val selectedYear = datePicker.year
            val selectedMonth = datePicker.month
            val selectedDay = datePicker.dayOfMonth

            showToast("Selected Date: $selectedDay/${selectedMonth + 1}/$selectedYear")

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showTimePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_time_picker)

        val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)
        val btnSetTime = dialog.findViewById<Button>(R.id.btnSetTime)

        btnSetTime.setOnClickListener {
            val selectedHour = timePicker.currentHour
            val selectedMinute = timePicker.currentMinute

            showToast("Selected Time: $selectedHour:$selectedMinute")

            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showAddressEntryDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_address_entry)

        val etAddress = dialog.findViewById<EditText>(R.id.etAddress)
        val btnSaveAddress = dialog.findViewById<Button>(R.id.btnSaveAddress)

        btnSaveAddress.setOnClickListener {
            val address = etAddress.text

            showToast("Typed Address: $address")

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveSchedule() {
        val email =  FirebaseAuth.getInstance().currentUser?.email.toString()
        val vaccineName = etVaccineName.text.toString().trim()

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

            // val firebaseData = FireStoreData(email, vaccineName, dateTime, address)

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
