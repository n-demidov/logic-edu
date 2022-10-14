package com.demidovn.fruitbounty.server.persistence.entities.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackValue implements Serializable {

    private String userId;
    private String userName;
    private String date;
    private String msg;
    private String game;

}
