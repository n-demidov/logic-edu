package com.demidovn.fruitbounty.game.services.game.parsing;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.model.descriptors.MissionDescriptor;
import com.demidovn.fruitbounty.game.model.descriptors.MissionsDescriptor;
import com.demidovn.fruitbounty.game.model.descriptors.StandardDescriptor;
import com.demidovn.fruitbounty.game.services.game.format.QuestVerbsFormatter;
import com.demidovn.fruitbounty.game.services.game.generating.CommonSources;
import com.demidovn.fruitbounty.game.services.game.parsing.validators.DescriptorValidator;
import com.demidovn.fruitbounty.game.services.list.ListUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DescriptorService {

    @Autowired
    private DescriptorValidator descriptorValidator;

    @Autowired
    private QuestVerbsFormatter questVerbsFormatter;

    @Autowired
    private ListUtil listUtil;

    @Autowired
    private CommonSources commonSources;

    private ObjectMapper objectMapper;
    private MissionsDescriptor missionsDescriptor;
    private StandardDescriptor brief1Descriptor;
    private StandardDescriptor brief2Descriptor;
    private StandardDescriptor brief3Descriptor;
    private StandardDescriptor table1Descriptor;
    private StandardDescriptor table2Descriptor;
    private StandardDescriptor table3Descriptor;
    private StandardDescriptor table3_2Descriptor;
    private StandardDescriptor brief100Descriptor;

    @PostConstruct
    public void postConstruct() throws IOException {
        createObjectMapper();
        MissionsDescriptor missionsDescriptor = parseMissionsDescriptor();
        StandardDescriptor brief1Descriptor = parseBrief1Descriptor();
        StandardDescriptor brief2Descriptor = parseBrief2Descriptor();
        StandardDescriptor brief3Descriptor = parseBrief3Descriptor();
        StandardDescriptor table1Descriptor = parseTable1Descriptor();
        StandardDescriptor table2Descriptor = parseTable2Descriptor();
        StandardDescriptor table3Descriptor = parseTable3Descriptor();
        StandardDescriptor table3_2Descriptor = parseTable3_2Descriptor();
        StandardDescriptor brief100Descriptor = parseBrief100Descriptor();

        Map<Integer, MissionDescriptor> missionsByLevel = new HashMap<>();
        for (int i = 0; i < missionsDescriptor.getMissions().size(); i++) {
            MissionDescriptor missionDescriptor = missionsDescriptor.getMissions().get(i);

            validStandardFields(missionDescriptor);
            setStandardFields(missionDescriptor);

            int missionNumber = i + 1;
            missionDescriptor.setMissionNumber(missionNumber);

            missionsByLevel.put(missionNumber, missionDescriptor);
        }
        missionsDescriptor.setMissionsByLevel(missionsByLevel);

        validStandardFields(brief1Descriptor);
        setStandardFields(brief1Descriptor);

        validStandardFields(table1Descriptor);
        setStandardFields(table1Descriptor);

        validStandardFields(table2Descriptor);
        setStandardFields(table2Descriptor);

        validStandardFields(table3Descriptor);
        setStandardFields(table3Descriptor);

        validStandardFields(table3_2Descriptor);
        setStandardFields(table3_2Descriptor);

        validStandardFields(brief2Descriptor);
        setStandardFields(brief2Descriptor);

        validStandardFields(brief3Descriptor);
        setStandardFields(brief3Descriptor);

        validStandardFields(brief100Descriptor);
        setStandardFields(brief100Descriptor);

        this.missionsDescriptor = missionsDescriptor;
        this.brief1Descriptor = brief1Descriptor;
        this.table1Descriptor = table1Descriptor;
        this.table2Descriptor = table2Descriptor;
        this.table3Descriptor = table3Descriptor;
        this.table3_2Descriptor = table3_2Descriptor;
        this.brief2Descriptor = brief2Descriptor;
        this.brief3Descriptor = brief3Descriptor;
        this.brief100Descriptor = brief100Descriptor;
    }

    private void validStandardFields(StandardDescriptor descriptor) {
        descriptorValidator.validBaseFields(descriptor);
        descriptorValidator.validVerbsFilling(descriptor);
    }

    private void setStandardFields(StandardDescriptor descriptor) {
        descriptor.setVerbPositive(questVerbsFormatter.getPositiveVerb(descriptor));
        descriptor.setVerbNegative(questVerbsFormatter.getNegativeVerb(descriptor));
        descriptor.setVerb(null);

        descriptor.setObjects(listUtil.getImmutable(descriptor.getObjects()));
        descriptor.setSubjects(listUtil.getImmutable(descriptor.getSubjects()));

        if (descriptor.getLastObjectConnector() == null) {
            descriptor.setLastObjectConnector(commonSources.getCommonSources().getAnd());
        }
    }

    public MissionDescriptor getMissionsDescriptor(int mission) {
        return missionsDescriptor.missionsByLevel().get(mission);
    }

    public StandardDescriptor getBrief1Descriptor() {
        return brief1Descriptor;
    }

    public StandardDescriptor getTable1Descriptor() {
        return table1Descriptor;
    }

    public StandardDescriptor getTable2Descriptor() {
        return table2Descriptor;
    }

    public StandardDescriptor getTable3Descriptor() {
        return table3Descriptor;
    }

    public StandardDescriptor getTable3_2Descriptor() {
        return table3_2Descriptor;
    }

    public StandardDescriptor getBrief2Descriptor() {
        return brief2Descriptor;
    }

    public StandardDescriptor getBrief3Descriptor() {
        return brief3Descriptor;
    }

    public StandardDescriptor getBrief100Descriptor() {
        return brief100Descriptor;
    }

    private void createObjectMapper() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
    }

    private MissionsDescriptor parseMissionsDescriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.MISSIONS_CONFIG);
        return objectMapper.readValue(input, MissionsDescriptor.class);
    }

    private StandardDescriptor parseBrief1Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.BRIEF_1_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseTable1Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.TABLE_1_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseTable2Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.TABLE_2_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseTable3Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.TABLE_3_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseTable3_2Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.TABLE_3_2_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseBrief2Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.BRIEF_2_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseBrief3Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.BRIEF_3_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

    private StandardDescriptor parseBrief100Descriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.BRIEF_100_CONFIG);
        return objectMapper.readValue(input, StandardDescriptor.class);
    }

}
