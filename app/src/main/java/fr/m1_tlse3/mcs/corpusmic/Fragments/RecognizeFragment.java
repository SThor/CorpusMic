package fr.m1_tlse3.mcs.corpusmic.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.m1_tlse3.mcs.corpusmic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecognizeFragment extends Fragment {
    public native int stringFromJNI();
    private static final String TAG = "CorpusMic/Recognize";

    public RecognizeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recognize, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int commandIndex = stringFromJNI(); //fixme by adding call to the c++ function here
                String command = getResources().getStringArray(R.array.commands)[commandIndex];
                TextView textView = (TextView) container.findViewById(R.id.linear).findViewById(R.id.recognized_command);
                textView.setText(command);
            }
        });

        return v;
    }

    static {
        System.loadLibrary("native-lib");
    }

}
