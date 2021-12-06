package com.company.base;

import com.company.math.vector.Vector3;

public class ModelChange {
    private final Model initialModel;
    private Model changingModel = null;
    private double xStretching = 1;
    private double yStretching = 1;
    private double zStretching = 1;
    private final Vector3 shift = new Vector3(0, 0, 0);

    public ModelChange(Model initialModel) {
        this.initialModel = initialModel;
    }

    public void move(Vector3 v){
        shift.sum(v);
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.Shift(v);
    }

    public void XStretching(double n){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.XStretching(n/xStretching);
        xStretching = n;
    }

    public void YStretching(double n){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.YStretching(n/yStretching);
        yStretching = n;
    }

    public void ZStretching(double n){
        if(changingModel == null) changingModel = initialModel.clone();
        changingModel.ZStretching(n/zStretching);
        zStretching = n;
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

    public double getXStretching() {
        return xStretching;
    }

    public double getYStretching() {
        return yStretching;
    }

    public double getZStretching() {
        return zStretching;
    }
}
