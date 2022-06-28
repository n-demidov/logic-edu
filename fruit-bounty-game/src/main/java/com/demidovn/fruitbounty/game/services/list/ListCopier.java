package com.demidovn.fruitbounty.game.services.list;

import java.util.ArrayList;
import java.util.List;

public class ListCopier {

    public <T> List<List<T>> deepCopy(List<List<T>> list) {
        int size = list.size();
        List<List<T>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(new ArrayList<>(list.get(i)));
        }
        return result;
    }

}
