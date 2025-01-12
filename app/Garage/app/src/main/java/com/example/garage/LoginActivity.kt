package com.example.garage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity responsible for handling user login.
 * Provides input fields for email and password, a login button, and a link to navigate to the registration screen.
 */
class LoginActivity : AppCompatActivity() {

    // Repository for handling authentication logic
    private lateinit var authRepository: AuthRepository

    /**
     * Initializes the activity and sets up the UI elements and event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the authentication repository
        authRepository = AuthRepository(this)

        // Set up the login button click listener
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            // Perform login using the authentication repository
            authRepository.login(email, password,
                onSuccess = {
                    // Navigate to the home screen upon successful login
                    navigateToHomeScreen()
                },
                onError = { message ->
                    // Show error message if login fails
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            )
        }

        // Set up the registration link click listener
        findViewById<TextView>(R.id.tvRegister).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) // Navigate to the registration activity
        }
    }

    /**
     * Navigates the user to the home screen after a successful login.
     * Clears the back stack to prevent navigating back to the login screen.
     */
    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}