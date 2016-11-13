package fr.m1_tlse3.mcs.corpusmic.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.m1_tlse3.mcs.corpusmic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecognizeFragment extends Fragment {
    public native String stringFromJNI();

    public RecognizeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recognize, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int commandIndex = 2; //fixme by adding call to the c++ function here
                String command = getResources().getStringArray(R.array.commands)[commandIndex];
                TextView textView = (TextView) v.findViewById(R.id.linear).findViewById(R.id.recognized_command);
                textView.setText(command);
            }
        });

        return v;
    }

    static {
        System.loadLibrary("native-lib");
    }

}
