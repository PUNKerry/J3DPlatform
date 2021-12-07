package com.company.math.matrix;

import com.company.math.vector.Vector4;

import javax.vecmath.Matrix4f;
import java.util.Arrays;

public class Matrix4 {
    public float[][] matrix = new float[4][4];

    public Matrix4(float[] m) {
        float[][] localMx = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(m, i * 4 + 0, localMx[i], 0, 4);
            matrix = localMx;
        }
    }

    public Matrix4(float[][] m) {
        for (int i = 0; i < 4; i++) {
            matrix[i] = m[i].clone();
        }
    }

    public Matrix4(Matrix4 matrix4) {
        for (int i = 0; i < 4; i++) {
            matrix[i] = matrix4.matrix[i].clone();
        }
    }

    public Matrix4() {
        for (int i = 0; i < 4; i++) {
            Arrays.fill(matrix[i], 0);
        }
    }

    public void zeroMatrix(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public void unitMatrix(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(i == j) {
                    matrix[i][j] = 1;
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public void sum(Matrix4 m){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] += m.matrix[i][j];
            }
        }
    }

    public void subtraction(Matrix4 m){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] -= m.matrix[i][j];
            }
        }
    }

    public void transposition(){
        float[][] tmp = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = matrix[j][i];
            }
        }
        matrix = tmp.clone();
    }

    public Matrix4 transpositionMatrix(){
        float[][] res = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res[i][j] = matrix[j][i];
            }
        }
        return new Matrix4(res);
    }

    public Matrix4 reverseMatrix(){
        Matrix4 m = transpositionMatrix();
        float det = determinant();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m.matrix[i][j] /= det;
            }
        }
        return m;
    }

    public Vector4 multiplyingOnVector(Vector4 v){
        float[] m = new float[4];
        for (int i = 0; i < 4; i++) {
            m[i] =  matrix[i][0] * v.x + matrix[i][1] * v.y + matrix[i][2] * v.z + matrix[i][3] * v.k;
        }
        return new Vector4(m[0], m[1], m[2], m[3]);
    }

    protected float determinant4(float[][] m){
        float res = 0;
        float[][] d = {{m[1][1], m[1][2], m[1][3]},
                       {m[2][1], m[2][2], m[2][3]},
                       {m[3][1], m[3][2], m[3][3]}};
        res += m[0][0] * Matrix3.determinant3(d);
        float[][] d2 = {{m[1][0], m[1][2], m[1][3]},
                        {m[2][0], m[2][2], m[2][3]},
                        {m[3][0], m[3][2], m[3][3]}};
        res -= m[0][1] * Matrix3.determinant3(d2);
        float[][] d3 = {{m[1][0], m[1][1], m[1][3]},
                        {m[2][0], m[2][1], m[2][3]},
                        {m[3][0], m[3][1], m[3][3]}};
        res += m[0][2] * Matrix3.determinant3(d3);
        float[][] d4 = {{m[1][0], m[1][1], m[1][2]},
                        {m[2][0], m[2][1], m[2][2]},
                        {m[3][0], m[3][1], m[3][2]}};
        res -= m[0][3] * Matrix3.determinant3(d4);
        return res;
    }

    public float determinant(){
        return determinant4(matrix);
    }

    public Vector4 systemOfEquations(Vector4 v){
        float n = matrix[1][0]/matrix[0][0];
        for (int i = 0; i < 4; i++) {
            matrix[1][i] -= matrix[0][i] * n;
        }
        v.y -= v.x * n;
        n = matrix[2][0]/matrix[0][0];
        for (int i = 0; i < 4; i++) {
            matrix[2][i] -= matrix[0][i] * n;
        }
        v.z -= v.x * n;
        n = matrix[3][0]/matrix[0][0];
        for (int i = 0; i < 4; i++) {
            matrix[3][i] -= matrix[0][i] * n;
        }
        v.k -= v.x * n;

        n = matrix[2][1]/matrix[1][1];
        for (int i = 1; i < 4; i++) {
            matrix[2][i] -= matrix[1][i] * n;
        }
        v.z -= v.y * n;
        n = matrix[3][1]/matrix[1][1];
        for (int i = 1; i < 4; i++) {
            matrix[3][i] -= matrix[1][i] * n;
        }
        v.k -= v.y * n;

        n = matrix[3][2]/matrix[2][2];
        for (int i = 2; i < 4; i++) {
            matrix[3][i] -= matrix[2][i] * n;
        }
        v.k -= v.z * n;

        float k = v.k / matrix[3][3];
        v.z -= matrix[2][3] * k;
        float z = v.z / matrix[2][2];
        v.y -= matrix[1][2] * z;
        float y = v.y / matrix[1][1];
        v.x -= matrix[0][1] * y + matrix[0][2] *z;
        float x = v.x / matrix[0][0];
        return new Vector4(x, y, z, k);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix4 matrix4 = (Matrix4) o;
        return Arrays.deepEquals(matrix, matrix4.matrix);
    }

    public final void mul(Matrix4 var1) {
        float var2 = this.matrix[0][0] * var1.matrix[0][0] + this.matrix[0][1] * var1.matrix[1][0] + this.matrix[0][2] * var1.matrix[2][0] + this.matrix[0][3] * var1.matrix[3][0];
        float var3 = this.matrix[0][0] * var1.matrix[0][1] + this.matrix[0][1] * var1.matrix[1][1] + this.matrix[0][2] * var1.matrix[2][1] + this.matrix[0][3] * var1.matrix[3][1];
        float var4 = this.matrix[0][0] * var1.matrix[0][2] + this.matrix[0][1] * var1.matrix[1][2] + this.matrix[0][2] * var1.matrix[2][2] + this.matrix[0][3] * var1.matrix[3][2];
        float var5 = this.matrix[0][0] * var1.matrix[0][3] + this.matrix[0][1] * var1.matrix[1][3] + this.matrix[0][2] * var1.matrix[2][3] + this.matrix[0][3] * var1.matrix[3][3];
        float var6 = this.matrix[1][0] * var1.matrix[0][0] + this.matrix[1][1] * var1.matrix[1][0] + this.matrix[1][2] * var1.matrix[2][0] + this.matrix[1][3] * var1.matrix[3][0];
        float var7 = this.matrix[1][0] * var1.matrix[0][1] + this.matrix[1][1] * var1.matrix[1][1] + this.matrix[1][2] * var1.matrix[2][1] + this.matrix[1][3] * var1.matrix[3][1];
        float var8 = this.matrix[1][0] * var1.matrix[0][2] + this.matrix[1][1] * var1.matrix[1][2] + this.matrix[1][2] * var1.matrix[2][2] + this.matrix[1][3] * var1.matrix[3][2];
        float var9 = this.matrix[1][0] * var1.matrix[0][3] + this.matrix[1][1] * var1.matrix[1][3] + this.matrix[1][2] * var1.matrix[2][3] + this.matrix[1][3] * var1.matrix[3][3];
        float var10 = this.matrix[2][0] * var1.matrix[0][0] + this.matrix[2][1] * var1.matrix[1][0] + this.matrix[2][2] * var1.matrix[2][0] + this.matrix[2][3] * var1.matrix[3][0];
        float var11 = this.matrix[2][0] * var1.matrix[0][1] + this.matrix[2][1] * var1.matrix[1][1] + this.matrix[2][2] * var1.matrix[2][1] + this.matrix[2][3] * var1.matrix[3][1];
        float var12 = this.matrix[2][0] * var1.matrix[0][2] + this.matrix[2][1] * var1.matrix[1][2] + this.matrix[2][2] * var1.matrix[2][2] + this.matrix[2][3] * var1.matrix[3][2];
        float var13 = this.matrix[2][0] * var1.matrix[0][3] + this.matrix[2][1] * var1.matrix[1][3] + this.matrix[2][2] * var1.matrix[2][3] + this.matrix[2][3] * var1.matrix[3][3];
        float var14 = this.matrix[3][0] * var1.matrix[0][0] + this.matrix[3][1] * var1.matrix[1][0] + this.matrix[3][2] * var1.matrix[2][0] + this.matrix[3][3] * var1.matrix[3][0];
        float var15 = this.matrix[3][0] * var1.matrix[0][1] + this.matrix[3][1] * var1.matrix[1][1] + this.matrix[3][2] * var1.matrix[2][1] + this.matrix[3][3] * var1.matrix[3][1];
        float var16 = this.matrix[3][0] * var1.matrix[0][2] + this.matrix[3][1] * var1.matrix[1][2] + this.matrix[3][2] * var1.matrix[2][2] + this.matrix[3][3] * var1.matrix[3][2];
        float var17 = this.matrix[3][0] * var1.matrix[0][3] + this.matrix[3][1] * var1.matrix[1][3] + this.matrix[3][2] * var1.matrix[2][3] + this.matrix[3][3] * var1.matrix[3][3];
        this.matrix[0][0] = var2;
        this.matrix[0][1] = var3;
        this.matrix[0][2] = var4;
        this.matrix[0][3] = var5;
        this.matrix[1][0] = var6;
        this.matrix[1][1] = var7;
        this.matrix[1][2] = var8;
        this.matrix[1][3] = var9;
        this.matrix[2][0] = var10;
        this.matrix[2][1] = var11;
        this.matrix[2][2] = var12;
        this.matrix[2][3] = var13;
        this.matrix[3][0] = var14;
        this.matrix[3][1] = var15;
        this.matrix[3][2] = var16;
        this.matrix[3][3] = var17;
    }
    
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    public Vector4 getColumn(int index) {
        return new Vector4(matrix[0][index], matrix[1][index], matrix[2][index], matrix[3][index]);
    }

}
