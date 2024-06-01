package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        tableView.prefHeightProperty().bind(content.heightProperty().multiply(0.75));

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



        tableButton.setOnAction(e -> {
            Table Table = new Table();
            Stage currentStage = (Stage) tableButton.getScene().getWindow();
            try {
                Table.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });
        catalogButton.setOnAction(e -> {
            MapMarines Table = new MapMarines();
            Stage currentStage = (Stage) tableButton.getScene().getWindow();
            try {
                Table.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });
        wallButton.setOnAction(e ->{
            Main main = new Main();
            Stage currentStage = (Stage) wallButton.getScene().getWindow();
            try {
                main.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
        });
        workshopButton.setOnAction(e->{
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


    public void deleteLast(ActionEvent actionEvent) {
        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                int id = selectedItem.getId();
                udpGui.createSocket();
                Request request = new Request();
                request.setCommand("remove_lower");
                request.setElem(selectedItem);
                request.setLogin(this.getLogin());
                request.setPassword(this.getPassword());
                Response response = udpGui.sendRequest(request);
                tableView.setItems(getData());
            } catch (SocketException | UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Ничего не выбрано.");
        }

    }

    public void deleteFirst(ActionEvent actionEvent) {
        try {
            udpGui.createSocket();
            Request request = new Request();
            request.setCommand("remove_head");
            request.setLogin(this.getLogin());
            request.setPassword(this.getPassword());
            Response response = udpGui.sendRequest(request);
            System.out.println(response.getError());
            tableView.setItems(getData());
        }catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteId(ActionEvent actionEvent) {

        SpaceMarine selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                int id = selectedItem.getId();
                udpGui.createSocket();
                Request request = new Request();
                request.setCommand("remove_by_id");
                request.setArg(Integer.toString(selectedItem.getId()));
                request.setLogin(this.getLogin());
                request.setPassword(this.getPassword());
                Response response = udpGui.sendRequest(request);
                tableView.setItems(getData());
            } catch (SocketException | UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Ничего не выбрано.");
        }

    }
}
