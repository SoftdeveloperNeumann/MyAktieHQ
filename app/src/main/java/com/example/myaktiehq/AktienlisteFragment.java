package com.example.myaktiehq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AktienlisteFragment extends Fragment {

    private static final String TAG = AktienlisteFragment.class.getSimpleName();
    private ArrayAdapter<String> mAktienListeAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String STATE_DATA = "Finanzdaten";
    private ArrayList<String> aktienListe;

    public AktienlisteFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: FragmentView erstellt");


//        String[] aktienlisteArray = {
//                "Adidas - Kurs: 73,45 €",
//                "Allianz - Kurs: 145,12 €",
//                "BASF - Kurs: 84,27 €",
//                "Bayer - Kurs: 128,60 €",
//                "Beiersdorf - Kurs: 80,55 €",
//                "BMW St. - Kurs: 104,11 €",
//                "Commerzbank - Kurs: 12,47 €",
//                "Continental - Kurs: 209,94 €",
//                "Daimler - Kurs: 84,33 €"
//        };

        if (savedInstanceState != null) {
            aktienListe = savedInstanceState.getStringArrayList(STATE_DATA);
            createAdapter();
            Log.d(TAG, "onCreateView: Fragment wieder hergestellt");
        }else{
            aktienListe = new ArrayList<>();
            createAdapter();
            aktualisiereDaten();
        }




        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);
        ListView aktienlisteListView = rootView.findViewById(R.id.listview_aktienliste);
        aktienlisteListView.setAdapter(mAktienListeAdapter);

        aktienlisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aktienInfo = (String) parent.getItemAtPosition(position);
                Intent aktiendetailIntent = new Intent(AktienlisteFragment.this.getActivity(), AktiendetailActivity.class);
                aktiendetailIntent.putExtra(Intent.EXTRA_TEXT, aktienInfo);
                AktienlisteFragment.this.startActivity(aktiendetailIntent);
            }
        });

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout_aktienliste);
        mSwipeRefreshLayout.setOnRefreshListener(() -> aktualisiereDaten());

//        aktualisiereDaten();
        return rootView;

    }

    private void createAdapter() {
        mAktienListeAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_aktienliste,
                R.id.list_item_aktienliste_textview,
                aktienListe
        );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(STATE_DATA, aktienListe);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate: Fragment erstellt");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: Fragment an Activity gebunden");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: die Fragment ist fertig erstellt");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Fragment wird gestartet");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Fragment kann verwendet werden");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Fragment geht in den Hintergrund");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Fragment ist im Hintergrund");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: FragmentView wird zerstört");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Fragment selbst wird zerstört");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Fragment wird von Activity gelöst");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_aktienlistefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_daten_aktualisieren:
                aktualisiereDaten();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void aktualisiereDaten() {
        HoleDatenTask holeDatenTask = new HoleDatenTask();

        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefAktienlisteKey = getString(R.string.preference_aktienliste_key);
        String prefAktienlisteDefault = getString(R.string.preference_aktienliste_default);
        String aktienliste = sPrefs.getString(prefAktienlisteKey, prefAktienlisteDefault);

        String prefIndizemodusKey = getString(R.string.preference_indizemodus_key);
        Boolean indizemodus = sPrefs.getBoolean(prefIndizemodusKey, false);
        if (indizemodus) {
            String indizeListe = "^GDAXI,^TECDAX,^MDAXI,^SDAXI,^GSPC,^N225,^HSI,XAGUSD=X,XAUUSD=X";
            holeDatenTask.execute(indizeListe);
        } else {
            holeDatenTask.execute(aktienliste);
        }


        Toast.makeText(getContext(), "Daten werden abgefragt", Toast.LENGTH_SHORT).show();
    }

    public class HoleDatenTask extends AsyncTask<String, Integer, String[]> {

        private final String TAG = HoleDatenTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            final String URL_PARAMETER = "http://www.programmierenlernenhq.de/tools/query.php";
            String symbols = strings[0];

            String anfrageString = URL_PARAMETER + "?s=" + symbols;
            Log.d(TAG, "doInBackground: " + anfrageString);

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String aktiendatenXmlString = "";

            try {
                URL url = new URL(anfrageString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    aktiendatenXmlString += line + "\n";
                }
                if (aktiendatenXmlString.length() == 0) {
                    return null;
                }
                Log.d(TAG, "doInBackground: " + aktiendatenXmlString);
                publishProgress(1, 1);

            } catch (IOException ioe) {
                Log.e(TAG, "doInBackground: Error", ioe);
                return null;

            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.d(TAG, "doInBackground: Error");
                    }
                }

            }

            return leseXmlAktiendatenAus(aktiendatenXmlString);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                mAktienListeAdapter.clear();
//                for (String aktienString : strings){
//                    mAktienListeAdapter.add(aktienString);
//                }
                mAktienListeAdapter.addAll(strings);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen", Toast.LENGTH_SHORT).show();
        }

        private String[] leseXmlAktiendatenAus(String xmlString) {
            Document doc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                doc = db.parse(is);
            } catch (ParserConfigurationException e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error", e);
                return null;
            } catch (SAXException e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error", e);
                return null;
            } catch (IOException e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error", e);
                return null;
            }
            Element xmlAktiendaten = doc.getDocumentElement();
            NodeList aktienListe = xmlAktiendaten.getElementsByTagName("row");

            int anzahlAktien = aktienListe.getLength();
            int anzahlAktienParameter = aktienListe.item(0).getChildNodes().getLength();

            String[] ausgabeArray = new String[anzahlAktien];
            String[][] alleAktienDatenArray = new String[anzahlAktien][anzahlAktienParameter];

            Node aktienParameter;
            String aktienParameterWert;

            for (int i = 0; i < anzahlAktien; i++) {
                NodeList aktienParameterListe = aktienListe.item(i).getChildNodes();
                for (int j = 0; j < anzahlAktienParameter; j++) {
                    aktienParameter = aktienParameterListe.item(j);
                    aktienParameterWert = aktienParameter.getFirstChild().getNodeValue();
                    alleAktienDatenArray[i][j] = aktienParameterWert;
                }
                ausgabeArray[i] = alleAktienDatenArray[i][0];                // symbol
                ausgabeArray[i] += ": " + alleAktienDatenArray[i][4];         // price
                ausgabeArray[i] += " " + alleAktienDatenArray[i][2];          // currency
                ausgabeArray[i] += " (" + alleAktienDatenArray[i][8] + ")";   // percent
                ausgabeArray[i] += " - [" + alleAktienDatenArray[i][1] + "]"; // name

                Log.v(TAG, "XML Output:" + ausgabeArray[i]);
            }


            return ausgabeArray;
        }
    }
}
