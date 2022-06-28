package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class CheckAnswerInfersValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard, simple"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, 1, minAnswers, maxAnswers, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | truePhrases | rangeIdxs | trgSubj | trgObj | minAnswers | maxAnswers | inferFromNegatives | result
        /* 3*3 (a0 b1 c2)
           a0 b1 TT */
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 0       | 0      | 0          | 0          | true               | true
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 0       | 1      | 0          | 0          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 0       | 2      | 0          | 0          | true               | false

        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 1       | 0      | 0          | 0          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 1       | 1      | 0          | 0          | true               | true
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 1       | 2      | 0          | 0          | true               | false

        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 0      | 0          | 0          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 1      | 0          | 0          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 2      | 1          | 1          | true               | true

        // Postive variation of minAnswers/maxAnswers
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 2      | 0          | 1          | true               | true
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 2      | 1          | MAX_INT    | true               | true

        // Negative variation of minAnswers/maxAnswers
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 0       | 0      | 1          | 2          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 0       | 0      | 1          | MAX_INT    | true               | false

        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 2      | 0          | 0          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 2      | 2          | 3          | true               | false
        [[0, 1]]     | [[0, 1]]    | [[F, F]] | [[T, T]]    | [0, 1, 2] | 2       | 2      | 2          | MAX_INT    | true               | false

    }

}
