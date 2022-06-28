package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition
import spock.lang.Specification

class NoRepeatsValidatorTest extends Specification {
    private static final boolean T = true
    private static final boolean F = false

    NoRepeatsValidator noRepeatsValidator = new NoRepeatsValidator()

    def "should valid repeats correctly"() {
        expect:
        apply(null, objectIdxs, inverse, repeatsLimit) == result

        where:
        objectIdxs        | inverse                              | repeatsLimit | result
        [[0, 0]]          | [[false, false]]                     | 0            | false
        [[0, 1, 0]]       | [[true, true, true]]                 | 0            | false
        [[0, 0, 1, 1]]    | [[false, false, true, true]]         | 0            | false
        [[0, 1, 2, 1, 2]] | [[true, true, false, true, true]]    | 0            | false
        [[0, 1, 0, 1, 2]] | [[false, false, true, false, true]]  | 0            | false
        [[0, 1, 0, 2, 2]] | [[true, true, false, false, false]]  | 0            | false
        [[0, 1, 0, 2, 2]] | [[true, true, true, false, true]]    | 0            | false
        [[0, 1, 0, 2, 0]] | [[true, true, false, false, true]]   | 0            | false
        [[0, 1, 0, 2, 0]] | [[true, true, false, false, false]]  | 0            | false
        [[0, 1, 2]]       | [[false, false, false]]              | 0            | true
        [[0, 1, 2]]       | [[true, false, false]]               | 0            | true
        [[0, 1, 2]]       | [[true, true, true]]                 | 0            | true
        [[0, 0, 1, 1]]    | [[false, true, true, false]]         | 0            | true
        [[0, 0, 1, 2]]    | [[false, true, false, false]]        | 0            | true
        [[0, 1, 2, 1, 2]] | [[true, false, true, true, false]]   | 0            | true

        [[0, 0, 0]]       | [[false, false, false]]              | 0            | false
        [[0, 0, 0]]       | [[false, false, false]]              | 1            | false
        [[0, 0, 0]]       | [[false, false, false]]              | 2            | false
        [[0, 0, 0]]       | [[false, false, false]]              | 3            | true
        [[0, 0, 0]]       | [[false, false, false]]              | 4            | true
        [[0, 0, 0]]       | [[false, false, true]]               | 2            | true
        [[0, 0, 0, 1]]    | [[false, false, true, true]]         | 2            | true
        [[0, 0, 0, 1]]    | [[false, false, false, false]]       | 3            | true

        [[0, 0, 1, 1]]    | [[false, false, false, false]]       | 3            | false
        [[0, 0, 1, 1]]    | [[false, false, false, false]]       | 4            | true

        [[0, 0, 1, 1]]    | [[false, false, true, false]]        | 0            | false
        [[0, 0, 1, 1]]    | [[false, false, true, false]]        | 1            | false
        [[0, 0, 1, 1]]    | [[false, false, true, false]]        | 2            | true
        [[0, 0, 1, 1]]    | [[false, false, true, false]]        | 3            | true
        [[0, 0, 1, 1, 2]] | [[false, false, true, false, false]] | 3            | true

        [[0, 1, 1, 2, 0]] | [[false, false, false, false, true]] | 0            | false
        [[0, 1, 1, 2, 0]] | [[false, false, false, false, true]] | 1            | false
        [[0, 1, 1, 2, 0]] | [[false, false, false, false, true]] | 2            | true
        [[0, 1, 1, 2, 0]] | [[false, false, false, false, true]] | 3            | true
    }

    def "should valid repeats correctly, nStatementsInSentence"() {
        expect:
        apply(subjectIdxs, objectIdxs, inverse, repeatsLimit) == result

        where:
        subjectIdxs              | objectIdxs               | inverse                  | repeatsLimit | result
        /* valid:
           b1  a2
           b!1 a3 */
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[F, F], [T, F]]         | 0            | true

        /* 1 repeat
           b1  a2
           b!1 a2 */
        [[1, 0], [1, 0]]         | [[1, 2], [1, 2]]         | [[F, F], [T, F]]         | 0            | false
        [[1, 0], [1, 0]]         | [[1, 2], [1, 2]]         | [[F, F], [T, F]]         | 1            | false
        [[1, 0], [1, 0]]         | [[1, 2], [1, 2]]         | [[F, F], [T, F]]         | 2            | true
        [[1, 0], [1, 0]]         | [[1, 2], [1, 2]]         | [[F, F], [T, F]]         | 3            | true

        /* 1 repeat
           b1 a2
           b1 a3 */
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[F, F], [F, F]]         | 0            | false
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[F, F], [F, F]]         | 1            | false
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[F, F], [F, F]]         | 2            | true

        /* 1 repeat
           b!1 a2
           b!1 a3 */
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[T, F], [T, F]]         | 0            | false
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[T, F], [T, F]]         | 1            | false
        [[1, 0], [1, 0]]         | [[1, 2], [1, 3]]         | [[T, F], [T, F]]         | 2            | true

        /* 2 repeats
           b!1 a2
           a3 b!1
           c1 b!1 */
        [[1, 0], [0, 1], [2, 1]] | [[1, 2], [3, 1], [1, 1]] | [[T, F], [F, T], [F, T]] | 0            | false
        [[1, 0], [0, 1], [2, 1]] | [[1, 2], [3, 1], [1, 1]] | [[T, F], [F, T], [F, T]] | 1            | false
        [[1, 0], [0, 1], [2, 1]] | [[1, 2], [3, 1], [1, 1]] | [[T, F], [F, T], [F, T]] | 2            | false
        [[1, 0], [0, 1], [2, 1]] | [[1, 2], [3, 1], [1, 1]] | [[T, F], [F, T], [F, T]] | 3            | true
    }

    boolean apply(List<List<Integer>> subjectIdxs, List<List<Integer>> objectIdxs, List<List<Boolean>> inverse, int repeatsLimit) {
        List<List<MiniquestCondition>> conditions = new ArrayList<>()

        int counterIdx = 0
        for (int i = 0; i < objectIdxs.size(); i++) {
            List<Integer> sentence = objectIdxs.get(i)
            List<MiniquestCondition> row = new ArrayList<>()

            for (int j = 0; j < sentence.size(); j++) {
                int objectId = sentence.get(j)
                int subjectId = subjectIdxs == null ? 0 : subjectIdxs.get(i).get(j)
                row.add(new MiniquestCondition(counterIdx, subjectId, objectId, inverse.get(i).get(j)))
                counterIdx++
            }
            conditions.add(row)
        }

        return noRepeatsValidator.valid(conditions, repeatsLimit)
    }

}
