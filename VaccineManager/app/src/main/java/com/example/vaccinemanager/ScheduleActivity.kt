package com.example.vaccinemanager

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinemanager.firestore.FireStoreData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

/**
 * ScheduleActivity allows users to schedule vaccination appointments.
 * Users can select the vaccine name, date, time, and enter the address
 * of an appointment.
 *
 * The activity consists of various dialogs for selecting vaccine name,
 * date, and time, as well as entering the address.
 * Upon saving the appointment, the details are stored in Firebase Firestore.
 *
 * @property btnVaccineName Button to set the vaccine name.
 * @property btnPickDate Button to pick the appointment date.
 * @property btnPickTime Button to pick the appointment time.
 * @property btnEnterAddress Button to enter the appointment address.
 * @property btnSaveSchedule Button to save the appointment details.
 * @property selectedDate Calendar instance to hold the selected date.
 * @property selectedTime Calendar instance to hold the selected time.
 * @property dateString String to hold the selected date in string format.
 * @property timeString String to hold the selected time in string format.
 * @property vaccineString String to hold the selected vaccine name.
 * @property enteredAddress String to hold the entered address.

 */

class ScheduleActivity : AppCompatActivity() {

    // Buttons for UI interaction
    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var btnSaveSchedule: Button
    private lateinit var btnEnterAddress: Button
    private lateinit var btnVaccineName: Button

    // Calendar instances for date and time selection
    private lateinit var selectedDate: Calendar
    private lateinit var selectedTime: Calendar

    // Strings to hold appointment details
    private var vaccineString: String = ""
    private var dateString: String = ""
    private var timeString: String = ""
    private var enteredAddress: String = ""

    /**
     * Initializes the UI components and sets up onClick listeners for buttons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        initViews()

        btnVaccineName.setOnClickListener {
            showVaccineNameDialog()
        }

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
        }
    }

    /**
     * Initializes the UI components by finding their respective views.
     */
    private fun initViews() {
        btnPickDate = findViewById(R.id.btnDatePicker)
        btnPickTime = findViewById(R.id.btnTimePicker)
        selectedDate = Calendar.getInstance()
        selectedTime = Calendar.getInstance()
        btnSaveSchedule = findViewById(R.id.btnScheduleAppointment)
        btnEnterAddress = findViewById(R.id.btnEnterAddress)
        btnVaccineName = findViewById(R.id.btnSetVaccineName)
    }

    /**
     * Shows a dialog for selecting the vaccine name.
     * Allows users to choose from a predefined list of vaccines or enter a custom name.
     */
    private fun showVaccineNameDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_vaccine_selection)

        val spinnerVaccineList = dialog.findViewById<Spinner>(R.id.spinnerVaccineList)
        val etCustomVaccineName = dialog.findViewById<EditText>(R.id.etCustomVaccineName)
        val btnSetVaccine = dialog.findViewById<Button>(R.id.btnSetVaccine)

        val vaccineList = arrayOf(
            "COVID-19 mRNA",
            "COVID-19 protein subunit",
            "BCG - Tuberculosis",
            "Hepatitis B",
            "Poliovirus",
            "DTP - Diphtheria",
            "DTP - Tetanus",
            "DTP - Pertussis",
            "HIB",
            "Pneumococcal",
            "Rotavirus",
            "MMR - Measles",
            "MMR - Mumps",
            "MMR - Rubella",
            "HPV",
            "Other"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccineList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVaccineList.adapter = adapter

        spinnerVaccineList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == vaccineList.size - 1) {
                    etCustomVaccineName.visibility = View.VISIBLE
                } else {
                    etCustomVaccineName.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnSetVaccine.setOnClickListener {
            val selectedVaccine: String = if (spinnerVaccineList.selectedItemPosition == vaccineList.size - 1) {
                etCustomVaccineName.text.toString().trim()
            } else {
                spinnerVaccineList.selectedItem.toString()
            }
            showToast("Selected Vaccine: $selectedVaccine")
            dialog.dismiss()
            vaccineString = selectedVaccine
        }

        dialog.show()
    }

    /**
     * Shows a dialog for selecting the appointment date.
     * Allows users to pick a date from the date picker.
     */
    private fun showDatePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_date_picker)

        val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker)
        val btnSetDate = dialog.findViewById<Button>(R.id.btnSetDate)

        btnSetDate.setOnClickListener {
            val selectedYear = datePicker.year
            val selectedMonth = datePicker.month
            val selectedDay = datePicker.dayOfMonth

            dateString = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            showToast("Selected Date: $dateString")

            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Shows a dialog for selecting the appointment time.
     * Allows users to pick a time from the time picker.
     */
    private fun showTimePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_time_picker)

        val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)
        val btnSetTime = dialog.findViewById<Button>(R.id.btnSetTime)

        btnSetTime.setOnClickListener {
            val selectedHour = timePicker.currentHour
            val selectedMinute = timePicker.currentMinute

            val formattedHour = String.format("%02d", selectedHour)
            val formattedMinute = String.format("%02d", selectedMinute)

            timeString = "$formattedHour:$formattedMinute"
            showToast("Selected Time: $timeString")

            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Shows a dialog for entering the appointment address.
     * Allows users to input the appointment address.
     */
    private fun showAddressEntryDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_address_entry)

        val etAddress = dialog.findViewById<EditText>(R.id.etAddress)
        val btnSaveAddress = dialog.findViewById<Button>(R.id.btnSaveAddress)

        btnSaveAddress.setOnClickListener {
            enteredAddress = etAddress.text.toString()
            showToast("Entered Address: $enteredAddress")

            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Saves the appointment details to Firebase Firestore, using the data
     * set up in saveScheduleToFirestore function.
     * Checks for duplicate vaccine names and appends a number if necessary,
     * in order to show the dose of vaccine.
     */
    private fun saveSchedule() {
        val email =  FirebaseAuth.getInstance().currentUser?.email.toString()
        var vaccineName = vaccineString.trim()

        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection(email)

        userCollection.get()
            .addOnSuccessListener { documents ->
                var count = 2
                val originalVaccineName = vaccineName

                while (documents.any { it.id == vaccineName }) {
                    vaccineName = "$originalVaccineName ($count)"
                    count++
                }

                saveScheduleToFirestore(email, vaccineName)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error checking document existence", e)
                showToast("Failed to save appointment. Please try again.")
            }
    }

    /**
     * Saves the appointment details to Firebase Firestore.
     *
     * @param email The user's email address.
     * @param vaccineName The selected vaccine name.
     */
    private fun saveScheduleToFirestore(email: String, vaccineName: String) {
        val firebaseData = FireStoreData(
            email,
            vaccineName,
            dateString,
            timeString,
            enteredAddress
        )

        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection(email)

        userCollection.document(vaccineName)
            .set(firebaseData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: $vaccineName")
                showToast("Appointment saved successfully")
                startActivity(Intent(this@ScheduleActivity, HomeActivity::class.java))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                showToast("Failed to save appointment. Please try again.")
            }
    }

    /**
     * Displays a short toast message to the user.
     *
     * @param message The message to display.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
