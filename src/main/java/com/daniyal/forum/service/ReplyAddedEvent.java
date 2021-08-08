package com.daniyal.forum.service;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * This POJO represents the ApplicationEvent corresponding to the persisting of a new ReplyEntity record.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@ToString
public class ReplyAddedEvent extends ApplicationEvent {
    @Getter
    private final Long questionId;

    @Getter
    private final Long replyId;

    public ReplyAddedEvent(Object source, long questionId, long replyId) {
        super(source);
        this.questionId = questionId;
        this.replyId = replyId;
    }
}
