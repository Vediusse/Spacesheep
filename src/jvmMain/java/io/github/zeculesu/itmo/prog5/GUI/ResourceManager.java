package io.github.zeculesu.itmo.prog5.GUI;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager {
    private static final ResourceManager instance = new ResourceManager();
    private final ObjectProperty<Locale> localeProperty;
    private ResourceBundle resourceBundle;

    private ResourceManager() {
        localeProperty = new SimpleObjectProperty<>(Locale.getDefault());
        resourceBundle = ResourceBundle.getBundle("messages", localeProperty.get());

        localeProperty.addListener((observable, oldLocale, newLocale) -> {
            resourceBundle = ResourceBundle.getBundle("messages", newLocale);
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
}
