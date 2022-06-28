package com.demidovn.fruitbounty.game.model.singleplay;

import lombok.Data;

import java.util.List;

@Data
public class CompositeSources {
    private List<String> subjects;
    private List<ObjectsInfo> objectsInfos;
    private String objectToSubjectPrefix;

}
