package nl.ralphrouwen.hue.Helper;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Models.Light;

public class LightController {

    ArrayList<Light> lights;

    private static LightController instance = null;
    protected LightController() {
        lights = new ArrayList<>();
        // Exists only to defeat instantiation.
    }
    public static LightController getInstance() {
        if(instance == null) {
            instance = new LightController();
        }
        return instance;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }
}

