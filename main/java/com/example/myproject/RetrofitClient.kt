package com.example.myproject

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private  val BASE_URL =
        "http://10.0.2.2:3000/"// Replace with your actual base URL

    val apiEndPoint: ApiEndPoint by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            .create(ApiEndPoint::class.java)
    }




}
