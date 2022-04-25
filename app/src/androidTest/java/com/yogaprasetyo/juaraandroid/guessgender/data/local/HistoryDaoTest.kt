package com.yogaprasetyo.juaraandroid.guessgender.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.yogaprasetyo.juaraandroid.guessgender.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HistoryDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: HistoryRoomDatabase
    private lateinit var dao: HistoryDao
    private val sampleHistory = History(1, "yoga", "2022, 1 Jan 01:20 PM")

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HistoryRoomDatabase::class.java
        ).build()
        dao = database.historyDao()
    }

    @After
    fun closeDB() = database.close()

    @Test
    fun insert() = runBlockingTest {
        dao.insert(sampleHistory)

        val actualHistory = dao.getAllHistory().getOrAwaitValue()
        assertEquals(sampleHistory.name, actualHistory[0].name)
    }

    @Test
    fun delete() = runBlockingTest {
        dao.insert(sampleHistory)
        dao.delete(sampleHistory)

        val actualHistory = dao.getAllHistory().getOrAwaitValue()
        assertTrue(actualHistory.isEmpty())
    }

    @Test
    fun getAllHistory() = runBlockingTest {
        dao.insert(sampleHistory)
        dao.insert(sampleHistory)

        val actualHistory = dao.getAllHistory().getOrAwaitValue()
        assertTrue(actualHistory.isNotEmpty())
    }

    @Test
    fun deleteAll() = runBlockingTest {
        dao.insert(sampleHistory)
        dao.insert(sampleHistory)
        dao.deleteAll()

        val actualHistory = dao.getAllHistory().getOrAwaitValue()
        assertTrue(actualHistory.isEmpty())
    }
}