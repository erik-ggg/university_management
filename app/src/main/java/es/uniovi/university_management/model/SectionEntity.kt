package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SectionEntity(val weighting: Double) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
        set(id) {
            field = id
        }
    var mean: Double? = null
}