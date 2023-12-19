package com.example.vaccinemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSignUp: TextView = findViewById(R.id.tvSignUp)
        btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }
    }
}