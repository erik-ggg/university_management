package es.uniovi.university_management.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Office;
import es.uniovi.university_management.classes.Subject;
import es.uniovi.university_management.classes.Teacher;
import es.uniovi.university_management.classes.TimeSubject;
import es.uniovi.university_management.classes.Year;
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.model.OfficeEntity;
import es.uniovi.university_management.model.PracticeEntity;
import es.uniovi.university_management.model.SectionTimeEntity;
import es.uniovi.university_management.model.SeminaryEntity;
import es.uniovi.university_management.model.SubjectEntity;
import es.uniovi.university_management.model.TeacherEntity;
import es.uniovi.university_management.model.TeacherSubjectEntity;
import es.uniovi.university_management.model.TheoryEntity;
import es.uniovi.university_management.parser.CSVReader;
import es.uniovi.university_management.parser.XmlReader;
import es.uniovi.university_management.ui.adapters.SubjectsAdapter;

public class Subjects extends AppCompatActivity {

    private ArrayList<Subject> subjectsAdded;
    private ArrayList<Subject> subjectsToAdd;
    SubjectsAdapter mAdapter;
    private SearchView searchView;
    //esto es para poder crear asignaturas
    ArrayList<Teacher> teachers = new ArrayList<Teacher>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = findViewById(R.id.toolbar_subjects);
        setSupportActionBar(toolbar);

        // TODO: esto deberia ser: el usuario selecciona un xml con los datos a cargar o boton de carga automatica

        subjectsAdded = new ArrayList<>();
        subjectsAdded.addAll(getSavedSubjects());

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
    public void onResume() {
        super.onResume();
        subjectsAdded.addAll(getSavedSubjects());
        mAdapter.notifyDataSetChanged();

    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        subjectsAdded = new ArrayList<>();
//        subjectsAdded.addAll(getSavedSubjects());
//
//        RecyclerView listaAsignaturasView = (RecyclerView) findViewById(R.id.lista_asignaturas);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        listaAsignaturasView.setLayoutManager(mLayoutManager);
//        //listaAsignaturasView.setItemAnimator(new DefaultItemAnimator());
//        //listaAsignaturasView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        listaAsignaturasView.setItemViewCacheSize(subjectsAdded.size());
//
//        mAdapter = new SubjectsAdapter(subjectsAdded, getApplicationContext(), Subjects.this);
//        listaAsignaturasView.setAdapter(mAdapter);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        subjectsAdded = new ArrayList<>();
//        subjectsAdded.addAll(getSavedSubjects());
//
//        RecyclerView listaAsignaturasView = (RecyclerView) findViewById(R.id.lista_asignaturas);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        listaAsignaturasView.setLayoutManager(mLayoutManager);
//        //listaAsignaturasView.setItemAnimator(new DefaultItemAnimator());
//        //listaAsignaturasView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        listaAsignaturasView.setItemViewCacheSize(subjectsAdded.size());
//
//        mAdapter = new SubjectsAdapter(subjectsAdded, getApplicationContext(), Subjects.this);
//        listaAsignaturasView.setAdapter(mAdapter);
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        subjectsAdded = new ArrayList<>();
//        subjectsAdded.addAll(getSavedSubjects());
//
//        RecyclerView listaAsignaturasView = (RecyclerView) findViewById(R.id.lista_asignaturas);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        listaAsignaturasView.setLayoutManager(mLayoutManager);
//        //listaAsignaturasView.setItemAnimator(new DefaultItemAnimator());
//        //listaAsignaturasView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        listaAsignaturasView.setItemViewCacheSize(subjectsAdded.size());
//
//        mAdapter = new SubjectsAdapter(subjectsAdded, getApplicationContext(), Subjects.this);
//        listaAsignaturasView.setAdapter(mAdapter);
//    }

    @Override
    protected void onStop() {
        super.onStop();
        subjectsAdded = new ArrayList<>();
        subjectsAdded.addAll(getSavedSubjects());

        RecyclerView listaAsignaturasView = (RecyclerView) findViewById(R.id.lista_asignaturas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listaAsignaturasView.setLayoutManager(mLayoutManager);
        //listaAsignaturasView.setItemAnimator(new DefaultItemAnimator());
        //listaAsignaturasView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listaAsignaturasView.setItemViewCacheSize(subjectsAdded.size());

        mAdapter = new SubjectsAdapter(subjectsAdded, getApplicationContext(), Subjects.this);
        listaAsignaturasView.setAdapter(mAdapter);
    }

    private List<Subject> getSavedSubjects() {
        final List<SubjectEntity>[] data = new List[]{new ArrayList<>()};
        final List<Subject> subjects = new ArrayList<>();

        Thread t = new Thread() {
            public void run() {
//                AppDatabase db = new AppDatabase_Impl();
                data[0] = AppDatabase.Companion.getAppDatabase(getApplicationContext()).subjectDao().getAll();
                for (SubjectEntity entity : data[0]) {
                    Subject subject = new Subject(entity.getName());
                    subject.setId(entity.getId());
                    subjects.add(subject);
                }
            }
        };
        t.start();
        return subjects;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subjects, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return true;
            }
        });
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

