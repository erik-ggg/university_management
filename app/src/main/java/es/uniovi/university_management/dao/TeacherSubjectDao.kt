package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.TeacherSubject

@Dao
interface TeacherSubjectDao {
    @Query("SELECT * FROM teacherSubject")
    fun getAll(): List<TeacherSubject>

    @Insert
    fun insert(teacherSubject: TeacherSubject): Int

    @Insert
    fun insertAll(vararg teachersSubjects: TeacherSubject)

    @Delete
    fun delete(teacherSubject: TeacherSubject)
}