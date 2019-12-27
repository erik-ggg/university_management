package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Subject(
    val name: String
) {
    constructor(name: String, teacher: MutableList<Teacher>) : this(name)

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    set(id) {
        field = id
    }
    @Ignore
    val teachers: MutableList<Teacher> = ArrayList()
    var yearId: Long? = null
}