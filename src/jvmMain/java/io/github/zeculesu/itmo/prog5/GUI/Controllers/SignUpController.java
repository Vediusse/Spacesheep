package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.ExecutorResource;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SignUpController extends BaseController {

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField pwBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button signUpButton;

    @FXML
    private Hyperlink signInLink;
    @FXML
    private Label registerTitle;

    private ResourceBundle bundle;


    @FXML
    public void initialize() {
        errorLabel.setVisible(false);

        signUpButton.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            Request request = new Request();
            request.setCommand("register");
            request.setArg(username + " " + password);

            CompletableFuture.supplyAsync(() -> {
                try {
                    udpGui.createSocket();
                    byte[] sendData = UDPGui.castToByte(request);
                    udpGui.sendPacket(sendData);
                    Response response = udpGui.getResponse();
                    udpGui.closeClientSocket();
                    return response;
                } catch (SocketException | UnknownHostException e) {
                    throw new RuntimeException("Socket error", e);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("I/O error", e);
                }
            }, ExecutorResource.getExecutor()).thenAcceptAsync(response -> {
                Platform.runLater(() -> {
                    if (response.getStatus() == 0) {
                        errorLabel.setVisible(true);
                    } else {
                        LogIn logIn = new LogIn();
                        Stage currentStage = (Stage) signInLink.getScene().getWindow();
                        try {
                            logIn.start(new Stage());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        currentStage.close();
                    }
                });
            }, Platform::runLater).exceptionally(e -> {
                Platform.runLater(() -> {
                    e.printStackTrace();
                });
                return null;
            });


        });

        signInLink.setOnAction(e -> {
            LogIn logIn = new LogIn();
            Stage currentStage = (Stage) signInLink.getScene().getWindow();
            try {
                logIn.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });
        this.bundle = ResourceManager.getInstance().getResourceBundle();
        this.updateTexts();
        ResourceManager.getInstance().localeProperty().addListener((observable, oldValue, newValue) -> {
            updateTexts();
        });
    }
    public void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        updateTexts();
    }

    public void updateTexts() {
        registerTitle.setText(bundle.getString("registerTitle"));
        userTextField.setPromptText(bundle.getString("login"));
        pwBox.setPromptText(bundle.getString("pwBoxPrompt"));
        errorLabel.setText(bundle.getString("errorLabel"));
        signUpButton.setText(bundle.getString("logUpButton"));
        signInLink.setText(bundle.getString("signInLinkReg"));
    }
}