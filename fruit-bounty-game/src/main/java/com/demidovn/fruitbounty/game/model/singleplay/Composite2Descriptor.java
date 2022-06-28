package com.demidovn.fruitbounty.game.model.singleplay;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Composite2Descriptor implements Serializable {
    private int version;

    private final String editorNote;
    private final String intro;

    // Standard
    private List<String> subjects;
    private String verb;
    private String plusVerbPositive;
    private String plusVerbNegative;
    private List<String> minusVerbPositive;
    private List<String> minusVerbNegative;

    private List<String> objects;
    private final int statementsNumMin;
    private final int statementsNumMax;
    private final int repeatsLimit;
    private final boolean contradictionsEnabled;
    private final int answerOutOfStatementsChance;

    // Single-2
    private List<String> hooksVerbs;
    private List<String> minusHooks;
    private List<String> plusHooks;
    private String miniquestIntro;
    private String positiveMiniquestIntro;
    private String negativeMiniquestIntro;
    private String positiveMiniquestIntro2;
    private String negativeMiniquestIntro2;

}
