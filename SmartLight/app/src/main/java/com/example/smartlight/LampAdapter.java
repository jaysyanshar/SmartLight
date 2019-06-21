package com.example.smartlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LampAdapter extends ArrayAdapter<Lamp> {

    private Context context;

    public static final String EXTRA_CONFIG = "config";
    public static final String EXTRA_CONFIG_INDEX = "config_index";

    public LampAdapter(Activity context, ArrayList<Lamp> lamps) {
        super(context, 0, lamps);
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

        // Get the current lamp
        final Lamp currentLamp = getItem(position);
        currentLamp.setId(position);

        // Set the view elements
        listItemView.setTag(position);

        TextView lampNameTextView = listItemView.findViewById(R.id.lampNameTextView);
        lampNameTextView.setText(currentLamp.getName());

        TextView lampUrlTextView = listItemView.findViewById(R.id.lampUrlTextView);
        lampUrlTextView.setText(currentLamp.getUrl());

        final TextView lampStatusTextView = listItemView.findViewById(R.id.lampStatus);
        lampStatusTextView.setText(currentLamp.isStatusOn() ? context.getString(R.string.on) : context.getString(R.string.off));
        lampStatusTextView.setTag(position);
        lampStatusTextView.setVisibility(View.VISIBLE);

        final Button lampRemoveButton = listItemView.findViewById(R.id.lampRemoveButton);
        lampRemoveButton.setTag(position);
        lampRemoveButton.setVisibility(View.GONE);

        final ImageView lampImageView = listItemView.findViewById(R.id.lampImageView);
        setLampImage(lampImageView, currentLamp.getType());

        // Listener
        listItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                lampImageView.setVisibility(View.GONE);
                lampRemoveButton.setVisibility(View.VISIBLE);

                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        lampRemoveButton.setVisibility(View.GONE);
                        lampImageView.setVisibility(View.VISIBLE);
                    }
                }.start();

                return false;
            }
        });

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfigureLamp.class);
                intent.putExtra(EXTRA_CONFIG, currentLamp);
                context.startActivity(intent);
            }
        });

        // Return the list view
        return listItemView;
    }

    private void setLampImage(ImageView lampImage, int lampType) {
        switch (lampType) {
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
    }
}
