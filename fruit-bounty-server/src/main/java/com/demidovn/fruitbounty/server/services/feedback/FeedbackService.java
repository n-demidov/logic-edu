package com.demidovn.fruitbounty.server.services.feedback;

import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.server.persistence.entities.User;
import com.demidovn.fruitbounty.server.persistence.entities.feedback.Feedback;
import com.demidovn.fruitbounty.server.persistence.entities.feedback.FeedbackValue;
import com.demidovn.fruitbounty.server.persistence.entities.feedback.FeedbackValues;
import com.demidovn.fruitbounty.server.persistence.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class FeedbackService {
    private static final long FEEDBACK_ID = 1L;

    private static final Object lock = new Object();

    @Autowired
    private FeedbackRepository feedbackRepository;

    public void addFeedback(String msg, User user, Optional<Game> game) {
        // todo: extract to async thread-pool
        synchronized (lock) {
            Feedback feedback = loadFeedback();
            addFeedback(msg, user, game, feedback);
            feedbackRepository.save(feedback);
        }
    }

    private Feedback loadFeedback() {
        // Keep all in one row because free DB has a limit of rows :)
        Feedback result = feedbackRepository.findOne(FEEDBACK_ID);
        if (result == null) {
            result = new Feedback();
            result.setFeedbackValues(new FeedbackValues());
        }
        return result;
    }

    private void addFeedback(String msg, User user, Optional<Game> game, Feedback feedback) {
        String gameDescription = game.isPresent() ? game.get().toString() : "";

        FeedbackValue feedbackValue = new FeedbackValue(
                String.valueOf(user.getId()),
                user.getPublicName(),
                LocalDateTime.now(ZoneId.of("GMT")).toString(),
                msg,
                gameDescription
        );

        feedback.getFeedbackValues().getFeedbackValues().add(feedbackValue);
    }

}
