package com.example.vaccinemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * HomeActivity provides navigation options for various features
 * within the application.
 *
 * @property btnSchedule Button to navigate to the ScheduleActivity.
 * @property btnInfo Button to navigate to the InfoActivity.
 * @property btnTravelReq Button to navigate to the RequirementsActivity.
 * @property btnVacHistory Button to navigate to the HistoryActivity.
 * @property btnLogOut Button to initiate the logout process.
 */
class HomeActivity : AppCompatActivity() {

    // Buttons for UI interaction
    private lateinit var btnSchedule : Button
    private lateinit var btnInfo : Button
    private lateinit var btnTravelReq : Button
    private lateinit var btnVacHistory : Button
    private lateinit var btnLogOut : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()

        btnSchedule.setOnClickListener {
            startActivity(Intent(this@HomeActivity,
                ScheduleActivity::class.java))
        }

        btnInfo.setOnClickListener {
            startActivity(Intent(this@HomeActivity,
                InfoActivity::class.java))
        }

        btnTravelReq.setOnClickListener {
            startActivity(Intent(this@HomeActivity,
                RequirementsActivity::class.java))
        }

        btnLogOut.setOnClickListener {
            startActivity(Intent(this@HomeActivity,
                LoginActivity::class.java))
        }

        btnVacHistory.setOnClickListener {
            startActivity(Intent(this@HomeActivity,
                HistoryActivity::class.java))
        }

    }

    /**
     * Initializes all the views and sets up necessary listeners.
     */
    private fun initViews() {
        btnSchedule = findViewById(R.id.btnSchedule)
        btnInfo = findViewById(R.id.btnInfo)
        btnTravelReq = findViewById(R.id.btnTravelReq)
        btnVacHistory = findViewById(R.id.btnVacHistory)
        btnLogOut = findViewById(R.id.btnLogOut)
    }
}