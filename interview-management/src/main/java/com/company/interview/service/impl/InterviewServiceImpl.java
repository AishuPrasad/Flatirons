package com.company.interview.service.impl;

import com.company.interview.dto.FeedbackRequest;
import com.company.interview.dto.InterviewRequest;
import com.company.interview.entity.Candidate;
import com.company.interview.entity.Feedback;
import com.company.interview.entity.Interview;
import com.company.interview.entity.Interviewer;
import com.company.interview.enums.InterviewStatus;
import com.company.interview.exception.ResourceNotFoundException;
import com.company.interview.repository.CandidateRepository;
import com.company.interview.repository.FeedbackRepository;
import com.company.interview.repository.InterviewRepository;
import com.company.interview.repository.InterviewerRepository;
import com.company.interview.service.InterviewService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewerRepository interviewerRepository;
    private final FeedbackRepository feedbackRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository,
            CandidateRepository candidateRepository,
            InterviewerRepository interviewerRepository,
            FeedbackRepository feedbackRepository) {
        this.interviewRepository = interviewRepository;
        this.candidateRepository = candidateRepository;
        this.interviewerRepository = interviewerRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Interview scheduleInterview(InterviewRequest request) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Candidate not found with id: " + request.getCandidateId()));

        List<Interviewer> interviewers = interviewerRepository.findAllById(request.getInterviewerIds());
        if (interviewers.size() != request.getInterviewerIds().size()) {
            throw new ResourceNotFoundException("One or more interviewers not found");
        }

        Interview interview = new Interview(
                request.getScheduledAt(),
                InterviewStatus.SCHEDULED,
                candidate,
                interviewers);

        return interviewRepository.save(interview);
    }

    @Override
    public Interview submitFeedback(Long interviewId, FeedbackRequest request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + interviewId));

        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot provide feedback for a cancelled interview");
        }

        Feedback feedback = new Feedback(
                request.getRating(),
                request.getComments(),
                interview);
        feedbackRepository.save(feedback);

        interview.setStatus(InterviewStatus.COMPLETED);
        return interviewRepository.save(interview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Interview> searchInterviews(String candidateName, String interviewerName, Pageable pageable) {
        Specification<Interview> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (candidateName != null && !candidateName.isEmpty()) {
                Join<Interview, Candidate> candidateJoin = root.join("candidate");
                predicates.add(cb.like(cb.lower(candidateJoin.get("name")), "%" + candidateName.toLowerCase() + "%"));
            }

            if (interviewerName != null && !interviewerName.isEmpty()) {
                Join<Interview, Interviewer> interviewerJoin = root.join("interviewers");
                predicates
                        .add(cb.like(cb.lower(interviewerJoin.get("name")), "%" + interviewerName.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return interviewRepository.findAll(spec, pageable);
    }
}
