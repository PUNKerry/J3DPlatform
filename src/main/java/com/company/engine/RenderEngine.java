package com.company.engine;

import com.company.base.Model;
import com.company.base.Polygon;
import com.company.math.entity.Point2;
import com.company.math.matrix.Matrix4;
import com.company.math.vector.Vector3;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
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

        final int nPolygons = model.getCountOfPolygons();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final Polygon polygon = model.getPolygon(polygonInd);

            final int nVerticesInPolygon = polygon.countOfVertices();

            List<Point2> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = model.getVertex(polygon.getVertexIndex(vertexInPolygonInd));
                Point2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);
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

                resultPoints.sort((p1, p2) -> {
                    int res = Float.compare(p1.y, p2.y);
                    if (res != 0) {
                        return res;
                    } else {
                        return Float.compare(p1.x, p2.x);
                    }
                });

                Point2f p0 = resultPoints.get(0);
                Point2f p1 = resultPoints.get(1);
                Point2f p2 = resultPoints.get(2);

                if (Math.round(p0.y) == Math.round(p1.y)) {
                    drawDownDirected(pixelWriter, p2, p0, p1);
                } else if (Math.round(p1.y) == Math.round(p2.y)) {
                    drawUpDirected(pixelWriter, p0, p1, p2);
                } else {
                    Point2f p3 = new Point2f(calcFormula(p0, p2, p1.y), p1.y);
                    if (p1.x < p3.x) {
                        drawUpDirected(pixelWriter, p0, p1, p3);
                        drawDownDirected(pixelWriter, p2, p1, p3);
                    } else {
                        drawUpDirected(pixelWriter, p0, p3, p1);
                        drawDownDirected(pixelWriter, p2, p3, p1);
                    }
                }
            }
        }
    }

    private static void drawUpDirected(PixelWriter pixelWriter, Point2f p0, Point2f p1, Point2f p2) {
        for (int row = Math.round(p0.y); row <= Math.round(p1.y); row++) {
            draw(pixelWriter, p0, p1, p2, row);
        }
    }

    private static void drawDownDirected(PixelWriter pixelWriter, Point2f p0, Point2f p1, Point2f p2) {
        for (int row = Math.round(p0.y); row >= Math.round(p1.y); row--) {
            draw(pixelWriter, p0, p1, p2, row);
        }
    }

    private static void draw(PixelWriter pixelWriter, Point2f p0, Point2f p1, Point2f p2, int row) {
        float left = calcFormula(p0, p1, row);
        float right = calcFormula(p0, p2, row);
        for (int col = Math.round(left); col <= Math.round(right); col++) {
            pixelWriter.setColor(col, row, Color.rgb(113,255,73,1.0));
        }
        pixelWriter.setColor(Math.round(left), row, Color.rgb(0,0,0,1.0));
        pixelWriter.setColor(Math.round(right), row, Color.rgb(0,0,0,1.0));
    }

    private static float calcFormula(final Point2f p0,
                                    final Point2f p1,
                                    final float y) {
        return (y - p0.y) * (p1.x - p0.x) / (p1.y - p0.y) + p0.x;
    }


}