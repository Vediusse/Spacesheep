
package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.client.UDPClient;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class BaseController {
    protected UDPGui udpGui = new UDPGui("127.0.0.1",45001);

    public void setUdpClient(UDPGui udpClient) {
        this.udpGui = udpClient;
    }

    void goToMain(Button button) {
        Main main = new Main();
        Stage currentStage = (Stage) button.getScene().getWindow();
        try {
            main.start(new Stage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        currentStage.close();
    }
}

