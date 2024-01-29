package com.example.vaccinemanager.firestore

import com.google.firebase.Timestamp

data class FireStoreData (
    var email: String = "",
    var vaccineName: String = "",
    var date: String = "",
    var time: String = "",
    var address: String = ""
)