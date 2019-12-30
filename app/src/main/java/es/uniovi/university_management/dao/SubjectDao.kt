package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.SubjectEntity

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjectentity")
    fun getAll(): List<SubjectEntity>

    @Insert
    fun insert(subject: SubjectEntity): Long

    @Insert
    fun insertAll(vararg subjects: SubjectEntity)

    @Delete
    fun delete(subject: SubjectEntity)
}