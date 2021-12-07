package com.company.engine;
import com.company.math.entity.Point2;
import com.company.math.matrix.Matrix4;
import com.company.math.vector.Vector3;

import javax.vecmath.*;

public class GraphicConveyor {

    public static Matrix4 rotateScaleTranslate() {
        float[] matrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1};
        return new Matrix4(matrix);
    }

    public static Matrix4 lookAt(final Vector3 eye,
                                  final Vector3 target) {
        return lookAt(eye, target, new Vector3(0F, 1.0F, 0F));
    }

    public static Matrix4 lookAt(final Vector3 eye,
                                  final Vector3 target,
                                  final Vector3 up) {
        Vector3 resultX = new Vector3();
        Vector3 resultY = new Vector3();
        Vector3 resultZ = new Vector3();

        resultZ.sub(target, eye);
        resultX.cross(up, resultZ);
        resultY.cross(resultZ, resultX);

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        float[] matrix = new float[]{
                resultX.x, resultY.x, resultZ.x, 0,
                resultX.y, resultY.y, resultZ.y, 0,
                resultX.z, resultY.z, resultZ.z, 0,
                -resultX.dot(eye), -resultY.dot(eye), -resultZ.dot(eye), 1};
        return new Matrix4(matrix);
    }

    public static Matrix4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4 result = new Matrix4();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.matrix[0][0] = tangentMinusOnDegree / aspectRatio;
        result.matrix[1][1] = tangentMinusOnDegree;
        result.matrix[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.matrix[2][3] = 1.0F;
        result.matrix[3][2] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    public static Vector3 multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3 vertex) {
        final float x = (vertex.x * matrix.matrix[0][0]) + (vertex.y * matrix.matrix[1][0]) + (vertex.z * matrix.matrix[2][0]) + matrix.matrix[3][0];
        final float y = (vertex.x * matrix.matrix[0][1]) + (vertex.y * matrix.matrix[1][1]) + (vertex.z * matrix.matrix[2][1]) + matrix.matrix[3][1];
        final float z = (vertex.x * matrix.matrix[0][2]) + (vertex.y * matrix.matrix[1][2]) + (vertex.z * matrix.matrix[2][2]) + matrix.matrix[3][2];
        final float w = (vertex.x * matrix.matrix[0][3]) + (vertex.y * matrix.matrix[1][3]) + (vertex.z * matrix.matrix[2][3]) + matrix.matrix[3][3];
        return new Vector3(x / w, y / w, z / w);
    }

    public static Vector3 vertexToPoint(final Vector3 vertex, final int width, final int height) {
        return new Vector3(vertex.x * width + width / 2.0F, (-vertex.y * height + height / 2.0F), vertex.z);
    }
}
