package com.company;

import com.company.base.Model;
import com.company.engine.Direction;
import com.company.files.obj.ObjReader;
import com.company.engine.Camera;
import com.company.engine.RenderEngine;
import com.company.files.obj.ObjWriter;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import javax.vecmath.Vector3f;

public class GuiController {

    private static final float DEFAULT_TRANSLATION = 0.5F;

    private float translation = DEFAULT_TRANSLATION;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model model = null;

    private Camera camera = new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
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

            if (model != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void loadFileOnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            model = ObjReader.read(fileContent);
            // todo: обработка ошибок
        } catch (IOException exception) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveFileOnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            ObjWriter.writeToFile(file, model);
            // todo: обработка ошибок
        } catch (IOException exception) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long lastEventTime = 0;
    private Direction lastEventDirection = Direction.FORWARD;
    private boolean cameraMoved = true;

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
    public void handleCameraForward(ActionEvent actionEvent) {
        Direction nowDirection = Direction.FORWARD;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, translation);
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        Direction nowDirection = Direction.BACK;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, -translation);
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        Direction nowDirection = Direction.LEFT;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, -translation);
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        Direction nowDirection = Direction.RIGHT;
        changeTranslation(nowDirection);
        camera.movePosition(nowDirection, translation);
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
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
    public void viewPointUp(ActionEvent actionEvent) {
        Direction nowDirection = Direction.UP;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3f(0F, translation, 0F));
    }

    @FXML
    public void viewPointDown(ActionEvent actionEvent) {
        Direction nowDirection = Direction.DOWN;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3f(0F, -translation, 0F));
    }

    @FXML
    public void viewPointLeft(ActionEvent actionEvent) {
        Direction nowDirection = Direction.LEFT;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3f(translation, 0F , 0F));
    }

    @FXML
    public void viewPointRight(ActionEvent actionEvent) {
        Direction nowDirection = Direction.RIGHT;
        changeTranslation(nowDirection);
        camera.moveTarget(new Vector3f(-translation, 0F , 0F));
    }
}