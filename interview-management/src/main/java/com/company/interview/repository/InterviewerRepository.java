package com.company.interview.repository;

import com.company.interview.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
}
