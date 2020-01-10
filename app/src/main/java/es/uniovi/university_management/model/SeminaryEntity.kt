package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SeminaryEntity(var subjectId: Long?) {
    constructor(): this(null)
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
        set(id) {
            field = id
        }
}