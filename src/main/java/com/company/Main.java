package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {
        Path fileName = Path.of("ObjModels\\WrapJaw.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        System.out.println("All good?");
//        String s = """
//                v 4.721225 1.69754 4.41113
//                vt 4.721225 1.69754 4
//                vn 4.721225 1.69754 4.5
//                v 4.721225 1.69754 4.41113
//                vt 4.721225 1.69754 4
//                vn 4.721225 1.69754 4.5
//                v 4.721225 1.69754 4.41113
//                vt 4.721225 1.69754 4
//                vn 4.721225 1.69754 4.5
//                f  1/2/3 1/2/3 1/2/3""";
//            Model m = ObjReader.read(s);
    }
}
