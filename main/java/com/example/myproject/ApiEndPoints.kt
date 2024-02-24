package com.example.myproject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {

    @GET("/items") // Replace with your actual API endpoint
    fun getPosts(@Query("userId") userId: String): Call<List<Post>>
    @DELETE("/api/todos/{id}")
    fun deleteTodo(@Path("id") id: Int): Call<Void>
}