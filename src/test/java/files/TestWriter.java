package files;

import com.company.base.Model;
import com.company.base.Polygon;
import com.company.files.obj.ObjWriter;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.vecmath.Vector2f;
import java.util.List;


public class TestWriter {

    @Test
    public void testWrite1() {
        Vector3 v = new Vector3(1, 3, 6);
        Assertions.assertEquals("v 1.0 3.0 6.0", ObjWriter.vertexToObjLine(v));
    }

    @Test
    public void testWrite2() {
        Vector3 v = new Vector3(0, 0, 0);
        Assertions.assertEquals("v 0.0 0.0 0.0", ObjWriter.vertexToObjLine(v));
    }

    @Test
    public void testWrite3() {
        Vector2 v = new Vector2(5, 2);
        Assertions.assertEquals("vt 5.0 2.0", ObjWriter.textureVertexToObjLine(v));
    }

    @Test
    public void testWrite4() {
        Vector2 v = new Vector2(0, 0);
        Assertions.assertEquals("vt 0.0 0.0", ObjWriter.textureVertexToObjLine(v));
    }

    @Test
    public void testWrite5() {
        Vector3 v = new Vector3(10, 9, 6);
        Assertions.assertEquals("vn 10.0 9.0 6.0", ObjWriter.normalToObjLine(v));
    }

    @Test
    public void testWrite6() {
        Vector3 v = new Vector3(0, 0, 0);
        Assertions.assertEquals("vn 0.0 0.0 0.0", ObjWriter.normalToObjLine(v));
    }

    @Test
    public void testWrite7() {
        Model model = new Model();
        Vector3 v1 = new Vector3(5, 3, 0);
        Vector3 v2 = new Vector3(4, 2, 1);
        Vector3 v3 = new Vector3(0, 0, 0);
        model.addNewVertex(v1);
        model.addNewVertex(v2);
        model.addNewVertex(v3);
        List<String> writerLines = ObjWriter.verticesToObjLines(model);
        List<String> myLines = List.of("v 5.0 3.0 0.0", "v 4.0 2.0 1.0", "v 0.0 0.0 0.0");
        Assertions.assertEquals(myLines, writerLines);
    }

    @Test
    public void testWrite8() {
        Model model = new Model();
        Vector2 v1 = new Vector2(5, 3);
        Vector2 v2 = new Vector2(4, 2);
        Vector2 v3 = new Vector2(0, 0);
        model.addNewTextureVertex(v1);
        model.addNewTextureVertex(v2);
        model.addNewTextureVertex(v3);
        List<String> writerLines = ObjWriter.textureVerticesToObjLines(model);
        List<String> myLines = List.of("vt 5.0 3.0", "vt 4.0 2.0", "vt 0.0 0.0");
        Assertions.assertEquals(myLines, writerLines);
    }

    @Test
    public void testWrite9() {
        Model model = new Model();
        Vector3 v1 = new Vector3(5, 3, 0);
        Vector3 v2 = new Vector3(4, 2, 1);
        Vector3 v3 = new Vector3(0, 0, 0);
        model.addNewNormal(v1);
        model.addNewNormal(v2);
        model.addNewNormal(v3);
        List<String> writerLines = ObjWriter.normalsToObjLines(model);
        List<String> myLines = List.of("vn 5.0 3.0 0.0", "vn 4.0 2.0 1.0", "vn 0.0 0.0 0.0");
        Assertions.assertEquals(myLines, writerLines);
    }

    @Test
    public void testWrite10() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(2, 2, 1);
        Assertions.assertEquals("f 2/2/1", ObjWriter.polygonToObjLine(p));
    }

    @Test
    public void testWrite11() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(2, 2, 3);
        p.addNewVertex(5, 1, 5);
        p.addNewVertex(4, 3, 2);
        Assertions.assertEquals("f 2/2/3 5/1/5 4/3/2", ObjWriter.polygonToObjLine(p));
    }

    @Test
    public void testWrite12() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(2, -1, 3);
        p.addNewVertex(5, -1, 5);
        p.addNewVertex(4, -1, 2);
        Assertions.assertEquals("f 2//3 5//5 4//2", ObjWriter.polygonToObjLine(p));
    }

    @Test
    public void testWrite13() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(2, -1, -1);
        p.addNewVertex(5, -1, -1);
        p.addNewVertex(4, -1, -1);
        Assertions.assertEquals("f 2 5 4", ObjWriter.polygonToObjLine(p));
    }

    @Test
    public void testWrite14() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(2, 5, -1);
        p.addNewVertex(5, 4, -1);
        p.addNewVertex(4, 2, -1);
        Assertions.assertEquals("f 2/5 5/4 4/2", ObjWriter.polygonToObjLine(p));
    }

    @Test
    public void testWrite15() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(9, -1, -1);
        p.addNewVertex(1, -1, -1);
        p.addNewVertex(4, -1, -1);
        p.addNewVertex(8, -1, -1);
        p.addNewVertex(2, -1, -1);
        Assertions.assertEquals("f 9 1 4 8 2", ObjWriter.polygonToObjLine(p));
    }

    @Test
    public void testWrite16() throws Exception {
        Polygon p = new Polygon();
        p.addNewVertex(9, 2, -1);
        p.addNewVertex(1, 2, -1);
        p.addNewVertex(4, 2, -1);
        p.addNewVertex(8, 2, -1);
        p.addNewVertex(2, 2, -1);
        Assertions.assertEquals("f 9/2 1/2 4/2 8/2 2/2", ObjWriter.polygonToObjLine(p));
    }
}