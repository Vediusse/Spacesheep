package io.github.zeculesu.itmo.prog5.GUI.Windows;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.CrudsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Cruds extends ApplicationAbsract {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/layout.fxml"));
        Parent mainRoot = mainLoader.load();

        // Загружаем FXML файл с координатной плоскостью
        FXMLLoader coordinateLoader = new FXMLLoader(getClass().getResource("/fxml/cruds.fxml"));
        Parent coordinateRoot = coordinateLoader.load();

        // Добавляем координатную плоскость в основную сцену
        ScrollPane scrollPane = (ScrollPane) mainRoot.lookup("#mainContent").lookup(".scroll-pane");
        VBox mainContent = (VBox) scrollPane.getContent().lookup("#content");  // Обратите внимание на # перед content
        mainContent.getChildren().add(coordinateRoot);

        CrudsController controller = (CrudsController) coordinateLoader.getController();

        Scene scene = new Scene(mainRoot, 1080, 640);
        scene.getStylesheets().add(getClass().getResource("/style/cruds.css").toExternalForm()); // Подключаем CSS
        primaryStage.setTitle("Map Marines");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
