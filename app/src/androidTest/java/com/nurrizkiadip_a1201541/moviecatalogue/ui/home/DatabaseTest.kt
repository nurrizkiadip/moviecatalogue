
package com.nurrizkiadip_a1201541.moviecatalogue.ui.home

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.room.MovieDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
abstract class DatabaseTest {

  protected lateinit var appDatabase: MovieDatabase

  @Before
  fun initDb() {
    appDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        MovieDatabase::class.java)
        .allowMainThreadQueries()
        .build()
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    appDatabase.close()
  }
}