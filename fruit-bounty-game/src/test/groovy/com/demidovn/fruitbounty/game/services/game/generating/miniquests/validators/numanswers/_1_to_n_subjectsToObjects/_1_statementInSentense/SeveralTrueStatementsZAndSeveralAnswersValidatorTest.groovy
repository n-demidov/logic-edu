package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._1_to_n_subjectsToObjects._1_statementInSentense

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor;
import spock.lang.Specification;

class SeveralTrueStatementsZAndSeveralAnswersValidatorTest extends Specification {
    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard, 2 right statements and 2 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                              | rangeIdxs  | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1
        [0, 1, 1, 1]     | [true, false, true, true]            | [0, 1]     | 0            | 2                 | 2                | true
        [0, 1, 1, 1]     | [true, false, true, true]            | [0, 1]     | 1            | 2                 | 2                | true

        [0, 1, 1, 1]     | [true, false, true, true]            | [0, 1]     | 0            | 2                 | 1                | false
        [0, 1, 1, 1]     | [true, false, true, true]            | [0, 1]     | 1            | 2                 | 1                | false
        [0, 1, 1, 1]     | [true, false, true, true]            | [0, 1]     | 0            | 1                 | 2                | false
        [0, 1, 1, 1]     | [true, false, true, true]            | [0, 1]     | 1            | 1                 | 2                | false

        // Case #2
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 0            | 2                 | 2                | true
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 1            | 2                 | 2                | true
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 2            | 2                 | 2                | false

        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 0            | 2                 | 1                | false
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 1            | 2                 | 1                | false
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 2            | 2                 | 1                | false
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 0            | 1                 | 2                | false
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 1            | 1                 | 2                | false
        [0, 1, 1, 1, 2]  | [true, false, true, true, false]     | [0, 1, 2]  | 2            | 1                 | 2                | false

