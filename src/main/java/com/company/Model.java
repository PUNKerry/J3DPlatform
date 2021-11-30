package com.company;



import com.company.exceptions.ModelException;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<Vector3f> vertices = new ArrayList<>();
    private final List<Vector2f> textureVertices = new ArrayList<>();
    private final List<Vector3f> normals = new ArrayList<>();

    private final List<Polygon> polygons = new ArrayList<>();

    private boolean texturesInPolygons = false;
    private boolean normalsInPolygons = false;

    public void addNewVertex(final Vector3f v) {
        vertices.add(v);
    }

    public Vector3f getVertex(final int index) {
        return vertices.get(index);
    }

    public int getCountOfVertices() {
        return vertices.size();
    }

    public void addNewTextureVertex(final Vector2f vt) {
        textureVertices.add(vt);
    }

    public Vector2f getTextureVertex(final int index) {
        return textureVertices.get(index);
    }

    public boolean isTexturesInPolygons() {
        return texturesInPolygons;
    }

    public int getCountOfTextureVertices() {
        return textureVertices.size();
    }

    public void addNewNormal(final Vector3f vn) {
        normals.add(vn);
    }

    public Vector3f getNormal(final int index) {
        return normals.get(index);
    }

    public boolean isNormalsInPolygons() {
        return normalsInPolygons;
    }

    public int getCountOfNormals() {
        return normals.size();
    }

    public void addNewPolygon(final Polygon p) throws Exception {

        if (p.countOfVertices() < 3) {
            throw new ModelException("Ошибка модели: добавляемый полигон не может содержать меньше 3 вершин");
        }

        if (polygons.size() == 0) {
            texturesInPolygons = p.isTexturesExists();
            normalsInPolygons = p.isNormalsExists();
        } else {
            if (texturesInPolygons && !p.isTexturesExists()) {
                throw new ModelException("Ошибка модели: в полигоне не указаны текстурные координаты, хотя должны");
            }
            if (!texturesInPolygons && p.isTexturesExists()) {
                throw new ModelException("Ошибка модели: в полигоне не должны быть указаны текстурные координаты");
            }
            if (normalsInPolygons && !p.isNormalsExists()) {
                throw new ModelException("Ошибка модели: в полигоне не указаны нормали, хотя должны");
            }
            if (!normalsInPolygons && p.isNormalsExists()) {
                throw new ModelException("Ошибка модели: в полигоне не должны быть указаны нормали");
            }
        }

        polygons.add(p);
    }

    public int getCountOfPolygons() {
        return polygons.size();
    }

    public Polygon getPolygon(final int index) {
        return polygons.get(index);
    }
}

//package com.company;
//
//
//import java.util.*;
//
//public class Model {
//
//    public Model() {
//        vertices = new ArrayList<Vector3f>();
//        textureVertices = new ArrayList<Vector2f>();
//        normals = new ArrayList<Vector3f>();
//        polygonVertexIndices = new ArrayList<ArrayList<Integer>>();
//        polygonTextureVertexIndices = new ArrayList<ArrayList<Integer>>();
//        polygonNormalIndices = new ArrayList<ArrayList<Integer>>();
//    }
//
//    ArrayList<Vector3f> vertices;
//    ArrayList<Vector2f> textureVertices;
//    ArrayList<Vector3f> normals;
//
//    // Каждый контейнер - список полигонов, где полигон описывается индексами вершин, текстурных вершин или нормалей.
//    // Индексов для полигона может быть 3 (треугольник) и более.
//    ArrayList<ArrayList<Integer>> polygonVertexIndices;
//    ArrayList<ArrayList<Integer>> polygonTextureVertexIndices;
//    ArrayList<ArrayList<Integer>> polygonNormalIndices;
//}