package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class CheckInferDataByNegativesValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 1, minAnswers, maxAnswers, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs                               | objectsIdxs                                | inverses                                   | truePhrases                                | rangeIdxs    | rightVarIdx | minAnswers | maxAnswers | result
        /* 3*3 (a1, b2, c3)
           a!2 c1 TF
           a!3 c1 TF
           b!3 c1 TF
           = answers are only inferred from negative data (= a!2 c!1 a!3 b!3) */
        [[0, 2], [0, 2], [1, 2]]                   | [[1, 0], [2, 0], [2, 0]]                   | [[T, F], [T, F], [T, F]]                   | [[T, F], [T, F], [T, F]]                   | [0, 1, 2]    | 0           | 0          | MAX_INT    | true
        [[0, 2], [0, 2], [1, 2]]                   | [[1, 0], [2, 0], [2, 0]]                   | [[T, F], [T, F], [T, F]]                   | [[T, F], [T, F], [T, F]]                   | [0, 1, 2]    | 0           | 0          | 0          | false

        // impossible to determine range:
        [[0, 2], [0, 2], [1, 2]]                   | [[1, 0], [2, 0], [2, 0]]                   | [[T, F], [T, F], [T, F]]                   | [[T, F], [T, F], [T, F]]                   | [0, 1, 2, 3] | 0           | 0          | MAX_INT    | false

        /* 3*3 (a1, b2, c3)
           a!2 c1 TF
           a!3 c1 TF
           b2 c1  TF
           = answers are only inferred from negative data (= a!2 a!3 c!1 b2) */
        [[0, 2], [0, 2], [1, 2]]                   | [[1, 0], [2, 0], [1, 0]]                   | [[T, F], [T, F], [F, F]]                   | [[T, F], [T, F], [T, F]]                   | [0, 1, 2]    | 0           | 0          | MAX_INT    | true
        [[0, 2], [0, 2], [1, 2]]                   | [[1, 0], [2, 0], [1, 0]]                   | [[T, F], [T, F], [F, F]]                   | [[T, F], [T, F], [T, F]]                   | [0, 1, 2]    | 0           | 0          | 0          | false

        /* 3*3 (a1 b2 c3 d4)
           a!2 a!3 c1 c1
           a!4 c!4 c1 c1
           b!3 b!4 c1 c1
           = answers are only inferred from negative data + d4 is dropped out of answers (= a!2 a!3 a!4 b!3 b!4 c!1 c!4) */
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 0], [3, 3, 0, 0], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0           | 0          | MAX_INT    | true
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 0], [3, 3, 0, 0], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0           | 0          | 0          | false
    }

    def "should valid when standard, 2 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgObject, rightVarIdx, 2, minAnswers, maxAnswers, SKIP, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs                               | objectsIdxs                                | inverses                                   | truePhrases                                | rangeIdxs    | trgObject | rightVarIdx | minAnswers | maxAnswers | result
        /* 4*4
           a!2 a!3 c1 c2 TTFF/TTFF/TFFT
           a!4 c!4 c1 c4 TTFF/TFFT/TTFF
           b!3 b!4 c1 c1 TTFF/TTFF/TTFF
           = (a1 b2 c3 d4) OR (a1 b2 c4 d3) OR (a3 b1 c2 d4) */
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0         | 0           | 0          | MAX_INT    | true
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0         | 1           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0         | 2           | 0          | MAX_INT    | true
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0         | 3           | 0          | MAX_INT    | false

        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 1         | 0           | 0          | MAX_INT    | true
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 1         | 1           | 0          | MAX_INT    | true
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 1         | 2           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 1         | 3           | 0          | MAX_INT    | false

        // Negative: cause expected 2 rightAnswers, but found 2.
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 2         | 0           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 2         | 1           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 2         | 2           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 2         | 3           | 0          | MAX_INT    | false

        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 3         | 0           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 3         | 1           | 0          | MAX_INT    | false
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 3         | 2           | 0          | MAX_INT    | true
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 3         | 3           | 0          | MAX_INT    | true

        // It may be different if you make a calculation for each option separately... But now 'sharedMinAnswerInfers' = 0 (e.g. for a=3).
        [[0, 0, 2, 2], [0, 2, 2, 2], [1, 1, 2, 2]] | [[1, 2, 0, 1], [3, 3, 0, 3], [2, 3, 0, 0]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [[T, T, F, F], [T, T, F, F], [T, T, F, F]] | [0, 1, 2, 3] | 0         | 0           | 1          | MAX_INT    | false
    }

    def "should valid when infer by objects"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubjIdx, rightVarIdx, 1, minAnswers, maxAnswers, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses    | truePhrases | rangeIdxs | trgSubjIdx | rightVarIdx | minAnswers | maxAnswers | result
        /* 3*3 (a2 b1 c0)
           a!1 b!2 c0 TTT
           => a!1 b!0 b!2 c!1 => b1 */
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 0          | 0           | 0          | MAX_INT    | false
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 0          | 1           | 0          | MAX_INT    | false
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 0          | 2           | 0          | MAX_INT    | true

        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 1          | 0           | 0          | MAX_INT    | false
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 1          | 1           | 0          | MAX_INT    | true
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 1          | 2           | 0          | MAX_INT    | false

        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 2          | 0           | 0          | MAX_INT    | true
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 2          | 1           | 0          | MAX_INT    | false
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 2          | 2           | 0          | MAX_INT    | false

        // Disabled 'inferFromNegatives'
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 0          | 2           | 0          | 0          | false
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 1          | 1           | 0          | 0          | false
        [[0, 1, 2]]  | [[1, 2, 0]] | [[T, T, F]] | [[T, T, T]] | [0, 1, 2] | 2          | 0           | 1          | MAX_INT    | false
    }

    def "should valid when infer by objects, a little more difficult"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubjIdx, rightVarIdx, 1, minAnswers, maxAnswers, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | rangeIdxs    | trgSubjIdx | rightVarIdx | minAnswers | maxAnswers | result
        /* 3*3 (a3 b2 c1 d0)
           a!2 d0  TT
           b!1 b!3 TT
           a!1 a!1 TT
            0) d0
            1)
             => a!00 a!1 a!2
             => b!00 b!1 b!3
            2)
             => a3
             => b2
             => c1 */
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 0          | 0           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 0          | 1           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 0          | 2           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 0          | 3           | 2          | 2          | true

        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 1          | 0           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 1          | 1           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 1          | 2           | 2          | 2          | true
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 1          | 3           | 0          | MAX_INT    | false

        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 2          | 0           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 2          | 1           | 2          | 2          | true
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 2          | 2           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 2          | 3           | 0          | MAX_INT    | false

        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 3          | 0           | 0          | 0          | true
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 3          | 1           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 3          | 2           | 0          | MAX_INT    | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 3          | 3           | 0          | MAX_INT    | false

        // Disabled 'inferFromNegatives'
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 0          | 3           | 0          | 1          | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 0          | 3           | 3          | MAX_INT    | false

        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 1          | 2           | 0          | 1          | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 1          | 2           | 3          | MAX_INT    | false

        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 2          | 1           | 0          | 1          | false
        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 2          | 1           | 3          | MAX_INT    | false

        [[0, 3], [1, 1], [0, 0]] | [[2, 0], [1, 3], [1, 1]] | [[T, F], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3] | 3          | 0           | 1          | MAX_INT    | false
    }

    def "should inferDataFromNegatives"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                targetObjectIdx, rightVarIdx, 1, minAnswers, maxAnswers, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses    | truePhrases | rangeIdxs | targetObjectIdx | rightVarIdx | minAnswers | maxAnswers | result
        /* 3*3 (a0 b1 c2)
           a!1 a!2 b!2 */
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 0               | 0           | 0          | MAX_INT    | true
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 0               | 1           | 0          | MAX_INT    | false
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 0               | 2           | 0          | MAX_INT    | false
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 1               | 0           | 0          | MAX_INT    | false
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 1               | 1           | 0          | MAX_INT    | true
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 1               | 2           | 0          | MAX_INT    | false
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 2               | 0           | 0          | MAX_INT    | false
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 2               | 1           | 0          | MAX_INT    | false
        [[0, 0, 1]]  | [[1, 2, 2]] | [[T, T, T]] | [[T, T, T]] | [0, 1, 2] | 2               | 2           | 0          | MAX_INT    | true
    }

}
