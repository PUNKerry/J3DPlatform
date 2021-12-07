package com.company.math.matrix;

import com.company.math.vector.Vector3;

import java.util.Arrays;

public class Matrix3 {
    public float[][] matrix = new float[3][3];

    public Matrix3(float[][] m) {
        for (int i = 0; i < 3; i++) {
            matrix[i] = m[i].clone();
        }
    }

    public float[] getVerticles(int index) {
        float[] toReturn = new float[3];
        for (int i = 0; i < 3; i++) {
            toReturn[i] = matrix[i][index];
        }

        return toReturn;
    }

    public Matrix3() {
        for (int i = 0; i < 3; i++) {
            Arrays.fill(matrix[i], 0);
        }
    }

    public void zeroMatrix(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public void unitMatrix(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i == j) {
                    matrix[i][j] = 1;
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public void sum(Matrix3 m){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] += m.matrix[i][j];
            }
        }
    }

    public void subtraction(Matrix3 m){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] -= m.matrix[i][j];
            }
        }
    }

    public void transposition(){
        float[][] tmp = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tmp[i][j] = matrix[j][i];
            }
        }
        matrix = tmp.clone();
    }

    public Matrix3 transpositionMatrix(){
        float[][] res = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res[i][j] = matrix[j][i];
            }
        }
        return new Matrix3(res);
    }

    public Matrix3 reverseMatrix(){
        Matrix3 m = transpositionMatrix();
        float det = determinant();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m.matrix[i][j] /= det;
            }
        }
        return m;
    }

    public Vector3 multiplyingOnVector(final Vector3 v){
        float[] m = new float[3];
        for (int i = 0; i < 3; i++) {
            m[i] =  matrix[i][0] * v.x + matrix[i][1] * v.y + matrix[i][2] * v.z;
        }
        return new Vector3(m[0], m[1], m[2]);
    }

    private static float determinant2(float[][] m){
        return m[0][0] * m[1][1] - m[1][0] * m[0][1];
    }

    protected static float determinant3(float[][] m){
        float res = 0;
        float[][] d = {{m[1][1], m[1][2]}, {m[2][1], m[2][2]}};
        res += m[0][0] * determinant2(d);
        float[][] d2 = {{m[1][0], m[1][2]}, {m[2][0], m[2][2]}};
        res -= m[0][1] * determinant2(d2);
        float[][] d3 = {{m[1][0], m[1][1]}, {m[2][0], m[2][1]}};
        res += m[0][2] * determinant2(d3);
        return res;
    }

    public float determinant(){
        return determinant3(matrix);
    }

    public Vector3 systemOfEquations(Vector3 v){
        float n = matrix[1][0]/matrix[0][0];
        for (int i = 0; i < 3; i++) {
            matrix[1][i] -= matrix[0][i] * n;
        }
        v.y -= v.x * n;
        n = matrix[2][0]/matrix[0][0];
        for (int i = 0; i < 3; i++) {
            matrix[2][i] -= matrix[0][i] * n;
        }
        v.z -= v.x * n;
        n = matrix[2][1]/matrix[1][1];
        for (int i = 1; i < 3; i++) {
            matrix[2][i] -= matrix[1][i] * n;
        }
        v.z -= v.y * n;
        float z = v.z / matrix[2][2];
        v.y -= matrix[1][2] * z;
        float y = v.y / matrix[1][1];
        v.x -= matrix[0][1] * y + matrix[0][2] *z;
        float x = v.x / matrix[0][0];
        return new Vector3(x,y,z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix3 matrix3 = (Matrix3) o;
        return Arrays.deepEquals(matrix, matrix3.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
