package com.company.base;

import com.company.exceptions.PolygonException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Polygon {

    private List<Integer> indicesVertexes = new ArrayList<>();
    private List<Integer> indicesTextureVertexes = new ArrayList<>();
    private List<Integer> indicesNormalVertexes = new ArrayList<>();

    public Polygon() {
    }

    public void shiftIndexes(int v, int vt, int vn){
        indicesVertexes = indicesVertexes.stream()
                .map(i -> i += v)
                .collect(Collectors.toList());
        indicesTextureVertexes = indicesTextureVertexes.stream()
                .map(i -> i += vt)
                .collect(Collectors.toList());
        indicesNormalVertexes = indicesNormalVertexes.stream()
                .map(i -> i += vn)
                .collect(Collectors.toList());
    }

    public int getVertexIndex(final int index) {
        return indicesVertexes.get(index);
    }

    public int getTextureVertexIndex(final int index) {
        return indicesTextureVertexes.get(index);
    }

    public int getNormalIndex(final int index) {
        return indicesNormalVertexes.get(index);
    }

    public void addNewVertex(final int indexV,
                             final int indexVt,     // -1 - если не указано
                             final int indexVn)     // -1 - если не указано
            throws Exception {
        if (indicesVertexes.size() == 0) {
            if (indexVt != -1) {
                indicesTextureVertexes.add(indexVt);
            }
            if (indexVn != -1) {
                indicesNormalVertexes.add(indexVn);
            }
        } else {
            addToList(indicesTextureVertexes, indexVt);
            addToList(indicesNormalVertexes, indexVn);
        }
        indicesVertexes.add(indexV);
    }

    private void addToList(final List<Integer> indicesOfVertexes,
                           final int index)
            throws Exception {
        if (index != -1) {
            if (indicesOfVertexes.size() != 0) {
                indicesOfVertexes.add(index);
            } else {
                throw new PolygonException("Error adding a vertex to a polygon: this type of coordinates cannot be added for this polygon");
            }
        } else {
            if (indicesOfVertexes.size() != 0) {
                throw new PolygonException("Error adding a vertex to a polygon: there is no coordinate type that is necessary for this polygon");
            }
        }
    }

    public boolean isTexturesExists() {
        return indicesTextureVertexes.size() != 0;
    }

    public boolean isNormalsExists() {
        return indicesNormalVertexes.size() != 0;
    }

    public int countOfVertices() {
        return indicesVertexes.size();
    }

    public List<Polygon> triangulate() throws Exception {
        if (countOfVertices() > 3) {
            List<Polygon> newPolygons = new ArrayList<>();
            for (int vertexIndex = 2; vertexIndex < countOfVertices(); vertexIndex++) {
                Polygon newPolygon = new Polygon();

                for (int vertexIndexInsideNew = 0; vertexIndexInsideNew != vertexIndex; ) {
                    if (newPolygon.countOfVertices() == 1) {
                        vertexIndexInsideNew = vertexIndex - 1;
                    }
                    if (newPolygon.countOfVertices() == 2) {
                        vertexIndexInsideNew = vertexIndex;
                    }
                    int indexV = getVertexIndex(vertexIndexInsideNew);
                    int indexVt = -1;
                    if (isTexturesExists()) {
                        indexVt = getTextureVertexIndex(vertexIndexInsideNew);
                    }
                    int indexVn = -1;
                    if (isNormalsExists()) {
                        indexVn = getNormalIndex(vertexIndexInsideNew);
                    }
                    newPolygon.addNewVertex(indexV, indexVt, indexVn);
                }

                newPolygons.add(newPolygon);
            }
            return newPolygons;
        } else {
            return List.of(this);
        }
    }

    public void addReCalcNormal(final int normalIndex) {
        indicesNormalVertexes.clear();
        for (int index = 0; index < indicesVertexes.size(); index++) {
            indicesNormalVertexes.add(normalIndex);
        }
    }

    public int getNormalForVertex(final int vertexIndex) {
        int pos = indicesVertexes.indexOf(vertexIndex);
        if (pos != -1) {
            return indicesNormalVertexes.get(pos);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon polygon = (Polygon) o;
        return Objects.equals(indicesVertexes, polygon.indicesVertexes) && Objects.equals(indicesTextureVertexes, polygon.indicesTextureVertexes) && Objects.equals(indicesNormalVertexes, polygon.indicesNormalVertexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicesVertexes, indicesTextureVertexes, indicesNormalVertexes);
    }
}
