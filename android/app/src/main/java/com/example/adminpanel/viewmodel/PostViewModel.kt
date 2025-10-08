package com.example.adminpanel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adminpanel.data.Post
import com.example.adminpanel.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val repository: PostRepository = PostRepository()) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _posts.value = repository.getPosts()
            } catch (e: Exception) {
                _error.value = "Failed to fetch posts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPost(title: String, content: String) {
        viewModelScope.launch {
            try {
                val newPost = Post(id = 0, title = title, content = content) // ID is ignored by backend
                repository.createPost(newPost)
                fetchPosts() // Refresh the list
            } catch (e: Exception) {
                _error.value = "Failed to add post: ${e.message}"
            }
        }
    }

    fun updatePost(id: Int, title: String, content: String) {
        viewModelScope.launch {
            try {
                val updatedPost = Post(id = id, title = title, content = content)
                repository.updatePost(id, updatedPost)
                fetchPosts() // Refresh the list
            } catch (e: Exception) {
                _error.value = "Failed to update post: ${e.message}"
            }
        }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch {
            try {
                repository.deletePost(id)
                fetchPosts() // Refresh the list
            } catch (e: Exception) {
                _error.value = "Failed to delete post: ${e.message}"
            }
        }
    }
}