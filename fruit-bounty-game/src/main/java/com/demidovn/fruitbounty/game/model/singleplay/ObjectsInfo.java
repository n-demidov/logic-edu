package com.demidovn.fruitbounty.game.model.singleplay;

import com.demidovn.fruitbounty.game.services.game.VerbDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ObjectsInfo implements VerbDescriptor {
    private List<String> objects;
    private final String intro;

    private String verb;
    private String verbPositive;
    private String verbNegative;

}
