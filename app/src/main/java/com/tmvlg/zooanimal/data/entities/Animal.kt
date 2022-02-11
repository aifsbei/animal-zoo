package com.tmvlg.zooanimal.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_table")
data class Animal(
    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "latin_name")
    var latinName: String = "",

    @ColumnInfo(name = "active_time")
    var activeTime: String = "",

    @ColumnInfo(name = "average_length")
    var averageLength: Float = UNDEFINED_LENGTH,

    @ColumnInfo(name = "average_weight")
    var averageWeight: Float = UNDEFINED_WEIGHT,

    @ColumnInfo(name = "lifespan")
    var lifespan: Int = UNDEFINED_LIFESPAN,

    @ColumnInfo(name = "habitat")
    var habitat: String = "",

    @ColumnInfo(name = "diet")
    var diet: String = "",

    @ColumnInfo(name = "geo")
    var geo: String = "",

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String = "",

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = UNDEFINED_ID,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Animal) return false
        return this.name == (other as Animal).name
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        const val UNDEFINED_LENGTH = -1f
        const val UNDEFINED_WEIGHT = -1f
        const val UNDEFINED_LIFESPAN = -1
        const val UNDEFINED_ID = -1
    }
}