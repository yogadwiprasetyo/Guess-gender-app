package com.yogaprasetyo.juaraandroid.guessgender.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("SELECT * FROM history")
    fun getAllHistory(): LiveData<List<History>>

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}