package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.Year

@Dao
interface YearDao {
    @Query("SELECT * FROM year")
    fun getAll(): List<Year>

    @Insert
    fun insert(year: Year): Int

    @Insert
    fun insertAll(vararg years: Year)

    @Delete
    fun delete(year: Year)
}