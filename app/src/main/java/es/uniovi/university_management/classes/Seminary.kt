package es.uniovi.university_management.classes

data class Seminary(val section: Section, val numberOfLessons: Int): SubjectSection(numberOfLessons)