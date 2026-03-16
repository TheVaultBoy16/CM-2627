package com.example.cm_g9.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HomeItemDaoTest {
    private lateinit var homeItemDao: HomeItemDao
    private lateinit var db: AppDatabase
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        // Forzamos que la base de datos se escriba en el disco del sistema de archivos de la app
        db = Room.databaseBuilder(context, AppDatabase::class.java, "test_database").build()
        homeItemDao = db.homeItemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        // NO cerramos la DB inmediatamente para que el inspector pueda leerla
        // db.close()
    }

    @Test
    fun daoInsertAndGetItems_test() = runBlocking {
        homeItemDao.deleteAll()

        val item1 = HomeItemDB(
            name = "com.android.settings",
            date = "22/02/2026",
            horaUsadas = 1,
            minUsadas = 30,
            segUsadas = 0,
            habilitado = true
        )

        homeItemDao.insertAll(listOf(item1))


        println("Buscando proceso: ${context.packageName}")
        println("Datos insertados. Tienes 5 minutos para revisar el inspector...")

        delay(300_000)
    }
}