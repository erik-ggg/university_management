package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AbsenceEntity(val sectionId: Long, val sectionType: Int, val date: Long, val isAutomatic: Boolean) {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
        set(id) {
            field = id
        }
}