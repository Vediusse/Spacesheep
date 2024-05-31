package io.github.zeculesu.itmo.prog5.GUI.Windows;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogIn extends ApplicationAbsract {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        BaseController controller = loader.getController();
        controller.setUdpClient(this.udpGui);

        primaryStage.setTitle("Log in");
        Scene scene = new Scene(root, 1080, 640); // Размер окна
        scene.getStylesheets().add(getClass().getResource("/style/signup.css").toExternalForm()); // Подключаем CSS

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}