package es.uniovi.university_management.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.uniovi.university_management.dao.*
import es.uniovi.university_management.model.*

@Database(entities = arrayOf(YearEntity::class, SubjectEntity::class, TeacherEntity::class, TeacherSubjectEntity::class,
    OfficeEntity::class, PracticeEntity::class, TheoryEntity::class, SeminaryEntity::class,
    SectionEntity::class, TestEntity::class), exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun yearDao(): YearDao
    abstract fun subjectDao(): SubjectDao
    abstract fun teacherDao(): TeacherDao
    abstract fun teacherSubjectDao(): TeacherSubjectDao
    abstract fun officeDao(): OfficeDao
    abstract fun theoryDao(): TheoryDao
    abstract fun practiceDao(): PracticeDao
    abstract fun seminaryDao(): SeminaryDao
    abstract fun sectionDao(): SectionDao
    abstract fun testDao(): TestDao


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
    }

    fun destroy() {
        INSTANCE = null
    }
}