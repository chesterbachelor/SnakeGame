package com.snakegame.snakegame;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class EngineTest {
    Engine engine;
    IRandomLocationGenerator locationGeneratorMock = Mockito.mock(IRandomLocationGenerator.class);

    @Test
    void isGameOver_whenSnakeSizeEqualsGridSize_returnsGameWon() {
        //given

        Point dummyPoint = new Point(1, 1);
        engine = new Engine(locationGeneratorMock, 2, Direction.right, dummyPoint, dummyPoint, dummyPoint, dummyPoint);

        //when
        GameStatus gameStatus = engine.isGameOver();

        //then
        assertSame(GameStatus.GAME_WON, gameStatus);

    }

    @Test
    void isGameOver_whenSnakeHeadCollidesWithTail_returnsGameOver() {
        //given
        Point snakePiece = new Point(1, 1);
        engine = new Engine(locationGeneratorMock, 2, Direction.right, snakePiece, snakePiece);

        //when
        GameStatus gameStatus = engine.isGameOver();

        //then
        assertSame(GameStatus.GAME_OVER, gameStatus);

    }

    @Test
    void isGameOver_whenSnakeSizeSmallerThanGridSize_returnsGameRunning() {
        Point head = new Point(1, 1);
        Point tail = new Point(0, 1);
        engine = new Engine(locationGeneratorMock, 3, Direction.right, head, tail);

        GameStatus gameStatus = engine.isGameOver();

        assertSame(GameStatus.GAME_RUNNING, gameStatus);
    }

    @Test
    void isGameOver_whenSnakeSizeIsAlmostEqualToGridSize_returnsGameRunning() {
        Point head = new Point(1, 1);
        Point firstTail = new Point(0, 1);
        Point secondTail = new Point(0, 0);
        engine = new Engine(locationGeneratorMock, 2, Direction.right, head, firstTail, secondTail);

        GameStatus gameStatus = engine.isGameOver();

        assertSame(GameStatus.GAME_RUNNING, gameStatus);
    }

    @Test
    void getScore_alwaysReturnsSnakeSizeTimesHundred() {
        Point dummyPoint = new Point(0, 0);
        engine = new Engine(locationGeneratorMock, 5, Direction.right, dummyPoint, dummyPoint, dummyPoint, dummyPoint);

        int scoreToTest = engine.getScore();

        assertEquals(400, scoreToTest);
    }

    @Test
    void getSnake_alwaysReturnsSnakePartsWithCoordinatesGivenInConstructor() {
        Point head = new Point(3, 5);
        Point tail = new Point(2, 5);

        engine = new Engine(locationGeneratorMock, 6, Direction.right, head, tail);
        List<SnakePart> snakeToTest = engine.getSnake();

        assertEquals(2, snakeToTest.size());
        assertEquals(head, snakeToTest.get(0).currentLocation);
        assertEquals(head, snakeToTest.get(0).lastLocation);
        assertEquals(tail, snakeToTest.get(1).currentLocation);
        assertEquals(tail, snakeToTest.get(1).lastLocation);
    }

    @Test
    void getFood_alwaysReturnsGeneratedFoodLocation() {
        int dimension = 5;
        Point foodLocation = new Point(2, 4);
        Mockito.when(locationGeneratorMock.generatePoint(dimension, dimension)).thenReturn(foodLocation);
        engine = new Engine(locationGeneratorMock, dimension, Direction.right, new Point(1, 1));

        SnakePart foodLocationToTest = engine.getFood();

        assertEquals(foodLocation, foodLocationToTest.currentLocation);
    }

    @Test
    void moveSnake_whenDirectionEqualsRight_SnakeMovesRight() {
        Point head = new Point(2, 2);
        Point tail = new Point(1, 2);
        engine = new Engine(locationGeneratorMock, 4, Direction.right, head, tail);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(2 + 1, snakeToTest.get(0).currentLocation.x);
        assertEquals(2, snakeToTest.get(0).currentLocation.y);
        assertEquals(2, snakeToTest.get(1).currentLocation.x);
        assertEquals(2, snakeToTest.get(1).currentLocation.y);
    }

    @Test
    void moveSnake_whenDirectionEqualsUp_SnakeMovesUp() {
        Point head = new Point(2, 2);
        Point tail = new Point(1, 2);
        engine = new Engine(locationGeneratorMock, 4, Direction.up, head, tail);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(2, snakeToTest.get(0).currentLocation.x);
        assertEquals(2 - 1, snakeToTest.get(0).currentLocation.y);
        assertEquals(2, snakeToTest.get(1).currentLocation.x);
        assertEquals(2, snakeToTest.get(1).currentLocation.y);
    }

    @Test
    void moveSnake_whenDirectionEqualsDown_SnakeMovesDown() {
        Point head = new Point(2, 2);
        Point tail = new Point(1, 2);
        engine = new Engine(locationGeneratorMock, 4, Direction.down, head, tail);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(2, snakeToTest.get(0).currentLocation.x);
        assertEquals(2 + 1, snakeToTest.get(0).currentLocation.y);
        assertEquals(2, snakeToTest.get(1).currentLocation.x);
        assertEquals(2, snakeToTest.get(1).currentLocation.y);
    }

    @Test
    void moveSnake_whenDirectionEqualsLeft_SnakeMovesLeft() {
        Point head = new Point(2, 2);
        Point tail = new Point(3, 2);
        engine = new Engine(locationGeneratorMock, 4, Direction.left, head, tail);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(2 - 1, snakeToTest.get(0).currentLocation.x);
        assertEquals(2, snakeToTest.get(0).currentLocation.y);
        assertEquals(2, snakeToTest.get(1).currentLocation.x);
        assertEquals(2, snakeToTest.get(1).currentLocation.y);
    }

    @Test
    void moveSnake_whenSnakeHeadHitsRightBorder_SnakeAppearsOnTheLeftSide() {
        Point head = new Point(2, 2);
        Point tail = new Point(1, 2);
        engine = new Engine(locationGeneratorMock, 3, Direction.right, head, tail);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(0, snakeToTest.get(0).currentLocation.x);
        assertEquals(2, snakeToTest.get(0).currentLocation.y);
    }

    @Test
    void moveSnake_whenSnakeHeadHitsLeftBorder_SnakeAppearsOnTheRightSide() {
        Point head = new Point(0, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.left, head);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(2, snakeToTest.get(0).currentLocation.x);
        assertEquals(0, snakeToTest.get(0).currentLocation.y);
    }

    @Test
    void moveSnake_whenSnakeHeadHitsUpperBorder_SnakeAppearsOnTheBottom() {
        Point head = new Point(0, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.up, head);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(0, snakeToTest.get(0).currentLocation.x);
        assertEquals(2, snakeToTest.get(0).currentLocation.y);
    }

    @Test
    void moveSnake_whenSnakeHeadHitsBottomBorder_SnakeAppearsOnTheTop() {
        Point head = new Point(2, 2);
        engine = new Engine(locationGeneratorMock, 3, Direction.down, head);

        engine.moveSnake();

        List<SnakePart> snakeToTest = engine.getSnake();
        assertEquals(2, snakeToTest.get(0).currentLocation.x);
        assertEquals(0, snakeToTest.get(0).currentLocation.y);
    }

    @Test
    void moveSnake_whenSnakeHeadStepsOnFood_FoodIsAppendedToSnake() {
        int dimension = 3;
        Mockito.when(locationGeneratorMock.generatePoint(dimension, dimension)).thenReturn(new Point(2, 1))
                .thenReturn(new Point(0, 0));
        Point head = new Point(1, 1);
        Point tail = new Point(0, 1);
        engine = new Engine(locationGeneratorMock, dimension, Direction.right, head, tail);
        int snakeSizeBeforeAppending = engine.getSnake().size();
        assertEquals(2, snakeSizeBeforeAppending);

        engine.moveSnake();

        int snakeSizeAfterAppending = engine.getSnake().size();
        assertEquals(3, snakeSizeAfterAppending);
    }

    @Test
    void changeDirection_whenGivenCorrectUpDirection_SnakeMovesUp() {
        Point head = new Point(1, 2);
        engine = new Engine(locationGeneratorMock, 3, Direction.right, head);

        engine.changeDirection(Direction.up);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1, snakeHead.currentLocation.x);
        assertEquals(2 - 1, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirectionDown_whenGivenCorrectDownDirection_SnakeMovesDown() {
        Point head = new Point(1, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.right, head);

        engine.changeDirection(Direction.down);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1, snakeHead.currentLocation.x);
        assertEquals(0 + 1, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirectionRight_whenGivenCorrectRightDirection_SnakeMovesRight() {
        Point head = new Point(1, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.up, head);

        engine.changeDirection(Direction.right);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1 + 1, snakeHead.currentLocation.x);
        assertEquals(0, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirection_whenGivenCorrectLeftDirection_SnakeMovesLeft() {
        Point head = new Point(1, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.up, head);

        engine.changeDirection(Direction.left);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1 - 1, snakeHead.currentLocation.x);
        assertEquals(0, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirection_whenGivenOppositeLeftDirection_SnakeStillMovesRight() {
        Point head = new Point(1, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.right, head);

        engine.changeDirection(Direction.left);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1 + 1, snakeHead.currentLocation.x);
        assertEquals(0, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirection_whenGivenOppositeRightDirection_SnakeStillMovesLeft() {
        Point head = new Point(1, 0);
        engine = new Engine(locationGeneratorMock, 3, Direction.left, head);

        engine.changeDirection(Direction.right);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1 - 1, snakeHead.currentLocation.x);
        assertEquals(0, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirection_whenGivenOppositeUpDirection_SnakeStillMovesDown() {
        Point head = new Point(1, 1);
        engine = new Engine(locationGeneratorMock, 3, Direction.down, head);

        engine.changeDirection(Direction.up);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1, snakeHead.currentLocation.x);
        assertEquals(1 + 1, snakeHead.currentLocation.y);
    }

    @Test
    void changeDirection_whenGivenOppositeDownDirection_SnakeStillMovesUp() {
        Point head = new Point(1, 1);
        engine = new Engine(locationGeneratorMock, 3, Direction.up, head);

        engine.changeDirection(Direction.down);
        engine.moveSnake();

        SnakePart snakeHead = engine.getSnake().get(0);
        assertEquals(1, snakeHead.currentLocation.x);
        assertEquals(1 - 1, snakeHead.currentLocation.y);
    }

}
