package com.company.interview.service.impl;

import com.company.interview.entity.Candidate;
import com.company.interview.exception.ResourceNotFoundException;
import com.company.interview.repository.CandidateRepository;
import com.company.interview.service.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Candidate createCandidate(Candidate candidate) {
        if (candidate.getName() == null || candidate.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (candidate.getEmail() == null || candidate.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (!EMAIL_PATTERN.matcher(candidate.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        candidateRepository.findByEmail(candidate.getEmail())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Candidate with this email already exists");
                });
        return candidateRepository.save(candidate);
    }

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
    }

    @Override
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate not found with id: " + id);
        }
        candidateRepository.deleteById(id);
    }
}
