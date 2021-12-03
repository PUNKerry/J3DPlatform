package com.company;

import com.company.files.obj.ObjReader;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {
        Path fileName = Path.of("src\\main\\resources\\ObjModels\\WrapBody.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        System.out.println("All good?");
    }
}
