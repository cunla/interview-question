package com.daniyal.forum.service;

import com.daniyal.RecordNotFoundException;
import com.daniyal.forum.persistence.QuestionEntity;
import com.daniyal.forum.persistence.QuestionRepository;
import com.daniyal.forum.persistence.ReplyEntity;
import com.daniyal.forum.persistence.ReplyRepository;
import com.google.common.collect.Collections2;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * This service class represents the majority of the Forum API's business logic. It provides functions for each of the
 * API endpoints along with the logic for processing data state change events.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class ForumService {
    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    /**
     * Returns QuestionEntity with all the associate replies pre-populated, hence the name getThread.
     *
     * @param questionId Id of the Question being retrieved
     * @return QuestionEntity with pre-populated replies attribute
     * @throws RecordNotFoundException Throws when questionId is invalid
     */
    public QuestionEntity getThread(long questionId)
            throws RecordNotFoundException {
        Optional<QuestionEntity> questionOpt = questionRepository.findWithRepliesById(questionId);

        if (questionOpt.isPresent()) {
            return questionOpt.get();
        } else {
            throw new RecordNotFoundException("Question with id " + questionId + " not found.");
        }
    }

    /**
     * Returns list of all the QuestionEntity records.
     *
     * @return
     */
    public List<QuestionEntity> getQuestions() {
        return questionRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * Adds a Question entity with the given author and message attributes.
     *
     * @param author  author of the question
     * @param message text representing the question
     * @return The newly created Question entity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public QuestionEntity addQuestion(String author, String message) {
        QuestionEntity question = new QuestionEntity();
        question.setAuthor(author);
        question.setMessage(message);
        return questionRepository.saveAndFlush(question);
    }

    /**
     * Adds reply entity to the question corresponding to the given id.
     *
     * @param questionId ID of the question to which reply needs to be added
     * @param author     author of the reply
     * @param message    reply text
     * @return newly created Reply entity
     * @throws RecordNotFoundException Throws when questionId is invalid
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReplyEntity addReply(long questionId, String author, String message)
            throws RecordNotFoundException {
        Optional<QuestionEntity> questionOpt = questionRepository.findById(questionId);

        if (questionOpt.isPresent()) {
            ReplyEntity reply = new ReplyEntity();
            reply.setQuestionId(questionOpt.get().getId());
            reply.setAuthor(author);
            reply.setMessage(message);
            return replyRepository.saveAndFlush(reply);
        } else {
            throw new RecordNotFoundException("Question with id " + questionId + " not found.");
        }
    }

    /**
     * Processes ReplyAddedEvent and updates replyCount of the corresponding Question
     *
     * @param event ReplyAddedEvent ApplicationEvent
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handleReplyAddedEvent(ReplyAddedEvent event) {
        log.debug("Received ReplyAddedEvent " + event);
        questionRepository.findWithRepliesById(event.getQuestionId()).ifPresent(question -> {
            //Checks if the Reply Id that triggered this ApplicationEvent is part of the list retrieved
            //from the persistence layer
            boolean triggerReplyIncluded = Collections2.transform(question.getReplies(), it -> it.getId())
                    .contains(event.getReplyId());

            //If the triggering Reply ID is not part of the list of replies retrieved from the persistence layer,
            //then it's primarily because that change hasn't been committed to the DB yet. Therefore, the correct
            // current count of replies is 1 + number of replies retrieved from the persistence layer
            int repliesCount = question.getReplies().size() + (triggerReplyIncluded ? 0 : 1);
            question.setReplyCount(repliesCount);
            questionRepository.save(question);

            log.debug("Updated QuestionEntity " + question.getId() + " with replyCount " + question.getReplyCount());
        });
    }
}
