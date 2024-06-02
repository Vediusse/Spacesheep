package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.SignUp;
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

public class LogInController extends BaseController {

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
    private Label loginTitle;

    private ResourceBundle bundle;

    private static final int TIMEOUT = 3000; // 3 seconds

    private static final ExecutorService executor = Executors.newCachedThreadPool();


    @FXML
    public void initialize() {
        setLocale(Locale.getDefault());
        errorLabel.setVisible(false);

        signUpButton.setOnAction(e -> {
            if (userTextField.getText().isEmpty() || pwBox.getText().isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                handleAuth();
            }
        });

        signInLink.setOnAction(e -> {
            SignUp signUp = new SignUp();
            Stage currentStage = (Stage) signInLink.getScene().getWindow();
            try {
                signUp.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });
    }

    private void handleAuth() {
        String username = userTextField.getText();
        String password = pwBox.getText();

        Request request = new Request();
        request.setCommand("auth");
        request.setArg(username + " " + password);
        request.setLogin(username);

        CompletableFuture.supplyAsync(() -> {
            try {
                udpGui.createSocket();
                byte[] sendData = UDPGui.castToByte(request);
                udpGui.sendPacket(sendData);
                Response response =  udpGui.getResponse();
                udpGui.closeClientSocket();
                return response;
            } catch (SocketException | UnknownHostException e) {
                throw new RuntimeException("Socket error", e);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("I/O error", e);
            }
        }, executor).thenAcceptAsync(response -> {
            Platform.runLater(() -> {
                if (response.getStatus() == 200) {
                    this.setLogin(username);
                    this.setPassword(password);
                    this.goToMain(signUpButton);
                } else {
                    errorLabel.setVisible(true);
                }
            });
        }, Platform::runLater).exceptionally(e -> {
            Platform.runLater(() -> {
                errorLabel.setVisible(true);
                e.printStackTrace();
            });
            return null;
        });
    }


    public void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        updateTexts();
    }

    public void updateTexts() {
        loginTitle.setText(bundle.getString("loginTitle"));
        userTextField.setPromptText(bundle.getString("login"));
        pwBox.setPromptText(bundle.getString("pwBoxPrompt"));
        errorLabel.setText(bundle.getString("errorLabel"));
        signUpButton.setText(bundle.getString("signUpButton"));
        signInLink.setText(bundle.getString("signInLink"));
    }
}
