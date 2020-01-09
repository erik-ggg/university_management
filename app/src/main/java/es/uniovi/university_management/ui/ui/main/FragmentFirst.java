package es.uniovi.university_management.ui.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import es.uniovi.university_management.R;

public class FragmentFirst extends Fragment {
    public static FragmentFirst newInstance(int sectionNumber) {
        FragmentFirst fragment = new FragmentFirst();
        return fragment;
    }

    public FragmentFirst() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        return rootView;
    }
}
