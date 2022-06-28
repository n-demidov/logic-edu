package com.demidovn.fruitbounty.game.services.game.generating;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.services.Randomizer;
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
import java.util.List;

@Slf4j
@Component
public class CommonSources {
    private ObjectMapper objectMapper;

    @Autowired
    private ListUtil listUtil;

    private com.demidovn.fruitbounty.game.model.sources.CommonSources commonSources;
    private static final Randomizer rand = new Randomizer();

    @PostConstruct
    public void postConstruct() throws IOException {
        createObjectMapper();
        com.demidovn.fruitbounty.game.model.sources.CommonSources commonSources = parseCommonSources();

        commonSources.setAgreementWords(listUtil.getImmutable(commonSources.getAgreementWords()));
        commonSources.setMenNames(listUtil.getImmutable(commonSources.getMenNames()));
        commonSources.setEngSurnames(listUtil.getImmutable(commonSources.getEngSurnames()));

        this.commonSources = commonSources;
    }

    public List<String> generateMenNames(int size) {
        List<String> result = new ArrayList<>(commonSources.getMenNames());

        rand.shuffle(result);
        result = result.subList(0, size);

        return result;
    }

    public List<String> generateEngSurnames(int size) {
        List<String> result = new ArrayList<>(commonSources.getEngSurnames());

        rand.shuffle(result);
        result = result.subList(0, size);

        return result;
    }

    public com.demidovn.fruitbounty.game.model.sources.CommonSources getCommonSources() {
        return commonSources;
    }

    public List<String> getAgreementWords() {
        return commonSources.getAgreementWords();
    }

    private void createObjectMapper() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
    }

    private com.demidovn.fruitbounty.game.model.sources.CommonSources parseCommonSources() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(GameOptions.COMMON_SOURCES_CONFIG);
        return objectMapper.readValue(input, com.demidovn.fruitbounty.game.model.sources.CommonSources.class);
    }

}
