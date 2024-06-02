package io.github.zeculesu.itmo.prog5.GUI.Windows;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.AddController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddForm extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1080, 640); // Размер окна
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/add.css")).toExternalForm());

        primaryStage.setTitle("New Ship Creation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

