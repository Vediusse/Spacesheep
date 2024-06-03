package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.models.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;

public class EditController extends BaseController {

    @FXML
    private VBox content;

    @FXML
    private TableView<SpaceMarine> tableView;

    @FXML
    private TableColumn<SpaceMarine, String> nameColumn;

    @FXML
    private TableColumn<SpaceMarine, String> xCoordinateColumn;

    @FXML
    private TableColumn<SpaceMarine, String> yCoordinateColumn;

    @FXML
    private TableColumn<SpaceMarine, String> creationDateColumn;

    @FXML
    private TableColumn<SpaceMarine, Integer> healthColumn;

    @FXML
    private TableColumn<SpaceMarine, AstartesCategory> categoryColumn;

    @FXML
    private TableColumn<SpaceMarine, Weapon> weaponTypeColumn;

    @FXML
    private TableColumn<SpaceMarine, MeleeWeapon> meleeWeaponColumn;

    @FXML
    private TableColumn<SpaceMarine, String> chapterNameColumn;

    @FXML
    private TableColumn<SpaceMarine, String> chapterLegionColumn;

    @FXML
    private TableColumn<SpaceMarine, String> ownerColumn;


    @FXML
    public void initialize() {
        tableView.setEditable(true);

        // Name column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        nameColumn.setOnEditCommit(event -> handleEditCommit(event, "name"));

        // Coordinates columns
        xCoordinateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getCoordinates().getX())));
        xCoordinateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        xCoordinateColumn.setOnEditCommit(event -> handleCoordinateEditCommit(event, "x"));

        yCoordinateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getCoordinates().getY())));
        yCoordinateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        yCoordinateColumn.setOnEditCommit(event -> handleCoordinateEditCommit(event, "y"));

        // Creation date column
        creationDateColumn.setCellValueFactory(cellData -> {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            return new ReadOnlyStringWrapper(df.format(cellData.getValue().getCreationDate()));
        });

        // Health column
        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        healthColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        healthColumn.setOnEditCommit(event -> handleEditCommit(event, "health"));

        // Chapter name column
        chapterNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getName()));
        chapterNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        chapterNameColumn.setOnEditCommit(event -> handleEditCommit(event, "chapterName"));

        // Chapter legion column
        chapterLegionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getParentLegion()));
        chapterLegionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        chapterLegionColumn.setOnEditCommit(event -> handleEditCommit(event, "chapterLegion"));

        // Owner column (disabled for editing)
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));

        // Category column with dropdown
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(AstartesCategory.values()));

        // Weapon type column with dropdown
        weaponTypeColumn.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
        weaponTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Weapon.values()));

        // Melee weapon column with dropdown
        meleeWeaponColumn.setCellValueFactory(new PropertyValueFactory<>("meleeWeapon"));
        meleeWeaponColumn.setCellFactory(ComboBoxTableCell.forTableColumn(MeleeWeapon.values()));

        // Adding columns to tableView in the correct order
        tableView.getColumns().setAll(nameColumn, xCoordinateColumn, yCoordinateColumn, creationDateColumn, healthColumn, categoryColumn, weaponTypeColumn, meleeWeaponColumn, chapterNameColumn, chapterLegionColumn, ownerColumn);

        // Load data into the table
        ObservableList<SpaceMarine> data = getData();
        tableView.setItems(data);

        this.bundle = ResourceManager.getInstance().getResourceBundle();
        updateTexts();
        ResourceManager.getInstance().registerController(this);
    }

    private void handleEditCommit(TableColumn.CellEditEvent<SpaceMarine, ?> event, String field) {
        SpaceMarine marine = event.getRowValue();
        Object newValue = event.getNewValue();
        Object oldValue = event.getOldValue();

        updateField(marine, field, newValue);

        CompletableFuture.runAsync(() -> {
            int status = sendUpdate(marine);
            if (status != 200) {
                Platform.runLater(() -> {
                    updateField(marine, field, oldValue);
                    tableView.setItems(getData());
                });
            }
        });
    }

    private void updateField(SpaceMarine marine, String field, Object value) {
        switch (field) {
            case "name":
                marine.setName((String) value);
                break;
            case "health":
                marine.setHealth((Integer) value);
                break;
            case "chapterName":
                marine.getChapter().setName((String) value);
                break;
            case "chapterLegion":
                marine.getChapter().setParentLegion((String) value);
                break;
            case "owner":
                marine.setOwner((String) value);
                break;
            case "x":
                marine.getCoordinates().setX(Long.parseLong((String) value));
                break;
            case "y":
                marine.getCoordinates().setY(Float.parseFloat((String) value));
                break;
        }
    }

    private void switchScene(Object newScene, Button button) {
        Stage currentStage = (Stage) button.getScene().getWindow();
        try {
            if (newScene instanceof Main) {
                ((Main) newScene).start(new Stage());
            } else if (newScene instanceof MapMarines) {
                ((MapMarines) newScene).start(new Stage());
            } else if (newScene instanceof Cruds) {
                ((Cruds) newScene).start(new Stage());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();
    }

    private int sendUpdate(SpaceMarine marine) {
        try {
            udpGui.createSocket();
            Request request = new Request();
            request.setCommand("update");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            request.setArg(String.valueOf(marine.getId()));
            request.setElem(marine);

            Response response = udpGui.sendRequest(request);
            return response.getStatus();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<SpaceMarine> getData() {
        ObservableList<SpaceMarine> data = FXCollections.observableArrayList();
        try {
            udpGui.createSocket();
            Request request = new Request();
            request.setCommand("show");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());

            Response response = udpGui.sendRequest(request);
            data.addAll(response.getOutputElement());
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private void handleCoordinateEditCommit(TableColumn.CellEditEvent<SpaceMarine, String> event, String coordinate) {
        SpaceMarine marine = event.getRowValue();
        try {
            if (coordinate.equals("x")) {
                Long newValue = Long.parseLong(event.getNewValue());
                marine.getCoordinates().setX(newValue);
                handleEditCommit(event, "x");
            } else if (coordinate.equals("y")) {
                float newValue = Float.parseFloat(event.getNewValue());
                marine.getCoordinates().setY(newValue);
                handleEditCommit(event, "y");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user if the input is invalid
        }
    }

    public void updateTexts(){
        this.bundle = ResourceManager.getInstance().getResourceBundle();
        nameColumn.setText(bundle.getString("nameColumn"));
        xCoordinateColumn.setText(bundle.getString("coordinatesXPrompt"));
        yCoordinateColumn.setText(bundle.getString("coordinatesYPrompt"));
        creationDateColumn.setText(bundle.getString("creationDateColumn"));
        healthColumn.setText(bundle.getString("healthColumn"));
        categoryColumn.setText(bundle.getString("categoryColumn"));
        weaponTypeColumn.setText(bundle.getString("weaponTypeColumn"));
        meleeWeaponColumn.setText(bundle.getString("meleeWeaponColumn"));
        chapterNameColumn.setText(bundle.getString("chapterNameColumn"));
        chapterLegionColumn.setText(bundle.getString("chapterLegionColumn"));
        ownerColumn.setText(bundle.getString("ownerColumn"));
    }
}
