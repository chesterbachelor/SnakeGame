package com.snakegame.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {
    private final int DIMENSION = 24;
    private final int boardSize = 600;
    private final int INTERVAL_MS = 600;
    private Canvas canvas;
    private Timeline timeline;
    private boolean gameRunning = false;
    private IPainter painter;
    private IEngine engine;

    @Override
    public void start(Stage stage) {
        createStartingBoard();
        painter.clearScreen();
        painter.paintText("Press ENTER to start");

        Pane pane = new Pane();
        pane.getChildren().add(canvas);

        timeline = new Timeline(new KeyFrame(Duration.millis(INTERVAL_MS), this::draw));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Scene scene = new Scene(pane);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case DOWN:
                    case UP:
                    case LEFT:
                    case RIGHT:
                        engine.changeDirection(convertCodeToDirection(e.getCode()));
                        break;
                    case ENTER:
                        if (!gameRunning) {
                            engine = new Engine(DIMENSION);
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

    private Direction convertCodeToDirection(KeyCode code) {
        switch (code) {
            case DOWN:
                return Direction.down;
            case UP:
                return Direction.up;
            case LEFT:
                return Direction.left;
            case RIGHT:
                return Direction.right;
            default:
                return null;
        }
    }

    private void draw(ActionEvent actionEvent) {
        painter.clearScreen();
        if (engine.isGameOver() == GameStatus.GAME_OVER) {
            endTheGameUnsuccessfully();
            return;
        }
        if (engine.isGameOver() == GameStatus.GAME_WON) {
            endTheGameSuccessfully();
            return;
        }
        nextMove();
    }

    private void createStartingBoard() {
        canvas = new Canvas(boardSize, boardSize);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        painter = new Painter(boardSize, DIMENSION, graphicsContext);
    }

    private void endTheGameUnsuccessfully() {
        painter.paintDeadSnake(engine.getSnake());
        painter.paintScore(engine.getScore());
        painter.paintText("Game Over \n Press Enter to RESTART");
        stopTheGame();
    }

    private void endTheGameSuccessfully() {
        painter.paintSnake(engine.getSnake());
        painter.paintScore(engine.getScore());
        painter.paintText("CONGRATULATIONS YOU WON!!");
        stopTheGame();
    }

    private void stopTheGame(){
        gameRunning = false;
        timeline.stop();
    }

    private void nextMove() {
        engine.moveSnake();
        painter.paintSnake(engine.getSnake());
        painter.paintFood(engine.getFood());
        painter.paintScore(engine.getScore());
    }

    public static void main(String[] args) {
        launch();
    }
}


