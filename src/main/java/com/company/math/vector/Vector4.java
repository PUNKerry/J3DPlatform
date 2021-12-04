package com.company.math.vector;

import java.util.Objects;

public class Vector4 implements Vector<Vector4> {
    public double x;
    public double y;
    public double z;
    public double k;

    public Vector4(double x, double y, double z, double k) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.k = k;
    }

    @Override
    public double length(){
        return Math.sqrt(x * x + y * y + z * z + k * k);
    }

    @Override
    public Vector4 sum(Vector4 v){
        return new Vector4(v.x + x, v.y + y, v.z + z, v.k + k);
    }

    @Override
    public Vector4 subtraction(Vector4 v){
        return new Vector4(x - v.x, y - v.y, z - v.z, k - v.k);
    }
    

    public void multiplyingAVectorByAScalar(double n){
        x *= n;
        y *= n;
        z *= n;
        k *= n;
    }

     public double scalarProduct(Vector4 v){
        return (x * v.x + y * v.y + z * v.z + k * v.k)/length()/v.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4 vector4 = (Vector4) o;
        return Double.compare(vector4.x, x) == 0 && Double.compare(vector4.y, y) == 0 && Double.compare(vector4.z, z) == 0 && Double.compare(vector4.k, k) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, k);
    }
}