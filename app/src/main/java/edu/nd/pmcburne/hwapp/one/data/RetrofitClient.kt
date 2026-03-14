package edu.nd.pmcburne.hwapp.one.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

object RetrofitClient {
    // 1. The base URL that all our API calls will start with
    private const val BASE_URL = "https://ncaa-api.henrygd.me/"

    // 2. THIS IS CRITICAL: We must tell the JSON parser to ignore keys we don't care about.
    // The NCAA API returns dozens of fields (like video links and bracket IDs).
    // If we don't set this to true, the app will crash because it can't find those fields in our Data Classes!
    private val networkJson = Json { ignoreUnknownKeys = true }

    // 3. The actual Retrofit builder
    val apiService: NcaaApiService by lazy {
        val contentType = MediaType.get("application/json")

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(NcaaApiService::class.java)
    }
}