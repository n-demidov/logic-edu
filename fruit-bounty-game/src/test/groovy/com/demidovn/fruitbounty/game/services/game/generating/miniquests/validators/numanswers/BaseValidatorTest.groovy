package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers

import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements
import spock.lang.Specification

abstract class BaseValidatorTest extends Specification {
    protected static final int MAX_INT = Integer.MAX_VALUE
    protected static final boolean T = true
    protected static final boolean F = false
    protected static final NonTargetAnswers SKIP = NonTargetAnswers.SKIP_VALIDATION;
    protected static final NonTargetAnswers MEN1 = NonTargetAnswers.ONLY_ONE_ANSWER_FOR_MENTIONED_IN_STATEMENTS;
    protected static final NonTargetAnswers ALL1 = NonTargetAnswers.ONLY_ONE_ANSWER_FOR_ALL;
    protected static final OutOfStatements ANY = OutOfStatements.SKIP_VALIDATION
    protected static final OutOfStatements IN = OutOfStatements.STRICTLY_IN_STATEMENTS
    protected static final OutOfStatements OUT = OutOfStatements.STRICTLY_OUT_OF_STATEMENTS

}
