package com.company.gui;

import javafx.scene.paint.Color;

public class RenderParams {
    public boolean drawTexture;
    public boolean drawMesh;
    public boolean drawShadows;
    public boolean drawOnlyMesh = false;
    public float lightScale = 0.5F;
    public Color fillingColor = Color.rgb(52, 52, 52, 1.0);
    public Color meshColor = Color.BLACK;

    public RenderParams(boolean drawTexture, boolean drawMesh, boolean drawShadows) {
        this.drawTexture = drawTexture;
        this.drawMesh = drawMesh;
        this.drawShadows = drawShadows;
    }
}