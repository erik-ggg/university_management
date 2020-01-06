package es.uniovi.university_management.classes

class Subject(val name: String) {
//    val name: String,
//    val teachers: MutableList<Teacher>
//) {    constructor(name: String, teachers: MutableList<Teacher>,
//                theory: Theory,
//                practice: Practice,
//                seminary: Seminary) : this(name, teachers)
//
//    val theory: Theory? = null
//    val practice: Practice? = null
//    val seminary: Seminary? = null
    constructor(name: String, teachers: MutableList<Teacher>): this(name)
    constructor(name: String, teachers: MutableList<Teacher>, theory: Theory,
                practice: Practice, seminary: Seminary): this(name,teachers)

    val teachers: MutableList<Teacher>? = null
    val theory: Theory? = null
    val practice: Practice? = null
    val seminary: Seminary? = null
    var id: Long = -1
}