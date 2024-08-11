package com.example.filmhaven.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmhaven.api.NetworkResponse
import com.example.filmhaven.api.RetrofitInstance
import com.example.filmhaven.constant.Constant
import com.example.filmhaven.model.MovieModel
import com.example.filmhaven.model.MovieResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _movieResult = MutableLiveData<NetworkResponse<MovieResponse>>()
    val movieResult: LiveData<NetworkResponse<MovieResponse>> = _movieResult

    private var moviesList: List<MovieModel> = emptyList()

    private val db = FirebaseFirestore.getInstance()
    private val favoritesCollection = db.collection("favorites")

    private val _favoriteMovies = MutableLiveData<List<MovieModel>>()
    val favoriteMovies: LiveData<List<MovieModel>> = _favoriteMovies

    fun getData(movie: String, type: String) {
        _movieResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.movieApi.getMovie(Constant.apiKey, movie, type)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _movieResult.value = NetworkResponse.Success(it)
                        moviesList = it.search ?: emptyList()
                    }
                } else {
                    _movieResult.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                _movieResult.value = NetworkResponse.Error("Failed to load data")
            }
        }
    }

    fun getMovieById(movieId: String): MovieModel? {
        return moviesList.find { it.imdbID == movieId }
    }

    fun addFavorite(movie: MovieModel) {

        favoritesCollection.document(movie.imdbID).set(movie)
            .addOnSuccessListener {
                Log.d("MovieViewModel", "Movie added to favorites successfully")
            }
            .addOnFailureListener { e ->
                Log.e("MovieViewModel", "Error adding movie to favorites", e)
            }
    }

    fun removeFavorite(movie: MovieModel) {
        favoritesCollection.document(movie.imdbID).delete()
            .addOnSuccessListener {
                Log.d("MovieViewModel", "Movie removed from favorites successfully")
            }
            .addOnFailureListener { e ->
                Log.e("MovieViewModel", "Error removing movie from favorites", e)
            }
    }

    fun fetchFavorites() {
        viewModelScope.launch {
            try {
                favoritesCollection.get()
                    .addOnSuccessListener { result ->
                        val favorites = result.mapNotNull { doc ->
                            try {
                                doc.toObject(MovieModel::class.java)
                            } catch (e: Exception) {
                                Log.e("MovieViewModel", "Error mapping document to MovieModel", e)
                                null
                            }
                        }
                        _favoriteMovies.value = favorites
                    }
                    .addOnFailureListener { e ->
                        Log.e("MovieViewModel", "Error fetching favorite movies", e)
                        _favoriteMovies.value = emptyList()
                    }
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Exception fetching favorite movies", e)
                _favoriteMovies.value = emptyList()
            }
        }
    }
}
