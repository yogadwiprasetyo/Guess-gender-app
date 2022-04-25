package com.yogaprasetyo.juaraandroid.guessgender.data

import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.retrofit.ApiService

class FakeApiService : ApiService {
    override suspend fun guessGender(name: String): ResponseGuessGender {
        return ResponseGuessGender("men", 95.3, "yoga", 3324)
    }
}