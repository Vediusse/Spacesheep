package io.github.zeculesu.itmo.prog5.GUI.Controllers;

public class LanguageManager {
    private static LanguageManager instance;
    private String activeLanguageButtonId;

    private LanguageManager() {
    }

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String getActiveLanguageButtonId() {
        return activeLanguageButtonId;
    }

    public void setActiveLanguageButtonId(String activeLanguageButtonId) {
        this.activeLanguageButtonId = activeLanguageButtonId;
    }
}
