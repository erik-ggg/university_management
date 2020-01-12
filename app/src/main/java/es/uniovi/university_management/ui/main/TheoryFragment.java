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


public class TheoryFragment extends AbstractFragment {

    private List<Date> theoryDate = new ArrayList<>();
    private final Long[] theoryId = new Long[1];


    public TheoryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TheoryFragment newInstance(String subject) {
        TheoryFragment fragment = new TheoryFragment();
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
                    theoryId[0] = db[0].theoryDao().getBySubjectId(subjectId[0]).getId();
                }

                List<SectionTimeEntity> theorySectionsTime = db[0].sectionTimeDao().getBySectionIdAndType(theoryId[0], 1);
                Date date;
                for (SectionTimeEntity item : theorySectionsTime) {
                    date = new Date(item.getStartDate());
                    theoryDate.add(date);
                }
                TextView nextLesson = rootView.findViewById(R.id.textNextLesson);
                nextLesson.setText(getNextLesson(theoryDate));

                List<TestEntity> theoryTest = db[0].testDao().getBySectionId(theoryId[0]);
                String name;
                Double mark;
                for (TestEntity item : theoryTest) {
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
        Thread t1 = new Thread() {
            public void run() {
                AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                db.testDao().insert(new TestEntity(theoryId[0], testDescription, d, 1));
            }
        };
        t1.start();
    }


}