package com.example.myaktiehq;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AktiendetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktiendetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aktiendetail, menu);
        String aktienInfo="";
        Intent empfangenerIntent = getIntent();
        if(empfangenerIntent!= null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
            aktienInfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
        }
        MenuItem shareMenuItem = menu.findItem(R.id.action_teile_aktiendaten);

        ShareActionProvider sAP;
        sAP = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Daten zu: " + aktienInfo);

        if(sAP != null){
            sAP.setShareIntent(shareIntent);
        }else{
            Toast.makeText(this, "Kein Provider vorhanden", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings wurde gedrückt", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_starte_browser:
                zeigeWebsiteImBrowser();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void zeigeWebsiteImBrowser() {
        String webseitenUrl="";
        Intent empfangenerIntent = getIntent();
        if(empfangenerIntent!= null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)){
            String aktienInfo  = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
            int pos = aktienInfo.indexOf(":");
            String symbol = aktienInfo.substring(0,pos);
            webseitenUrl = "http://finance.yahoo.com/q?s=" + symbol;
        }
        Uri webseitenUri = Uri.parse(webseitenUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW,webseitenUri);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }else{
            Toast.makeText(this, "Keine Web-App installiert", Toast.LENGTH_SHORT).show();
        }

        


    }
}
