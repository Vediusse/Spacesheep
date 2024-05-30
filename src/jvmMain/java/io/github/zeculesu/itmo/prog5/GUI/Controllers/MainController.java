package io.github.zeculesu.itmo.prog5.GUI.Controllers;
import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
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
    public void initialize() {
        // Bind the width of the ImageView to the width of its parent container
    }
}
