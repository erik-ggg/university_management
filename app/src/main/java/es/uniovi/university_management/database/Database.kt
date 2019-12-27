package es.uniovi.university_management.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.uniovi.university_management.dao.SubjectDao
import es.uniovi.university_management.dao.TeacherDao
import es.uniovi.university_management.dao.TeacherSubjectDao
import es.uniovi.university_management.dao.YearDao
import es.uniovi.university_management.model.Subject
import es.uniovi.university_management.model.Teacher
import es.uniovi.university_management.model.Year

@Database(entities = arrayOf(Year::class, Subject::class, Teacher::class, TeacherSubjectDao::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun yearDao(): YearDao
    abstract fun subjectDao(): SubjectDao
    abstract fun teacherDao(): TeacherDao

    companion object {
        private var INSTANCE: AppDatabase? = null;

        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "university.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}