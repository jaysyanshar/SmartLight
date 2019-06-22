package com.example.smartlight;

import java.util.ArrayList;

class LightData {

    private ArrayList<Light> lights;

    LightData(ArrayList<Light> lights) {
        this.lights = new ArrayList<>();
        setLights(lights);
    }

    LightData() {
        this.lights = new ArrayList<>();
    }

    private void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    ArrayList<Light> getLights() {
        return lights;
    }

    void add(Light light) {
        lights.add(light);
    }

    void remove(int index) {
        lights.remove(index);
    }

}
