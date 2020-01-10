package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SectionTimeEntity(val sectionId: Long, val startDate: Long, val endDate: Long) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}