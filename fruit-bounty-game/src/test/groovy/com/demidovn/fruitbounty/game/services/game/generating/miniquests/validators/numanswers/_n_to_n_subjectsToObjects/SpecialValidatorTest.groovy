package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._n_to_n_subjectsToObjects

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor

class SpecialValidatorTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    def "should valid when standard: TTT TTT"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs, 0, rightVarIdx,
                1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs    | rightVarIdx | result
        /* a1 b2 c3 TTT
           a1 b2 c3 TTT */
        [[0, 1, 2], [0, 1, 2]] | [[0, 1, 2], [0, 1, 2]] | [[F, F, F], [F, F, F]] | [[T, T, T], [T, T, T]] | [0, 1, 2]    | 0           | true

        /* a1  b2 c3 TTT
           a!1 b2 c3 TTT */
        [[0, 1, 2], [0, 1, 2]] | [[0, 1, 2], [0, 1, 2]] | [[F, F, F], [T, F, F]] | [[T, T, T], [T, T, T]] | [0, 1, 2]    | 0           | false

        /* 3*3 (a1, b2, c3)
           a1  b2  c!2 TTT
           a!2 b!3 c!1 TTT */
        [[0, 1, 2], [0, 1, 2]] | [[0, 1, 1], [1, 2, 0]] | [[F, F, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1, 2]    | 0           | true
        [[0, 1, 2], [0, 1, 2]] | [[0, 1, 1], [1, 2, 0]] | [[F, F, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1, 2, 3] | 0           | false

        /* 3*3 (a1, b2, c3)
           a1  b2  c!3 TTT
           a!2 b!3 c!1 TTT
           no answers */
        [[0, 1, 2], [0, 1, 2]] | [[0, 1, 2], [1, 2, 0]] | [[F, F, T], [T, T, T]] | [[T, T, T], [T, T, T]] | [0, 1, 2]    | 0           | false
    }

    def "should valid when standard: TTF FFT"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgObjIdx, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs    | trgObjIdx | rightVarIdx | result
        /* a1  b2  b!2 TTF/FTF/TFT/etc.
           a!1 b!2 b2  FFT/TFT/FTF/etc.
           = 3 answers: (a1 or a2 or a3), (b1 or b2 or b3), (c1 or c2 or c3) */
        // Negative: cause expected 1 rightAnswers, but found 2.
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 1           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 2           | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 1           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 2           | false

        // Negative: cause expected 1 rightAnswers for 'c', but found 3.
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 1           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 2           | false

        /* a1  b2  b!2 TTF/FFT
           a!1 b!2 b2  FFT/TTF
           = 2 answers: (a1 b2) OR (a2, b2) */
        // Negative: cause expected 1 rightAnswers, but found 2.
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 0         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 0         | 1           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 1         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 1         | 1           | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2, 3] | 0         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, T, T]] | [0, 1, 2]    | 0         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[T, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 0           | false
    }

    def "should valid when standard: TTF FFT, 2 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgObjIdx, rightVarIdx, 2, 0, MAX_INT, nonTargetAnswers, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs    | trgObjIdx | rightVarIdx | nonTargetAnswers | result
        /* a1  b2  b!2 TTF/FTF/TFT/etc.
           a!1 b!2 b2  FFT/TFT/FTF/etc.
           = 3 answers: (a1 or a2 or a3), (b1 or b2 or b3), (c1 or c2 or c3) */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 0           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 1           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 2           | SKIP             | false

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 0           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 1           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 2           | SKIP             | false

        // Negative: cause expected 2 rightAnswers for 'c', but found 3.
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 0           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 1           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 2           | SKIP             | false

        // Just negatives
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2, 3] | 0         | 0           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, T, T]] | [0, 1, 2]    | 0         | 0           | SKIP             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[T, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 0           | SKIP             | false

        /* a1  b2  b!2 TTF/FFT
           a!1 b!2 b2  FFT/TTF
           = 2 answers: (a1 b2) OR (a2, b1) */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 0         | 0           | SKIP             | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 0         | 1           | SKIP             | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 1         | 0           | SKIP             | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 1         | 1           | SKIP             | true

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 0         | 0           | ALL1             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 0         | 1           | ALL1             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 1         | 0           | ALL1             | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1]       | 1         | 1           | ALL1             | false
    }

    def "should valid when standard: TTF FFT, 3 rightAnswers"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgObjIdx, rightVarIdx, 3, 0, MAX_INT, SKIP, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs           | objectsIdxs            | inverses               | truePhrases            | rangeIdxs    | trgObjIdx | rightVarIdx | result
        /* a1  b2  b!2 TTF/FTF/TFT/etc.
           a!1 b!2 b2  FFT/TFT/FTF/etc.
           = 3 answers: (a1 or a2 or a3), (b1 or b2 or b3), (c1 or c2 or c3) */
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 0           | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 1           | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 2           | true

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 0           | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 1           | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 1         | 2           | true

        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 0           | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 1           | true
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 2         | 2           | true

        // Just negatives
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2, 3] | 0         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[F, F, T], [T, T, F]] | [[T, T, F], [F, T, T]] | [0, 1, 2]    | 0         | 0           | false
        [[0, 1, 1], [0, 1, 1]] | [[0, 1, 1], [0, 1, 1]] | [[T, F, T], [T, T, F]] | [[T, T, F], [F, F, T]] | [0, 1, 2]    | 0         | 0           | false
    }

    def "should valid when standard: TT, TT, FF"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                0, rightVarIdx, 1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | rangeIdxs       | rightVarIdx | result
        /* 3*3
           a!3 b2  TT
           c3  a!2 TT
           a!1 a2  FF */
        [[0, 1], [2, 0], [0, 0]] | [[2, 1], [2, 1], [0, 1]] | [[T, F], [F, T], [T, F]] | [[T, T], [T, T], [F, F]] | [0, 1, 2]       | 0           | true
        [[0, 1], [2, 0], [0, 0]] | [[2, 1], [2, 1], [0, 1]] | [[T, F], [F, T], [T, F]] | [[T, T], [T, T], [F, F]] | [0, 1, 2, 3]    | 0           | true
        [[0, 1], [2, 0], [0, 0]] | [[2, 1], [2, 1], [0, 1]] | [[T, F], [F, T], [T, F]] | [[T, T], [T, T], [F, F]] | [0, 1, 2, 3, 4] | 0           | false

        /* 3*3
           a!3 b2  TT
           c!3 a!2 TT
           a!1 a2  FF
           no answers: contradictions */
        [[0, 1], [2, 0], [0, 0]] | [[2, 1], [2, 1], [0, 1]] | [[T, F], [T, T], [T, F]] | [[T, T], [T, T], [F, F]] | [0, 1, 2]       | 0           | false
    }

    def "should valid when standard: TT, FF, TF; answerOutOfStatements modes"() {
        expect:
        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, rangeIdxs, rangeIdxs,
                trgObjIdx, rightVarIdx, 1, 0, MAX_INT, SKIP, outOfStMode, Collections.emptyMap()).contains(!result)

        where:
        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | rangeIdxs | trgObjIdx | rightVarIdx | outOfStMode | result
        /* 3*3
           a0  b!1 FF
           a!1 b!1 TF
           c!1 b!2 TT
           = a2 b1 c0 */
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 0         | 0           | ANY         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 0         | 1           | ANY         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 0         | 2           | ANY         | true
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 0         | 2           | OUT         | true
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 0         | 2           | IN          | false

        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 1         | 0           | ANY         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 1         | 1           | ANY         | true
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 1         | 2           | ANY         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 1         | 1           | OUT         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 1         | 1           | IN          | true

        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 2         | 0           | ANY         | true
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 2         | 1           | ANY         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 2         | 2           | ANY         | false
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 2         | 0           | OUT         | true
        [[0, 1], [0, 1], [2, 1]] | [[0, 1], [1, 1], [1, 2]] | [[F, T], [T, T], [T, T]] | [[T, T], [T, F], [F, F]] | [0, 1, 2] | 2         | 0           | IN          | false
    }

}
