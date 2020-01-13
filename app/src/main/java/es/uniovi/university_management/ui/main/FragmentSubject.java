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
import android.widget.Toast;

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
import es.uniovi.university_management.classes.Absence;
import es.uniovi.university_management.classes.Practice;
import es.uniovi.university_management.classes.Section;
import es.uniovi.university_management.classes.Seminary;
import es.uniovi.university_management.classes.Test;
import es.uniovi.university_management.classes.Theory;
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.model.AbsenceEntity;
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

    final List<Date> theoryDate = new ArrayList<>();
    final List<Date> practiceDate = new ArrayList<>();
    final List<Date> seminaryDate = new ArrayList<>();

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

        Section section1 = new Section(60);
        Section section2 = new Section(30);
        Section section3 = new Section(10);
        final Theory[] theory = new Theory[1];
        final Practice[] practice = new Practice[1];
        final Seminary[] seminary = new Seminary[1];

        final int[] allowedAbscensesPractice = {0};
        final int[] allowedAbscensesSeminary = {0};

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

                List<SectionTimeEntity> theorySectionsTime = db[0].sectionTimeDao().getBySectionIdAndType(theoryId[0], 1);
                theory[0] = new Theory(section2, theorySectionsTime.size());
                Date date;
                for (SectionTimeEntity item : theorySectionsTime) {
                    date = new Date(item.getStartDate());
                    theoryDate.add(date);
                }

                List<SectionTimeEntity> practiceSectionsTime = db[0].sectionTimeDao().getBySectionIdAndType(practiceId[0], 2);
                practice[0] = new Practice(section1, practiceSectionsTime.size());
                for (SectionTimeEntity item : practiceSectionsTime) {
                    date = new Date(item.getStartDate());
                    practiceDate.add(date);
                }

                List<SectionTimeEntity> seminarySectionsTime = db[0].sectionTimeDao().getBySectionIdAndType(seminaryId[0], 3);
                seminary[0] = new Seminary(section3, seminarySectionsTime.size());
                for (SectionTimeEntity item : seminarySectionsTime) {
                    date = new Date(item.getStartDate());
                    seminaryDate.add(date);
                }

                List<AbsenceEntity> absences = db[0].absenceDao().getBySubjectId(id);
                for (AbsenceEntity absence : absences) {
                    int type = absence.getSectionType();
                    if (type == 1)
                        theory[0].getAbsences().add(new Absence(new Date(absence.getDate()), 1, absence.isAutomatic()));
                    else if (type == 2)
                        practice[0].getAbsences().add(new Absence(new Date(absence.getDate()), 2, absence.isAutomatic()));
                    else
                        seminary[0].getAbsences().add(new Absence(new Date(absence.getDate()), 3, absence.isAutomatic()));
                }

                List<TestEntity> theoryTest = db[0].testDao().getBySectionId(theoryId[0]);
                String name;
                Double mark;
                int type;
                for (TestEntity item : theoryTest) {
                    name = item.getName();
                    mark = item.getMark();
                    type = item.getType();
                    if (type == 1) {
                        if (!isTestInList(name, notasTeoria))
                            notasTeoria.add(new Test(name, 1, mark));
                    }
                    if (type == 2) {
                        if (!isTestInList(name, notasPractica))
                            notasPractica.add(new Test(name, 2, mark));
                    }
                    if (type == 3) {
                        if (!isTestInList(name, notasSeminario))
                            notasSeminario.add(new Test(name, 3, mark));
                    }
                }



                int maxAbscensesPractice = (int) practice[0].getMaxAbscense();
                int maxAbscensesSeminary = (int) seminary[0].getMaxAbscense();
                allowedAbscensesPractice[0] = maxAbscensesPractice - (practice[0].getAbsences().size());
                allowedAbscensesSeminary[0] = maxAbscensesSeminary - (seminary[0].getAbsences().size());

                getActivity().runOnUiThread(() -> {
                    TextView textPractice = rootView.findViewById(R.id.textAusenciasRestantesPracticas);
                    if (!practice[0].isInContinua())
                        textPractice.setText("Has perdido la evaluación continua en prácticas");
                    else {
                        String textoPractica = "Puedes faltar a " + allowedAbscensesPractice[0] + " sesiones más de prácticas";
                        textPractice.setText(textoPractica);
                    }
                    TextView textSeminary = rootView.findViewById(R.id.textAusenciasRestantesSeminario);
                    if (!seminary[0].isInContinua())
                        textSeminary.setText("Has perdido la evaluación continua en seminario");
                    else {
                        String textoSeminario = "Puedes faltar a " + allowedAbscensesSeminary[0] + " sesiones más de seminario";
                        textSeminary.setText(textoSeminario);
                    }
                });

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

//        TextView textPractice = rootView.findViewById(R.id.textAusenciasRestantesPracticas);
//        if (!practice[0].isInContinua())
//            textPractice.setText("Has perdido la evaluación continua en prácticas");
//        else {
//            String textoPractica = "Puedes faltar a " + allowedAbscensesPractice[0] + " sesiones más de prácticas";
//            textPractice.setText(textoPractica);
//        }
//        TextView textSeminary = rootView.findViewById(R.id.textAusenciasRestantesSeminario);
//        if (!seminary[0].isInContinua())
//            textSeminary.setText("Has perdido la evaluación continua en seminario");
//        else {
//            String textoSeminario = "Puedes faltar a " + allowedAbscensesSeminary[0] + " sesiones más de seminario";
//            textSeminary.setText(textoSeminario);
//        }


        RecyclerView listaNotasView = rootView.findViewById(R.id.notasRecycler);
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
                Double markDouble = null;
                try {
                    markDouble = Double.parseDouble(testMark);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast toast1 =
                            Toast.makeText(getActivity(),
                                    "Error: el valor de la nota es incorrecto", Toast.LENGTH_LONG);

                    toast1.show();
                }
                if (markDouble != null)
                    saveTest(sectionSelected, testDescription, markDouble);

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

    private void saveTest(int sectionSelected, String testDescription, Double d) {


        switch (sectionSelected) {
            case 1:
                notasTeoria.add(new Test(testDescription, 1, d));
                adapterTeoria.notifyDataSetChanged();
                Thread t1 = new Thread() {
                    public void run() {
                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                        db.testDao().insert(new TestEntity(theoryId[0], testDescription, d, 1));
                    }
                };
                t1.start();
                break;

            case 2:
                notasPractica.add(new Test(testDescription, 2, d));
                adapterPracticas.notifyDataSetChanged();
                Thread t2 = new Thread() {
                    public void run() {
                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                        db.testDao().insert(new TestEntity(practiceId[0], testDescription, d, 2));

                    }
                };
                t2.start();
                break;

            case 3:
                notasSeminario.add(new Test(testDescription, 3, d));
                adapterSeminario.notifyDataSetChanged();
                Thread t3 = new Thread() {
                    public void run() {
                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                        db.testDao().insert(new TestEntity(seminaryId[0], testDescription, d, 3));

                    }
                };
                t3.start();
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

