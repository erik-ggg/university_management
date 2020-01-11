package es.uniovi.university_management.repositories

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import es.uniovi.university_management.classes.Subject
import es.uniovi.university_management.classes.TimeSubject
import es.uniovi.university_management.database.AppDatabase.Companion.getAppDatabase
import es.uniovi.university_management.model.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SubjectRepository {
    fun addSubjects(subjects: List<Subject>, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = getAppDatabase(context)
                //                    db.subjectDao().insertSubjects(subjects, db);
                val teachersId: MutableList<Long> =
                    ArrayList()
                var officeId = 1L
                for (subject in subjects) {
                    for (teacher in subject.teachers!!) {
                        Log.d("Teacher", teacher.toString())
                        val office = teacher.office
                        if (office != null) {
                            officeId = db!!.officeDao().insert(
                                OfficeEntity(
                                    office.building!!,
                                    office.floor!!, office.door!!, office.coordinates!!
                                )
                            )
                            Log.d("OfficeId", officeId.toString())
                            teachersId.add(
                                db.teacherDao().insert(
                                    TeacherEntity(
                                        teacher.name!!,
                                        teacher.email!!, officeId
                                    )
                                )
                            )
                        }
                    }
                    try {
                        val subjectId =
                            db!!.subjectDao().insert(SubjectEntity(subject.name, 1L))
                        db.theoryDao().insert(TheoryEntity(subjectId))
                        db.practiceDao().insert(PracticeEntity(subjectId))
                        db.seminaryDao().insert(SeminaryEntity(subjectId))
                        Log.d("SubjectId", subjectId.toString())
                        for (teacherId in teachersId) {
                            db.teacherSubjectDao()
                                .insert(TeacherSubjectEntity(teacherId, subjectId))
                        }
                        teachersId.clear()
                    } catch (e: SQLiteConstraintException) {
                        Log.e("SQL_ERROR", "Unique field insert error.")
                    }
                }
            }
        }
        t.start()
    }

    fun addDates(timeSubjects: List<TimeSubject>, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = getAppDatabase(context)
                val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy HH.mm")
                var sectionId: Long? = -1L
                for ((name, type, startDate, startTime) in timeSubjects) {
                    val subjectEntity = db!!.subjectDao().getByName(name)
                    if (subjectEntity != null) {
                        when (type) {
                            1 -> sectionId =
                                db.theoryDao().getBySubjectId(subjectEntity.id!!.toLong()).id
                            2 -> sectionId =
                                db.practiceDao().getBySubjectId(subjectEntity.id!!.toLong()).id
                            3 -> sectionId =
                                db.seminaryDao().getBySubjectId(subjectEntity.id!!.toLong()).id
                        }
                        // Si existe algun dato ya introducido no lo aniadimos
                        val data =
                            db.sectionTimeDao().getBySectionId(sectionId!!)
                        if (data.size == 0) {
                            for (i in startDate.indices) {
                                var date: String? = startDate[i]
                                val time = startTime[i]
                                date += time
                                try {
                                    val resDate = formatter.parse(date)
                                    db.sectionTimeDao().insert(
                                        SectionTimeEntity(
                                            sectionId,
                                            resDate.time,
                                            resDate.time
                                        )
                                    )
                                } catch (e: ParseException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }
        t.start()
    }

    fun getSubjects(subjectsAdded: ArrayList<Subject>, context: Context): MutableList<Subject>? {
        val subjects: MutableList<Subject> = ArrayList()
        val t: Thread = object : Thread() {
            override fun run() {
                val data = getAppDatabase(context)!!.subjectDao().getAll()
                for (entity in data) {
                    val subject = Subject(entity.name)
                    subject.id = entity.id!!.toLong()
                    subjects.add(subject)
                }
                subjectsAdded.addAll(subjects)
            }
        }
        t.start()
        return subjects
    }
}