package es.uniovi.university_management.classes


/**
 * type: 1 = Theory
 * type: 2 = Practice
 * type: 3 = Seminary
 */
data class TimeSubject(val name: String, val type: Int, var startDate: List<String>, val startTime: List<String>)