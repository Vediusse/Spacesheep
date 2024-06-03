package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.zeculesu.itmo.prog5.GUI.NotificationManager;
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Settings;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SettingsController extends BaseController {


    @FXML
    public Label owner;
    @FXML
    public Button exit;
    public String typeCollection;
    public String countCollection;
    @FXML
    public Label info;

    public void initialize() {
        Request request = new Request();
        request.setCommand("info");
        request.setLogin(this.getLogin());
        request.setPassword(this.getPassword());

        sendRequestAsync(request).thenAccept(response -> {
            Platform.runLater(() -> {
                try {
                    System.out.print(response.getOutput());
                    List<String> output = response.getOutput();
                    this.typeCollection = output.get(0);
                    this.countCollection = output.get(1);
                    updateTexts();
                } catch (Exception ignored) {
                }
            });
        }).exceptionally(e -> null);

        updateTexts();
        ResourceManager.getInstance().registerController(this);
    }

    public void exit(ActionEvent actionEvent) {
        LogIn logIn = new LogIn();
        Stage currentStage = (Stage) owner.getScene().getWindow();
        try {
            logIn.start(new Stage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();
    }


    public void updateTexts() {
        bundle = ResourceManager.getInstance().getResourceBundle();
        owner.setText(bundle.getString("owner") + " " + getLogin());
        exit.setText(bundle.getString("exit"));
        if (typeCollection != null) info.setText(bundle.getString("info") + "\n");
        if (countCollection != null) {
            String[] out = countCollection.split(" ");
            String count = out[out.length - 1];
            info.setText(info.getText() + bundle.getString("count") + " " + count);
        }
    }

    private CompletableFuture<Response> sendRequestAsync(Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return udpGui.sendRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}