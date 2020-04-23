package com.example.bowling.entity;

import static com.example.bowling.ARandom.aRandom;

public class RandomFrameEntityBuilder extends FrameEntity.FrameEntityBuilder {
    public RandomFrameEntityBuilder() {
        int firstRoll = aRandom.number().numberBetween(0,9);
        int secondRoll = aRandom.number().numberBetween(0, 9 - firstRoll);
        int[] rolls = {firstRoll, secondRoll};
        this.id(aRandom.uuid())
                .rolls(rolls);
    }
}
