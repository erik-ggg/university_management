package es.uniovi.university_management.classes

abstract class SubjectSection(numberOfLessons: Int)
{
    val assistancePercent = 0.8
    val maxAbscense = numberOfLessons - numberOfLessons * assistancePercent
    var absences = mutableListOf<Absence>()

    fun isInContinua(): Boolean {
        if (absences.size > maxAbscense)
            return false
        return true
    }
}