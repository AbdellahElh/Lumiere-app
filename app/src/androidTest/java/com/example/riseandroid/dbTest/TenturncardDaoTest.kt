package com.example.riseandroid.dbTest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.riseandroid.data.RiseDatabase
import com.example.riseandroid.data.entitys.TenturncardDao
import org.junit.runner.RunWith
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.data.entitys.TenturncardEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertEquals
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TenturncardDaoTest {
    private lateinit var tenturncardDao : TenturncardDao
    private lateinit var risedb : RiseDatabase

    private var toAddTenturncard = TenturncardEntity(
        id = 1,
        amountLeft = 10,
        ActivationCode = "activationCode",
        UserTenturncardId = 1,
        purchaseDate = null,
        expirationDate = null
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        risedb = Room.inMemoryDatabaseBuilder(context, RiseDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        tenturncardDao = risedb.tenturncardDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        risedb.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoAdd_addTenturncardToDb() = runBlocking {
        tenturncardDao.addTenturncard(toAddTenturncard)
        val allTenturncards = tenturncardDao.getAllTenturncards().first()
        assertEquals(allTenturncards.first(), toAddTenturncard)
    }
}