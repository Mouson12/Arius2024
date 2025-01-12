package com.example.garage

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity responsible for user registration.
 * Provides input fields for username, email, and password,
 * and handles registration by interacting with the backend through `AuthRepository`.
 */
class RegisterActivity : AppCompatActivity() {

    // Repository for managing authentication and user-related operations
    private lateinit var authRepository: AuthRepository

    /**
     * Called when the activity is created.
     * Initializes UI components and sets up event listeners for user interaction.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the authentication repository
        authRepository = AuthRepository(this)

        // Set up the register button click listener
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUsername).text.toString().trim()
            val email = findViewById<EditText>(R.id.etEmail).text.toString().trim()
            val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()

            // Validate input fields before proceeding
            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter a username!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter an email address!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call the register function from the repository
            authRepository.register(username, email, password,
                onSuccess = {
                    // Inform the user of a successful registration and navigate back to login screen
                    Toast.makeText(this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show()
                    finish() // Closes the activity and returns to the login screen
                },
                onError = { message ->
                    // Show an error message if registration fails
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}