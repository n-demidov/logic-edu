package com.demidovn.fruitbounty.game.services.list

import spock.lang.Specification

class ListCombinatorTest extends Specification {

    ListCombinator listCombinator = new ListCombinator()

    def "should works correctly"() {
        expect:
        apply(list, nDimension) == result

        where:
        list            | nDimension | result
        [0, 1, 2, 3, 4] | 1          | [[0], [1], [2], [3], [4]]
        [0, 1, 2, 3, 4] | 2          | [[0, 1], [0, 2], [0, 3], [0, 4], [1, 2], [1, 3], [1, 4], [2, 3], [2, 4], [3, 4]]
        [0, 1, 2, 3, 4] | 3          | [[0, 1, 2], [0, 1, 3], [0, 1, 4], [0, 2, 3], [0, 2, 4], [0, 3, 4], [1, 2, 3], [1, 2, 4], [1, 3, 4], [2, 3, 4]]
        [0, 1, 2, 3, 4] | 4          | [[0, 1, 2, 3], [0, 1, 2, 4], [0, 1, 3, 4], [0, 2, 3, 4], [1, 2, 3, 4]]
        [0, 1, 2, 3, 4] | 5          | [[0, 1, 2, 3, 4]]

        [1, 2, 3]       | 2          | [[1, 2], [1, 3], [2, 3]]
    }

    def apply(List<Integer> list, int nDimension) {
        return listCombinator.getCombinations(list, nDimension)
    }

}
