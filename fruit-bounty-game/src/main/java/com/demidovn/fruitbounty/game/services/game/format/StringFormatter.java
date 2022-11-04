package com.demidovn.fruitbounty.game.services.game.format;

import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.generating.CommonSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StringFormatter {
    private static final int NOT_FOUND_FIRST_DUPLICATE = -1;

    @Autowired
    private CommonSources commonSources;

    private static final Randomizer rand = new Randomizer();

    public String joinList(List<String> subjects) {
        return joinList(subjects, commonSources.getCommonSources().getAnd());
    }

    public String joinList(List<String> subjects, String lastConnector) {
        if (lastConnector == null) {
            lastConnector = commonSources.getCommonSources().getAnd();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subjects.size() - 1; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            String subject = subjects.get(i);
            sb.append(subject);
        }

        if (subjects.size() > 1) {
            sb.append(" ");
            sb.append(lastConnector);
            sb.append(" ");
        }

        String subject = subjects.get(subjects.size() - 1);
        sb.append(subject);

        return sb.toString();
    }

    public String replaceStatements(List<String> statements, List<String> witnessesNames) {
        StringBuilder sb = new StringBuilder();

        int idx = 0;
        for (int i = 0; i < statements.size(); i++) {
            String statement = statements.get(i);
            sb.append("• ");
            sb.append(witnessesNames.get(idx));

            sb.append(": «");

            addAgreementWordIfNeed(statements, i, witnessesNames, sb);

            sb.append(statement);
            sb.append("»<br>");
            idx++;
        }

        return sb.toString();
    }

    private void addAgreementWordIfNeed(List<String> statements, int currentStatementIdx, List<String> names, StringBuilder sb) {
        if (isPreviousStatementEquals(currentStatementIdx, statements) ) {
            String agreementWord = rand.getRandomElement(commonSources.getAgreementWords());
            sb.append(agreementWord);
            sb.append(", ");
            return;
        }

        int firstDuplicateIdx = getFirstDuplicateIdx(currentStatementIdx, statements);
        if (firstDuplicateIdx != NOT_FOUND_FIRST_DUPLICATE) {
            String name = names.get(firstDuplicateIdx);
            sb.append(name);
            sb.append(" ");
            sb.append(commonSources.getCommonSources().getRight());
            sb.append(": ");
        }
    }

    private boolean isPreviousStatementEquals(int currentStatementIdx, List<String> statements) {
        if (currentStatementIdx == 0) {
            return false;
        }

        return statements.get(currentStatementIdx).equals(statements.get(currentStatementIdx - 1));
    }

    private int getFirstDuplicateIdx(int currentStatementIdx, List<String> statements) {
        String currentStatement = statements.get(currentStatementIdx);
        for (int i = 0; i < currentStatementIdx; i++) {
            String statement = statements.get(i);
            if (statement.equals(currentStatement)) {
                return i;
            }
        }

        return NOT_FOUND_FIRST_DUPLICATE;
    }

}
