package com.example.myaktiehq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class EinstellungenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       addPreferencesFromResource(R.xml.preferences);
//       Preference aktienlistePref = findPreference(getString(R.string.preference_aktienliste_key));
//       aktienlistePref.setOnPreferenceChangeListener(this);
//
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(),"");
//        onPreferenceChange(aktienlistePref,gespeicherteAktienliste);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,new EinstellungenFragment())
        .commit();
    }

//    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//        preference.setSummary(newValue.toString());
//        return true;
//    }
}
