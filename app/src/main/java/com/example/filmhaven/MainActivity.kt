package com.example.filmhaven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.filmhaven.ui.theme.FilmHavenTheme
import com.example.filmhaven.ui.view.MovieApp
import com.example.filmhaven.viewmodel.MovieViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Obtain the MovieViewModel instance
        val movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        setContent {
            FilmHavenTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Film Haven") },
                            colors = TopAppBarDefaults.mediumTopAppBarColors(
                                containerColor = Color(0xFF008080),
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MovieApp(viewModel = movieViewModel)
                    }
                }
            }
        }
    }
}
