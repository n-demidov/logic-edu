package com.demidovn.fruitbounty.game.services.list;

import com.demidovn.fruitbounty.game.services.Randomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ListUtil {
    private static final Randomizer rand = new Randomizer();

    public <T> List<T> getImmutable(List<T> list) {
        if (list == null) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }

    public void removeNItems(List<String> list, int remainingItems) {
        if (list.size() <= remainingItems) {
            return;
        }

        do {
            list.remove(0);
        } while (list.size() > remainingItems);
    }

    public <T> T getRandomItem(List<T> list) {
        int randomIdx = rand.generateFromRange(0, list.size() - 1);
        return list.get(randomIdx);
    }

    public List<Integer> toList(int[] array) {
        List<Integer> tempInverses = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            tempInverses.add(array[i]);
        }
        return tempInverses;
    }

    public List<Boolean> toList(boolean[] array) {
        List<Boolean> tempInverses = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            tempInverses.add(array[i]);
        }
        return tempInverses;
    }

    public int[] toArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}
