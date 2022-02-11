package com.tmvlg.zooanimal.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tmvlg.zooanimal.data.entities.Animal

@Database(entities = arrayOf(Animal::class), version = 3, exportSchema = false)
abstract class AnimalsDB: RoomDatabase() {

    abstract fun animalDAO(): AnimalDao

    companion object {
        @Volatile
        private var instance: AnimalsDB? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AnimalsDB::class.java,
                "animals.db")
                .fallbackToDestructiveMigration()
                .build()
    }

}