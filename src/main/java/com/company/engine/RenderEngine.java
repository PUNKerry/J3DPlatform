package com.company.engine;

import com.company.base.Model;
import com.company.base.Polygon;
import com.company.math.entity.Point2;
import com.company.math.matrix.Matrix3;
import com.company.math.matrix.Matrix4;
import com.company.math.vector.Vector2;
import com.company.math.vector.Vector3;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.company.engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model model,
            final Image texture,
            final int width,
            final int height)
    {
        Matrix4 modelMatrix = rotateScaleTranslate();
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4 modelViewProjectionMatrix = new Matrix4(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        float[][] zBuffer = new float[height][width];

        final int nPolygons = model.getCountOfPolygons();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final Polygon polygon = model.getPolygon(polygonInd);

            final int nVerticesInPolygon = polygon.countOfVertices();

            List<Vector3> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = model.getVertex(polygon.getVertexIndex(vertexInPolygonInd));
                Vector3 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);
                resultPoints.add(resultPoint);
            }
            if (texture == null) {
                for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {

                    graphicsContext.strokeLine(
                            resultPoints.get(vertexInPolygonInd - 1).x,
                            resultPoints.get(vertexInPolygonInd - 1).y,
                            resultPoints.get(vertexInPolygonInd).x,
                            resultPoints.get(vertexInPolygonInd).y);

                }
                if (nVerticesInPolygon > 0)
                    graphicsContext.strokeLine(
                            resultPoints.get(nVerticesInPolygon - 1).x,
                            resultPoints.get(nVerticesInPolygon - 1).y,
                            resultPoints.get(0).x,
                            resultPoints.get(0).y);
            } else {

                PixelWriter pixelWriter = graphicsContext.getPixelWriter();
                PixelReader pixelReader = texture.getPixelReader();
                for (float[] arr : zBuffer) {
                    Arrays.fill(arr, Float.MIN_VALUE);
                }

                List<Vector3> copyResult = new ArrayList<>(resultPoints);

                resultPoints.sort((p1, p2) -> {
                    int res = Float.compare(p1.y, p2.y);
                    if (res != 0) {
                        return res;
                    } else {
                        return Float.compare(p1.x, p2.x);
                    }
                });

                Vector3 p0 = resultPoints.get(0);
                Vector3 p1 = resultPoints.get(1);
                Vector3 p2 = resultPoints.get(2);

                Vector2 vt0 = model.getTextureVertex(polygon.getTextureVertexIndex(copyResult.indexOf(p0)));
                Vector2 vt1 = model.getTextureVertex(polygon.getTextureVertexIndex(copyResult.indexOf(p1)));
                Vector2 vt2 = model.getTextureVertex(polygon.getTextureVertexIndex(copyResult.indexOf(p2)));

                if (Math.round(p0.y) == Math.round(p1.y)) {
                    drawDownDirected(pixelWriter, pixelReader, zBuffer, p2, p0, p1, vt2, vt0, vt1);
                } else if (Math.round(p1.y) == Math.round(p2.y)) {
                    drawUpDirected(pixelWriter, pixelReader, zBuffer, p0, p1, p2, vt0, vt1, vt2);
                } else {
                    Vector3 p3 = new Vector3(calcFormulaX(p0.toVector2(), p2.toVector2(), p1.y), p1.y, calcFormulaZ(p0, p2, p1.y));
                    float scale = p3.subtraction(p0).length() / p2.subtraction(p0).length();
                    Vector2 vt3 = vt2.subtraction(vt0);
                    vt3.multiplyingAVectorByAScalar(scale);
                    vt3 = vt3.sum(vt0);
                    if (p1.x < p3.x) {
                        drawUpDirected(pixelWriter, pixelReader, zBuffer, p0, p1, p3, vt0, vt1, vt3);
                        drawDownDirected(pixelWriter, pixelReader, zBuffer, p2, p1, p3, vt2, vt1, vt3);
                    } else {
                        drawUpDirected(pixelWriter, pixelReader, zBuffer, p0, p3, p1, vt0, vt3, vt1);
                        drawDownDirected(pixelWriter, pixelReader, zBuffer, p2, p3, p1, vt2, vt3, vt1);
                    }
                }
            }
        }
    }

    private static void drawUpDirected(PixelWriter pixelWriter, PixelReader pixelReader, float[][] zBuffer,
                                       Vector3 p0, Vector3 p1, Vector3 p2,
                                       Vector2 vt0, Vector2 vt1, Vector2 vt2) {
        for (int row = (int) Math.ceil(p0.y); row <= Math.floor(p1.y); row++) {
            draw(pixelWriter, pixelReader, zBuffer, p0, p1, p2, vt0, vt1, vt2, row);
        }
    }

    private static void drawDownDirected(PixelWriter pixelWriter, PixelReader pixelReader, float[][] zBuffer,
                                         Vector3 p0, Vector3 p1, Vector3 p2,
                                         Vector2 vt0, Vector2 vt1, Vector2 vt2) {
        for (int row = (int) Math.floor(p0.y); row >= Math.ceil(p1.y); row--) {
            draw(pixelWriter, pixelReader, zBuffer, p0, p1, p2, vt0, vt1, vt2, row);
        }
    }

    private static void draw(PixelWriter pixelWriter, Vector3 p0, Vector3 p1, Vector3 p2, int row) { // without texture (single color)
        float left = calcFormulaX(p0.toVector2(), p1.toVector2(), row);
        float right = calcFormulaX(p0.toVector2(), p2.toVector2(), row);
        for (int col = Math.round(left); col <= Math.round(right); col++) {
            pixelWriter.setColor(col, row, Color.rgb(113,255,73,1.0));
        }
        pixelWriter.setColor(Math.round(left), row, Color.rgb(0,0,0,1.0));
        pixelWriter.setColor(Math.round(right), row, Color.rgb(0,0,0,1.0));
    }

    private static void draw(PixelWriter pixelWriter, PixelReader pixelReader, float[][] zBuffer,
                             Vector3 p0, Vector3 p1, Vector3 p2,
                             Vector2 vt0, Vector2 vt1, Vector2 vt2, int row) {
        float left = calcFormulaX(p0.toVector2(), p1.toVector2(), row);
        float right = calcFormulaX(p0.toVector2(), p2.toVector2(), row);
        for (int col = (int) Math.ceil(left); col <= Math.floor(right); col++) {
            Vector2 o = new Vector2(col, row);
            if (col < 0 || row < 0 || row >= zBuffer.length || col >= zBuffer[0].length) {
                continue;
            }
            float z = calcZOnSurface(p0, p1, p2, o);
            if (zBuffer[row][col] < z) {
                zBuffer[row][col] = z;
                Vector2 dp1 = p1.subtraction(p0).toVector2();
                Vector2 dp2 = p2.subtraction(p0).toVector2();
                Vector2 dO = o.subtraction(p0.toVector2());
                float beta = calcBeta(dO, dp1, dp2);
                float alpha = calcAlpha(dO, dp1, dp2, beta);
                Vector2 pointOnTexture = new Vector2(vt0.x * (1 - alpha - beta) + vt1.x * alpha + vt2.x * beta,
                        vt0.y * (1 - alpha - beta) + vt1.y * alpha + vt2.y * beta);
                Color color = pixelReader.getColor(Math.round(pointOnTexture.x), Math.round(pointOnTexture.y));
                pixelWriter.setColor(col, row, color);
            }
        }
        pixelWriter.setColor(Math.round(left), row, Color.rgb(0,0,0,1.0));
        pixelWriter.setColor(Math.round(right), row, Color.rgb(0,0,0,1.0));
    }

    private static float calcFormulaX(final Vector2 p0,
                                      final Vector2 p1,
                                      final float y) {
        return (y - p0.y) * (p1.x - p0.x) / (p1.y - p0.y) + p0.x;
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

    private static float calcBeta(Vector2 dO, Vector2 dp1, Vector2 dp2) {
        return (dp1.y * dO.x - dO.y * dp1.x) / (dp1.y * dp2.x - dp2.y * dp1.x);
    }

    private static float calcAlpha(Vector2 dO, Vector2 dp1, Vector2 dp2, float beta) {
        return (dO.x - beta * dp2.x) / dp1.x;
    }
}