package com.example.smartlight;

import java.util.ArrayList;

public class LampData {

    private ArrayList<Lamp> lamps;

    public LampData(ArrayList<Lamp> lamps) {
        this.lamps = new ArrayList<>();
        setLamps(lamps);
    }

    public LampData() {
        this.lamps = new ArrayList<>();
    }

    public void setLamps(ArrayList<Lamp> lamps) {
        this.lamps = lamps;
    }

    public ArrayList<Lamp> getLamps() {
        return lamps;
    }

    public void add(Lamp lamp) {
        lamps.add(lamp);
    }

    public void remove(int index) {
        lamps.remove(index);
    }
}
