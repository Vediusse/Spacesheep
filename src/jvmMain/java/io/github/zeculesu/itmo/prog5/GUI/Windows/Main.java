package io.github.zeculesu.itmo.prog5.GUI.Windows;


import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.Controllers.CrudsController;
import io.github.zeculesu.itmo.prog5.GUI.Controllers.MainController;
import io.github.zeculesu.itmo.prog5.GUI.Controllers.ResourceManager;
import io.github.zeculesu.itmo.prog5.GUI.ExecutorResource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends ApplicationAbsract {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/index.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Стена");



        Scene scene = new Scene(root, 1080, 640); // Размер окна
        scene.getStylesheets().add(getClass().getResource("/style/index.css").toExternalForm()); // Подключаем CSS
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.requestFocus();
    }

    @Override
    public void stop() throws Exception {
        // Перед тем как приложение закроется, вы можете вызвать метод shutdownExecutor
        ExecutorResource.shutdownExecutor();

        // Если у вас есть другие ресурсы для очистки, сделайте это здесь
    }


    public static void main(String[] args) {
        launch(args);
    }
}
