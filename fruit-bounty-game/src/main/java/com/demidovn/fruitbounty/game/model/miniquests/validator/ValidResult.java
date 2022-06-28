package com.demidovn.fruitbounty.game.model.miniquests.validator;

import lombok.Data;

import java.util.Set;

@Data
public class ValidResult {
    public final Set<Integer> objects;
    public int sharedMinAnswerInfers;

    public ValidResult(Set<Integer> objects, int sharedMinAnswerInfers) {
        this.objects = objects;
        this.sharedMinAnswerInfers = sharedMinAnswerInfers;
    }
}
