package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest

class GlobalAnswersValidatorTest extends BaseValidatorTest {
    private GlobalAnswersValidator validator = new GlobalAnswersValidator()

    def "should invalid correctly when empty globalRightAnswers"() {
        setup:
        MiniquestCondition statement = new MiniquestCondition(0, subject, object, inversed)

        expect:
        apply(statement, truth, Collections.emptyMap()) == result

        where:
        subject | object | inversed | truth | result
        0       | 0      | F        | T     | true
        1       | 3      | F        | T     | true
    }

    def "should invalid correctly"() {
        setup:
        MiniquestCondition statement = new MiniquestCondition(0, subject, object, inversed)

        Map<Integer, Integer> globalRightAnswers = new HashMap<>()
        for (int row = 0; row < rAnswers.size(); row++) {
            List<Integer> columns = rAnswers[row]
            globalRightAnswers.put(columns.get(0), columns.get(1))
        }

        expect:
        apply(statement, truth, globalRightAnswers) == result

        where:
        subject | object | inversed | truth | rAnswers | result
        // No such subjects
        0       | 0      | F        | T     | [[1, 1]] | true
        1       | 0      | F        | T     | [[0, 1]] | true

        0       | 0      | T        | T     | [[1, 1]] | true
        1       | 0      | T        | T     | [[0, 1]] | true
        0       | 0      | T        | F     | [[1, 1]] | true
        1       | 0      | T        | F     | [[0, 1]] | true
        0       | 0      | F        | F     | [[1, 1]] | true
        1       | 0      | F        | F     | [[0, 1]] | true

        // if verity=T and inversed=F -- values should be equals
        1       | 1      | F        | T     | [[1, 1]] | true
        1       | 1      | F        | T     | [[1, 0]] | false
        1       | 1      | F        | T     | [[1, 2]] | false

        // if verity=T and inversed=T -- values should be different
        1       | 1      | T        | T     | [[1, 0]] | true
        1       | 1      | T        | T     | [[1, 2]] | true
        1       | 1      | T        | T     | [[1, 3]] | true
        1       | 1      | T        | T     | [[1, 1]] | false

        // if verity=F and inversed=F -- values should be different
        1       | 1      | F        | F     | [[1, 0]] | true
        1       | 1      | F        | F     | [[1, 2]] | true
        1       | 1      | F        | F     | [[1, 3]] | true
        1       | 1      | F        | F     | [[1, 1]] | false


        // if verity=F and inversed=T -- values should be equals
        1       | 1      | T        | F     | [[1, 1]] | true
        1       | 1      | T        | F     | [[1, 0]] | false
        1       | 1      | T        | F     | [[1, 2]] | false
    }

    boolean apply(MiniquestCondition checkingStatement, boolean truth, Map<Integer, Integer> globalRightAnswers) {
        return validator.validNewCondition(checkingStatement, truth, globalRightAnswers)
    }

}
