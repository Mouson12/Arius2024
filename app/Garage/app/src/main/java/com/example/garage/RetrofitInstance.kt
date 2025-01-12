package com.example.garage

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Singleton object responsible for providing a configured instance of Retrofit for API calls.
 */
object RetrofitInstance {

    // Base URL for the API
    private const val BASE_URL = "http://157.90.162.7:5001"

    // Logging interceptor to log request and response bodies for debugging
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with customized timeout and retry configurations
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging) // Add logging interceptor for debugging network calls
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS) // Set connection timeout
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)    // Set read timeout
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)   // Set write timeout
        .retryOnConnectionFailure(true) // Enable automatic retry on connection failures
        .build()

    /**
     * Lazy initialization of the `ApiService` instance.
     * Ensures that the Retrofit instance is created only once and reused for all API calls.
     */
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for the API
            .client(client) // Attach the OkHttpClient for HTTP configurations
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON serialization/deserialization
            .build()
            .create(ApiService::class.java) // Create the API service interface
    }
}