package com.company.math.vector;

public interface Vector<V> {
     float length();

     V sum(V v);

     V subtraction(V v);

     void multiplyingAVectorByAScalar(float k);

     float scalarProduct(V v);

     default void dividingAVectorByAScalar(float k){
        multiplyingAVectorByAScalar(1/k);
    }

     default void normalization(){
        dividingAVectorByAScalar(length());
    }
}
