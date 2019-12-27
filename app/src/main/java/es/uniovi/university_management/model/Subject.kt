package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Subject(
    val name: String,
    @Ignore
    val teachers: MutableList<Teacher>
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
    var yearId: Int? = null
}