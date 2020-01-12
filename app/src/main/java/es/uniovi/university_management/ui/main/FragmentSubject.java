package es.uniovi.university_management.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import es.uniovi.university_management.model.TestEntity;
import es.uniovi.university_management.ui.AbsencesActivity;
import es.uniovi.university_management.ui.TeachersActivity;
import es.uniovi.university_management.ui.TimeTableActivity;
import es.uniovi.university_management.ui.adapters.SubjectAdapter;

public class FragmentSubject extends Fragment {

    private int section;
    private String subjectName;
    private String testDescription = "";
    private String testMark = "";
    private int sectionSelected;
    private ArrayList<Test> notasTeoria;
    private ArrayList<Test> notasPractica;
    private ArrayList<Test> notasSeminario;
    private SubjectAdapter adapterTeoria;
    private SubjectAdapter adapterPracticas;
    private SubjectAdapter adapterSeminario;

    private final AppDatabase[] db = new AppDatabase[1];
    private final Long[] subjectId = new Long[1];
    private final Long[] theoryId = new Long[1];
    private final Long[] practiceId = new Long[1];
    private final Long[] seminaryId = new Long[1];

    public static FragmentSubject newInstance(int sectionNumber, String subject) {
        FragmentSubject fragment = new FragmentSubject();
        fragment.section = sectionNumber;
        fragment.subjectName = subject;
        return fragment;
    }

    public FragmentSubject() {
        notasTeoria = new ArrayList<Test>();
        notasPractica = new ArrayList<Test>();
        notasSeminario = new ArrayList<Test>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        RecyclerView recyclerNotas = rootView.findViewById(R.id.notasRecycler);

        recyclerNotas.setLayoutManager(new LinearLayoutManager(getActivity()));

        setHasOptionsMenu(true);
        //getActivity().invalidateOptionsMenu();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_subject);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("menu añadir nota", "he pasado por aquí");
                int id = item.getItemId();
                if (id == R.id.add_note) {
                    showSectionDialog();
                    return false;
                }

                if (id == R.id.contact_teachers) {
                    return createIntent(TeachersActivity.class);
                }

                if (id == R.id.absence_control) {
                    return createIntent(AbsencesActivity.class);
                }

                if (id == R.id.manage_timetable) {
                    return createIntent(TimeTableActivity.class);
                }


                return true;
            }
        });


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
                    practiceId[0] = db[0].theoryDao().getBySubjectId(subjectId[0]).getId();
                    seminaryId[0] = db[0].theoryDao().getBySubjectId(subjectId[0]).getId();
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

                List<TestEntity> theoryTest = db[0].testDao().getBySectionId(theoryId[0]);
                String name;
                Double mark;
                for (TestEntity item : theoryTest) {
                    name = item.getName();
                    mark = item.getMark();
                    if (!isTestInList(name, notasTeoria))
                        notasTeoria.add(new Test(name, 1, mark));
                }

                List<TestEntity> practiseTest = db[0].testDao().getBySectionId(practiceId[0]);
                for (TestEntity item : practiseTest) {
                    name = item.getName();
                    mark = item.getMark();
                    if (!isTestInList(name, notasPractica))
                        notasPractica.add(new Test(name, 2, mark));
                }

                List<TestEntity> seminaryTest = db[0].testDao().getBySectionId(seminaryId[0]);
                for (TestEntity item : seminaryTest) {
                    name = item.getName();
                    mark = item.getMark();
                    if (!isTestInList(name, notasSeminario))
                        notasSeminario.add(new Test(name, 3, mark));
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
        //notasTeoria = new ArrayList<Test>();
        //notasPractica = new ArrayList<Test>();
        //notasSeminario = new ArrayList<Test>();
        //notasTeoria.add(new Test("examen 1",1, 5.2));
        //notasPractica.add(new Test("examen 2", 2,8.7));
        //fin hardcoding

        RecyclerView listaNotasView = (RecyclerView) rootView.findViewById(R.id.notasRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        listaNotasView.setLayoutManager(mLayoutManager);
        listaNotasView.setItemAnimator(new DefaultItemAnimator());
        listaNotasView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));


        adapterTeoria = new SubjectAdapter(notasTeoria, rootView.getContext());
        adapterPracticas = new SubjectAdapter(notasPractica, rootView.getContext());
        adapterSeminario = new SubjectAdapter(notasSeminario, rootView.getContext());
        switch (section) {
            case 0:
                listaNotasView.setAdapter(adapterTeoria);
                break;
            case 1:
                listaNotasView.setAdapter(adapterPracticas);
                break;
            case 2:
                listaNotasView.setAdapter(adapterSeminario);
                break;

        }


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


    private boolean createIntent(Class c) {
        Intent i = new Intent(getActivity(), c);
        i.putExtra("nombreAsignatura", subjectName);
        startActivity(i);
        return true;

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


    private void showSectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[3];

        items[0] = "Teoría";
        items[1] = "Prácticas";
        items[2] = "Seminario";

        builder.setTitle("Elija la sección")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sectionSelected = which + 1;
                        Log.i("Selección de sección", "seleccionado " + items[which]);
                        showInputTextDialog();
                    }
                })
                .show();
    }

    private void showInputTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Descripción del Examen");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        // Set up the inputs
        final EditText input1 = new EditText(getActivity());
        input1.setHint("Descripción del Test");
        input1.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input1);
        final EditText input2 = new EditText(getActivity());
        input2.setHint("Nota");
        layout.addView(input2);

        builder.setView(layout);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                testDescription = input1.getText().toString();
                testMark = input2.getText().toString();
                saveTest(sectionSelected, testDescription, testMark);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveTest(int sectionSelected, String testDescription, String testMark) {
        Double d = Double.parseDouble(testMark);


        switch (sectionSelected) {
            case 1:
                Thread t1 = new Thread() {
                    public void run() {

                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                        db.testDao().insert(new TestEntity(theoryId[0], testDescription, d));
                        notasTeoria.add(new Test(testDescription, 1, d));
                    }
                };
                t1.start();
                adapterTeoria.notifyDataSetChanged();
                break;

            case 2:
                Thread t2 = new Thread() {
                    public void run() {
                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                        db.testDao().insert(new TestEntity(practiceId[0], testDescription, d));
                        notasPractica.add(new Test(testDescription, 2, d));
                    }

                };
                t2.start();
                adapterPracticas.notifyDataSetChanged();
                break;

            case 3:
                Thread t3 = new Thread() {
                    public void run() {
                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                        db.testDao().insert(new TestEntity(seminaryId[0], testDescription, d));
                        notasSeminario.add(new Test(testDescription, 3, d));
                    }

                };
                t3.start();
                adapterSeminario.notifyDataSetChanged();
                break;
            default:
                //assume you only have 3
                throw new IllegalArgumentException();
        }
    }


    private boolean isTestInList(String test, List<Test> lista) {
        boolean isIn = false;

        for (Test item : lista) {
            if (test.equals(item.getName()))
                isIn = true;
        }
        return isIn;
    }

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

