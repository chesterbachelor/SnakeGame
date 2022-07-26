package com.snakegame.snakegame;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Engine implements IEngine {
    private final int DIMENSION;
    private Direction currentDirection;
    private Direction previousDirection;
    private final List<SnakePart> snake;
    private final SnakePart food;
    private final SnakePart head;

    private final IRandomLocationGenerator locationGenerator;

    Engine(IRandomLocationGenerator locationGenerator , int dimension, Direction startingDirection, Point ... startingPoints ) {
        this.DIMENSION = dimension;
        this.locationGenerator = locationGenerator;

        this.snake = Arrays.stream(startingPoints)
                .map(partPosition -> new SnakePart(partPosition, partPosition))
                .collect(Collectors.toList());
        head = snake.get(0);

        currentDirection = startingDirection;
        previousDirection = currentDirection;
        Point point = new Point(0, 0);
        food = new SnakePart(point, point);
        moveFood();
    }


    private void eatFoodIfPossible() {
        if (head.currentLocation.equals(food.currentLocation)) {
            addSnakePart();
            moveFood();
        }
    }

    private void addSnakePart() {
        SnakePart snakePart = new SnakePart(snake.get(1).currentLocation, head.lastLocation);
        snake.add(snakePart);
    }

    private void moveFood() {
        spawnFoodInRandomLocation();
        while (foodSpawnedInsideSnake())
            moveFood();
    }

    private void spawnFoodInRandomLocation() {
        food.currentLocation = locationGenerator.generatePoint(DIMENSION, DIMENSION);
    }

    private boolean foodSpawnedInsideSnake() {

        return snake.stream()
                .anyMatch(currentPart -> currentPart.currentLocation.equals(food.currentLocation));
    }

    @Override
    public void changeDirection(Direction direction) {
        switch (direction) {
            case down:
                if (!(currentDirection == Direction.up) && !(previousDirection == Direction.up)) {
                    currentDirection = direction;
                }
                break;
            case up:
                if (!(currentDirection == Direction.down) && !(previousDirection == Direction.down)) {
                    currentDirection = direction;
                }
                break;
            case left:
                if (!(currentDirection == Direction.right) && !(previousDirection == Direction.right)) {
                    currentDirection = direction;
                }
                break;
            case right:
                if (!(currentDirection == Direction.left) && !(previousDirection == Direction.left)) {
                    currentDirection = direction;
                }
                break;

        }
    }

    @Override
    public void moveSnake() {
        moveSnakeHead(head);
        for (int i = 1; i < snake.size(); i++)
            moveSnakeTail(snake.get(i), snake.get(i - 1));
        eatFoodIfPossible();
        previousDirection = currentDirection;
    }

    private void moveSnakeHead(SnakePart headPart) {
        if (currentDirection.equals(Direction.right)) {
            headPart.lastLocation.setCoordinates(headPart.currentLocation);
            headPart.currentLocation.x += 1;
            if (headPart.currentLocation.x >= DIMENSION)
                headPart.currentLocation.x = 0;

        } else if (currentDirection.equals(Direction.left)) {
            headPart.lastLocation.setCoordinates(headPart.currentLocation);
            headPart.currentLocation.x -= 1;
            if (headPart.currentLocation.x < 0)
                headPart.currentLocation.x = DIMENSION - 1;

        } else if (currentDirection.equals(Direction.down)) {
            headPart.lastLocation.setCoordinates(headPart.currentLocation);
            headPart.currentLocation.y += 1;
            if (headPart.currentLocation.y >= DIMENSION)
                headPart.currentLocation.y = 0;

        } else if (currentDirection.equals(Direction.up)) {
            headPart.lastLocation.setCoordinates(headPart.currentLocation);
            headPart.currentLocation.y -= 1;
            if (headPart.currentLocation.y < 0)
                headPart.currentLocation.y = DIMENSION - 1;
        }
    }

    private void moveSnakeTail(SnakePart currentPart, SnakePart previousPart) {
        currentPart.lastLocation.setCoordinates(currentPart.currentLocation);
        currentPart.currentLocation.setCoordinates(previousPart.lastLocation);
    }

    @Override
    public GameStatus isGameOver() {
        if (snake.size() == DIMENSION * DIMENSION) {
            return GameStatus.GAME_WON;
        }

        boolean snakeHeadCollidesWithBody = snake.stream()
                .skip(1)
                .anyMatch(currentPart -> currentPart.currentLocation.equals(head.currentLocation));

        if (snakeHeadCollidesWithBody)
            return GameStatus.GAME_OVER;


        return GameStatus.GAME_RUNNING;
    }

    @Override
    public List<SnakePart> getSnake() {
        return snake;
    }

    @Override
    public int getScore() {
        return snake.size() * 100;
    }

    @Override
    public SnakePart getFood() {
        return food;
    }
}
