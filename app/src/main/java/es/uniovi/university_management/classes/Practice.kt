package es.uniovi.university_management.classes

data class Practice(val section: Section, val numberOfLessons: Int): SubjectSection(numberOfLessons)