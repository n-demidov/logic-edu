package com.demidovn.fruitbounty.game.services.game.converters;

import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class NamesConverters {

    public Map<String, List<String>> convert2Strings(Map<Integer, ValidResult> objectsBySubjectsIdxs, List<String> subjects, List<String> objects) {
        Map<String, List<String>> result = new HashMap<>(objectsBySubjectsIdxs.size());
        Iterator<Map.Entry<Integer, ValidResult>> it = objectsBySubjectsIdxs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, ValidResult> next = it.next();
            int subjectIdx = next.getKey();
            ValidResult validResult = next.getValue();
            List<String> tempObjects = new ArrayList<>(validResult.objects.size());
            Iterator<Integer> it2 = validResult.objects.iterator();
            while (it2.hasNext()) {
                int objectIdx = it2.next();
                tempObjects.add(getString(objectIdx, objects));
            }
            result.put(getString(subjectIdx, subjects), tempObjects);
        }
        return result;
    }

    public String getString(int idx, List<String> strings) {
        return strings.get(idx);
    }
}
