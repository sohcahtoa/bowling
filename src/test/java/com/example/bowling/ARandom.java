package com.example.bowling;

import com.github.javafaker.Faker;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ARandom {
    @Delegate
    private Random random;

    @Delegate
    private Faker faker;


    public ARandom() {
        long seed = System.currentTimeMillis();
        random = new Random(seed);
        faker = Faker.instance(random);
    }

    public static final ARandom aRandom = new ARandom();

    public UUID uuid() {
        return UUID.randomUUID();
    }

    public List<String> playerNames(int numPlayers) {
        List<String> playerNames = new ArrayList<>(numPlayers);
        for(int i = 0; i < numPlayers; i++) {
            playerNames.add(aRandom.name().firstName());
        }
        return playerNames;
    }
}
