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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Test;
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.ui.AbsencesActivity;
import es.uniovi.university_management.ui.TeachersActivity;
import es.uniovi.university_management.ui.TimeTableActivity;
import es.uniovi.university_management.ui.adapters.SubjectAdapter;

public abstract class AbstractFragment extends Fragment {

    private int section;
    protected String subjectName;
    private String testDescription = "";
    private String testMark = "";
    private int sectionSelected;
    protected ArrayList<Test> notas = new ArrayList<Test>();
    private SubjectAdapter mAdapter;

    protected final AppDatabase[] db = new AppDatabase[1];
    protected final Long[] subjectId = new Long[1];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_subject, container, false);
        cargaBD(rootView);

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
                    showInputTextDialog();
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


        RecyclerView listaNotasView = (RecyclerView) rootView.findViewById(R.id.notasRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        listaNotasView.setLayoutManager(mLayoutManager);
        listaNotasView.setItemAnimator(new DefaultItemAnimator());
        listaNotasView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));


        mAdapter = new SubjectAdapter(notas, rootView.getContext());
        listaNotasView.setAdapter(mAdapter);

        return rootView;
    }

    abstract void cargaBD(View rootView);


    private boolean createIntent(Class c) {
        Intent i = new Intent(getActivity(), c);
        i.putExtra("nombreAsignatura", subjectName);
        startActivity(i);
        return true;

    }

    protected String getNextLesson(List<Date> dates) {
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


    /*private void showSectionDialog() {
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
    }*/

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
        notas.add(new Test(testDescription, 1, d));
        mAdapter.notifyDataSetChanged();
        TabLayout tabs = getActivity().findViewById(R.id.tabs);
        int tabPosition = tabs.getSelectedTabPosition();
        insertData(testDescription, d, tabPosition);


    }

    protected abstract void insertData(String testDescription, Double d, int tabPosition);


    protected boolean isTestInList(String test, List<Test> lista) {
        boolean isIn = false;

        for (Test item : lista) {
            if (test.equals(item.getName()))
                isIn = true;
        }
        return isIn;
    }

}

