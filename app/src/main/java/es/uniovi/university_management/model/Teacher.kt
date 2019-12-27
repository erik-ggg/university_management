package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Teacher(
    var name: String,
    var email: String
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
}