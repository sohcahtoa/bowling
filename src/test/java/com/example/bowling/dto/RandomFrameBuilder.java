package com.example.bowling.dto;

import static com.example.bowling.ARandom.aRandom;

public class RandomFrameBuilder extends Frame.FrameBuilder {
    public RandomFrameBuilder() {
        this.id(aRandom.uuid());
    }
}
