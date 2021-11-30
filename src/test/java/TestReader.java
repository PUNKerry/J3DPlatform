import com.company.Model;
import com.company.ObjReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestReader {

    @Test
    public void testRead2() {
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
        try {
            Model m = ObjReader.read(s);
        } catch (Exception exception) {
            String expectedError = "Error parsing OBJ file on line: 8. Normal coordinates specified not for all polygons";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testRead1() {
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
        } catch (Exception exception) {
            String expectedError = "Error parsing OBJ file on line: 10. the index of the N vertex exceeding their number is specified";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }
}
