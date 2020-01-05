package es.uniovi.university_management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import es.uniovi.university_management.R;
import es.uniovi.university_management.ui.ui.main.SectionsPagerAdapter;

public class SubjectActivity extends AppCompatActivity {

    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = findViewById(R.id.toolbar_subject);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Bundle param = this.getIntent().getExtras();
        subjectName = param.getString("nombreAsignatura");
        getSupportActionBar().setTitle(subjectName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contact_teachers) {
            return createIntent(TeachersActivity.class);
        }

        if (id == R.id.absence_control) {
            return createIntent(AbsencesActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean createIntent(Class c) {
        Intent i = new Intent(this, c);
        i.putExtra("nombreAsignatura", subjectName);
        startActivity(i);
        return true;
    }
}