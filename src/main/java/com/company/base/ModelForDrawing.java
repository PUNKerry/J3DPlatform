package com.company.base;

import com.company.math.matrix.Matrix3;
import com.company.math.vector.Vector3;
import javafx.scene.image.Image;

public class ModelForDrawing {
    private final Model initialModel;
    private Model changingModel = null;
    private float xStretching = 1;
    private float yStretching = 1;
    private float zStretching = 1;
    private float xRotation = 0;
    private float yRotation = 0;
    private float zRotation = 0;
    private Vector3 shift = new Vector3(0, 0, 0);
    private boolean isChangingNow = true;
    private Image texture = null;

    public ModelForDrawing(Model initialModel) {
        this.initialModel = initialModel;
    }

    public void move(Vector3 v){
        shift = shift.sum(v);
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.shift(v);
    }

    public void XStretching(float n){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.XStretching(n/xStretching);
        xStretching = n;
    }

    public void YStretching(float n){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.YStretching(n/yStretching);
        yStretching = n;
    }

    public void ZStretching(float n){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.ZStretching(n/zStretching);
        zStretching = n;
    }

    public void rotationX(float A){
        xRotation += A;
        float COS_A = (float) Math.cos(A);
        float SIN_A = (float) Math.sin(A);
        float[][] m = {{1, 0, 0},{0, COS_A, SIN_A},{0, -SIN_A, COS_A}};
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.rotation(new Matrix3(m), shift);
    }

    public void rotationY(float A){
        yRotation += A;
        float COS_A = (float) Math.cos(A);
        float SIN_A = (float) Math.sin(A);
        float[][] m = {{COS_A, 0, SIN_A},{0, 1, 0},{-SIN_A, 0, COS_A}};
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.rotation(new Matrix3(m), shift);
    }

    public void rotationZ(float A){
        zRotation += A;
        float COS_A = (float) Math.cos(A);
        float SIN_A = (float) Math.sin(A);
        float[][] m = {{COS_A, SIN_A, 0},{-SIN_A, COS_A, 0},{0, 0, 1}};
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.rotation(new Matrix3(m), shift);
    }

    public Model getChangingModel() {
        return changingModel;
    }

    public Model getInitialModel() {
        return initialModel;
    }

    public Model getActualModel(){
        if(changingModel != null) return changingModel;
        return initialModel;
    }

    public float getxRotation() {
        return xRotation;
    }

    public float getxStretching() {
        return xStretching;
    }

    public float getyRotation() {
        return yRotation;
    }

    public float getyStretching() {
        return yStretching;
    }

    public float getzRotation() {
        return zRotation;
    }

    public float getzStretching() {
        return zStretching;
    }

    public Vector3 getShift() {
        return shift;
    }

    public float getXStretching() {
        return xStretching;
    }

    public float getYStretching() {
        return yStretching;
    }

    public float getZStretching() {
        return zStretching;
    }

    public boolean isChangingNow() {
        return isChangingNow;
    }

    public void setChangingNow(boolean changingNow) {
        isChangingNow = changingNow;
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public void triangulate() throws Exception {
        initialModel.triangulate();
        if(changingModel != null) changingModel.triangulate();
    }

    public void reCalcNormals(){
        initialModel.reCalcNormals();
        if(changingModel != null) changingModel.reCalcNormals();
    }
}
