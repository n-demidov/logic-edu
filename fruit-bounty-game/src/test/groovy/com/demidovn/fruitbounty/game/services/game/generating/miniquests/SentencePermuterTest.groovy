package com.demidovn.fruitbounty.game.services.game.generating.miniquests

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition
import spock.lang.Specification

import java.util.stream.Collectors

class SentencePermuterTest extends Specification {

    private SentencePermuter sentencePermuter = new SentencePermuter()

    def "should invalid correctly"() {
        expect:
        apply(subjectIdxs, objectIdxs, inverses) == resultIds

        where:
        subjectIdxs      | objectIdxs       | inverses                           | resultIds
        // "Swede drank whiskey. Swede did not drink rum."
        [0, 0]           | [0, 1]           | [false, true]                      | [1, 0]
        [1, 0, 2, 0, 3]  | [2, 0, 2, 1, 2]  | [true, false, true, true, true]    | [0, 3, 2, 1, 4]
        [0, 0, 2, 2, 3]  | [0, 1, 0, 1, 3]  | [false, true, false, true, false]  | [1, 0, 3, 2, 4]
    }

    def "should valid correctly"() {
        expect:
        apply(subjectIdxs, objectIdxs, inverses) == resultIds

        where:
        subjectIdxs      | objectIdxs       | inverses                           | resultIds
        [0, 0]           | [0, 1]           | [true, true]                       | [0, 1]
        [0, 1]           | [0, 1]           | [false, true]                      | [0, 1]
        [0, 0]           | [0, 1]           | [true, false]                      | [0, 1]
        [0, 1, 2, 0, 1]  | [0, 1, 0, 1, 0]  | [true, true, false, false, false]  | [0, 1, 2, 3, 4]
    }

    List<Integer> apply(List<Integer> subjectIdxs, List<Integer> objectIdxs, List<Boolean> inverses) {
        List<MiniquestCondition> conditions = new ArrayList<>(subjectIdxs.size())
        int idCounter = 0
        for (int i = 0; i < subjectIdxs.size(); i++) {
            def condition = new MiniquestCondition(
                    idCounter,
                    subjectIdxs.get(i),
                    objectIdxs.get(i),
                    inverses.get(i))
            idCounter++
            conditions.add(condition)
        }

        sentencePermuter.permuteIfNeed(conditions)

        return conditions.stream()
                .map({ c -> c.id })
                .collect(Collectors.toList())
    }

}
