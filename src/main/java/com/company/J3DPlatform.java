package com.company;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.File;
import java.util.Objects;

public class J3DPlatform extends Application {

    private Stage stage;

    private void sceneSettings() {
        stage.setX(0);
        stage.setY(0);
        stage.setMinWidth(Screen.getPrimary().getBounds().getWidth());
        stage.setMinHeight(Screen.getPrimary().getBounds().getHeight());
        stage.setMaximized(true);
    }

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane viewport = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml" + File.separator + "gui.fxml")));
        this.stage = stage;

        sceneSettings();

        Scene scene = new Scene(viewport);

        viewport.prefWidthProperty().bind(scene.widthProperty());
        viewport.prefHeightProperty().bind(scene.heightProperty());

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            }
        });
        stage.setTitle("J3DPlatform");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}
