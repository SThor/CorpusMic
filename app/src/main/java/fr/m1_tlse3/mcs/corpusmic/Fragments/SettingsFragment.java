package fr.m1_tlse3.mcs.corpusmic.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.app.Fragment;

import fr.m1_tlse3.mcs.corpusmic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
