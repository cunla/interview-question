package com.daniyal.forum;

import com.daniyal.forum.dto.Thread;
import com.daniyal.forum.dto.*;
import com.daniyal.forum.persistence.QuestionEntity;
import com.daniyal.forum.persistence.QuestionRepository;
import com.daniyal.forum.persistence.ReplyEntity;
import com.daniyal.forum.persistence.ReplyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SpringBoot integration test with MockMvc to execute all the different scenarios of the endpoint calls.
 *
 * @author Ali Daniyal
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class ForumControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private EntityManager entityManager;

    private final Random random = new Random();

    /**
     * Tests POST:/questions endpoint with an invalid request payload that doesn't include author attribute.
     * The response should be a 400.
     */
    @Test
    public void whenRequestPayloadIsMissingAuthor_addQuestion_returns400Response() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("message", "Message");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/questions")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("author cannot be blank"));
    }

    /**
     * Tests POST:/questions endpoint with an invalid request payload that doesn't include message attribute.
     * The response should be a 400.
     */
    @Test
    public void whenRequestPayloadIsMissingMessage_addQuestion_returns400Response() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("author", "Author");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/questions")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("message cannot be blank"));
    }

    /**
     * Tests POST:/questions endpoint by sending a valid request payload.
     * Verifies 201 response along with expected response payload.
     * Also checks if the corresponding QuestionEntity was created and has expected values.
     */
    @Test
    public void whenRequestPayloadIsGood_addQuestion_addsQuestionAndReturnsIt() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("author", "Author");
        request.put("message", "Message");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/questions")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();

        Question questionResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                Question.class);

        assertThat(questionResponse)
                .as("Response question must have valid author, message and replies count")
                .hasFieldOrPropertyWithValue("author", "Author")
                .hasFieldOrPropertyWithValue("message", "Message")
                .hasFieldOrPropertyWithValue("replies", 0);

        assertThat(questionResponse.getId())
                .as("Question Id must be a positive value")
                .isGreaterThan(0L);

        Optional<QuestionEntity> questionEntityOpt = questionRepository.findById(questionResponse.getId());

        assertThat(questionEntityOpt)
                .as("Question entity should exist against the id returned in the response")
                .isNotEmpty();

        assertThat(questionResponse)
                .as("Question entity data must match the response")
                .isEqualTo(questionMapper.entityToQuestion(questionEntityOpt.get()));
    }

    /**
     * Tests POST:/questions/{questionId}/reply endpoint with an invalid request payload that doesn't include author
     * attribute. The response should be a 400.
     */
    @Test
    public void whenRequestPayloadIsMissingAuthor_addReply_returns400Response() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("message", "Message");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/questions/1/reply")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("author cannot be blank"));
    }

    /**
     * Tests POST:/questions/{questionId}/reply endpoint with an invalid request payload that doesn't include message
     * attribute. The response should be a 400.
     */
    @Test
    public void whenRequestPayloadIsMissingMessage_addReply_returns400Response() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("author", "Author");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/questions/1/reply")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("message cannot be blank"));
    }

    /**
     * Tests POST:/questions/{questionId}/reply endpoint with a valid request payload but an invalid questionId.
     * Since Question is not found, 404 must be generated.
     */
    @Test
    public void whenQuestionDoesntExist_addReply_returns404Response() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("author", "Author");
        request.put("message", "Message");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/questions/1/reply")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Question with id 1 not found."));
    }

    /**
     * Tests POST:/questions/{questionId}/reply endpoint with a valid request payload and a valid questionId.
     * Setup: Add a question by making call to POST:/questions
     * To Test: Call POST:/questions/{questionId}/reply using the questionId just generated with a valid request payload.
     * Verify: 201 response generated by the call and returned Reply data is as expected. Also verify that a ReplyEntity
     * record was created with expected values.
     */
    @Test
    public void whenQuestionExists_addReply_addsReplyAndReturnsIt() throws Exception {
        Question question = addQuestion("Author", "Message");
        Reply reply = addReply(question.getId(), "Author1", "Message1");

        assertThat(reply)
                .as("Response reply must have valid author, message and replies count")
                .hasFieldOrPropertyWithValue("author", "Author1")
                .hasFieldOrPropertyWithValue("message", "Message1")
                .hasFieldOrPropertyWithValue("questionId", question.getId());

        assertThat(reply.getId())
                .as("Question Id must be a positive value")
                .isGreaterThan(0L);

        ReplyEntity replyEntity = replyRepository.getOne(reply.getId());
        assertThat(reply)
                .as("Reply entity data must match the response")
                .isEqualTo(replyMapper.entityToReply(replyEntity));

        QuestionEntity questionEntity = questionRepository.getOne(question.getId());
        assertThat(questionEntity.getReplyCount())
                .as("Reply count must be 1")
                .isEqualTo(1);
    }

    /**
     * Tests that in the absence of any QuestionEntity in the DB GET:/questions returns a 200 response with an empty list.
     */
    @Test
    public void whenThereAreNoQuestions_getQuestions_returnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    /**
     * Tests that if there are QuestionEntity records in the DB GET:/questions return a 200 response along with those
     * records:
     * Setup: Create 2 QuestionEntity records in the DB
     * To Test: Call GET:/questions
     * Verify: Call returned 200 ok result with list of Questions that include the entities created
     */
    @Test
    public void whenThereAreSomeQuestions_getQuestions_returnsAll() throws Exception {
        QuestionEntity questionEntity1 = new QuestionEntity();
        questionEntity1.setAuthor("Author1");
        questionEntity1.setMessage("Message1");

        QuestionEntity questionEntity2 = new QuestionEntity();
        questionEntity2.setAuthor("Author2");
        questionEntity2.setMessage("Message2");

        //persisting questions
        questionEntity1 = questionRepository.saveAndFlush(questionEntity1);
        //Taking a little pause so that the updatedAt timestamp on the second question is a little later
        java.lang.Thread.sleep(100);
        questionEntity2 = questionRepository.saveAndFlush(questionEntity2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Question[] response = objectMapper.readValue(result.getResponse().getContentAsString(), Question[].class);

        assertThat(response)
                .as("Response must have both questions")
                .containsAll(ImmutableList.of(questionMapper.entityToQuestion(questionEntity2),
                        questionMapper.entityToQuestion(questionEntity1)));
    }

    /**
     * Tests GET:/questions/{questionId} generates 404 with appropriate error message if questionId is invalid
     */
    @Test
    public void whenQuestionDoesntExist_getThread_returns404Response() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/questions/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Question with id 1 not found."));
    }

    /**
     * Tests GET:/questions/{questionId} returns a 200 response when questionId corresponds to an existing question.
     * Setup: Creates a QuestionEntity record
     * To Test: Call GET:/questions/{questionId} with id generated in the setup
     * Verify: Call returns a 200 response with Thread object containing expected values for the Question.
     * This tests doesn't verify corresponding replies.
     */
    @Test
    public void whenQuestionExistsButNotReplies_getThread_returnsThread() throws Exception {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setAuthor("Author1");
        questionEntity.setMessage("Message1");
        questionEntity = questionRepository.saveAndFlush(questionEntity);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/questions/" + questionEntity.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Thread threadResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Thread.class);

        assertThat(threadResponse)
                .as("Response thread must have valid id, author, message and replies")
                .hasFieldOrPropertyWithValue("id", questionEntity.getId())
                .hasFieldOrPropertyWithValue("author", "Author1")
                .hasFieldOrPropertyWithValue("message", "Message1");
    }

    /**
     * Tests GET:/questions/{questionId} returns a 200 response when questionId corresponds to an existing question.
     * This test includes verification of corresponding replies.
     * Setup: Creates a Question by calling POST:/questions. Creates two corresponding replies by calling
     * POST:/questions/{questionId}/reply with the generated questionId.
     * To Test: Call GET:/questions/{questionId} with id generated in the setup
     * Verify: Call returns a 200 response with Thread object containing expected values for the Question as well
     * as replies.
     */

    @Test
    public void whenQuestionExistsWithReplies_getThread_returnsThreadWithReplies() throws Exception {
        Question question = addQuestion("Author1", "Message1");
        Reply reply1 = addReply(question.getId(), "Author2", "Message2");
        Reply reply2 = addReply(question.getId(), "Author3", "Message3");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/questions/" + question.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Thread threadResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Thread.class);

        assertThat(threadResponse)
                .as("Response thread must have valid id, author, message and replies")
                .hasFieldOrPropertyWithValue("id", question.getId())
                .hasFieldOrPropertyWithValue("author", "Author1")
                .hasFieldOrPropertyWithValue("message", "Message1")
                .hasFieldOrPropertyWithValue("replies", ImmutableList.of(reply1, reply2));
    }

    private Question addQuestion(String author, String message) throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("author", author);
        request.put("message", message);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/questions")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
        Question questionResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                Question.class);

        assertThat(questionResponse.getId())
                .as("Question must have been added")
                .isGreaterThan(0);

        return questionResponse;
    }

    private Reply addReply(long questionId, String author, String message) throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("author", author);
        request.put("message", message);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/questions/" + questionId + "/reply")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json")
        )
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();

        Reply replyResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                Reply.class);

        assertThat(replyResponse.getId())
                .as("Reply must have been created")
                .isGreaterThan(0);

        return replyResponse;
    }
}
