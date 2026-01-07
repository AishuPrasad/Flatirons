package com.company.interview;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.sql.init.mode=never")
class InterviewManagementApplicationTests {

	@org.springframework.test.context.bean.override.mockito.MockitoBean
	private com.company.interview.repository.CandidateRepository candidateRepository;

	@org.springframework.test.context.bean.override.mockito.MockitoBean
	private com.company.interview.repository.InterviewerRepository interviewerRepository;

	@org.springframework.test.context.bean.override.mockito.MockitoBean
	private com.company.interview.repository.FeedbackRepository feedbackRepository;

	@org.springframework.test.context.bean.override.mockito.MockitoBean
	private com.company.interview.repository.InterviewRepository interviewRepository;

	@Test
	void contextLoads() {
	}

}
