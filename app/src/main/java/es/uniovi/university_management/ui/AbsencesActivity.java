package es.uniovi.university_management.ui;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Absence;
import es.uniovi.university_management.ui.adapters.AbsencesAdapter;
import es.uniovi.university_management.ui.dialog.DatePickerFragment;
import es.uniovi.university_management.util.DateParser;

public class AbsencesActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private int sectionSelected;
    private Date newDate;
    private List<Absence> absencesTeoria;
    private List<Absence> absencesPractica;
    private List<Absence> absencesSeminario;
    AbsencesAdapter adapterTeoria;
    AbsencesAdapter adapterPractica;
    AbsencesAdapter adapterSeminario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absences);
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

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //TODO leer de la BBDD. Se leen todas y luego según su type se añaden a una lista u otra
        absencesTeoria = new ArrayList<>();
        absencesPractica = new ArrayList<>();
        absencesSeminario = new ArrayList<>();
        Absence a1 = new Absence(calendar, 2, false);
        Absence a2 = new Absence(calendar, 3, true);
        absencesPractica.add(a1);
        absencesSeminario.add(a2);
        //fin hardcodeado
        getSupportActionBar().setTitle("Ausencias de " + subjectName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterTeoria = new AbsencesAdapter(absencesTeoria, getApplicationContext(), AbsencesActivity.this);
        adapterPractica = new AbsencesAdapter(absencesPractica, getApplicationContext(), AbsencesActivity.this);
        adapterSeminario = new AbsencesAdapter(absencesSeminario, getApplicationContext(), AbsencesActivity.this);

        listaTeoriaView.setAdapter(adapterTeoria);
        listaPracticaView.setAdapter(adapterPractica);
        listaSeminarioView.setAdapter(adapterSeminario);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSectionDialog();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                        showDatePickerDialog();
                    }
                })
                .show();
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //TODO crear el objeto timeSUbject
                String diaFormateado = (day < 10) ? String.valueOf(CERO + day) : String.valueOf(day);
                // +1 because January is zero
                String mesFormateado = (month + 1 < 10) ? String.valueOf(CERO + (month + 1)) : String.valueOf(month + 1);
                String date = diaFormateado + "/" + mesFormateado + "/" + year;
                Log.i("Selección de la fecha", diaFormateado + " / " + mesFormateado + " / " + year);
                DateParser parser = new DateParser();
                newDate = parser.stringToDate(date);
                saveDate(sectionSelected, newDate);
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");

    }

    private void saveDate(int sectionSelected, Date newDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        switch (sectionSelected) {
            //TODO guardar en la base de datos
            case 1:
                absencesTeoria.add(new Absence(calendar, 1, false));
                adapterTeoria.notifyDataSetChanged();
                break;
            case 2:
                absencesPractica.add(new Absence(calendar, 1, false));
                adapterPractica.notifyDataSetChanged();
                break;
            case 3:
                absencesSeminario.add(new Absence(calendar, 1, false));
                adapterSeminario.notifyDataSetChanged();
                break;
            default:
                //assume you only have 3
                throw new IllegalArgumentException();
        }
    }

    private void manageLayout(RecyclerView recycler, RecyclerView.LayoutManager mLayoutManager) {

        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

}
