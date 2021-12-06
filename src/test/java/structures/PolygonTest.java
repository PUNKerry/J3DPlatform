package structures;

import com.company.base.Polygon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PolygonTest {
    //1 step of testing - test in adding vertexes to poly
    @Test
    void testVertCounter1() {
        Polygon polygon = new Polygon();
        int count = 5;
        for (int i = 0; i < count; i++) {
            try {
                polygon.addNewVertex(i,i,i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Assertions.assertEquals(count, polygon.countOfVertices());
    }

    @Test
    void testVertCounter2() {
        Polygon polygon = new Polygon();
        int count = 288;
        for (int i = 0; i < count; i++) {
            try {
                polygon.addNewVertex(i,i,i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Assertions.assertEquals(count, polygon.countOfVertices());
    }

    @Test
    void testVertCounter3() {
        Polygon polygon = new Polygon();
        int count = 12849024;
        for (int i = 0; i < count; i++) {
            try {
                polygon.addNewVertex(i,i,i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Assertions.assertEquals(count, polygon.countOfVertices());
    }
    //2 step of testing - checking indexes work in poly struct (checking getters)
    @Test
    void getVertexIndexTest() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,2,3);
        Assertions.assertEquals(1,poly.getVertexIndex(0));
    }

    @Test
    void getNormalIndexTest() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,2,3);
        Assertions.assertEquals(3,poly.getNormalIndex(0));
    }

    @Test
    void getTextureIndexTest() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,2,3);
        Assertions.assertEquals(2,poly.getTextureVertexIndex(0));
    }

    @Test
    void addAndSaveNewVertexTest() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,1,1);
        boolean fCheck = poly.getVertexIndex(0) == 1;
        boolean sCheck = poly.getNormalIndex(0) == 1;
        boolean tCheck = poly.getTextureVertexIndex(0) == 1;
        boolean result = fCheck && sCheck && tCheck;

        Assertions.assertTrue(result);
    }
    //check is... methods on true and false
    @Test
    void booleanTextCheckTest1() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,-1,1);
        Assertions.assertFalse(poly.isTexturesExists());
    }

    @Test
    void booleanTextCheckTest2() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,1,1);
        Assertions.assertTrue(poly.isTexturesExists());
    }

    @Test
    void booleanNormalsCheckTest1() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,1,-1);
        Assertions.assertFalse(poly.isNormalsExists());
    }

    @Test
    void booleanNormalsCheckTest2() throws Exception {
        Polygon poly = new Polygon();
        poly.addNewVertex(1,1,1);
        Assertions.assertTrue(poly.isNormalsExists());
    }

    @Test
    void triangulationTest1() throws Exception {
        Polygon polygon = new Polygon();
        polygon.addNewVertex(1, -1, -1);
        polygon.addNewVertex(2, -1, -1);
        polygon.addNewVertex(3, -1, -1);
        polygon.addNewVertex(4, -1, -1);
        polygon.addNewVertex(5, -1, -1);
        List<Polygon> polygons = polygon.triangulate();
        Assertions.assertEquals(polygons.size(), 3);
        for (Polygon triangulated : polygons) {
            Assertions.assertEquals(triangulated.countOfVertices(), 3);
        }
    }
}
