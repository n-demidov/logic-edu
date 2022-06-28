package com.demidovn.fruitbounty.game.services.game.generating;

import com.demidovn.fruitbounty.game.model.quest.AskAbout;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneratorMethods {

    public List<String> getAnswerOptions(AskAbout askAbout, List<String> subjects, List<String> objects) {
        if (askAbout == AskAbout.OBJECT) {
            return objects;
        } else if (askAbout == AskAbout.SUBJECT) {
            return subjects;
        } else {
            throw new IllegalArgumentException("Unknown answerOptions: " + askAbout);
        }
    }

    public String getQuestAnswer(AskAbout askAbout, String answerSubject, String answerObject) {
        if (askAbout == AskAbout.OBJECT) {
            return answerObject;
        } else if (askAbout == AskAbout.SUBJECT) {
            return answerSubject;
        } else {
            throw new IllegalArgumentException("Unknown answerOptions: " + askAbout);
        }
    }

}
