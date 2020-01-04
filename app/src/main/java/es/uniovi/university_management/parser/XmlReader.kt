package es.uniovi.university_management.parser

import android.content.Context
import android.content.res.XmlResourceParser
import android.util.Log
import es.uniovi.university_management.classes.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class XmlReader {

    var years = mutableListOf<Year>()

    fun readAndParse(context: Context): MutableList<Year> {
        val parserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser: XmlPullParser = parserFactory.newPullParser()
        val inputStream: InputStream = context.assets.open("mails.xml")
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        var eventType = parser.next()

        var isTeacher = false
        var isOffice = false

        var name: String
        var year: Year? = null
        var subject: Subject? = null
        var teacher: Teacher? = null
        var office: Office? = null
        while(eventType != XmlResourceParser.END_DOCUMENT) {
            try {
                when(eventType) {
                    XmlResourceParser.START_TAG -> {
                        name = parser.name
                        Log.d("XML", parser.name)
                        if (isOffice) {
                            if (name == "building")
                                office?.building = parser.nextText()
                            else if (name == "floor")
                                office?.floor = parser.nextText().toInt()
                            else if (name == "door")
                                office?.door = parser.nextText()
                            else if (name == "coordinates") {
                                office?.coordinates = parser.nextText()
                                teacher?.office = office
                            }
                        } else if (isTeacher && !isOffice){
                            if (name == "name")
                                teacher?.name = parser.nextText()
                            else if (name == "email")
                                teacher?.email = parser.nextText()
                            else {
                                office = Office(null, null, null, null)
                                isOffice = true
                            }
                        } else {
                            if (name == "year") {
                                val yearNumber = parser.getAttributeValue(null, "number")
                                year = Year(yearNumber.toInt(), mutableListOf())
                            } else if (name == "subject") {
                                val subjectName = parser.getAttributeValue(null, "name")
                                subject = Subject(subjectName, mutableListOf(),
                                    // TODO: Add number of lessons
                                    Theory(Section(0.3), 10),
                                    Practice(Section(0.6), 10),
                                    Seminary(Section(0.1), 100)
                                )
                            } else if (name == "teacher") {
                                teacher = Teacher(null, null, null)
                                isTeacher = true
                            }
                        }
                    }
                    XmlResourceParser.END_TAG -> {
                        name = parser.name
                        Log.d("XML", name)
                        if (name == "office") {
                            isOffice = false
                        }
                        else if (name == "teacher") {
                            teacher?.let { subject?.teachers?.add(it) }
                            isTeacher = false
                        }
                        else if (name == "subject")
                            subject?.let { year?.subjects?.add(it) }
                        else if (name == "year")
                            year?.let { years.add(it) }
                    }
                }
                eventType = parser.next()
            }catch (e: XmlPullParserException) {
                Log.e("XML", e.printStackTrace().toString())
            }
        }
        return years
    }
}

