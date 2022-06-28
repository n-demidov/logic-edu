package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition
import spock.lang.Specification

class NoContradictionsValidatorTest extends Specification {
    private static final boolean T = true
    private static final boolean F = false

    NoContradictionsValidator validator = new NoContradictionsValidator()

    def "should valid correctly"() {
        expect:
        apply(null, objectIdxs, inverse, contrad) == result

        where:
        objectIdxs        | inverse                             | contrad | result
        [[0, 0]]          | [[false, false]]                    | true    | true
        [[0, 0, 1, 2]]    | [[false, false, false, false]]      | true    | true
        [[0, 0, 1, 2]]    | [[true, true, true, true]]          | true    | true
        [[0, 0, 1, 2]]    | [[true, false, true, true]]         | true    | true

        [[0, 0, 1, 2]]    | [[false, false, false, false]]      | false   | true
        [[0, 0, 1, 2]]    | [[true, true, true, true]]          | false   | true
        [[0, 0, 1, 2, 2]] | [[true, true, false, false, false]] | false   | true
        [[0, 0, 0, 2, 2]] | [[true, true, true, false, false]]  | false   | true

        [[0, 0, 1, 2]]    | [[true, false, true, true]]         | false   | false
        [[0, 0, 1, 2]]    | [[false, true, false, false]]       | false   | false
        [[0, 0, 1, 2, 2]] | [[true, true, false, false, true]]  | false   | false
        [[0, 0, 0, 2, 2]] | [[true, true, false, false, false]] | false   | false
    }

    def "should valid correctly, nStatementsInSentence"() {
        expect:
        apply(subjectIdxs, objectIdxs, inverse, contrad) == result

        where:
        subjectIdxs      | objectIdxs       | inverse          | contrad | result
        /* don't valid:
           a1  b!2
           a!1 c3 */
        [[0, 1], [0, 2]] | [[1, 2], [1, 3]] | [[F, T], [T, F]] | false   | false
        [[0, 1], [0, 2]] | [[1, 2], [1, 3]] | [[F, T], [T, F]] | true    | true

        /* don't valid:
           a1  b!2
           a!2 b2 */
        [[0, 1], [0, 1]] | [[1, 2], [2, 2]] | [[F, T], [T, F]] | false   | false
        [[0, 1], [0, 1]] | [[1, 2], [2, 2]] | [[F, T], [T, F]] | true    | true

        /* valid:
           a1 b!2
           a1 c3 */
        [[0, 1], [0, 2]] | [[1, 2], [1, 3]] | [[F, T], [F, F]] | false   | true
        [[0, 1], [0, 2]] | [[1, 2], [1, 3]] | [[F, T], [F, F]] | true    | true

        /* valid:
           a1  b!2
           a!2 c3 */
        [[0, 1], [0, 2]] | [[1, 2], [2, 3]] | [[F, T], [T, F]] | false   | true
        [[0, 1], [0, 2]] | [[1, 2], [2, 3]] | [[F, T], [T, F]] | true    | true

        /* valid:
           a1 b!2
           a2 c3 */
        [[0, 1], [0, 2]] | [[1, 2], [2, 3]] | [[F, T], [F, F]] | false   | true
        [[0, 1], [0, 2]] | [[1, 2], [2, 3]] | [[F, T], [F, F]] | true    | true
    }

    boolean apply(List<List<Integer>> subjectIdxs, List<List<Integer>> objectIdxs, List<List<Boolean>> inverse, boolean contradictionsEnabled) {
        int counterIdx = 0
        List<List<MiniquestCondition>> conditions = new ArrayList<>()
        for (int i = 0; i < objectIdxs.size(); i++) {
            List<Integer> sentence = objectIdxs.get(i)
            List<MiniquestCondition> resultRow = new ArrayList<>()

            for (int j = 0; j < sentence.size(); j++) {
                int objectId = sentence.get(j)
                int subjectId = subjectIdxs == null ? 0 : subjectIdxs.get(i).get(j)
                resultRow.add(new MiniquestCondition(counterIdx, subjectId, objectId, inverse.get(i).get(j)))
                counterIdx++
            }
            conditions.add(resultRow)
        }

        return validator.valid(conditions, contradictionsEnabled)
    }

}
