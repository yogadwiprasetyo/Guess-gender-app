package com.yogaprasetyo.juaraandroid.guessgender.data

/**
 * This sealed class for result of source data remote
 * */
sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
