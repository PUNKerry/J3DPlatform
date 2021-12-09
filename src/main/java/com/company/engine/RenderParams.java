package com.company.engine;

public class RenderParams {
    public boolean drawTexture;
    public boolean drawMesh;

    public RenderParams(boolean drawTexture, boolean drawMesh) {
        this.drawTexture = drawTexture;
        this.drawMesh = drawMesh;
    }
}