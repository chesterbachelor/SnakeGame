package com.snakegame.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    Canvas canvas;
    SnakePart head;
    int blockSize = 50;
    List<SnakePart> snake;
    Dir direction = Dir.right;
    Dir previousDirection = direction;
    Scene scene;
    Pane pane;
    Random random = new Random();
    int rows;
    int columns;
    SnakePart food;

    public enum Dir {
        up, down, left, right
    }


    @Override

    public void start(Stage stage) throws IOException {

        canvas = new Canvas(300, 300);
        Rectangle background = new Rectangle(0, 0, canvas.getHeight(), canvas.getWidth());
        background.setFill(Paint.valueOf("BLACK"));

        pane = new Pane();
        columns = (int) canvas.getWidth() / blockSize;
        rows = (int) canvas.getHeight() / blockSize;
        snake = new ArrayList<>();

        head = new SnakePart(canvas.getHeight() / 2, canvas.getWidth() / 2, blockSize, blockSize);
        head.setFill(Paint.valueOf("RED"));
        snake.add(head);
        SnakePart tail = new SnakePart(snake.get(0).locationX - blockSize, snake.get(0).locationY, blockSize, blockSize);
        tail.setFill(Paint.valueOf("#fc9403"));
        snake.add(tail);
        SnakePart tail1 = new SnakePart(snake.get(1).locationX - blockSize, snake.get(1).locationY, blockSize, blockSize);
        tail1.setFill(Paint.valueOf("fc9403"));

        snake.add(tail1);

        food = new SnakePart(-50, -50, blockSize, blockSize);
        moveFood();

        pane.getChildren().add(canvas);
        pane.getChildren().add(background);
        pane.getChildren().add(food);
        pane.getChildren().add(head);
        pane.getChildren().add(tail);
        pane.getChildren().add(tail1);


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), this::drawSnake));
        timeline.setCycleCount(Timeline.INDEFINITE);


        scene = new Scene(pane);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        timeline.play();

    }

    private void draw() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case DOWN:
                        if (!(direction == Dir.up) && !(previousDirection == Dir.up))
                            direction = Dir.down;
                        break;
                    case UP:
                        if (!(direction == Dir.down) && !(previousDirection == Dir.down))
                            direction = Dir.up;
                        break;
                    case LEFT:
                        if (!(direction == Dir.right) && !(previousDirection == Dir.right))
                            direction = Dir.left;
                        break;
                    case RIGHT:
                        if (!(direction == Dir.left) && !(previousDirection == Dir.left))
                            direction = Dir.right;
                        break;
                }
            }
        });
        isSnakeAlive();
        moveSnakeHead(head);
        for (int i = 1; i < snake.size(); i++)
            moveSnakeTail(snake.get(i), i);
        verifyIfFoodEaten(food);
    }


    private void addSnakePart() {
        SnakePart snakePart = new SnakePart(head.lastLocationX, head.lastLocationY, blockSize, blockSize);
        snakePart.setFill(Paint.valueOf("fc9403"));
        snake.add(snakePart);
        pane.getChildren().add(snakePart);
    }


    private void drawSnake(ActionEvent actionEvent) {
        draw();
        previousDirection = direction;
    }

    private void moveSnakeHead(SnakePart head) {
        if (direction.equals(Dir.right)) {
            head.lastLocationY = head.locationY;
            head.lastLocationX = head.locationX;
            head.locationX = head.locationX + blockSize;
            if (head.locationX > canvas.getWidth())
                head.locationX = 0;
            head.setX(head.locationX);

        } else if (direction.equals(Dir.left)) {
            head.lastLocationY = head.locationY;
            head.lastLocationX = head.locationX;
            head.locationX = head.locationX - blockSize;
            if (head.locationX < 0)
                head.locationX = canvas.getWidth();
            head.setX(head.locationX);

        } else if (direction.equals(Dir.down)) {
            head.lastLocationX = head.locationX;
            head.lastLocationY = head.locationY;
            head.locationY = head.locationY + blockSize;
            if (head.locationY > canvas.getHeight())
                head.locationY = 0;
            head.setY(head.locationY);

        } else if (direction.equals(Dir.up)) {
            head.lastLocationX = head.locationX;
            head.lastLocationY = head.locationY;
            head.locationY = head.locationY - blockSize;
            if (head.locationY < 0)
                head.locationY = canvas.getHeight();
            head.setY(head.locationY);
        }
    }

    private void moveFood() {
        spawnFoodInRandomLocation();
        while (verifyIfFoodInsideSnake())
            moveFood();
    }

    private void spawnFoodInRandomLocation() {
        food.setFill(Paint.valueOf("Green"));
        food.locationX = random.nextInt(columns) * blockSize;
        food.locationY = random.nextInt(rows) * blockSize;

        food.setX(food.locationX);
        food.setY(food.locationY);
        System.out.println("X =" + food.locationX + " Y = " + food.locationY);
    }

    private boolean verifyIfFoodInsideSnake() {
        for (int i = 0; i < snake.size(); i++) {
            if (snake.get(i).getX() == food.getX() && snake.get(i).getY() == food.getY()) {
                return true;
            }
        }
        return false;
    }

    private void verifyIfFoodEaten(SnakePart food) {
        if (head.locationY == food.locationY && head.locationX == food.locationX) {
            System.out.println("eaten");
            addSnakePart();
            moveFood();
        }
    }

    private void isSnakeAlive() {
        for (int i = 1; i < snake.size(); i++) {
            if (head.getX() == snake.get(i).getX() && head.getY() == snake.get(i).getY()) {
                endGame();
            }
        }
    }

    private void endGame() {
        System.out.println("GG");
        System.exit(0);
    }

    private void moveSnakeTail(SnakePart tail, int position) {
        tail.lastLocationX = tail.getX();
        tail.lastLocationY = tail.getY();
        tail.setX(snake.get(position - 1).lastLocationX);
        tail.setY(snake.get(position - 1).lastLocationY);
    }


    public static void main(String[] args) {
        launch();
    }
}

class SnakePart extends Rectangle {
    double locationX;
    double locationY;
    double lastLocationX;
    double lastLocationY;

    public SnakePart(double v, double v1, double v2, double v3) {
        super(v, v1, v2, v3);
        locationX = v;
        locationY = v1;
        lastLocationX = v;
        lastLocationY = v1;
    }
}
