package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OfficeEntity(val buiding: String, val floor: Int, val door: String) {
    @PrimaryKey
    var id: Int? = null
    set(id) {
        field = id
    }
}