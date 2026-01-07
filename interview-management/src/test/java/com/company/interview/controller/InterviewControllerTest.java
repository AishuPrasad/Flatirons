package com.company.interview.controller;

import com.company.interview.dto.FeedbackRequest;
import com.company.interview.dto.InterviewRequest;
import com.company.interview.entity.Interview;
import com.company.interview.enums.InterviewStatus;
import com.company.interview.service.InterviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InterviewController.class)
public class InterviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterviewService interviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void scheduleInterview_Success() throws Exception {
        InterviewRequest request = new InterviewRequest(1L, Arrays.asList(1L, 2L), LocalDateTime.now().plusDays(1));
        Interview interview = new Interview();

        java.lang.reflect.Field idField = Interview.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(interview, 100L);
        interview.setStatus(InterviewStatus.SCHEDULED);

        when(interviewService.scheduleInterview(any(InterviewRequest.class))).thenReturn(interview);

        mockMvc.perform(post("/api/interviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    public void submitFeedback_Success() throws Exception {
        FeedbackRequest request = new FeedbackRequest(5, "Great candidate!");
        Interview interview = new Interview();
        java.lang.reflect.Field idField = Interview.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(interview, 100L);
        interview.setStatus(InterviewStatus.COMPLETED);

        when(interviewService.submitFeedback(eq(100L), any(FeedbackRequest.class))).thenReturn(interview);

        mockMvc.perform(post("/api/interviews/100/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    public void searchInterviews_Success() throws Exception {
        Interview interview = new Interview();
        java.lang.reflect.Field idField = Interview.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(interview, 100L);
        interview.setStatus(InterviewStatus.SCHEDULED);

        List<Interview> interviewList = Collections.singletonList(interview);
        Page<Interview> page = new PageImpl<>(interviewList);

        when(interviewService.searchInterviews(any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/interviews")
                .param("candidateName", "John")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100L))
                .andExpect(jsonPath("$.content[0].status").value("SCHEDULED"));
    }
}
