package com.example.adminpanel.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adminpanel.ui.theme.AdminPanelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditScreen(
    post: Post?, // Null if creating a new post
    onSave: (title: String, content: String) -> Unit,
    onNavigateUp: () -> Unit
) {
    var title by remember { mutableStateOf(post?.title ?: "") }
    var content by remember { mutableStateOf(post?.content ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (post == null) "New Post" else "Edit Post") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        // In a real app, you'd use an arrow icon
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onSave(title, content) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewPostEditScreenPreview() {
    AdminPanelTheme {
        PostEditScreen(post = null, onSave = { _, _ -> }, onNavigateUp = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ExistingPostEditScreenPreview() {
    AdminPanelTheme {
        PostEditScreen(
            post = Post(1, "Sample Title", "Sample content."),
            onSave = { _, _ -> },
            onNavigateUp = {}
        )
    }
}