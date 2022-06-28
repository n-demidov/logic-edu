package com.demidovn.fruitbounty.game.services.game.format;

import com.demidovn.fruitbounty.game.services.game.VerbDescriptor;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

@Component
public class QuestVerbsFormatter {

    public String getPositiveVerb(VerbDescriptor missionsDesc) {
        if (!Strings.isNullOrEmpty(missionsDesc.getVerb())) {
            return missionsDesc.getVerb();
        }

        return missionsDesc.getVerbPositive();
    }

    public String getNegativeVerb(VerbDescriptor missionsDesc) {
        if (!Strings.isNullOrEmpty(missionsDesc.getVerb())) {
            return "не " + missionsDesc.getVerb();
        }

        return missionsDesc.getVerbNegative();
    }

}
