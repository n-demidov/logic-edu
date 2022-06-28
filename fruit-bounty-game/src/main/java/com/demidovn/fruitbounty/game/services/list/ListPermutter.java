package com.demidovn.fruitbounty.game.services.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ListPermutter {

    public <T> List<T> permute(List<?> list) {
        int size = list.size();
        List<T> result = new ArrayList<>();
        T[] objects = (T[]) list.toArray();
        permute(size, objects, result);
        return result;
    }

    public <T> Set<T> uniquePermute(List<?> list) {
        int size = list.size();
        Set<T> result = new LinkedHashSet<>();
        T[] objects = (T[]) list.toArray();
        permute(size, objects, result);
        return result;
    }

    public <T> void permute(int n, T[] elements, Collection<T> result) {
        result.add((T) toNewList(elements));
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                result.add((T) toNewList(elements));

                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }
    }

    private <T> void swap(T[] input, int a, int b) {
        T tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private <T> List<T> toNewList(T[] elements) {
        int size = elements.length;
        List<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(elements[i]);
        }
        return result;
    }

}
