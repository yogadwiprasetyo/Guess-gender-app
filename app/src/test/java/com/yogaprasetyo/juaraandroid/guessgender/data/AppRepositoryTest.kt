package com.yogaprasetyo.juaraandroid.guessgender.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yogaprasetyo.juaraandroid.guessgender.MainCoroutineRule
import com.yogaprasetyo.juaraandroid.guessgender.data.local.History
import com.yogaprasetyo.juaraandroid.guessgender.data.local.HistoryDao
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.retrofit.ApiService
import com.yogaprasetyo.juaraandroid.guessgender.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AppRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var historyDao: HistoryDao
    private lateinit var appRepository: AppRepository

    @Before
    fun setup() {
        apiService = FakeApiService()
        historyDao = FakeHistoryDao()
        appRepository = AppRepository(apiService, historyDao)
    }

    @Test
    fun `when guessGender Should Not Null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = ResponseGuessGender("men", 95.3, "yoga", 3324)
        val actualResponse = apiService.guessGender("yoga")

        assertNotNull(actualResponse)
        assertEquals(expectedResponse.name, actualResponse.name)
    }

    @Test
    fun `when insert Should Exists in getAllHistory`() = mainCoroutineRule.runBlockingTest {
        val sampleHistory = History(1, "yoga", "2022, 1 Jan 01:20 PM")
        historyDao.insert(sampleHistory)

        val actualHistory = historyDao.getAllHistory().getOrAwaitValue()

        assertTrue(actualHistory.contains(sampleHistory))
    }

    @Test
    fun `when delete Should Not Exists in getAllHistory`() = mainCoroutineRule.runBlockingTest {
        val sampleHistory = History(1, "yoga", "2022, 1 Jan 01:20 PM")
        historyDao.insert(sampleHistory)
        historyDao.delete(sampleHistory)

        val actualHistory = historyDao.getAllHistory().getOrAwaitValue()

        assertFalse(actualHistory.contains(sampleHistory))
    }

    @Test
    fun `when getAllHistory Should Return List`() = mainCoroutineRule.runBlockingTest {
        historyDao.insert(History(1, "yoga", "2022, 1 Jan 01:20 PM"))
        historyDao.insert(History(3, "saga", "2022, 3 Jan 01:20 PM"))
        historyDao.insert(History(4, "daga", "2022, 4 Jan 01:20 PM"))

        val actualHistory = historyDao.getAllHistory().getOrAwaitValue()

        assertTrue(actualHistory.isNotEmpty())
    }

    @Test
    fun `when deleteAll Should Empty in getAllHistory`() = mainCoroutineRule.runBlockingTest {
        historyDao.insert(History(1, "yoga", "2022, 1 Jan 01:20 PM"))
        historyDao.insert(History(3, "saga", "2022, 3 Jan 01:20 PM"))
        historyDao.insert(History(4, "daga", "2022, 4 Jan 01:20 PM"))
        historyDao.deleteAll()

        val actualHistory = historyDao.getAllHistory().getOrAwaitValue()

        assertTrue(actualHistory.isEmpty())
    }
}