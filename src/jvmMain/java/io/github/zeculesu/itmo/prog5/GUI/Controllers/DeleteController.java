package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.NotificationManager;
import io.github.zeculesu.itmo.prog5.models.MeleeWeapon;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;

public class DeleteController extends BaseController {

    @FXML
    private VBox content;
    @FXML
    private TableColumn<SpaceMarine, String> xCoordinateColumn;

    @FXML
    private TableColumn<SpaceMarine, String> yCoordinateColumn;

    @FXML
    private TableView<SpaceMarine> tableView;

    @FXML
    private TableColumn<SpaceMarine, String> nameColumn;

    @FXML
    private TableColumn<SpaceMarine, String> coordinatesColumn;

    @FXML
    private TableColumn<SpaceMarine, String> creationDateColumn;

    @FXML
    private TableColumn<SpaceMarine, Integer> healthColumn;

    @FXML
    private TableColumn<SpaceMarine, String> categoryColumn;

    @FXML
    private TableColumn<SpaceMarine, String> weaponTypeColumn;

    @FXML
    private TableColumn<SpaceMarine, String> meleeWeaponColumn;

    @FXML
    private TableColumn<SpaceMarine, String> chapterNameColumn;

    @FXML
    private TableColumn<SpaceMarine, String> chapterLegionColumn;

    @FXML
    private TableColumn<SpaceMarine, String> ownerColumn;
    @FXML
    private Button deleteSelectedButton;

    @FXML
    private Button deleteFirstButton;

    @FXML
    private Button deleteByWeaponButton;

    @FXML
    public void initialize() {
        this.bundle = ResourceManager.getInstance().getResourceBundle();
        updateTexts();
        ResourceManager.getInstance().registerController(this);
        // Initialize the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        coordinatesColumn.setCellValueFactory(cellData -> {
            SpaceMarine sm = cellData.getValue();
            return new ReadOnlyStringWrapper("x: " + sm.getCoordinates().getX() + ", y: " + sm.getCoordinates().getY());
        });
        creationDateColumn.setCellValueFactory(cellData -> {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            return new ReadOnlyStringWrapper(df.format(cellData.getValue().getCreationDate()));
        });
        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        categoryColumn.setCellValueFactory(cellData -> {
            SpaceMarine sm = cellData.getValue();
            return new ReadOnlyStringWrapper(sm.getCategory() != null ? sm.getCategory().name() : "");
        });
        weaponTypeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getWeaponType().name()));
        meleeWeaponColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMeleeWeapon().name()));
        chapterNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getName()));
        chapterLegionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getParentLegion()));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));

        // Load data into the table
        ObservableList<SpaceMarine> data = getData();
        tableView.setItems(data);
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

    public void deleteLast(ActionEvent actionEvent) {
        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (!selectedItem.getOwner().equals(this.getLogin())){
                NotificationManager.getInstance().showNotification(bundle.getString("notOwnerMessage"), "error");
                return;
            }
            int id = selectedItem.getId();
            Request request = new Request();
            request.setCommand("remove_lower");
            request.setElem(selectedItem);
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    tableView.setItems(getData());
                    NotificationManager.getInstance().showNotification(bundle.getString("changeDone"), "success");
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> NotificationManager.getInstance().showNotification(bundle.getString("errorLabel"), "error"));
                e.printStackTrace();
                return null;
            });
        } else {
            NotificationManager.getInstance().showNotification(bundle.getString("nothingSelected"), "error");
        }
    }

    public void deleteFirst(ActionEvent actionEvent) {
        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (!selectedItem.getOwner().equals(this.getLogin())){
                NotificationManager.getInstance().showNotification(bundle.getString("notOwnerMessage"), "error");
                return;
            }
            MeleeWeapon meleeWeapon = selectedItem.getMeleeWeapon();
            Request request = new Request();
            request.setCommand("remove_all_by_melee_weapon");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            request.setArg(String.valueOf(meleeWeapon));

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    tableView.setItems(getData());
                    NotificationManager.getInstance().showNotification(bundle.getString("changeDone"), "success");
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> NotificationManager.getInstance().showNotification(bundle.getString("errorLabel"), "error"));
                e.printStackTrace();
                return null;
            });
        } else {
            NotificationManager.getInstance().showNotification(bundle.getString("nothingSelected"), "error");
        }
    }

    public void deleteId(ActionEvent actionEvent) {
        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (!selectedItem.getOwner().equals(this.getLogin())){
                NotificationManager.getInstance().showNotification(bundle.getString("notOwnerMessage"), "error");
                return;
            }
            int id = selectedItem.getId();
            Request request = new Request();
            request.setCommand("remove_by_id");
            request.setArg(Integer.toString(id));
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    tableView.setItems(getData());
                    NotificationManager.getInstance().showNotification(bundle.getString("changeDone"), "success");
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> NotificationManager.getInstance().showNotification(bundle.getString("errorLabel"), "error"));
                e.printStackTrace();
                return null;
            });
        } else {
            NotificationManager.getInstance().showNotification(bundle.getString("nothingSelected"), "error");
        }
    }

    @Override
    public void updateTexts() {
        bundle = ResourceManager.getInstance().getResourceBundle();
        deleteSelectedButton.setText(bundle.getString("deleteSelectedButton"));
        deleteFirstButton.setText(bundle.getString("deleteFirstButton"));
        deleteByWeaponButton.setText(bundle.getString("deleteByWeaponButton"));
        nameColumn.setText(bundle.getString("nameColumn"));
        coordinatesColumn.setText(bundle.getString("coordinatesColumn"));
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
