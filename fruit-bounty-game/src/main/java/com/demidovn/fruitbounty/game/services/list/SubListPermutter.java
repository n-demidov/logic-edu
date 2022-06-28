package com.demidovn.fruitbounty.game.services.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SubListPermutter {
    private final ListPermutter listPermutter = new ListPermutter();
    private final SubListsInnerElementsPermutter subListsInnerElementsPermutter = new SubListsInnerElementsPermutter();

    public <T> Set<List<List<T>>> permuteAll(List<List<T>> lists) {
        if (isAllEquals(lists)) {
            Set<List<List<T>>> result = new LinkedHashSet<>();
            result.add(lists);
            return result;
        }

        Set<List<List<T>>> listsPermutations = subListsInnerElementsPermutter.permuteOnlyElementsInSublists(lists);

        Set<List<List<T>>> result = new LinkedHashSet<>();
        Iterator<List<List<T>>> iterator = listsPermutations.iterator();
        while (iterator.hasNext()) {
            List<List<T>> listsPermutation = iterator.next();
            List<List<List<T>>> newPermutations = new ArrayList<>(listPermutter.uniquePermute(listsPermutation));
            result.addAll(newPermutations);
        }

        return result;
    }

    private <T> boolean isAllEquals(List<List<T>> lists) {
        T temp = null;

        for (int rowIdx = 0; rowIdx < lists.size(); rowIdx++) {
            List<T> col = lists.get(rowIdx);
            for (int colIdx = 0; colIdx < col.size(); colIdx++) {
                T value = col.get(colIdx);
                if (rowIdx == 0 && colIdx == 0) {
                    temp = value;
                } else {
                    if (!Objects.equals(value, temp)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