        // Case 3: 2 answers: 0 and 3
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 0        | 2      | 2      | true
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 3        | 2      | 2      | true
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 1        | 2      | 2      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 2        | 2      | 2      | false

        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 0        | 2      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 1        | 2      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 2        | 2      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 3        | 2      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 0        | 3      | 2      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 1        | 3      | 2      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 2        | 3      | 2      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 3        | 3      | 2      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 0        | 3      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 1        | 3      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 2        | 3      | 3      | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]  | 3        | 3      | 3      | false

        // Case #4
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]      | [0, 1, 2, 3]  | 0            | 2                 | 2                | true
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]      | [0, 1, 2, 3]  | 1            | 2                 | 2                | true
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]      | [0, 1, 2, 3]  | 2            | 2                 | 2                | false
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]      | [0, 1, 2, 3]  | 3            | 2                 | 2                | false

        // Case #4
        [0, 0, 1, 1, 2, 3]  | [false, false, false, false, false, false]  | [0, 1, 2, 3]  | 0            | 2                 | 2                | true
        [0, 0, 1, 1, 2, 3]  | [false, false, false, false, false, false]  | [0, 1, 2, 3]  | 1            | 2                 | 2                | true
        [0, 0, 1, 1, 2, 3]  | [false, false, false, false, false, false]  | [0, 1, 2, 3]  | 2            | 2                 | 2                | false
        [0, 0, 1, 1, 2, 3]  | [false, false, false, false, false, false]  | [0, 1, 2, 3]  | 3            | 2                 | 2                | false
    }

    def "should valid when standard, 3 right statements and 2 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                              | rangeIdxs  | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 1            | 3                 | 2                | true
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 2            | 3                 | 2                | true
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 0            | 3                 | 2                | false

        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 0            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 1            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 2            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 0            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 1            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 2            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 0            | 3                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 1            | 3                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2]  | 2            | 3                 | 3                | false
    }

    def "should valid when standard, 2 right statements and 3 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                          | rangeIdxs           | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1: out of range
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4, 5]  | 0            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4, 5]  | 1            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4, 5]  | 2            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4, 5]  | 3            | 2                 | 3                | true
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4, 5]  | 4            | 2                 | 3                | true
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4, 5]  | 5            | 2                 | 3                | true

        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4]     | 0            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4]     | 1            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4]     | 2            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4]     | 3            | 2                 | 3                | false
        [0, 0, 1, 2]     | [true, true, false, false]       | [0, 1, 2, 3, 4]     | 4            | 2                 | 3                | false

        // Case #2: negative
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]  | [0, 1, 2, 3]        | 0            | 2                 | 3                | false
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]  | [0, 1, 2, 3]        | 1            | 2                 | 3                | false
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]  | [0, 1, 2, 3]        | 2            | 2                 | 3                | false
        [0, 0, 1, 1, 2]  | [true, true, true, true, false]  | [0, 1, 2, 3]        | 3            | 2                 | 3                | false
    }

    def "should valid when standard, 3 right statements and 3 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                              | rangeIdxs     | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1: inner + out of range
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 3                 | 3                | true
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 3                 | 3                | true
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 3                 | 3                | true
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 3                 | 3                | false

        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 2                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 3                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 3                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 3                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 3                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 2                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 1                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 1                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 1                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 1                 | 2                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 2                 | 1                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 2                 | 1                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 2                 | 1                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 2                 | 1                | false

        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 1                 | 1                | true
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 1                 | 1                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 1                 | 1                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 1                 | 1                | false

        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 3                 | 4                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 3                 | 4                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 3                 | 4                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 3                 | 4                | false

        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 4                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 4                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 4                 | 3                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 4                 | 3                | false

        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 0            | 4                 | 4                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 1            | 4                 | 4                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 2            | 4                 | 4                | false
        [0, 0, 1, 1]     | [true, true, true, false]            | [0, 1, 2, 3]  | 3            | 4                 | 4                | false
    }

    def "should valid when standard, 4 right statements and 1 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs             | inverse                                         | rangeIdxs        | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1: negative
        [0, 1, 2, 3, 4]        | [true, true, true, true, false]                 | [0, 1, 2, 3, 4]  | 0            | 4                 | 1                | false
        [0, 1, 2, 3, 4]        | [true, true, true, true, false]                 | [0, 1, 2, 3, 4]  | 1            | 4                 | 1                | false
        [0, 1, 2, 3, 4]        | [true, true, true, true, false]                 | [0, 1, 2, 3, 4]  | 2            | 4                 | 1                | false
        [0, 1, 2, 3, 4]        | [true, true, true, true, false]                 | [0, 1, 2, 3, 4]  | 3            | 4                 | 1                | false
        [0, 1, 2, 3, 4]        | [true, true, true, true, false]                 | [0, 1, 2, 3, 4]  | 4            | 4                 | 1                | false

        // Case #2: inner
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 2            | 4                 | 1                | true
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 0            | 4                 | 1                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 1            | 4                 | 1                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 3            | 4                 | 1                | false

        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 0            | 4                 | 2                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 1            | 4                 | 2                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 2            | 4                 | 2                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 3            | 4                 | 2                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 0            | 3                 | 1                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 1            | 3                 | 1                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 2            | 3                 | 1                | false
        [0, 0, 0, 1, 1, 2, 3]  | [true, true, false, false, false, false, true]  | [0, 1, 2, 3]     | 3            | 3                 | 1                | false
    }

    def "should valid when standard, 4 right statements and 4 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs       | inverse                              | rangeIdxs              | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1: out of range
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 4            | 4                 | 3                | true
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 5            | 4                 | 3                | true
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 6            | 4                 | 3                | true
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 0            | 4                 | 3                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 1            | 4                 | 3                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 2            | 4                 | 3                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 3            | 4                 | 3                | false

        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 0            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 1            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 2            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 3            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 4            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 5            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 6            | 4                 | 2                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 0            | 4                 | 4                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 1            | 4                 | 4                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 2            | 4                 | 4                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 3            | 4                 | 4                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 4            | 4                 | 4                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 5            | 4                 | 4                | false
        [0, 1, 2, 3]     | [true, true, true, true]             | [0, 1, 2, 3, 4, 5, 6]  | 6            | 4                 | 4                | false

        // Case #2
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3, 4]        | 1            | 4                 | 4                | true
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3, 4]        | 2            | 4                 | 4                | true
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3, 4]        | 3            | 4                 | 4                | true
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3, 4]        | 4            | 4                 | 4                | true
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3, 4]        | 0            | 4                 | 4                | false

        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3]           | 0            | 4                 | 4                | false
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3]           | 1            | 4                 | 4                | false
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3]           | 2            | 4                 | 4                | false
        [0, 0, 0, 0]     | [true, true, true, true]             | [0, 1, 2, 3]           | 3            | 4                 | 4                | false
    }

    def "should valid when standard, 4 right statements and 5 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs          | inverse                           | rangeIdxs              | rightVarIdx  | trueStatementsNum | rightAnswersNum  | result
        // Case #1: inner
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 0            | 4                 | 5                | true
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 1            | 4                 | 5                | true
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 2            | 4                 | 5                | true
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 3            | 4                 | 5                | true
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 4            | 4                 | 5                | true
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 5            | 4                 | 5                | false

        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 0            | 3                 | 5                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 1            | 3                 | 5                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 2            | 3                 | 5                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 3            | 3                 | 5                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 4            | 3                 | 5                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 5            | 3                 | 5                | false

        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 0            | 4                 | 4                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 1            | 4                 | 4                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 2            | 4                 | 4                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 3            | 4                 | 4                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 4            | 4                 | 4                | false
        [0, 1, 2, 3, 4]     | [true, true, true, true, true]    | [0, 1, 2, 3, 4, 5]     | 5            | 4                 | 4                | false
    }

    def "should valid when standard, 5 right statements and 5 right answers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs             | inverse                                       | rangeIdxs                 | rightVarIdx  | trueStatementsNum  | rightAnswersNum  | result
        // Case #1: inner + out of range
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 5                  | 5                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 5                  | 5                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 5                  | 5                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 5                  | 5                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 7            | 5                  | 5                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 5                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 5                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 5                  | 5                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 5                  | 6                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 5                  | 6                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 5                  | 6                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 5                  | 6                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 5                  | 6                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 5                  | 6                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 5                  | 6                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 7            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 6                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 6                  | 5                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 4                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 4                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 4                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 4                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 4                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 4                  | 5                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 4                  | 5                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 5                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 5                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 5                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 5                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 5                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 5                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 5                  | 4                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 4                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 4                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 4                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 4                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 4                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 4                  | 4                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 4                  | 4                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 4                  | 1                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 4                  | 1                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 4                  | 1                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 4                  | 1                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 4                  | 1                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 4                  | 1                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 4                  | 1                | false

        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 2            | 4                  | 3                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 3            | 4                  | 3                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 4            | 4                  | 3                | true
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 0            | 4                  | 3                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 1            | 4                  | 3                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 5            | 4                  | 3                | false
        [0, 0, 1, 1, 2, 3, 4]  | [false, true, false, true, true, true, true]  | [0, 1, 2, 3, 4, 5, 6, 7]  | 6            | 4                  | 3                | false
    }

}
