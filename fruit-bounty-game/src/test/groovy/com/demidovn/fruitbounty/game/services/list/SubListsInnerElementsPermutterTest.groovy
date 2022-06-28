package com.demidovn.fruitbounty.game.services.list

import spock.lang.Specification

class SubListsInnerElementsPermutterTest extends Specification {

    SubListsInnerElementsPermutter subListsInnerElementsPermutter = new SubListsInnerElementsPermutter()

    def "should works correctly"() {
        expect:
        def actual = apply(list)
        new ArrayList<>(actual) == result

        where:
        list                      | result
        [[1, 2], [3, 4], [5, 6]]  | [[[1, 2], [3, 4], [5, 6]], [[1, 2], [3, 4], [6, 5]], [[1, 2], [4, 3], [5, 6]], [[1, 2], [4, 3], [6, 5]], [[2, 1], [3, 4], [5, 6]], [[2, 1], [3, 4], [6, 5]], [[2, 1], [4, 3], [5, 6]], [[2, 1], [4, 3], [6, 5]]]

        // With duplicates
        [[0, 0], [0, 0]]          | [[[0, 0], [0, 0]]]
        [[0, 0], [0, 0], [0, 0]]  | [[[0, 0], [0, 0], [0, 0]]]
        [[1, 0], [1, 0]]          | [[[1, 0], [1, 0]], [[1, 0], [0, 1]], [[0, 1], [1, 0]], [[0, 1], [0, 1]]]
    }

    def apply(List<List<Integer>> list) {
        return subListsInnerElementsPermutter.permuteOnlyElementsInSublists(list)
    }

}
