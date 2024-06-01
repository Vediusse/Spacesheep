package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class EditController extends BaseController {

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
    private Button wallButton;

    @FXML
    private Button catalogButton;

    @FXML
    private Button tableButton;

    @FXML
    private Button workshopButton;

    @FXML
    private Button settingsButton;

    @FXML
    public void initialize() {
        // Initialize the table columns
        tableView.setEditable(true);
        tableView.prefHeightProperty().bind(content.heightProperty().multiply(0.75));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        nameColumn.setOnEditCommit(event -> {
            SpaceMarine marine = event.getRowValue();
            marine.setName(event.getNewValue());
            this.sendUpdate(marine);
        });

        coordinatesColumn.setCellValueFactory(cellData -> {
            SpaceMarine sm = cellData.getValue();
            return new ReadOnlyStringWrapper("x: " + sm.getCoordinates().getX() + ", y: " + sm.getCoordinates().getY());
        });

        creationDateColumn.setCellValueFactory(cellData -> {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");

            return new ReadOnlyStringWrapper(df.format(cellData.getValue().getCreationDate()));
        });

        healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));
        healthColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        healthColumn.setOnEditCommit(event -> {
            SpaceMarine marine = event.getRowValue();
            marine.setHealth(event.getNewValue());
            this.sendUpdate(marine);

        });

        categoryColumn.setCellValueFactory(cellData -> {
            SpaceMarine sm = cellData.getValue();
            this.sendUpdate(sm);
            return new ReadOnlyStringWrapper(sm.getCategory() != null ? sm.getCategory().name() : "");
        });

        weaponTypeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getWeaponType().name()));

        meleeWeaponColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMeleeWeapon().name()));

        chapterNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getName()));
        chapterNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        chapterNameColumn.setOnEditCommit(event -> {
            SpaceMarine marine = event.getRowValue();
            marine.getChapter().setName(event.getNewValue());
            this.sendUpdate(marine);
        });

        chapterLegionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChapter().getParentLegion()));
        chapterLegionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        chapterLegionColumn.setOnEditCommit(event -> {
            SpaceMarine marine = event.getRowValue();
            marine.getChapter().setParentLegion(event.getNewValue());
            this.sendUpdate(marine);
        });

        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        ownerColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        ownerColumn.setOnEditCommit(event -> {
            SpaceMarine marine = event.getRowValue();
            marine.setOwner(event.getNewValue());
            this.sendUpdate(marine);
        });

        // Load data into the table
        ObservableList<SpaceMarine> data = getData();
        tableView.setItems(data);

        wallButton.setOnAction(e -> {
            Main main = new Main();
            Stage currentStage = (Stage) wallButton.getScene().getWindow();
            try {
                main.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });

        catalogButton.setOnAction(e -> {
            MapMarines table = new MapMarines();
            Stage currentStage = (Stage) tableButton.getScene().getWindow();
            try {
                table.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });

        workshopButton.setOnAction(e -> {
            Cruds main = new Cruds();
            Stage currentStage = (Stage) workshopButton.getScene().getWindow();
            try {
                main.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });

    }

    private void sendUpdate(SpaceMarine marine) {
        try {
            udpGui.createSocket();
            Request request = new Request();
            request.setCommand("update");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            request.setArg(String.valueOf(marine.getId()));
            request.setElem(marine);

            Response response = udpGui.sendRequest(request);
            System.out.println(response);

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
}
