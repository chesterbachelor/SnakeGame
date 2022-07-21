package com.snakegame.snakegame;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setCoordinates(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Point point)) return false;
        return x == point.x && y == point.y;
    }
}
