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


public class PracticeFragment extends AbstractFragment {

    List<Date> practiceDate = new ArrayList<>();
    private final Long[] practiceId = new Long[1];


    public PracticeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PracticeFragment newInstance(String subject) {
        PracticeFragment fragment = new PracticeFragment();
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
                    practiceId[0] = db[0].practiceDao().getBySubjectId(subjectId[0]).getId();
                }

                List<SectionTimeEntity> practiceSectionsTime = db[0].sectionTimeDao().getBySectionIdAndType(practiceId[0], 2);
                Date date;
                for (SectionTimeEntity item : practiceSectionsTime) {
                    date = new Date(item.getStartDate());
                    practiceDate.add(date);
                }
                TextView nextLesson = rootView.findViewById(R.id.textNextLesson);
                nextLesson.setText(getNextLesson(practiceDate));

                List<TestEntity> practiceTest = db[0].testDao().getBySectionId(practiceId[0]);
                String name;
                Double mark;
                for (TestEntity item : practiceTest) {
                    name = item.getName();
                    mark = item.getMark();
                    if (!isTestInList(name, notas))
                        notas.add(new Test(name, 2, mark));

                }
            }
        };
        t1.start();
    }

    @Override
    protected void insertData(String testDescription, Double d, int tabPosition) {
        Thread t2 = new Thread() {
            public void run() {
                AppDatabase db = AppDatabase.Companion.getAppDatabase(getActivity());
                db.testDao().insert(new TestEntity(practiceId[0], testDescription, d, 2));

            }
        };
        t2.start();
    }


}