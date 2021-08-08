package com.daniyal.forum.persistence;

import com.daniyal.forum.service.ReplyAddedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;

/**
 * This is an EntityListener for ReplyEntity.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@Component
@AllArgsConstructor
@Slf4j
public class ReplyListener {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * This function processes PostPersist event related to ReplyEntity. It publishes ReplyAddedEvent ApplicationEvent.
     *
     * @param replyEntity Entity corresponding to the PostPersist event
     */
    @PostPersist
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterPersistingReply(final ReplyEntity replyEntity) {
        log.debug("Publishing ReplyAddedEvent for reply ID " + replyEntity.getId()
                + " and question ID " + replyEntity.getQuestionId());
        eventPublisher.publishEvent(new ReplyAddedEvent(this, replyEntity.getQuestionId(), replyEntity.getId()));
    }
}
