package com.daniyal.forum.persistence;

import lombok.Data;

import javax.persistence.*;

/**
 * This class represents the Reply entity. question attribute provides the associated Question, which is fetched
 * with a LAZY strategy. This class has an associated EntityListener, `ReplyListener`.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@EntityListeners(ReplyListener.class)
@Table(name = "reply")
public class ReplyEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private QuestionEntity question;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String message;

}
