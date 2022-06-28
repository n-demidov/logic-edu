package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers

import com.demidovn.fruitbounty.game.exceptions.NotImplementedException
import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidParams
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements

class NumAnswersValidatorExceptionsTest extends BaseValidatorTest {

    NumAnswersExecutor numAnswersExecutor = new NumAnswersExecutor()

    // General

    def "should throw exception when 'k > n'"() {
        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange, trgtSubj, rightVarIdx,
                1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        then:
        def e = thrown(NotImplementedException)

        where:
        subjIdxs    | objectIdxs  | inverse     | truePhrases | subjRange | objRange | trgtSubj | rightVarIdx | result
        [[0, 1, 2]] | [[0, 1, 2]] | [[T, T, F]] | [[F, T, F]] | [0, 1, 2] | [0, 1]   | 0        | 0           | false
    }

    def "should throw exception when 'rightAnswersNum < 0'"() {
        when:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, 1, OutOfStatements.SKIP_VALIDATION).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage().startsWith("rightAnswersNum < 0")

        where:
        objectIdxs | inverse            | rangeIdxs    | rightVarIdx | rightAnswersNum | result
        [0, 1, 2]  | [true, true, true] | [0, 1, 2, 3] | 0           | -1              | false
    }

    def "should throw exception when 'rightAnswersNum > objectsRangeIdxs'"() {
        when:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, 1, OutOfStatements.SKIP_VALIDATION).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage().startsWith("rightAnswersNum > objectsRangeIdxs.size()")

        where:
        objectIdxs | inverse            | rangeIdxs    | rightVarIdx | rightAnswersNum | result
        [0, 1, 2]  | [true, true, true] | [0, 1, 2, 3] | 0           | 5               | false
        [0, 1, 2]  | [true, true, true] | [0, 1]       | 0           | 3               | false
    }

    def "should throw exception on illegal 'minAnswerInfers' or 'maxAnswerInfers'"() {
        setup:
        List<List<Boolean>> verityAllocation = [[T, F], [F, F]]

        when:
        !numAnswersExecutor.apply(objectIdxs, objectIdxs, verityAllocation, verityAllocation, range, range,
                0, 0,
                new ValidParams(1, SKIP, ANY, minAnswerInfers, maxAnswerInfers, Collections.emptyMap()),
                false)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage().startsWith("Illegal minAnswerInfers or maxAnswerInfers")

        where:
        objectIdxs       | range  | minAnswerInfers | maxAnswerInfers
        [[0, 1], [0, 1]] | [0, 1] | -1              | 1
        [[0, 1], [0, 1]] | [0, 1] | -9              | 2
        [[0, 1], [0, 1]] | [0, 1] | 1               | 0
        [[0, 1], [0, 1]] | [0, 1] | 3               | 2
    }

    def "should throw exception when 'statements.size() != truePhrases.size()'"() {
        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange, trgtSubj, rightVarIdx,
                1, 0, MAX_INT, ALL1, ANY, Collections.emptyMap()).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "verityAllocation.size() != sentencesNum"

        where:
        subjIdxs    | objectIdxs  | inverse     | truePhrases | subjRange | objRange  | trgtSubj | rightVarIdx | result
        [[0]]       | [[0, 1, 2]] | [[T, T, F]] | [[F], [T]]  | [0, 1, 2] | [0, 1, 2] | 0        | 0           | false
        [[0, 1, 2]] | [[0, 1, 2]] | [[T, T, F]] | [[F], [T]]  | [0, 1, 2] | [0, 1, 2] | 0        | 0           | false
    }

    def "should throw exceptions when 'globalRightAnswers are out of subjectsRange'"() {
        setup:
        Map<Integer, Integer> globalRightAnswers = new HashMap<>()
        for (int row = 0; row < rightAnswers.size(); row++) {
            List<Integer> columns = rightAnswers[row]
            globalRightAnswers.put(columns.get(0), columns.get(1))
        }

        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange, trgtSubj, rightVarIdx,
                1, 0, MAX_INT, ALL1, OutOfStatements.SKIP_VALIDATION, globalRightAnswers)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "globalRightAnswers are out of subjectsRange"

        where:
        subjIdxs | objectIdxs | inverse  | truePhrases | subjRange | objRange  | trgtSubj | rightVarIdx | rightAnswers
        [[0, 2]] | [[0, 2]]   | [[T, T]] | [[F, F]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | [[-1, 2]]
        [[0, 2]] | [[0, 2]]   | [[T, T]] | [[F, F]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | [[3, 2]]
        [[0, 2]] | [[0, 2]]   | [[T, T]] | [[F, F]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | [[99, 2]]
    }

    def "should throw exceptions when 'globalRightAnswers are out of objectsRangeIdxs'"() {
        setup:
        Map<Integer, Integer> globalRightAnswers = new HashMap<>()
        for (int row = 0; row < rightAnswers.size(); row++) {
            List<Integer> columns = rightAnswers[row]
            globalRightAnswers.put(columns.get(0), columns.get(1))
        }

        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange, trgtSubj, rightVarIdx,
                1, 0, MAX_INT, ALL1, OutOfStatements.SKIP_VALIDATION, globalRightAnswers)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "globalRightAnswers are out of objectsRangeIdxs"

        where:
        subjIdxs | objectIdxs | inverse  | truePhrases | subjRange | objRange  | trgtSubj | rightVarIdx | rightAnswers
        [[0, 2]] | [[0, 2]]   | [[T, T]] | [[F, F]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | [[0, -1]]
        [[0, 2]] | [[0, 2]]   | [[T, T]] | [[F, F]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | [[0, 3]]
        [[0, 2]] | [[0, 2]]   | [[T, T]] | [[F, F]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | [[0, 99]]
    }

    ////
    // Only NumAnswersValidator

    def "should throw exception when 'statements.get(i).size() != truePhrases.get(i).size()'"() {
        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange,
                trgtSubj, rightVarIdx, 1, 0, MAX_INT, ALL1, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "statements.get(i).size() != truePhrases.get(i).size()"

        where:
        subjIdxs    | objectIdxs  | inverse     | truePhrases | subjRange | objRange  | trgtSubj | rightVarIdx | result
        [[0, 1, 2]] | [[0, 1, 2]] | [[T, T, F]] | [[F, T]]    | [0, 1, 2] | [0, 1, 2] | 0        | 0           | false
        [[0, 1]]    | [[0, 1, 2]] | [[T, T, F]] | [[F, T, T]] | [0, 1, 2] | [0, 1, 2] | 0        | 0           | false
        [[0, 1]]    | [[0, 1]]    | [[T, T, F]] | [[F, T, T]] | [0, 1, 2] | [0, 1, 2] | 0        | 0           | false
    }

    def "should throw exception when some statements out of 'subjectsRangeIdxs'"() {
        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange,
                trgtSubj, rightVarIdx, 1, 0, MAX_INT, ALL1, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "statement.subjectIdx is out of 'subjectsRangeIdxs'"

        where:
        subjIdxs    | objectIdxs  | inverse     | truePhrases | subjRange | objRange  | trgtSubj | rightVarIdx | result
        [[0, 1, 3]] | [[0, 1, 2]] | [[T, T, F]] | [[F, T, F]] | [0, 1, 2] | [0, 1, 2] | 0        | 0           | false
        [[0, 1, 3]] | [[0, 1, 2]] | [[T, T, F]] | [[F, T, F]] | [1, 2, 3] | [0, 1, 2] | 3        | 0           | false
    }

    def "should throw exception when some statements out of 'objectsRangeIdxs'"() {
        when:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, rightAnswersNum, trueStatementsNum, OutOfStatements.SKIP_VALIDATION).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "statement.objectIdx is out of 'objectsRangeIdxs'"

        where:
        objectIdxs | inverse            | rangeIdxs    | rightVarIdx | trueStatementsNum | rightAnswersNum | result
        [4]        | [true]             | [0, 1, 2, 3] | 0           | 1                 | 1               | false
        [2]        | [true]             | [0, 1, 6, 7] | 0           | 1                 | 1               | false
        [0, 4, 6]  | [true, true, true] | [0, 1, 6, 7] | 0           | 1                 | 1               | false
        [0, 5]     | [false, false]     | [0, 1, 6, 7] | 1           | 2                 | 2               | false
    }

    def "should throw exception on invalid 'targetSubjectIdx' input"() {
        when:
        !numAnswersExecutor.apply(subjIdxs, objectIdxs, inverse, truePhrases, subjRange, objRange,
                trgtSubj, rightVarIdx, 1, 0, MAX_INT, ALL1, OutOfStatements.SKIP_VALIDATION, Collections.emptyMap()).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage().startsWith("targetSubjectIdx should be contains in subjectsRangeIdxs:")

        where:
        subjIdxs    | objectIdxs  | inverse     | truePhrases | subjRange | objRange  | trgtSubj | rightVarIdx | result
        [[0, 1, 2]] | [[0, 1, 2]] | [[T, T, F]] | [[F, T, F]] | [0, 1, 2] | [0, 1, 2] | -1       | 0           | false
        [[0, 1, 2]] | [[0, 1, 2]] | [[T, T, F]] | [[F, T, F]] | [0, 1, 2] | [0, 1, 2] | 3        | 0           | false
    }

    def "should throw exception on invalid 'rightVariantIdx' input"() {
        when:
        !numAnswersExecutor.apply(objectIdxs, inverse, rangeIdxs, rightVarIdx, 1, 1, OutOfStatements.SKIP_VALIDATION).contains(!result)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage().startsWith("rightVariantIdx should be contains in objectsRangeIdxs: ")

        where:
        objectIdxs | inverse            | rangeIdxs    | rightVarIdx | result
        [0, 1, 2]  | [true, true, true] | [0, 1, 2, 3] | -1          | false
        [0, 1, 2]  | [true, true, true] | [0, 1, 2, 3] | -2          | false
        [0, 1, 2]  | [true, true, true] | [0, 1, 2, 3] | 4           | false
        [0, 1, 2]  | [true, true, true] | [0, 1, 2, 3] | 5           | false
    }

}
