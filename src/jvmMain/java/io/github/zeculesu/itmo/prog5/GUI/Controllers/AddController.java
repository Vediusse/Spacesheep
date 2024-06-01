package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import javafx.scene.control.Button;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class AddController extends BaseController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label coordinatesXErrorLabel;

    @FXML
    private Label coordinatesYErrorLabel;

    @FXML
    private Label healthErrorLabel;

    @FXML
    private Label categoryErrorLabel;

    @FXML
    private Label weaponTypeErrorLabel;

    @FXML
    private Label meleeWeaponErrorLabel;

    @FXML
    private Label chapterErrorLabel;

    @FXML
    private Label chapterLErrorLabel;

    @FXML
    private Label errorLabel;

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

    @FXML
    private TextField chapterLField;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    private List<TextField> textFields = new ArrayList<>();

    @FXML
    public void initialize() {
        Locale.setDefault(new Locale("ru", "RU"));
        setLocale(Locale.getDefault());
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
    public void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        updateTexts();
    }
    public void updateTexts(){
        titleLabel.setText(bundle.getString("createNewShip"));
        nameField.setPromptText(bundle.getString("namePrompt"));
        nameErrorLabel.setText(bundle.getString("nameError"));
        coordinatesXField.setPromptText(bundle.getString("coordinatesXPrompt"));
        coordinatesXErrorLabel.setText(bundle.getString("coordinatesXError"));
        coordinatesYField.setPromptText(bundle.getString("coordinatesYPrompt"));
        coordinatesYErrorLabel.setText(bundle.getString("coordinatesYError"));
        healthField.setPromptText(bundle.getString("healthPrompt"));
        healthErrorLabel.setText(bundle.getString("healthError"));
        categoryField.setPromptText(bundle.getString("categoryPrompt"));
        categoryErrorLabel.setText(bundle.getString("categoryError"));
        weaponTypeField.setPromptText(bundle.getString("weaponTypePrompt"));
        weaponTypeErrorLabel.setText(bundle.getString("weaponTypeError"));
        meleeWeaponField.setPromptText(bundle.getString("meleeWeaponPrompt"));
        meleeWeaponErrorLabel.setText(bundle.getString("meleeWeaponError"));
        chapterField.setPromptText(bundle.getString("chapterPrompt"));
        chapterErrorLabel.setText(bundle.getString("chapterError"));
        chapterLField.setPromptText(bundle.getString("chapterLPrompt"));
        chapterLErrorLabel.setText(bundle.getString("chapterLError"));
        errorLabel.setText(bundle.getString("errorLabel"));
        createButton.setText(bundle.getString("createButton"));
        cancelButton.setText(bundle.getString("cancelButton"));
    }
}
