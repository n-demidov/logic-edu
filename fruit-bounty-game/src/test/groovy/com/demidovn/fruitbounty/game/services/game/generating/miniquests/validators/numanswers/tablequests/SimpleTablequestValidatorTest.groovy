package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.tablequests

import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidParams
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class SimpleTablequestValidatorTest extends BaseValidatorTest {
    private static final int ONLY_ONE_ANSWER = 1

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, rangeIdxs, rangeIdxs, trgSubjIdx, rightVarIdx,
                new ValidParams(ONLY_ONE_ANSWER, SKIP, ANY, minAnswerInfers, maxAnswerInfers, Collections.emptyMap())).contains(!result)

        where:
        subjectsIdxs   | objectsIdxs    | inverses       | rangeIdxs | trgSubjIdx | rightVarIdx | minAnswerInfers | maxAnswerInfers | result
        /* 3*3
           a!0 b!1 b!2 c!1
           = a1 b0 c2 */
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 0          | 0           | 0               | 9               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 0          | 1           | 1               | 1               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 0          | 2           | 0               | 9               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 1          | 0           | 1               | 1               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 1          | 1           | 0               | 9               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 1          | 2           | 0               | 9               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 0           | 0               | 9               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 1           | 0               | 9               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 2               | 2               | true

        // Other variations of minAnswerInfers/maxAnswerInfers
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 0          | 1           | 0               | 1               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 1          | 0           | 1               | 2               | true

        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 2               | 3               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 1               | 2               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 1               | 3               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 0               | 4               | true
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 0               | 99              | true

        // Invalid variations of minAnswerInfers/maxAnswerInfers
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 0          | 1           | 0               | 0               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 1          | 0           | 2               | 2               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 0               | 0               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 1               | 1               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 3               | 3               | false
        [[0, 1, 1, 2]] | [[0, 1, 2, 1]] | [[T, T, T, T]] | [0, 1, 2] | 2          | 2           | 4               | 4               | false

        /* 3*3
           b0 a!2
           = a1 b0 c2 */
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 0           | 0               | 9               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 2               | 2               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 2           | 0               | 9               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 0           | 0               | 0               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 1           | 0               | 9               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 2           | 0               | 9               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 2          | 0           | 0               | 9               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 2          | 1           | 0               | 9               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 2          | 2           | 2               | 2               | true

        // Other variations of minAnswerInfers/maxAnswerInfers
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 1               | 2               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 2               | 3               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 0               | 9               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 0           | 0               | 1               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 0           | 0               | 9               | true
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 2          | 2           | 1               | 3               | true

        // Invalid variations of minAnswerInfers/maxAnswerInfers
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 0               | 0               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 1               | 1               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 0          | 1           | 3               | 3               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 0           | 1               | 1               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 1          | 0           | 1               | 99              | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 2          | 2           | 0               | 1               | false
        [[1, 0]]       | [[0, 2]]       | [[F, T]]       | [0, 1, 2] | 2          | 2           | 3               | 99              | false
    }

    def "should valid when last gap, special case"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, rangeIdxs, rangeIdxs, trgSubjIdx, rightVarIdx,
                new ValidParams(ONLY_ONE_ANSWER, SKIP, ANY, minAnswerInfers, maxAnswerInfers, Collections.emptyMap())).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | rangeIdxs | trgSubjIdx | rightVarIdx | minAnswerInfers | maxAnswerInfers | result
        /* 3*3
           a0 b1
           = a0 b1 c2 */
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 0          | 0           | 0               | 0               | true
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 0          | 0           | 1               | 99              | false

        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 1          | 1           | 0               | 0               | true
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 1          | 1           | 1               | 99              | false

        // Only "1" infer
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 2          | 2           | 0               | 0               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 2          | 2           | 1               | 1               | true
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 2          | 2           | 2               | 99              | false

        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 2          | 0           | 0               | 99              | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [0, 1, 2] | 2          | 1           | 0               | 99              | false
    }

    def "should valid when 4*4"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, rangeIdxs, rangeIdxs, trgSubjIdx, rightVarIdx,
                new ValidParams(ONLY_ONE_ANSWER, SKIP, ANY, minAnswerInfers, maxAnswerInfers, Collections.emptyMap())).contains(!result)

        where:
        subjectsIdxs   | objectsIdxs    | inverses       | rangeIdxs    | trgSubjIdx | rightVarIdx | minAnswerInfers | maxAnswerInfers | result
        /* 4*4 AnswerInfers = 3
           b3 c!0 d!1 c!1
           = a1 b3 c2 d0 */
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 0          | 1           | 2               | 2               | true
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 1          | 3           | 0               | 0               | true
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 2          | 2           | 2               | 2               | true
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 3          | 0           | 3               | 3               | true

        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 0          | 0           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 0          | 2           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 0          | 3           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 1          | 0           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 1          | 1           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 1          | 2           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 2          | 0           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 2          | 1           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 2          | 3           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 3          | 1           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 3          | 2           | 0               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 3          | 3           | 0               | 999             | false

        // Invalid variations of minAnswerInfers/maxAnswerInfers
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 0          | 1           | 0               | 1               | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 0          | 1           | 3               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 1          | 3           | 1               | 9               | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 2          | 2           | 0               | 1               | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 2          | 2           | 3               | 999             | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 3          | 0           | 0               | 2               | false
        [[1, 2, 3, 2]] | [[3, 0, 1, 1]] | [[F, T, T, T]] | [0, 1, 2, 3] | 3          | 0           | 4               | 999             | false
    }

    def "should valid when 6*6"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, rangeIdxs, rangeIdxs, trgSubj, rightVar,
                new ValidParams(ONLY_ONE_ANSWER, SKIP, ANY, minAInfers, maxAInfers, Collections.emptyMap())).contains(!result)

        where:
        subjectsIdxs         | objectsIdxs          | inverses             | rangeIdxs          | trgSubj | rightVar | minAInfers | maxAInfers | result
        /* 6*6 AnswerInfers = 3
           a5 b4 e1 c!2 c!3 d!3
           = a5 b4 c0 e1 f3 */
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 5        | 0          | 0          | true
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 4        | 0          | 0          | true
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 0        | 2          | 2          | true
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 3        | 2          | 2          | true
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 2        | 3          | 3          | true

        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 1        | 2          | 2          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 2        | 2          | 2          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 3        | 2          | 2          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 4        | 2          | 2          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 5        | 2          | 2          | false

        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 0        | 3          | 3          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 1        | 3          | 3          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 3        | 3          | 3          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 4        | 3          | 3          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 5        | 3          | 3          | false

        // Invalid variations of minAnswerInfers/maxAnswerInfers
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 5        | 1          | 999        | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 4        | 1          | 999        | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 0        | 0          | 1          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 0        | 3          | 999        | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 3        | 0          | 1          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 3        | 3          | 999        | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 2        | 0          | 2          | false
        [[0, 1, 4, 2, 2, 3]] | [[5, 4, 1, 2, 3, 3]] | [[F, F, F, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 2        | 4          | 999        | false
    }

    def "should valid when 6*6, #2"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, rangeIdxs, rangeIdxs, trgSubj, rightVar,
                new ValidParams(ONLY_ONE_ANSWER, SKIP, ANY, minAInfers, maxAInfers, Collections.emptyMap()), false).contains(!result)

        where:
        subjectsIdxs                        | objectsIdxs                         | inverses                            | rangeIdxs          | trgSubj | rightVar | minAInfers | maxAInfers | result
        /* 6*6 AnswerInfers = 4
           b!3 c!0 a!0 a!4 b!0 f1 e!0 c!4 b!2 b!4 a!2
           = a3 b5 c2 d0 e4 f1 */
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 0        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 1        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 2        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 3        | 3          | 3          | true
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 4        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 0       | 5        | 0          | 999        | false

        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 0        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 1        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 2        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 3        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 4        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 1       | 5        | 2          | 2          | true

        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 0        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 1        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 2        | 4          | 4          | true
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 3        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 4        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 2       | 5        | 0          | 999        | false

        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 0        | 2          | 2          | true
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 1        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 2        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 3        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 4        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 3       | 5        | 0          | 999        | false

        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 4       | 0        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 4       | 1        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 4       | 2        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 4       | 3        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 4       | 4        | 3          | 3          | true
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 4       | 5        | 0          | 999        | false

        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 0        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 1        | 0          | 0          | true
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 2        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 3        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 4        | 0          | 999        | false
        [[1, 2, 0, 0, 1, 5, 4, 2, 1, 1, 0]] | [[3, 0, 0, 4, 0, 1, 0, 4, 2, 4, 2]] | [[T, T, T, T, T, F, T, T, T, T, T]] | [0, 1, 2, 3, 4, 5] | 5       | 5        | 0          | 999        | false
    }

    def "should valid when 4*4, nNonTargetAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjects, objects, inverses, range, range, trgSubj, trgObj,
                new ValidParams(nAnswers, nonTrgAn, outOfSt, minInfers, maxInfers, Collections.emptyMap())).contains(!result)

        where:
        subjects          | objects           | inverses          | range        | trgSubj | trgObj | minInfers | maxInfers | nonTrgAn | outOfSt | nAnswers | result
        /* 4*4
           a!0 a!1 b!0 b!1 c!0
           = (a0 OR a1) (b0 OR b1) c1 d0 */
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 1         | 1         | SKIP     | OUT     | 1        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 2         | 2         | SKIP     | OUT     | 1        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 1         | 1         | SKIP     | ANY     | 1        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 2         | 2         | SKIP     | ANY     | 1        | true

        // Wrong answers
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 2      | 0         | 999       | SKIP     | ANY     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 3      | 0         | 999       | SKIP     | ANY     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 2      | 0         | 999       | SKIP     | ANY     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 3      | 0         | 999       | SKIP     | ANY     | 1        | false

        // minAnswerInfers, maxAnswerInfers
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 0         | 0         | SKIP     | OUT     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 2         | 999       | SKIP     | OUT     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 0         | 1         | SKIP     | OUT     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 3         | 999       | SKIP     | OUT     | 1        | false

        // NonTargetAnswers
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 1         | 1         | ALL1     | OUT     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 2         | 2         | ALL1     | OUT     | 1        | false

        // OutOfStatements
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 1         | 1         | SKIP     | IN      | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 2         | 2         | SKIP     | IN      | 1        | false

        // rightAnswersNum
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 3       | 0      | 0         | 999       | SKIP     | ANY     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 2       | 1      | 0         | 999       | SKIP     | ANY     | 2        | false

        // a, b
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 2      | 3         | 3         | SKIP     | OUT     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 3      | 3         | 3         | SKIP     | OUT     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 2      | 3         | 3         | SKIP     | OUT     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 3      | 3         | 3         | SKIP     | OUT     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 2      | 3         | 3         | SKIP     | ANY     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 3      | 3         | 3         | SKIP     | ANY     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 2      | 3         | 3         | SKIP     | ANY     | 2        | true
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 3      | 3         | 3         | SKIP     | ANY     | 2        | true

        // Wrong answers
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 0      | 3         | 3         | SKIP     | OUT     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 1      | 3         | 3         | SKIP     | OUT     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 0      | 3         | 3         | SKIP     | OUT     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 1      | 3         | 3         | SKIP     | OUT     | 2        | false

        // minAnswerInfers, maxAnswerInfers
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 2      | 0         | 2         | SKIP     | ANY     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 3      | 4         | 999       | SKIP     | ANY     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 2      | 4         | 999       | SKIP     | ANY     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 3      | 0         | 2         | SKIP     | ANY     | 2        | false

        // NonTargetAnswers
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 2      | 3         | 3         | ALL1     | OUT     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 3      | 3         | 3         | ALL1     | OUT     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 2      | 3         | 3         | ALL1     | OUT     | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 3      | 3         | 3         | ALL1     | OUT     | 2        | false

        // OutOfStatements
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 2      | 3         | 3         | SKIP     | IN      | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 3      | 3         | 3         | SKIP     | IN      | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 2      | 3         | 3         | SKIP     | IN      | 2        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 3      | 3         | 3         | SKIP     | IN      | 2        | false

        // rightAnswersNum
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 0      | 0         | 999       | SKIP     | ANY     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 0       | 1      | 0         | 999       | SKIP     | ANY     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 0      | 0         | 999       | SKIP     | ANY     | 1        | false
        [[0, 0, 1, 1, 2]] | [[0, 1, 0, 1, 0]] | [[T, T, T, T, T]] | [0, 1, 2, 3] | 1       | 1      | 0         | 999       | SKIP     | ANY     | 1        | false
    }

}
