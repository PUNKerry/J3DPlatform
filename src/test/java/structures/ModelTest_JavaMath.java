package structures;

import com.company.base.Model;
import com.company.base.Polygon;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class ModelTest_JavaMath {
    @Test
    void testAddNormalProcessSt1() {
        Model testable = new Model();
        int count = 15;
        for (int i = 0; i < count; i++) {
            testable.addNewNormal(new Vector3());
        }
        int result = testable.getCountOfNormals();
        Assertions.assertEquals(count, result);
    }

    @Test
    void testAddNormalProcessSt2() {
        Model testable = new Model();
        int count = 99999;
        for (int i = 0; i < count; i++) {
            testable.addNewNormal(new Vector3());
        }
        Assertions.assertEquals(count, testable.getCountOfNormals());
    }

    @Test
    void testAddNormalProcessSt3() {
        Model testable = new Model();
        Vector3 testableVector = new Vector3();
        
        testableVector.x = 1;
        testableVector.y = 2;
        testableVector.z = 3;
        
        testable.addNewNormal(testableVector);
        
        boolean checker = testable.getNormal(0).x == 1 &&
                testable.getNormal(0).y == 2 &&
                testable.getNormal(0).z == 3;
        
        Assertions.assertTrue(checker);
    }

    @Test
    void testAddTextureProcessSt1() {
        Model testable = new Model();
        int count = 15;
        for (int i = 0; i < count; i++) {
            testable.addNewTextureVertex(new Vector2());
        }
        Assertions.assertEquals(count, testable.getCountOfTextureVertices());
    }

    @Test
    void testAddTextureProcessSt2() {
        Model testable = new Model();
        int count = 9999;
        for (int i = 0; i < count; i++) {
            testable.addNewTextureVertex(new Vector2());
        }
        Assertions.assertEquals(count, testable.getCountOfTextureVertices());
    }

    @Test
    void testAddTextureProcessSt3() {
        Model testable = new Model();
        Vector2 testableVector = new Vector2();

        testableVector.x = 1;
        testableVector.y = 2;

        testable.addNewTextureVertex(testableVector);

        boolean checker = testable.getTextureVertex(0).x == 1 &&
                testable.getTextureVertex(0).y == 2;

        Assertions.assertTrue(checker);
    }

    @Test
    void testBooleanTV() {
        Model md = new Model();
        Assertions.assertFalse(md.isTexturesInPolygons());
    }

    @Test
    void testBooleanN() {
        Model md = new Model();
        Assertions.assertFalse(md.isNormalsInPolygons());
    }

    @Test
    void testAddPoly1() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,1,1);
        poly.addNewVertex(2,2,2);
        poly.addNewVertex(3,3,3);
        Model md = new Model();
        md.addNewPolygon(poly);

        Assertions.assertEquals(1, md.getCountOfPolygons());
    }
}
