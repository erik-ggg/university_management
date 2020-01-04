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
        var type = -1
        var startDates = mutableListOf<String>()
        var startTimes = mutableListOf<String>()

        try {
            // Read the header
            fileReader.readLine()

            // Read the content
            val line = fileReader.readLine()
            if (line != null)
                    currentSubject = line.split(",")[0]
            while(line != null) {
                val tokens = line.split(",")
                val name = getSubjectName(tokens[0].split(".")[0])
                // Si es la misma asignatura seguimos agregando datos
                if (currentSubject == name) {
                    type = getSbujectType(tokens[0].split(".")[1])
                    val startDate = tokens[1]
                    val startTime = tokens[2]
                    startDates.add(startDate)
                    startTimes.add(startTime)
                } else {
                    // Cambiamos la asignatura actual, reiniciamos las listas y añadimos los datos
                    currentSubject = name
                    subjectsCalendar.add(TimeSubject(name, type, startDates, startTimes))
                    startDates.clear()
                    startTimes.clear()
                }
            }
        } catch (e: Exception) {
            Log.e("CSV Reading error", e.stackTrace.toString())
        }

        return subjectsCalendar
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
        when(token) {
            "T" -> return 1
            "L" -> return 1
            "S" -> return 1
            else -> return -1
        }
    }
}