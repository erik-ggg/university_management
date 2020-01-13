package es.uniovi.university_management.repositories

import android.content.Context
import es.uniovi.university_management.classes.Absence
import es.uniovi.university_management.database.AppDatabase
import es.uniovi.university_management.model.AbsenceEntity

class AbsencesRepository {
    fun addAbsence(subjectName: String, absence: Absence, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = AppDatabase.getAppDatabase(context)
                val id = db?.subjectDao()?.getByName(subjectName)?.id
                db?.absenceDao()?.insert(AbsenceEntity(id!!.toLong(), absence.type, absence.date.time, absence.isAutomatic))
            }
        }
        t.start()
    }

    fun delete(absence: Absence, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = AppDatabase.getAppDatabase(context)
                val absenceEntity = db?.absenceDao()?.getByDate(absence.date.time)
                absenceEntity?.let { db.absenceDao().delete(it) }
            }
        }
        t.start()
    }
}