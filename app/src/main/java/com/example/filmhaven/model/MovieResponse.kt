package com.example.filmhaven.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search")
    val search: List<MovieModel>?,

    @SerializedName("totalResults")
    val totalResults: String?,

    @SerializedName("Response")
    val response: String?,

    @SerializedName("Error")
    val error: String?
)

