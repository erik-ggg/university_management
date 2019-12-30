package es.uniovi.university_management.model

data class Section(val weighing: Double) {
    var mean: Double = 0.0
    var tests: MutableList<Test> = mutableListOf()
}