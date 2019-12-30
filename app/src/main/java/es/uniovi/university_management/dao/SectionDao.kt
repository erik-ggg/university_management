package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.SectionEntity

@Dao
interface SectionDao {
    @Query("SELECT * FROM sectionentity")
    fun getAll(): List<SectionEntity>

    @Insert
    fun insert(section: SectionEntity): Long

    @Insert
    fun insertAll(vararg sections: SectionEntity)

    @Delete
    fun delete(section: SectionEntity)
}