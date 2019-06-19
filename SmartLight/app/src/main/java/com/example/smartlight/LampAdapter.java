package com.example.smartlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    private Context context;
    public static final String EXTRA_CONFIG = "config";

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

        lampToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentLamp.setStatusOn(isChecked);
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
