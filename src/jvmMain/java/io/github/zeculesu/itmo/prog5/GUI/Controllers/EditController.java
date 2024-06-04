package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.ElementFormConsole;
import io.github.zeculesu.itmo.prog5.GUI.ExecutorResource;
import io.github.zeculesu.itmo.prog5.GUI.NotificationManager;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        nameColumn.setOnEditCommit(event -> handleEditCommit(event, "name"));

        xCoordinateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getCoordinates().getX())));
        xCoordinateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        xCoordinateColumn.setOnEditCommit(event -> handleCoordinateEditCommit(event, "x"));

        yCoordinateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getCoordinates().getY())));
        yCoordinateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        yCoordinateColumn.setOnEditCommit(event -> handleCoordinateEditCommit(event, "y"));

        creationDateColumn.setCellValueFactory(cellData -> {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            return new ReadOnlyStringWrapper(df.format(cellData.getValue().getCreationDate()));
        });

        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        healthColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        healthColumn.setOnEditCommit(event -> handleEditCommit(event, "health"));

        chapterNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getName()));
        chapterNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        chapterNameColumn.setOnEditCommit(event -> handleEditCommit(event, "chapterName"));

        chapterLegionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getParentLegion()));
        chapterLegionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        chapterLegionColumn.setOnEditCommit(event -> handleEditCommit(event, "chapterLegion"));

        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(AstartesCategory.values()));
        categoryColumn.setOnEditCommit(event -> handleEditCommit(event, "category"));

        weaponTypeColumn.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
        weaponTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Weapon.values()));
        weaponTypeColumn.setOnEditCommit(event -> handleEditCommit(event, "weaponType"));

        meleeWeaponColumn.setCellValueFactory(new PropertyValueFactory<>("meleeWeapon"));
        meleeWeaponColumn.setCellFactory(ComboBoxTableCell.forTableColumn(MeleeWeapon.values()));
        meleeWeaponColumn.setOnEditCommit(event -> handleEditCommit(event, "meleeWeapon"));

        tableView.getColumns().setAll(nameColumn, xCoordinateColumn, yCoordinateColumn, creationDateColumn, healthColumn, categoryColumn, weaponTypeColumn, meleeWeaponColumn, chapterNameColumn, chapterLegionColumn, ownerColumn);

        getData();

        this.bundle = ResourceManager.getInstance().getResourceBundle();
        updateTexts();
        ResourceManager.getInstance().registerController(this);
    }

    private void handleEditCommit(TableColumn.CellEditEvent<SpaceMarine, ?> event, String field) {
        SpaceMarine marine = event.getRowValue();
        Object newValue = event.getNewValue();
        Object oldValue = event.getOldValue();

        if (!isOwner(marine.getOwner())) {
            NotificationManager.getInstance().showNotification(bundle.getString("notOwnerMessage"), "error");
            tableView.refresh();
            return;
        }

        try {
            validateAndUpdateField(marine, field, newValue);
        } catch (InputFormException | NamingEnumException e) {
            NotificationManager.getInstance().showNotification(e.getMessage(), "error");
            event.getTableView().getItems().set(event.getTablePosition().getRow(), marine);
        }

        CompletableFuture.runAsync(() -> {
            updateField(marine, field, newValue);
            int status = sendUpdate(marine);
            if (status != 200) {
                Platform.runLater(() -> {
                    NotificationManager.getInstance().showNotification(bundle.getString("changeDone"), "success");
                    getData();
                });

            }else {
                NotificationManager.getInstance().showNotification(bundle.getString("errorLabel"), "error");
                tableView.refresh();
            }
        });
    }

    private void validateAndUpdateField(SpaceMarine marine, String field, Object value) throws InputFormException, NamingEnumException {
        switch (field) {
            case "name":
                marine.setName(ElementFormConsole.checkName((String) value));
                break;
            case "health":
                marine.setHealth(ElementFormConsole.checkHealth(String.valueOf(value)));
                break;
            case "chapterName":
                marine.getChapter().setName(ElementFormConsole.checkName((String) value));
                break;
            case "chapterLegion":
                marine.getChapter().setParentLegion(ElementFormConsole.checkName((String) value));
                break;
            case "x":
                marine.getCoordinates().setX(ElementFormConsole.checkCoordinatesX(String.valueOf(value), marine.getCoordinates()).getX());
                break;
            case "y":
                marine.getCoordinates().setY(ElementFormConsole.checkCoordinatesY(String.valueOf(value),marine.getCoordinates()).getY());
                break;
        }
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

    private void getData() {
        CompletableFuture.supplyAsync(() -> {
            ObservableList<SpaceMarine> data = FXCollections.observableArrayList();
            try {
                udpGui.createSocket();
                Request request = new Request();
                request.setCommand("show");
                request.setLogin(this.getLogin());
                request.setPassword(this.getPassword());

                Response response = udpGui.sendRequest(request);
                data.addAll(response.getOutputElement());
                return data;
            } catch (SocketException | UnknownHostException e) {
                throw new RuntimeException("Socket error", e);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("I/O error", e);
            }
        }, ExecutorResource.getExecutor()).thenAcceptAsync(data -> {
            Platform.runLater(() -> {
                tableView.setItems(data);
                tableView.refresh();
            });
        }, Platform::runLater).exceptionally(e -> {
            Platform.runLater(() -> {
                NotificationManager.getInstance().showNotification("Ошибка загрузки данных: " + e.getMessage(), "error");
            });
            return null;
        });
    }

    private void updateField(SpaceMarine marine, String field, Object value) {
        try {
            switch (field) {
                case "name":
                    marine.setName(ElementFormConsole.checkName(String.valueOf(value)));
                    break;
                case "health":
                    marine.setHealth(ElementFormConsole.checkHealth((String.valueOf(value))));
                    break;
                case "chapterName":
                    marine.getChapter().setName(ElementFormConsole.checkName(String.valueOf(value)));
                    break;
                case "chapterLegion":
                    marine.getChapter().setParentLegion(ElementFormConsole.checkName(String.valueOf(value)));
                    break;
                case "x":
                    Coordinates newCoordinatesX = new Coordinates(
                            Long.parseLong(String.valueOf(value)), marine.getCoordinates().getY());
                    marine.setCoordinates(ElementFormConsole.checkCoordinatesX(
                            String.valueOf(value), marine.getCoordinates() ));
                    break;
                case "y":
                    Coordinates newCoordinatesY = new Coordinates(
                            marine.getCoordinates().getX(), Float.parseFloat((String) value));
                    marine.setCoordinates(ElementFormConsole.checkCoordinatesY(
                            String.valueOf(value), marine.getCoordinates()));
                    break;
                case "category":
                    marine.setCategory(AstartesCategory.valueOf(String.valueOf(value)));
                    break;
                case "weaponType":
                    marine.setWeaponType(Weapon.valueOf(String.valueOf(value)));
                    break;
                case "meleeWeapon":
                    marine.setMeleeWeapon(MeleeWeapon.valueOf(String.valueOf(value)));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown field: " + field);
            }
        } catch (InputFormException e) {
            NotificationManager.getInstance().showNotification(e.getMessage(), "error");
        }
    }

    private void handleCoordinateEditCommit(TableColumn.CellEditEvent<SpaceMarine, String> event, String coordinate) {
        SpaceMarine marine = event.getRowValue();
        String newValue = event.getNewValue();
        String oldValue = event.getOldValue();

        if (!isOwner(marine.getOwner())) {
            NotificationManager.getInstance().showNotification(bundle.getString("notOwnerMessage"), "error");
            tableView.refresh();
            return;
        }

        try {
            if (coordinate.equals("x")) {
                long x = Long.parseLong(newValue);
                ElementFormConsole.checkCoordinatesX(newValue,marine.getCoordinates());
                marine.getCoordinates().setX(x);
            } else if (coordinate.equals("y")) {
                float y = Float.parseFloat(newValue);
                ElementFormConsole.checkCoordinatesY(newValue,marine.getCoordinates());
                marine.getCoordinates().setY(y);
            }
            handleEditCommit(event, coordinate);
        } catch (InputFormException | NumberFormatException e) {
            NotificationManager.getInstance().showNotification(e.getMessage(), "error");
            updateField(marine, coordinate, oldValue);
            tableView.refresh();
        }
    }

    private boolean isOwner(String owner) {
        return owner.equals(this.getLogin());
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

