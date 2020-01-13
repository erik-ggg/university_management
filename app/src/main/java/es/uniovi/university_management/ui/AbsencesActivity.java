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
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.model.AbsenceEntity;
import es.uniovi.university_management.repositories.AbsencesRepository;
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
    String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absences);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle param = this.getIntent().getExtras();
        subjectName = param.getString("nombreAsignatura");

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

        absencesTeoria = new ArrayList<>();
        absencesPractica = new ArrayList<>();
        absencesSeminario = new ArrayList<>();

        Thread t = new Thread() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.Companion.getAppDatabase(getApplicationContext());
                Long id = Long.valueOf(db.subjectDao().getByName(subjectName).getId());
                List<AbsenceEntity> absences = db.absenceDao().getBySubjectId(id);
                for (AbsenceEntity absence : absences) {
                    int type = absence.getSectionType();
                    if (type == 1)
                        absencesTeoria.add(new Absence(new Date(absence.getDate()), 1, absence.isAutomatic()));
                    else if (type == 2)
                        absencesPractica.add(new Absence(new Date(absence.getDate()), 2, absence.isAutomatic()));
                     else
                        absencesSeminario.add(new Absence(new Date(absence.getDate()), 3, absence.isAutomatic()));
                }
            }
        };
        t.start();

        getSupportActionBar().setTitle("Ausencias de " + subjectName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterTeoria = new AbsencesAdapter(subjectName, absencesTeoria, getApplicationContext(), AbsencesActivity.this);
        adapterPractica = new AbsencesAdapter(subjectName, absencesPractica, getApplicationContext(), AbsencesActivity.this);
        adapterSeminario = new AbsencesAdapter(subjectName, absencesSeminario, getApplicationContext(), AbsencesActivity.this);

        listaTeoriaView.setAdapter(adapterTeoria);
        listaPracticaView.setAdapter(adapterPractica);
        listaSeminarioView.setAdapter(adapterSeminario);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showSectionDialog();
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
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
        AbsencesRepository repository = new AbsencesRepository();
        Absence absence;
        switch (sectionSelected) {
            case 1:
                absence = new Absence(newDate, 2, false);
                repository.addAbsence(subjectName, absence, getApplicationContext());
                absencesTeoria.add(absence);
                adapterTeoria.notifyDataSetChanged();
                break;
            case 2:
                absence = new Absence(newDate, 2, false);
                repository.addAbsence(subjectName, absence, getApplicationContext());
                absencesPractica.add(absence);
                adapterPractica.notifyDataSetChanged();
                break;
            case 3:
                absence = new Absence(newDate, 3, false);
                repository.addAbsence(subjectName, absence, getApplicationContext());
                absencesSeminario.add(absence);
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
