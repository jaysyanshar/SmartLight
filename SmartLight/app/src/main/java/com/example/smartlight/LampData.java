package com.example.smartlight;

import java.util.ArrayList;

public class LampData {

    private ArrayList<Lamp> lamps;

    public LampData(ArrayList<Lamp> lamps) {
        this.lamps = new ArrayList<>();
        setLamps(lamps);

        // Temporary Data for Testing
        Lamp lamp = new Lamp("Lampu Kamar", "www.lampukamar.co.id", Lamp.CEILING, false);
        add(lamp);
        lamp = new Lamp("Lampu Belajar", "www.lampubelajar.co.id", Lamp.DESK, false);
        add(lamp);
        lamp = new Lamp("Lampu Ruang Keluarga", "www.livinglamp.com", Lamp.FLOOR, true);
        add(lamp);
        lamp = new Lamp("Lampu Biasa", "www.originoflamp.com", Lamp.DEFAULT, true);
        add(lamp);
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
        lamps.remove(lamps.get(index));
    }
}
