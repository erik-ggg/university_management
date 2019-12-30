package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.TeacherEntity

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teacherentity")
    fun getAll(): List<TeacherEntity>

    @Insert
    fun insert(teacher: TeacherEntity): Long

    @Insert
    fun insertAll(vararg teachers: TeacherEntity)

    @Delete
    fun delete(teacher: TeacherEntity)
}