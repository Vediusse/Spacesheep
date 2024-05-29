package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;


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
    public void initialize() {
        errorLabel.setVisible(false);

        signUpButton.setOnAction(e -> {
            if (userTextField.getText().isEmpty() || pwBox.getText().isEmpty()) {
                errorLabel.setVisible(true);
            } else {
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
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                udpGui.closeClientSocket();

                if (response.getStatus() == 200) {
                    System.out.println("Пользователь зарегистрирован: " + userTextField.getText());
                }else {
                    System.out.println("Пользователь знеарегистрирован: " + userTextField.getText());
                }
//                try {
//                    udpGui.createSocket();
//                } catch (SocketException ex) {
//                    throw new RuntimeException(ex);
//                } catch (UnknownHostException ex) {
//                    throw new RuntimeException(ex);
//                }
//
//                Request request = new Request();
//                request.setCommand("register");
//                request.setArg(userTextField.getText() + " " + pwBox.getText());
//                Response response;
//                try {
//                    byte[] sendData = UDPGui.castToByte(request);
//                    udpGui.sendPacket(sendData);
//                    response = udpGui.getResponse();
//                } catch (IOException | ClassNotFoundException ex) {
//                    throw new RuntimeException(ex);
//                }
//                udpGui.closeClientSocket();

                if (response.getStatus() == 200) {
                    System.out.println("Пользователь зарегистрирован: " + userTextField.getText());
                } else {
                    System.out.println(response.getMessage());
                }

            }
        });

        signInLink.setOnAction(e -> {
            // Логика перенаправления на форму входа
            System.out.println("Перейти к форме входа");
        });
    }
}
