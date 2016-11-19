package fr.m1_tlse3.mcs.corpusmic.Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.File;

import fr.m1_tlse3.mcs.corpusmic.CorpusAdapter;
import fr.m1_tlse3.mcs.corpusmic.R;

/**
 * Created by silmathoron on 12/11/2016.
 */

public class CorpusFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_corpus, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final CorpusAdapter adapter = new CorpusAdapter(getActivity());
        setListAdapter(adapter);

        final Button button = (Button) getActivity().findViewById(R.id.deleteAll_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filepath = Environment.getExternalStorageDirectory().getPath()+"/CorpusMic";
                File dir = new File(filepath);
                for(File file: dir.listFiles()) {
                    if (!file.isDirectory()) {
                        file.delete();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
