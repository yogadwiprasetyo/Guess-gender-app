package com.yogaprasetyo.juaraandroid.guessgender.helper

import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {

        /**
         * Get current date with format
         * 2022, 1 Jan 12:00 a/p.m
         * */
        fun getCurrentDate(): String {
            val pattern = "yyyy, d MMM hh:mm a"
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}