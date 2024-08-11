package com.example.filmhaven.ui.view
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filmhaven.api.NetworkResponse
import com.example.filmhaven.viewmodel.MovieViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController

@Composable
fun MovieHomePage(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel,
    navController: NavController
) {
    var movieName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("movie") }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val movieResult by viewModel.movieResult.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {

                OutlinedTextField(
                    value = movieName.trim(),
                    onValueChange = { movieName = it },
                    label = { Text(text = "Search ${selectedType.capitalize()}")
                            },
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = selectedType.capitalize())
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Icon",
                                modifier = Modifier.clickable { expanded = true }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Movie") },
                                    onClick = {
                                        selectedType = "movie"
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Series") },
                                    onClick = {
                                        selectedType = "series"
                                        expanded = false
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

            }
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                viewModel.getData(movieName.trim(),selectedType)
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search For ${selectedType.capitalize()}",
                    modifier = Modifier.size(36.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (val result = movieResult) {
            is NetworkResponse.Error<*> -> {
                Text(text = result.message.toString())
            }
            NetworkResponse.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NetworkResponse.Success -> {
                val data = result.data
                if (data.response == "False") {
                    Text(
                        text = data.error ?: "Not found!",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                } else {
                    val movies = data.search ?: emptyList()
                    if (movies.isEmpty()) {
                        Text(
                            text = "No results found.",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(alignment = Alignment.CenterHorizontally)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),

                        ) {
                            items(movies) { movie ->
                                MovieCard(
                                    movie = movie,

                                    onClick = {
                                        navController.navigate("movie/${movie.imdbID}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
            null -> {}
        }
    }
}
