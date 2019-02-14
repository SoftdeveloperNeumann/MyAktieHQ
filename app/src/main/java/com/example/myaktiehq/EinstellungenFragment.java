package com.example.myaktiehq;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;


public class EinstellungenFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        return true;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
        Preference aktienlistePref = findPreference(getString(R.string.preference_aktienliste_key));
        aktienlistePref.setOnPreferenceChangeListener((this));

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(),"");
        onPreferenceChange(aktienlistePref,gespeicherteAktienliste);

    }
}
