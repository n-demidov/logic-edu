package com.demidovn.fruitbounty.game.services.game;

public interface VerbDescriptor {

    String getVerb();
    String getVerbPositive();
    String getVerbNegative();

    void setVerb(String verb);
    void setVerbPositive(String verbPositive);
    void setVerbNegative(String verbNegative);

}
