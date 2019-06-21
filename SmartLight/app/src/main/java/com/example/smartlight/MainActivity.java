package com.example.smartlight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int LAMP_REQUEST = 1;
    private static final String SHARED_PREFERENCES_NAME = "lamp_data";
    private static final String LAMP_KEY = "lamp_key";

    private LampData lampData;
    private ListView lampListView;

    public void removeLamp(View view) {
        String name = lampData.getLamps().get(Integer.valueOf(view.getTag().toString())).getName();
        lampData.remove(Integer.valueOf(view.getTag().toString()));
        updateListView();
        Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.remove_message, name), Snackbar.LENGTH_LONG).show();
    }

    private void updateListView() {
        LampAdapter lampAdapter = new LampAdapter(this, lampData.getLamps());
        lampListView.setAdapter(lampAdapter);
    }

    private void saveData() {
        ArrayList<Lamp> lampArrayList = lampData.getLamps();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lampArrayList);
        editor.putString(LAMP_KEY, json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LAMP_KEY, null);
        Type type = new TypeToken<ArrayList<Lamp>>(){}.getType();
        ArrayList<Lamp> lampArrayList;
        lampArrayList = gson.fromJson(json, type);

        if (lampArrayList == null) {
            lampData = new LampData();
        } else {
            lampData = new LampData(lampArrayList);
        }
    }

    private void getConfigChange() {
        try {
            Intent intent = getIntent();
            Lamp lamp = intent.getParcelableExtra(ConfigureLamp.EXTRA_RETURN_CONFIG);
            lampData.getLamps().get(lamp.getId()).setName(lamp.getName());
            lampData.getLamps().get(lamp.getId()).setUrl(lamp.getUrl());
            lampData.getLamps().get(lamp.getId()).setBrightness(lamp.getBrightness());
            lampData.getLamps().get(lamp.getId()).setStatusOn(lamp.isStatusOn());
            updateListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadData();

        lampListView = findViewById(R.id.lampListView);
        updateListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getConfigChange();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAMP_REQUEST) {
            if (resultCode == RESULT_OK) {
                Lamp lamp = data.getParcelableExtra(AddLamp.EXTRA_LAMP_DATA);

                boolean urlAlreadyExist = false;
                for (Lamp currentLamp : lampData.getLamps()) {
                    if (currentLamp.getUrl().equals(lamp.getUrl())) {
                        urlAlreadyExist = true;
                        break;
                    }
                }

                if (urlAlreadyExist) {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.failed_message), Snackbar.LENGTH_LONG).show();
                } else {
                    lampData.add(lamp);
                    updateListView();
                    Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.failed_message), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_lamp) {

            Intent intent = new Intent(getApplicationContext(), AddLamp.class);
            startActivityForResult(intent, LAMP_REQUEST);

        }

        return super.onOptionsItemSelected(item);
    }
}
