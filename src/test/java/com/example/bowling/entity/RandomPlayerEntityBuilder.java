package com.example.bowling.entity;

import static com.example.bowling.ARandom.aRandom;

public class RandomPlayerEntityBuilder extends PlayerEntity.PlayerEntityBuilder {
    public RandomPlayerEntityBuilder() {
        this.id(aRandom.uuid())
                .name(aRandom.name().firstName());
    }
}
