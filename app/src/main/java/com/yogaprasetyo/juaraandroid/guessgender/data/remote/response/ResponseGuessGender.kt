package com.yogaprasetyo.juaraandroid.guessgender.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseGuessGender(

    @field:SerializedName("gender")
    val gender: String?,

    @field:SerializedName("probability")
    val probability: Double,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("count")
    val count: Int
)
