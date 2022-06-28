package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class HalfTrueHalfFalseValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid, simple"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, nAnswers, minInfers, maxInfers, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | rangeIdxs | trgSubj | trgObj | nAnswers | minInfers | maxInfers | nonTrg | result
        /* 2*2
           a!0 b0  TF/FT
           a0  b!0 FT/FT
           = a0 OR a1, b0 OR b1 */
        [[0, 1], [0, 1]] | [[0, 0], [0, 0]] | [[T, F], [F, T]] | [[T, T], [F, F]] | [0, 1]    | 0       | 0      | 1        | 0         | 9         | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 0], [0, 0]] | [[T, F], [F, T]] | [[T, T], [F, F]] | [0, 1]    | 0       | 1      | 1        | 0         | 9         | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 0], [0, 0]] | [[T, F], [F, T]] | [[T, T], [F, F]] | [0, 1]    | 1       | 0      | 1        | 0         | 9         | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 0], [0, 0]] | [[T, F], [F, T]] | [[T, T], [F, F]] | [0, 1]    | 1       | 1      | 1        | 0         | 9         | ALL1   | false
    }

    def "should valid when standard"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs                             | objectsIdxs                              | inverses                                 | truePhrases                              | rangeIdxs          | rightVarIdx | result
        /* 3*3 (a1, b2, c3)
           a1 b3 TF
           b2 a2 TF
           a1 c2 TF
           = 1 answer (contradiction: a2 to c2); c3 - infer */
        [[0, 1], [1, 0], [0, 2]]                 | [[0, 2], [1, 1], [0, 1]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]          | 0           | true

        /* 3*3 (a1, b2, c3)
           a!2 c1 TF
           a!3 c1 TF
           b!3 c1 TF
           = answers infer from negative data (= a!2 c!1 a!3 b!3) */
        [[0, 2], [0, 2], [1, 2]]                 | [[1, 0], [2, 0], [2, 0]]                 | [[T, F], [T, F], [T, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]          | 0           | true
        [[0, 2], [0, 2], [1, 2]]                 | [[1, 0], [2, 0], [2, 0]]                 | [[T, F], [T, F], [T, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3]       | 0           | false

        /* 4*4 (a3 b1 c4 d2)
        c2 b1 FT
        c1 d2 FT
        a3 b4 TF
        (if c2=T, then get contradiction: c2 to d2) */
        [[2, 1], [2, 3], [0, 1]]                 | [[1, 0], [0, 1], [2, 3]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3]       | 2           | true
        [[2, 1], [2, 3], [0, 1]]                 | [[1, 0], [0, 1], [2, 3]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3]       | 0           | false
        [[2, 1], [2, 3], [0, 1]]                 | [[1, 0], [0, 1], [2, 3]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3]       | 1           | false
        [[2, 1], [2, 3], [0, 1]]                 | [[1, 0], [0, 1], [2, 3]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3]       | 3           | false
        [[2, 1], [2, 3], [0, 1]]                 | [[1, 0], [0, 1], [2, 3]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3, 4]    | 0           | false

        /* 5*5 (a1 b2 c4 d3 e5)
           d1 b2
           a1 c3
           c4 b1
           c3 e5
           e5 a4
           = (if d1=T then get contradiction: d1 to b1); d - infer */
        [[3, 1], [0, 2], [2, 1], [2, 4], [4, 0]] | [[0, 1], [0, 2], [3, 0], [2, 4], [4, 3]] | [[F, F], [F, F], [F, F], [F, F], [F, F]] | [[T, F], [T, F], [T, F], [T, F], [T, F]] | [0, 1, 2, 3, 4]    | 0           | true
        [[3, 1], [0, 2], [2, 1], [2, 4], [4, 0]] | [[0, 1], [0, 2], [3, 0], [2, 4], [4, 3]] | [[F, F], [F, F], [F, F], [F, F], [F, F]] | [[T, F], [T, F], [T, F], [T, F], [T, F]] | [0, 1, 2, 3, 4, 5] | 0           | false
    }

    def "should valid generated in UI case"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, nAnswers, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | rangeIdxs    | trgSubj | trgObj | nAnswers | nonTrg | result
        /* 4*4
           a0  b1 TF/FT
           a1  c2 FT/FT
           b!2 d2 TF/TF
           (a0 b3 c2 d1) OR (a3 b1 c2 d0) */
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 3      | 1        | SKIP   | false

        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 3      | 1        | SKIP   | false

        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 2      | 1        | SKIP   | true
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 3      | 1        | SKIP   | false

        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 3      | 1        | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 2      | 1        | ALL1   | false

        // 2 rightAnswers
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 0      | 2        | SKIP   | true
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 1      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 3      | 2        | SKIP   | true

        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 0      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 1      | 2        | SKIP   | true
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 3      | 2        | SKIP   | true

        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 0      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 1      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 2       | 3      | 2        | SKIP   | false

        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 0      | 2        | SKIP   | true
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 1      | 2        | SKIP   | true
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 3      | 2        | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 0      | 2        | ALL1   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 0       | 3      | 2        | ALL1   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 1      | 2        | ALL1   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 1       | 3      | 2        | ALL1   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 0      | 2        | ALL1   | false
        [[0, 1], [0, 2], [1, 3]] | [[0, 1], [1, 2], [2, 2]] | [[F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F]] | [0, 1, 2, 3] | 3       | 1      | 2        | ALL1   | false

        /* 3*3
           a0  b1
           c!2 b1
           b!1 c2
           = a2 b1 c0 */
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 0       | 0      | 1        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 0       | 1      | 1        | SKIP   | false
        // Cause 'b' hasn't any object
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 0       | 2      | 1        | SKIP   | true
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 0      | 1        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 1      | 1        | SKIP   | true
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 2      | 1        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 2       | 0      | 1        | SKIP   | true
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 2       | 1      | 1        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 2       | 2      | 1        | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 1      | 1        | ALL1   | true

        // 2 rightAnswers
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 0       | 0      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 0       | 1      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 0       | 2      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 0      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 1      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 1       | 2      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 2       | 0      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 2       | 1      | 2        | SKIP   | false
        [[0, 1], [2, 1], [1, 2]] | [[0, 1], [2, 1], [1, 2]] | [[F, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1, 2]    | 2       | 2      | 2        | SKIP   | false
    }

    def "should valid when standard, negatives"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs                             | objectsIdxs                              | inverses                                 | truePhrases                              | rangeIdxs       | rightVarIdx | result
        /* 2*2 (a1, b2)
           a1 b2 TF
           a2 b1 FT
           = negative: contradictions */
        [[0, 1], [0, 1]]                         | [[0, 1], [1, 0]]                         | [[F, F], [F, F]]                         | [[T, F], [T, F]]                         | [0, 1]          | 0           | false

        /* 3*3
           a2 b1
           a1 b1
           b2 c1
           = negative: (if a2=T, then get contradiction b1 to !b1); if a2=F, then also get contradiction: b1 to c1 */
        [[0, 1], [0, 1], [1, 2]]                 | [[1, 0], [0, 0], [1, 0]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 0           | false

        /* 3*3
           a2 b1
           a3 b1
           b2 c1
           - if a2=T, then contradiction (b1 to !b1); if a2=F, then contradiction (b1 to c1) */
        [[0, 1], [0, 1], [1, 2]]                 | [[1, 0], [2, 0], [1, 0]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 0           | false
        [[0, 1], [0, 1], [1, 2]]                 | [[1, 0], [2, 0], [1, 0]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 1           | false
        [[0, 1], [0, 1], [1, 2]]                 | [[1, 0], [2, 0], [1, 0]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 2           | false

        /* 3*3 (a1, b2, c3)
           a1 b3 TF
           a2 b2 FT
           = negative: a1 b2 OR a2 b3 - more then 1 answers (may be 2 answers) */
        [[0, 1], [0, 1]]                         | [[0, 2], [1, 1]]                         | [[F, F], [F, F]]                         | [[T, F], [T, F]]                         | [0, 1, 2]       | 0           | false

        /* 3*3
           a!2 a3
           a!2 a1
           b1 c2 */
        [[0, 0], [0, 0], [1, 2]]                 | [[1, 2], [1, 0], [0, 1]]                 | [[T, F], [T, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 0           | false

        /* 3*3
           a!2 a3
           a!2 a1
           !b1 c2 */
        [[0, 0], [0, 0], [1, 2]]                 | [[1, 2], [1, 0], [0, 1]]                 | [[T, F], [T, F], [T, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 0           | false

        /* 3*3
           a!2 a3
           a!2 a1
           !b1 !c2 */
        [[0, 0], [0, 0], [1, 2]]                 | [[1, 2], [1, 0], [0, 1]]                 | [[T, F], [T, F], [T, T]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2]       | 0           | false

        /* 4*4 (a3 b1 c4 d2)
           c2 b1
           c1 d2
           a2 b4
           no answers: contradictions */
        [[2, 1], [2, 3], [0, 1]]                 | [[1, 0], [0, 1], [1, 3]]                 | [[F, F], [F, F], [F, F]]                 | [[T, F], [T, F], [T, F]]                 | [0, 1, 2, 3]    | 0           | false

        /* 5*5 (a1 b2 c4 d3 e5)
           d1 b2
           a1 c3
           c4 b1
           c3 e5
           e!5 a4
           no answers: contradictions */
        [[3, 1], [0, 2], [2, 1], [2, 4], [4, 0]] | [[0, 1], [0, 2], [3, 0], [2, 4], [4, 3]] | [[F, F], [F, F], [F, F], [F, F], [T, F]] | [[T, F], [T, F], [T, F], [T, F], [T, F]] | [0, 1, 2, 3, 4] | 0           | false
    }

    def "should valid when standard, 2 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, 2, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | rangeIdxs | trgSubj | trgObj | nonTrg | result
        /* 2*2 (a1, b2)
           a1 b1 TF/FT
           a2 b2 FT/TF
           = 2 answers */
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 0       | 0      | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 0       | 1      | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 1       | 0      | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 1       | 1      | SKIP   | true

        // Disabled skipNonTrg
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 0       | 0      | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 0       | 1      | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 1       | 0      | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 0], [1, 1]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 1       | 1      | ALL1   | false

        /* 2*2 (a1, b2)
           a1 b!2 TF/FT
           a2 b!1 FT/TF
           = 2 answers */
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 0       | 0      | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 0       | 1      | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 1       | 0      | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 1       | 1      | SKIP   | true

        // Disabled skipNonTrg
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 0       | 0      | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 0       | 1      | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 1       | 0      | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, T], [F, T]] | [[T, F], [T, F]] | [0, 1]    | 1       | 1      | ALL1   | false
    }

    def "should valid when standard, 2 rightAnswers, negatives"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 2, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | rangeIdxs | rightVarIdx | result
        /* 2*2 (a1, b2)
           a1 b2 TF
           a2 b1 FT
           = negative: contradictions */
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [1, 0]] | [[F, F], [F, F]] | [[T, F], [T, F]] | [0, 1]    | 1           | false
    }

    def "should valid when standard 2"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, nAnswers, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | rangeIdxs | trgSubj | trgObj | nAnswers | nonTrg | result
        /* 3*3
           a1  b0  TT/FF
           a!1 b!0 FF/TT
           = 1) a1 b0 c2
           = 2) (a0 OR a2), (b1 OR b2), (c0 OR c1 OR c2) */
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 1        | SKIP   | false

        // 2 rightAnswers
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 2        | SKIP   | false

        // 3 rightAnswers
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 3        | SKIP   | true

        // Disabled skipNonTrg
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [1, 0]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 3        | ALL1   | false
    }

    def "should valid when standard 3"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, nAnswers, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | rangeIdxs | trgSubj | trgObj | nAnswers | nonTrg | result
        /* 3*3
           a1 b0 TT/FF
           a0 b1 FF/TT
           = 1) a1 b0 c2
           = 2) a0 b1 c2 */
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 1        | SKIP   | true

        // Disabled skipNonTrg
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 1        | ALL1   | false

        // 2 rightAnswers
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 2        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 2        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 2        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 2        | SKIP   | true
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 2        | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 2        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 2        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 2        | ALL1   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 2        | ALL1   | false

        // 3 rightAnswers
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 0      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 1      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 0       | 2      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 0      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 1      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 1       | 2      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 0      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 1      | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[1, 0], [0, 1]] | [[F, F], [F, F]] | [[T, T], [F, F]] | [0, 1, 2] | 2       | 2      | 3        | SKIP   | false
    }

}
