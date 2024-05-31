package io.github.zeculesu.itmo.prog5.GUI.Windows;


import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Table extends ApplicationAbsract {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/table.xml"));
        Parent root = loader.load();

        primaryStage.setTitle("Стена");


        Scene scene = new Scene(root, 1080, 640); // Размер окна
        scene.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm()); // Подключаем CSS
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.requestFocus();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
