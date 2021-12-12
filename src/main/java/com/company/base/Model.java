package com.company.base;

import com.company.exceptions.ModelException;
import com.company.math.matrix.Matrix3;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Model {

    private List<Vector3> vertices = new ArrayList<>();
    private List<Vector2> textureVertices = new ArrayList<>();
    private List<Vector3> normals = new ArrayList<>();

    private List<Polygon> polygons = new ArrayList<>();

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
            throw new ModelException("Model error: The polygon being added cannot contain less than 3 vertices");
        }

        if (polygons.size() == 0) {
            texturesInPolygons = p.isTexturesExists();
            normalsInPolygons = p.isNormalsExists();
        } else {
            if (texturesInPolygons && !p.isTexturesExists()) {
                throw new ModelException("Model error: texture coordinates are not specified in the polygon, although they should");
            }
            if (!texturesInPolygons && p.isTexturesExists()) {
                throw new ModelException("Model error: texture coordinates should not be specified in the polygon");
            }
            if (normalsInPolygons && !p.isNormalsExists()) {
                throw new ModelException("Model error: normals are not specified in the polygon, although they should");
            }
            if (!normalsInPolygons && p.isNormalsExists()) {
                throw new ModelException("Model error: normals should not be specified in the polygon");
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

    public void triangulate() throws Exception {
        List<Polygon> allPolygons = new ArrayList<>();
        for (Polygon polygon : polygons) {
            allPolygons.addAll(polygon.triangulate());
        }
        polygons.clear();
        polygons.addAll(allPolygons);
    }

    public void reCalcNormals() {
        normals.clear();
        for (Polygon polygon : polygons) {
            Vector3 v0 = getVertex(polygon.getVertexIndex(0));
            Vector3 v1 = getVertex(polygon.getVertexIndex(1));
            Vector3 v2 = getVertex(polygon.getVertexIndex(2));
            Vector3 dv1 = v1.subtraction(v0);
            Vector3 dv2 = v2.subtraction(v0);
            Vector3 vn = dv1.vectorProduct(dv2);
            vn.normalize();
            int index = normals.size();
            polygon.addReCalcNormal(index);
            normals.add(vn);
        }
        normalsInPolygons = true;
    }

    public List<Vector3> getNormalsOfVertex(final int vertexIndex) {
        List<Vector3> res = new ArrayList<>();
        for (Polygon polygon : polygons) {
            int normalIndex = polygon.getNormalForVertex(vertexIndex);
            if (normalIndex != -1) {
                res.add(normals.get(normalIndex));
            }
        }
        return res;
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

    public Model clone(){
        Model clone = new Model();
        clone.normals = new ArrayList<>(this.normals);
        clone.vertices = new ArrayList<>(this.vertices);
        clone.textureVertices = new ArrayList<>(this.textureVertices);
        clone.polygons = new ArrayList<>(this.polygons);
        clone.normalsInPolygons = normalsInPolygons;
        clone.texturesInPolygons = texturesInPolygons;
        return clone;
    }

    public void shift(Vector3 v){
        vertices = vertices.stream().map(s -> s = s.sum(v)).collect(Collectors.toList());
    }

    public void XStretching(float n){
        vertices.forEach(s -> s.x *= n);
    }

    public void YStretching(float n){
        vertices.forEach(s -> s.y *= n);
    }

    public void ZStretching(float n){
        vertices.forEach(s -> s.z *= n);
    }

    public void rotation(Matrix3 rotationMatrix, Vector3 shift){
        shift(new Vector3(-shift.x, -shift.y, -shift.z));
        vertices = vertices.stream().map(s -> s = rotationMatrix.multiplyingOnVector(s)).collect(Collectors.toList());
        shift(shift);
    }

    public void addModel(Model model){
        model.polygons.forEach(p -> p.shiftIndexes(this.vertices.size(), this.textureVertices.size(), this.normals.size()));
        this.polygons.addAll(model.polygons);
        this.vertices.addAll(model.vertices);
        this.textureVertices.addAll(model.textureVertices);
        this.normals.addAll(model.normals);
        this.normalsInPolygons = normals.size() != 0;
        this.texturesInPolygons = textureVertices.size() != 0;
    }
}
