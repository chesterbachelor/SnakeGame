package com.snakegame.snakegame;

import java.util.List;

public interface IPainter {
    void paintSnake(List<SnakePart> snake);

    void paintFood(SnakePart food);

    void paintScore(int score);

    void paintText(String message);

    void clearScreen();

    void paintDeadSnake(List<SnakePart> snake);

}
