package es.uniovi.university_management.model

data class Subject(
    val name: String,
    val teachers: MutableList<Teacher>
)