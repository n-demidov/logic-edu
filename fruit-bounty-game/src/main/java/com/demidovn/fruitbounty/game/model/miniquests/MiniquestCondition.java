package com.demidovn.fruitbounty.game.model.miniquests;

public class MiniquestCondition {
    public final int id;
    public final int subjectIdx;
    public final int objectIdx;
    public boolean inverse;

    public MiniquestCondition(int id, int objectIdx, boolean inverse) {
        this(id, 0, objectIdx, inverse);
    }

    public MiniquestCondition(int id, int subjectIdx, int objectIdx, boolean inverse) {
        this.id = id;
        this.subjectIdx = subjectIdx;
        this.objectIdx = objectIdx;
        this.inverse = inverse;
    }

    @Override
    public String toString() {
        return "MiniQuestCondition{" +
                "id=" + id +
                ", subjectIdx=" + subjectIdx +
                ", objectIdx=" + objectIdx +
                ", inverse=" + inverse +
                '}';
    }

}
