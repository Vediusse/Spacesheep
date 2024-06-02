package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.NotificationManager;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import io.github.zeculesu.itmo.prog5.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

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
    private ComboBox<String> categoryField;

    @FXML
    private ComboBox<String> weaponTypeField;

    @FXML
    private ComboBox<String> meleeWeaponField;

    @FXML
    private TextField chapterField;

    @FXML
    private TextField chapterLField;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);

        this.bundle = ResourceManager.getInstance().getResourceBundle();
        this.updateTexts();
        ResourceManager.getInstance().localeProperty().addListener((observable, oldValue, newValue) -> {
            updateTexts();
        });

        // Populate the ComboBoxes
        categoryField.setItems(FXCollections.observableArrayList("SCOUT", "SUPPRESSOR", "LIBRARIAN", "HELIX"));
        weaponTypeField.setItems(FXCollections.observableArrayList("BOLTGUN", "HEAVY_BOLTGUN", "BOLT_RIFLE", "FLAMER", "MULTI_MELTA"));
        meleeWeaponField.setItems(FXCollections.observableArrayList("CHAIN_SWORD", "POWER_SWORD", "CHAIN_AXE", "MANREAPER", "POWER_BLADE"));
    }

    @FXML
    public void handleSubmit() {
        try {
            String name = nameField.getText();
            Long x = Long.parseLong(coordinatesXField.getText());
            int y = Integer.parseInt(coordinatesYField.getText());
            int health = Integer.parseInt(healthField.getText());
            AstartesCategory category = AstartesCategory.valueOf(categoryField.getValue().toUpperCase());
            Weapon weaponType = Weapon.valueOf(weaponTypeField.getValue().toUpperCase());
            MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(meleeWeaponField.getValue().toUpperCase());
            String chapterName = chapterField.getText();

            if (name.isEmpty() || chapterName.isEmpty()) {
                showError("Поля имени и главы не должны быть пустыми.");
                return;
            }

            Coordinates coordinates = new Coordinates(x, y);
            Chapter chapter = new Chapter(chapterName, chapterLField.getText());
            SpaceMarine spaceMarine = new SpaceMarine(0, name, coordinates, health, category, weaponType, meleeWeapon, chapter);

            Request request = new Request();
            request.setCommand("add");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            request.setElem(spaceMarine);

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    Table table = new Table();
                    Stage currentStage = (Stage) nameField.getScene().getWindow();
                    try {
                        table.start(new Stage());
                        NotificationManager.getInstance().showNotification("Задача выполнена успешно.", "success");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    currentStage.close();
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> showError("Ошибка выполнения задачи."));
                e.printStackTrace();
                return null;
            });

        } catch (IllegalArgumentException e) {
            showError("Неправильный формат данных.");
        }
    }

    private CompletableFuture<Response> sendRequestAsync(Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                udpGui.createSocket();
                return udpGui.sendRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void showError(String error) {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    @FXML
    public void back(ActionEvent event) {
        Cruds cruds = new Cruds();
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        try {
            cruds.start(new Stage());
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
