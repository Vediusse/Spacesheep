package io.github.zeculesu.itmo.prog5.GUI.Windows;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Delete extends ApplicationAbsract {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/layout.fxml"));
        Parent mainRoot = mainLoader.load();

        // Загружаем FXML файл с координатной плоскостью
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/delete.fxml"));
        Parent root = loader.load();

        ScrollPane scrollPane = (ScrollPane) mainRoot.lookup("#mainContent").lookup(".scroll-pane");
        VBox mainContent = (VBox) scrollPane.getContent().lookup("#content");  // Обратите внимание на # перед content

        mainContent.getChildren().add(root);

        primaryStage.setTitle("Стена");

        Scene scene = new Scene(mainRoot, 1080, 640); // Размер окна
        scene.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm()); // Подключаем CSS
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.requestFocus();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
