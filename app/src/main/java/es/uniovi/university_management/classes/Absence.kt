package es.uniovi.university_management.classes

import java.util.*

/**
 * type: 1 = Theory
 * type: 2 = Practice
 * type: 3 = Seminary
 */
data class Absence(val date: Calendar, val type: Int, val isAutomatic: Boolean)