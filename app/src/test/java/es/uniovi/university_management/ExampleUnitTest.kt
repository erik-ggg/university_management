package es.uniovi.university_management

import android.content.Context
import android.os.Build
import es.uniovi.university_management.database.AppDatabase
import es.uniovi.university_management.model.Subject
import es.uniovi.university_management.model.Teacher
import es.uniovi.university_management.model.Year
import es.uniovi.university_management.xmlParser.MailReader
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ExampleUnitTest {

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        // TODO: wipe db first
    }

    @Test
    fun read_xml() {
        // Teachers
        val teacher1 = Teacher("Profesor1", "Profesor1@mail.com")
        val teacher2 = Teacher("Profesor2", "Profesor2@mail.com")

        // Subjects
        val subject1 = Subject("Introducción a la Programación", mutableListOf(teacher1, teacher2))
        val subject2 = Subject("Metodología de la Programación", mutableListOf())

        // Years
        val year1 = Year(1, mutableListOf(subject1, subject2))
        val year2 = Year(2, mutableListOf())
        val year3 = Year(3, mutableListOf())
        val year4 = Year(4, mutableListOf())

        val years: List<Year> = listOf(year1, year2, year3, year4)

        val mailReader = MailReader()
        assertEquals(years, mailReader.readAndParse(context))
    }

    @Test
    fun importXml() {
        val xmlReader = MailReader()
        val xmlData = xmlReader.readAndParse(context)
        val db = AppDatabase.getAppDatabase(context)

        // TODO: creo profesores y guardo sus id, luego, creo la asignatura, con la asignatura creada creo la relacion entre ambos usando el id de la asignatura y la lista de los profesores
        var teacherIds = mutableListOf<Int>()
        var yearId = -1

        xmlData.forEach() {
            yearId = db?.yearDao()?.insert(it)!!
            it.subjects.forEach() {
                it.teachers.forEach() {
                    teacherIds.add(db?.teacherDao()?.insert(it)!!)
                }
                it.yearId = yearId
                db?.subjectDao()?.insert(it)
                teacherIds.forEach() {

                }
            }
            db?.yearDao()?.insert(it)
        }
        assertTrue(db?.yearDao()?.getAll()?.size!! > 0)
        assertTrue(db?.subjectDao()?.getAll()?.size!! > 0)
        assertTrue(db?.teacherDao()?.getAll()?.size!! > 0)
    }
}
