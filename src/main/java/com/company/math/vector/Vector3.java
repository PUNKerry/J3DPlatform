package com.company.math.vector;

import java.util.Objects;

public class Vector3 implements Vector<Vector3> {
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {

    }

    public final float dot(Vector3 v1)
    {
        return (float)(this.x*v1.x + this.y*v1.y + this.z*v1.z);
    }

    public final void normalize(Vector3 v1)
    {
        float norm;

        norm = (float) (1.0/Math.sqrt(v1.x*v1.x + v1.y*v1.y + v1.z*v1.z));
        this.x = v1.x*norm;
        this.y = v1.y*norm;
        this.z = v1.z*norm;
    }

    public final void normalize()
    {
        float norm;

        norm = (float)
                (1.0/Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z));
        this.x *= norm;
        this.y *= norm;
        this.z *= norm;
    }

    public final void cross(Vector3 v1, Vector3 v2)
    {
        float x,y;
        x = (float) (v1.y*v2.z - v1.z*v2.y);
        y = (float) (v2.x*v1.z - v2.z*v1.x);
        this.z = v1.x*v2.y - v1.y*v2.x;
        this.x = x;
        this.y = y;
    }

    public float length(){
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 sum(Vector3 v){
        return new Vector3(v.x + x, v.y + y, v.z + z);
    }

    public Vector3 vectorProduct(Vector3 v) {
       return new Vector3( z * v.y - y * v.z, -(z * v.x - x * v.z), y * v.x - x * v.y);
    }

    public Vector3 subtraction(Vector3 v){
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public void multiplyingAVectorByAScalar(float k){
        x *= k;
        y *= k;
        z *= k;
    }

    public final void sub(Vector3 var1, Vector3 var2) {
        this.x = var1.x - var2.x;
        this.y = var1.y - var2.y;
        this.z = var1.z - var2.z;
    }

    public float scalarProduct(Vector3 v){
        return (x * v.x + y * v.y + z * v.z)/length()/v.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return Float.compare(vector3.x, x) == 0 && Float.compare(vector3.y, y) == 0 && Float.compare(vector3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
