package com.demidovn.fruitbounty.game.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
//    private final Random random = new Random();
//
//    public int generateRandomInt(int upperRange) {
//        return random.nextInt(upperRange);
//    }

    public int generateFromRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public <T> T getRandomElement(Set<T> set) {
        return getRandomElement(new ArrayList<>(set));
    }

    public <T> T getRandomElement(List<T> list) {
        return list.get(generateFromRange(0, list.size() - 1));
    }

    public <T> void shuffle(List<T> collection) {
        Collections.shuffle(collection);
    }

    public <T> void shuffleIncludeSublists(List<List<T>> collection) {
        shuffle(collection);
        for (int i = 0; i < collection.size(); i++) {
            shuffle(collection.get(i));
        }
    }

    public boolean isPercentFired(int percentChance) {
        if (percentChance < 0 || percentChance > 100) {
            throw new IllegalArgumentException("Wrong percentChance: " + percentChance);
        }

        int randomPercent = generateFromRange(1, 100);
        return randomPercent <= percentChance;
    }
}
