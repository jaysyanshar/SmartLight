package com.example.smartlight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

public class AddLight extends AppCompatActivity {

    public static final String EXTRA_LIGHT_DATA = "light_data";
    public static final String LIGHT_DEFAULT_NAME = "New Light";

    private Spinner typeSpinner;

    private boolean isValidUrl(String url) {
        String httpUrl = getString(R.string.http_url, url);

        URLValidator validator = new URLValidator();

        try {

            return validator.execute(httpUrl).get();

        } catch (ExecutionException e) {

            e.printStackTrace();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        return false;
    }

    public void addLight(View view) {
        EditText urlEditText = findViewById(R.id.urlEditText);
        String url = urlEditText.getText().toString();

        if (isValidUrl(url)) {
            EditText nameEditText = findViewById(R.id.nameEditText);
            String name = nameEditText.getText().toString();

            if (name.equals("")) {
                name = LIGHT_DEFAULT_NAME;
            }

            Light light = new Light(0, name, url, typeSpinner.getSelectedItemPosition(), 100, false);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(EXTRA_LIGHT_DATA, light);
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_light);

        String[] lightNames = getResources().getStringArray(R.array.light_types);

        typeSpinner = findViewById(R.id.lightTypeSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, lightNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(spinnerAdapter);
    }
}
