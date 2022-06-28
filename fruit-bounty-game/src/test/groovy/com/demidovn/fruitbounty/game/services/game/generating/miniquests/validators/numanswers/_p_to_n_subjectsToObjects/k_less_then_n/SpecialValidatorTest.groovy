package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._p_to_n_subjectsToObjects.k_less_then_n

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class SpecialValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard: TT, TF"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, trgObj, 1, 0, MAX_INT, nonTrg, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | subjRange | objRange     | trgSubj | trgObj | nonTrg | result
        /* 2*4 (a1, b2, 3, 4)
           a1 b2  TT/TF
           a1 b!2 TF/TT
           = a1, (b2 OR b3 OR b4) */
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 0      | SKIP   | true
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 1      | SKIP   | false
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 2      | SKIP   | false
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 3      | SKIP   | false

        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 1      | SKIP   | false
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 0      | SKIP   | false
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 2      | SKIP   | false
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 3      | SKIP   | false

        // Disabled skipNonTrg
        [[0, 1], [0, 1]]       | [[0, 1], [0, 1]]       | [[F, F], [F, T]]       | [[T, T], [T, F]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 0      | ALL1   | false

        /* 2*4 (a3, b2, 1, 0)
           a!0 a!1 a!2 TTT
           b!0 b!1 a!0 TTT
           = infer from negative statements */
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 0       | 0      | SKIP   | false
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 0       | 1      | SKIP   | false
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 0       | 2      | SKIP   | false
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 0       | 3      | SKIP   | true

        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 1       | 0      | SKIP   | false
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 1       | 1      | SKIP   | false
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 1       | 2      | SKIP   | true
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 1       | 3      | SKIP   | false

        // Disabled skipNonTrg
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 0       | 3      | ALL1   | true
        [[0, 0, 0], [1, 1, 0]] | [[0, 1, 2], [0, 1, 0]] | [[T, T, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1]    | [0, 1, 2, 3] | 1       | 2      | ALL1   | true
    }

    def "should valid when negative: TT, TF"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | subjectsRangeIdxs | objectsRangeIdxs | targetSubjectIdx | rightVarIdx | result
        /* 2*4 (a1, b2, 3, 4)
           a1  b2
           a!1 b!2
           no answer */
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 1           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 2           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 3           | false

        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 1           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 2           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [T, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 3           | false
    }

    def "should valid when standard: TT, TF, 2 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx, 2, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | subjectsRangeIdxs | objectsRangeIdxs | targetSubjectIdx | rightVarIdx | result
        /* 2*4 (a1, b2, 3, 4)
           a1 b2  TT/TF
           a1 b!2 TF/TT
           = a1, (b2 OR b3 OR b4)
           for 'b': both standard + outOfRange */
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 1           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 2           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 3           | false

        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 1           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 2           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 3           | false
    }

    def "should valid when standard: TT, TF, 3 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx, 3, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | subjectsRangeIdxs | objectsRangeIdxs | targetSubjectIdx | rightVarIdx | result
        /* 2*4 (a0, b1, 2, 3)
           a0 b2  TT/TF
           a0 b!2 TF/TT
           = a0, (b1 OR b2 OR b3)
           for 'b': both standard + outOfRange */
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 0           | false
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 1           | false
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 2           | false
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 3           | false

        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 0           | false
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 1           | true
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 2           | true
        [[0, 1], [0, 1]] | [[0, 2], [0, 2]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 3           | true
    }

    def "should valid when standard: TT, TF, 4 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx, 4, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | subjectsRangeIdxs | objectsRangeIdxs | targetSubjectIdx | rightVarIdx | result
        /* 2*4 (a1, b2, 3, 4)
           a1 b2  TT/TF
           a1 b!2 TF/TT
           = a1, (b2 OR b3 OR b4)
           standard + out of range */
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 1           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 2           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 0                | 3           | false

        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 0           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 1           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 2           | false
        [[0, 1], [0, 1]] | [[0, 1], [0, 1]] | [[F, F], [F, T]] | [[T, T], [T, F]] | [0, 1]            | [0, 1, 2, 3]     | 1                | 3           | false
    }

    def "should valid, skipNonTargetSubjects"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, rightVar, 1, 0, MAX_INT, nonTrgt, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs   | objectsIdxs    | inverses       | truePhrases    | subjRange | objRange     | trgSubj | rightVar | nonTrgt | result
        /* 2*3 (a2, b1, 0)
           b!0 b!1 b!2
           a!0 a!1 */
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 0       | 0        | SKIP    | false
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 0       | 1        | SKIP    | false
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 0       | 2        | SKIP    | true
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 1       | 0        | SKIP    | false
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 1       | 1        | SKIP    | true
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 1       | 2        | SKIP    | false

        // Disabled 'skipNonTargetSubjects'
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 0       | 2        | ALL1    | true
        [[1, 1, 0, 0]] | [[0, 2, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2]    | 0       | 2        | MEN1    | true

        /* 2*4 (a0, b1, 2, 3)
           b1 a!2 a!3 */
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 0       | 0        | SKIP    | true
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 0       | 1        | SKIP    | false
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 0       | 2        | SKIP    | false
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 1       | 0        | SKIP    | false
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 1       | 1        | SKIP    | true
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 1       | 2        | SKIP    | false

        // Disabled 'skipNonTargetSubjects'
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 0       | 0        | ALL1    | true
        [[1, 0, 0]]    | [[1, 2, 3]]    | [[F, T, T]]    | [[T, T, T]]    | [0, 1]    | [0, 1, 2, 3] | 1       | 1        | ALL1    | true
    }

    def "should valid, negative"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, rightVar, 1, 0, MAX_INT, nonTrgt, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs      | objectsIdxs       | inverses          | truePhrases       | subjRange | objRange  | trgSubj | rightVar | nonTrgt | result
        /* 2*3 (a2, b, 0, 1)
           b!0 b!1 b!2
           a!0 a!1
            = no answer, cause empty 'b' */
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 0        | SKIP    | false
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 1        | SKIP    | false
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 2        | SKIP    | false
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 0        | SKIP    | false
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 1        | SKIP    | false
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 2        | SKIP    | false

        // Disabled 'skipNonTargetSubjects'
        [[1, 1, 1, 0, 0]] | [[0, 1, 2, 0, 1]] | [[T, T, T, T, T]] | [[T, T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 2        | ALL1    | false
    }

    def "should valid, skipNonTargetSubjects, negatives"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, rightVar, 1, 0, MAX_INT, nonTrgt, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs   | objectsIdxs    | inverses       | truePhrases    | subjRange | objRange  | trgSubj | rightVar | nonTrgt | result
        /* 2*3 (a2, b, 0, 1)
           a!0 a!1
           b!0 b!1
           cause: contradictions or ambiguity */
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 0        | SKIP    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 1        | SKIP    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 2        | SKIP    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 0        | SKIP    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 1        | SKIP    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 2        | SKIP    | false

        // Disabled 'skipNonTargetSubjects'
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 0        | ALL1    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 1        | ALL1    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 0       | 2        | ALL1    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 0        | ALL1    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 1        | ALL1    | false
        [[0, 0, 1, 1]] | [[0, 1, 0, 1]] | [[T, T, T, T]] | [[T, T, T, T]] | [0, 1]    | [0, 1, 2] | 1       | 2        | ALL1    | false
    }

    def "should valid, skipNonTargetSubjects, 3 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, rightVar, 3, 0, MAX_INT, nonTrgt, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | truePhrases | subjRange | objRange     | trgSubj | rightVar | nonTrgt | result
        /* 2*4 (a0, b1, 2, 3)
           b!0
           = (a0 OR a1 OR a2 OR a3), (b1 OR b2 OR b3) outOfRange */
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 0        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 1        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 2        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 3        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 0        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 1        | SKIP    | true
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 2        | SKIP    | true
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 3        | SKIP    | true

        // Disabled 'skipNonTargetSubjects'
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 0        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 1        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 2        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 3        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 0        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 1        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 2        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 3        | ALL1    | false
    }

    def "should valid, skipNonTargetSubjects, 4 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjRange, objRange,
                trgSubj, rightVar, 4, 0, MAX_INT, nonTrgt, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs | objectsIdxs | inverses | truePhrases | subjRange | objRange     | trgSubj | rightVar | nonTrgt | result
        /* 2*4 (a0, b1, 2, 3)
           b!0
           = (a0 OR a1 OR a2 OR a3), (b1 OR b2 OR b3) outOfRange */
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 0        | SKIP    | true
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 1        | SKIP    | true
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 2        | SKIP    | true
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 3        | SKIP    | true
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 0        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 1        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 2        | SKIP    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 3        | SKIP    | false

        // Disabled 'skipNonTargetSubjects'
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 0        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 1        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 2        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 0       | 3        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 0        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 1        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 2        | ALL1    | false
        [[1]]        | [[0]]       | [[T]]    | [[T]]       | [0, 1]    | [0, 1, 2, 3] | 1       | 3        | ALL1    | false
    }

}
