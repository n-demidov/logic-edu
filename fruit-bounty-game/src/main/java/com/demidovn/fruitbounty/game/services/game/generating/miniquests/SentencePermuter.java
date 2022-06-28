package com.demidovn.fruitbounty.game.services.game.generating.miniquests;

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;
import com.demidovn.fruitbounty.game.services.FastUtilAssistant;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Collections;
import java.util.List;

public class SentencePermuter {
    private static final int EMPTY_VALUE = -1;

    public void permuteIfNeed(List<MiniquestCondition> statementsInSentence) {
        Int2IntMap idxBySubject = new Int2IntOpenHashMap(statementsInSentence.size());
        Int2IntMap lastIdxBySubject = new Int2IntOpenHashMap(statementsInSentence.size());

        for (int i = 0; i < statementsInSentence.size(); i++) {
            MiniquestCondition statement = statementsInSentence.get(i);

            if (!statement.inverse) {
                idxBySubject.put(statement.subjectIdx, i);
            } else {
                if (idxBySubject.containsKey(statement.subjectIdx)) {
                    lastIdxBySubject.put(statement.subjectIdx, i);
                }
            }
        }

        ObjectIterator<Int2IntMap.Entry> it = FastUtilAssistant.entrySetIterator(idxBySubject);
        while (it.hasNext()) {
            Int2IntMap.Entry next = it.next();
            int subject = next.getIntKey();
            int firstSwapIndex = next.getIntValue();
            int secondSwapIndex = lastIdxBySubject.getOrDefault(subject, EMPTY_VALUE);
            if (secondSwapIndex == EMPTY_VALUE) {
                continue;
            }

            Collections.swap(statementsInSentence, firstSwapIndex, secondSwapIndex);
        }
    }

}
