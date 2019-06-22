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
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int LIGHT_REQUEST = 1;
    private static final String SHARED_PREFERENCES_NAME = "light_data";
    private static final String LIGHT_KEY = "light_key";

    private LightData lightData;
    private ListView lightListView;

    public void removeLight(View view) {
        String name = lightData.getLights().get(Integer.valueOf(view.getTag().toString())).getName();
        lightData.remove(Integer.valueOf(view.getTag().toString()));
        updateListView();
        Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.remove_message, name), Snackbar.LENGTH_LONG).show();
    }

    private void updateListView() {
        LightAdapter lightAdapter = new LightAdapter(this, lightData.getLights());
        lightListView.setAdapter(lightAdapter);
    }

    private void saveData() {
        ArrayList<Light> lightArrayList = lightData.getLights();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lightArrayList);
        editor.putString(LIGHT_KEY, json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LIGHT_KEY, null);
        Type type = new TypeToken<ArrayList<Light>>(){}.getType();
        ArrayList<Light> lightArrayList;
        lightArrayList = gson.fromJson(json, type);

        if (lightArrayList == null) {
            lightData = new LightData();
        } else {
            lightData = new LightData(lightArrayList);
        }
    }

    private void getConfigChange() {
        try {
            Intent intent = getIntent();
            Light light = intent.getParcelableExtra(ConfigureLight.EXTRA_RETURN_CONFIG);
            lightData.getLights().get(light.getId()).setName(light.getName());
            lightData.getLights().get(light.getId()).setUrl(light.getUrl());
            lightData.getLights().get(light.getId()).setBrightness(light.getBrightness());
            lightData.getLights().get(light.getId()).setStatusOn(light.isStatusOn());
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

        lightListView = findViewById(R.id.lightListView);
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
        if (requestCode == LIGHT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Light light = data.getParcelableExtra(AddLight.EXTRA_LIGHT_DATA);

                boolean urlAlreadyExist = false;
                for (Light currentLight : lightData.getLights()) {
                    if (currentLight.getUrl().equals(light.getUrl())) {
                        urlAlreadyExist = true;
                        break;
                    }
                }

                if (urlAlreadyExist) {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.failed_message), Snackbar.LENGTH_LONG).show();
                } else {
                    lightData.add(light);
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
        if (id == R.id.action_add_light) {

            Intent intent = new Intent(getApplicationContext(), AddLight.class);
            startActivityForResult(intent, LIGHT_REQUEST);

        }

        return super.onOptionsItemSelected(item);
    }
}
