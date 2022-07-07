package com.demidovn.fruitbounty.game.services.game.missions;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.model.missions.MissionDescriptor;
import com.demidovn.fruitbounty.game.model.missions.MissionsDescriptor;
import com.demidovn.fruitbounty.game.services.game.format.QuestVerbsFormatter;
import com.demidovn.fruitbounty.game.services.game.missions.validators.MissionDescriptorValidator;
import com.demidovn.fruitbounty.game.services.list.ListUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MissionsService {
    private ObjectMapper objectMapper;

    @Autowired
    private MissionDescriptorValidator missionDescriptorValidator;

    @Autowired
    private QuestVerbsFormatter questVerbsFormatter;

    @Autowired
    private ListUtil listUtil;

    private MissionsDescriptor missionsDescriptor;

    @PostConstruct
    public void postConstruct() throws IOException {
        createObjectMapper();
        MissionsDescriptor missionsDescriptor = parseMissionsDescriptor();

        Map<Integer, MissionDescriptor> missionsByLevel = new HashMap<>();
        for (int i = 0; i < missionsDescriptor.getMissions().size(); i++) {
            MissionDescriptor missionDescriptor = missionsDescriptor.getMissions().get(i);
            missionDescriptorValidator.validBaseFields(missionDescriptor);
            missionDescriptorValidator.validVerbsFilling(missionDescriptor);

            int missionNumber = i + 1;
            missionDescriptor.setMissionNumber(missionNumber);

            missionDescriptor.setVerbPositive(questVerbsFormatter.getPositiveVerb(missionDescriptor));
            missionDescriptor.setVerbNegative(questVerbsFormatter.getNegativeVerb(missionDescriptor));
            missionDescriptor.setVerb(null);

            missionDescriptor.setObjects(listUtil.getImmutable(missionDescriptor.getObjects()));
            missionDescriptor.setSubjects(listUtil.getImmutable(missionDescriptor.getSubjects()));

            if (missionDescriptor.getVerityAllocation() == null) {
                setVerityAllocation(missionDescriptor);
            }

            missionsByLevel.put(missionNumber, missionDescriptor);
        }
        missionsDescriptor.setMissionsByLevel(missionsByLevel);

        this.missionsDescriptor = missionsDescriptor;
    }

    public MissionDescriptor getMissionsDescriptor(int mission) {
        return missionsDescriptor.missionsByLevel().get(mission);
    }

    private void createObjectMapper() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
    }

    private MissionsDescriptor parseMissionsDescriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.MISSIONS_CONFIG);
        return objectMapper.readValue(input, MissionsDescriptor.class);
    }

    private void setVerityAllocation(MissionDescriptor missionDescriptor) {
        int size = missionDescriptor.getSentencesNum();
        List<List<Boolean>> truePhrases = new ArrayList<>(size);
        int counter = 0;
        for (int i = 0; i < size; i++) {
            List<Boolean> sentenceTruePhrases = new ArrayList<>(1);
            sentenceTruePhrases.add(counter < missionDescriptor.getTrueStatementsNum());
            truePhrases.add(sentenceTruePhrases);

            counter++;
        }

        missionDescriptor.setVerityAllocation(truePhrases);
    }

}
