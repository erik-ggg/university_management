package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Year(
    var value: Int
) {
    constructor(value: Int, subject: MutableList<Subject>) : this(value)

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    set(id) {
        field = id
    }
    @Ignore
    var subjects: MutableList<Subject> = ArrayList()
}