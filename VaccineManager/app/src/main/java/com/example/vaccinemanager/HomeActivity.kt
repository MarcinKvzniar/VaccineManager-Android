package com.example.vaccinemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {

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


    }

    private fun initViews() {
        btnSchedule = findViewById(R.id.btnSchedule)
        btnInfo = findViewById(R.id.btnInfo)
        btnTravelReq = findViewById(R.id.btnTravelReq)
        btnVacHistory = findViewById(R.id.btnVacHistory)
        btnLogOut = findViewById(R.id.btnLogOut)
    }
}