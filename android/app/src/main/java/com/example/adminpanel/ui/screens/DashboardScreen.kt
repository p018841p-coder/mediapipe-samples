package com.example.adminpanel.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adminpanel.data.Post
import com.example.adminpanel.ui.theme.AdminPanelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    posts: List<Post>,
    onAddPost: () -> Unit,
    onPostClick: (Post) -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Posts")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPost) {
                Icon(Icons.Default.Add, contentDescription = "Add Post")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(posts) { post ->
                PostListItem(post = post, onClick = { onPostClick(post) })
            }
        }
    }
}

@Composable
fun PostListItem(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val samplePosts = listOf(
        Post(1, "First Post", "This is the content of the first post."),
        Post(2, "Second Post", "This is the content of the second post.")
    )
    AdminPanelTheme {
        DashboardScreen(posts = samplePosts, onAddPost = {}, onPostClick = {}, onRefresh = {})
    }
}