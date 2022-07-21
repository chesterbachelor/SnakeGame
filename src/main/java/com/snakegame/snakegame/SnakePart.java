package com.snakegame.snakegame;

public class SnakePart {
    Point currentLocation;
    Point lastLocation;

    public SnakePart(Point currentLocation, Point lastLocation) {
        this.currentLocation = new Point(currentLocation.x, currentLocation.y);
        this.lastLocation = new Point(lastLocation.x, lastLocation.y);
    }
}

