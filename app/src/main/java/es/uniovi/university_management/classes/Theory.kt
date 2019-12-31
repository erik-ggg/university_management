package es.uniovi.university_management.classes

data class Theory(val section: Section, val numberOfLessons: Int): SubjectSection(numberOfLessons)