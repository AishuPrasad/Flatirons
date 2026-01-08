package com.company.interview.service;

import com.company.interview.entity.Interviewer;
import java.util.List;

public interface InterviewerService {
    Interviewer createInterviewer(Interviewer interviewer);

    List<Interviewer> getAllInterviewers();

    Interviewer getInterviewerById(Long id);

    void deleteInterviewer(Long id);
}
