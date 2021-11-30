package com.company.files.writer;

import com.company.Model;
import com.company.Polygon;
import com.company.Vector2f;
import com.company.Vector3f;
import com.company.exceptions.ObjWriterException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ObjWriter {

    public static final String OBJ_VERTEX_TOKEN = "v";
    public static final String OBJ_TEXTURE_TOKEN = "vt";
    public static final String OBJ_NORMAL_TOKEN = "vn";
    public static final String OBJ_POLYGON_TOKEN = "f";

    public static void saveToFile(File file,
                                  final Model model)
            throws ObjWriterException, FileNotFoundException {
        if (model == null) {
            throw new ObjWriterException("Модели не существует");
        }

        PrintStream out = new PrintStream(file);

        printVertices(out, model);

        if (model.isTexturesInPolygons()) {
            printTextureVertices(out, model);
        }

        if (model.isNormalsInPolygons()) {
            printNormals(out, model);
        }

        printPolygons(out, model);
    }

    private static void printVertices(PrintStream out,
                                      final Model model) {
        for (int vertexNumber = 0; vertexNumber < model.getCountOfVertices(); vertexNumber++) {
            Vector3f v = model.getVertex(vertexNumber);

            StringBuilder sb = new StringBuilder(OBJ_VERTEX_TOKEN + " ");

            sb.append(v.x + " " + v.y + " " + v.z);

            out.println(sb);
        }
    }

    private static void printTextureVertices(PrintStream out,
                                             final Model model) {
        for (int vertexNumber = 0; vertexNumber < model.getCountOfTextureVertices(); vertexNumber++) {
            Vector2f v = model.getTextureVertex(vertexNumber);

            StringBuilder sb = new StringBuilder(OBJ_TEXTURE_TOKEN + " ");

            sb.append(v.x + " " + v.y);

            out.println(sb);
        }
    }

    private static void printNormals(PrintStream out,
                                     final Model model) {
        for (int vertexNumber = 0; vertexNumber < model.getCountOfNormals(); vertexNumber++) {
            Vector3f v = model.getNormal(vertexNumber);

            StringBuilder sb = new StringBuilder(OBJ_NORMAL_TOKEN + " ");

            sb.append(v.x + " " + v.y + " " + v.z);

            out.println(sb);
        }
    }

    private static void printPolygons(PrintStream out,
                                      final Model model) {
        for (int polygonNumber = 0; polygonNumber < model.getCountOfPolygons(); polygonNumber++) {
            Polygon p = model.getPolygon(polygonNumber);

            StringBuilder sb = new StringBuilder(OBJ_POLYGON_TOKEN + " ");

            for (int vertexIndex = 0; vertexIndex < p.countOfVertices(); vertexIndex++) {
                sb.append(p.getVertexIndex(vertexIndex));
                if (p.isTexturesExists()) {
                    sb.append("/" + p.getTextureVertexIndex(vertexIndex));
                } else if (p.isNormalsExists()) {
                    sb.append("/");
                }
                if (p.isNormalsExists()) {
                    sb.append("/" + p.getNormalIndex(vertexIndex));
                }
            }
            out.println(sb);

        }
    }

}
