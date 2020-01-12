package es.uniovi.university_management.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.uniovi.university_management.model.SectionTimeEntity

@Dao
interface SectionTimeDao {
    @Query("SELECT * FROM sectiontimeentity")
    fun getAll(): List<SectionTimeEntity>

    @Query("SELECT * FROM sectiontimeentity WHERE sectionId = :id AND sectionType = :type")
    fun getBySectionIdAndType(id: Long, type: Int): List<SectionTimeEntity>

    @Insert
    fun insert(section: SectionTimeEntity): Long

    @Insert
    fun insertAll(vararg sections: SectionTimeEntity)

    @Delete
    fun delete(section: SectionTimeEntity)
}