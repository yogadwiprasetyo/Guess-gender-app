package com.yogaprasetyo.juaraandroid.guessgender.data

import androidx.lifecycle.LiveData
import com.yogaprasetyo.juaraandroid.guessgender.data.local.History
import com.yogaprasetyo.juaraandroid.guessgender.data.local.HistoryDao
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.retrofit.ApiService
import com.yogaprasetyo.juaraandroid.guessgender.helper.Helper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val apiService: ApiService,
    private val historyDao: HistoryDao
) {

    /**
     * Source Network
     *
     * Get the result for guessing gender from name,
     * then return response and insert to local database if success.
     *
     * @param name
     * */
    fun getGenderFromName(name: String): Flow<Result<ResponseGuessGender>> = flow {
        emit(Result.Loading)

        try {
            val response = apiService.guessGender(name)
            val newHistory = History(
                name = response.name,
                date = Helper.getCurrentDate()
            )
            emit(Result.Success(response))
            historyDao.insert(newHistory)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     * Source Local
     *
     * Get all the history from local database,
     * and always update when have the change.
     * */
    fun getAllGuessingHistory(): LiveData<List<History>> = historyDao.getAllHistory()

    /**
     * Source Local
     *
     * Remove all the data history in local database
     * It's function to run on background thread because modified local database.
     * */
    suspend fun deleteAllGuessingHistory() {
        historyDao.deleteAll()
    }

    /**
     * Source Local
     *
     * Remove a specific data history in local database
     * It's function to run on background thread because modified local database.
     *
     * @param history
     * */
    suspend fun deleteHistory(history: History) {
        historyDao.delete(history)
    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, historyDao)
            }.also { instance = it }
    }
}