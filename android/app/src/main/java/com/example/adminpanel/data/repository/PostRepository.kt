package com.example.adminpanel.data.repository

import com.example.adminpanel.data.ApiService
import com.example.adminpanel.data.Post
import com.example.adminpanel.data.RetrofitInstance

class PostRepository(private val apiService: ApiService = RetrofitInstance.api) {

    suspend fun getPosts(): List<Post> {
        return apiService.getPosts()
    }

    suspend fun createPost(post: Post): Post {
        return apiService.createPost(post)
    }

    suspend fun updatePost(id: Int, post: Post): Post {
        return apiService.updatePost(id, post)
    }

    suspend fun deletePost(id: Int) {
        apiService.deletePost(id)
    }
}