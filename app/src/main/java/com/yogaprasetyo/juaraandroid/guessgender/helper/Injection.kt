package com.yogaprasetyo.juaraandroid.guessgender.helper

import android.content.Context
import com.yogaprasetyo.juaraandroid.guessgender.data.AppRepository
import com.yogaprasetyo.juaraandroid.guessgender.data.local.HistoryRoomDatabase
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.retrofit.ApiConfig

/**
 * This object for using injection all the service
 * like ApiService, Database, and Repository
 *
 * @return AppRepository
 * */
object Injection {
    fun provideRepository(context: Context): AppRepository {
        val apiService = ApiConfig.getApiService()
        val database = HistoryRoomDatabase.getDatabase(context)
        val dao = database.historyDao()
        return AppRepository.getInstance(apiService, dao)
    }
}