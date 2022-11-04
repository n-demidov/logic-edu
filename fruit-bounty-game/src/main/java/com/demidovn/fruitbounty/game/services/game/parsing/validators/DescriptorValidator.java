package com.demidovn.fruitbounty.game.services.game.parsing.validators;

import com.demidovn.fruitbounty.game.model.descriptors.DescriptorType;
import com.demidovn.fruitbounty.game.model.descriptors.StandardDescriptor;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class DescriptorValidator {

    public void validBaseFields(StandardDescriptor descriptor) {
        if (descriptor.getType() == null) {
            throw new IllegalArgumentException("Type is null");
        }

        if (descriptor.getContradictionsEnabled() == null
                || !(descriptor.getContradictionsEnabled() instanceof Boolean || descriptor.getContradictionsEnabled() instanceof Integer)) {
            throw new IllegalArgumentException("ContradictionsEnabled incorrect: " + descriptor.getContradictionsEnabled());
        }

        if (descriptor.getType() != DescriptorType.MISSION_EDU && !descriptor.isTable()
                && descriptor.getType() != DescriptorType.MISSION_TABLE_2) {

            if ((descriptor.getMinObjectsNum() == null && descriptor.getMaxObjectsNum() != null)
                    || descriptor.getMinObjectsNum() != null && descriptor.getMaxObjectsNum() == null) {
                throw new IllegalArgumentException("Illegal values for MinObjectsNum and MaxObjectsNum");
            }

            if (descriptor.getSentencesNum() != null
                    && (descriptor.getSentencesNumMin() != null || descriptor.getSentencesNumMax() != null)) {
                throw new IllegalArgumentException("sentencesNum filled but sentencesNumMin/sentencesNumMax are filled too.");
            } else if (descriptor.getSentencesNum() == null
                    && (descriptor.getSentencesNumMin() == null || descriptor.getSentencesNumMax() == null)) {
                throw new IllegalArgumentException("sentencesNum empty but sentencesNumMin and sentencesNumMax are not filled too.");
            }

            if (descriptor.getSentencesNumMin() != null || descriptor.getSentencesNumMax() != null) {
                if (descriptor.getVerityAllocation() != null) {
                    throw new IllegalArgumentException("It seems that VerityAllocation should be evaluated dynamically...");
                }
            }

            int trueStatementsNum = descriptor.getTrueStatementsNum();
            int allStatementsNum = getMinSentencesNum(descriptor) * descriptor.getStatementsInSentence();
            if ((trueStatementsNum < -1 || trueStatementsNum > allStatementsNum)
                    && descriptor.getType() == DescriptorType.MISSION_STANDARD && !descriptor.isTable()) {
                throw new IllegalArgumentException("Invalid 'trueStatementsNum': " + trueStatementsNum);
            }

            if (trueStatementsNum == -1 && descriptor.getVerityAllocation() == null) {
                throw new IllegalArgumentException("Empty 'trueStatementsNum' and 'VerityAllocation'");
            } else if (trueStatementsNum != -1 && descriptor.getVerityAllocation() != null) {
                throw new IllegalArgumentException("Filled both 'trueStatementsNum' and 'VerityAllocation'");
            }

        }
    }

    public void validVerbsFilling(StandardDescriptor descriptor) {
        Objects.requireNonNull(descriptor.getSubjects());

        if (descriptor.getType() == DescriptorType.MISSION_EDU
                || descriptor.getType() == DescriptorType.MISSION_TABLE_2) {
            return;
        }

        boolean verbIsEmpty = Strings.isNullOrEmpty(descriptor.getVerb());
        boolean positiveVerbIsEmpty = Strings.isNullOrEmpty(descriptor.getVerbPositive());
        boolean negativeVerbIsEmpty = Strings.isNullOrEmpty(descriptor.getVerbNegative());

        if ((!verbIsEmpty && (!positiveVerbIsEmpty || !negativeVerbIsEmpty)) ||
                (verbIsEmpty && (positiveVerbIsEmpty || negativeVerbIsEmpty))) {
            log.error("Illegal verbs filling for descriptor: {}", descriptor);
            throw new IllegalArgumentException("Illegal verbs filling for descriptor: " + descriptor);
        }
    }

    private int getMinSentencesNum(StandardDescriptor descriptor) {
        if (descriptor.getSentencesNum() != null) {
            return descriptor.getSentencesNum();
        }
        return descriptor.getSentencesNumMin();
    }

}
