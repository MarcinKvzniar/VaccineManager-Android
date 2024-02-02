package com.example.vaccinemanager

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinemanager.firestore.FireStoreData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * HistoryActivity displays the vaccination history of the current user.
 *
 * @property recyclerView RecyclerView to display the vaccination history.
 * @property adapter Adapter for the RecyclerView.
 * @property vaccinationList MutableList containing vaccination history data.
 * @property btnBackHome Button to navigate back to the HomeActivity.
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var vaccinationList: MutableList<FireStoreData> = mutableListOf()
    private lateinit var btnBackHome: Button

    /**
     * Initializes the activity layout, RecyclerView, and listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(vaccinationList)
        recyclerView.adapter = adapter

        btnBackHome = findViewById(R.id.btnBackHome2)

        btnBackHome.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, HomeActivity::class.java))
        }

        fetchVaccinationHistoryFromFirestore()
    }

    /**
     * Fetches vaccination history data from Firestore and populates the RecyclerView.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchVaccinationHistoryFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

        db.collection(userEmail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val vaccineName = document.id
                    val email = document.getString("email") ?: ""
                    val date = document.getString("date") ?: ""
                    val time = document.getString("time") ?: ""
                    val address = document.getString("address") ?: ""
                    val fireStoreData = FireStoreData(email, vaccineName, date, time, address)
                    vaccinationList.add(fireStoreData)
                }
                vaccinationList.sortBy { it.vaccineName }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}
