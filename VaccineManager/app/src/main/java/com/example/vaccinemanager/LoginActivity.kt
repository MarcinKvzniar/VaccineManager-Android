package com.example.vaccinemanager

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider

/**
 * LoginActivity allows users to log in to the application
 * using email/password or Google authentication.
 *
 * @property inputEmailLog EditText for user email input.
 * @property inputPasswordLog EditText for user password input.
 * @property btnLogin Button to initiate login process.
 * @property btnGoToRegister TextView to navigate to the registration screen.
 * @property btnGoogleLog Button to initiate Google Sign-In.
 * @property googleSignInClient GoogleSignInClient for Google Sign-In.
 * @property firebaseAuth FirebaseAuth instance for Firebase Authentication.
 * @property showPassword CheckBox to toggle password visibility.
 */
class LoginActivity : AppCompatActivity() {

    // Buttons for UI interaction
    private lateinit var btnLogin: Button
    private lateinit var btnGoToRegister: TextView
    private lateinit var btnGoogleLog: Button

    // EditTexts for user input
    private lateinit var inputEmailLog: EditText
    private lateinit var inputPasswordLog: EditText

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var showPassword : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            logInRegisteredUser()
        }

        btnGoogleLog.setOnClickListener {
            signInWithGoogle()
        }
    }

    /**
     * Initializes all the views and sets up necessary listeners.
     */
    private fun initViews() {
        inputEmailLog = findViewById(R.id.inputEmailLog)
        inputPasswordLog = findViewById(R.id.inputPasswordLog)
        showPassword = findViewById(R.id.showPasswordCheckBox)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoToRegister = findViewById(R.id.tvSignUp)
        btnGoogleLog = findViewById(R.id.btnGoogleLog)

        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                inputPasswordLog.
                transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                inputPasswordLog.
                transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    /**
     * Validates the login details entered by the user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(inputEmailLog.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(getString(R.string.err_msg_enter_email))
                false
            }
            TextUtils.isEmpty(inputPasswordLog.text.toString().trim()) -> {
                showBasicToast(getString(R.string.err_msg_enter_password))
                false
            }
            else -> true
            }
        }

    /**
     * Logs in the registered user using email and password.
     */
    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            val email = inputEmailLog.text.toString().trim()
            val password = inputPasswordLog.text.toString().trim()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showBasicToast("Logged in successfully.")
                        goToHomeActivity()
                        finish()
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                getString(R.string.err_msg_user_not_found)
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                getString(R.string.err_msg_wrong_password)
                            }
                            else -> {
                                getString(R.string.err_msg_unknown_error)
                            }
                        }
                        showBasicToast(errorMessage)
                    }
                }
        }
    }

    /**
     * Navigates the user to the HomeActivity upon successful login.
     */
    private fun goToHomeActivity() {
        val uid = FirebaseAuth.getInstance().currentUser?.email.toString()

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("uID", uid)
        startActivity(intent)
    }

    /**
     * Shows a basic Toast message.
     */
    private fun showBasicToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Initiates the Google Sign-In process.
     */
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Handles the result of the Google Sign-In activity.
     */
    @Deprecated("Deprecated in Java")
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

    /**
     * Authenticates the user with Firebase using Google credentials.
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google Sign-In successful",
                        Toast.LENGTH_SHORT).show()
                    goToHomeActivity()
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Request code for Google Sign-In
    companion object {
        private const val RC_SIGN_IN = 123
    }
}
