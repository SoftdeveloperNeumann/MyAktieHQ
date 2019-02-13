package com.example.myaktiehq;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AktienlisteFragment extends Fragment {

    private static final String TAG = AktienlisteFragment.class.getSimpleName();
    private ArrayAdapter<String> mAktienListeAdapter;

    public AktienlisteFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Debugmeldung" + getActivity().toString());
        Log.e(TAG, "onCreateView: Errormeldung" );

        String[] aktienlisteArray = {
                "Adidas - Kurs: 73,45 €",
                "Allianz - Kurs: 145,12 €",
                "BASF - Kurs: 84,27 €",
                "Bayer - Kurs: 128,60 €",
                "Beiersdorf - Kurs: 80,55 €",
                "BMW St. - Kurs: 104,11 €",
                "Commerzbank - Kurs: 12,47 €",
                "Continental - Kurs: 209,94 €",
                "Daimler - Kurs: 84,33 €"
        };

        List<String> aktienListe = new ArrayList<>(Arrays.asList(aktienlisteArray));


        mAktienListeAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_aktienliste,
                R.id.list_item_aktienliste_textview,
                aktienListe
        );

        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);
        ListView aktienlisteListView = rootView.findViewById(R.id.listview_aktienliste);
        aktienlisteListView.setAdapter(mAktienListeAdapter);

        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_aktienlistefragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_daten_aktualisieren:
                HoleDatenTask holeDatenTask=new HoleDatenTask();
                holeDatenTask.execute("Aktie");
                Toast.makeText(getContext(), "Daten werden abgefragt", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class HoleDatenTask extends AsyncTask<String,Integer,String[]>{

        private final String TAG = HoleDatenTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... strings) {
            String[] ergebnisArray = new String[20];
            for(int i=0;i<20;i++){
                ergebnisArray[i]= strings[0]+"_"+(i+1);

                if(i%5 == 4){
                    publishProgress(i+1,20);
                }
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    Log.e(TAG, "doInBackground: Error",e );
                }
            }
            return ergebnisArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if( strings != null){
                mAktienListeAdapter.clear();
//                for (String aktienString : strings){
//                    mAktienListeAdapter.add(aktienString);
//                }
                mAktienListeAdapter.addAll(strings);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen", Toast.LENGTH_SHORT).show();
        }
    }
}
