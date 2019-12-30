package es.uniovi.university_management.model

data class Year(
    var value: Int,
    var subjects: MutableList<Subject>
)