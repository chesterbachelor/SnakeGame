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

public class HelloApplication extends Application {
    Canvas canvas;
    SnakePart head;
    int blockSize = 30;
    List<SnakePart> snake;
    Dir direction = Dir.right;
    Scene scene;
    Pane pane;

    public enum Dir {
        up, down, left, right
    }


    @Override

    public void start(Stage stage) throws IOException {


        canvas = new Canvas(500, 500);
        snake = new ArrayList<>();

        head = new SnakePart(50, 50, blockSize, blockSize);
        head.setFill(Paint.valueOf("RED"));
        snake.add(head);
        SnakePart tail = new SnakePart(snake.get(0).locationX -blockSize,snake.get(0).locationY,blockSize,blockSize);
        snake.add(tail);
        SnakePart tail1 = new SnakePart(snake.get(1).locationX -blockSize,snake.get(1).locationY,blockSize,blockSize);
        snake.add(tail1);



        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), this::drawSnake));
        timeline.setCycleCount(Timeline.INDEFINITE);

        pane = new Pane();


        pane.getChildren().add(canvas);
        pane.getChildren().add(head);
        pane.getChildren().add(tail);
        pane.getChildren().add(tail1);


        scene = new Scene(pane,600,600);

        stage.setHeight(800);
        stage.setWidth(800);
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
                        direction = Dir.down;
                        break;
                    case UP:
                        direction = Dir.up;
                        break;
                    case LEFT:
                        direction = Dir.left;
                        break;
                    case RIGHT:
                        direction = Dir.right;
                        break;
                }
            }
        });
        moveSnakeHead(head);
        for(int i =1;i<snake.size();i++)
            moveSnakeTail(snake.get(i),i);
    }


//    private void addSnakePart() {
//        SnakePart snakePart = new SnakePart(headLocationX, headLocationY, blockSize, blockSize);
//        snake.add(snakePart);
//        pane.getChildren().add(snakePart);
//    }

    private void drawSnake(ActionEvent actionEvent) {
        draw();
    }

    private void moveSnakeHead(SnakePart head) {
        if (direction.equals(Dir.right)) {
            head.lastLocationY = head.locationY;
            head.lastLocationX = head.locationX;
            head.locationX = head.locationX + blockSize;
            head.setX(head.locationX);

        } else if (direction.equals(Dir.left)) {
            head.lastLocationY = head.locationY;
            head.lastLocationX = head.locationX;
            head.locationX = head.locationX - blockSize;
            head.setX(head.locationX);

        } else if (direction.equals(Dir.down)) {
            head.lastLocationX = head.locationX;
            head.lastLocationY = head.locationY;
            head.locationY = head.locationY + blockSize;
            head.setY(head.locationY);

        } else if (direction.equals(Dir.up)) {
            head.lastLocationX = head.locationX;
            head.lastLocationY = head.locationY;
            head.locationY = head.locationY - blockSize;
            head.setY(head.locationY);
        }
    }

    private void moveSnakeTail(SnakePart tail,int position){
        tail.lastLocationX = tail.getX();
        tail.lastLocationY = tail.getY();
        tail.setX(snake.get(position-1).lastLocationX);
        tail.setY(snake.get(position-1).lastLocationY);

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
