package com.example.cm_g9.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HomeItemDaoTest {
    private lateinit var homeItemDao: HomeItemDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        homeItemDao = db.homeItemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsertAndGetItems_test() = runBlocking {
        val item1 = HomeItem(
            name = "App1",
            imageRes = 101,
            date = "22/02/2026",
            horaUsadas = 1,
            minUsadas = 30,
            segUsadas = 0
        )
        val item2 = HomeItem(
            name = "App2",
            imageRes = 102,
            date = "23/02/2026",
            horaUsadas = 0,
            minUsadas = 45,
            segUsadas = 15
        )
        val testItems = listOf(item1, item2)


        homeItemDao.insertAll(testItems)


        val allItems = homeItemDao.getAllItems().first()


        assertEquals(2, allItems.size)
        

        val firstItem = allItems[0]
        assertEquals("App1", firstItem.name)
        assertEquals(101, firstItem.imageRes)
        assertEquals(1, firstItem.horaUsadas)


        val secondItem = allItems[1]
        assertEquals("App2", secondItem.name)
        assertEquals(102, secondItem.imageRes)
        assertEquals(45, secondItem.minUsadas)
    }
}
