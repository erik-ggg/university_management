package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.Subject

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subject")
    fun getAll(): List<Subject>

    @Insert
    fun insert(subject: Subject): Long

    @Insert
    fun insertAll(vararg subjects: Subject)

    @Delete
    fun delete(subject: Subject)
}