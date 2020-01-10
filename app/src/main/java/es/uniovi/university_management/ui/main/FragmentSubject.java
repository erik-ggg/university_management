package es.uniovi.university_management.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.TimeSubject;

public class FragmentSubject extends Fragment {

    private int section;

    public static FragmentSubject newInstance(int sectionNumber) {
        FragmentSubject fragment = new FragmentSubject();
        fragment.section = sectionNumber;
        return fragment;
    }

    public FragmentSubject() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        RecyclerView recyclerNotas = (RecyclerView) rootView.findViewById(R.id.notasRecycler);

        recyclerNotas.setLayoutManager(new LinearLayoutManager(getActivity()));

        //harcodeando los horarios

        ArrayList startDates1 = new ArrayList<>();
        startDates1.add("07/01/2020");
        startDates1.add("10/01/2020");
        startDates1.add("14/01/2020");
        startDates1.add("17/01/2020");
        ArrayList startTimes1 = new ArrayList<>();
        startTimes1.add("9.00");
        startTimes1.add("10.00");
        startTimes1.add("9.00");
        startTimes1.add("10.00");
        ArrayList startDates2 = new ArrayList<>();
        startDates2.add("08/01/2020");
        startDates2.add("11/01/2020");
        startDates2.add("15/01/2020");
        startDates2.add("18/01/2020");
        ArrayList startTimes2 = new ArrayList<>();
        startTimes2.add("9.00");
        startTimes2.add("10.00");
        startTimes2.add("9.00");
        startTimes2.add("10.00");
        ArrayList startDates3 = new ArrayList<>();
        startDates3.add("08/11/2019");
        startDates3.add("11/11/2019");
        startDates3.add("15/11/2019");
        startDates3.add("18/11/2019");
        ArrayList startTimes3 = new ArrayList<>();
        startTimes3.add("14.00");
        startTimes3.add("15.00");
        startTimes3.add("14.00");
        startTimes3.add("15.00");
        TimeSubject horarioTeoria = new TimeSubject("Asignatura", 1, startDates1, startTimes1);
        TimeSubject horarioPracticas = new TimeSubject("Asignatura", 2, startDates2, startTimes2);
        TimeSubject horarioSeminarios = new TimeSubject("Asignatura", 3, startDates3, startTimes3);
        //fin hardcoding

        TextView nextLesson = rootView.findViewById(R.id.textNextLesson);
        switch (section) {
            case 0:
                nextLesson.setText(getNextLesson(horarioTeoria));
                break;
            case 1:
                nextLesson.setText(getNextLesson(horarioPracticas));
                break;
            case 2:
                nextLesson.setText(getNextLesson(horarioSeminarios));
                break;

        }


        return rootView;
    }

    private String getNextLesson(TimeSubject horario) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        Date fechaActual = new Date();
        Date fechaHorario = null;
        List<String> date = horario.getStartDate();
        List<String> hour = horario.getStartTime();
        String temporal = "";
        for (int i = 0; i < date.size(); i++) {
            if (hour.get(i).length() == 4)
                temporal = "0" + hour.get(i);
            else
                temporal = hour.get(i);
            try {
                fechaHorario = df.parse(date.get(i) + " " + temporal);
                if (fechaHorario.compareTo(fechaActual) > 0)
                    return (df.format(fechaHorario) + " - " + hour.get(i));
            } catch (ParseException e) {
                Log.i("Fechas", "No se ha podido parsear la fecha.");
                e.printStackTrace();
            }

        }

        return ("No hay más clases para esta asignatura");
    }
}
