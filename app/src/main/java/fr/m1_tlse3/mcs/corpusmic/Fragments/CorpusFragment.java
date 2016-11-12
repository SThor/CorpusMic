package fr.m1_tlse3.mcs.corpusmic.Fragments;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import fr.m1_tlse3.mcs.corpusmic.CorpusAdapter;
import fr.m1_tlse3.mcs.corpusmic.R;

/**
 * Created by silmathoron on 12/11/2016.
 */

public class CorpusFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_corpus, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CorpusAdapter adapter = new CorpusAdapter(getActivity());
        setListAdapter(adapter);
    }

}
