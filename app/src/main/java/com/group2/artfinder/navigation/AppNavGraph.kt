package com.group2.artfinder.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.group2.artfinder.ui.screens.ArtDetailScreen
import com.group2.artfinder.ui.screens.ArtListScreen
import com.group2.artfinder.ui.screens.LoginScreen
import com.group2.artfinder.ui.screens.RegisterScreen
import com.group2.artfinder.ui.screens.ProfileScreen
import com.group2.artfinder.ui.theme.Gold
import com.group2.artfinder.ui.theme.Muted
import com.group2.artfinder.ui.theme.MuseumSurface
import com.group2.artfinder.viewmodel.ArtViewModel
import com.group2.artfinder.viewmodel.AuthViewModel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.group2.artfinder.ui.screens.ArtMapScreen
import com.group2.artfinder.ui.screens.VisitedArtworkScreen
import com.group2.artfinder.viewmodel.VisitedArtworkViewModel

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Discover",   "artList", Icons.Filled.Search,   Icons.Outlined.Search),
    BottomNavItem("Collection", "visited", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    BottomNavItem("Profile",    "profile", Icons.Filled.Person,   Icons.Outlined.Person)
)

@Composable
fun AppNavGraph() {
    val navController   = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val artViewModel: ArtViewModel = viewModel()
    val visitedViewModel: VisitedArtworkViewModel = viewModel()
    val startDestination = if (authViewModel.isLoggedIn()) "artList" else "login"

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("artList", "visited", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MuseumSurface,
                    tonalElevation = Dp(0f)
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected    = selected,
                            onClick     = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = Gold,
                                selectedTextColor   = Gold,
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted,
                                indicatorColor      = MuseumSurface
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = startDestination,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(navController)
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("artList") {
                ArtListScreen(navController, artViewModel)
            }
            composable(
                route     = "artDetail/{artworkId}",
                arguments = listOf(navArgument("artworkId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("artworkId")
                ArtDetailScreen(navController, artworkId = id, artViewModel, visitedViewModel)
            }

            composable("visited") {
                VisitedArtworkScreen(navController, visitedViewModel)
            }
            composable("artMap/{title}/{latitude}/{longitude}/{isVisited}") { backStackEntry ->
                val title     = backStackEntry.arguments?.getString("title") ?: ""
                val latitude  = backStackEntry.arguments?.getString("latitude")?.toDoubleOrNull() ?: 0.0
                val longitude = backStackEntry.arguments?.getString("longitude")?.toDoubleOrNull() ?: 0.0
                val isVisited = backStackEntry.arguments?.getString("isVisited")?.toBoolean() ?: false
                ArtMapScreen(navController, title, latitude, longitude, isVisited)
            }

            composable("profile") {
                ProfileScreen(navController)
            }
        }
    }
}
