package com.company.gui;

import javafx.scene.paint.Color;

public class RenderParams {
    public boolean drawTexture;
    public boolean drawMesh;
    public boolean drawShadows;
    public boolean drawOnlyMesh = false;
    public Color fillingColor = Color.rgb(102, 102, 102, 1.0);

    public RenderParams(boolean drawTexture, boolean drawMesh, boolean drawShadows) {
        this.drawTexture = drawTexture;
        this.drawMesh = drawMesh;
        this.drawShadows = drawShadows;
    }
}