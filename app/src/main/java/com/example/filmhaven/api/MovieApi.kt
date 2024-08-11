package com.example.filmhaven.api
import com.example.filmhaven.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(".")
    suspend fun getMovie(
        @Query("apikey") apiKey : String,
        @Query("s") movieName : String,
        @Query("type") type : String
    ): Response<MovieResponse>
}