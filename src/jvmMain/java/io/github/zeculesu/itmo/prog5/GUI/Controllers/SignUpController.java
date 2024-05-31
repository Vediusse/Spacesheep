package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;


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
        Locale.setDefault(new Locale("mk", "MK"));
        setLocale(Locale.getDefault());
        errorLabel.setVisible(false);

        signUpButton.setOnAction(e -> {
            if (userTextField.getText().isEmpty() || pwBox.getText().isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                try {
                    udpGui.createSocket();
                } catch (SocketException ex) {
                    throw new RuntimeException(ex);
                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex);
                }

                Request request = new Request();
                request.setCommand("register");
                request.setArg(userTextField.getText() + " " + pwBox.getText());
                Response response;
                try {
                    byte[] sendData = UDPGui.castToByte(request);
                    udpGui.sendPacket(sendData);
                    response = udpGui.getResponse();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                udpGui.closeClientSocket();

                if (response.getStatus() == 200) {
                    System.out.println("Пользователь зарегистрирован: " + userTextField.getText());
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

            }
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
    }
    public void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        updateTexts();
    }

    private void updateTexts() {
        registerTitle.setText(bundle.getString("registerTitle"));
        userTextField.setPromptText(bundle.getString("login"));
        pwBox.setPromptText(bundle.getString("pwBoxPrompt"));
        errorLabel.setText(bundle.getString("errorLabel"));
        signUpButton.setText(bundle.getString("logUpButton"));
        signInLink.setText(bundle.getString("signInLinkReg"));
    }
}