        if (id == R.id.action_addSchedule) {
            loadSchedule();
        }

        if (id == R.id.app_bar_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadSchedule() {
        CSVReader reader = new CSVReader();
        final List<TimeSubject> timeSubjects = reader.readCSV(getApplicationContext());
        Thread t = new Thread() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.Companion.getAppDatabase(getApplicationContext());
                DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy HH.mm");
                List<SectionTimeEntity> sectionTimeEntities = new ArrayList<>();
                Long targetId = -1L;

                for (TimeSubject item: timeSubjects) {
                    SubjectEntity subjectEntity = db.subjectDao().getByName(item.getName());
                    switch (item.getType()) {
                        case 1:
                            targetId = db.theoryDao().getBySubjectId(subjectEntity.getId()).getId();
                            break;
                        case 2:
                            targetId = db.practiceDao().getBySubjectId(subjectEntity.getId()).getId();
                            break;
                        case 3:
                            targetId = db.seminaryDao().getBySubjectId(subjectEntity.getId()).getId();
                            break;
                    }
                    for (int i = 0; i < item.getStartDate().size(); i++) {
                        String date = item.getStartDate().get(i);
                        String time = item.getStartTime().get(i);
                        date += " " + time;
                        try {
                            Date resDate = formatter.parse(date);
                            db.sectionTimeDao().insert(new SectionTimeEntity(targetId, resDate.getTime(), resDate.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        t.start();
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    public void goToSettings(MenuItem item) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void selectSubjects() {
        subjectsToAdd = new ArrayList<>();
        // Leemos los datos del xml
        XmlReader xmlReader = new XmlReader();
        List<Year> data = xmlReader.readAndParse(getApplicationContext());
        for (Year year : data) {
            subjectsToAdd.addAll(year.getSubjects());
        }
        // Cargamos los nombres de las asignaturas
        final String[] listItems = new String[subjectsToAdd.size()];
        for (int i = 0; i < subjectsToAdd.size(); i++)
            listItems[i] = subjectsToAdd.get(i).getName();
        final boolean[] checkedItems = new boolean[subjectsToAdd.size()]; //this will checked the items when user open the dialog
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
                        saveInDB(subjectsAdded);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveInDB(final List<Subject> subjects) {
        if (subjectsAdded.size() > 0) {
            final List<SubjectEntity>[] all = new List[]{new ArrayList<>()};
            Thread t = new Thread() {
                    public void run() {
                        AppDatabase db = AppDatabase.Companion.getAppDatabase(getApplicationContext());
//                    db.subjectDao().insertSubjects(subjects, db);
                        List<Long> teachersId = new ArrayList<>();
                        Long officeId = 1L;
                        for (Subject subject : subjects) {
                            for (Teacher teacher : subject.getTeachers()) {
                                Log.d("Teacher", teacher.toString());
                                Office office = teacher.getOffice();
                                if (office != null) {
                                    officeId = db.officeDao().insert(new OfficeEntity(office.getBuilding(),
                                            office.getFloor(), office.getDoor(), office.getCoordinates()));
                                    Log.d("OfficeId", officeId.toString());
                                    teachersId.add(db.teacherDao().insert(new TeacherEntity(teacher.getName(),
                                            teacher.getEmail(), officeId)));
                                }
                            }
                            Long subjectId = db.subjectDao().insert(new SubjectEntity(subject.getName(), 1L));
                            db.theoryDao().insert(new TheoryEntity(subjectId));
                            db.practiceDao().insert(new PracticeEntity(subjectId));
                            db.seminaryDao().insert(new SeminaryEntity(subjectId));
                            Log.d("SubjectId", subjectId.toString());
                            for (Long teacherId : teachersId) {
                                db.teacherSubjectDao().insert(new TeacherSubjectEntity(teacherId, subjectId));
                            }
                            teachersId.clear();
                        }
                        all[0] = db.subjectDao().getAll();
                    }
            };
            t.start();
        }
    }


}