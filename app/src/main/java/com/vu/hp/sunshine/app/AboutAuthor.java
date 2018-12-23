package com.vu.hp.sunshine.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAuthor extends Fragment {

    static AboutAuthor instances;

    public static AboutAuthor getInstances() {
        if (instances == null){
            instances = new AboutAuthor();
        }
        return instances;
    }

    public AboutAuthor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_author, container, false);
    }

}
