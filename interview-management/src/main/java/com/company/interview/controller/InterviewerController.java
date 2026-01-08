package com.company.interview.controller;

import com.company.interview.entity.Interviewer;
import com.company.interview.service.InterviewerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviewers")
public class InterviewerController {

    private final InterviewerService interviewerService;

    public InterviewerController(InterviewerService interviewerService) {
        this.interviewerService = interviewerService;
    }

    @PostMapping
    public ResponseEntity<Interviewer> createInterviewer(@RequestBody Interviewer interviewer) {
        Interviewer createdInterviewer = interviewerService.createInterviewer(interviewer);
        return new ResponseEntity<>(createdInterviewer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Interviewer>> getAllInterviewers() {
        List<Interviewer> interviewers = interviewerService.getAllInterviewers();
        return new ResponseEntity<>(interviewers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interviewer> getInterviewerById(@PathVariable Long id) {
        Interviewer interviewer = interviewerService.getInterviewerById(id);
        return new ResponseEntity<>(interviewer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewer(@PathVariable Long id) {
        interviewerService.deleteInterviewer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
