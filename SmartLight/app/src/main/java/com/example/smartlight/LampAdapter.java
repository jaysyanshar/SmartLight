package com.example.smartlight;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class LampAdapter extends ArrayAdapter<Lamp> {

    public LampAdapter(Activity context, ArrayList<Lamp> lamps) {
        super(context, 0, lamps);
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

        // Set the view elements
        TextView lampNameTextView = listItemView.findViewById(R.id.lampNameTextView);
        lampNameTextView.setText(currentLamp.getName());

        TextView lampUrlTextView = listItemView.findViewById(R.id.lampUrlTextView);
        lampUrlTextView.setText(currentLamp.getUrl());

        final ToggleButton lampToggleButton = listItemView.findViewById(R.id.lampToggleButton);
        lampToggleButton.setChecked(currentLamp.isStatusOn());
        lampToggleButton.setTag(position);
        lampToggleButton.setVisibility(View.VISIBLE);

        final Button lampRemoveButton = listItemView.findViewById(R.id.lampRemoveButton);
        lampRemoveButton.setTag(position);
        lampRemoveButton.setVisibility(View.GONE);

        final ImageView lampImageView = listItemView.findViewById(R.id.lampImageView);
        switch (currentLamp.getType()) {
            case Lamp.DESK: {
                lampImageView.setImageResource(R.drawable.lamp_desk);
                break;
            }
            case Lamp.FLOOR: {
                lampImageView.setImageResource(R.drawable.lamp_floor);
                break;
            }
            case Lamp.CEILING: {
                lampImageView.setImageResource(R.drawable.lamp_ceiling);
                break;
            }
            default: {
                lampImageView.setImageResource(R.drawable.lamp_default);
                break;
            }
        }

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

            }
        });

        lampToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentLamp.setStatusOn(isChecked);
            }
        });

        // Return the list view
        return listItemView;
    }
}
