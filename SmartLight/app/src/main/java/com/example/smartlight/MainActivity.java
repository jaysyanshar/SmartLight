package com.example.smartlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int LAMP_REQUEST = 1;

    private LampData lampData;
    private LampAdapter lampAdapter;
    private ListView lampListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Lamp> lamps = new ArrayList<>();
        lampData = new LampData(lamps);

        lampAdapter = new LampAdapter(this, lampData.getLamps());
        lampListView = findViewById(R.id.lampListView);
        lampListView.setAdapter(lampAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAMP_REQUEST) {
            if (resultCode == RESULT_OK) {
                Lamp lamp = data.getParcelableExtra(AddLamp.EXTRA_LAMP_DATA);
                lampData.add(lamp);

                lampAdapter = new LampAdapter(this, lampData.getLamps());
                lampListView.setAdapter(lampAdapter);
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
