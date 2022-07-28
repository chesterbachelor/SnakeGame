package com.snakegame.snakegame;

import java.util.List;

public interface IEngine {

    boolean changeDirection(Direction direction);

    void moveSnake();

    GameStatus isGameOver();

    List<SnakePart> getSnake();

    int getScore();

    SnakePart getFood();

}
