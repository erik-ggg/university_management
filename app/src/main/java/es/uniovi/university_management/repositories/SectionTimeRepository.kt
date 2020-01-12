package es.uniovi.university_management.repositories

import android.content.Context
import es.uniovi.university_management.classes.TimeSubject
import es.uniovi.university_management.database.AppDatabase
import es.uniovi.university_management.model.SectionTimeEntity
import es.uniovi.university_management.util.DateParser
import java.lang.RuntimeException

class SectionTimeRepository {
    fun getAllBySubjectName(name: String, theoryDate: TimeSubject, practiceDate: TimeSubject
               , seminaryDate: TimeSubject, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = AppDatabase.getAppDatabase(context)
                var subjectId: Long = -1
                subjectId = db?.subjectDao()?.getByName(name)?.id!!.toLong()
                    if (subjectId == -1L)
                        throw RuntimeException("Subject not found")

                if (db != null) {
                    val theoryId = db.theoryDao().getBySubjectId(subjectId).id
                    val practiceId = db.practiceDao().getBySubjectId(subjectId).id
                    val seminaryId = db.seminaryDao().getBySubjectId(subjectId).id

                    if (theoryId != null && practiceId != null && seminaryId != null) {
                        val theoryStringDates = mutableListOf<String>()
                        val practiceStringDates = mutableListOf<String>()
                        val seminaryStringDates = mutableListOf<String>()

                        db.sectionTimeDao().getBySectionId(theoryId).forEach{
                            theoryStringDates.add(DateParser.LongToString(it.startDate))
                        }
                        theoryDate.startDate = theoryStringDates

                        db.sectionTimeDao().getBySectionId(practiceId).forEach{
                            practiceStringDates.add(DateParser.LongToString(it.startDate))
                        }
                        practiceDate.startDate = practiceStringDates

                        db.sectionTimeDao().getBySectionId(seminaryId).forEach{
                            seminaryStringDates.add(DateParser.LongToString(it.startDate))
                        }
                        seminaryDate.startDate = seminaryStringDates
                    } else
                        throw RuntimeException("Theory or Practice or Seminary is null")
                }
            }
        }
        t.start()
    }

    fun getTheoryDates(theoryId: Long, context: Context) {

    }
    fun getPracticeDates(practiceId: Long, context: Context) {

    }
    fun getSeminaryDates(seminaryId: Long, context: Context) {

    }
}