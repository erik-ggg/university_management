package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.TeacherSubjectEntity

@Dao
interface TeacherSubjectDao {
    @Query("SELECT * FROM teachersubjectentity")
    fun getAll(): List<TeacherSubjectEntity>

    @Query("SELECT * FROM teachersubjectentity WHERE subjectId = :id")
    fun getBySubjectId(id: Long): List<TeacherSubjectEntity>

    @Query("SELECT * FROM teachersubjectentity WHERE teacherId = :id")
    fun getByTeacherId(id: Int?): TeacherSubjectEntity

    @Insert
    fun insert(teacherSubject: TeacherSubjectEntity): Long

    @Insert
    fun insertAll(vararg teachersSubjects: TeacherSubjectEntity)

    @Delete
    fun delete(teacherSubject: TeacherSubjectEntity)
}