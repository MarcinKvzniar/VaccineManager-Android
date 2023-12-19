package com.example.vaccinemanager

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnLogin: TextView = findViewById(R.id.tvLogin)
        btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,
                LoginActivity::class.java)
            )
        }
    }
}