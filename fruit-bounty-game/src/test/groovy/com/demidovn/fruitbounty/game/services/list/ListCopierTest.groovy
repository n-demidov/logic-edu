package com.demidovn.fruitbounty.game.services.list

import spock.lang.Specification

class ListCopierTest extends Specification {
    ListCopier listCopier = new ListCopier()

    def "should make deep copy correctly"() {
        setup:
        int d = 3
        List<List<Integer>> input = new ArrayList<>()

        for (int i = 0; i < d; i++) {
            List<Integer> tmpList = new ArrayList<>()
            tmpList.add(3)
            tmpList.add(9)
            input.add(tmpList)
        }

        when:
        List<List<Integer>> actual = listCopier.deepCopy(input)

        for (int i = 0; i < d; i++) {
            List<Integer> tempList = actual.get(i)
            for (int jj = 0; jj < tempList.size(); jj++) {
                int val = tempList.get(jj)
                tempList.set(jj, val + 100)
            }
        }

        then:
        actual.size() == input.size()
        actual != input

        input.get(0).get(0) == 3
        input.get(0).get(1) == 9

        actual.get(0).get(0) == 103
        actual.get(0).get(1) == 109
    }

}
