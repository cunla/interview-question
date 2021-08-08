package com.daniyal.forum.dto;

import com.daniyal.forum.persistence.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mappings(
            @Mapping(target = "replies", source = "replyCount")
    )
    Question entityToQuestion(QuestionEntity questionEntity);

    Thread entityToThread(QuestionEntity questionEntity);

    Question[] entityListToQuestionList(List<QuestionEntity> entityList);
}
