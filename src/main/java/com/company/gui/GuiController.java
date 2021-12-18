package com.company.gui;

import com.company.base.Model;
import com.company.base.ModelForDrawing;
import com.company.engine.Light;
import com.company.exceptions.RenderException;
import com.company.files.obj.ObjReader;
import com.company.engine.Camera;
import com.company.engine.RenderEngine;
import com.company.files.obj.ObjWriter;
import com.company.math.vector.Vector3;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Path;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GuiController {

    private static final float DEFAULT_TRANSLATION = 0.5F;

    private float translation = DEFAULT_TRANSLATION;

    @FXML
    AnchorPane anchorPane;

    @FXML
    MenuBar menuBar;

    @FXML
    Menu menu;

    private boolean isDarkTheme = false;

    @FXML
    public void changeTheme(){
        if(isDarkTheme){
            anchorPane.setStyle("-fx-background-color: white");
            menuBar.setStyle("-fx-background-color: white");
            params.forEach(p -> p.meshColor = Color.rgb(40,40,40));
        }else {
            anchorPane.setStyle("-fx-background-color: rgb(40,40,40)");
            menuBar.setStyle("-fx-background-color: rgb(40,40,40)");
            params.forEach(p -> p.meshColor = Color.GRAY);
        }
        isDarkTheme = !isDarkTheme;
    }

    @FXML
    private Canvas canvas;

    private final List<ModelForDrawing> models = new ArrayList<>();
    private final List<RenderParams> params = new ArrayList<>();

    private final Camera camera = new Camera(
            new Vector3(0, 0, 100),
            new Vector3(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private final Light light = new Light(new Vector3(0, 100, 0));

    private Timeline timeline;

    @FXML
    Label fpsViewer = new Label("");

    @FXML
    TextField cameraTargetX;

    @FXML
    TextField cameraTargetY;

    @FXML
    TextField cameraTargetZ;

    @FXML
    private void targetApply(){
        camera.setTarget(new Vector3(Float.valueOf(cameraTargetX.getText()), Float.valueOf(cameraTargetY.getText()), Float.valueOf(cameraTargetZ.getText())));
    }

    @FXML
    TextField cameraPositionX;

    @FXML
    TextField cameraPositionY;

    @FXML
    TextField cameraPositionZ;

    @FXML
    private void positionApply(){
        camera.setPosition(new Vector3(Float.valueOf(cameraPositionX.getText()), Float.valueOf(cameraPositionY.getText()), Float.valueOf(cameraPositionZ.getText())));
    }

    @FXML
    TextField lightX;

    @FXML
    TextField lightY;

    @FXML
    TextField lightZ;

    @FXML
    private void setLight(){
        light.setPosition(new Vector3(Float.valueOf(lightX.getText()), Float.valueOf(lightY.getText()), Float.valueOf(lightZ.getText())));
    }

    @FXML
    TextField lightPower;

    @FXML
    private void addLightScale(){
        light.setLightScale(light.getLightScale() + 0.1f);
        lightPower.setText(String.format("%1f", light.getLightScale()));

    }

    @FXML
    private void deLightScale(){
        light.setLightScale(light.getLightScale() - 0.1f);
        lightPower.setText(String.format("%1f", light.getLightScale()));
    }

    @FXML
    private void initialize() {
        anchorPane.setStyle("-fx-background-color: white");
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        anchorPane.getStylesheets().add("style.css");
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        fpsViewer.setAlignment(Pos.CENTER);

        RenderEngine render = new RenderEngine();

        KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {

            long lastTime = System.nanoTime();

            int width = (int) canvas.getWidth();
            int height = (int) canvas.getHeight();

            render.setWidth(width);
            render.setHeight(height);

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);

            camera.setAspectRatio((float) width / height);

            render.setCamera(camera);

            final float[][] zBuffer = new float[(int) height][(int) width];

            for (float[] row : zBuffer) {
                Arrays.fill(row, Float.MAX_VALUE);
            }

            render.setzBuffer(zBuffer);

            WritableImage wi = new WritableImage(width, height);

            render.setPw(wi.getPixelWriter());

            render.setLight(light);
           // updateParams();

            for (int modelIndex = 0; modelIndex < models.size(); modelIndex++) {
                try {
                    render.render(models.get(modelIndex), params.get(modelIndex));
                } catch (RenderException e) {
                    handle(e);
                }
            }

            canvas.getGraphicsContext2D().drawImage(wi, 0, 0);

            fpsViewer.setText(String.valueOf(1000000000 / (System.nanoTime() - lastTime)));
        });

        cameraPositionX.setText(String.valueOf(camera.getPosition().x));
        cameraPositionY.setText(String.valueOf(camera.getPosition().y));
        cameraPositionZ.setText(String.valueOf(camera.getPosition().z));

        cameraTargetX.setText(String.valueOf(camera.getTarget().x));
        cameraTargetY.setText(String.valueOf(camera.getTarget().y));
        cameraTargetZ.setText(String.valueOf(camera.getTarget().z));

        lightX.setText(String.valueOf(light.getPosition().x));
        lightY.setText(String.valueOf(light.getPosition().y));
        lightZ.setText(String.valueOf(light.getPosition().z));

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    GridPane gridPaneModels;

    @FXML
    public void showMenu(){
        gridPaneModels.setVisible(!gridPaneModels.isVisible());
        aboutModel.setVisible(!aboutModel.isVisible());
    }

    @FXML
    GridPane aboutModel;

    @FXML
    Label shift;

    @FXML
    Label stretch;

    @FXML
    Label rotation;

    private ModelForDrawing chosenModel;

    private double round(float value){
        double scale = Math.pow(10, 2);
        return Math.ceil(value * scale) / scale;
    }

    private void setChosenModel(ModelForDrawing model) {
        chosenModel = model;

        shift.setText("Model shifts: x:" + model.getShift().x + " y:" + model.getShift().y + " z:" + model.getShift().z);
        stretch.setText("Model stretch: x:" + round(model.getXStretching()) + " y:" + round(model.getYStretching()) + " z:" + round(model.getZStretching()));
        rotation.setText("Model rotation: x:" + round(model.getxRotation()) + " y:" + round(model.getyRotation()) + " z:" + round(model.getzRotation()));
    }

    @FXML
    private void loadFileOnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            Model base = ObjReader.readFromFile(fileName);
            base.triangulate();
            base.reCalcNormals();
            ModelForDrawing model = new ModelForDrawing(base);
            models.add(model);
            RenderParams param = new RenderParams(false, true, false);
            params.add(param);

            gridPaneModels.getRowConstraints().add(new RowConstraints(100));

            int n = models.size();

            CheckBox active = new CheckBox();
            active.setOnMouseClicked(mouseEvent ->  model.setChangingNow(!model.isChangingNow()));
            gridPaneModels.add(active, 1, n);

            CheckBox light = new CheckBox();
            light.setOnMouseClicked(mouseEvent ->  param.drawShadows = !param.drawShadows);
            gridPaneModels.add(light, 4, n);

            Button delete = new Button("Delete");
            delete.setOnMouseClicked(mouseEvent -> {
                models.remove(n - 1);
                params.remove(n - 1);
//                for (int i = 0; i < 7; i++) {
//                    gridPaneModels.getChildren().remove(i, n);
//                }
            });
            gridPaneModels.add(delete, 3, n);

            Button chooseModel = new Button(model.getActualModel().getName());
            chooseModel.setOnMouseClicked(mouseEvent -> {
                setChosenModel(model);
            });
            gridPaneModels.add(chooseModel, 0, n);

            Button addTexture = new Button("Add texture");
            addTexture.setOnMouseClicked(mouseEvent -> {
                addTextureToModel(model, param);
                if (model.getTexture() != null) {
                    textureButton(param, n);
                }
            });
            gridPaneModels.add(addTexture, 5, n);

            Button drawMesh = new Button("Draw Mesh");
            drawMesh.setOnMouseClicked(mouseEvent -> {
                if (param.drawOnlyMesh) {
                    param.drawOnlyMesh = false;
                    param.drawMesh = true;
                    drawMesh.setText("Draw Mesh");
                }
                else {
                    if(param.drawMesh) {
                        drawMesh.setText("Not draw mesh");
                        param.drawOnlyMesh = false;
                        param.drawMesh = false;
                    }
                    else {
                        drawMesh.setText("Draw only mesh");
                        param.drawOnlyMesh = true;
                    }
                }
            });
            gridPaneModels.add(drawMesh, 2, n);
        } catch (Exception e) {
            handle(e);
        }
    }

    private void textureButton(RenderParams param, int n) {
        CheckBox drawTexture = new CheckBox();
        drawTexture.setSelected(true);
        drawTexture.setOnMouseClicked(mouseEvent -> {
            param.drawTexture = !param.drawTexture;
        });
        gridPaneModels.add(drawTexture, 6, n);
    }

    @FXML
    private void removeShift() {
        Vector3 shif = chosenModel.getShift();
        chosenModel.move(new Vector3(-shif.x, -shif.y, -shif.z));

    }

    @FXML
    private void removeStretch() {
        chosenModel.XStretching(1);
        chosenModel.YStretching(1);
        chosenModel.ZStretching(1);
    }

    @FXML
    private void removeRotation() {
        chosenModel.rotationX(-chosenModel.getxRotation());
        chosenModel.rotationY(-chosenModel.getyRotation());
        chosenModel.rotationZ(-chosenModel.getzRotation());
    }

    private static final float STRETCH = 0.01f;
    private static final float MOVE = 2;
    private static final float A = 0.02f;

    @FXML
    private void stretchX() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.XStretching(modelForDrawing.getXStretching() + STRETCH);
        });
    }

    @FXML
    private void stretchY() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.YStretching(modelForDrawing.getYStretching() + STRETCH);
        });
    }

    @FXML
    private void stretchZ() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.ZStretching(modelForDrawing.getZStretching() + STRETCH);
        });
    }

    @FXML
    private void pullItOffX() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.XStretching(modelForDrawing.getXStretching() - STRETCH);
        });
    }

    @FXML
    private void pullItOffY() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.YStretching(modelForDrawing.getYStretching() - STRETCH);
        });
    }

    @FXML
    private void pullItOffZ() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.ZStretching(modelForDrawing.getZStretching() - STRETCH);
        });
    }

    @FXML
    private void moveXInAPositiveDirection() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.move(new Vector3(MOVE, 0, 0));
        });
    }

    @FXML
    private void moveXInANegativeDirection() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.move(new Vector3(-MOVE, 0, 0));
        });
    }

    @FXML
    private void moveYInAPositiveDirection() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.move(new Vector3(0, MOVE, 0));
        });
    }

    @FXML
    private void moveYInANegativeDirection() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.move(new Vector3(0, -MOVE, 0));
        });
    }

    @FXML
    private void moveZInAPositiveDirection() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.move(new Vector3(0, 0, MOVE));
        });
    }

    @FXML
    private void moveZInANegativeDirection() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.move(new Vector3(0, 0, -MOVE));
        });
    }

    @FXML
    private void rotateX() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotationX(A);
        });
    }

    @FXML
    private void rotateY() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotationY(A);
        });
    }

    @FXML
    private void rotateZ() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotationZ(A);
        });
    }

    @FXML
    private void rotateInTheOppositeDirectionX() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotationX(-A);
        });
    }

    @FXML
    private void rotateInTheOppositeDirectionY() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotationY(-A);
        });
    }

    @FXML
    private void rotateInTheOppositeDirectionZ() {
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotationZ(-A);
        });
    }

    private void addTextureToModel(ModelForDrawing model, RenderParams params) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.png),(*.jpeg),(*.jpg),(*.tga)", "*.png", "*.jpeg", "*.jpg", "*.tga"));
        fileChooser.setTitle("Load Texture");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Image texture = new Image(file.toString());
        if (texture == null) {
            return;
        }
        try {
            params.drawTexture = true;
            model.setTexture(texture);
            model.triangulate();
        } catch (Exception e) {
            handle(e);
        }
    }

    @FXML
    private void saveFileOnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");

        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            Model m = new Model();
            models.forEach(modelForDrawing -> {
                m.addModel(modelForDrawing.getActualModel());
            });
            ObjWriter.writeToFile(file, m);
        } catch (Exception e) {
            handle(e);
        }
    }

    public void handle(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Exception");
        alert.setHeaderText(null);
        alert.setContentText(exception.getMessage());

        alert.showAndWait();
    }

    private long lastEventTime = 0;
    private Direction lastEventDirection = Direction.FORWARD;

    private void changeTranslation(Direction direction) {
        long now = new Date().getTime();
        if (direction == lastEventDirection && now - lastEventTime < 75) {
            increaseTranslation();
        } else {
            translation = DEFAULT_TRANSLATION;
        }
        lastEventDirection = direction;
        lastEventTime = now;
    }

    private void increaseTranslation() {
        if (translation < DEFAULT_TRANSLATION * 20) {
            translation += 2 * DEFAULT_TRANSLATION;
        }
    }

    @FXML
    public void handleCameraForward() {
        Direction nowDirection = Direction.FORWARD;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, translation);
    }

    @FXML
    public void handleCameraBackward() {
        Direction nowDirection = Direction.BACK;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, -translation);
    }

    @FXML
    public void handleCameraLeft() {
        Direction nowDirection = Direction.LEFT;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, -translation);
    }

    @FXML
    public void handleCameraRight() {
        Direction nowDirection = Direction.RIGHT;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, translation);
    }

    @FXML
    public void handleCameraUp() {
        Direction nowDirection = Direction.UP;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, translation);
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        Direction nowDirection = Direction.DOWN;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, -translation);
    }

    @FXML
    public void viewClear() {
        camera.setTarget(new Vector3(0, 0, 0));
    }

    @FXML
    public void viewPointUp() {
        Direction nowDirection = Direction.UP;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3(0F, translation, 0F));
    }

    @FXML
    public void viewPointDown() {
        Direction nowDirection = Direction.DOWN;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3(0F, -translation, 0F));
    }

    @FXML
    public void viewPointLeft() {
        Direction nowDirection = Direction.LEFT;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3(translation, 0F , 0F));
    }

    @FXML
    public void viewPointRight() {
        Direction nowDirection = Direction.RIGHT;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3(-translation, 0F , 0F));
    }

    @FXML
    public void triangulateModel(){
        models.forEach(modelForDrawing -> {
            try {
                modelForDrawing.triangulate();
            } catch (Exception e) {
                handle(e);
            }
        });
    }
}