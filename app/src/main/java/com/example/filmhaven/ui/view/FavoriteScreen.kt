package com.example.filmhaven.ui.view
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.filmhaven.model.MovieModel
import com.example.filmhaven.viewmodel.MovieViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            viewModel.fetchFavorites()
        } catch (_: Exception) {
        }
    }

    val favoriteMovies by viewModel.favoriteMovies.observeAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Favorites",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteMovies.isEmpty()) {
            Text("No favorite movies yet.")
        } else {
            LazyColumn {
                items(favoriteMovies) { movie ->
                    FavoriteMovieCard(
                        movie = movie,
                        onRemoveClick = {
                            viewModel.removeFavorite(movie)
                            viewModel.fetchFavorites()
                            Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show()
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteMovieCard(
    movie: MovieModel,
    onRemoveClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium // Rounded corners
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(movie.poster),
                contentDescription = movie.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    style = TextStyle(
                        fontSize = 16.sp,

                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.year,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }

            Button(
                onClick = onRemoveClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688)) // Custom color
            ) {
                Text("Remove", color = Color.White)
            }
        }
    }
}

