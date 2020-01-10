package es.uniovi.university_management.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.TimeSubject;
import es.uniovi.university_management.ui.adapters.DatesAdapter;

public class TimeTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle param = this.getIntent().getExtras();
        String subjectName = param.getString("nombreAsignatura");

        RecyclerView listaTeoriaView = (RecyclerView) findViewById(R.id.recycler_dates_theory);
        RecyclerView listaPracticaView = (RecyclerView) findViewById(R.id.recycler_dates_practic);
        RecyclerView listaSeminarioView = (RecyclerView) findViewById(R.id.recycler_dates_sem);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getApplicationContext());

        manageLayout(listaTeoriaView, mLayoutManager);
        manageLayout(listaPracticaView, mLayoutManager2);
        manageLayout(listaSeminarioView, mLayoutManager3);

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

        DatesAdapter adapterTeoria = new DatesAdapter(horarioTeoria, getApplicationContext());
        DatesAdapter adapterPractica = new DatesAdapter(horarioPracticas, getApplicationContext());
        DatesAdapter adapterSeminario = new DatesAdapter(horarioSeminarios, getApplicationContext());
        listaTeoriaView.setAdapter(adapterTeoria);
        listaPracticaView.setAdapter(adapterPractica);
        listaSeminarioView.setAdapter(adapterSeminario);

        getSupportActionBar().setTitle("Horario de " + subjectName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void manageLayout(RecyclerView recycler, RecyclerView.LayoutManager mLayoutManager) {

        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

}
