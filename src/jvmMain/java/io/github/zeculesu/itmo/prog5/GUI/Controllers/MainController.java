package io.github.zeculesu.itmo.prog5.GUI.Controllers;
import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
public class MainController extends BaseController{

    @FXML
    private HBox root; // Привязываем корневой элемент HBox

    // Sidebar
    @FXML
    private VBox sidebar;

    @FXML
    private Label sidebarTitle;

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
    private Label myGangsLabel;


    @FXML
    private VBox myGangsBox;

    @FXML
    private Button vibeButton;

    @FXML
    private Button notVibeButton;

    @FXML
    private Button testButton;

    @FXML
    private Button loremIpsumButton;

    // Main Content Area
    @FXML
    private VBox mainContent;

    @FXML
    private Label mainTitle;

    @FXML
    private ImageView mainImage;

    @FXML
    private Label mainHeader;

    @FXML
    private Label mainDescription;

    @FXML
    private Button popularButton;

    @FXML
    private Button newButton;

    @FXML
    private Label aboutAuthorLabel;


    @FXML
    private ImageView imageView;

    @FXML
    private VBox content;



    @FXML
    public void initialize() {

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
    }



}
