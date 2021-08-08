package com.daniyal.forum.dto;

import com.daniyal.forum.persistence.ReplyEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * This is a Mapper interface based on the mapstruct. It maps Reply entities to the DTOs.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface ReplyMapper {
    /**
     * Maps ReplyEntity to Reply DTO, primarily based on the names of the attributes.
     *
     * @param replyEntity
     * @return Reply DTO
     */
    Reply entityToReply(ReplyEntity replyEntity);

    /**
     * Maps a list of ReplyEntity objects to Reply DTO instances
     *
     * @param replyEntities
     * @return list of Reply DTOs corresponding to the given list of entities
     */
    List<Reply> entityListToReplyList(List<ReplyEntity> replyEntities);
}
