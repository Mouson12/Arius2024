package com.example.garage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authRepository = AuthRepository(this)

        checkTokenAndNavigate()
    }

    private fun checkTokenAndNavigate() {
        lifecycleScope.launch {
            val token = authRepository.getToken()
            if (token != null) {
                navigateToHomeScreen()
            } else {
                navigateToLoginScreen()
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // aby nie wrócić do ekranu logowania
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
