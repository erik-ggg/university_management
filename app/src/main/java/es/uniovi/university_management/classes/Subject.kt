package es.uniovi.university_management.classes

data class Subject(
    val name: String,
    val teachers: MutableList<Teacher>
) {    constructor(name: String, teachers: MutableList<Teacher>,
                theory: Theory,
                practice: Practice,
                seminary: Seminary) : this(name, teachers)

    val theory: Theory? = null
    val practice: Practice? = null
    val seminary: Seminary? = null
}