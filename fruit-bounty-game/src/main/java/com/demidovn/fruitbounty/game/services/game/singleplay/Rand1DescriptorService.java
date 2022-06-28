package com.demidovn.fruitbounty.game.services.game.singleplay;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.model.singleplay.Composite1Descriptor;
import com.demidovn.fruitbounty.game.services.game.format.QuestVerbsFormatter;
import com.demidovn.fruitbounty.game.services.list.ListUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class Rand1DescriptorService {
    private ObjectMapper objectMapper;

//    @Autowired
//    private MissionDescriptorValidator missionDescriptorValidator;

    @Autowired
    private ListUtil listUtil;

    @Autowired
    private QuestVerbsFormatter questVerbsFormatter;

    private Composite1Descriptor randDescriptor;

    @PostConstruct
    public void postConstruct() throws IOException {
        createObjectMapper();
        Composite1Descriptor compositeDescriptor = parseMissionsDescriptor();

        // May add validation

        compositeDescriptor.setVerbPositive(questVerbsFormatter.getPositiveVerb(compositeDescriptor));
        compositeDescriptor.setVerbNegative(questVerbsFormatter.getNegativeVerb(compositeDescriptor));
        compositeDescriptor.setVerb(null);

        compositeDescriptor.setObjects(listUtil.getImmutable(compositeDescriptor.getObjects()));
        compositeDescriptor.setSubjects(listUtil.getImmutable(compositeDescriptor.getSubjects()));

        this.randDescriptor = compositeDescriptor;
    }

    public Composite1Descriptor getDescriptor() {
        return randDescriptor;
    }

    private void createObjectMapper() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
    }

    private Composite1Descriptor parseMissionsDescriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.COMPOSITE1_CONFIG);
        return objectMapper.readValue(input, Composite1Descriptor.class);
    }

}
