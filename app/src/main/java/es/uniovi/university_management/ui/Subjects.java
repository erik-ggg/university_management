package es.uniovi.university_management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Office;
import es.uniovi.university_management.classes.Subject;
import es.uniovi.university_management.classes.Teacher;
import es.uniovi.university_management.classes.TimeSubject;
import es.uniovi.university_management.classes.Year;
import es.uniovi.university_management.parser.CSVReader;
import es.uniovi.university_management.parser.XmlReader;
import es.uniovi.university_management.ui.adapters.SubjectsAdapter;

public class Subjects extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = findViewById(R.id.toolbar_subjects);
        setSupportActionBar(toolbar);

        // TODO: esto deberia ser: el usuario selecciona un xml con los datos a cargar o boton de carga automatica
        CSVReader reader = new CSVReader();
        XmlReader xmlReader = new XmlReader();
        List<Year> data = xmlReader.readAndParse(getApplicationContext());
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        // Cargamos las asignaturas
        for (Year year : data) {
            subjects.addAll(year.getSubjects());
        }
        RecyclerView listaAsignaturasView = (RecyclerView) findViewById(R.id.lista_asignaturas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listaAsignaturasView.setLayoutManager(mLayoutManager);
        //listaAsignaturasView.setItemAnimator(new DefaultItemAnimator());
        //listaAsignaturasView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listaAsignaturasView.setItemViewCacheSize(subjects.size());

        SubjectsAdapter mAdapter = new SubjectsAdapter(subjects, getApplicationContext(), Subjects.this);
        listaAsignaturasView.setAdapter(mAdapter);
        /*CardView card_view = (CardView) findViewById(R.id.card_view);

        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Subjects.class);
                //i.putExtra("nombreAsignatura", itemSelected.getText().toString());
                startActivity(i);

            }
        });

        /*final TextView itemSelected = findViewById(R.id.nombre_asignatura);
        itemSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Subjects.class);
                i.putExtra("nombreAsignatura", itemSelected.getText().toString());
                startActivity(i);

            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subjects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToSettings(MenuItem item) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }


}