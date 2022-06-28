package com.demidovn.fruitbounty.server.persistence.repository;

import com.demidovn.fruitbounty.server.persistence.entities.feedback.Feedback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}
