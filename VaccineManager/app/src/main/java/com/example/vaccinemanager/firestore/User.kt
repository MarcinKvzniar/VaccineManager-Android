package com.example.vaccinemanager.firestore

import com.google.firebase.Timestamp

class User(
    val id: String="",
    val name: String="",
    val registeredUser: Boolean = false,
    val email: String=""
)
