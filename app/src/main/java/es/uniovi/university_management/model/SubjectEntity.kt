package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity( indices = arrayOf(Index(value = ["name"], unique = true)))
data class SubjectEntity(
    val name: String,
    val yearId: Long
) {
//    constructor(name: String, yearId: Long, theoryId: Long?, practiceId: Long?, seminaryId: Long?) : this(name, yearId)

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
        set(id) {
            field = id
        }
}