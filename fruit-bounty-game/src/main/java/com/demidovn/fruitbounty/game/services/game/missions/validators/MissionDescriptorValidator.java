package com.demidovn.fruitbounty.game.services.game.missions.validators;

import com.demidovn.fruitbounty.game.model.missions.MissionDescriptor;
import com.demidovn.fruitbounty.game.model.missions.MissionType;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class MissionDescriptorValidator {

    public void validVerbsFilling(MissionDescriptor missionDescriptor) {
        Objects.requireNonNull(missionDescriptor.getSubjects());

        if (missionDescriptor.getType() == MissionType.EDU || missionDescriptor.getType() == MissionType.TABLE_2) {
            return;
        }

        boolean verbIsEmpty = Strings.isNullOrEmpty(missionDescriptor.getVerb());
        boolean positiveVerbIsEmpty = Strings.isNullOrEmpty(missionDescriptor.getVerbPositive());
        boolean negativeVerbIsEmpty = Strings.isNullOrEmpty(missionDescriptor.getVerbNegative());

        if ((!verbIsEmpty && (!positiveVerbIsEmpty || !negativeVerbIsEmpty)) ||
                (verbIsEmpty && (positiveVerbIsEmpty || negativeVerbIsEmpty))) {
            log.error("Illegal verbs filling for missionNumber: {}; mission={}", missionDescriptor.getMissionNumber(), missionDescriptor);
            throw new IllegalArgumentException("Illegal verbs filling for mission: " + missionDescriptor);
        }
    }

    public void validBaseFields(MissionDescriptor missionDescriptor) {
        int trueStatementsNum = missionDescriptor.getTrueStatementsNum();
        int allStatementsNum = missionDescriptor.getSentencesNum() * missionDescriptor.getStatementsInSentence();
        if ((trueStatementsNum < -1 || trueStatementsNum > allStatementsNum) && missionDescriptor.getType() == MissionType.STANDARD && !missionDescriptor.isTable()) {
            throw new IllegalArgumentException("Invalid 'trueStatementsNum': " + trueStatementsNum);
        }

        if (missionDescriptor.getType() != MissionType.EDU && !missionDescriptor.isTable() && missionDescriptor.getType() != MissionType.TABLE_2) {
            if (trueStatementsNum == -1 && missionDescriptor.getVerityAllocation() == null) {
                throw new IllegalArgumentException("Empty 'trueStatementsNum' and 'VerityAllocation'");
            } else if (trueStatementsNum != -1 && missionDescriptor.getVerityAllocation() != null) {
                throw new IllegalArgumentException("Filled both 'trueStatementsNum' and 'VerityAllocation'");
            }
        }
    }

}
