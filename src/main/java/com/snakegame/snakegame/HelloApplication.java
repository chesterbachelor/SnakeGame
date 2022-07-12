package com.snakegame.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    final int WIDTH = 600;
    final int HEIGHT = 600;
    Canvas canvas;
    SnakePart head;
    int blockSize = 50;
    List<SnakePart> snake = new ArrayList<>();
    Dir direction;
    Dir previousDirection;
    Scene scene;
    Pane pane;
    Random random = new Random();
    int rows = HEIGHT / blockSize;
    int columns = WIDTH / blockSize;
    SnakePart food;
    Timeline timeline;
    GraphicsContext graphicsContext;
    boolean gameRunning = false;

    public enum Dir {
        up, down, left, right
    }


    @Override

    public void start(Stage stage) throws IOException {
        canvas = new Canvas(WIDTH, HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();
        pane = new Pane();

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0,0,WIDTH,HEIGHT);

        pane.getChildren().add(canvas);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Press ENTER to start", 15,15);

        timeline = new Timeline(new KeyFrame(Duration.millis(200), this::drawSnake));
        timeline.setCycleCount(Timeline.INDEFINITE);

        scene = new Scene(pane);

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
                    case ENTER:
                        if(!gameRunning) {
                            createStartingBoard();
                            gameRunning = true;
                            timeline.play();
                        }
                }
            }
        });

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();


    }

    private void draw() {
        isSnakeAlive();
        if(!gameRunning) {
            return;
        }
        moveSnakeHead(head);
        for (int i = 1; i < snake.size(); i++)
            moveSnakeTail(snake.get(i), i);
        verifyIfFoodEaten(food);
    }


    private void addSnakePart() {
        SnakePart snakePart = new SnakePart(head.lastLocationX, head.lastLocationY, blockSize, blockSize);
        snakePart.setFill(Color.BEIGE);
        snake.add(snakePart);
        pane.getChildren().add(snakePart);
    }

    private void drawSnake(ActionEvent actionEvent) {
        if (snake.size() == rows * columns)
            endGame();
        draw();


        previousDirection = direction;
    }

    private void moveSnakeHead(SnakePart head) {
        if (direction.equals(Dir.right)) {
            head.lastLocationY = head.locationY;
            head.lastLocationX = head.locationX;
            head.locationX = head.locationX + blockSize;
            if (head.locationX > canvas.getWidth() - blockSize)
                head.locationX = 0;
            head.setX(head.locationX);

        } else if (direction.equals(Dir.left)) {
            head.lastLocationY = head.locationY;
            head.lastLocationX = head.locationX;
            head.locationX = head.locationX - blockSize;
            if (head.getX() == 0)
                head.locationX = canvas.getWidth() - blockSize;
            head.setX(head.locationX);

        } else if (direction.equals(Dir.down)) {
            head.lastLocationX = head.locationX;
            head.lastLocationY = head.locationY;
            head.locationY = head.locationY + blockSize;
            if (head.locationY > canvas.getHeight() - blockSize)
                head.locationY = 0;
            head.setY(head.locationY);

        } else if (direction.equals(Dir.up)) {
            head.lastLocationX = head.locationX;
            head.lastLocationY = head.locationY;
            head.locationY = head.locationY - blockSize;
            if (head.locationY < -1)
                head.locationY = canvas.getHeight() - blockSize;
            head.setY(head.locationY);
        }
    }

    private void moveFood() {
        spawnFoodInRandomLocation();
        while (verifyIfFoodInsideSnake())
            moveFood();
    }

    private void createStartingBoard(){
        for(SnakePart snakepart: snake) {
            pane.getChildren().remove(snakepart);
        }
        pane.getChildren().removeIf(Text.class::isInstance);
        pane.getChildren().remove(food);
        snake.clear();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0,0,WIDTH,HEIGHT);


        direction = Dir.right;

        head = new SnakePart(canvas.getHeight() / 2, canvas.getWidth() / 2, blockSize, blockSize);
        head.setFill(Color.BEIGE);
        snake.add(head);
        SnakePart tail = new SnakePart(snake.get(0).locationX - blockSize, snake.get(0).locationY, blockSize, blockSize);
        tail.setFill(Color.BEIGE);
        snake.add(tail);
        food = new SnakePart(-50, -50, blockSize, blockSize);
        moveFood();

        pane.getChildren().add(food);
        pane.getChildren().add(head);
        pane.getChildren().add(tail);

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
                snake.get(i).setFill(Color.RED);
                endGame();
            }
        }
    }

    private void endGame() {
        gameRunning = false;
        Text gameOver = new Text(25,25,"Game Over\n Press ENTER to retry");
        gameOver.setFill(Paint.valueOf("#67007a"));
        gameOver.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));

        pane.getChildren().add(gameOver);

        timeline.stop();
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
