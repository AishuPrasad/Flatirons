package com.company.interview.service;

import com.company.interview.entity.Candidate;
import java.util.List;

public interface CandidateService {
    Candidate createCandidate(Candidate candidate);

    List<Candidate> getAllCandidates();

    Candidate getCandidateById(Long id);

    void deleteCandidate(Long id);
}
