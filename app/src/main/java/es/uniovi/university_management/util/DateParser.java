package es.uniovi.university_management.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {

    private static final String ZERO = "0";

    public static String LongToString(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        Date aux = new Date(date);
        return formatter.format(aux);
    }

    public Date stringToDate(String date, String time) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        Date fechaHorario = null;
        String temporal = "";
        if (time.length() == 4)
            temporal = ZERO + time;
        else
            temporal = time;
        try {
            fechaHorario = df.parse(date + " " + temporal);
        } catch (ParseException e) {
            Log.i("Fechas", "No se ha podido parsear la fecha.");
            e.printStackTrace();
        }
        return fechaHorario;
    }

    public Date stringToDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaHorario = null;
        try {
            fechaHorario = df.parse(date);
        } catch (ParseException e) {
            Log.i("Fechas", "No se ha podido parsear la fecha.");
            e.printStackTrace();
        }
        return fechaHorario;
    }

    public String dateToString(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        return df.format(d);
    }

    public String dateToStringWithoutHour(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(d);
    }


}
