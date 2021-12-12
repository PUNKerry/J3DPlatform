package com.company.math.vector;

import java.util.Objects;

public class Vector4 implements Vector<Vector4> {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4(float x, float y, float z, float k) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = k;
    }

    @Override
    public float length(){
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    @Override
    public Vector4 sum(Vector4 v){
        return new Vector4(v.x + x, v.y + y, v.z + z, v.w + w);
    }

    @Override
    public Vector4 subtraction(Vector4 v){
        return new Vector4(x - v.x, y - v.y, z - v.z, w - v.w);
    }
    

    public Vector4 multiplyingAVectorByAScalar(float k){
        return new Vector4(x * k, y * k, z * k, w * k);
    }

     public float scalarProduct(Vector4 v){
        return (x * v.x + y * v.y + z * v.z + w * v.w)/length()/v.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4 vector4 = (Vector4) o;
        return Float.compare(vector4.x, x) == 0 && Float.compare(vector4.y, y) == 0 && Float.compare(vector4.z, z) == 0 && Float.compare(vector4.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}