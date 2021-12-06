package files;

import com.company.base.Model;
import com.company.base.Polygon;
import com.company.files.obj.ObjReader;
import com.company.exceptions.ObjReaderException;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector2f;
import java.nio.file.Path;
import java.util.ArrayList;
import static com.company.files.obj.ObjReader.*;

public class TestReader {
    @Test
    public void testRead1() throws Exception {
        String s = """
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                f  1/2/5 1/2/3 1/2/3""";
        try {
            Model m = ObjReader.read(s);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file: A normal index is specified that exceeds the number of vertices";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testRead2() throws Exception {
        String s = """
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                f  1/5/3 1/2/3 1/2/3""";
        try {
            Model m = ObjReader.read(s);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file: A texture index is specified that exceeds the number of vertices";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testRead3() throws Exception {
        String s = """
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                f  5/2/3 1/2/3 1/2/3""";
        try {
            Model m = ObjReader.read(s);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file: A vertex index is specified that exceeds the number of vertices";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testRead4() throws Exception {
        String s = """
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                f  1/2/3 1/2/3 1/2/3""";
        try {
            Model m = ObjReader.read(s);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file: the quantity of vertices and normals does not match";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testReadFromFile() throws Exception {
        String s = """
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                v 4.721225 1.69754 4.41113
                vt 4.721225 1.69754 4
                vn 4.721225 1.69754 4.5
                f  1/2/3 1/2/3 1/2/3""";

        Model m1 = ObjReader.read(s);
        Path fileName = Path.of("src\\main\\resources\\com\\company\\models\\Test.obj");
        Model m2 = ObjReader.readFromFile(fileName);
        Assertions.assertEquals(m1, m2);
    }

    @Test
    public void testParseVertex1() {
        ArrayList<String> wordsInLine = new ArrayList<>();
        wordsInLine.add("1.12313");
        wordsInLine.add("1.234234");
        wordsInLine.add("2.36242");
        Vector3 res = parseVertex(wordsInLine, 1);
        Vector3 expected = new Vector3(1.12313f, 1.234234f, 2.36242f);
        Assertions.assertEquals(res , expected);
    }

    @Test
    public void testParseVertex2() {
        ArrayList<String> wordsInLine = new ArrayList<>();
        wordsInLine.add("1.12313");
        wordsInLine.add("1.234234");
        try {
            Vector3 res = parseVertex(wordsInLine, 1);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 1. Too few vertex arguments.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testParseNormal1() {
        ArrayList<String> wordsInLine = new ArrayList<>();
        wordsInLine.add("1.12313");
        wordsInLine.add("1.234234");
        wordsInLine.add("2.36242");
        Vector3 res = parseNormal(wordsInLine, 1);
        Vector3 expected = new Vector3(1.12313f, 1.234234f, 2.36242f);
        Assertions.assertEquals(res , expected);
    }

    @Test
    public void testParseTexture1() {
        ArrayList<String> wordsInLine = new ArrayList<>();
        wordsInLine.add("1.12313");
        wordsInLine.add("1.234234");
        Vector2 res = parseTextureVertex(wordsInLine, 1);
        Vector2 expected = new Vector2(1.12313f, 1.234234f);
        Assertions.assertEquals(res , expected);
    }

    @Test
    public void testParseNormal2() {
        ArrayList<String> wordsInLine = new ArrayList<>();
        wordsInLine.add("1.12313");
        wordsInLine.add("1.234234");
        try {
            Vector3 res = parseNormal(wordsInLine, 1);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 1. Too few normal arguments.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testParseTexture2() {
        ArrayList<String> wordsInLine = new ArrayList<>();
        wordsInLine.add("1.12313");
        try {
            Vector2 res = parseTextureVertex(wordsInLine, 1);
        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 1. Too few texture vertex arguments.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testParseFaceWord3() throws Exception {
        String wordsInLine = "1/1/1";
        Polygon res = new Polygon();
        parseFaceWord(wordsInLine, 1, res);

        Polygon expected = new Polygon();
        expected.addNewVertex(0,0,0);
        Assertions.assertEquals(expected, res);
    }

    @Test
    public void testParseFaceWord2() throws Exception {
        String wordsInLine = "3/3";
        Polygon res = new Polygon();
        parseFaceWord(wordsInLine, 1, res);

        Polygon expected = new Polygon();
        expected.addNewVertex(2,2,-1);
        Assertions.assertEquals(expected, res);
    }

    @Test
    public void testParseFaceWord1() throws Exception {
        String wordsInLine = "5";
        Polygon res = new Polygon();
        parseFaceWord(wordsInLine, 1, res);

        Polygon expected = new Polygon();
        expected.addNewVertex(4,-1,-1);
        Assertions.assertEquals(expected, res);
    }

    @Test
    public void testParseFaceWord4() throws Exception {
        String wordsInLine = "3//3";
        Polygon res = new Polygon();
        parseFaceWord(wordsInLine, 1, res);

        Polygon expected = new Polygon();
        expected.addNewVertex(2,-1,2);
        Assertions.assertEquals(expected, res);
    }

    @Test
    public void testParseFace1() throws Exception {
        String wordsInLine1 = "3/1/3";
        String wordsInLine2 = "1/1/3";
        String wordsInLine3 = "1/2/3";
        String wordsInLine4 = "3/1/1";
        Polygon expected = new Polygon();
        expected.addNewVertex(2,0,2);
        expected.addNewVertex(0,0,2);
        expected.addNewVertex(0,1,2);
        expected.addNewVertex(2,0,0);
        ArrayList<String> list = new ArrayList<>();
        list.add(wordsInLine1);
        list.add(wordsInLine2);
        list.add(wordsInLine3);
        list.add(wordsInLine4);
        Polygon res = ObjReader.parseFace(list, 10);

        Assertions.assertEquals(expected, res);
    }

}
