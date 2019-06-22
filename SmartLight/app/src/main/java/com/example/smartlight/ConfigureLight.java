package com.example.smartlight;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;

public class ConfigureLight extends AppCompatActivity {

    public static final String EXTRA_RETURN_CONFIG = "return_config";

    private static final int SETTINGS_REQUEST_CODE = 1000;

    private Light light;

    // Screen Brightness
    private boolean settingsGranted = false;
    private int defaultScreenBrightnessMode;
    private int defaultScreenBrightness;

    // Camera Flashlight
    private CameraManager mCameraManager;
    private String mCameraId;

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void editCurrentText(ImageButton imageButton, EditText editText) {
        if (imageButton.getTag().toString().equals(String.valueOf(0))) {
            imageButton.setTag(1);
            imageButton.setImageResource(android.R.drawable.ic_menu_save);
            editText.setText(editText.getHint());
            editText.setEnabled(true);
            editText.setSelection(editText.getText().length());
            showKeyboard();
        } else {
            imageButton.setTag(0);
            editText.setEnabled(false);
            hideKeyboard();
            imageButton.setImageResource(android.R.drawable.ic_menu_edit);
            if (!editText.getText().toString().equals("")) {
                String edited = editText.getText().toString();
                editText.setHint(edited);
            }
            editText.setText("");
        }
    }

    private void setLightImage(ImageView lightImage, int lightType) {
        switch (lightType) {
            case Light.DESK: {
                lightImage.setImageResource(R.drawable.light_desk);
                break;
            }
            case Light.FLOOR: {
                lightImage.setImageResource(R.drawable.light_floor);
                break;
            }
            case Light.CEILING: {
                lightImage.setImageResource(R.drawable.light_ceiling);
                break;
            }
            default: {
                lightImage.setImageResource(R.drawable.light_default);
                break;
            }
        }
    }

    private boolean isFlashAvailable() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void getCamera() {
        //getting the camera manager and camera id
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            assert mCameraManager != null;
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void toggleFlash(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setView(ConstraintLayout layout, boolean lightIsOn, EditText name, EditText url, SeekBar brightness) {
        // Flashlight
        if (isFlashAvailable()) {
            toggleFlash(lightIsOn);
        }

        // View and brightness
        if (lightIsOn) {
            if (settingsGranted) {
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS, light.getBrightness());
            }
            layout.setBackgroundColor(getColor(android.R.color.background_light));
            name.setTextColor(getColor(android.R.color.primary_text_light));
            url.setTextColor(getColor(android.R.color.primary_text_light));
            brightness.setEnabled(true);
        } else {
            if (settingsGranted) {
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE, defaultScreenBrightnessMode);
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS, defaultScreenBrightness);
            }
            layout.setBackgroundColor(getColor(android.R.color.background_dark));
            name.setTextColor(getColor(android.R.color.primary_text_dark));
            url.setTextColor(getColor(android.R.color.primary_text_dark));
            brightness.setEnabled(false);
        }
    }

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

    private void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 255) {
            brightness = 100;
        }

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    private int getBrightness() {
        int brightness = 100;

        try {
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return brightness;
    }

    @SuppressLint("ObsoleteSdkInt")
    private void getPermission() {
        boolean value;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            value = Settings.System.canWrite(getApplicationContext());
            if (value) {
                settingsGranted = true;
                try {
                    defaultScreenBrightnessMode = Settings.System.getInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE);
                    defaultScreenBrightness = getBrightness();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
            }
        } else {
            settingsGranted = true;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean value = Settings.System.canWrite(getApplicationContext());
                if (value) {
                    settingsGranted = true;
                    try {
                        defaultScreenBrightnessMode = Settings.System.getInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE);
                        defaultScreenBrightness = getBrightness();
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Settings access permission denied.", Toast.LENGTH_SHORT).show();
                }
            } else {
                settingsGranted = true;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_light);

        // Get light config
        Intent intent = getIntent();
        light = intent.getParcelableExtra(LightAdapter.EXTRA_CONFIG);

        final EditText name = findViewById(R.id.configLightNameET);
        name.setHint(light.getName());

        final ImageButton editName = findViewById(R.id.configLightNameBtn);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCurrentText(editName, name);
                if (editName.getTag().toString().equals(String.valueOf(0))) {
                    if (name.getHint() != "") {
                        light.setName(name.getHint().toString());
                    } else {
                        name.setHint(light.getName());
                    }
                }
            }
        });

        final EditText url = findViewById(R.id.configUrlET);
        url.setHint(light.getUrl());

        final ImageButton editUrl = findViewById(R.id.configUrlBtn);
        editUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCurrentText(editUrl, url);
                if (editUrl.getTag().toString().equals(String.valueOf(0))) {
                    if (isValidUrl(url.getHint().toString())) {
                        light.setUrl(url.getHint().toString());
                    } else {
                        url.setHint(light.getUrl());
                    }
                }
            }
        });

        final SeekBar brightControl = findViewById(R.id.brightControl);
        brightControl.setMax(255);
        brightControl.setProgress(light.getBrightness());
        brightControl.setKeyProgressIncrement(1);

        getPermission();

        brightControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light.setBrightness(progress);
                if (fromUser && settingsGranted) {
                    setBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!settingsGranted) {
                    Toast.makeText(ConfigureLight.this, "Settings access permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ConstraintLayout configLayout = findViewById(R.id.configLayout);

        getCamera();
        setView(configLayout, light.isStatusOn(), name, url, brightControl);

        ImageView lightImage = findViewById(R.id.configLightImageView);
        setLightImage(lightImage, light.getType());
        lightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light.setStatusOn(!light.isStatusOn());
                setView(configLayout, light.isStatusOn(), name, url, brightControl);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (light.isStatusOn()) {
            if (isFlashAvailable()) {
                toggleFlash(false);
            }

            if (settingsGranted) {
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE, defaultScreenBrightnessMode);
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS, defaultScreenBrightness);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (light.isStatusOn()) {
            if (isFlashAvailable()) {
                toggleFlash(true);
            }

            if (settingsGranted) {
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS, light.getBrightness());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isFlashAvailable()) {
            toggleFlash(false);
        }

        if (settingsGranted) {
            Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS_MODE, defaultScreenBrightnessMode);
            Settings.System.putInt(getApplicationContext().getContentResolver(), SCREEN_BRIGHTNESS, defaultScreenBrightness);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_RETURN_CONFIG, light);
        startActivity(intent);
        finish();

    }
}
