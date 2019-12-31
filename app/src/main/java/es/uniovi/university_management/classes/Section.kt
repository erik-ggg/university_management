package es.uniovi.university_management.classes

data class Section(val weighing: Double) {
    var mean: Double = 0.0
    var tests: MutableList<Test> = mutableListOf()
}