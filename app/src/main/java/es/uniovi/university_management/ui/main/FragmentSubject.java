package es.uniovi.university_management.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Test;
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.model.SectionTimeEntity;
import es.uniovi.university_management.ui.adapters.SubjectAdapter;

public class FragmentSubject extends Fragment {

    private int section;
    private String subjectName;

    public static FragmentSubject newInstance(int sectionNumber, String subject) {
        FragmentSubject fragment = new FragmentSubject();
        fragment.section = sectionNumber;
        fragment.subjectName = subject;
        return fragment;
    }

    public FragmentSubject() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        RecyclerView recyclerNotas = rootView.findViewById(R.id.notasRecycler);

        recyclerNotas.setLayoutManager(new LinearLayoutManager(getActivity()));

        final AppDatabase[] db = new AppDatabase[1];
        final Long[] subjectId = new Long[1];
        final Long[] theoryId = new Long[1];
        final Long[] practiceId = new Long[1];
        final Long[] seminaryId = new Long[1];

        final List<Date> theoryDate = new ArrayList<>();
        final List<Date> practiceDate = new ArrayList<>();
        final List<Date> seminaryDate = new ArrayList<>();


        // Carga desde base de datos
        Thread t1 = new Thread() {
            @Override
            public void run() {
                db[0] = AppDatabase.Companion.getAppDatabase(getContext());
                Integer id = db[0].subjectDao().getByName(subjectName).getId();
                if (id != null) {
                    subjectId[0] = Long.valueOf(id);
                    theoryId[0] = db[0].theoryDao().getBySubjectId(subjectId[0]).getId();
                    practiceId[0] = db[0].practiceDao().getBySubjectId(subjectId[0]).getId();
                    seminaryId[0] = db[0].seminaryDao().getBySubjectId(subjectId[0]).getId();
                }

                List<SectionTimeEntity> theorySectionsTime = db[0].sectionTimeDao().getBySectionId(theoryId[0]);
                Date date;
                for (SectionTimeEntity item : theorySectionsTime) {
                    date = new Date(item.getStartDate());
                    theoryDate.add(date);
                }

                List<SectionTimeEntity> practiceSectionsTime = db[0].sectionTimeDao().getBySectionId(practiceId[0]);
                for (SectionTimeEntity item : practiceSectionsTime) {
                    date = new Date(item.getStartDate());
                    practiceDate.add(date);
                }

                List<SectionTimeEntity> seminarySectionsTime = db[0].sectionTimeDao().getBySectionId(seminaryId[0]);
                for (SectionTimeEntity item : seminarySectionsTime) {
                    date = new Date(item.getStartDate());
                    seminaryDate.add(date);
                }

                TextView nextLesson = rootView.findViewById(R.id.textNextLesson);
                switch (section) {
                    case 0:
                        nextLesson.setText(getNextLesson(theoryDate));
                        break;
                    case 1:
                        nextLesson.setText(getNextLesson(practiceDate));
                        break;
                    case 2:
                        nextLesson.setText(getNextLesson(seminaryDate));
                        break;

                }
            }
        };
        t1.start();


        //harcodeando las notas
        ArrayList<Test> notas = new ArrayList<Test>();
        notas.add(new Test("examen 1", 5.2));
        notas.add(new Test("examen 2", 8.7));
        //fin hardcoding

        RecyclerView listaNotasView = (RecyclerView) rootView.findViewById(R.id.notasRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        listaNotasView.setLayoutManager(mLayoutManager);
        listaNotasView.setItemAnimator(new DefaultItemAnimator());
        listaNotasView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));


        SubjectAdapter mAdapter = new SubjectAdapter(notas, rootView.getContext());
        listaNotasView.setAdapter(mAdapter);

        //harcodeando los horarios

//        ArrayList startDates1 = new ArrayList<>();
//        startDates1.add("07/01/2020");
//        startDates1.add("10/01/2020");
//        startDates1.add("14/01/2020");
//        startDates1.add("17/01/2020");
//        ArrayList startTimes1 = new ArrayList<>();
//        startTimes1.add("9.00");
//        startTimes1.add("10.00");
//        startTimes1.add("9.00");
//        startTimes1.add("10.00");
//        ArrayList startDates2 = new ArrayList<>();
//        startDates2.add("08/01/2020");
//        startDates2.add("11/01/2020");
//        startDates2.add("15/01/2020");
//        startDates2.add("18/01/2020");
//        ArrayList startTimes2 = new ArrayList<>();
//        startTimes2.add("9.00");
//        startTimes2.add("10.00");
//        startTimes2.add("9.00");
//        startTimes2.add("10.00");
//        ArrayList startDates3 = new ArrayList<>();
//        startDates3.add("08/11/2019");
//        startDates3.add("11/11/2019");
//        startDates3.add("15/11/2019");
//        startDates3.add("18/11/2019");
//        ArrayList startTimes3 = new ArrayList<>();
//        startTimes3.add("14.00");
//        startTimes3.add("15.00");
//        startTimes3.add("14.00");
//        startTimes3.add("15.00");
//        TimeSubject horarioTeoria = new TimeSubject("Asignatura", 1, theorySectionsTime.get, startTimes1);
//        TimeSubject horarioPracticas = new TimeSubject("Asignatura", 2, startDates2, startTimes2);
//        TimeSubject horarioSeminarios = new TimeSubject("Asignatura", 3, startDates3, startTimes3);
        //fin hardcoding
        return rootView;
    }

    private String getNextLesson(List<Date> dates) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        Date fechaActual = new Date();
        for (Date item : dates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item);
            if (item.compareTo(fechaActual) > 0) {
                return df.format(item);
            }
        }
        return "Clases finalizadas.";
    }

//    private String getNextLesson(TimeSubject horario) {
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm");
//        Date fechaActual = new Date();
//        Date fechaHorario = null;
//        List<String> date = horario.getStartDate();
//        List<String> hour = horario.getStartTime();
//        String temporal = "";
//        for (int i = 0; i < date.size(); i++) {
//            if (hour.get(i).length() == 4)
//                temporal = "0" + hour.get(i);
//            else
//                temporal = hour.get(i);
//            try {
//                fechaHorario = df.parse(date.get(i) + " " + temporal);
//                if (fechaHorario.compareTo(fechaActual) > 0)
//                    return (df.format(fechaHorario) + " - " + hour.get(i));
//            } catch (ParseException e) {
//                Log.e("Fechas", "No se ha podido parsear la fecha.");
//                e.printStackTrace();
//            }
//
//        }
//
//        return ("No hay más clases para esta asignatura");
//    }
}
