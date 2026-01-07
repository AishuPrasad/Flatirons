package com.company.interview.service;

import com.company.interview.dto.FeedbackRequest;
import com.company.interview.dto.InterviewRequest;
import com.company.interview.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewService {
    Interview scheduleInterview(InterviewRequest request);

    Interview submitFeedback(Long interviewId, FeedbackRequest request);

    Page<Interview> searchInterviews(String candidateName, String interviewerName, Pageable pageable);
}
