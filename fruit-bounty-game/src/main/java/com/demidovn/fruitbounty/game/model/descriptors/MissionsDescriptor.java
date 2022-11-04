package com.demidovn.fruitbounty.game.model.descriptors;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MissionsDescriptor implements Serializable {
    private int version;
    private List<MissionDescriptor> missions;

    private Map<Integer, MissionDescriptor> missionsByLevel;

    public Map<Integer, MissionDescriptor> missionsByLevel() {
        return missionsByLevel;
    }

    public List<MissionDescriptor> getMissions() {
        return missions;
    }

}
