package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class CheckSkipNonTargetSubjectsValidatorTest extends BaseValidatorTest {
    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                targetObjectIdx, rightVarIdx, 1, 0, MAX_INT, nonTargetSubjects, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs         | objectsIdxs          | inverses             | truePhrases          | rangeIdxs    | targetObjectIdx | rightVarIdx | nonTargetSubjects | result
        /* 3*3 skipNonTargetSubjects
           a!0 a!2 => a1
           (infer outOfRange) */
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 0           | SKIP              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 1           | SKIP              | true
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 2           | SKIP              | false

        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 0           | SKIP              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 1           | SKIP              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 2           | SKIP              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 2               | 0           | SKIP              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 2               | 1           | SKIP              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 2               | 2           | SKIP              | false

        // Disabled 'skipNonTargetSubjects'
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 0           | ALL1              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 1           | ALL1              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 2           | ALL1              | false

        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 0           | MEN1              | false
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 1           | MEN1              | true
        [[0, 0]]             | [[0, 2]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 2           | MEN1              | false

        /* 3*3; skipNonTargetSubjects;
           a1 F
           = (a!1 => a0 OR a2) (b1 OR b2 OR b3) (c1 OR c2 OR c3)
           outOfRange */
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 0               | 0           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 0               | 1           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 0               | 2           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 1               | 0           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 1               | 1           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 1               | 2           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 2               | 0           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 2               | 1           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[F]]                | [0, 1, 2]    | 2               | 2           | SKIP              | false

        /* 3*3
           a1 T
           = a1 (b0 OR b2) (c0 or c2)
           outOfRange */
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 0               | 0           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 0               | 1           | SKIP              | true
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 0               | 2           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 1               | 0           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 1               | 1           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 1               | 2           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 2               | 0           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 2               | 1           | SKIP              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 2               | 2           | SKIP              | false

        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 0               | 1           | ALL1              | false
        [[0]]                | [[1]]                | [[F]]                | [[T]]                | [0, 1, 2]    | 0               | 1           | MEN1              | true

        /* 3*3; skipNonTargetSubjects
           a!0 c!0 => b0
           = (a1 OR a2) b0 (c1 OR c2) */
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 0           | SKIP              | false
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 1           | SKIP              | false
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 0               | 2           | SKIP              | false

        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 0           | SKIP              | true
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 1           | SKIP              | false
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 2           | SKIP              | false

        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 2               | 0           | SKIP              | false
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 2               | 1           | SKIP              | false
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 2               | 2           | SKIP              | false

        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 0           | MEN1              | false
        [[0, 2]]             | [[0, 0]]             | [[T, T]]             | [[T, T]]             | [0, 1, 2]    | 1               | 0           | ALL1              | false

        /* 3*3 skipNonTargetSubjects
           a2 b0 c1
           => b0 */
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 0               | 0           | SKIP              | false
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 0               | 1           | SKIP              | false
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 0               | 2           | SKIP              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 1               | 0           | SKIP              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 1               | 1           | SKIP              | false
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 1               | 2           | SKIP              | false
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 2               | 0           | SKIP              | false
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 2               | 1           | SKIP              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 2               | 2           | SKIP              | false

        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 0               | 2           | MEN1              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 1               | 0           | MEN1              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 2               | 1           | MEN1              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 0               | 2           | ALL1              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 1               | 0           | ALL1              | true
        [[0, 1, 2]]          | [[2, 0, 1]]          | [[F, F, F]]          | [[T, T, T]]          | [0, 1, 2]    | 2               | 1           | ALL1              | true

        /* 4*4
           b!0 c!0 d!0 c!1 d!1
           1) a0 (by object infer)
           2) b1 (by object infer)
           = a0 b1 (c2 OR c3) (d2 OR d3) */
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 0               | 0           | SKIP              | true
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 0               | 1           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 0               | 2           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 0               | 3           | SKIP              | false

        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 1               | 0           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 1               | 1           | SKIP              | true
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 1               | 2           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 1               | 3           | SKIP              | false

        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 2               | 0           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 2               | 1           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 2               | 2           | SKIP              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 2               | 3           | SKIP              | false

        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 0               | 0           | MEN1              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 1               | 1           | MEN1              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 0               | 0           | ALL1              | false
        [[1, 2, 3, 2, 3]]    | [[0, 0, 0, 1, 1]]    | [[T, T, T, T, T]]    | [[T, T, T, T, T]]    | [0, 1, 2, 3] | 1               | 1           | ALL1              | false

        /* 4*4 (a0 b1 c2 d3)
           b!0 c!0 d!0 c!1 d!1 c!3
           1) a0 (by object infer)
           2) b1 (by object infer)
           3) c2 */
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 0               | 0           | SKIP              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 0               | 1           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 0               | 2           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 0               | 3           | SKIP              | false

        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 1               | 0           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 1               | 1           | SKIP              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 1               | 2           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 1               | 3           | SKIP              | false

        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 2               | 0           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 2               | 1           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 2               | 2           | SKIP              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 2               | 3           | SKIP              | false

        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 3               | 0           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 3               | 1           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 3               | 2           | SKIP              | false
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 3               | 3           | SKIP              | true

        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 0               | 0           | MEN1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 1               | 1           | MEN1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 2               | 2           | MEN1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 3               | 3           | MEN1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 0               | 0           | ALL1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 1               | 1           | ALL1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 2               | 2           | ALL1              | true
        [[1, 2, 3, 2, 3, 2]] | [[0, 0, 0, 1, 1, 3]] | [[T, T, T, T, T, T]] | [[T, T, T, T, T, T]] | [0, 1, 2, 3] | 3               | 3           | ALL1              | true
    }

    def "should valid, negatives"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                targetObjectIdx, rightVarIdx, 1, 0, MAX_INT, nonTargetSubjects, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | truePhrases | rangeIdxs | targetObjectIdx | rightVarIdx | nonTargetSubjects | result
        /* 3*3 skipNonTargetSubjects; rightAnswers=2
           a!2 => a0 OR a1
           outOfRange */
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | SKIP              | false

        /* 3*3; skipNonTargetSubjects; rightAnswers=3
           a1 a!1 TF/FT
           = a1 (b0 OR b2) (c0 or c2) OR (a!1 => a0 OR a2) (b0 OR b1 OR b2) (c0 OR c1 OR c2)
           standard + outOfRange */
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 0           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 2           | SKIP              | false
    }

    def "should valid, 2 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                targetObjectIdx, rightVarIdx, 2, 0, MAX_INT, nonTargetSubjects, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | truePhrases | rangeIdxs | targetObjectIdx | rightVarIdx | nonTargetSubjects | result
        /* 3*3 skipNonTargetSubjects; rightAnswers=2
           a!2 => a0 OR a1
           = (a0 OR a1) (b0 OR b1 OR b2) (c0 OR c1 OR c2)
           outOfRange */
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | SKIP              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | SKIP              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | SKIP              | false

        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 1               | 0           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 1               | 2           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 2               | 0           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 2               | 2           | SKIP              | false

        // Disabled 'skipNonTargetSubjects'
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | ALL1              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | ALL1              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | ALL1              | false

        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | MEN1              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | MEN1              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | MEN1              | false

        /* 3*3 skipNonTargetSubjects; rightAnswers=2
           b2 => a0 OR a1
           = (a0 OR a1) b2 (c0 OR c1 OR c2)
           outOfRange */
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 0           | SKIP              | true
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 1           | SKIP              | true
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 2           | SKIP              | false

        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 0           | MEN1              | true
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 1           | MEN1              | true
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 2           | MEN1              | false
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 0           | ALL1              | false
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 1           | ALL1              | false
        [[1]]        | [[2]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 2           | ALL1              | false

        //
        /* 3*3 skipNonTargetSubjects; rightAnswers=2
           a!2 => a0 OR a1
           = (a0 OR a1) (b0 OR b1 OR b2) (c0 OR c1 OR c2)
           outOfRange */
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | SKIP              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | SKIP              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | SKIP              | false

        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 1               | 0           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 1               | 2           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 2               | 0           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 2               | 2           | SKIP              | false

        // Disabled 'skipNonTargetSubjects'
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | ALL1              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | ALL1              | false
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | ALL1              | false

        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 0           | MEN1              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 1           | MEN1              | true
        [[0]]        | [[2]]       | [[T]]    | [[T]]       | [0, 1, 2] | 0               | 2           | MEN1              | false

        /* 3*3; skipNonTargetSubjects;
           a1 F
           = (a!1 => a0 OR a2) (b1 OR b2 OR b3) (c1 OR c2 OR c3)
           outOfRange */
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 0           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 2           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 0           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 2           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 0           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 2           | SKIP              | false

        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 0           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 1           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 2           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 0           | MEN1              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 1           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 2           | MEN1              | true

        /* 3*3
           a1 T
           = a1 (b0 OR b2) (c0 or c2)
           outOfRange */
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 0           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 0               | 2           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 0           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 2           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 0           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 2           | SKIP              | true

        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 0           | MEN1              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 2           | MEN1              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 0           | MEN1              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 2           | MEN1              | true
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 0           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 1               | 2           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 0           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[T]]       | [0, 1, 2] | 2               | 2           | ALL1              | false

        /* 3*3; skipNonTargetSubjects; rightAnswers=3
           a1 a!1 TF/FT
           = a1 (b0 OR b2) (c0 or c2) OR (a!1 => a0 OR a2) (b0 OR b1 OR b2) (c0 OR c1 OR c2)
           standard + outOfRange */
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 0           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 2           | SKIP              | false

        /* 3*3 skipNonTargetSubjects
           a!0 a!2 => a1
           = a1 (b0 OR b2) (c0 OR c2)
           (infer outOfRange) */
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 0               | 0           | SKIP              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 0               | 2           | SKIP              | false

        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 0           | SKIP              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 1           | SKIP              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 2           | SKIP              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 0           | SKIP              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 2           | SKIP              | true

        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 0           | MEN1              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 2           | MEN1              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 0           | MEN1              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 2           | MEN1              | true
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 0           | ALL1              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 1               | 2           | ALL1              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 0           | ALL1              | false
        [[0, 0]]     | [[0, 2]]    | [[T, T]] | [[T, T]]    | [0, 1, 2] | 2               | 2           | ALL1              | false
    }

    def "should valid, 3 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                targetObjectIdx, rightVarIdx, 3, 0, MAX_INT, nonTargetSubjects, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | truePhrases | rangeIdxs | targetObjectIdx | rightVarIdx | nonTargetSubjects | result
        /* 3*3; skipNonTargetSubjects;
           a1 F
           = (a!1 => a0 OR a2) (b0 OR b1 OR b2) (c0 OR c1 OR c2)
           outOfRange */
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 0           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 1           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 0               | 2           | SKIP              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 0           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 1           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 2           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 0           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 1           | SKIP              | true
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 2           | SKIP              | true

        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 0           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 1           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 2           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 0           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 1           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 2           | MEN1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 0           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 1           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 1               | 2           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 0           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 1           | ALL1              | false
        [[0]]        | [[1]]       | [[F]]    | [[F]]       | [0, 1, 2] | 2               | 2           | ALL1              | false

        /* 3*3; skipNonTargetSubjects; rightAnswers=3
           a1 a!1 TF/FT
           = a1 (b0 OR b2) (c0 or c2) OR (a!1 => a0 OR a2) (b0 OR b1 OR b2) (c0 OR c1 OR c2)
           standard + outOfRange */
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 0           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 1           | SKIP              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 2           | SKIP              | true

        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | MEN1              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | MEN1              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | MEN1              | true
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | MEN1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | MEN1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | MEN1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 0           | MEN1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 1           | MEN1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 2           | MEN1              | false

        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 0           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 1           | ALL1              | false
        [[0, 0]]     | [[1, 1]]    | [[F, T]] | [[T, F]]    | [0, 1, 2] | 2               | 2           | ALL1              | false

        /* 3*3
         a!1 b!1 TF/FT
         = (a0 OR a2) b1 (c0 OR c2) OR a1 (b0 OR b2) (c0 OR c2)
         standard + outOfRange */
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | SKIP              | true
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | SKIP              | true
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | SKIP              | true
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | SKIP              | true
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | SKIP              | true
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | SKIP              | true
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 2               | 0           | SKIP              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 2               | 1           | SKIP              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 2               | 2           | SKIP              | false

        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | MEN1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | MEN1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | MEN1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | MEN1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | MEN1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | MEN1              | false

        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 0           | ALL1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 1           | ALL1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 0               | 2           | ALL1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 0           | ALL1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 1           | ALL1              | false
        [[0, 1]]     | [[1, 1]]    | [[T, T]] | [[T, F]]    | [0, 1, 2] | 1               | 2           | ALL1              | false
    }

}
