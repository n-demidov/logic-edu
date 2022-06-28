package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._1_to_n_subjectsToObjects._1_statementInSentense

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor
import spock.lang.Specification

class OneAnswerValidatorTest extends Specification {
    private static final int ONLY_ONE_ANSWER = 1
    private static final int TRUE_STATEMENTS_NUM = 1
    private static final OutOfStatements ANY = OutOfStatements.SKIP_VALIDATION
    private static final OutOfStatements IN = OutOfStatements.STRICTLY_IN_STATEMENTS
    private static final OutOfStatements OUT = OutOfStatements.STRICTLY_OUT_OF_STATEMENTS

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when rightVarIdx was specified"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, objectIdxs, rightVarIdx, ONLY_ONE_ANSWER, TRUE_STATEMENTS_NUM, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs      | inverse                           | rightVarIdx | result
        [0, 1, 0, 1, 2] | [false, false, true, false, true] | 2           | true
        [0, 1, 0, 1, 2] | [false, false, true, false, true] | 0           | false
        [0, 1, 0, 1, 2] | [false, false, true, false, true] | 1           | false
    }

    def "should valid when standard"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, objectIdxs, rightVarIdx, ONLY_ONE_ANSWER, TRUE_STATEMENTS_NUM, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs      | inverse                             | rightVarIdx | result
        [0, 0, 1, 1]    | [false, false, false, false]        | 0           | false  // Contradictions (противоречия)
        [0, 1, 2]       | [false, false, false]               | 1           | false  // Several correct answers (should be only 1)
        [0, 1, 2]       | [true, false, false]                | 2           | false  // No one correct answers + Several correct answers
        [0, 1]          | [false, true]                       | 0           | false  // Several correct answers in 1 condition
        [0, 0, 0, 1, 1] | [false, true, true, true, true]     | 1           | false  // Contradictions + more than 1 correct answers

        [0, 1, 1]       | [false, false, false]               | 0           | true
        [0, 1, 0]       | [true, true, true]                  | 0           | true
        [0, 1, 0, 2, 2] | [true, true, true, false, false]    | 0           | true
        [0, 0, 1, 2]    | [false, true, false, false]         | 0           | true
        [0, 1, 2, 1, 2] | [false, false, false, false, false] | 0           | true
        [0, 1, 2, 1, 2] | [true, false, false, true, false]   | 0           | true  // Cause only 1 answer is right
        [0, 1, 0, 1, 2] | [false, false, true, false, true]   | 2           | true
        [0, 1, 0, 2, 2] | [true, true, false, false, false]   | 1           | true
    }

    def "should valid when standard, part 2"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, TRUE_STATEMENTS_NUM, OutOfStatements.SKIP_VALIDATION).contains(!result)

        where:
        objectIdxs | inverse | rangeIdxs | rightVarIdx | rightAnswersNum | result
        /* 1*3
           a0 T */
        [0]        | [false] | [0, 1, 2] | 0           | 1               | true
        [0]        | [false] | [0, 1, 2] | 1           | 1               | false
        [0]        | [false] | [0, 1, 2] | 2           | 1               | false
    }

    def "should valid when different outOfStatements modes"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVar, ONLY_ONE_ANSWER, TRUE_STATEMENTS_NUM, outOfStMode).contains(!result)

        where:
        objectIdxs   | rangeIdxs    | inverse                     | rightVar | outOfStMode | result
        [0, 1, 1]    | [0, 1, 2]    | [true, false, false]        | 2        | ANY         | true
        [0, 1, 1]    | [0, 1, 2]    | [true, false, false]        | 0        | ANY         | false
        [0, 1, 1]    | [0, 1, 2]    | [true, false, false]        | 1        | ANY         | false
        [0, 1, 1]    | [0, 1, 2]    | [true, false, false]        | 2        | OUT         | true
        [0, 1, 1]    | [0, 1, 2]    | [true, false, false]        | 2        | IN          | false

        // Cause: 2 answers
        [0, 1, 1]    | [0, 1, 2]    | [false, false, true]        | 0        | ANY         | false
        [0, 1, 1]    | [0, 1, 2]    | [false, false, true]        | 1        | ANY         | false
        [0, 1, 1]    | [0, 1, 2]    | [false, false, true]        | 2        | ANY         | false

        [0, 0, 1, 0] | [0, 1, 2]    | [false, false, false, true] | 0        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2]    | [false, false, false, true] | 1        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2]    | [false, false, false, true] | 2        | ANY         | true
        [0, 0, 1, 0] | [0, 1, 2]    | [false, false, false, true] | 2        | OUT         | true
        [0, 0, 1, 0] | [0, 1, 2]    | [false, false, false, true] | 2        | IN          | false

        // Cause: 2 answers
        [0, 0, 1, 0] | [0, 1, 2, 3] | [false, false, false, true] | 0        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [false, false, false, true] | 1        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [false, false, false, true] | 2        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [false, false, false, true] | 3        | ANY         | false

        [0, 0, 1, 0] | [0, 1, 2]    | [true, true, false, false]  | 0        | ANY         | true
        [0, 0, 1, 0] | [0, 1, 2]    | [true, true, false, false]  | 1        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2]    | [true, true, false, false]  | 2        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2]    | [true, true, false, false]  | 0        | OUT         | false
        [0, 0, 1, 0] | [0, 1, 2]    | [true, true, false, false]  | 0        | IN          | true

        [0, 0, 1, 0] | [0, 1, 2, 3] | [true, true, false, false]  | 0        | ANY         | true
        [0, 0, 1, 0] | [0, 1, 2, 3] | [true, true, false, false]  | 1        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [true, true, false, false]  | 2        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [true, true, false, false]  | 3        | ANY         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [true, true, false, false]  | 0        | OUT         | false
        [0, 0, 1, 0] | [0, 1, 2, 3] | [true, true, false, false]  | 0        | IN          | true

        // Cause: 3 answers
        [0, 1, 1]    | [0, 1, 2, 3] | [false, false, true]        | 0        | ANY         | false
        [0, 1, 1]    | [0, 1, 2, 3] | [false, false, true]        | 1        | ANY         | false
        [0, 1, 1]    | [0, 1, 2, 3] | [false, false, true]        | 2        | ANY         | false
        [0, 1, 1]    | [0, 1, 2, 3] | [false, false, true]        | 3        | ANY         | false
    }

    def "should valid when different outOfStatements modes, nAnswers"() {
        expect:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVar, nAnswers, TRUE_STATEMENTS_NUM, outOfStMode).contains(!result)

        where:
        objectIdxs | rangeIdxs    | inverse              | rightVar | outOfStMode | nAnswers | result
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 0        | ANY         | 2        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 1        | ANY         | 2        | true
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 2        | ANY         | 2        | true
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 1        | OUT         | 2        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 1        | IN          | 2        | true
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 2        | OUT         | 2        | true
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 2        | IN          | 2        | false

        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 0        | ANY         | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 1        | ANY         | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 2        | ANY         | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 0        | OUT         | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 1        | OUT         | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 2        | OUT         | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 0        | IN          | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 1        | IN          | 3        | false
        [0, 1, 1]  | [0, 1, 2]    | [false, false, true] | 2        | IN          | 3        | false

        //
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 0        | ANY         | 3        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 1        | ANY         | 3        | true
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 2        | ANY         | 3        | true
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 3        | ANY         | 3        | true
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 0        | OUT         | 3        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 1        | OUT         | 3        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 2        | OUT         | 3        | true
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 3        | OUT         | 3        | true
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 0        | IN          | 3        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 1        | IN          | 3        | true
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 2        | IN          | 3        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 3        | IN          | 3        | false

        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 0        | ANY         | 2        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 1        | ANY         | 2        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 2        | ANY         | 2        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 3        | ANY         | 2        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 0        | ANY         | 4        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 1        | ANY         | 4        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 2        | ANY         | 4        | false
        [0, 1, 1]  | [0, 1, 2, 3] | [false, false, true] | 3        | ANY         | 4        | false
    }

}
