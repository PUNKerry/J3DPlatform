package com.company.engine;

import com.company.math.vector.Vector3;

public class Light {

    private Vector3 position;

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
