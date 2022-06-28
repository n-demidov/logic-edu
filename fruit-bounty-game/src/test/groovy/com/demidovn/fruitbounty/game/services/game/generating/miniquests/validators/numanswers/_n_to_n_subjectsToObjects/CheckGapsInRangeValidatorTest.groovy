package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class CheckGapsInRangeValidatorTest extends BaseValidatorTest {

    def "should valid, 1 rightAnswer"() {
        setup:
        Map<Integer, Integer> globalRightAnswers = new HashMap<>()
        for (int row = 0; row < rAnswers.size(); row++) {
            List<Integer> columns = rAnswers[row]
            globalRightAnswers.put(columns.get(0), columns.get(1))
        }

        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, trgObj, 1, 0, MAX_INT, MEN1, ANY, globalRightAnswers).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | subjRange | objRange | trgSubj | trgObj | rAnswers         | result
        /* 2*2
          b!2 a!2 TT
          b!2 b!0 TT
          = no answer
         */
        [[1, 0], [1, 1]] | [[2, 2], [2, 0]] | [[T, T], [T, T]] | [[T, T], [T, T]] | [0, 1]    | [0, 2]   | 0       | 0      | [[0, 2], [1, 0]] | false

    }

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

}
