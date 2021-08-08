package com.daniyal.forum.persistence;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class includes tests for both the Question and Reply repositories. The reason to have a single test class
 * for both of these repositories is because of the dependencies of the entities. When extending the functionality
 * of these two repositories, consider splitting this class into separate test classes if warranted.
 *
 * @author Ali Daniyal
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest
@Transactional
@Rollback
public class RepositoryIntegrationTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Tests saving and retrieval of QuestionEntity.
     * Also verifies that ID and timestamp fields are auto-generated successfully.
     */
    @Test
    public void questionCanBeSavedAndRetrievedSuccessfully() {
        QuestionEntity question = new QuestionEntity();
        question.setAuthor("Author");
        question.setMessage("Message");

        QuestionEntity saved = questionRepository.saveAndFlush(question);

        assertThat(saved.getId())
                .as("Id must exist for the saved entity")
                .isGreaterThan(0);

        assertThat(saved.getAuthor())
                .as("author should match")
                .isEqualTo(question.getAuthor());

        assertThat(saved.getMessage())
                .as("message should match")
                .isEqualTo(question.getMessage());

        assertThat(saved.getCreatedAt())
                .as("createdAt must be set to recent value")
                .isAfter(OffsetDateTime.now().minusSeconds(5));

        assertThat(saved.getUpdatedAt())
                .as("updatedAt must be set to recent value")
                .isAfter(OffsetDateTime.now().minusSeconds(5));

        QuestionEntity found = questionRepository.getOne(saved.getId());

        assertThat(found)
                .as("Question must be found on retrieval")
                .isNotNull();
    }

    /**
     * This test primarily focuses on QuestionRepository::findById and the entity graph it employs to fetch
     * associated replies.
     * It persists a question and two replies and then verifies that the question can be retrieved along with its
     * replied using findById.
     */
    @Test
    public void questionCanBeRetrievedWithReplies() {
        QuestionEntity question = new QuestionEntity();
        question.setAuthor("Author");
        question.setMessage("Message");
        QuestionEntity savedQuestion = questionRepository.saveAndFlush(question);

        ReplyEntity reply1 = new ReplyEntity();
        reply1.setAuthor("Reply1Author");
        reply1.setMessage("Reply1Message");
        reply1.setQuestionId(savedQuestion.getId());

        ReplyEntity reply2 = new ReplyEntity();
        reply2.setAuthor("Reply2Author");
        reply2.setMessage("Reply2Message");
        reply2.setQuestionId(savedQuestion.getId());

        replyRepository.save(reply1);
        replyRepository.save(reply2);
        replyRepository.flush();

        entityManager.clear();

        Optional<QuestionEntity> foundOpt = questionRepository.findWithRepliesById(question.getId());

        assertThat(foundOpt)
                .isNotEmpty();

        PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        foundOpt.ifPresent(found -> {
            assertThat(unitUtil.isLoaded(found, "replies"))
                    .isTrue();
            assertThat(found.getReplies())
                    .isNotNull();
            assertThat(found.getReplies().stream().map(it -> it.getId()).collect(Collectors.toList()))
                    .as("Replies should be retrieved successfully")
                    .containsExactlyInAnyOrder(reply1.getId(), reply2.getId());
        });
    }

    /**
     * This test makes sure that if we call findByOrderByUpdatedAtDesc on the QuestionRepository,
     * then since LAZY fetching is employed, replies attributes are not initialized. Test sets up two questions and 1
     * reply each. Calls findByOrderByUpdatedAtDesc and verifies that only the questions are retrieves and that for
     * each question record, replies attribute is still not initialized. The test users PersistenceUnitUtil to
     * verify that replies attribute is not loaded after making the call.
     */
    @Test
    public void allQuestionCanBeRetrievedWithoutFetchingReplies() {
        QuestionEntity question1 = new QuestionEntity();
        question1.setAuthor("Author");
        question1.setMessage("Message");
        QuestionEntity savedQuestion1 = questionRepository.saveAndFlush(question1);

        ReplyEntity reply1 = new ReplyEntity();
        reply1.setAuthor("Reply1Author");
        reply1.setMessage("Reply1Message");
        reply1.setQuestionId(savedQuestion1.getId());
        replyRepository.saveAndFlush(reply1);

        QuestionEntity question2 = new QuestionEntity();
        question2.setAuthor("Author");
        question2.setMessage("Message");
        QuestionEntity savedQuestion2 = questionRepository.saveAndFlush(question2);

        ReplyEntity reply2 = new ReplyEntity();
        reply2.setAuthor("Reply2Author");
        reply2.setMessage("Reply2Message");
        reply2.setQuestionId(savedQuestion2.getId());
        replyRepository.saveAndFlush(reply2);

        entityManager.clear(); //Clearing the entity manager so that the cached entities are not returned

        List<QuestionEntity> questions = questionRepository.findByOrderByUpdatedAtDesc();

        assertThat(Collections2.transform(questions, it -> it.getId()))
                .as("All the questions must be returned")
                .containsAll(ImmutableList.of(savedQuestion1.getId(), savedQuestion2.getId()));

        PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        questions.forEach(it ->
                assertThat(unitUtil.isLoaded(it, "replies"))
                        .as("Replies shouldn't be retrieved")
                        .isFalse()
        );
    }
}
