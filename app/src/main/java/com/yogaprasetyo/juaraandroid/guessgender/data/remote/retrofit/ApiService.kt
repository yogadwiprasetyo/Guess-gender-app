package com.yogaprasetyo.juaraandroid.guessgender.data.remote.retrofit

import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("/")
    suspend fun guessGender(
        @Query("name") name: String
    ): ResponseGuessGender
}