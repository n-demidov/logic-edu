package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._1_to_n_subjectsToObjects._1_statementInSentense

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor
import spock.lang.Specification

class SeveralAnswersValidatorTest extends Specification {
    private static final int TRUE_STATEMENTS_NUM = 1

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard, 2 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, TRUE_STATEMENTS_NUM, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                              | rangeIdxs  | rightVarIdx  | rightAnswersNum  | result
        // right 1 var
        [0, 1, 2]        | [false, true, true]                  | [0, 1, 2]  | 1            | 2                | true
        [0, 1, 2]        | [false, true, true]                  | [0, 1, 2]  | 2            | 2                | true
        [0, 1, 2]        | [false, true, true]                  | [0, 1, 2]  | 0            | 2                | false

        // right 2 var (it's both standard and out of range)
        [0, 0, 1]        | [false, true, false]                 | [0, 1, 2]  | 0            | 2                | true
        [0, 0, 1]        | [false, true, false]                 | [0, 1, 2]  | 2            | 2                | true
        [0, 0, 1]        | [false, true, false]                 | [0, 1, 2]  | 1            | 2                | false

        // right 3 var
        [0, 1, 1, 2]     | [true, false, false, true]           | [0, 1, 2]  | 0            | 2                | true
        [0, 1, 1, 2]     | [true, false, false, true]           | [0, 1, 2]  | 2            | 2                | true
        [0, 1, 1, 2]     | [true, false, false, true]           | [0, 1, 2]  | 1            | 2                | false

        // Cause: 2 answers if one true - is invalid
        [0, 1, 1]        | [true, true, false]                  | [0, 1, 2]  | 0            | 2                | false
        [0, 1, 1]        | [true, true, false]                  | [0, 1, 2]  | 1            | 2                | false
        [0, 1, 1]        | [true, true, false]                  | [0, 1, 2]  | 2            | 2                | false

        // Cause: 2 answers if one true - is invalid
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 0            | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 1            | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 2            | 2                | false

        [0, 1, 1]        | [false, false, false]                | [0, 1, 2]  | 0            | 2                | false
        [0, 1, 0]        | [true, true, true]                   | [0, 1, 2]  | 0            | 2                | false
        [0, 1, 0, 2, 2]  | [true, true, true, false, false]     | [0, 1, 2]  | 0            | 2                | false
        [0, 0, 1, 2]     | [false, true, false, false]          | [0, 1, 2]  | 0            | 2                | false
        [0, 1, 2, 1, 2]  | [false, false, false, false, false]  | [0, 1, 2]  | 0            | 2                | false
        [0, 1, 2, 1, 2]  | [true, false, false, true, false]    | [0, 1, 2]  | 2            | 2                | false
    }

    def "should valid when standard, 3 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, TRUE_STATEMENTS_NUM, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                              | rangeIdxs        | rightVarIdx  | rightAnswersNum  | result
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2]        | 0            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2]        | 1            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2]        | 2            | 3                | true

        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3]     | 0            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3]     | 1            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3]     | 2            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3]     | 3            | 3                | false

        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3, 4]  | 0            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3, 4]  | 1            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3, 4]  | 2            | 3                | true
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3, 4]  | 3            | 3                | false
        [0, 1, 2]        | [false, false, false]                | [0, 1, 2, 3, 4]  | 4            | 3                | false

        [0, 0, 1, 2, 3]  | [false, false, false, false, false]  | [0, 1, 2, 3]     | 1            | 3                | true
        [0, 0, 1, 2, 3]  | [false, false, false, false, false]  | [0, 1, 2, 3]     | 2            | 3                | true
        [0, 0, 1, 2, 3]  | [false, false, false, false, false]  | [0, 1, 2, 3]     | 3            | 3                | true
        [0, 0, 1, 2, 3]  | [false, false, false, false, false]  | [0, 1, 2, 3]     | 0            | 3                | false

        // Out of range
        [0]              | [true]                               | [0, 1, 2, 3]     | 1            | 3                | true
        [0]              | [true]                               | [0, 1, 2, 3]     | 2            | 3                | true
        [0]              | [true]                               | [0, 1, 2, 3]     | 3            | 3                | true
        [0]              | [true]                               | [0, 1, 2, 3]     | 0            | 3                | false

        [0]              | [true]                               | [0, 1, 2, 3, 4]  | 0            | 3                | false
        [0]              | [true]                               | [0, 1, 2, 3, 4]  | 1            | 3                | false
        [0]              | [true]                               | [0, 1, 2, 3, 4]  | 2            | 3                | false
        [0]              | [true]                               | [0, 1, 2, 3, 4]  | 3            | 3                | false
        [0]              | [true]                               | [0, 1, 2, 3, 4]  | 4            | 3                | false

        // Both: standard & out of range
        [0, 1, 1]        | [false, false, true]                 | [0, 1, 2, 3]     | 1            | 3                | true
        [0, 1, 1]        | [false, false, true]                 | [0, 1, 2, 3]     | 2            | 3                | true
        [0, 1, 1]        | [false, false, true]                 | [0, 1, 2, 3]     | 3            | 3                | true
        [0, 1, 1]        | [false, false, true]                 | [0, 1, 2, 3]     | 0            | 3                | false

        // Out of range
        [0, 1]           | [true, false]                        | [0, 1, 2, 3, 4]  | 2            | 3                | true
        [0, 1]           | [true, false]                        | [0, 1, 2, 3, 4]  | 3            | 3                | true
        [0, 1]           | [true, false]                        | [0, 1, 2, 3, 4]  | 4            | 3                | true
        [0, 1]           | [true, false]                        | [0, 1, 2, 3, 4]  | 0            | 3                | false
        [0, 1]           | [true, false]                        | [0, 1, 2, 3, 4]  | 1            | 3                | false
    }

}
