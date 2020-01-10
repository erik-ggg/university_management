package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.TheoryEntity

@Dao
interface TheoryDao {
    @Query("SELECT * FROM theoryentity")
    fun getAll(): List<TheoryEntity>

    @Query("SELECT * FROM theoryentity WHERE subjectId == :id")
    fun getBySubjectId(id: Long): TheoryEntity

    @Insert
    fun insert(theory: TheoryEntity): Long

    @Insert
    fun insertAll(vararg theories: TheoryEntity)

    @Delete
    fun delete(theory: TheoryEntity)
}