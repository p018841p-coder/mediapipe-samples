package com.example.adminpanel.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @POST("api/posts")
    suspend fun createPost(@Body post: Post): Post

    @PUT("api/posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Post

    @DELETE("api/posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Unit>
}