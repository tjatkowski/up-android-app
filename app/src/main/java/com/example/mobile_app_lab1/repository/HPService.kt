package com.example.mobile_app_lab1.repository

import com.example.mobile_app_lab1.repository.model.Character
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path

interface HPService {

    @GET("/api/characters")
    suspend fun getCharactersList(): Response<List<Character>>

    @GET("/api/character/{characterId}")
    suspend fun getCharacterDetails(@Path("characterId") characterId: String): Response<List<Character>>

    companion object {
        private const val STAR_URL = "https://hp-api.onrender.com"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(STAR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val hpService: HPService by lazy {
            retrofit.create(HPService::class.java)
        }
    }
}