package com.example.garage
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.garage.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var repository: RepairRepository
    private lateinit var binding: ActivityMainBinding


    //TODO: Remove it. Only for test
    private lateinit var apiTestRunner: ApiTestRunner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Domyślnie wyświetl "Cennik"
        replaceFragment(HomeFragment())


        authRepository = AuthRepository(this)


        //TODO: Remove it, only for test
//        apiTestRunner = ApiTestRunner(repository, lifecycleScope)
//        apiTestRunner.runAllTests()

        checkTokenAndNavigate()
    }

    private fun checkTokenAndNavigate() {
        lifecycleScope.launch {
            val token = authRepository.getToken()
            if (token != null) {
                val apiService = RetrofitInstance.api
                repository = RepairRepository(apiService, token)  // Pass token here
                showMainScreen()
            } else {
                navigateToLoginScreen()
            }
        }
    }

    private fun showMainScreen() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_new_order -> {
                    replaceFragment(NewOrderFragment())
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

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}



