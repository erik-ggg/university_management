package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.Teacher

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teacher")
    fun getAll(): List<Teacher>

    @Insert
    fun insert(teacher: Teacher): Int

    @Insert
    fun insertAll(vararg teachers: Teacher)

    @Delete
    fun delete(teacher: Teacher)
}