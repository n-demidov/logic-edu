package com.demidovn.fruitbounty.game.model.descriptors;

public enum DescriptorType {

    MISSION_EDU(true),
    MISSION_STANDARD(true),
    MISSION_TABLE_2(true),
    STANDARD(false);

    private final boolean mission;

    DescriptorType(boolean mission) {
        this.mission = mission;
    }

    public boolean isMission() {
        return mission;
    }
}
