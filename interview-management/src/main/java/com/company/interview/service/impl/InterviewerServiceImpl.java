package com.company.interview.service.impl;

import com.company.interview.entity.Interviewer;
import com.company.interview.exception.ResourceNotFoundException;
import com.company.interview.repository.InterviewerRepository;
import com.company.interview.service.InterviewerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewerServiceImpl implements InterviewerService {

    private final InterviewerRepository interviewerRepository;

    public InterviewerServiceImpl(InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
    }

    @Override
    public Interviewer createInterviewer(Interviewer interviewer) {
        if (interviewer.getName() == null || interviewer.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (interviewer.getExpertise() == null || interviewer.getExpertise().isBlank()) {
            throw new IllegalArgumentException("Expertise cannot be blank");
        }
        return interviewerRepository.save(interviewer);
    }

    @Override
    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    @Override
    public Interviewer getInterviewerById(Long id) {
        return interviewerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interviewer not found with id: " + id));
    }

    @Override
    public void deleteInterviewer(Long id) {
        if (!interviewerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Interviewer not found with id: " + id);
        }
        interviewerRepository.deleteById(id);
    }
}
