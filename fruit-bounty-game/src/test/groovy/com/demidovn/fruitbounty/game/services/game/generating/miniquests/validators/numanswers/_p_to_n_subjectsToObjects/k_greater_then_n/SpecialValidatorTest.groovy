package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers._p_to_n_subjectsToObjects.k_greater_then_n

import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.NumAnswersExecutor
import spock.lang.Specification

// This functionality not implemented
abstract class SpecialValidatorTest extends Specification {
    private static final boolean T = true
    private static final boolean F = false
    private static final boolean DETERMINE_BY_NEGATIVES = true

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    /*
    - где k > n:
      - выведение через отрицательные утверждения;
      - не забыть outOfRange в утверждениях;

        == k < n:
        --- на потом: для c - нет ответа. Когда добавится соответствующий параметр.
        - a1, b2, c, 3, 4
        a1 b2  TT
        a1 b!2 TF
        = a1, b2

        --- на потом: для b можно сделать out of Range с 3 rightAnswers
        - a1, b2, 3, 4
        a1 b2  TT
        a1 b!2 TF
     */

//    def "should valid when standard: TF, TF, FF"() {
//        expect:
//        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRange, objectsRange, targSubjId, rightVarId, 1, DETERMINE_BY_NEGATIVES, skipNonTarget).contains(!result)
//
//        where:
//        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | subjectsRange | objectsRange | targSubjId | rightVarId | skipNonTarget | result
        /* 4*2 (a, b, c, d; 0, 1)
           c1 a1  TF/FF
           d0 a1  TF/TF
           a1 c!1 FF/FT
           = (c1 d0 b!1) OR (b1 d0 c!1) */
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 0          | 0          | true          | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 0          | 1          | true          | false
//
//        // Cause: "John was definitely drinking something"
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 1          | 0          | true          | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 1          | 1          | true          | true
//
//        // Cause: "John was definitely drinking something"
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 2          | 0          | true          | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 2          | 1          | true          | true
//
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 3          | 0          | true          | true
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 3          | 1          | true          | false
//
//        // With disabled 'skipNonTargetSubjects'
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 0          | 0          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 0          | 1          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 1          | 0          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 1          | 1          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 2          | 0          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 2          | 1          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 3          | 0          | false         | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [0, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]  | [0, 1]       | 3          | 1          | false         | false

        /* 4*2 (a, b, c1, d, 0)
           d!1 a!1 TT
           a!1 b!1 TT
           = c1; a/b/d - ?
           infer from negatives */
//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 0          | 0          | true                  | true
//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 0          | 1          | true                  | false

//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 1          | 0          | true                  | false
//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 1          | 1          | true                  | false

//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 2          | 0          | true                  | false
//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 2          | 1          | true                  | true

//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 3          | 0          | true                  | false
//        [[3, 0], [0, 1]]         | [[1, 1], [1, 1]]         | [[T, T], [T, T]]         | [[T, T], [T, T]]         | [0, 1, 2, 3]      | [0, 1]           | 3          | 1          | true                  | false

        /* 4*2 (a, b, c1, d0)
           d!1 a!1 TT
           a!1 b!1 TT
           a!0 b!0 TT
           infer from negatives */
//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 0          | 0          | true                  | false
//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 0          | 1          | true                  | false
//
//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 1          | 0          | true                  | false
//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 1          | 1          | true                  | false

//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 2          | 0          | true                  | false
//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 2          | 1          | true                  | true

//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 3          | 0          | true                  | true
//        [[3, 0], [0, 1], [0, 1]] | [[1, 1], [1, 1], [0, 0]] | [[T, T], [T, T], [T, T]] | [[T, T], [T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 3          | 1          | true                  | false
//    }

//    def "should valid when standard (more complex): TF, TF, FF"() {
//        expect:
//        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs, targSubjId, rightVarId, 1, DETERMINE_BY_NEGATIVES, skipNonTargetSubjects).contains(!result)
//
//        where:
//        subjectsIdxs             | objectsIdxs              | inverses                 | truePhrases              | subjectsRangeIdxs | objectsRangeIdxs | targSubjId | rightVarId | skipNonTargetSubjects | result
//        /* 4*2 (a, b, c1, d2, 0)
//           c1 a1  TF/FF
//           d2 a1  TF/TF
//           a1 c!1 FF/FT
//           = 1) (c1, d2) с1 a!1 d2
//           = 2) (b0, d2) a!1 c!1 d2 => a!1 b0 c!1 d2 */
//        // Или тут вообще доступно нескл. вариантов для 'b' и 'c'? Для 'b': b0 и ни один... И как тогда быть? Не должен ли тест вообще не валидироваться?
//        // Или так оставить,  - для м/кв миссий пойдет?
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 0          | 0          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 0          | 1          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 0          | 2          | true                  | false

//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 1          | 0          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 1          | 1          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 1          | 2          | true                  | false
//
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 2          | 0          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 2          | 1          | true                  | true
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 2          | 2          | true                  | false
//
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 3          | 0          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 3          | 1          | true                  | false
//        [[2, 0], [3, 0], [0, 2]] | [[1, 1], [2, 1], [1, 1]] | [[F, F], [F, F], [F, T]] | [[T, F], [T, F], [F, F]] | [0, 1, 2, 3]      | [0, 1, 2]        | 3          | 2          | true                  | true
//    }

//    def "should valid when negative: TT, TT"() {
//        expect:
//        !numAnswersExecutor.apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs, targSubjId, rightVarId, 1, DETERMINE_BY_NEGATIVES, skipNonTargetSubjects).contains(!result)
//
//        where:
//        subjectsIdxs     | objectsIdxs      | inverses         | truePhrases      | subjectsRangeIdxs | objectsRangeIdxs | targSubjId | rightVarId | skipNonTargetSubjects | result
//        /* 4*2 (a, b, c1, d0)
//           c1 a1 TT
//           d0 a1 TT
//           negative */
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 0          | 0          | true                  | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 0          | 1          | true                  | false
//
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 1          | 0          | true                  | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 1          | 1          | true                  | false
//
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 2          | 0          | true                  | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 2          | 1          | true                  | false
//
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 3          | 0          | true                  | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 3          | 1          | true                  | false
//
//        // With disabled 'skipNonTargetSubjects'
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 0          | 0          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 0          | 1          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 1          | 0          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 1          | 1          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 2          | 0          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 2          | 1          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 3          | 0          | false                 | false
//        [[2, 0], [3, 0]] | [[1, 1], [0, 1]] | [[F, F], [F, F]] | [[T, T], [T, T]] | [0, 1, 2, 3]      | [0, 1]           | 3          | 1          | false                 | false
//    }

}
