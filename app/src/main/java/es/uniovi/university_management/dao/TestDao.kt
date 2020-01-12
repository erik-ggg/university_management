package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.TestEntity

@Dao
interface TestDao {
    @Query("SELECT * FROM testentity")
    fun getAll(): List<TestEntity>

    @Query("SELECT * FROM testentity WHERE sectionId = :id")
    fun getBySectionId(id: Long): List<TestEntity>

    @Insert
    fun insert(test: TestEntity): Long

    @Insert
    fun insertAll(vararg tests: TestEntity)

    @Delete
    fun delete(test: TestEntity)
}