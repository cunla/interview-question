package com.daniyal.forum.persistence;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents the Question entity. replies attribute provides a list of replies linked ot this question.
 * A name entity graph "complete-thread" includes "replies" attribtue for eager fetching.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@NamedEntityGraph(
        name = "complete-thread",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("message"),
                @NamedAttributeNode("replies"),
        }
)
@Data
@Entity
@Table(name = "question")
public class QuestionEntity extends BaseEntity {

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private int replyCount;

    @OneToMany(mappedBy = "question")
    private List<ReplyEntity> replies;
}
