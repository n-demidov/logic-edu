package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._1_to_n_subjectsToObjects._1_statementInSentense

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor
import spock.lang.Specification

class SeveralTrueStatementsValidatorTest extends Specification {
    private static final int ONLY_ONE_ANSWER = 1

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard, 2 right statements"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, ONLY_ONE_ANSWER, 2, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs             | inverse                                            | rangeIdxs        | rightVarIdx  | result
        [0, 1, 1]              | [true, true, true]                                 | [0, 1]           | 0            | true
        [0, 1, 1]              | [true, true, true]                                 | [0, 1]           | 1            | false

        [0, 0, 1]              | [false, false, false]                              | [0, 1]           | 0            | true
        [0, 0, 1]              | [false, false, false]                              | [0, 1]           | 1            | false

        [0, 0, 1, 1]           | [true, false, true, false]                         | [0, 1]           | 1            | false
        [0, 0, 1, 1]           | [true, false, true, false]                         | [0, 1]           | 0            | false

        [0, 0, 1, 1, 1]        | [false, false, false, false, false]                | [0, 1]           | 0            | true
        [0, 0, 1, 1, 1]        | [false, false, false, false, false]                | [0, 1]           | 1            | false

        [0, 0, 1, 1, 1, 2]     | [false, false, false, false, false, false]         | [0, 1, 2]        | 0            | true
        [0, 0, 1, 1, 1, 2]     | [false, false, false, false, false, false]         | [0, 1, 2]        | 1            | false
        [0, 0, 1, 1, 1, 2]     | [false, false, false, false, false, false]         | [0, 1, 2]        | 2            | false

        [0, 0, 1, 2]           | [false, false, false, false]                       | [0, 1, 2]        | 0            | true
        [0, 0, 1, 2]           | [false, false, false, false]                       | [0, 1, 2]        | 1            | false
        [0, 0, 1, 2]           | [false, false, false, false]                       | [0, 1, 2]        | 2            | false

        [0, 1, 0, 1, 2]        | [false, false, true, false, true]                  | [0, 1, 2]        | 0            | true
        [0, 1, 0, 1, 2]        | [false, false, true, false, true]                  | [0, 1, 2]        | 1            | false
        [0, 1, 0, 1, 2]        | [false, false, true, false, true]                  | [0, 1, 2]        | 2            | false

        [1, 0, 2, 1, 0, 3, 1]  | [false, false, false, false, false, false, false]  | [0, 1, 2, 3]     | 0            | true
        [1, 0, 2, 1, 0, 3, 1]  | [false, false, false, false, false, false, false]  | [0, 1, 2, 3]     | 1            | false
        [1, 0, 2, 1, 0, 3, 1]  | [false, false, false, false, false, false, false]  | [0, 1, 2, 3]     | 2            | false
        [1, 0, 2, 1, 0, 3, 1]  | [false, false, false, false, false, false, false]  | [0, 1, 2, 3]     | 3            | false

        [2, 1, 0, 4, 1, 3, 0]  | [false, false, true, true, false, false, true]     | [0, 1, 2, 3, 4]  | 4            | true
        [2, 1, 0, 4, 1, 3, 0]  | [false, false, true, true, false, false, true]     | [0, 1, 2, 3, 4]  | 0            | false
        [2, 1, 0, 4, 1, 3, 0]  | [false, false, true, true, false, false, true]     | [0, 1, 2, 3, 4]  | 1            | false
        [2, 1, 0, 4, 1, 3, 0]  | [false, false, true, true, false, false, true]     | [0, 1, 2, 3, 4]  | 2            | false
        [2, 1, 0, 4, 1, 3, 0]  | [false, false, true, true, false, false, true]     | [0, 1, 2, 3, 4]  | 3            | false

