package com.company.engine;

import com.company.base.Model;
import com.company.base.ModelForDrawing;
import com.company.base.Polygon;
import com.company.exceptions.RenderException;
import com.company.gui.RenderParams;
import com.company.math.matrix.Matrix3;
import com.company.math.matrix.Matrix4;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

import static com.company.engine.GraphicConveyor.*;

public class RenderEngine {

    private final GraphicsContext graphicsContext;
    private final Camera camera;
    private final Light light;
    private final ModelForDrawing modelForDrawing;
    private final RenderParams params;
    private final int width;
    private final int height;
    private final float[][] zBuffer;

    private Vector3 v0; //global coordinates
    private Vector3 v1;
    private Vector3 v2;

    private Vector3 p0; //screen
    private Vector3 p1;
    private Vector3 p2;

    private Vector2 vt0;
    private Vector2 vt1;
    private Vector2 vt2;

    private Vector3 vn0;
    private Vector3 vn1;
    private Vector3 vn2;

    public RenderEngine(GraphicsContext graphicsContext, Camera camera, Light light,
                        ModelForDrawing modelForDrawing, RenderParams params,
                        int width, int height, float[][] zBuffer) {
        this.graphicsContext = graphicsContext;
        this.camera = camera;
        this.light = light;
        this.modelForDrawing = modelForDrawing;
        this.params = params;
        this.width = width;
        this.height = height;
        this.zBuffer = zBuffer;
    }

