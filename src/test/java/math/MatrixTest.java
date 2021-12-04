package math;

import com.company.math.matrix.Matrix3;
import com.company.math.matrix.Matrix4;
import com.company.math.vector.Vector3;
import com.company.math.vector.Vector4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MatrixTest {

    @Test
    public void testDeterminant3() {
        double[][] d = {{1,2,3},{4,5,6},{7,8,9}};
        Matrix3 m = new Matrix3(d);
        double det = m.determinant();
        Assertions.assertEquals(0, det);
    }

    @Test
    public void testDeterminant4() {
        double[][] d = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        Matrix4 m = new Matrix4(d);
        double det = m.determinant();
        Assertions.assertEquals(-192, det);
    }

    @Test
    public void testZeroMatrix4() {
        double[][] d1 = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        double[][] d2 = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        Matrix4 m1 = new Matrix4(d1);
        Matrix4 m2 = new Matrix4(d2);
        m1.zeroMatrix();
        Assertions.assertEquals(m1, m2);
    }

    @Test
    public void testZeroMatrix3() {
        double[][] d1 = {{1,2,3},{4,9,6},{7,8,9}};
        double[][] d2 = {{0,0,0},{0,0,0},{0,0,0}};
        Matrix3 m1 = new Matrix3(d1);
        Matrix3 m2 = new Matrix3(d2);
        m1.zeroMatrix();
        Assertions.assertEquals(m1, m2);
    }

    @Test
    public void testUnitMatrix4() {
        double[][] d1 = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        double[][] d2 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
        Matrix4 m1 = new Matrix4(d1);
        Matrix4 m2 = new Matrix4(d2);
        m1.unitMatrix();
        Assertions.assertEquals(m1, m2);
    }

    @Test
    public void testUnitMatrix3() {
        double[][] d1 = {{1,2,3},{4,9,6},{7,8,9}};
        double[][] d2 = {{1,0,0},{0,1,0},{0,0,1}};
        Matrix3 m1 = new Matrix3(d1);
        Matrix3 m2 = new Matrix3(d2);
        m1.unitMatrix();
        Assertions.assertEquals(m1, m2);
    }

    @Test
    public void testSum4() {
        double[][] d1 = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        double[][] d2 = {{1,1,1,1},{0,1,0,1},{0,2,2,0},{3,0,0,5}};
        Matrix4 m1 = new Matrix4(d1);
        Matrix4 m2 = new Matrix4(d2);
        double[][] r = {{2,3,4,2},{4,10,6,2},{7,10,11,1},{4,2,3,10}};
        Matrix4 res = new Matrix4(r);
        m1.sum(m2);
        Assertions.assertEquals(m1, res);
    }

    @Test
    public void testSum3() {
        double[][] d1 = {{1,2,3},{4,9,6},{7,8,9}};
        double[][] d2 = {{0,5,7},{2,1,0},{0,8,9}};
        Matrix3 m1 = new Matrix3(d1);
        Matrix3 m2 = new Matrix3(d2);
        double[][] r = {{1,7,10},{6,10,6},{7,16,18}};
        Matrix3 res = new Matrix3(r);
        m1.sum(m2);
        Assertions.assertEquals(m1, res);
    }

    @Test
    public void testSubtraction4() {
        double[][] d1 = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        double[][] d2 = {{1,1,1,1},{0,1,0,1},{0,2,2,0},{3,0,0,5}};
        Matrix4 m1 = new Matrix4(d1);
        Matrix4 m2 = new Matrix4(d2);
        double[][] r = {{0,1,2,0},{4,8,6,0},{7,6,7,1},{-2,2,3,0}};
        Matrix4 res = new Matrix4(r);
        m1.subtraction(m2);
        Assertions.assertEquals(m1, res);
    }

    @Test
    public void testSubtraction3() {
        double[][] d1 = {{1,2,3},{4,9,6},{7,8,9}};
        double[][] d2 = {{0,5,7},{2,1,0},{0,8,9}};
        Matrix3 m1 = new Matrix3(d1);
        Matrix3 m2 = new Matrix3(d2);
        double[][] r = {{1,-3,-4},{2,8,6},{7,0,0}};
        Matrix3 res = new Matrix3(r);
        m1.subtraction(m2);
        Assertions.assertEquals(m1, res);
    }

    @Test
    public void testTransposition4() {
        double[][] d1 = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        Matrix4 m1 = new Matrix4(d1);
        double[][] r = {{1,4,7,1},{2,9,8,2},{3,6,9,3},{1,1,1,5}};
        Matrix4 res = new Matrix4(r);
        m1.transposition();
        Assertions.assertEquals(m1, res);
    }

    @Test
    public void testTransposition3() {
        double[][] d1 = {{1,2,3},{4,9,6},{7,8,9}};
        Matrix3 m1 = new Matrix3(d1);
        double[][] r = {{1,4,7},{2,9,8},{3,6,9}};
        Matrix3 res = new Matrix3(r);
        m1.transposition();
        Assertions.assertEquals(m1, res);
    }

    @Test
    public void testMultiplying4() {
        double[][] d1 = {{1,2,3,1},{4,9,6,1},{7,8,9,1},{1,2,3,5}};
        Matrix4 m1 = new Matrix4(d1);
        Vector4 v1 = new Vector4(1, 2, 3, 4);
        Vector4 v2 = m1.multiplyingOnVector(v1);
        Vector4 res = new Vector4(18, 44, 54, 34);
        m1.transposition();
        Assertions.assertEquals(v2, res);
    }

    @Test
    public void testMultiplying3() {
        double[][] d1 = {{1,2,3},{4,9,6},{7,8,9}};
        Matrix3 m1 = new Matrix3(d1);
        Vector3 v1 = new Vector3(1, 2, 3);
        Vector3 v2 = m1.multiplyingOnVector(v1);
        Vector3 res = new Vector3(14, 40, 50);
        m1.transposition();
        Assertions.assertEquals(v2, res);
    }

    @Test
    public void testSystemOfEquations3() {
        double[][] d1 = {{5,-2,4},{2,3,-1},{3,-1,2}};
        Matrix3 m = new Matrix3(d1);
        Vector3 v = m.systemOfEquations(new Vector3(5,7,3));
        Vector3 res = new Vector3(1,2,1);
        Assertions.assertEquals(v, res);
    }

    @Test
    public void name() {
    }
}
