package com.daniyal.forum.service;

import com.daniyal.RecordNotFoundException;
import com.daniyal.forum.persistence.QuestionEntity;
import com.daniyal.forum.persistence.QuestionRepository;
import com.daniyal.forum.persistence.ReplyEntity;
import com.daniyal.forum.persistence.ReplyRepository;
import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class defines Mockito based unit tests for ForumService. Repositories are mocked and all the service
 * functions implementing the endpoint logics are tested with the mock data returned by the repositories.
 * Function names are expressive and self-explanatory.
 *
 * @author Ali Daniyal
 */
@ExtendWith(MockitoExtension.class)
class ForumServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ForumService forumService;

    @Test
    public void addQuestionSavesEntityWithExpectedAuthorAndMessage() {
        when(questionRepository.saveAndFlush(any()))
                .thenAnswer(it -> it.getArguments()[0]);

        QuestionEntity questionEntity = forumService.addQuestion("Author", "Message");

        assertThat(questionEntity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("author", "Author")
                .hasFieldOrPropertyWithValue("message", "Message");

        verify(questionRepository).saveAndFlush(any());
    }

    @Test
    public void whenQuestionDoesntExist_addReply_throwsRecordNotFoundException() {
        assertThatThrownBy(() -> forumService.addReply(1, "Author", "Message"))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    public void whenQuestionExists_addReply_savesAndReturnsTheReply() throws Exception {
        final long questionId = 1L;
        final long replyId = 2L;

        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(questionId);
        questionEntity.setAuthor("Author");
        questionEntity.setMessage("Message");

        when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(questionEntity));
        when(replyRepository.saveAndFlush(any()))
                .thenAnswer(it -> {
                    ((ReplyEntity) it.getArguments()[0]).setId(replyId);
                    return it.getArguments()[0];
                });

        final ReplyEntity savedReply = forumService.addReply(questionId, "ReplyAuthor", "ReplyMessage");

        assertThat(savedReply)
                .as("A valid reply entity must have been returned after addReply")
                .hasFieldOrPropertyWithValue("id", replyId)
                .hasFieldOrPropertyWithValue("author", "ReplyAuthor")
                .hasFieldOrPropertyWithValue("message", "ReplyMessage");

        verify(replyRepository).saveAndFlush(savedReply);
    }

    @Test
    public void whenThereAreNoQuestions_getQuestions_returnsEmptyList() {
        when(questionRepository.findByOrderByUpdatedAtDesc())
                .thenReturn(Lists.emptyList());

        assertThat(forumService.getQuestions())
                .isEmpty();

        verify(questionRepository).findByOrderByUpdatedAtDesc();
    }

    @Test
    public void whenThereSomeQuestions_getQuestions_returnsAllOfThem() {
        QuestionEntity questionEntity1 = new QuestionEntity();
        questionEntity1.setId(1L);
        QuestionEntity questionEntity2 = new QuestionEntity();
        questionEntity1.setId(2L);

        when(questionRepository.findByOrderByUpdatedAtDesc())
                .thenReturn(ImmutableList.of(questionEntity1, questionEntity2));

        assertThat(forumService.getQuestions())
                .containsExactlyInAnyOrder(questionEntity1, questionEntity2);

        verify(questionRepository).findByOrderByUpdatedAtDesc();
    }

    @Test
    public void whenQuestionDoesntExist_getThread_throwsRecordNotFoundException() {
        assertThatThrownBy(() -> forumService.getThread(1L))
                .isInstanceOf(RecordNotFoundException.class);

        verify(questionRepository).findWithRepliesById(1L);
    }

    @Test
    public void whenQuestionExists_getThread_returnsTheQuestion() throws Exception {
        final long questionId = 1L;

        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(questionId);
        questionEntity.setAuthor("Author");
        questionEntity.setMessage("Message");

        when(questionRepository.findWithRepliesById(questionId))
                .thenReturn(Optional.of(questionEntity));

        assertThat(forumService.getThread(questionId))
                .as("getThread should return the question")
                .isEqualTo(questionEntity);

        verify(questionRepository).findWithRepliesById(questionId);
    }
}
