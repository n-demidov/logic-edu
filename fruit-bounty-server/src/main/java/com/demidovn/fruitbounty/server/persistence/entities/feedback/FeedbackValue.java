package com.demidovn.fruitbounty.server.persistence.entities.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FeedbackValue {

    private String userId;
    private String userName;
    private String date;
    private String msg;
    private String game;

}
