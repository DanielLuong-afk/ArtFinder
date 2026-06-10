package com.group2.artfinder.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.group2.artfinder.ui.artwork.ArtDetailScreen
import com.group2.artfinder.ui.artwork.ArtListScreen
import com.group2.artfinder.ui.auth.LoginScreen
import com.group2.artfinder.ui.auth.RegisterScreen
import com.group2.artfinder.ui.profile.ProfileScreen
import com.group2.artfinder.viewmodel.AuthViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArtFinderApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val startDestination = if (authViewModel.isLoggedIn()) "artList" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("artList") {
            ArtListScreen(navController)
        }
        composable("artDetail/{artworkId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("artworkId")?.toIntOrNull()
            ArtDetailScreen(navController, artworkId = id)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
    }
}