package math;

import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;
import com.company.math.vector.Vector4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class VectorsTest {

    @Test
    public void testLength4() {
        Vector4 v = new Vector4(1, 1, 1, 1);
        double l = v.length();
        Assertions.assertEquals(2, l);
    }

    @Test
    public void testLength3() {
        Vector3 v = new Vector3(6, 2, 3);
        double l = v.length();
        Assertions.assertEquals(7, l);
    }

    @Test
    public void testLength2() {
        Vector2 v = new Vector2(3, 4);
        double l = v.length();
        Assertions.assertEquals(5, l);
    }

    @Test
    public void testVectorProduct() {
        Vector3 v1 = new Vector3(1, 2, 3);
        Vector3 v2 = new Vector3(4, 5, 6);
        Vector3 v3 = v2.vectorProduct(v1);
        Vector3 res = new Vector3(-3, 6, -3);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testSum2() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(4, 5);
        Vector2 v3 = v1.sum(v2);
        Vector2 res = new Vector2(5, 7);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testSum3() {
        Vector3 v1 = new Vector3(1, 2, 3);
        Vector3 v2 = new Vector3(4, 5, 6);
        Vector3 v3 = v1.sum(v2);
        Vector3 res = new Vector3(5, 7, 9);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testSum4() {
        Vector4 v1 = new Vector4(1, 2, 3, 4);
        Vector4 v2 = new Vector4(4, 5, 6, 7);
        Vector4 v3 = v1.sum(v2);
        Vector4 res = new Vector4(5, 7, 9, 11);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testSubtraction2() {
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(4, 5);
        Vector2 v3 = v1.subtraction(v2);
        Vector2 res = new Vector2(-3, -3);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testSubtraction3() {
        Vector3 v1 = new Vector3(1, 2, 11);
        Vector3 v2 = new Vector3(4, 10, 6);
        Vector3 v3 = v1.subtraction(v2);
        Vector3 res = new Vector3(-3, -8, 5);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testSubtraction4() {
        Vector4 v1 = new Vector4(1, 2, 3, 4);
        Vector4 v2 = new Vector4(6, 4, 1, 3);
        Vector4 v3 = v1.subtraction(v2);
        Vector4 res = new Vector4(-5, -2, 2, 1);
        Assertions.assertEquals(res, v3);
    }

    @Test
    public void testMultiplying2() {
        Vector2 v = new Vector2(1, 2);
        v.multiplyingAVectorByAScalar(5);
        Vector2 res = new Vector2(5, 10);
        Assertions.assertEquals(res, v);
    }

    @Test
    public void testMultiplying3() {
        Vector3 v = new Vector3(1, 2, 11);
        v.multiplyingAVectorByAScalar(2);
        Vector3 res = new Vector3(2, 4, 22);
        Assertions.assertEquals(res, v);
    }

    @Test
    public void testMultiplying4() {
        Vector4 v = new Vector4(1, 2, 3, 4);
        v.multiplyingAVectorByAScalar(-5);
        Vector4 res = new Vector4(-5, -10, -15, -20);
        Assertions.assertEquals(res, v);
    }

    @Test
    public void testScalarProduct2() {
        Vector2 v1 = new Vector2(0, 4);
        Vector2 v2 = new Vector2(3, 4);
        double res = v1.scalarProduct(v2);
        double result = 0.8;
        Assertions.assertEquals(res, result);
    }

    @Test
    public void testScalarProduct3() {
        Vector3 v1 = new Vector3(0, 4, 0);
        Vector3 v2 = new Vector3(2, 0, 6);
        double res = v1.scalarProduct(v2);
        double result = 0;
        Assertions.assertEquals(res, result);
    }

    @Test
    public void testScalarProduct4() {
        Vector4 v1 = new Vector4(1, 1, 1, 1);
        Vector4 v2 = new Vector4(2, 2, 2, 2);
        double res = v1.scalarProduct(v2);
        double result = 1;
        Assertions.assertEquals(res, result);
    }

    @Test
    public void testDividing4() {
        Vector4 v = new Vector4(1, 10, 5, 15);
        v.dividingAVectorByAScalar(5);
        Vector4 res = new Vector4((float) 0.2, 2, 1, 3);
        Assertions.assertEquals(res, v);
    }

    @Test
    public void testNormalization4() {
        Vector4 v = new Vector4(1, 0, 0, 0);
        v.normalization();
        Vector4 res = new Vector4(1, 0, 0, 0);
        Assertions.assertEquals(res, v);
    }
}