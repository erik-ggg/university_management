package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Year(
    var value: Int,
    @Ignore
    var subjects: MutableList<Subject>
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
}