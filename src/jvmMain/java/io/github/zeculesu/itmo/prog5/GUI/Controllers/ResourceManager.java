package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager {
    private static final ResourceManager instance = new ResourceManager();
    private final ObjectProperty<Locale> localeProperty;
    private ResourceBundle resourceBundle;
    private final List<BaseController> localizableControllers;

    private ResourceManager() {
        localeProperty = new SimpleObjectProperty<>(Locale.getDefault());
        resourceBundle = ResourceBundle.getBundle("messages", localeProperty.get());
        localizableControllers = new ArrayList<>();

        localeProperty.addListener((observable, oldLocale, newLocale) -> {
            resourceBundle = ResourceBundle.getBundle("messages", newLocale);
            notifyAllControllers();
        });
    }

    public static ResourceManager getInstance() {
        return instance;
    }

    public Locale getLocale() {
        return localeProperty.get();
    }

    public void setLocale(Locale locale) {
        localeProperty.set(locale);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public ObjectProperty<Locale> localeProperty() {
        return localeProperty;
    }

    public void registerController(BaseController controller) {
        localizableControllers.add(controller);
    }

    public void unregisterController(BaseController controller) {
        localizableControllers.remove(controller);
    }

    private void notifyAllControllers() {
        for (BaseController controller : localizableControllers) {
            controller.updateTexts();
        }
    }
}
