package com.example.adminpanel.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // The IP address 10.0.2.2 is a special alias to the host loopback interface (127.0.0.1)
    // This is needed for the Android emulator to connect to a server running on the same machine.
    private const val BASE_URL = "http://10.0.2.2:5000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}