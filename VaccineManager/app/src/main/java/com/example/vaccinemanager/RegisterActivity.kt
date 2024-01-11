package com.example.vaccinemanager

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinemanager.firestore.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.vaccinemanager.firestore.FireStoreClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputEmailReg: EditText
    private lateinit var inputNameReg: EditText
    private lateinit var inputPasswordReg: EditText
    private lateinit var inputRepPassReg: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnGoToLogin: TextView
    private lateinit var btnGoogleReg: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()

        btnGoToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,
                    LoginActivity::class.java))
        }

        btnRegister.setOnClickListener {
            if (validateRegisterDetails()) {
                registerUser()
            }
        }

        btnGoogleReg.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun initViews() {
        inputNameReg = findViewById(R.id.inputName)
        inputEmailReg = findViewById(R.id.inputEmailReg)
        inputPasswordReg = findViewById(R.id.inputPasswordReg)
        inputRepPassReg = findViewById(R.id.inputRepeatPasswordReg)
        btnRegister = findViewById(R.id.btnRegister)
        btnGoToLogin = findViewById(R.id.tvLogin)
        btnGoogleReg = findViewById(R.id.btnGoogleReg)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun validateRegisterDetails(): Boolean {
        val specialChars = "!@#$%^&*-_+=,./\\".toCharArray()
        return when {
            TextUtils.isEmpty(inputNameReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_name))
                false
            }
            TextUtils.isEmpty(inputEmailReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_email))
                false
            }
            TextUtils.isEmpty(inputPasswordReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_password))
                false
            }
            TextUtils.isEmpty(inputRepPassReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_reppassword))
                false
            }
            inputPasswordReg.text.toString().trim { it <= ' ' }.length < 8 -> {
                showBasicToast(resources.getString(R.string.err_msg_password_length))
                false
            }
            !(inputPasswordReg.text.toString().trim { it <= ' ' }.any {
                    char -> char in specialChars}) -> {
                showBasicToast(resources.getString(R.string.err_msg_password_special_chars))
                false
            }
            inputPasswordReg.text.toString().trim { it <= ' ' } !=
                    inputRepPassReg.text.toString().trim { it <= ' ' } -> {
                showBasicToast(resources.getString(R.string.err_msg_password_mismatch))
                false
            }
            else -> true
        }
    }

    private fun registerUser() {
        val login = inputEmailReg.text.toString().trim()
        val password = inputPasswordReg.text.toString().trim()
        val name = inputNameReg.text.toString().trim()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result?.user!!
                    showBasicToast("You are registered successfully." +
                            " Your user id is ${firebaseUser.uid}")

                    val user = User("Test ID", name, true, login)
                    FireStoreClass().registerUserFS(this@RegisterActivity, user)

                    startActivity(Intent(this@RegisterActivity,
                        LoginActivity::class.java))
                    finish()
                } else {
                    showBasicToast(task.exception?.message.toString())
                }
            }
    }

    private fun showBasicToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun userRegistrationSuccess() {
        Toast.makeText(this@RegisterActivity, getString(R.string.register_success),
            Toast.LENGTH_LONG).show()
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign In Failed",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    goToHomeActivity()
                } else {
                    Toast.makeText(this, "Authentication Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}

