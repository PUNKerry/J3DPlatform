package com.company;

public class Vector2f {

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final float x, y;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        float eps = 1e-7f;
        Vector2f other = (Vector2f) o;
        return Math.abs(x - other.x) < eps && Math.abs(y - other.y) < eps;
    }
}