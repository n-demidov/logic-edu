package com.demidovn.fruitbounty.game.services.list

import spock.lang.Specification

class ListPermutterTest extends Specification {

    ListPermutter listPermutter = new ListPermutter()

    def "should works correctly"() {
        expect:
        def actual = apply(list)
        actual == result

        where:
        list           | result
        [0, 1]         | [[0, 1], [1, 0]]
        [0, 1, 2]      | [[0, 1, 2], [1, 0, 2], [2, 0, 1], [0, 2, 1], [1, 2, 0], [2, 1, 0]]
        [0, 1, 2, 3]   | [[0, 1, 2, 3], [1, 0, 2, 3], [2, 0, 1, 3], [0, 2, 1, 3], [1, 2, 0, 3], [2, 1, 0, 3], [3, 1, 0, 2], [1, 3, 0, 2], [0, 3, 1, 2], [3, 0, 1, 2], [1, 0, 3, 2], [0, 1, 3, 2], [0, 2, 3, 1], [2, 0, 3, 1], [3, 0, 2, 1], [0, 3, 2, 1], [2, 3, 0, 1], [3, 2, 0, 1], [3, 2, 1, 0], [2, 3, 1, 0], [1, 3, 2, 0], [3, 1, 2, 0], [2, 1, 3, 0], [1, 2, 3, 0]]

        // With duplicate values
        [0, 0, 2]      | [[0, 0, 2], [0, 0, 2], [2, 0, 0], [0, 2, 0], [0, 2, 0], [2, 0, 0]]
        [0, 0, 0]      | [[0, 0, 0], [0, 0, 0], [0, 0, 0], [0, 0, 0], [0, 0, 0], [0, 0, 0]]
    }

    def "should works correctly unique"() {
        expect:
        def actual = applyUnique(list)
        new ArrayList<>(actual) == result

        where:
        list           | result
        [0, 1]         | [[0, 1], [1, 0]]
        [0, 1, 2]      | [[0, 1, 2], [1, 0, 2], [2, 0, 1], [0, 2, 1], [1, 2, 0], [2, 1, 0]]
        [0, 1, 2, 3]   | [[0, 1, 2, 3], [1, 0, 2, 3], [2, 0, 1, 3], [0, 2, 1, 3], [1, 2, 0, 3], [2, 1, 0, 3], [3, 1, 0, 2], [1, 3, 0, 2], [0, 3, 1, 2], [3, 0, 1, 2], [1, 0, 3, 2], [0, 1, 3, 2], [0, 2, 3, 1], [2, 0, 3, 1], [3, 0, 2, 1], [0, 3, 2, 1], [2, 3, 0, 1], [3, 2, 0, 1], [3, 2, 1, 0], [2, 3, 1, 0], [1, 3, 2, 0], [3, 1, 2, 0], [2, 1, 3, 0], [1, 2, 3, 0]]

        // With duplicate values
        [0, 0, 2]      | [[0, 0, 2], [2, 0, 0], [0, 2, 0]]
        [0, 0, 0]      | [[0, 0, 0]]
    }

    def apply(List<Integer> list) {
        return listPermutter.permute(list)
    }

    def applyUnique(List<Integer> list) {
        return listPermutter.uniquePermute(list)
    }

}
