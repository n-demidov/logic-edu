package com.demidovn.fruitbounty.game.model.singleplay;

import com.demidovn.fruitbounty.game.services.game.VerbDescriptor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Composite1Descriptor implements VerbDescriptor, Serializable {
    private int version;

    private final String editorNote;
    private final String intro;

    // Standard
    private List<String> subjects;
    private String verb;
    private String verbPositive;
    private String verbNegative;
    private List<String> objects;
    private final int sentencesNumMin;
    private final int sentencesNumMax;
    private final int repeatsLimit;

}
