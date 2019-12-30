package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SeminaryEntity(var sectionId: Long?) {
    constructor(): this(null)
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
        set(id) {
            field = id
        }
}