package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import io.github.zeculesu.itmo.prog5.models.Coordinates;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CoordinatePlaneController extends BaseController {

    @FXML
    private Canvas coordinatePlane;

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

    private final double AXIS_WIDTH = 2.0; // Ширина осей координат
    private final Color AXIS_COLOR = Color.FLORALWHITE; // Цвет осей координат

    private final double POINT_RADIUS = 3.0; // Радиус точек

    private final double SCALE = 10.0; // Масштаб

    private double lastX, lastY; // Переменные для хранения предыдущих координат мыши
    private double dragStartX, dragStartY; // Начальные координаты мыши при начале перетаскивания
    private double dragOffsetX = 0, dragOffsetY = 0; // Смещение мыши при перетаскивании

    private Map<String, ArrayList<Coordinates>> loginCoord; // Список точек для отображения

    public void initialize() {
        // Asynchronous request to get coordinates
        Request request = new Request();
        request.setCommand("getlogincoords");
        request.setLogin(this.getLogin());
        request.setPassword(this.getPassword());

        sendRequestAsync(request).thenAccept(response -> {
            Platform.runLater(() -> {
                this.loginCoord = response.getLoginCoord();
                redrawCoordinatePlane();
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> {
                this.loginCoord = new HashMap<>();
                redrawCoordinatePlane(); // Перерисовываем координатную плоскость и точки при инициализации
            });
            e.printStackTrace();
            return null;
        });

        // Добавляем обработчики событий мыши для перемещения карты
        coordinatePlane.setOnMousePressed(this::onMousePressed);
        coordinatePlane.setOnMouseDragged(this::onMouseDragged);

        // Добавляем обработчики событий для точек
        coordinatePlane.setOnMouseClicked(this::onPointClicked);
    }

    private CompletableFuture<Response> sendRequestAsync(Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                udpGui.createSocket();
                return udpGui.sendRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onMousePressed(MouseEvent event) {
        dragStartX = event.getX();
        dragStartY = event.getY();
        lastX = dragStartX;
        lastY = dragStartY;
    }

    private void onMouseDragged(MouseEvent event) {
        dragOffsetX += event.getX() - lastX;
        dragOffsetY += event.getY() - lastY;
        lastX = event.getX();
        lastY = event.getY();

        redrawCoordinatePlane(); // Перерисовываем координатную плоскость и точки при перемещении мыши
    }

    private void redrawCoordinatePlane() {
        GraphicsContext gc = coordinatePlane.getGraphicsContext2D();
        double width = coordinatePlane.getWidth();
        double height = coordinatePlane.getHeight();

        // Очищаем холст перед перерисовкой
        gc.clearRect(0, 0, width, height);

        // Перерисовываем координатную плоскость с учетом смещения
        drawCoordinatePlane(gc, width, height);

        // Перерисовываем точки с учетом смещения
        drawPoints(gc, width, height);
    }

    private void drawCoordinatePlane(GraphicsContext gc, double width, double height) {
        // Рисование осей координат с учетом смещения и увеличения их длины
        gc.setStroke(AXIS_COLOR);
        gc.setLineWidth(AXIS_WIDTH);
        gc.strokeLine(-1000, height / 2 + dragOffsetY, 1000, height / 2 + dragOffsetY); // Ось X
        gc.strokeLine(width / 2 + dragOffsetX, -1000, width / 2 + dragOffsetX, 1000); // Ось Y
    }

    private void drawPoints(GraphicsContext gc, double width, double height) {
        if (loginCoord != null) { // Add null check here
            // Proceed with drawing points
            for (String login : loginCoord.keySet()) {
                Color color = Color.color(Math.random(), Math.random(), Math.random());
                for (Coordinates point : loginCoord.get(login)) {
                    double scaledX = width / 2 + point.getX() * SCALE + dragOffsetX;
                    double scaledY = height / 2 - point.getY() * SCALE + dragOffsetY; // Инвертируем y и учитываем масштаб
                    gc.setFill(color);
                    gc.fillOval(scaledX - POINT_RADIUS, scaledY - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
                }
            }
        }
    }

    private void onPointClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        // Увеличиваем радиус проверки для более широкой зоны нажатия
        double clickRadius = POINT_RADIUS * 2.5;

        // Проверяем, попадает ли нажатие мыши в радиус какой-либо точки
        for (String login : loginCoord.keySet()) {
            for (Coordinates point : loginCoord.get(login)) {
                double scaledX = coordinatePlane.getWidth() / 2 + point.getX() * SCALE + dragOffsetX;
                double scaledY = coordinatePlane.getHeight() / 2 - point.getY() * SCALE + dragOffsetY;
                if (Math.pow(mouseX - scaledX, 2) + Math.pow(mouseY - scaledY, 2) <= Math.pow(clickRadius, 2)) {
                    // Если попали в точку, выводим информацию о ней
                    System.out.println("Clicked point: (" + point.getX() + ", " + point.getY() + ")");
                    return;
                }
            }
        }
    }

    public void setPoints(Map<String, ArrayList<Coordinates>> loginCoord) {
        this.loginCoord = loginCoord;
    }
}
