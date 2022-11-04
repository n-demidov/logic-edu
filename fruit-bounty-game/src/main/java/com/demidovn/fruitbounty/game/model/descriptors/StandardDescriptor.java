package com.demidovn.fruitbounty.game.model.descriptors;

import com.demidovn.fruitbounty.game.model.quest.AskAbout;
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.services.game.VerbDescriptor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StandardDescriptor implements VerbDescriptor, Serializable {

    private DescriptorType type;

    private String editorNote;
    private String intro;
    private AskAbout askAbout = AskAbout.OBJECT;

    private boolean table;

    // Standard
    private List<String> subjects;
    private String lastSubjectConnector;
    private String verb;
    private String verbPositive;
    private String verbNegative;
    private List<String> objects;
    private Integer minObjectsNum;
    private Integer maxObjectsNum;
    private Integer minSubjectsAndObjectsNum;
    private Integer maxSubjectsAndObjectsNum;
    private String lastObjectConnector;

    private Integer sentencesNum;
    private Integer sentencesNumMin;
    private Integer sentencesNumMax;
    private int statementsInSentence = 1;

    private List<List<Boolean>> verityAllocation;
    private int trueStatementsNum = -1;

    private int repeatsLimit;
    private Object contradictionsEnabled = false; // boolean or percents (in int)

    private int rightAnswersNum = 1;
    private NonTargetAnswers nonTargetAnswers = NonTargetAnswers.SKIP_VALIDATION;
    private OutOfStatements answerOutOfStatements = OutOfStatements.SKIP_VALIDATION;  // Is answer there in statements or not
    private int minAnswerInfers;
    private int maxAnswerInfers = Integer.MAX_VALUE;

    // Table-2
    private List<String> verbMany;
    private List<List<String>> objectsMany;
    private String demonstrativePronoun;

    // Edu
    private String rightAnswer;
    private String solution;

}
