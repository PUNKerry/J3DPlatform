package com.company;

import com.company.base.Model;
import com.company.base.ModelForDrawing;
import com.company.engine.Direction;
import com.company.engine.RenderParams;
import com.company.exceptions.RenderException;
import com.company.files.obj.ObjReader;
import com.company.engine.Camera;
import com.company.engine.RenderEngine;
import com.company.files.obj.ObjWriter;
import com.company.math.matrix.Matrix3;
import com.company.math.vector.Vector3;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
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
    private Canvas canvas;

    private final List<ModelForDrawing> models = new ArrayList<>();
    private final List<RenderParams> params = new ArrayList<>();

    private Camera camera = new Camera(
            new Vector3(0, 0, 100),
            new Vector3(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));
            final float[][] zBuffer = new float[(int) height][(int) width];

            for (float[] row : zBuffer) {
                Arrays.fill(row, Float.MAX_VALUE);
            }
            for (int modelIndex = 0; modelIndex < models.size(); modelIndex++) {
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, models.get(modelIndex), params.get(modelIndex), (int) width, (int) height, zBuffer);
                } catch (RenderException e) {
                    handle(e);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    GridPane gridPane;

    private final ArrayList<Button> buttons = new ArrayList<>();

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
            String fileContent = Files.readString(fileName);
            ModelForDrawing model = new ModelForDrawing(ObjReader.read(fileContent));
            model.triangulate();
            models.add(model);
            RenderParams param = new RenderParams(false, true);
            params.add(param);

            gridPane.getRowConstraints().add(new RowConstraints(100));

            int n = models.size();

            Button button = new Button("Active");
            button.setFont(new Font(15));
            button.setMinSize(100,70);
            button.setStyle("-fx-background-color: gray;");
            button.setOnMouseClicked(mouseEvent -> activateModel(n, model));
            buttons.add(button);
            gridPane.add(button, 1, n);

            Label label = new Label(" Model " + String.valueOf(n));
            label.setMinSize(80,70);
            label.setFont(new Font(15));
            label.setStyle("-fx-background-color: gray;");
            gridPane.add(label, 0, n);

            Button addTexture = new Button("addTexture");
            addTexture.setFont(new Font(15));
            addTexture.setMinSize(100,70);
            addTexture.setStyle("-fx-background-color: gray;");
            addTexture.setOnMouseClicked(mouseEvent -> {
                addTextureToModel(model, param);
                textureButton(param, n);
            });
            gridPane.add(addTexture, 2, n);

            Button drawMesh = new Button("DrawMesh");
            drawMesh.setFont(new Font(14));
            drawMesh.setMinSize(100,75);
            drawMesh.setStyle("-fx-background-color: gray;");
            drawMesh.setOnMouseClicked(mouseEvent -> {
                param.drawMesh = !param.drawMesh;
                if (param.drawMesh) {
                    drawMesh.setText("DrawMesh");
                }
                else {
                    drawMesh.setText("NotDrawMesh");
                }
            });
            gridPane.add(drawMesh, 3, n);





        } catch (Exception e) {
            handle(e);
        }

    }

    private void textureButton(RenderParams param, int n){
        Button drawTexture = new Button("NotDrawTexture");
        drawTexture.setFont(new Font(14));
        drawTexture.setMinSize(100,75);
        drawTexture.setStyle("-fx-background-color: gray;");
        drawTexture.setOnMouseClicked(mouseEvent -> {
            param.drawTexture = !param.drawTexture;
            if (param.drawTexture) {
                drawTexture.setText("DrawTexture");
            }
            else {
                drawTexture.setText("NotDrawTexture");
            }
        });
        gridPane.add(drawTexture, 4, n);
    }

    private static final float STRETCH = 0.01f;
    private static final float MOVE = 2;
    private static final float A = 0.02f;
    private static final float COS_A = (float) Math.cos(A);
    private static final float SIN_A = (float) Math.sin(A);

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
        float[][] m = {{1, 0, 0},{0, COS_A, SIN_A},{0, -SIN_A, COS_A}};
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotation(new Matrix3(m));
        });
    }

    @FXML
    private void rotateY() {
        float[][] m = {{COS_A, 0, SIN_A},{0, 1, 0},{-SIN_A, 0, COS_A}};
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotation(new Matrix3(m));
        });
    }

    @FXML
    private void rotateZ() {
        float[][] m = {{COS_A, SIN_A, 0},{-SIN_A, COS_A, 0},{0, 0, 1}};
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotation(new Matrix3(m));
        });
    }

    @FXML
    private void rotateInTheOppositeDirectionX() {
        float[][] m = {{1, 0, 0},{0, COS_A, -SIN_A},{0, SIN_A, COS_A}};
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotation(new Matrix3(m));
        });
    }

    @FXML
    private void rotateInTheOppositeDirectionY() {
        float[][] m = {{COS_A, 0, -SIN_A},{0, 1, 0},{SIN_A, 0, COS_A}};
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotation(new Matrix3(m));
        });
    }

    @FXML
    private void rotateInTheOppositeDirectionZ() {
        float[][] m = {{COS_A, -SIN_A, 0},{SIN_A, COS_A, 0},{0, 0, 1}};
        models.forEach(modelForDrawing -> {
            if(modelForDrawing.isChangingNow()) modelForDrawing.rotation(new Matrix3(m));
        });
    }

    private void addTextureToModel(ModelForDrawing model, RenderParams params) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.png)", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.jpeg)", "*.jpeg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.jpg)", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.tga)", "*.tga"));
        //todo: optimize it
        fileChooser.setTitle("Load Texture");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            params.drawTexture = true;
            model.setTexture(new Image(file.toString()));
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

    public void activateModel(int n, ModelForDrawing model){

            model.setChangingNow(!model.isChangingNow());
            if(model.isChangingNow()) buttons.get(n - 1).setText("Active");
            else buttons.get(n - 1).setText("Not active");

    }
}