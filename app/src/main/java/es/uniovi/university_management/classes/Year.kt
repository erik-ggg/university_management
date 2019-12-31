package es.uniovi.university_management.classes

data class Year(
    var value: Int,
    var subjects: MutableList<Subject>
)