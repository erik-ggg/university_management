package es.uniovi.university_management.ui.main;


import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Test;
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.model.SectionTimeEntity;
import es.uniovi.university_management.model.TestEntity;


public class SeminaryFragment extends AbstractFragment {

    private List<Date> seminaryDate = new ArrayList<>();
    private final Long[] seminaryId = new Long[1];


    public SeminaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SeminaryFragment newInstance(String subject) {
        SeminaryFragment fragment = new SeminaryFragment();
        fragment.subjectName = subject;
        return fragment;
    }


    @Override
    void cargaBD(View rootView) {

        Thread t1 = new Thread() {
            @Override
            public void run() {
                db[0] = AppDatabase.Companion.getAppDatabase(getContext());
                Integer id = db[0].subjectDao().getByName(subjectName).getId();
                if (id != null) {
                    subjectId[0] = Long.valueOf(id);
                    seminaryId[0] = db[0].seminaryDao().getBySubjectId(subjectId[0]).getId();
                }

                List<SectionTimeEntity> seminarySectionsTime = db[0].sectionTimeDao().getBySectionIdAndType(seminaryId[0], 3);
                Date date;
                for (SectionTimeEntity item : seminarySectionsTime) {
                    date = new Date(item.getStartDate());
                    seminaryDate.add(date);
                }
                TextView nextLesson = rootView.findViewById(R.id.textNextLesson);
                nextLesson.setText(getNextLesson(seminaryDate));

                List<TestEntity> seminaryTest = db[0].testDao().getBySectionId(seminaryId[0]);
                String name;
                Double mark;
                for (TestEntity item : seminaryTest) {
                    name = item.getName();
                    mark = item.getMark();
                    if (!isTestInList(name, notas))
                        notas.add(new Test(name, 1, mark));

                }
            }
        };
        t1.start();
    }

    @Override
    protected void insertData(String testDescription, Double d, int tabPosition) {
        Thread t3 = new Thread() {
            public void run() {
                AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                db.testDao().insert(new TestEntity(seminaryId[0], testDescription, d, 3));

            }
        };
        t3.start();
    }


}