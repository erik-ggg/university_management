package es.uniovi.university_management.xmlParser

import android.content.Context
import android.content.res.XmlResourceParser
import android.util.Log
import es.uniovi.university_management.model.Subject
import es.uniovi.university_management.model.Teacher
import es.uniovi.university_management.model.Year
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class MailReader {

    var years = mutableListOf<Year>()

    fun readAndParse(context: Context): MutableList<Year> {
        val parserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser: XmlPullParser = parserFactory.newPullParser()
        val inputStream: InputStream = context.assets.open("mails.xml")
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        var eventType = parser.next()

        var name: String
        var year: Year? = null
        var subject: Subject? = null
        var teacher: Teacher? = null
        while(eventType != XmlResourceParser.END_DOCUMENT) {
            try {
                when(eventType) {
                    XmlResourceParser.START_TAG -> {
                        name = parser.name
                        Log.d("XML", parser.name)
                        val triple = startTagAux(name, parser, year, subject, teacher)
                        subject = triple.first
                        teacher = triple.second
                        year = triple.third
                    }
                    XmlResourceParser.END_TAG -> {
                        name = parser.name
                        Log.d("XML", name)
                        if (name == "subject")
                            subject?.let { year?.subjects?.add(it) }
                        else if (name == "year") {
                            year?.let { years.add(it) }
                        }
                    }
                }
                eventType = parser.next()
            }catch (e: XmlPullParserException) {
                Log.e("XML", e.printStackTrace().toString())
            }
        }

        return years
    }

    private fun startTagAux(
        name: String,
        parser: XmlPullParser,
        year: Year?,
        subject: Subject?,
        teacher: Teacher?
    ): Triple<Subject?, Teacher?, Year?> {
        var year1 = year
        var subject1 = subject
        var teacher1 = teacher
        if (name == "year") {
            val yearNumber = parser.getAttributeValue(null, "number")
            year1 = Year(yearNumber.toInt(), mutableListOf())
        } else if (name == "subject") {
            val subjectName = parser.getAttributeValue(null, "name")
            subject1 = Subject(subjectName, mutableListOf())
        } else if (name == "teacher") {
            teacher1 = Teacher("", "")
        } else {
            if (name == "name")
                teacher1?.name = parser.nextText()
            if (name == "email") {
                teacher1?.email = parser.nextText()
                teacher1?.let { subject1?.teachers?.add(it) }
            }
        }
        return Triple(subject1, teacher1, year1)
    }
}

