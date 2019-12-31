package es.uniovi.university_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.uniovi.university_management.xmlParser.XmlReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reader = XmlReader()
        reader.readAndParse(applicationContext)
    }
}
