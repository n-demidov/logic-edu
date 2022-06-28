package com.demidovn.fruitbounty.server.persistence.entities.feedback;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class FeedbackValues implements Serializable {

    private List<FeedbackValue> feedbackValues = new ArrayList<>();
    private long version = 1;

}
