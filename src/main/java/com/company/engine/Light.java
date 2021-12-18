package com.company.engine;

import com.company.math.vector.Vector3;

public class Light {

    private Vector3 position;

    private float lightScale = 0.5F;

    public float getLightScale() {
        return lightScale;
    }

    public void setLightScale(float lightScale) {
        if(lightScale >= 0 && lightScale <= 1) {
            this.lightScale = lightScale;
        }
    }

    public Light(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(final Vector3 position) {
        this.position = position;
    }
}
