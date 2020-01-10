package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.SeminaryEntity

@Dao
interface SeminaryDao {
    @Query("SELECT * FROM seminaryentity")
    fun getAll(): List<SeminaryEntity>

    @Query("SELECT * FROM seminaryentity WHERE subjectId == :id")
    fun getBySubjectId(id: Long): SeminaryEntity

    @Insert
    fun insert(seminary: SeminaryEntity): Long

    @Insert
    fun insertAll(vararg seminaries: SeminaryEntity)

    @Delete
    fun delete(seminary: SeminaryEntity)
}