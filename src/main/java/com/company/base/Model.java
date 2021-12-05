package com.company.base;

import com.company.exceptions.ModelException;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Model {

    private final List<Vector3> vertices = new ArrayList<>();
    private final List<Vector2> textureVertices = new ArrayList<>();
    private final List<Vector3> normals = new ArrayList<>();

    private final List<Polygon> polygons = new ArrayList<>();

    private boolean texturesInPolygons = false;
    private boolean normalsInPolygons = false;

    public void addNewVertex(final Vector3 v) {
        vertices.add(v);
    }

    public Vector3 getVertex(final int index) {
        return vertices.get(index);
    }

    public int getCountOfVertices() {
        return vertices.size();
    }

    public void addNewTextureVertex(final Vector2 vt) {
        textureVertices.add(vt);
    }

    public Vector2 getTextureVertex(final int index) {
        return textureVertices.get(index);
    }

    public boolean isTexturesInPolygons() {
        return texturesInPolygons;
    }

    public int getCountOfTextureVertices() {
        return textureVertices.size();
    }

    public void addNewNormal(final Vector3 vn) {
        normals.add(vn);
    }

    public Vector3 getNormal(final int index) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return texturesInPolygons == model.texturesInPolygons && normalsInPolygons == model.normalsInPolygons && Objects.equals(vertices, model.vertices) && Objects.equals(textureVertices, model.textureVertices) && Objects.equals(normals, model.normals) && Objects.equals(polygons, model.polygons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, textureVertices, normals, polygons, texturesInPolygons, normalsInPolygons);
    }
}
