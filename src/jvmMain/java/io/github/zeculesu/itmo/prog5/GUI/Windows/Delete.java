package io.github.zeculesu.itmo.prog5.GUI.Windows;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Delete extends ApplicationAbsract {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/delete.fxml"));
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
