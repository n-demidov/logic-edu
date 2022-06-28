package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition
import spock.lang.Specification

class CorrectSentenceValidatorTest extends Specification {
    private CorrectSentenceValidator sentenceValidator = new CorrectSentenceValidator()

    def "should invalid correctly"() {
        expect:
        apply(subjectIdxs, objectIdxs, inverses) == result

        where:
        subjectIdxs  | objectIdxs  | inverses        | result
        // Repeats
        [0, 0]       | [1, 1]      | [false, false]  | false
        [0, 0]       | [1, 1]      | [true, true]    | false

        // Self contradictions
        [0, 0]       | [1, 1]      | [true, false]   | false
        [0, 0]       | [1, 1]      | [false, true]   | false

        // "Swede drank whiskey. Swede drank rum." (self-contradiction)
        [0, 0]       | [1, 2]      | [false, false]  | false

        // Swede drink whiskey. Chinese drink whiskey." (self-contradiction)
        [0, 1]       | [1, 1]      | [false, false]  | false
    }

    def "should valid correctly"() {
        expect:
        apply(subjectIdxs, objectIdxs, inverses) == result

        where:
        subjectIdxs  | objectIdxs  | inverses             | result
        [0, 1]       | [0, 1]      | [false, false]       | true
        [0, 1, 2]    | [0, 1, 2]   | [true, true, false]  | true
    }

    boolean apply(List<Integer> subjectIdxs, List<Integer> objectIdxs, List<Boolean> inverses) {
        List<MiniquestCondition> conditions = new ArrayList<>(subjectIdxs.size())
        int idCounter = 0
        for (int i = 0; i < subjectIdxs.size(); i++) {
            def condition = new MiniquestCondition(
                    idCounter,
                    subjectIdxs.get(i),
                    objectIdxs.get(i),
                    inverses.get(i))
            idCounter++

            def result = sentenceValidator.validNewCondition(condition, conditions)
            if (!result) {
                return false
            }
            conditions.add(condition)
        }

        return true
    }
}
