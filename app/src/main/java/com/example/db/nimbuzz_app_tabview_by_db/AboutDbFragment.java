package com.example.db.nimbuzz_app_tabview_by_db;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class AboutDbFragment extends Fragment {

    private ImageView dbimg;
    public AboutDbFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_db, container, false);

        dbimg = (ImageView) v.findViewById(R.id.imageView);
        dbimg.setImageResource(R.drawable.dbhere2);
        return v;
    }

}
