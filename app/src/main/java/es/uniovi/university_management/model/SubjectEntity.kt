package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class SubjectEntity(
    val name: String,
    val yearId: Long
) {
    constructor(name: String, yearId: Long, theoryId: Long?, practiceId: Long?, seminaryId: Long?) : this(name, yearId)

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
        set(id) {
            field = id
        }
}