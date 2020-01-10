package es.uniovi.university_management.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

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

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.TimeSubject;
import es.uniovi.university_management.ui.adapters.DatesAdapter;
import es.uniovi.university_management.ui.dialog.DatePickerFragment;

public class TimeTableActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private int sectionSelected;
    private String newDate;
    private String newHour;
    public final Calendar c = Calendar.getInstance();
    private TimeSubject horarioTeoria;
    private TimeSubject horarioPracticas;
    private TimeSubject horarioSeminarios;
    private DatesAdapter adapterTeoria;
    private DatesAdapter adapterPractica;
    private DatesAdapter adapterSeminario;


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
        //TODO cargar los horarios desde la base de datos

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
        horarioTeoria = new TimeSubject("Asignatura", 1, startDates1, startTimes1);
        horarioPracticas = new TimeSubject("Asignatura", 2, startDates2, startTimes2);
        horarioSeminarios = new TimeSubject("Asignatura", 3, startDates3, startTimes3);
        //fin hardcoding

        adapterTeoria = new DatesAdapter(horarioTeoria, getApplicationContext(), TimeTableActivity.this);
        adapterPractica = new DatesAdapter(horarioPracticas, getApplicationContext(), TimeTableActivity.this);
        adapterSeminario = new DatesAdapter(horarioSeminarios, getApplicationContext(), TimeTableActivity.this);
        listaTeoriaView.setAdapter(adapterTeoria);
        listaPracticaView.setAdapter(adapterPractica);
        listaSeminarioView.setAdapter(adapterSeminario);

        getSupportActionBar().setTitle("Horario de " + subjectName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSectionDialog();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
                newDate = diaFormateado + "/" + mesFormateado + "/" + year;
                Log.i("Selección de la fecha", diaFormateado + " / " + mesFormateado + " / " + year);
                showTimePickerDialog();
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);

                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                //TODO crear la hora

                Log.i("Selección de hora", horaFormateada + ":" + minutoFormateado);
                newHour = horaFormateada + "." + minutoFormateado;
                saveDate(sectionSelected, newDate, newHour);

            }

        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

        recogerHora.show();
    }

    private void saveDate(int sectionSelected, String newDate, String newHour) {
        switch (sectionSelected) {
            //TODO guardar en la base de datos
            case 1:
                horarioTeoria.getStartDate().add(newDate);
                horarioTeoria.getStartTime().add(newHour);
                adapterTeoria.notifyDataSetChanged();
                break;
            case 2:
                horarioPracticas.getStartDate().add(newDate);
                horarioPracticas.getStartTime().add(newHour);
                adapterPractica.notifyDataSetChanged();
                break;
            case 3:
                horarioSeminarios.getStartDate().add(newDate);
                horarioSeminarios.getStartTime().add(newHour);
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
