package es.uniovi.university_management.repositories

import android.content.Context
import es.uniovi.university_management.classes.Office
import es.uniovi.university_management.classes.Teacher
import es.uniovi.university_management.database.AppDatabase.Companion.getAppDatabase
import es.uniovi.university_management.model.TeacherEntity
import java.util.*

class TeachersRepository {

    fun deleteTeacher(name: String, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = getAppDatabase(context)
                val teacher = db?.teacherDao()?.getByName(name)
                val teacherSubject = db?.teacherSubjectDao()?.getByTeacherId(teacher?.id)
                val office = teacher?.officeId?.let { db.officeDao().getById(it) }
                office?.let { db.officeDao().delete(it) }
                teacherSubject?.let { db.teacherSubjectDao().delete(it) }
                teacher?.let { db.teacherDao().delete(it) }
            }
        }
        t.start()
    }

    fun getTeachers(subjectName: String, teachers: MutableList<Teacher>, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                super.run()
                val db = getAppDatabase(context)
                val subjectId = db!!.subjectDao().getByName(subjectName).id
                val teachersId = db.teacherSubjectDao().getBySubjectId(subjectId!!.toLong())
                val teachersData: MutableList<Teacher> = ArrayList()
                for ((teacherId) in teachersId) {
                    val (name, email, officeId) = db.teacherDao().getById(teacherId)
                    val (buiding, floor, door, coordinates) = db.officeDao().getById(officeId)
                    teachersData.add(Teacher(name, email, Office(buiding, floor, door,coordinates)))
                }
                teachers.addAll(teachersData)
            }
        }
        t.start()
    }
}