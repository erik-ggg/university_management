package es.uniovi.university_management.parser

import android.content.Context
import android.util.Log
import es.uniovi.university_management.classes.TimeSubject
import java.io.*
import java.lang.Exception

class CSVReader() {

    private val CALCULO = "Cal"
    private val SEW = "SEW"
    private val CVVS = "CVVS"
    private val IR = "IR"
    private val SI = "SI"
    private val SEV = "SEV"
    private val SDM = "SDM"

    fun readCSV(context: Context): MutableList<TimeSubject> {
        val inputStream: InputStream = context.assets.open("plan.csv")
//        val fileReader = BufferedReader(FileReader(File("E:\\University\\4\\SDM\\university_management\\app\\src\\test\\java\\es\\uniovi\\university_management\\plan.csv")))
        val fileReader = BufferedReader(InputStreamReader(inputStream))
//        val fileReader = BufferedReader(context.assets.open("plan.csv").bufferedReader())
        var subjectsCalendar = mutableListOf<TimeSubject>()
        var currentSubject = ""
        var currentType = -1
        var type = -1
        var startDates = mutableListOf<String>()
        var startTimes = mutableListOf<String>()

        try {
            // Read the header
            var line = fileReader.readLine()
            // Skip the space
            line = fileReader.readLine()

            // Read the content
            while(line != null) {

                if (line != "") {
                    val tokens = line.split(",")
                    val name = getSubjectName(tokens[0].split(".")[0])
                    type = getSbujectType(tokens[0].split(".")[1])
                    // Si es la misma asignatura seguimos agregando datos
                    if (currentSubject == name && currentType == type) {
                        addDateAndTime(tokens, startDates, startTimes)
                    } else {
                        // Añadimos la asignatura con su tipo y las listas de datos
                        subjectsCalendar.add(TimeSubject(currentSubject, currentType, startDates, startTimes))

                        // Cambiamos las asignatura actual, si solo cambia el tipo esto no afecta
                        currentSubject = name
                        currentType = type
                        startDates.clear() // limpiamos las listas
                        startTimes.clear()

                        addDateAndTime(tokens, startDates, startTimes)
                    }
                }
                line = fileReader.readLine()
                // Establecemos la primera asignatura
                if (currentSubject == "") {
                    currentSubject = getSubjectName(line.split(",")[0].split(".")[0])
                    currentType = getSbujectType(line.split(",")[0].split(".")[1])
                }
                if (line == null) {
                    subjectsCalendar.add(TimeSubject(currentSubject, type, startDates, startTimes))
                }
            }
        } catch (e: Exception) {
            Log.e("CSV Reading error", e.stackTrace.toString())
        }

        return subjectsCalendar
    }

    private fun addDateAndTime(
        tokens: List<String>,
        startDates: MutableList<String>,
        startTimes: MutableList<String>
    ) {
        val startDate = tokens[1]
        val startTime = tokens[2]
        startDates.add(startDate)
        startTimes.add(startTime)
    }

    private fun getSubjectName(token: String): String {
        when(token) {
            // TODO: Pasar a constantes
            CALCULO -> return "Calculo"
            SEW -> return "Software y Estándares para la Web"
            CVVS -> return "Calidad Validación y Verificación"
            IR -> return "Ingeniería de Requisitos"
            SI -> return "Sistemas Inteligentes"
            SEV -> return "Software de Entretenimiento y Videojuegos"
            SDM -> return "Software y Dispositivos Móviles"
            else -> return ""
        }
    }

    private fun getSbujectType(token: String): Int {
        // TODO: tutorias grupales
        when(token) {
            "T" -> return 1
            "L" -> return 2
            "S" -> return 3
            "TG" -> return 1
            else -> return -1
        }
    }
}