package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class CheckRightAnswerIdxValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                targetObjectIdx, rightVarIdx, 1, 0, MAX_INT, ALL1, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | rangeIdxs | targetObjectIdx | rightVarIdx | result
        /* 3*3
           a1 b!2 TT
           a1 c!2 TT
           negative: contradictions in non-target: b3 to c3 */
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 0               | 0           | false
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 0               | 1           | false
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 0               | 2           | false

        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 1               | 0           | false
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 1               | 1           | false
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 1               | 2           | false

        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 2               | 0           | false
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 2               | 1           | false
        [[0, 1], [0, 2]]         | [[0, 1], [0, 1]]         | [[F, T], [F, T]]         | [[T, T], [T, T]]         | [0, 1, 2] | 2               | 2           | false

        /* 3*3 (a2 b1 c3)
           b1  a1 TF
           b!2 a1 TF
           c!2 a1 TF
           = 1 answer */
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 0               | 0           | false
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 0               | 1           | true
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 0               | 2           | false

        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 1               | 0           | true
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 1               | 1           | false
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 1               | 2           | false

        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 2               | 0           | false
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 2               | 1           | false
        [[1, 0], [1, 0], [2, 0]] | [[0, 0], [1, 0], [1, 0]] | [[F, F], [T, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2] | 2               | 2           | true
    }

}
