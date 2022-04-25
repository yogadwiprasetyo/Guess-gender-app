package com.yogaprasetyo.juaraandroid.guessgender.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.yogaprasetyo.juaraandroid.guessgender.MainCoroutineRule
import com.yogaprasetyo.juaraandroid.guessgender.data.AppRepository
import com.yogaprasetyo.juaraandroid.guessgender.data.Result
import com.yogaprasetyo.juaraandroid.guessgender.data.local.History
import com.yogaprasetyo.juaraandroid.guessgender.data.remote.response.ResponseGuessGender
import com.yogaprasetyo.juaraandroid.guessgender.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AppViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var appRepository: AppRepository
    private lateinit var appViewModel: AppViewModel

    @Before
    fun setup() {
        appViewModel = AppViewModel(appRepository)
    }

    @Test
    fun `when setName Invoke Should Result Have Value`() = mainCoroutineRule.runBlockingTest {
        val expectedResult = flowOf(Result.Success(ResponseGuessGender("men", 95.9, "yoga", 2234)))

        `when`(appRepository.getGenderFromName("yoga")).thenReturn(expectedResult)
        appViewModel.setName("yoga")
        val actualResult = appViewModel.result.getOrAwaitValue()

        assertNotNull("should not null", actualResult)
        assertTrue("should true", actualResult is Result.Success)
    }

    @Test
    fun `when loadAllHistory Available Should Return List`() {
        val expectedLiveDataResult = MutableLiveData<List<History>>()
        val listHistory = listOf(
            History(1, "yoga", "2022, 1 Jan 01:20 PM"),
            History(2, "woga", "2022, 2 Jan 01:20 PM"),
            History(3, "saga", "2022, 3 Jan 01:20 PM"),
            History(4, "daga", "2022, 4 Jan 01:20 PM"),
        )
        expectedLiveDataResult.value = listHistory

        `when`(appViewModel.loadAllHistory()).thenReturn(expectedLiveDataResult)
        val actualResult = appViewModel.loadAllHistory().getOrAwaitValue()

        assertTrue(actualResult.isNotEmpty())
        assertEquals(listHistory.size, actualResult.size)
    }

    @Test
    fun `when loadAllHistory Empty Should Return Empty List`() {
        val expectedLiveDataResult = MutableLiveData<List<History>>()
        val listHistory = emptyList<History>()
        expectedLiveDataResult.value = listHistory

        `when`(appViewModel.loadAllHistory()).thenReturn(expectedLiveDataResult)
        val actualResult = appViewModel.loadAllHistory().getOrAwaitValue()

        assertTrue(actualResult.isEmpty())
        assertEquals(listHistory.size, actualResult.size)
    }

    @Test
    fun `when removeAllHistory Should Call deleteAllGuessingHistory`() =
        mainCoroutineRule.runBlockingTest {
            appViewModel.removeAllHistory()
            verify(appRepository).deleteAllGuessingHistory()
        }

    @Test
    fun `when removeHistory Should Call deleteHistory`() = mainCoroutineRule.runBlockingTest {
        appViewModel.removeHistory(History(1, "yoga", "2022, 1 Jan 01:20 PM"))
        verify(appRepository).deleteHistory(History(1, "yoga", "2022, 1 Jan 01:20 PM"))
    }
}