package com.company.base;

import com.company.math.matrix.Matrix3;
import com.company.math.vector.Vector3;

public class ModelChange {
    private final Model initialModel;
    private Model changingModel = null;
    private float xStretching = 1;
    private float yStretching = 1;
    private float zStretching = 1;
    private Vector3 shift = new Vector3(0, 0, 0);
    private boolean isChangingNow = true;

    public ModelChange(Model initialModel) {
        this.initialModel = initialModel;
    }

    public void move(Vector3 v){
        shift = shift.sum(v);
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.Shift(v);
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

    public void rotation(Matrix3 rotationMatrix){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.rotation(rotationMatrix, shift);
    }

    public Model getChangingModel() {
        return changingModel;
    }

    public Model getInitialModel() {
        return initialModel;
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
}
