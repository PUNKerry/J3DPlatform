package com.company.math.vector;

public interface Vector<V> {
     double length();

     V sum(V v);

     V subtraction(V v);

     void multiplyingAVectorByAScalar(double k);

     double scalarProduct(V v);

     default void dividingAVectorByAScalar(double k){
        multiplyingAVectorByAScalar(1/k);
    }

     default void normalization(){
        dividingAVectorByAScalar(length());
    }
}
