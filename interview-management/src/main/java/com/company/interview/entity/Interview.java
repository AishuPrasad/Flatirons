package com.company.interview.entity;

import com.company.interview.enums.InterviewStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @ManyToOne
    private Candidate candidate;

    @ManyToMany
    @JoinTable(
            name = "interview_interviewers",
            joinColumns = @JoinColumn(name = "interview_id"),
            inverseJoinColumns = @JoinColumn(name = "interviewer_id")
    )
    private List<Interviewer> interviewers;

    public Interview() {
    }

    public Interview(LocalDateTime scheduledAt,
                     InterviewStatus status,
                     Candidate candidate,
                     List<Interviewer> interviewers) {
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.candidate = candidate;
        this.interviewers = interviewers;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public List<Interviewer> getInterviewers() {
        return interviewers;
    }

    public void setInterviewers(List<Interviewer> interviewers) {
        this.interviewers = interviewers;
    }
}
