package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TeacherEntity(
    val name: String,
    val email: String,
    val officeId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    set(id) {
        field = id
    }


}