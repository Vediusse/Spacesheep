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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

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
    private VBox notificationsContainer;

    @FXML
    public void initialize() {
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
            int id = selectedItem.getId();
            Request request = new Request();
            request.setCommand("remove_lower");
            request.setElem(selectedItem);
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    tableView.setItems(getData());
                    NotificationManager.getInstance().showNotification("Задача выполнена успешно.", "success");
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> NotificationManager.getInstance().showNotification("Ошибка выполнения задачи.", "error"));
                e.printStackTrace();
                return null;
            });
        } else {
            NotificationManager.getInstance().showNotification("Ничего не выбрано.", "error");
        }
    }

    public void deleteFirst(ActionEvent actionEvent) {
        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            MeleeWeapon meleeWeapon = selectedItem.getMeleeWeapon();
            Request request = new Request();
            request.setCommand("remove_all_by_melee_weapon");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            request.setArg(String.valueOf(meleeWeapon));

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    tableView.setItems(getData());
                    NotificationManager.getInstance().showNotification("Задача выполнена успешно.", "success");
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> NotificationManager.getInstance().showNotification("Ошибка выполнения задачи.", "error"));
                e.printStackTrace();
                return null;
            });
        } else {
            NotificationManager.getInstance().showNotification("Ничего не выбрано.", "error");
        }
    }

    public void deleteId(ActionEvent actionEvent) {
        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int id = selectedItem.getId();
            Request request = new Request();
            request.setCommand("remove_by_id");
            request.setArg(Integer.toString(id));
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());

            sendRequestAsync(request).thenAccept(response -> {
                Platform.runLater(() -> {
                    tableView.setItems(getData());
                    NotificationManager.getInstance().showNotification("Задача выполнена успешно.", "success");
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> NotificationManager.getInstance().showNotification("Ошибка выполнения задачи.", "error"));
                e.printStackTrace();
                return null;
            });
        } else {
            NotificationManager.getInstance().showNotification("Ничего не выбрано.", "error");
        }
    }
}
