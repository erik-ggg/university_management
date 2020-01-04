package es.uniovi.university_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.uniovi.university_management.parser.CSVReader
import es.uniovi.university_management.parser.XmlReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reader = CSVReader()
        val data = reader.readCSV(applicationContext)
        print(data.toString())
    }
}
