package com.example.adminpanel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.adminpanel.ui.screens.DashboardScreen
import com.example.adminpanel.ui.screens.LoginScreen
import com.example.adminpanel.ui.screens.PostEditScreen
import com.example.adminpanel.ui.theme.AdminPanelTheme
import com.example.adminpanel.viewmodel.PostViewModel
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminPanelTheme {
                AdminPanelApp()
            }
        }
    }
}

@Composable
fun AdminPanelApp(postViewModel: PostViewModel = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val posts by postViewModel.posts.collectAsState()
    val isLoading by postViewModel.isLoading.collectAsState()
    val error by postViewModel.error.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("dashboard") {
                DashboardScreen(
                    posts = posts,
                    onAddPost = { navController.navigate("edit_post/new") },
                    onPostClick = { post ->
                        // Pass post data as a JSON string argument
                        val postJson = Gson().toJson(post)
                        navController.navigate("edit_post/$postJson")
                    },
                    onRefresh = { postViewModel.fetchPosts() }
                )
            }
            composable(
                route = "edit_post/{postJson}",
                arguments = listOf(navArgument("postJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val postJson = backStackEntry.arguments?.getString("postJson")
                val post = if (postJson == "new") null else Gson().fromJson(postJson, com.example.adminpanel.data.Post::class.java)

                PostEditScreen(
                    post = post,
                    onSave = { title, content ->
                        if (post == null) {
                            postViewModel.addPost(title, content)
                        } else {
                            postViewModel.updatePost(post.id, title, content)
                        }
                        navController.popBackStack()
                    },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}