package es.uniovi.university_management.classes

abstract class SubjectSection(numberOfLessons: Int)
{
    val assistancePercent = 0.8
    val maxAbscense = numberOfLessons - numberOfLessons * assistancePercent
    var numberOfAbsence: Int = 0

    fun isInContinua(): Boolean {
        if (numberOfAbsence > maxAbscense)
            return false
        return true
    }
}