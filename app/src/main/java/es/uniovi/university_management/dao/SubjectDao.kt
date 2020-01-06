package es.uniovi.university_management.dao

import android.content.Context
import android.util.Log
import androidx.room.*
import es.uniovi.university_management.classes.Subject
import es.uniovi.university_management.classes.TeacherSubject
import es.uniovi.university_management.database.AppDatabase
import es.uniovi.university_management.model.OfficeEntity
import es.uniovi.university_management.model.SubjectEntity
import es.uniovi.university_management.model.TeacherEntity
import es.uniovi.university_management.model.TeacherSubjectEntity

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjectentity")
    fun getAll(): List<SubjectEntity>

    @Insert
    fun insert(subject: SubjectEntity): Long

    @Insert
    fun insertAll(vararg subjects: SubjectEntity)

    @Transaction
    fun insertSubjects(subject: List<Subject>, db: AppDatabase) {
        Log.d("Subjects", subject.toString())
        var teachersId = mutableListOf<Long>()
        var officeId: Long = -1
        subject.forEach {
            it.teachers?.forEach {
                Log.d("Teacher", it.toString())
                val office = it.office
                officeId = db?.officeDao()?.insert(OfficeEntity(office?.building!!,
                    office.floor!!, office.door!!, office.coordinates!!))!!
                Log.d("OfficeId", officeId.toString())
                teachersId.add(db?.teacherDao()?.insert(TeacherEntity(it?.name!!, it.email!!, officeId)))
            }
            val subjectId = db?.subjectDao()?.insert(SubjectEntity(it.name, 1, 1, 1, 1))
            Log.d("SubjectId", subjectId.toString())
            teachersId.forEach {
                db?.teacherSubjectDao()?.insert(TeacherSubjectEntity(it, subjectId!!))
            }
            teachersId.clear()
        }
    }

    @Delete
    fun delete(subject: SubjectEntity)
}