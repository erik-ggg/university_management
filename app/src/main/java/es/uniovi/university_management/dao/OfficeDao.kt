package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.OfficeEntity

@Dao
interface OfficeDao {
    @Query("SELECT * FROM officeentity")
    fun getAll(): List<OfficeEntity>

    @Insert
    fun insert(office: OfficeDao): Long

    @Insert
    fun insertAll(vararg offices: OfficeEntity)

    @Delete
    fun delete(office: OfficeEntity)
}