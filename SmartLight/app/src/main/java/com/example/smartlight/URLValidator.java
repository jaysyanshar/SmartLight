package com.example.smartlight;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLValidator extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... urls) {

        try {

            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }
}
