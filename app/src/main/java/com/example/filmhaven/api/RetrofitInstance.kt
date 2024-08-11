package com.example.filmhaven.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val movieApi :  MovieApi = getInstance().create(MovieApi::class.java)

    private const val baseurl = "https://www.omdbapi.com/"

    private fun getInstance() : Retrofit {

        return Retrofit.Builder()

            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}