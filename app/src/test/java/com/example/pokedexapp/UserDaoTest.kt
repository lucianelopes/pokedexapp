package com.example.pokedexapp

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: PokemonDatabase
    private lateinit var userDao: UserDao

    @Beforere
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java).build()
        userDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetUser() = runBlocking {
        val user = UserEntity("testuser", "password")
        userDao.insert(user)

        val retrievedUser = userDao.getUser("testuser", "password")
        assertEquals(user, retrievedUser)
    }
}