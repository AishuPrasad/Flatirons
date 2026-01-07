package com.company.interview.service.impl;

import com.company.interview.dto.FeedbackRequest;
import com.company.interview.dto.InterviewRequest;
import com.company.interview.entity.Candidate;
import com.company.interview.entity.Interview;
import com.company.interview.entity.Interviewer;
import com.company.interview.enums.InterviewStatus;
import com.company.interview.exception.ResourceNotFoundException;
import com.company.interview.repository.CandidateRepository;
import com.company.interview.repository.FeedbackRepository;
import com.company.interview.repository.InterviewRepository;
import com.company.interview.repository.InterviewerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceImplTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InterviewerRepository interviewerRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private InterviewServiceImpl interviewService;

    @Test
    public void scheduleInterview_ValidData_CreatesInterview() {
        InterviewRequest request = new InterviewRequest(1L, Arrays.asList(1L, 2L), LocalDateTime.now().plusDays(1));
        Candidate candidate = new Candidate("John Doe", "john@example.com");
        Interviewer interviewer1 = new Interviewer("Jane Smith", "Java");
        Interviewer interviewer2 = new Interviewer("Bob Jones", "System Design");

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewerRepository.findAllById(request.getInterviewerIds()))
                .thenReturn(Arrays.asList(interviewer1, interviewer2));
        when(interviewRepository.save(any(Interview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Interview result = interviewService.scheduleInterview(request);

        assertNotNull(result);
        assertEquals(InterviewStatus.SCHEDULED, result.getStatus());
        assertEquals(candidate, result.getCandidate());
        assertEquals(2, result.getInterviewers().size());
        verify(interviewRepository).save(any(Interview.class));
    }

    @Test
    public void scheduleInterview_InvalidCandidate_ThrowsException() {
        InterviewRequest request = new InterviewRequest(99L, Collections.singletonList(1L),
                LocalDateTime.now().plusDays(1));

        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> interviewService.scheduleInterview(request));
    }

    @Test
    public void submitFeedback_ValidInterview_UpdatesStatus() {
        Long interviewId = 1L;
        FeedbackRequest request = new FeedbackRequest(5, "Excellent");
        Interview interview = new Interview(LocalDateTime.now(), InterviewStatus.SCHEDULED, new Candidate(),
                Collections.emptyList());

        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        when(interviewRepository.save(any(Interview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Interview result = interviewService.submitFeedback(interviewId, request);

        assertEquals(InterviewStatus.COMPLETED, result.getStatus());
        verify(feedbackRepository).save(any());
        verify(interviewRepository).save(interview);
    }

    @Test
    public void submitFeedback_InvalidInterview_ThrowsException() {
        Long interviewId = 99L;
        FeedbackRequest request = new FeedbackRequest(5, "Excellent");

        when(interviewRepository.findById(interviewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> interviewService.submitFeedback(interviewId, request));
    }
}
