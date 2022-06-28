package com.demidovn.fruitbounty.game.services.game.generating.miniquests

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestGeneratorInfo
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers.BaseValidatorTest

import java.util.stream.Collectors
import java.util.stream.IntStream

class MiniquestEngineGeneratorTest extends BaseValidatorTest {
    MiniquestEngineGenerator miniquestEngineGenerator = new MiniquestEngineGenerator()

    def "should throw exceptions when 'info.verityAllocation.get(i).size() != info.statementsInSentence'"() {
        setup:
        int sentencesNum = 2
        Set<Integer> variantIdxs = IntStream.range(0, sentencesNum)
                .boxed()
                .collect(Collectors.toSet())

        MiniquestGeneratorInfo input = new MiniquestGeneratorInfo(
                table, variantIdxs, -1, true,
                OutOfStatements.SKIP_VALIDATION, 1, variantIdxs,
                sentencesNum, statementsInSentence, verityAllocation,
                ALL1, 0, 9, Collections.emptyMap())

        when:
        miniquestEngineGenerator.generateStatements(input)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "info.verityAllocation.get(i).size() != info.statementsInSentence"

        where:
        verityAllocation | statementsInSentence | table
        [[T, F], [F, F]] | 1                    | false
        [[T, F], [F, F]] | 3                    | false
    }

    def "should throw exceptions on invalid answerOutOfStatements"() {
        setup:
        Set<Integer> variantIdxs = IntStream.range(0, variantsNum)
                .boxed()
                .collect(Collectors.toSet())
        MiniquestGeneratorInfo input = new MiniquestGeneratorInfo(
                false, variantIdxs, statementsNum, true, answerOutOfStatements,
                rightAnswersNum, variantIdxs, 2, statementsNum,
                [[T, F], [F, F]], ALL1, 0, 9, Collections.emptyMap())

        when:
        miniquestEngineGenerator.generateStatements(input)

        then:
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "Too low statementsNum for STRICTLY_OUT_OF_STATEMENTS"

        where:
        statementsNum | variantsNum | rightAnswersNum | answerOutOfStatements
        2             | 6           | 1               | OUT
        2             | 7           | 1               | OUT
        2             | 7           | 2               | OUT
        2             | 8           | 3               | OUT
    }

}
