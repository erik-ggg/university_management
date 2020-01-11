package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.AbsenceEntity

@Dao
interface AbsenceDao {
    @Query("SELECT * FROM absenceentity")
    fun getAll(): List<AbsenceEntity>

    @Insert
    fun insert(absence: AbsenceEntity): Long

    @Insert
    fun insertAll(vararg absences: AbsenceEntity)

    @Delete
    fun delete(absence: AbsenceEntity)
}