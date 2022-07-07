package com.demidovn.fruitbounty.game.model.singleplay;

import com.demidovn.fruitbounty.game.services.game.VerbDescriptor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Composite1Descriptor implements VerbDescriptor, Serializable {
    private int version;

    private String editorNote;
    private String intro;

    // Standard
    private List<String> subjects;
    private String verb;
    private String verbPositive;
    private String verbNegative;
    private List<String> objects;
    private int sentencesNumMin;
    private int sentencesNumMax;
    private int repeatsLimit;

}
