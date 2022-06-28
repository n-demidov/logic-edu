package com.demidovn.fruitbounty.game.model.missions;

import com.demidovn.fruitbounty.game.model.quest.AskAbout;
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.services.game.VerbDescriptor;
import lombok.Data;

import java.util.List;

@Data
public class MissionDescriptor implements VerbDescriptor {

    private int missionNumber;
    private final MissionType type;
    private final String editorNote;
    private final String intro;
    private AskAbout askAbout = AskAbout.OBJECT;

    private final boolean table;

    // Standard
    private List<String> subjects;
    private String verb;
    private String verbPositive;
    private String verbNegative;
    private List<String> objects;

    private final int sentencesNum;
    private int statementsInSentence = 1;

    private List<List<Boolean>> verityAllocation;
    private int trueStatementsNum = -1;

    private final int repeatsLimit;
    private final boolean contradictionsEnabled;

    private int rightAnswersNum = 1;
    private NonTargetAnswers nonTargetAnswers = NonTargetAnswers.SKIP_VALIDATION;
    private OutOfStatements answerOutOfStatements = OutOfStatements.SKIP_VALIDATION;  // Is answer there in statements or not
    private final int minAnswerInfers;
    private int maxAnswerInfers = Integer.MAX_VALUE;

    // Table-2
    private List<String> verbMany;
    private List<List<String>> objectsMany;
    private String demonstrativePronoun;

    // Edu
    private final String rightAnswer;
    private final String solution;

}
