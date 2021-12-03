package com.company.base;

import com.company.exceptions.PolygonException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon {

    private final List<Integer> indicesVertexes = new ArrayList<>();
    private final List<Integer> indicesTextureVertexes = new ArrayList<>();
    private final List<Integer> indicesNormalVertexes = new ArrayList<>();

    public Polygon() {
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
                throw new PolygonException("Ошибка добавления вершины в полигон: для данного полигона нельзя добавить этот вид координат");
            }
        } else {
            if (indicesOfVertexes.size() != 0) {
                throw new PolygonException("Ошибка добавления вершины в полигон: отсутствует вид координат, который необходим для данного полигона");
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
