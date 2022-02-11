package com.tmvlg.zooanimal.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tmvlg.zooanimal.data.entities.Animal

@Dao
interface AnimalDao {

    @Query("SELECT * FROM animal_table")
    fun getLocalAnimals(): List<Animal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(animals: List<Animal>)

    @Query("DELETE FROM animal_table")
    fun deleteLocal()

}