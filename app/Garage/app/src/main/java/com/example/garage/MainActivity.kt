package com.example.garage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var repository: RepairRepository

    //TODO: Remove it. Only for test
    private lateinit var apiTestRunner: ApiTestRunner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = RetrofitInstance.api
        authRepository = AuthRepository(this)
        repository = RepairRepository(apiService)

        //TODO: Remove it, only for test
        apiTestRunner = ApiTestRunner(repository, lifecycleScope)
        apiTestRunner.runAllTests()

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
