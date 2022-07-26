package com.snakegame.snakegame;

import java.util.Random;

public class RandomLocationGenerator implements IRandomLocationGenerator {
    Random random = new Random();

    @Override
    public Point generatePoint(int xRange, int yRange) {
        return new Point(random.nextInt(xRange), random.nextInt(yRange));
    }
}
