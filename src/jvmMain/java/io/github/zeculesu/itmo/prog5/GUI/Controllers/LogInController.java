package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn;
import io.github.zeculesu.itmo.prog5.GUI.Windows.SignUp;
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

    @FXML
    public void initialize() {
        // Установка локали по умолчанию (русский)
        Locale.setDefault(new Locale("ru", "RU"));
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
                request.setCommand("auth");
                request.setArg(userTextField.getText() + " " + pwBox.getText());
                request.setLogin(userTextField.getText());

                byte[] sendData;
                Response response;
                try {
                    sendData = UDPGui.castToByte(request);
                    udpGui.sendPacket(sendData);
                    response = udpGui.getResponse();
                }

                catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                udpGui.closeClientSocket();

                if (response.getStatus() == 200) {
                    this.setLogin(userTextField.getText());
                    this.setPassword(pwBox.getText());
                    this.goToMain(this.signUpButton);
                }else {
                    errorLabel.setVisible(true);
                }
            }
        });

        signInLink.setOnAction(e -> {
            SignUp logIn = new SignUp();
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
        loginTitle.setText(bundle.getString("loginTitle"));
        userTextField.setPromptText(bundle.getString("login"));
        pwBox.setPromptText(bundle.getString("pwBoxPrompt"));
        errorLabel.setText(bundle.getString("errorLabel"));
        signUpButton.setText(bundle.getString("signUpButton"));
        signInLink.setText(bundle.getString("signInLink"));
    }
}
