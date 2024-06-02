package io.github.zeculesu.itmo.prog5.GUI;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class NotificationManager {

    private static NotificationManager instance;
    private VBox notificationsContainer;

    private NotificationManager() {
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void setNotificationsContainer(VBox notificationsContainer) {
        this.notificationsContainer = notificationsContainer;
    }

    public void showNotification(String message, String type) {
        if (notificationsContainer == null) {
            throw new IllegalStateException("Notifications container is not set");
        }

        Platform.runLater(() -> {
            Label notificationLabel = new Label(message);
            notificationLabel.getStyleClass().add("notification");
            if ("success".equals(type)) {
                notificationLabel.getStyleClass().add("notification-success");
            } else if ("error".equals(type)) {
                notificationLabel.getStyleClass().add("notification-error");
            }

            notificationsContainer.getChildren().add(notificationLabel);

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> notificationsContainer.getChildren().remove(notificationLabel));
            pause.play();
        });
    }
}

