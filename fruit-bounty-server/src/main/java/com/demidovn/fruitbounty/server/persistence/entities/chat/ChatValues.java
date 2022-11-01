package com.demidovn.fruitbounty.server.persistence.entities.chat;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatValues implements Serializable {

    private List<ChatValue> chatValues = new ArrayList<>();
    private long version = 1;

}
