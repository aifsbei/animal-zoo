package com.tmvlg.zooanimal

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.local.db.AnimalDao
import com.tmvlg.zooanimal.data.local.db.AnimalsDB
import com.tmvlg.zooanimal.ui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DBUnitTest {

    private lateinit var db: AnimalsDB
    private lateinit var animalDao: AnimalDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AnimalsDB::class.java
        ).build()
        animalDao = db.animalDAO()
    }

    @Test
    fun setData() {
        val animal = Animal(
            "1",
            "2",
            "3",
            4f,
            5f,
            6,
            "7",
            "8",
            "9",
            "10",
        )
        animalDao.insert(animal)
    }

    @Test
    fun getData() {
        val animalsLD = animalDao.getLocalAnimals()
        println(animalsLD.value)
    }

    @After
    fun closeDb() {
        db.close()
    }
}