package com.example.vaccinemanager.firestore

import com.example.vaccinemanager.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUserFS(activity: RegisterActivity, userInfo: User){

        mFireStore.collection("Appointments")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
            }
    }
}