package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TeacherEntity(
    var name: String,
    var email: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    set(id) {
        field = id
    }


}