package com.example.smartlight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddLamp extends AppCompatActivity {

    public static final String EXTRA_LAMP_DATA = "lamp_data";

    private Spinner typeSpinner;

    public void addLamp(View view) {
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText urlEditText = findViewById(R.id.urlEditText);

        Lamp lamp = new Lamp(nameEditText.getText().toString(), urlEditText.getText().toString(), typeSpinner.getSelectedItemPosition(), false);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_LAMP_DATA, lamp);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lamp);

        String[] lampNames = getResources().getStringArray(R.array.lamp_types);

        typeSpinner = findViewById(R.id.lampTypeSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, lampNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(spinnerAdapter);
    }
}
