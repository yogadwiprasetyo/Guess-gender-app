package com.yogaprasetyo.juaraandroid.guessgender.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogaprasetyo.juaraandroid.guessgender.data.AppRepository
import com.yogaprasetyo.juaraandroid.guessgender.data.Result
import com.yogaprasetyo.juaraandroid.guessgender.data.local.History
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    // Prepare the data from source data remote
    private var _result = MediatorLiveData<Result<ResponseGuessGender>?>()
    val result: LiveData<Result<ResponseGuessGender>?> = _result

    init {
        restart()
    }

    /**
     * Clean the data or restart value livedata
     * */
    fun restart() {
        _result.value = null
    }

    /**
     * Get a response from data source remote and set object value live data _result
     * */
    fun setName(name: String) {
        val response = repository.getGenderFromName(name)
        _result.addSource(response) { mResult ->
            _result.value = mResult
        }
    }

    /**
     * Retrieve all data history on local database
     * */
    fun loadAllHistory() = repository.getAllGuessingHistory()

    /**
     * Remove all history in local database
     * and running function with coroutine scope
     * */
    fun removeAllHistory() {
        viewModelScope.launch {
            repository.deleteAllGuessingHistory()
        }
    }

    /**
     * Remove a specific history in local database
     * and running function with coroutine scope
     * */
    fun removeHistory(history: History) {
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }
}