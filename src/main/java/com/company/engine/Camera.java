package com.company.engine;

import com.company.math.matrix.Matrix4;
import com.company.math.vector.Vector3;
import com.company.math.vector.Vector4;

public class Camera {

    public Camera(
            final Vector3 position,
            final Vector3 target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector3 position) {
        this.position = position;
    }

    public void setTarget(final Vector3 target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getTarget() {
        return target;
    }

    public void movePosition(final Direction direction,
                             final float ratio) {
        int columnIndex = -1;
        switch (direction) {
            case FORWARD, BACK -> {
                columnIndex = 2;
            }
            case LEFT, RIGHT -> {
                columnIndex = 0;
            }
            case UP, DOWN-> {
                columnIndex = 1;
            }
        }
        Vector4 moving = getViewMatrix().getColumn(columnIndex);
        /*
        float range = (float) target.subtraction(position).length();
        float dZ = ratio * ratio / (2 * range);
        float dOther = ratio * (float) Math.sqrt(1 - dZ);
        position = position.sum(new Vector3(moving.x * ratio * dOther, moving.y * ratio * dOther, dZ * Math.signum(ratio)));
         */
        Vector3 addition = new Vector3(moving.x * ratio, moving.y * ratio, moving.z * ratio);
        position = position.sum(addition);
        target = target.sum(addition);
    }

    public void moveTarget(final Vector3 translation) {
        target = target.sum(translation);
    }

    public Matrix4 getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    public Matrix4 getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    private Vector3 position;
    private Vector3 target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
}