package com.example.smartlight;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ConfigureLamp extends AppCompatActivity {

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void editCurrentText(ImageButton imageButton, EditText editText) {
        if (imageButton.getTag().toString().equals(String.valueOf(0))) {
            imageButton.setTag(1);
            imageButton.setImageResource(android.R.drawable.ic_menu_save);
            editText.setEnabled(true);
            showKeyboard();
        } else {
            imageButton.setTag(0);
            editText.setEnabled(false);
            imageButton.setImageResource(android.R.drawable.ic_menu_edit);
            editText.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_lamp);

        Intent intent = getIntent();
        Lamp lamp = intent.getParcelableExtra(LampAdapter.EXTRA_CONFIG);


        final EditText name = findViewById(R.id.configLampNameET);
        name.setHint(lamp.getName());

        final ImageButton editName = findViewById(R.id.configLampNameBtn);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCurrentText(editName, name);
            }
        });

        final EditText url = findViewById(R.id.configUrlET);
        url.setHint(lamp.getUrl());

        final ImageButton editUrl = findViewById(R.id.configUrlBtn);
        editUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCurrentText(editUrl, url);
            }
        });

        ImageView lampImage = findViewById(R.id.configLampImageView);
        switch (lamp.getType()) {
            case Lamp.DESK: {
                lampImage.setImageResource(R.drawable.lamp_desk);
                break;
            }
            case Lamp.FLOOR: {
                lampImage.setImageResource(R.drawable.lamp_floor);
                break;
            }
            case Lamp.CEILING: {
                lampImage.setImageResource(R.drawable.lamp_ceiling);
                break;
            }
            default: {
                lampImage.setImageResource(R.drawable.lamp_default);
                break;
            }
        }

        ConstraintLayout configLayout = findViewById(R.id.configLayout);

        if (lamp.isStatusOn()) {
            configLayout.setBackgroundColor(getColor(android.R.color.background_light));
            name.setTextColor(getColor(android.R.color.primary_text_light));
            url.setTextColor(getColor(android.R.color.primary_text_light));
        } else {
            configLayout.setBackgroundColor(getColor(android.R.color.background_dark));
            name.setTextColor(getColor(android.R.color.primary_text_dark));
            url.setTextColor(getColor(android.R.color.primary_text_dark));
        }
    }
}
