package com.example.smartlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LightAdapter extends ArrayAdapter<Light> {

    private Context context;

    static final String EXTRA_CONFIG = "config";

    LightAdapter(Activity context, ArrayList<Light> lights) {
        super(context, 0, lights);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_content_main, parent, false);
        }

        // Get the current light
        final Light currentLight = getItem(position);
        assert currentLight != null;
        currentLight.setId(position);

        // Set the view elements
        listItemView.setTag(position);

        TextView lightNameTextView = listItemView.findViewById(R.id.lightNameTextView);
        lightNameTextView.setText(currentLight.getName());

        TextView lightUrlTextView = listItemView.findViewById(R.id.lightUrlTextView);
        lightUrlTextView.setText(currentLight.getUrl());

        final TextView lightStatusTextView = listItemView.findViewById(R.id.lightStatus);
        lightStatusTextView.setText(currentLight.isStatusOn() ? context.getString(R.string.on) : context.getString(R.string.off));
        lightStatusTextView.setTag(position);
        lightStatusTextView.setVisibility(View.VISIBLE);

        final Button lightRemoveButton = listItemView.findViewById(R.id.lightRemoveButton);
        lightRemoveButton.setTag(position);
        lightRemoveButton.setVisibility(View.GONE);

        final ImageView lightImageView = listItemView.findViewById(R.id.lightImageView);
        setLightImage(lightImageView, currentLight.getType());

        // Listener
        listItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                lightImageView.setVisibility(View.GONE);
                lightRemoveButton.setVisibility(View.VISIBLE);

                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        lightRemoveButton.setVisibility(View.GONE);
                        lightImageView.setVisibility(View.VISIBLE);
                    }
                }.start();

                return false;
            }
        });

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfigureLight.class);
                intent.putExtra(EXTRA_CONFIG, currentLight);
                context.startActivity(intent);
            }
        });

        // Return the list view
        return listItemView;
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
}
