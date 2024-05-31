package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.Windows.Main;
import io.github.zeculesu.itmo.prog5.GUI.Windows.MapMarines;
import io.github.zeculesu.itmo.prog5.GUI.Windows.Table;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CoordinatePlaneController {

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

    private final Color POINT_COLOR = Color.RED; // Цвет точек
    private final double POINT_RADIUS = 3.0; // Радиус точек

    private final double SCALE = 10.0; // Масштаб

    private double lastX, lastY; // Переменные для хранения предыдущих координат мыши
    private double dragStartX, dragStartY; // Начальные координаты мыши при начале перетаскивания
    private double dragOffsetX = 0, dragOffsetY = 0; // Смещение мыши при перетаскивании

    private final List<double[]> points = new ArrayList<>(); // Список точек для отображения

    public void initialize() {
        addPoint(5, 4);
        addPoint(8, 8);
        addPoint(-1, -2);
        redrawCoordinatePlane(); // Перерисовываем координатную плоскость и точки при инициализации

        // Добавляем обработчики событий мыши для перемещения карты
        coordinatePlane.setOnMousePressed(this::onMousePressed);
        coordinatePlane.setOnMouseDragged(this::onMouseDragged);

        // Добавляем обработчики событий для точек
        coordinatePlane.setOnMouseClicked(this::onPointClicked);




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
        // Добавляем обработчики событий для точек
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

    private void addPoint(double x, double y) {
        points.add(new double[]{x, y});
    }

    private void drawPoints(GraphicsContext gc, double width, double height) {
        gc.setFill(POINT_COLOR);

        // Переводим координаты точек в координаты на холсте с учетом масштаба и смещения
        for (double[] point : points) {
            double scaledX = width / 2 + point[0] * SCALE + dragOffsetX;
            double scaledY = height / 2 - point[1] * SCALE + dragOffsetY; // Инвертируем y и учитываем масштаб
            gc.fillOval(scaledX - POINT_RADIUS, scaledY - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }
    }

    private void onPointClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        // Увеличиваем радиус проверки для более широкой зоны нажатия
        double clickRadius = POINT_RADIUS * 2.5;

        // Проверяем, попадает ли нажатие мыши в радиус какой-либо точки
        for (double[] point : points) {
            double scaledX = coordinatePlane.getWidth() / 2 + point[0] * SCALE + dragOffsetX;
            double scaledY = coordinatePlane.getHeight() / 2 - point[1] * SCALE + dragOffsetY;
            if (Math.pow(mouseX - scaledX, 2) + Math.pow(mouseY - scaledY, 2) <= Math.pow(clickRadius, 2)) {
                // Если попали в точку, выводим информацию о ней
                System.out.println("Clicked point: (" + point[0] + ", " + point[1] + ")");
                return;
            }
        }
    }
}