package com.demidovn.fruitbounty.game.model.singleplay;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Composite2Descriptor implements Serializable {
    private int version;

    private String editorNote;
    private String intro;

    // Standard
    private List<String> subjects;
    private String verb;
    private String plusVerbPositive;
    private String plusVerbNegative;
    private List<String> minusVerbPositive;
    private List<String> minusVerbNegative;

    private List<String> objects;
    private int statementsNumMin;
    private int statementsNumMax;
    private int repeatsLimit;
    private boolean contradictionsEnabled;
    private int answerOutOfStatementsChance;

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
