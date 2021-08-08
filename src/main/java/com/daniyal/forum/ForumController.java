package com.daniyal.forum;

import com.daniyal.RecordNotFoundException;
import com.daniyal.forum.dto.Thread;
import com.daniyal.forum.dto.*;
import com.daniyal.forum.persistence.QuestionEntity;
import com.daniyal.forum.persistence.ReplyEntity;
import com.daniyal.forum.service.ForumService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Forum API", description = "Basic Forum API that supports threads"))
@RequestMapping("questions")
@Slf4j
public class ForumController {
    private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    private final ForumService forumService;
    private final QuestionMapper questionMapper;
    private final ReplyMapper replyMapper;

    @PostMapping()
    @Operation(description = "Post a question")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Question successfully posted",
                    content = @Content(
                            mediaType = MEDIA_TYPE_APPLICATION_JSON,
                            schema = @Schema(implementation = Question.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            )
    })
    public ResponseEntity<Question> postQuestion(@Valid @RequestBody Request request) {
        QuestionEntity questionEntity = forumService.addQuestion(request.getAuthor(), request.getMessage());
        log.info("Question added with id " + questionEntity.getId());

        return new ResponseEntity<>(questionMapper.entityToQuestion(questionEntity), HttpStatus.ACCEPTED);
    }

    @PostMapping("{questionId}/reply")
    @Operation(description = "Post a reply to the question")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Reply successfully posted",
                    content = @Content(
                            mediaType = MEDIA_TYPE_APPLICATION_JSON,
                            schema = @Schema(implementation = Reply.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Question not found",
                    content = @Content
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            )
    })
    public ResponseEntity<Reply> postReply(
            @PathVariable(name = "questionId") long questionId,
            @Valid @RequestBody Request request
    ) throws RecordNotFoundException {
        ReplyEntity replyEntity = forumService.addReply(questionId, request.getAuthor(), request.getMessage());
        log.info("Reply with id " + replyEntity.getId() + " added to question with id " + replyEntity.getQuestionId());

        return new ResponseEntity<>(replyMapper.entityToReply(replyEntity), HttpStatus.ACCEPTED);
    }

    @GetMapping("{questionId}")
    @Operation(description = "Get thread associated with the question")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Thread response",
                    content = @Content(
                            mediaType = MEDIA_TYPE_APPLICATION_JSON,
                            schema = @Schema(implementation = Thread.class)
                    )
            )
    })
    public ResponseEntity<Thread> getThread(@PathVariable(name = "questionId") long questionId)
            throws RecordNotFoundException {
        QuestionEntity questionEntity = forumService.getThread(questionId);
        return new ResponseEntity<>(questionMapper.entityToThread(questionEntity), HttpStatus.OK);
    }

    @GetMapping
    @Operation(description = "Get all the questions")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "All questions response",
                    content = @Content(
                            mediaType = MEDIA_TYPE_APPLICATION_JSON,
                            array = @ArraySchema(schema = @Schema(implementation = Question.class))
                    )
            )
    })
    public ResponseEntity<Question[]> getAllQuestions() {
        return new ResponseEntity<>(questionMapper.entityListToQuestionList(forumService.getQuestions()), HttpStatus.OK);
    }
}
