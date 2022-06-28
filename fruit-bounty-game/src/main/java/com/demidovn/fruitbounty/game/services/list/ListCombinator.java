package com.demidovn.fruitbounty.game.services.list;

import java.util.ArrayList;
import java.util.List;

// https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
public class ListCombinator {

    public <T> List<List<T>> getCombinations(List<T> list, int r) {
        return getCombinations(list, list.size(), r);
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    private <T> List<List<T>> getCombinations(List<T> list, int n, int r) {
        List<List<T>> result = new ArrayList<>();

        // A temporary array to store all combination one by one
        List<T> data = new ArrayList<>(r);
        for (int i = 0; i < r; i++) {
            data.add(null);
        }

        // Print all combination using temporary array 'data[]'
        combinationUtil(list, data, 0, n - 1, 0, r, result);
        return result;
    }

    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Starting and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    private <T> void combinationUtil(List<T> arr, List<T> data, int start,
                                int end, int index, int r, List<List<T>> result) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            result.add(new ArrayList<>(data));
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data.set(index, arr.get(i));
            combinationUtil(arr, data, i + 1, end, index + 1, r, result);
        }
    }

}
