package com.demidovn.fruitbounty.game.services.game.generating;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.model.singleplay.CompositeDescriptor;
import com.demidovn.fruitbounty.game.model.singleplay.CompositeSources;
import com.demidovn.fruitbounty.game.model.singleplay.ObjectsInfo;
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
public class CompositeGameConfig {
    private ObjectMapper objectMapper;

//    @Autowired
//    private MissionDescriptorValidator missionDescriptorValidator;

    @Autowired
    private ListUtil listUtil;

    @Autowired
    private QuestVerbsFormatter questVerbsFormatter;

    private CompositeSources compositeSources;
    private CompositeDescriptor compositeDescriptor;

    @PostConstruct
    public void postConstruct() throws IOException {
        createObjectMapper();
        CompositeSources compositeSources = parseMissionsDescriptor();
        CompositeDescriptor compositeDescriptor = parseCompositeDescriptor();

        compositeSources.setSubjects(listUtil.getImmutable(compositeSources.getSubjects()));
        compositeSources.setObjectsInfos(listUtil.getImmutable(compositeSources.getObjectsInfos()));

        for (int i = 0; i < compositeSources.getObjectsInfos().size(); i++) {
            ObjectsInfo objectsInfo = compositeSources.getObjectsInfos().get(i);
            objectsInfo.setObjects(listUtil.getImmutable(objectsInfo.getObjects()));

            objectsInfo.setVerbPositive(questVerbsFormatter.getPositiveVerb(objectsInfo));
            objectsInfo.setVerbNegative(questVerbsFormatter.getNegativeVerb(objectsInfo));
            objectsInfo.setVerb(null);
        }

        this.compositeSources = compositeSources;
        this.compositeDescriptor = compositeDescriptor;
    }

    public CompositeSources getCompositeSources() {
        return compositeSources;
    }

    public CompositeDescriptor getCompositeDescriptor() {
        return compositeDescriptor;
    }

    private void createObjectMapper() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
    }

    private CompositeSources parseMissionsDescriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.COMPOSITE_SOURCES_CONFIG);
        return objectMapper.readValue(input, CompositeSources.class);
    }

    private CompositeDescriptor parseCompositeDescriptor() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.COMPOSITE_CONFIG);
        return objectMapper.readValue(input, CompositeDescriptor.class);
    }

}
