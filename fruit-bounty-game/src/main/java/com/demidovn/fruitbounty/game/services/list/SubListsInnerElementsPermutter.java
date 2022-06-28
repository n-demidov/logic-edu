package com.demidovn.fruitbounty.game.services.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubListsInnerElementsPermutter {
    private static final int NOT_INITIALIZED_VALUE = -1;

    private final ListPermutter listPermutter = new ListPermutter();

    public <T> Set<List<List<T>>> permuteOnlyElementsInSublists(List<List<T>> lists) {
        int size = lists.size();
        int counterMaxValue = NOT_INITIALIZED_VALUE;
        Set<List<List<T>>> result = new LinkedHashSet<>(size);

        Map<Integer, List<List<T>>> listsById = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            List<T> list = lists.get(i);
            List<List<T>> permutations = listPermutter.permute(list);
            listsById.put(i, new ArrayList<>(permutations));
            counterMaxValue = permutations.size();
        }
        if (counterMaxValue == NOT_INITIALIZED_VALUE) {
            throw new IllegalStateException("counterMaxValue NOT_INITIALIZED_VALUE");
        }

        Counter counter = new Counter(size, counterMaxValue);
        while (counter.hasNext()) {
            List<List<T>> list = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                int counterValue = counter.counters[i];
                list.add(listsById.get(i).get(counterValue));
            }

            result.add(list);
            counter.increment();
        }

        return result;
    }

    private static class Counter {
        private final int[] counters;
        private final int countersNum;
        private final int counterMaxValue;
        private boolean hasNext = true;

        public Counter(int countersNum, int counterMaxValue) {
            this.countersNum = countersNum;
            this.counterMaxValue = counterMaxValue;
            this.counters = new int[countersNum];
        }

        private boolean hasNext() {
            return hasNext;
        }

        private void increment() {
            for (int i = countersNum - 1; i >= 0; i--) {
                int counterValue = counters[i];
                if (counterValue < counterMaxValue - 1) {
                    counters[i]++;
                    return;
                } else {
                    if (i == 0) {
                        hasNext = false;
                        return;
                    }
                    counters[i] = 0;
                }
            }
        }
    }

}
