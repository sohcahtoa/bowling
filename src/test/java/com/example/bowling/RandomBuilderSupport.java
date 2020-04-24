package com.example.bowling;

import com.example.bowling.dto.RandomFrameBuilder;
import com.example.bowling.entity.RandomFrameEntityBuilder;
import com.example.bowling.entity.RandomPlayerEntityBuilder;

public class RandomBuilderSupport {
    public RandomFrameEntityBuilder frameEntity() {
        return new RandomFrameEntityBuilder();
    }

    public RandomPlayerEntityBuilder playerEntity() {
        return new RandomPlayerEntityBuilder();
    }

    public RandomFrameBuilder frame() {
        return new RandomFrameBuilder();
    }
}
