package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.AddForm;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Delete;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Edit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CrudsController extends BaseController{
    @FXML
    private Button creationButton;

    @FXML
    private Text createShipText;


    @FXML
    private Text deleteShipText;

    @FXML
    private Button findButton;

    @FXML
    private Text findShipText;

    @FXML
    private Label mainHeader;
    public void initialize() {
        this.bundle = ResourceManager.getInstance().getResourceBundle();
        this.updateTexts();
        ResourceManager.getInstance().localeProperty().addListener((observable, oldValue, newValue) -> {
            updateTexts();
        });
    }

    public void handleCreate(ActionEvent actionEvent) {
        AddForm main = new AddForm();
        Stage currentStage = (Stage) mainHeader.getScene().getWindow();
        try {
            main.start(new Stage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();
    }

    public void handleDelete(ActionEvent actionEvent) {
        Delete main = new Delete();
        Stage currentStage = (Stage) mainHeader.getScene().getWindow();
        try {
            main.start(new Stage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();
    }

    public void handleFind(ActionEvent actionEvent) {
        Edit main = new Edit();
        Stage currentStage = (Stage) mainHeader.getScene().getWindow();
        try {
            main.start(new Stage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();

    }

    public void updateTexts() {
        mainHeader.setText(bundle.getString("mainHeader"));
        createShipText.setText(bundle.getString("createShipText"));
        deleteShipText.setText(bundle.getString("deleteShipText"));
        findShipText.setText(bundle.getString("findShipText"));
    }
}
