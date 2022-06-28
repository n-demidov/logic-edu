package com.demidovn.fruitbounty.game.services.game.generating.tablequests;

import com.demidovn.fruitbounty.game.model.tablequests.CellInfo;
import com.demidovn.fruitbounty.game.model.tablequests.TableQuestInfo2;
import com.demidovn.fruitbounty.game.model.tablequests.TableQuestResult;
import com.demidovn.fruitbounty.game.services.Randomizer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The 2-nd table quest
 */
@Component
public class TableQuestGenerator2 {
    private static final int PARAMS_NUM = 3;
    private static final int OBJECTS_LIST_NUM = 2;
    private static final int MIN_PARAM = 0;
    private static final int MAX_PARAM = 2;

    private final Randomizer rand = new Randomizer();
    private final List<CellInfo> staticStatementsIdxs = new ArrayList<>(4);
    private final List<CellInfo> table2RightIdxs = new ArrayList<>(PARAMS_NUM);

    @PostConstruct
    public void init() {
        // table 1
        staticStatementsIdxs.add(new CellInfo(0, 0, CellInfo.SUBJECT_ID, 0));
        staticStatementsIdxs.add(new CellInfo(1, 2, CellInfo.SUBJECT_ID, 0));
        staticStatementsIdxs.add(new CellInfo(2, 1, CellInfo.SUBJECT_ID, 0));

        // table 3
        staticStatementsIdxs.add(new CellInfo(2, 1, CellInfo.SUBJECT_ID, 1));

        // table 2
        table2RightIdxs.add(new CellInfo(0, 1, 0, 1, true));
        table2RightIdxs.add(new CellInfo(1, 0, 0, 1, true));
        table2RightIdxs.add(new CellInfo(2, 2, 0, 1, true));
    }

    public TableQuestResult generateStatements(TableQuestInfo2 questInfo) {
        validate(questInfo);

        List<CellInfo> dynamicIdxs = generateDynamicIdxs();
        List<CellInfo> allStatementsIdxs = generateAllStatementsTemplate(dynamicIdxs);

        List<String> statements = generateStatements(questInfo, allStatementsIdxs);

        int subjectRightAnswerIdx = getSubjectRightAnswerIdx();
//        int objectRightAnswerIdx = getObjectRightAnswerIdx(subjectRightAnswerIdx);
        String rightAnswer = questInfo.getObjectsMany().get(1).get(subjectRightAnswerIdx);
        String rightAnswerSubject = questInfo.getSubjects().get(subjectRightAnswerIdx);

        rand.shuffle(statements);
        rand.shuffle(questInfo.getSubjects());
        questInfo.getObjectsMany().forEach(rand::shuffle);

        return new TableQuestResult(statements, rightAnswer, rightAnswerSubject);
    }

    private List<CellInfo> generateDynamicIdxs() {
        CellInfo firstRandomElement = rand.getRandomElement(table2RightIdxs);
        CellInfo secondRandomElement = generateNegativeFor2Table(firstRandomElement);
        return Arrays.asList(firstRandomElement, secondRandomElement);
    }

    private CellInfo generateNegativeFor2Table(CellInfo firstRandomElement) {
        CellInfo result;
        do {
            int randomX = rand.generateFromRange(MIN_PARAM, MAX_PARAM);
            int randomY = rand.generateFromRange(MIN_PARAM, MAX_PARAM);
            result = new CellInfo(randomX, randomY, 0, 1);
        } while (result.getRow() == firstRandomElement.getRow() || result.getCol() == firstRandomElement.getCol());

        return result;
    }

    private List<CellInfo> generateAllStatementsTemplate(
            List<CellInfo> partIdxs) {
        List<CellInfo> allStatementsTemplate = new ArrayList<>(staticStatementsIdxs.size() + partIdxs.size());

        allStatementsTemplate.addAll(staticStatementsIdxs);
        allStatementsTemplate.addAll(partIdxs);

        return allStatementsTemplate;
    }

    private List<String> generateStatements(TableQuestInfo2 questInfo, List<CellInfo> allStatementsIdxs) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (CellInfo cellInfo : allStatementsIdxs) {
            sb.setLength(0);

            String subject;
            if (cellInfo.isXSubject()) {
                subject = questInfo.getSubjects().get(cellInfo.getRow());
                sb.append(subject);
            } else {
                sb.append(questInfo.getThirdPersonSex());
                sb.append(" кто ");
                sb.append(questInfo.getVerbs().get(cellInfo.getRowRef()));
                sb.append(" ");
                subject = questInfo.getObjectsMany().get(cellInfo.getRowRef()).get(cellInfo.getRow());
                sb.append(subject);
            }

            if (cellInfo.isPositive()) {
                sb.append(" ");
            } else {
                sb.append(" не ");
            }

            sb.append(questInfo.getVerbs().get(cellInfo.getColRef()));
            sb.append(" ");

            String object = questInfo.getObjectsMany().get(cellInfo.getColRef()).get(cellInfo.getCol());
            sb.append(object);

            result.add(sb.toString());
        }

        return result;
    }

    private int getSubjectRightAnswerIdx() {
        return rand.generateFromRange(MIN_PARAM, MAX_PARAM);
    }

//    private int getObjectRightAnswerIdx(int subjectRightAnswerIdx) {
//        if (subjectRightAnswerIdx == 0) {
//            return 0;
//        } else if (subjectRightAnswerIdx == 1) {
//            return 1;
//        } else {
//            return 3;
//        }
//    }

    private void validate(TableQuestInfo2 questInfo) {
        if (questInfo.getSubjects().size() != PARAMS_NUM) {
            throw new IllegalStateException("questInfo Subjects not equals to " + PARAMS_NUM);
        }

        if (questInfo.getObjectsMany().size() != OBJECTS_LIST_NUM) {
            throw new IllegalStateException("questInfo Objects list not equals to " + OBJECTS_LIST_NUM);
        }

        for (List<String> objects : questInfo.getObjectsMany()) {
            if (objects.size() != PARAMS_NUM) {
                throw new IllegalStateException("questInfo Objects not equals to " + PARAMS_NUM);
            }
        }
    }

}
