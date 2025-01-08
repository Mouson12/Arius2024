package com.example.garage

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val context: Context) {

    private val database = TokenDatabase.getInstance(context)
    private val api = RetrofitInstance.api

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = LoginRequest(email, password)
        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.access_token ?: return
                    saveToken(token)
                    onSuccess()
                } else {
                    onError("Invalid email or password")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = RegisterRequest(username, email, password)
        api.register(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess() // Rejestracja udana
                } else {
                    onError("Registration failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    private fun saveToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val authToken = AuthToken(token = token)
            database.tokenDao().insertToken(authToken)
        }
    }

    suspend fun getToken(): String? {
        return database.tokenDao().getToken()?.token
    }


}
