package com.example.vaccinemanager.firestore

import com.google.firebase.Timestamp

data class FireStoreData (
    var email: String = "",
    var vaccineName: String = "",
    var dateTime: Timestamp,
    var address: String = ""
)