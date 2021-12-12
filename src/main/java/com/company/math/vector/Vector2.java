package com.company.math.vector;

import java.util.Objects;

public class Vector2 implements Vector<Vector2>{
    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {

    }

    public float length(){
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 sum(Vector2 v){
        return new Vector2(v.x + x, v.y + y);
    }


    public Vector2 subtraction(Vector2 v){
        return new Vector2(x - v.x, y - v.y);
    }

    public Vector2 multiplyingAVectorByAScalar(float k){
        return new Vector2(x * k, y * k);
    }

    public float scalarProduct(Vector2 v){
        return (x * v.x + y * v.y)/length()/v.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
