package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Controllers.BaseController;
import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.*;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController extends BaseController {

    public VBox sidebarButtons;
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
    private Label authorName1, authorDescription1, authorName2, authorDescription2, footerText, aboutHeader, footerHeader;
    @FXML
    private ImageView imageView;

    @FXML
    private TextField userTextField;
    @FXML
    private VBox content;

    private ResourceBundle bundle;


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
        if(mainHeader!=null){
            this.mainHeader.setText("Приветствую, " + this.getLogin());
        }
        settingsButton.setOnAction(e ->{
            Settings main = new Settings();
            Stage currentStage = (Stage) workshopButton.getScene().getWindow();
            try {
                main.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            currentStage.close();
                }
        );
        this.bundle = ResourceManager.getInstance().getResourceBundle();
        this.updateTexts();
        ResourceManager.getInstance().localeProperty().addListener((observable, oldValue, newValue) -> {
            updateTexts();
        });

    }

    public void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        updateTexts();
    }

    public void updateTexts() {
        sidebarTitle.setText(bundle.getString("sidebarTitle"));
        wallButton.setText(bundle.getString("wallButton"));
        catalogButton.setText(bundle.getString("catalogButton"));
        tableButton.setText(bundle.getString("tableButton"));
        workshopButton.setText(bundle.getString("workshopButton"));
        settingsButton.setText(bundle.getString("settingsButton"));
        userTextField.setPromptText(bundle.getString("userTextFieldPrompt"));

        // Обновление динамически добавленного контента
        if (mainHeader != null) mainHeader.setText(bundle.getString("mainHeader"));
        if (aboutHeader != null) aboutHeader.setText(bundle.getString("aboutHeader"));
        if (authorName1 != null) authorName1.setText(bundle.getString("authorName1"));
        if (authorDescription1 != null) authorDescription1.setText(bundle.getString("authorDescription1"));
        if (authorName2 != null) authorName2.setText(bundle.getString("authorName2"));
        if (authorDescription2 != null) authorDescription2.setText(bundle.getString("authorDescription2"));
        if (footerHeader != null) footerHeader.setText(bundle.getString("footerHeader"));
        if (footerText != null) footerText.setText(bundle.getString("footerText"));
    }
}