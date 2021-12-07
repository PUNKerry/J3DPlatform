package com.company.math.entity;

public class Point2 {

    public float x;
    public float y;

    public Point2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2(float[] cords) {
        this.x = cords[0];
        this.y = cords[1];
    }

    public Point2() {
    }

    public final float distanceSquared(Point2 var1) {
        float var2 = this.x - var1.x;
        float var3 = this.y - var1.y;
        return var2 * var2 + var3 * var3;
    }

    public final float distance(Point2 var1) {
        float var2 = this.x - var1.x;
        float var3 = this.y - var1.y;
        return (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
    }

    public final float distanceL1(Point2 var1) {
        return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y);
    }

    public final float distanceLinf(Point2 var1) {
        return Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
    }
}
