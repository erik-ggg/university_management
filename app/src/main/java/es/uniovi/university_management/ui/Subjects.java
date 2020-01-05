package es.uniovi.university_management.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Office;
import es.uniovi.university_management.classes.Subject;
import es.uniovi.university_management.classes.Teacher;
import es.uniovi.university_management.ui.adapters.SubjectsAdapter;

public class Subjects extends AppCompatActivity {

    private ArrayList<Subject> subjectsAdded;
    private ArrayList<Subject> subjectsToAdd;
    SubjectsAdapter mAdapter;
    //esto es para poder crear asignaturas
    ArrayList<Teacher> teachers = new ArrayList<Teacher>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = findViewById(R.id.toolbar_subjects);
        setSupportActionBar(toolbar);

        // TODO: esto deberia ser: el usuario selecciona un xml con los datos a cargar o boton de carga automatica
        /*CSVReader reader = new CSVReader();
        XmlReader xmlReader = new XmlReader();
        List<Year> data = xmlReader.readAndParse(getApplicationContext());*/

        subjectsAdded = new ArrayList<Subject>();
        //harcodeando las asignaturas de la BBDD
        teachers.add(new Teacher("Pepe", "pepe@uniovi.es", new Office("a", 2, "b", "c")));
        subjectsAdded.add(new Subject("Diseño del Software", teachers));
        subjectsAdded.add(new Subject("CPM", teachers));
        subjectsAdded.add(new Subject("Álgebra", teachers));
        //fin hardcoding

        // Cargamos las asignaturas
        /*for (Year year : data) {
            subjectsToAdd.addAll(year.getSubjects());
        }*/

        RecyclerView listaAsignaturasView = (RecyclerView) findViewById(R.id.lista_asignaturas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listaAsignaturasView.setLayoutManager(mLayoutManager);
        //listaAsignaturasView.setItemAnimator(new DefaultItemAnimator());
        //listaAsignaturasView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listaAsignaturasView.setItemViewCacheSize(subjectsAdded.size());

        mAdapter = new SubjectsAdapter(subjectsAdded, getApplicationContext(), Subjects.this);
        listaAsignaturasView.setAdapter(mAdapter);


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

        if (id == R.id.action_anadir) {
            selectSubjects();
        }

        return super.onOptionsItemSelected(item);
    }


    public void goToSettings(MenuItem item) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void selectSubjects() {
        subjectsToAdd = new ArrayList<Subject>();
        //harcodeando las asignaturas del csv
        subjectsToAdd.add(new Subject("Computabilidad", teachers));
        subjectsToAdd.add(new Subject("Bases de Datos", teachers));
        subjectsToAdd.add(new Subject("Ingeniería de Requisitos", teachers));
        //fin hardcoding
        String[] listItems = new String[subjectsToAdd.size()];
        for (int i = 0; i < subjectsToAdd.size(); i++)
            listItems[i] = subjectsToAdd.get(i).getName();
        boolean[] checkedItems = new boolean[subjectsToAdd.size()]; //this will checked the items when user open the dialog
        for (int i = 0; i < checkedItems.length; i++)
            checkedItems[i] = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(Subjects.this)
                .setTitle("Selecciona las asignaturas")
                .setMultiChoiceItems(listItems, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkedItems[which] = isChecked ? true : false;
                                Log.i("Asignaturas", "Position: " + which + " Value: " + listItems[which] + " State: " + (isChecked ? "checked" : "unchecked"));
                            }
                        })
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < subjectsToAdd.size(); i++) {
                            if (checkedItems[i])
                                subjectsAdded.add(subjectsToAdd.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}