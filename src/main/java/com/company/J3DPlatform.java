package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class J3DPlatform extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane viewport = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml" + File.separator + "gui.fxml")));

        Scene scene = new Scene(viewport);
        stage.setMinWidth(1600);
        stage.setMinHeight(900);
        viewport.prefWidthProperty().bind(scene.widthProperty());
        viewport.prefHeightProperty().bind(scene.heightProperty());

        stage.setTitle("J3DPlatform");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
