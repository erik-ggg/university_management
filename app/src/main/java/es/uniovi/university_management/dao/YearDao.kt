package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.YearEntity

@Dao
interface YearDao {
    @Query("SELECT * FROM yearentity")
    fun getAll(): List<YearEntity>

    @Insert
    fun insert(year: YearEntity): Long

    @Insert
    fun insertAll(vararg years: YearEntity)

    @Delete
    fun delete(year: YearEntity)
}