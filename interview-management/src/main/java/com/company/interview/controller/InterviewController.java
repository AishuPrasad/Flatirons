package com.company.interview.controller;

import com.company.interview.dto.FeedbackRequest;
import com.company.interview.dto.InterviewRequest;
import com.company.interview.entity.Interview;
import com.company.interview.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<Interview> scheduleInterview(@Valid @RequestBody InterviewRequest request) {
        Interview interview = interviewService.scheduleInterview(request);
        return new ResponseEntity<>(interview, HttpStatus.CREATED);
    }

    @PostMapping("/{interviewId}/feedback")
    public ResponseEntity<Interview> submitFeedback(@PathVariable Long interviewId,
            @Valid @RequestBody FeedbackRequest request) {
        Interview interview = interviewService.submitFeedback(interviewId, request);
        return new ResponseEntity<>(interview, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Interview>> getInterviews(
            @RequestParam(required = false) String candidateName,
            @RequestParam(required = false) String interviewerName,
            Pageable pageable) {
        Page<Interview> interviews = interviewService.searchInterviews(candidateName, interviewerName, pageable);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }
}