    public void render() throws RenderException {
        Model model = modelForDrawing.getActualModel();
        Image texture = modelForDrawing.getTexture();
        Matrix4 modelMatrix = rotateScaleTranslate();
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4 modelViewProjectionMatrix = new Matrix4(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        for (int polygonInd = 0; polygonInd < model.getCountOfPolygons(); ++polygonInd) {
            final Polygon polygon = model.getPolygon(polygonInd);

            List<Vector3> resultPoints = new ArrayList<>();

            v0 = model.getVertex(polygon.getVertexIndex(0));
            v1 = model.getVertex(polygon.getVertexIndex(1));
            v2 = model.getVertex(polygon.getVertexIndex(2));

            resultPoints.add(vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, v0), width, height));
            resultPoints.add(vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, v1), width, height));
            resultPoints.add(vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, v2), width, height));

            if (params.drawOnlyMesh) {
                Color color = modelForDrawing.isChangingNow() ? Color.rgb(0, 30, 100, 1.0) : Color.rgb(0, 0, 0, 1.0);

                for (int vertexIndex = 1; vertexIndex < resultPoints.size(); vertexIndex++) {
                    drawLine(color, resultPoints.get(vertexIndex), resultPoints.get(vertexIndex - 1));
                }

                drawLine(color, resultPoints.get(resultPoints.size() - 1), resultPoints.get(0));
            } else {

                List<Vector3> copyResult = new ArrayList<>(resultPoints);

                resultPoints.sort((p1, p2) -> {
                    int res = Integer.compare((int) Math.floor(p1.y), (int) Math.floor(p2.y));
                    if (res != 0) {
                        return res;
                    } else {
                        return Float.compare(p1.x, p2.x);
                    }
                });

                p0 = resultPoints.get(0);
                p1 = resultPoints.get(1);
                p2 = resultPoints.get(2);

                if (params.drawTexture) {
                    if (!model.isTexturesInPolygons()) {
                        throw new RenderException("Can not draw model without texture coordinates");
                    }
                    if (texture == null) {
                        throw new RenderException("Can not draw model without texture");
                    }
                    vt0 = model.getTextureVertex(polygon.getTextureVertexIndex(copyResult.indexOf(p0)));
                    vt1 = model.getTextureVertex(polygon.getTextureVertexIndex(copyResult.indexOf(p1)));
                    vt2 = model.getTextureVertex(polygon.getTextureVertexIndex(copyResult.indexOf(p2)));
                }

                if (params.drawShadows) {
                    v0 = model.getVertex(polygon.getVertexIndex(copyResult.indexOf(p0)));
                    v1 = model.getVertex(polygon.getVertexIndex(copyResult.indexOf(p1)));
                    v2 = model.getVertex(polygon.getVertexIndex(copyResult.indexOf(p2)));


                    List<Vector3> normalsOfPoint = model.getNormalsOfVertex(polygon.getVertexIndex(copyResult.indexOf(p0)));
                    Vector3 res = new Vector3(0, 0, 0);
                    for (Vector3 normal : normalsOfPoint) {
                        res = res.subtraction(normal);
                    }
                    res.normalize();
                    vn0 = res;

                    normalsOfPoint = model.getNormalsOfVertex(polygon.getVertexIndex(copyResult.indexOf(p1)));
                    res = new Vector3(0, 0, 0);
                    for (Vector3 normal : normalsOfPoint) {
                        res = res.subtraction(normal);
                    }
                    res.normalize();
                    vn1 = res;

                    normalsOfPoint = model.getNormalsOfVertex(polygon.getVertexIndex(copyResult.indexOf(p2)));
                    res = new Vector3(0, 0, 0);
                    for (Vector3 normal : normalsOfPoint) {
                        res = res.subtraction(normal);
                    }
                    res.normalize();
                    vn2 = res;
                }

                if (Math.floor(p0.y) == Math.floor(p1.y)) {
                    drawDownDirected(p2, p0, p1, vt2, vt0, vt1, vn2, vn0, vn1, v2, v0, v1);
                } else if (Math.floor(p1.y) == Math.floor(p2.y)) {
                    drawUpDirected(p0, p1, p2, vt0, vt1, vt2, vn0, vn1, vn2, v0, v1, v2);
                } else {
                    Vector3 p3 = new Vector3(calcFormulaX(p0.toVector2(), p2.toVector2(), p1.y), p1.y, calcFormulaZ(p0, p2, p1.y));
                    float scale = p3.subtraction(p0).length() / p2.subtraction(p0).length();
                    Vector2 vt3 = null;
                    if (params.drawTexture) {
                        vt3 = vt2.subtraction(vt0).multiplyingAVectorByAScalar(scale).sum(vt0);
                    }
                    Vector3 vn3 = null;
                    Vector3 v3 = null;
                    if (params.drawShadows) {
                        vn3 = vn2.subtraction(vn0).multiplyingAVectorByAScalar(scale).sum(vn0);
                        v3 = v2.subtraction(v0).multiplyingAVectorByAScalar(scale).sum(v0);
                    }

                    if (p1.x < p3.x) {
                        drawUpDirected(p0, p1, p3, vt0, vt1, vt3, vn0, vn1, vn3, v0, v1, v3);
                        drawDownDirected(p2, p1, p3, vt2, vt1, vt3, vn2, vn1, vn3, v2, v1, v3);
                    } else {
                        drawUpDirected(p0, p3, p1, vt0, vt3, vt1, vn0, vn3, vn1, v0, v3, v1);
                        drawDownDirected(p2, p3, p1, vt2, vt3, vt1, vn2, vn3, vn1, v2, v3, v1);
                    }
                }
                if (params.drawMesh) {
                    Color color = modelForDrawing.isChangingNow() ? Color.rgb(0, 30, 100, 1.0) : Color.BLACK;
                    drawTriangle(color, p0, p1, p2);
                }
            }
        }
    }

    private void drawUpDirected(Vector3 p0, Vector3 p1, Vector3 p2,
                                Vector2 vt0, Vector2 vt1, Vector2 vt2,
                                Vector3 vn0, Vector3 vn1, Vector3 vn2,
                                Vector3 v0, Vector3 v1, Vector3 v2) {
        for (int row = (int) Math.ceil(p0.y); row <= Math.floor(p1.y); row++) {
            if (row < 0 || row >= zBuffer.length) {
                continue;
            }
            draw(p0, p1, p2, vt0, vt1, vt2, vn0, vn1, vn2, v0, v1, v2, row);
        }
    }

    private void drawDownDirected(Vector3 p0, Vector3 p1, Vector3 p2,
                                  Vector2 vt0, Vector2 vt1, Vector2 vt2,
                                  Vector3 vn0, Vector3 vn1, Vector3 vn2,
                                  Vector3 v0, Vector3 v1, Vector3 v2) {
        for (int row = (int) Math.floor(p0.y); row >= Math.ceil(p1.y); row--) {
            if (row < 0 || row >= zBuffer.length) {
                continue;
            }
            draw(p0, p1, p2, vt0, vt1, vt2, vn0, vn1, vn2, v0, v1, v2, row);
        }
    }

    private void draw(Vector3 p0, Vector3 p1, Vector3 p2,
                      Vector2 vt0, Vector2 vt1, Vector2 vt2,
                      Vector3 vn0, Vector3 vn1, Vector3 vn2,
                      Vector3 v0, Vector3 v1, Vector3 v2,
                      int row) {
        float left = calcFormulaX(p0.toVector2(), p1.toVector2(), row);
        float right = calcFormulaX(p0.toVector2(), p2.toVector2(), row);
        for (int col = (int) Math.ceil(left); col <= Math.floor(right); col++) {
            if (row < 0 || row >= zBuffer.length || col < 0 || col >= zBuffer[0].length) {
                continue;
            }
            Vector2 o = new Vector2(col, row);
            float z = calcZOnSurface(p0, p1, p2, o);
            if (zBuffer[row][col] > z) {
                zBuffer[row][col] = z;
                Color color = null;
                if (params.drawTexture) {
                    Vector2 dp1 = p1.subtraction(p0).toVector2();
                    Vector2 dp2 = p2.subtraction(p0).toVector2();
                    Vector2 dO = o.subtraction(p0.toVector2());
                    float alpha = calcAlpha(dO, dp1, dp2);
                    float beta = calcBeta(dO, dp1, dp2);
                    Vector2 pointOnTexture = vt0.multiplyingAVectorByAScalar(1 - alpha - beta)
                            .sum(vt1.multiplyingAVectorByAScalar(alpha))
                            .sum(vt2.multiplyingAVectorByAScalar(beta));
                    pointOnTexture.x = pointOnTexture.x * (float) modelForDrawing.getTexture().getWidth();
                    pointOnTexture.y = (float) modelForDrawing.getTexture().getHeight() * (1 - pointOnTexture.y);
                    color = modelForDrawing.getTexture().getPixelReader().getColor((int) Math.floor(pointOnTexture.x),
                            (int) Math.floor(pointOnTexture.y));
                } else {
                    color = params.fillingColor;
                }
                if (params.drawShadows) {
                    Vector2 dp1 = p1.subtraction(p0).toVector2();
                    Vector2 dp2 = p2.subtraction(p0).toVector2();
                    Vector2 dO = o.subtraction(p0.toVector2());
                    float alpha = calcAlpha(dO, dp1, dp2);
                    float beta = calcBeta(dO, dp1, dp2);

                    Vector2 global = v0.multiplyingAVectorByAScalar(1 - alpha - beta)
                            .sum(v1.multiplyingAVectorByAScalar(alpha))
                            .sum(v2.multiplyingAVectorByAScalar(beta)).toVector2();
                    Vector3 currInGlobal = new Vector3(global.x, global.y, calcZOnSurface(v0, v1, v2, global));
                    Vector3 toLight = light.getPosition().subtraction(currInGlobal);
                    toLight.normalize();

                    Vector3 vn = vn0.multiplyingAVectorByAScalar(1 - alpha - beta)
                            .sum(vn1.multiplyingAVectorByAScalar(alpha))
                            .sum(vn2.multiplyingAVectorByAScalar(beta));

                    vn.normalize();
                    float opacity = vn.scalarProduct(toLight);
                    if (opacity < 0) {
                        color = Color.BLACK;
                    } else {
                        color = Color.rgb((int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                                (int) (color.getBlue() * 255), (1 - 0.6 * opacity));
                    }
                }
                graphicsContext.getPixelWriter().setColor((int) o.x, (int) o.y, color);
            }
        }
    }

    private void drawTriangle(Color color,
                              Vector3 p0, Vector3 p1, Vector3 p2) {
        drawLine(color, p0, p1, p2);
        drawLine(color, p1, p2, p0);
        drawLine(color, p2, p0, p1);
    }

    private void drawLine(Color color,
                          Vector3 p0, Vector3 p1, Vector3 p2) {
        Vector2 dir = p1.subtraction(p0).toVector2();
        float k = Math.abs(dir.y / dir.x);
        if (Float.isNaN(k) || k > 1) {
            for (int row = (int) Math.ceil(Math.min(p0.y, p1.y)); row <= Math.ceil(Math.max(p0.y, p1.y)); row++) {
                int col = (int) Math.ceil(calcFormulaX(p0.toVector2(), p1.toVector2(), row));
                if (row < 0 || row >= zBuffer.length || col < 0 || col >= zBuffer[0].length) {
                    continue;
                }
                float z = calcZOnSurface(p0, p1, p2, new Vector2(col, row));
                if (zBuffer[row][col] >= z) {
                    zBuffer[row][col] = z;
                    graphicsContext.getPixelWriter().setColor(col, row, color);
                }
            }
        } else {
            for (int col = (int) Math.ceil(Math.min(p0.x, p1.x)); col <= Math.ceil(Math.max(p0.x, p1.x)); col++) {
                int row = (int) Math.ceil(calcFormulaY(p0.toVector2(), p1.toVector2(), col));
                if (row < 0 || row >= zBuffer.length || col < 0 || col >= zBuffer[0].length) {
                    continue;
                }
                float z = calcZOnSurface(p0, p1, p2, new Vector2(col, row));
                if (zBuffer[row][col] >= z) {
                    zBuffer[row][col] = z;
                    graphicsContext.getPixelWriter().setColor(col, row, color);
                }
            }
        }
    }

    private void drawLine(Color color,
                                 Vector3 p0, Vector3 p1) {
        Vector2 dir = p1.subtraction(p0).toVector2();
        float k = Math.abs(dir.y / dir.x);
        if (Float.isNaN(k) || k > 1) {
            for (int row = (int) Math.floor(Math.min(p0.y, p1.y)); row <= Math.ceil(Math.max(p0.y, p1.y)); row++) {
                int col = (int) Math.ceil(calcFormulaX(p0.toVector2(), p1.toVector2(), row));
                graphicsContext.getPixelWriter().setColor(col, row, color);
            }
        } else {
            for (int col = (int) Math.floor(Math.min(p0.x, p1.x)); col <= Math.ceil(Math.max(p0.x, p1.x)); col++) {
                int row = (int) Math.ceil(calcFormulaY(p0.toVector2(), p1.toVector2(), col));
                graphicsContext.getPixelWriter().setColor(col, row, color);
            }
        }
    }

    private static float calcFormulaX(final Vector2 p0,
                                      final Vector2 p1,
                                      final float y) {
        return (y - p0.y) * (p1.x - p0.x) / (p1.y - p0.y) + p0.x;
    }

    private static float calcFormulaY(final Vector2 p0,
                                      final Vector2 p1,
                                      final float x) {
        return (x - p0.x) * (p1.y - p0.y) / (p1.x - p0.x) + p0.y;
    }

    private static float calcFormulaZ(final Vector3 p1,
                                      final Vector3 p2,
                                      final float y) {
        Vector3 p = p2.subtraction(p1);
        return p.z * (y - p1.y) / p.y + p1.z;
    }

    private static float calcZOnSurface(final Vector3 p0,
                                        final Vector3 p1,
                                        final Vector3 p2,
                                        final Vector2 v) {
        Vector3 dp1 = p1.subtraction(p0);
        Vector3 dp2 = p2.subtraction(p0);
        return (-(v.x - p0.x) * Matrix3.determinant2(new float[][] {{dp1.y, dp2.y}, {dp1.z, dp2.z}})
                + (v.y - p0.y) * Matrix3.determinant2(new float[][] {{dp1.x, dp2.x}, {dp1.z, dp2.z}}))
                / Matrix3.determinant2(new float[][] {{dp1.x, dp2.x}, {dp1.y, dp2.y}})
                + p0.z;
    }

    private static float calcBeta(final Vector2 dO,
                                  final Vector2 dp1,
                                  final Vector2 dp2) {
        return (dp1.y * dO.x - dp1.x * dO.y) / (dp1.y * dp2.x - dp2.y * dp1.x);
    }

    private static float calcAlpha(final Vector2 dO,
                                   final Vector2 dp1,
                                   final Vector2 dp2) {
        return (dp2.x * dO.y - dp2.y * dO.x) / (dp1.y * dp2.x - dp2.y * dp1.x);
    }
}