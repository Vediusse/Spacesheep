package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import io.github.zeculesu.itmo.prog5.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AddController extends BaseController {
    @FXML
    public Label errorLabel;
    @FXML
    public TextField chapterLField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField healthField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField weaponTypeField;
    @FXML
    private TextField meleeWeaponField;
    @FXML
    private TextField chapterField;

    private List<TextField> textFields = new ArrayList<>();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleSubmit() {
        try {
            String name = nameField.getText();
            Long x = Long.parseLong(coordinatesXField.getText());
            int y = Integer.parseInt(coordinatesYField.getText());
            int health = Integer.parseInt(healthField.getText());
            AstartesCategory category = AstartesCategory.valueOf(categoryField.getText().toUpperCase());
            Weapon weaponType = Weapon.valueOf(weaponTypeField.getText().toUpperCase());
            MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(meleeWeaponField.getText().toUpperCase());
            String chapterName = chapterField.getText();

            if (name.isEmpty() || chapterName.isEmpty()) {
                showError("Поля имени и главы не должны быть пустыми.");
                return;
            }

            Coordinates coordinates = new Coordinates(x, y);
            Chapter chapter = new Chapter(chapterName, chapterLField.getText());  // Assuming Chapter has a constructor taking a name and parentLegion
            SpaceMarine spaceMarine = new SpaceMarine(0, name, coordinates, health, category, weaponType, meleeWeapon, chapter);

            // Здесь отправка объекта spaceMarine на сервер
            Request request = new Request();
            request.setCommand("add");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            request.setElem(spaceMarine);

            // Ваш метод для отправки запроса
            udpGui.createSocket();
            udpGui.sendRequest(request);

        } catch (IllegalArgumentException | SocketException | UnknownHostException e) {
            showError("Ошибка");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showError(String error) {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    @FXML
    public void back(ActionEvent event) {
        // Логика для обработки действия "Отменить", например, закрытие окна или очистка формы
        Table Table = new Table();
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        try {
            Table.start(new Stage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();
    }
}
