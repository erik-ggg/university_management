package es.uniovi.university_management

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import es.uniovi.university_management.classes.*
import es.uniovi.university_management.database.AppDatabase
import es.uniovi.university_management.model.*
import es.uniovi.university_management.parser.CSVReader
import es.uniovi.university_management.parser.XmlReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.lang.Exception

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ExampleUnitTest {

    lateinit var context: Context
    lateinit var db: AppDatabase

    @Before
    fun setUp() {
//        context = RuntimeEnvironment.application
        context = ApplicationProvider.getApplicationContext()
        db = AppDatabase.getAppDatabase(context)!!
        // TODO: wipe db first
    }

    @After
    fun tearDown() {
        db.close()
        db.destroy()
    }

    @Test
    fun read_xml() {
        // Office
        val office1 = Office("Facultad de Ciencas", 1, "111", "43.3577599,-5.8558198")

        // Teachers
        val teacher1 = Teacher("Profesor1", "Profesor1@mail.com", office1)
        val teacher2 = Teacher("Profesor2", "Profesor2@mail.com", null)

        // Subjects
        val subject1 = Subject("Introducción a la Programación", mutableListOf(teacher1, teacher2))
        val subject2 = Subject("Metodología de la Programación", mutableListOf())

        // Years
        val year1 = Year(1, mutableListOf(subject1, subject2))
        val year2 = Year(2, mutableListOf())
        val year3 = Year(3, mutableListOf())
        val year4 = Year(4, mutableListOf())

        val years: List<Year> = listOf(year1, year2, year3, year4)

        val mailReader = XmlReader()
        assertEquals(years, mailReader.readAndParse(context))
    }

    @Test
    fun importXml() {
        val xmlReader = XmlReader()
        val xmlData = xmlReader.readAndParse(context)

        // TODO: creo profesores y guardo sus id, luego, creo la asignatura, con la asignatura creada creo la relacion entre ambos usando el id de la asignatura y la lista de los profesores
        var teacherIds = mutableListOf<Int>()
        var subjectId: Long = -1
        var yearId: Long = -1

        var years = mutableListOf<YearEntity>()
        var subjects = mutableListOf<SubjectEntity>()
        var teachers = mutableListOf<TeacherEntity>()
//        var offices = mutableListOf<OfficeEntity>()
//        var practices = mutableListOf<PracticeEntity>()
//        var theories = mutableListOf<TheoryEntity>()
//        var seminaries = mutableListOf<SeminaryEntity>()
//        var sections = mutableListOf<SectionEntity>()

        try {
            GlobalScope.launch {
                xmlData.forEach() {
                    yearId = db?.yearDao()?.insert(YearEntity(it.value))!!
                    it.subjects?.forEach() {
                        it.teachers?.forEach() {
//                            val officeId = db?.officeDao().insert(
//                                OfficeEntity(
//                                    it?.office?.building!!,
//                                    it.office?.floor!!, it.office?.door!!, it.office?.coordinates!!
//                                )
//                            )
                            teacherIds.add(
                                db?.teacherDao()?.insert(
                                    TeacherEntity(
                                        it.name!!,
                                        it.email!!,
                                        1
                                    )
                                ).toInt()
                            )
                        }
                        // Inicializamos practica, teoria, seminario y los valores de las secciones.
//                    val theoryId = db?.theoryDao().insert(TheoryEntity(
//                        db?.sectionDao().insert(SectionEntity(it.theory?.section?.weighing!!))
//                    ))
//                    val practiceId = db?.practiceDao().insert(PracticeEntity(
//                        db?.sectionDao().insert(SectionEntity(it.practice?.section?.weighing!!))
//                    ))
//                    val seminaryId = db?.seminaryDao().insert(
//                        SeminaryEntity(
//                        db?.sectionDao().insert(SectionEntity(it.seminary?.section?.weighing!!))
//                    ))
                        subjectId =
                            db?.subjectDao()?.insert(SubjectEntity(it.name, yearId, 1, 1, 1))
                        teacherIds.forEach() {
                            db?.teacherSubjectDao()
                                ?.insert(TeacherSubjectEntity(it, subjectId.toInt()))
                        }
                    }
                }

                years = db?.yearDao()?.getAll() as MutableList<YearEntity>
                subjects = db?.subjectDao()?.getAll() as MutableList<SubjectEntity>
                teachers = db?.teacherDao()?.getAll() as MutableList<TeacherEntity>
//            offices = db?.officeDao().getAll() as MutableList<OfficeEntity>
//            practices = db?.practiceDao().getAll() as MutableList<PracticeEntity>
//            theories = db?.theoryDao().getAll() as MutableList<TheoryEntity>
//            seminaries = db?.seminaryDao().getAll() as MutableList<SeminaryEntity>
//            sections = db?.sectionDao().getAll() as MutableList<SectionEntity>
            }

            assertTrue(years.size > 0)
            assertTrue(subjects.size > 0)
            assertTrue(teachers.size > 0)
//        assertTrue(offices.size > 0)
//        assertTrue(theories.size > 0)
//        assertTrue(practices.size > 0)
//        assertTrue(seminaries.size > 0)
//        assertTrue(sections.size > 0)
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    @Test
    fun checkAbscenseWithoutCoordinatesTest() {
        val theory1 = Theory(Section(1.0), 10)
        theory1.numberOfAbsence = 1
        assertTrue(theory1.isInContinua())
        theory1.numberOfAbsence = 2
        assertTrue(theory1.isInContinua())
        theory1.numberOfAbsence = 3
        assertFalse(theory1.isInContinua())

        val theory2 = Theory(Section(1.0), 1)
        theory2.numberOfAbsence = 0
        assertTrue(theory2.isInContinua())
        theory2.numberOfAbsence = 1
        assertFalse(theory2.isInContinua())

        val theory3 = Theory(Section(1.0), 7)
        theory3.numberOfAbsence = 1
        assertTrue(theory3.isInContinua())
        theory3.numberOfAbsence = 2
        assertFalse(theory3.isInContinua())

        val practice = Theory(Section(1.0), 11)
        practice.numberOfAbsence = 2
        assertTrue(practice.isInContinua())
    }

    @Test
    fun checkAbscenseWithCoordinatesTest() {
        val userCoordinates = LatLng(43.3577599,-5.8558198)
        val universityCoordinates = LatLng(43.35480645581845, -5.8512203005943775)
        val distance = SphericalUtil.computeDistanceBetween(universityCoordinates, userCoordinates)
        val practice = Practice(Section(.8), 10)
        if (distance > 100) {
            practice.numberOfAbsence++
            practice.isInContinua()
            // TODO: que pasa de normal
            // TODO: Si no esta que pasa...
        }
    }

    @Test
    fun loadCSV() {
        val reader = CSVReader()
        val data = reader.readCSV(context)
        assertTrue(data.size > 0)
    }

    /**
     * Simulate user interaction.
     * First load the subjects and teachers data from the xml and selects which he needs
     */
    @Test
    fun test1() {

    }

    /**
     * TODO
     */
    @Test
    fun seminaryAssistanceGPSTest() {

    }
}
