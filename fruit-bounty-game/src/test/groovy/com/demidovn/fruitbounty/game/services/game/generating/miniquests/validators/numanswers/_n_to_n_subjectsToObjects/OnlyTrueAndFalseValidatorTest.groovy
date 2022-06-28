package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class OnlyTrueAndFalseValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVar, nAnswers, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | rangeIdxs    | rightVar | nAnswers | nonTrg | result
        /* 3*3 (a1, b2, c3)
           a1  b2  TT/FF
           a!1 b!2 FF/TT
           = 1) a1 b2 c3; infer data (c3)
           = 2) (a2 OR a3), (b1 OR b3), (c1 OR c2 OR c3) */
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 1        | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 2        | 1        | SKIP   | false

        // Contradictions
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, F]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 1        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2, 3] | 0        | 1        | SKIP   | false

        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 1        | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 2        | 2        | SKIP   | false

        // Contradictions
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, F]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 2        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2, 3] | 0        | 2        | SKIP   | false

        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 1        | 3        | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 2        | 3        | SKIP   | true

        // Disabled skipNonTrg
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 1        | 3        | ALL1   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2]    | 2        | 3        | ALL1   | false

        // Contradictions
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, F]] | [[T, T], [F, F]] | [0, 1, 2]    | 0        | 3        | SKIP   | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2, 3] | 0        | 3        | SKIP   | false

        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2, 3] | 0        | 4        | SKIP   | true
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [F, F]] | [0, 1, 2, 3] | 0        | 4        | ALL1   | false
    }

    def "should valid when UI some subject hasn't any object"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, nAnswers, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | rangeIdxs | trgSubj | trgObj | nAnswers | nonTrg | result
        /* 2*2
           a!0 a=1
           b!0 a=0
           b!1 b=0
            = no right answers cause: there isn't any object for 'b' */
        [[0, 0], [1, 0], [1, 1]] | [[0, 1], [0, 0], [1, 0]] | [[T, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1]    | 0       | 0      | 1        | SKIP   | false
        [[0, 0], [1, 0], [1, 1]] | [[0, 1], [0, 0], [1, 0]] | [[T, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1]    | 0       | 1      | 1        | SKIP   | false

        [[0, 0], [1, 0], [1, 1]] | [[0, 1], [0, 0], [1, 0]] | [[T, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1]    | 1       | 0      | 1        | SKIP   | false
        [[0, 0], [1, 0], [1, 1]] | [[0, 1], [0, 0], [1, 0]] | [[T, F], [T, F], [T, F]] | [[T, T], [T, F], [F, F]] | [0, 1]    | 1       | 1      | 1        | SKIP   | false
    }

    def "should valid when standard 2"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgSubj, trgObj, nAnswers, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs    | trgSubj | trgObj | nAnswers | nonTrg | result
        /* 3*3 (a1, b2, c3)
           a1  b2 b!1 TTT
           a!1 b1 b1  FFF
           = 1) a1 b2 c3; infer data
           = 2) (a2 OR a3), b1, (c2 OR c3) */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 0      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 1      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 2      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 0      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 1      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 2      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 0      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 1      | 1        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 2      | 1        | SKIP   | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 0      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 1      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 2      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 0      | 2        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 1      | 2        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 2      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 0      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 1      | 2        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 2      | 2        | SKIP   | true

        // Disabled skipNonTrg
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 0      | 2        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 1      | 2        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 1      | 2        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 2      | 2        | ALL1   | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 0      | 3        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 1      | 3        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 2      | 3        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 0      | 3        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 1      | 3        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1       | 2      | 3        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 0      | 3        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 1      | 3        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2       | 2      | 3        | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 0      | 3        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 1      | 3        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0       | 2      | 3        | ALL1   | false

        /* 3*3 (a1, b2...)
           a1  b2 b!1 TTT
           a!1 b1 b1  FFF
           = 1) a1,               b2, (c3 OR c4),       (d3 OR d4);
           = 2) (a2 OR a3 OR a4), b1, (c2 OR c3 OR c4), (d2 OR d3 OR d4) */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 0      | 4        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 1      | 4        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 2      | 4        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 3      | 4        | SKIP   | true

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 0      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 1      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 2      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 3      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 0      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 1      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 2      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 3      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 0      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 1      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 2      | 4        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 3      | 4        | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 0      | 4        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 1      | 4        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 2      | 4        | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 3      | 4        | ALL1   | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 0      | 2        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 1      | 2        | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 2      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1       | 3      | 2        | SKIP   | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 0      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 1      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 2      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0       | 3      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 0      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 1      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 2      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2       | 3      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 0      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 1      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 2      | 2        | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [0, 0, 0]] | [[F, F, T], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3       | 3      | 2        | SKIP   | false
    }

    def "should valid when standard, negatives"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs | rightVarIdx | result
        /* 3*3 (a1, b2, c3)
           a1 b2  b!1 TTT
           a2 b!2 b!1 FFF
           + infer data */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, T]] | [[T, T, T], [F, F, F]] | [0, 1, 2] | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, T]] | [[T, T, T], [F, F, F]] | [0, 1, 2] | 1           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, T]] | [[T, T, T], [F, F, F]] | [0, 1, 2] | 2           | false

        /* 3*3 (a1, b2, c3)
           a1  b2 c3 TTT
           a!1 b1 a1 FFF */
        [[0, 1, 2], [0, 1, 0]] | [[0, 1, 2], [0, 0, 0]] | [[F, F, F], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2] | 0           | false
        [[0, 1, 2], [0, 1, 0]] | [[0, 1, 2], [0, 0, 0]] | [[F, F, F], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2] | 1           | false
        [[0, 1, 2], [0, 1, 0]] | [[0, 1, 2], [0, 0, 0]] | [[F, F, F], [T, F, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2] | 2           | false
    }

    def "should valid when standard, 2 right answers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 2, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs    | rightVarIdx | nonTrg | result
        /* 3*3 (a1, b2, c3)
           a1 b2  b!1 TTT
           a2 b!2 b1  FFF
           = 1) a1 b2 c3; infer last gap
           = 2) a2 b1 c3; infer last gap */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 0           | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 1           | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2]    | 2           | SKIP   | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0           | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1           | SKIP   | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 2           | SKIP   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 3           | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0           | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1           | ALL1   | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 0           | ALL1   | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 0], [1, 1, 0]] | [[F, F, T], [F, T, F]] | [[T, T, T], [F, F, F]] | [0, 1, 2, 3] | 1           | ALL1   | false
    }

}
