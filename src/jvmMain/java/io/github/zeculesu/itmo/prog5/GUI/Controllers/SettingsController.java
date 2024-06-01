package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Settings;

public class SettingsController extends BaseController {


    private Settings settings; // Добавьте поле для хранения ссылки на объект Settings

    public void setSettings(Settings settings) {
        this.settings = settings;
    }



    public void changeLocale(ActionEvent actionEvent) {
        String buttonId = ((Button) actionEvent.getSource()).getId();
        Locale locale;
        switch (buttonId) {
            case "ru":
                ResourceManager.getInstance().setLocale(new Locale("ru"));
                break;
            case "es":
                ResourceManager.getInstance().setLocale(new Locale("es","CO"));
                break;
            case "mak":
                ResourceManager.getInstance().setLocale(new Locale("mk"));
                break;
            case "ell":
                ResourceManager.getInstance().setLocale(new Locale("lt"));

                break;
            default:
                locale = Locale.getDefault();
                break;
        }

    }

    public void setLocale(Locale locale) {
        Locale.setDefault(locale);
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }
}

// Определите метод updateTexts здесь, если он