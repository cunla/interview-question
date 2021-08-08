package com.daniyal.forum.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository
        extends JpaRepository<QuestionEntity, Long> {
    /**
     * Returns the QuestionEntity for the given id, if it exists. This function eagerly fetches all the ReplyEntity
     * records associated with this question.
     *
     * @param id
     * @return QuestionEntity with pre-populated full list of associted replies
     */
    @EntityGraph("complete-thread")
    Optional<QuestionEntity> findWithRepliesById(Long id);

    /**
     * Returns list of all QuestionEntity records. Note that replies attribute is not pre-populated when returned from
     * this function.
     *
     * @return List of all QuestionEntity instances. replies attributes is not pre-populated
     */
    List<QuestionEntity> findByOrderByUpdatedAtDesc();
}
