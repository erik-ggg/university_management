package es.uniovi.university_management.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.uniovi.university_management.dao.SubjectDao
import es.uniovi.university_management.dao.TeacherDao
import es.uniovi.university_management.dao.TeacherSubjectDao
import es.uniovi.university_management.dao.YearDao
import es.uniovi.university_management.model.SubjectEntity
import es.uniovi.university_management.model.TeacherEntity
import es.uniovi.university_management.model.TeacherSubjectEntity
import es.uniovi.university_management.model.YearEntity

@Database(entities = arrayOf(YearEntity::class, SubjectEntity::class, TeacherEntity::class, TeacherSubjectEntity::class), exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun yearDao(): YearDao
    abstract fun subjectDao(): SubjectDao
    abstract fun teacherDao(): TeacherDao
    abstract fun teacherSubjectDao(): TeacherSubjectDao

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