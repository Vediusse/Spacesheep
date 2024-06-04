package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.ExecutorResource;
import io.github.zeculesu.itmo.prog5.GUI.NotificationManager;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Settings;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class TableController extends BaseController {

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
        getData();

        // Set localized text

        this.bundle = ResourceManager.getInstance().getResourceBundle();
        this.updateTexts();
        ResourceManager.getInstance().localeProperty().addListener((observable, oldValue, newValue) -> {
            updateTexts();
        });
    }

    public void updateTexts(){
        this.bundle = ResourceManager.getInstance().getResourceBundle();
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
}
