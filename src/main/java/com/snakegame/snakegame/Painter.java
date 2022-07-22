package com.snakegame.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class Painter implements IPainter {
    private final int BOARD_SIZE;
    private final int blockSize;
    private final GraphicsContext graphicsContext;

    Painter(int boardSize, int dimension, GraphicsContext graphicsContext) {
        this.BOARD_SIZE = boardSize;
        this.blockSize = BOARD_SIZE / dimension;
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void paintSnake(List<SnakePart> snake) {
        graphicsContext.setFill(Color.BEIGE);
        snake.forEach(snakePart -> graphicsContext.fillRect(
                snakePart.currentLocation.x * blockSize,
                snakePart.currentLocation.y * blockSize,
                blockSize,
                blockSize));
    }

    @Override
    public void paintFood(SnakePart food) {
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(food.currentLocation.x * blockSize, food.currentLocation.y * blockSize, blockSize, blockSize);
    }

    @Override
    public void paintScore(int score) {
        graphicsContext.setFill(Color.SKYBLUE);
        graphicsContext.fillText(String.valueOf(score), 10, BOARD_SIZE - 30);
    }

    @Override
    public void paintText(String message) {
        graphicsContext.setFont(new Font(25));
        graphicsContext.setFill(Color.VIOLET);
        graphicsContext.fillText(message, 20, 20);
    }

    @Override
    public void clearScreen() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);
    }

    @Override
    public void paintDeadSnake(List<SnakePart> snake) {
        graphicsContext.setFill(Color.BEIGE);
        snake.stream()
                .skip(1)
                .forEach(snakePart -> graphicsContext.fillRect(
                        snakePart.currentLocation.x * blockSize,
                        snakePart.currentLocation.y * blockSize,
                            blockSize,
                            blockSize));

        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect(snake.get(0).currentLocation.x * blockSize,
                snake.get(0).currentLocation.y * blockSize,
                blockSize,
                blockSize);
    }
}