package com.company.files.obj;

import com.company.base.Model;
import com.company.base.Polygon;
import com.company.exceptions.ObjWriterException;


import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ObjWriter {

    public static void writeToFile(File file,
                                   final Model model)
            throws ObjWriterException, FileNotFoundException {
        if (model == null) {
            throw new ObjWriterException("Модели не существует");
        }

        PrintStream out = new PrintStream(file);

        List<String> lines = modelToObjLines(model);

        for (String line : lines) {
            out.println(line);
        }
    }

    public static List<String> modelToObjLines(final Model model) {
        List<String> lines = new ArrayList<>();

        lines.addAll(verticesToObjLines(model));

        if (model.isTexturesInPolygons()) {
            lines.addAll(textureVerticesToObjLines(model));
        }

        if (model.isNormalsInPolygons()) {
            lines.addAll(normalsToObjLines(model));
        }

        lines.addAll(polygonsToObjLines(model));

        return lines;
    }

    public static List<String> verticesToObjLines(final Model model) {
        List<String> lines = new ArrayList<>();
        for (int vertexNumber = 0; vertexNumber < model.getCountOfVertices(); vertexNumber++) {
            Vector3f v = model.getVertex(vertexNumber);
            lines.add(vertexToObjLine(v));
        }
        return lines;
    }

    public static String vertexToObjLine(final Vector3f v) {
        return ObjTokens.VERTEX + " " + v.x + " " + v.y + " " + v.z;
    }

    public static List<String> textureVerticesToObjLines(final Model model) {
        List<String> lines = new ArrayList<>();
        for (int vertexNumber = 0; vertexNumber < model.getCountOfTextureVertices(); vertexNumber++) {
            Vector2f v = model.getTextureVertex(vertexNumber);
            lines.add(textureVertexToObjLine(v));
        }
        return lines;
    }

    public static String textureVertexToObjLine(final Vector2f v) {
        return ObjTokens.TEXTURE + " " + v.x + " " + v.y;
    }

    public static List<String> normalsToObjLines(final Model model) {
        List<String> lines = new ArrayList<>();
        for (int vertexNumber = 0; vertexNumber < model.getCountOfNormals(); vertexNumber++) {
            Vector3f v = model.getNormal(vertexNumber);
            lines.add(normalToObjLine(v));
        }
        return lines;
    }

    public static String normalToObjLine(final Vector3f v) {
        return ObjTokens.NORMAL + " " + v.x + " " + v.y + " " + v.z;
    }

    public static List<String> polygonsToObjLines(final Model model) {
        List<String> lines = new ArrayList<>();
        for (int polygonNumber = 0; polygonNumber < model.getCountOfPolygons(); polygonNumber++) {
            Polygon polygon = model.getPolygon(polygonNumber);
            lines.add(polygonToObjLine(polygon));
        }
        return lines;
    }

    public static String polygonToObjLine(final Polygon polygon) {
        StringBuilder line = new StringBuilder(ObjTokens.POLYGON + " ");

        for (int vertexIndex = 0; vertexIndex < polygon.countOfVertices(); vertexIndex++) {
            StringBuilder vertexSB = new StringBuilder();
            vertexSB.append(polygon.getVertexIndex(vertexIndex) + 1);
            if (polygon.isTexturesExists()) {
                vertexSB.append("/" + (polygon.getTextureVertexIndex(vertexIndex) + 1));
            } else if (polygon.isNormalsExists()) {
                vertexSB.append("/");
            }
            if (polygon.isNormalsExists()) {
                vertexSB.append("/" + (polygon.getNormalIndex(vertexIndex) + 1));
            }
            if (vertexIndex > 0) {
                line.append(" ");
            }
            line.append(vertexSB);
        }
        return line.toString();
    }

}
