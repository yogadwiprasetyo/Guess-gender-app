package com.yogaprasetyo.juaraandroid.guessgender.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yogaprasetyo.juaraandroid.guessgender.data.local.History
import com.yogaprasetyo.juaraandroid.guessgender.data.local.HistoryDao

class FakeHistoryDao : HistoryDao {

    private var historyData = mutableListOf<History>()

    override suspend fun insert(history: History) {
        historyData.add(history)
    }

    override suspend fun delete(history: History) {
        historyData.remove(history)
    }

    override fun getAllHistory(): LiveData<List<History>> {
        val observableHistory = MutableLiveData<List<History>>()
        observableHistory.value = historyData
        return observableHistory
    }

    override suspend fun deleteAll() {
        historyData.removeAll(historyData)
    }
}