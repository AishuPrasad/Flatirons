package com.company.interview.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewRequest {
    private Long candidateId;
    private List<Long> interviewerIds;
    private LocalDateTime scheduledAt;

    public InterviewRequest() {
    }

    public InterviewRequest(Long candidateId, List<Long> interviewerIds, LocalDateTime scheduledAt) {
        this.candidateId = candidateId;
        this.interviewerIds = interviewerIds;
        this.scheduledAt = scheduledAt;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public List<Long> getInterviewerIds() {
        return interviewerIds;
    }

    public void setInterviewerIds(List<Long> interviewerIds) {
        this.interviewerIds = interviewerIds;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