        // Out of range:
        [0, 0]                 | [true, true]                                       | [0, 1]           | 1            | true
        [0, 0]                 | [true, true]                                       | [0, 1]           | 0            | false
    }

    def "should invalid when standard, 2 right statements"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, ONLY_ONE_ANSWER, 2, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs             | inverse                                         | rangeIdxs     | rightVarIdx  | result
        // Cause: 2 answers
        [0, 0, 1, 1]           | [false, false, false, false]                    | [0, 1]        | 0            | false
        [0, 0, 1, 1]           | [false, false, false, false]                    | [0, 1]        | 1            | false

        [0, 1, 0, 1]           | [true, true, false, true]                       | [0, 1]        | 0            | false
        [0, 1, 0, 1]           | [true, true, false, true]                       | [0, 1]        | 1            | false

        // Cause: 2 answers: "0" and "3"
        [2, 1, 0, 3, 1, 0, 0]  | [false, false, true, true, false, true, false]  | [0, 1, 2, 3]  | 0            | false
        [2, 1, 0, 3, 1, 0, 0]  | [false, false, true, true, false, true, false]  | [0, 1, 2, 3]  | 1            | false
        [2, 1, 0, 3, 1, 0, 0]  | [false, false, true, true, false, true, false]  | [0, 1, 2, 3]  | 2            | false
        [2, 1, 0, 3, 1, 0, 0]  | [false, false, true, true, false, true, false]  | [0, 1, 2, 3]  | 3            | false

        // Cause: no any answer
        [0, 0, 0]              | [false, true, true]                             | [0, 1, 2, 3]  | 0            | false
        [0, 0, 0]              | [false, true, true]                             | [0, 1, 2, 3]  | 1            | false
        [0, 0, 0]              | [false, true, true]                             | [0, 1, 2, 3]  | 2            | false
        [0, 0, 0]              | [false, true, true]                             | [0, 1, 2, 3]  | 3            | false

        [0, 0, 0]              | [true, true, true]                              | [0, 1]        | 0            | false
        [0, 0, 0]              | [true, true, true]                              | [0, 1]        | 1            | false

        [0, 0, 0]              | [false, false, false]                           | [0, 1]        | 0            | false
        [0, 0, 0]              | [false, false, false]                           | [0, 1]        | 1            | false

        [0, 1, 0, 1]           | [false, true, false, false]                     | [0, 1, 2, 3]  | 0            | false
        [0, 1, 0, 1]           | [false, true, false, false]                     | [0, 1, 2, 3]  | 1            | false
        [0, 1, 0, 1]           | [false, true, false, false]                     | [0, 1, 2, 3]  | 2            | false
        [0, 1, 0, 1]           | [false, true, false, false]                     | [0, 1, 2, 3]  | 3            | false

        // Cause: 3 answers, not 1
        [0, 1, 2]              | [false, true, true]                             | [0, 1, 2]     | 0            | false
        [0, 1, 2]              | [false, true, true]                             | [0, 1, 2]     | 1            | false
        [0, 1, 2]              | [false, true, true]                             | [0, 1, 2]     | 2            | false

        // Cause: out of range 2 elements (not 1)
        [0, 1]                 | [true, true]                                    | [0, 1, 2, 3]  | 0            | false
        [0, 1]                 | [true, true]                                    | [0, 1, 2, 3]  | 1            | false
        [0, 1]                 | [true, true]                                    | [0, 1, 2, 3]  | 2            | false
        [0, 1]                 | [true, true]                                    | [0, 1, 2, 3]  | 3            | false
    }

    def "should valid when standard, 3 right statements"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, ONLY_ONE_ANSWER, 3, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs          | inverse                                    | rangeIdxs     | rightVarIdx  | result
        [0, 1, 0, 1]        | [false, true, false, false]                | [0, 1, 2, 3]  | 0            | true
        [0, 1, 0, 1]        | [false, true, false, false]                | [0, 1, 2, 3]  | 1            | false
        [0, 1, 0, 1]        | [false, true, false, false]                | [0, 1, 2, 3]  | 2            | false
        [0, 1, 0, 1]        | [false, true, false, false]                | [0, 1, 2, 3]  | 3            | false

        [0, 1, 0, 1]        | [true, false, true, true]                  | [0, 1]        | 1            | true
        [0, 1, 0, 1]        | [true, false, true, true]                  | [0, 1]        | 0            | false

        // Out of range:
        [0, 0, 0]           | [true, true, true]                         | [0, 1]        | 1            | true
        [0, 0, 0]           | [true, true, true]                         | [0, 1]        | 0            | false

        // Out of range:
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2]     | 2            | true
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2]     | 0            | false
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2]     | 1            | false
    }

    def "should invalid when standard, 3 right statements"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, ONLY_ONE_ANSWER, 3, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs          | inverse                                    | rangeIdxs     | rightVarIdx  | result
        // Cause: contradictions
        [0, 0, 0]           | [false, true, true]                        | [0, 1, 2, 3]  | 0            | false
        [0, 0, 0]           | [false, true, true]                        | [0, 1, 2, 3]  | 1            | false
        [0, 0, 0]           | [false, true, true]                        | [0, 1, 2, 3]  | 2            | false
        [0, 0, 0]           | [false, true, true]                        | [0, 1, 2, 3]  | 3            | false

        // Cause: more then 1 answers.
        [0, 1, 0, 1]        | [true, true, true, false]                  | [0, 1, 2]     | 0            | false
        [0, 1, 0, 1]        | [true, true, true, false]                  | [0, 1, 2]     | 1            | false
        [0, 1, 0, 1]        | [true, true, true, false]                  | [0, 1, 2]     | 2            | false

        // Cause: all statements are wrong
        [0, 0, 1, 1]        | [true, true, true, true]                   | [0, 1, 2]     | 0            | false
        [0, 0, 1, 1]        | [true, true, true, true]                   | [0, 1, 2]     | 1            | false
        [0, 0, 1, 1]        | [true, true, true, true]                   | [0, 1, 2]     | 2            | false

        // Cause: out of range 2 elements (not 1)
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2, 3]  | 0            | false
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2, 3]  | 1            | false
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2, 3]  | 2            | false
        [0, 0, 1]           | [true, true, true]                         | [0, 1, 2, 3]  | 3            | false
    }

    def "should valid when standard, 4 right statements"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, ONLY_ONE_ANSWER, 4, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs          | inverse                                 | rangeIdxs     | rightVarIdx  | result
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 3            | true
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 0            | false
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 1            | false
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 2            | false

        // Out of range
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 3            | true
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 0            | false
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 1            | false
        [0, 0, 1, 2]        | [true, true, true, true]                | [0, 1, 2, 3]  | 2            | false
    }

    def "should invalid when standard, 4 right statements"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, ONLY_ONE_ANSWER, 4, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs          | inverse                                 | rangeIdxs     | rightVarIdx  | result
        [0, 0, 1, 1, 2]     | [false, false, false, false, false]     | [0, 1, 2]     | 0            | false
        [0, 0, 1, 1, 2]     | [false, false, false, false, false]     | [0, 1, 2]     | 1            | false
        [0, 0, 1, 1, 2]     | [false, false, false, false, false]     | [0, 1, 2]     | 2            | false
    }

}
