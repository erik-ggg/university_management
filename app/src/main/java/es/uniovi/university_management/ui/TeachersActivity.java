package es.uniovi.university_management.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Office;
import es.uniovi.university_management.classes.Teacher;
import es.uniovi.university_management.ui.adapters.TeachersAdapter;

public class TeachersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);
        Toolbar toolbar = findViewById(R.id.toolbar_teachers);
        setSupportActionBar(toolbar);

        //harcodeando los profesores
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        teachers.add(new Teacher("Pepe", "profesor1@uniovi.es", new Office("a", 2, "b", "c")));
        teachers.add(new Teacher("Juan", "profesor2@uniovi.es", new Office("c", 3, "d", "c")));
        //fin hardcoding

        RecyclerView listaProfesoresView = (RecyclerView) findViewById(R.id.lista_profesores);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listaProfesoresView.setLayoutManager(mLayoutManager);
        listaProfesoresView.setItemAnimator(new DefaultItemAnimator());
        listaProfesoresView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listaProfesoresView.setItemViewCacheSize(teachers.size());

        TeachersAdapter mAdapter = new TeachersAdapter(teachers, getApplicationContext(), TeachersActivity.this);
        listaProfesoresView.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teachers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_teacher)
            return true;
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
