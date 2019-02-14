package com.example.myaktiehq;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class EinstellungenActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.preferences);
       Preference aktienlistePref = findPreference(getString(R.string.preference_aktienliste_key));
       aktienlistePref.setOnPreferenceChangeListener(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(),"");
        onPreferenceChange(aktienlistePref,gespeicherteAktienliste);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        preference.setSummary(newValue.toString());
        return true;
    }
}
