package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.PracticeEntity

@Dao
interface PracticeDao {
    @Query("SELECT * FROM practiceentity")
    fun getAll(): List<PracticeEntity>

    @Query("SELECT * FROM practiceentity WHERE subjectId == :id")
    fun getBySubjectId(id: Long): PracticeEntity

    @Insert
    fun insert(practice: PracticeEntity): Long

    @Insert
    fun insertAll(vararg practices: PracticeEntity)

    @Delete
    fun delete(practice: PracticeEntity)
}