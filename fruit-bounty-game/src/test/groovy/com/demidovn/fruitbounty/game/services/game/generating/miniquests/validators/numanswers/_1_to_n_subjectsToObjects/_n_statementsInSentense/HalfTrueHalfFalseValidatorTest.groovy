package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._1_to_n_subjectsToObjects._n_statementsInSentense

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class HalfTrueHalfFalseValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard"() {
        setup:
        def subjectsRangeIdxs = [0]
        List<List<Integer>> subjectsIdxs = generateSubjectsIdxs(objectsIdxs.asList())

        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                0, rightVarIdx, 1, 0, MAX_INT,
                ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        objectsIdxs       | inverses          | truePhrases       | objectsRangeIdxs   | rightVarIdx  | result
        /* 1*3 (a1)
           a!2 a!1 TF
           a!3 a!1 TF
           = 1 right answer */
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 0            | true
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 1            | false
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 2            | false
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2, 3]       | 0            | true
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2, 3, 4]    | 0            | true

        /* 1*3 (a2)
           a!1 a!2 TF
           a!3 a1  TF
           = 1 right answer */
        [[0, 1], [2, 0]]  | [[T, T], [T, F]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 0            | false
        [[0, 1], [2, 0]]  | [[T, T], [T, F]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 1            | true
        [[0, 1], [2, 0]]  | [[T, T], [T, F]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 2            | false

        /* 1*3 (a1)
           a!2 a!1 TF
           a!3 a!1 TF
           = 1 right answer */
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 0            | true
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2, 3]       | 0            | true
        [[1, 0], [2, 0]]  | [[T, T], [T, T]]  | [[T, F], [T, F]]  | [0, 1, 2, 3, 4]    | 0            | true
    }

    def "should valid when out of range"() {
        setup:
        def subjectsRangeIdxs = [0]
        List<List<Integer>> subjectsIdxs = generateSubjectsIdxs(objectsIdxs.asList())

        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                0, rightVarIdx, 3, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        objectsIdxs       | inverses          | truePhrases       | objectsRangeIdxs   | rightVarIdx  | result
        /* 1*4
           a1 a!1 TF
           a1 a!1 TF
           out of range */
        [[0, 0], [0, 0]]  | [[F, T], [F, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 0            | true
        [[0, 0], [0, 0]]  | [[F, T], [F, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 1            | true
        [[0, 0], [0, 0]]  | [[F, T], [F, T]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 2            | true

        [[0, 0], [0, 0]]  | [[F, T], [F, T]]  | [[T, F], [T, F]]  | [0, 1, 2, 3]       | 0            | false
    }

    def "should valid when standard, negatives"() {
        setup:
        def subjectsRangeIdxs = [0]
        List<List<Integer>> subjectsIdxs = generateSubjectsIdxs(objectsIdxs.asList())

        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                0, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        objectsIdxs       | inverses          | truePhrases       | objectsRangeIdxs   | rightVarIdx  | result
        /* 1*3
           a1 a1 TF
           a1 a1 TF
           negative */
        [[0, 0], [0, 0]]  | [[F, F], [F, F]]  | [[T, F], [T, F]]  | [0, 1, 2]          | 0            | false

        /* 1*3: a-[1-4]
           a!1 a!2 TT
           negative: out of range */
        [[0, 1]]          | [[T, T]]          | [[T, F]]          | [0, 1, 2, 3]       | 0            | false
    }

    private ArrayList<List<Integer>> generateSubjectsIdxs(List<List<Integer>> objectsIdxs) {
        ArrayList<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < objectsIdxs.size(); i++) {
            List<Integer> it2 = objectsIdxs.get(i)
            List<Integer> subResult = new ArrayList<>()
            for (int jj = 0; jj < it2.size(); jj++) {
                subResult.add(0)
            }
            result.add(subResult)
        }
        result
    }

}
