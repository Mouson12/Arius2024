package com.example.garage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.garage.databinding.ActivityMainBinding

/**
 * Main activity of the Garage application.
 * Handles navigation between fragments and checks user authentication state.
 */
class MainActivity : AppCompatActivity() {

    // Repository for managing user authentication
    private lateinit var authRepository: AuthRepository

    // Repository for managing repair-related data
    private lateinit var repository: RepairRepository

    // View binding for the activity layout
    private lateinit var binding: ActivityMainBinding

    // For testing purposes only (to be removed in production)
    private lateinit var apiTestRunner: ApiTestRunner

    /**
     * Called when the activity is created.
     * Sets up the view, initializes dependencies, and checks user authentication status.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Display the default fragment (e.g., "Cennik" - Price List)
        replaceFragment(HomeFragment())

        // Initialize repositories
        authRepository = AuthRepository(this)

        // Uncomment for API testing (debug only)
        // apiTestRunner = ApiTestRunner(repository, lifecycleScope)
        // apiTestRunner.runAllTests()

        // Check user authentication and navigate accordingly
        checkTokenAndNavigate()
    }

    /**
     * Checks if a valid token exists.
     * Navigates to the main screen if authenticated, otherwise redirects to the login screen.
     */
    private fun checkTokenAndNavigate() {
        lifecycleScope.launch {
            val token = authRepository.getToken()
            if (token != null) {
                val apiService = RetrofitInstance.api
                repository = RepairRepository(apiService, token) // Pass token to repository
                showMainScreen(repository)
            } else {
                navigateToLoginScreen()
            }
        }
    }

    /**
     * Configures the bottom navigation bar and sets up fragment switching logic.
     *
     * @param repository Instance of `RepairRepository` for managing repair data.
     */
    private fun showMainScreen(repository: RepairRepository) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_new_order -> {
                    replaceFragment(NewOrderFragment(repository))
                    true
                }
                R.id.nav_history -> {
                    replaceFragment(HistoryFragment())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Redirects the user to the login screen if authentication is required.
     * Clears the current task to ensure the user cannot return to this activity.
     */
    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Replaces the current fragment in the container with the specified fragment.
     *
     * @param fragment The new fragment to display.
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}