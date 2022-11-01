package com.demidovn.fruitbounty.server.persistence.entities.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatValue implements Serializable {

    private String userId;
    private String userName;
    private String date;
    private String msg;

}
