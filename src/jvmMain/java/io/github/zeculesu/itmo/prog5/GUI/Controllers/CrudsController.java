package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.AddForm;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Cruds;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Delete;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CrudsController extends BaseController{


    @FXML
    private Label mainHeader;

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
    }

}
