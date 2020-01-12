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
                        val theoryStringTimes = mutableListOf<String>()
                        val practiceStringDates = mutableListOf<String>()
                        val practiceStringTimes = mutableListOf<String>()
                        val seminaryStringDates = mutableListOf<String>()
                        val seminaryStringTimes = mutableListOf<String>()

                        db.sectionTimeDao().getBySectionIdAndType(theoryId, 1).forEach{
                            val date = DateParser.LongToString(it.startDate).split(" ")
                            theoryStringDates.add(date[0])
                            theoryStringTimes.add(date[1])
                        }
                        theoryDate.startDate = theoryStringDates
                        theoryDate.startTime = theoryStringTimes

                        db.sectionTimeDao().getBySectionIdAndType(practiceId, 2).forEach{
                            val date = DateParser.LongToString(it.startDate).split(" ")
                            practiceStringDates.add(date[0])
                            practiceStringTimes.add(date[1])
                        }
                        practiceDate.startDate = practiceStringDates
                        practiceDate.startTime = practiceStringDates

                        db.sectionTimeDao().getBySectionIdAndType(seminaryId, 3).forEach{
                            val date = DateParser.LongToString(it.startDate).split(" ")
                            seminaryStringDates.add(date[0])
                            seminaryStringTimes.add(date[1])
                        }
                        seminaryDate.startDate = seminaryStringDates
                        seminaryDate.startTime = seminaryStringTimes
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

    fun addSectionTime(subjectName: String, sectionSelected: Int, startDate: Long, endDate: Long, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = AppDatabase.getAppDatabase(context)
                val subjectId = db?.subjectDao()?.getByName(subjectName)?.id?.toLong()
                if (subjectId == null || subjectId <= 0)
                    throw RuntimeException("Can't find the specified subject")
                db.sectionTimeDao().insert(SectionTimeEntity(subjectId, sectionSelected, startDate, endDate))
            }
        }
        t.start()
    }

    fun delete(subjectName: String, type: Int, date: Long, context: Context) {
        val t: Thread = object : Thread() {
            override fun run() {
                val db = AppDatabase.getAppDatabase(context)
                val subjectId = db?.subjectDao()?.getByName(subjectName)?.id!!.toLong()
                if (subjectId == null || subjectId <= 0)
                    throw RuntimeException("Can't find the specified subject")
                val sectionTimeEntity = db.sectionTimeDao().getBySectionIdAndTypeAndDate(subjectId, type, date)
                if (sectionTimeEntity.isEmpty())
                    throw RuntimeException("SectionTime not found")
                else
                db.sectionTimeDao().delete(sectionTimeEntity[0])
            }
        }
        t.start()
    }
}