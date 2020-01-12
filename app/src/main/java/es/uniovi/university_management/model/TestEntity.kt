package es.uniovi.university_management.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestEntity(val sectionId: Long, val name: String, val mark: Double, val type: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
        set(id) {
            field = id
        }
}