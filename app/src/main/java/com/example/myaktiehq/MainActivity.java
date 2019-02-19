package com.example.myaktiehq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: View ist fertig gebaut");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: hier wird neu gestartet");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: kurz vor sichtbar werden");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: für interaktion bereit");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: kurz vor in den Hintergrund gehen");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: jetzt im Hintergrund und angehalten");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( TAG, "onDestroy: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings wurde gedrückt", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,EinstellungenActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
