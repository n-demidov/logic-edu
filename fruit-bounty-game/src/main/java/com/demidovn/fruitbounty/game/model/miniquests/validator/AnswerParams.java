package com.demidovn.fruitbounty.game.model.miniquests.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AnswerParams {
    public final int targetSubjectIdx;
    public final int rightVariantIdx;
}
