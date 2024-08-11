package com.example.filmhaven.ui.view

import MoviePage
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*
import com.example.filmhaven.viewmodel.MovieViewModel

@Composable
fun MovieApp(viewModel: MovieViewModel) {
    val navController = rememberNavController()
    var selectedItemIndex by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Filled.Home
        ),
        BottomNavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Filled.Favorite
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            when (index) {
                                0 -> navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                                1 -> navController.navigate("favorites") {
                                    popUpTo("home") { inclusive = false }
                                }
                            }
                        },
                        label = { Text(text = item.title) },
                        icon = {
                            Icon(
                                imageVector =
                                if (selectedItemIndex == index)
                                    item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
        content = { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    Modifier.fillMaxSize()
                ) {
                    composable("home") {
                        MovieHomePage(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("favorites") {
                        FavoriteScreen(viewModel = viewModel)
                    }
                    composable("movie/{movieId}") { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")
                        if (movieId != null) {
                            val movie = viewModel.getMovieById(movieId)
                            if (movie != null) {
                                MoviePage(movie = movie) {
                                    viewModel.addFavorite(movie)
                                }
                            } else {
                                Text(text = "Movie not found", color = Color.White)
                            }
                        } else {
                            Text(text = "Movie not found", color = Color.White)
                        }
                    }
                }
            }
        }
    )
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
