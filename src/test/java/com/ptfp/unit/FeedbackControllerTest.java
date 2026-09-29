package com.ptfp.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptfp.controller.FeedbackController;
import com.ptfp.dto.FeedbackRequest;
import com.ptfp.dto.StatusUpdateRequest;
import com.ptfp.model.Feedback;
import com.ptfp.model.FeedbackCategory;
import com.ptfp.model.FeedbackStatus;
import com.ptfp.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedbackController.class)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeedbackService feedbackService;

    @Test
    void testCreateFeedback_Returns201Created() throws Exception {
        FeedbackRequest req = new FeedbackRequest();
        req.setRouteCode("ROUTE-101");
        req.setCategory(FeedbackCategory.DELAY);
        req.setDescription("Train delayed by 20 minutes");

        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setTicketId("PTFP-20260908-9999");
        feedback.setRouteCode("ROUTE-101");
        feedback.setCategory(FeedbackCategory.DELAY);
        feedback.setStatus(FeedbackStatus.SUBMITTED);
        feedback.setDescription(req.getDescription());

        when(feedbackService.createFeedback(any(FeedbackRequest.class))).thenReturn(feedback);

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value("PTFP-20260908-9999"))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    void testSearchFeedbacks_Returns200WithList() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setId(2L);
        feedback.setTicketId("PTFP-20260908-1111");
        feedback.setRouteCode("METRO-BLUE");
        feedback.setCategory(FeedbackCategory.CLEANLINESS);
        feedback.setStatus(FeedbackStatus.IN_REVIEW);

        when(feedbackService.searchFeedbacks(any(), any(), any())).thenReturn(List.of(feedback));

        mockMvc.perform(get("/api/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value("PTFP-20260908-1111"));
    }

    @Test
    void testUpdateStatus_Returns200Updated() throws Exception {
        StatusUpdateRequest updateReq = new StatusUpdateRequest(FeedbackStatus.RESOLVED, "Cleaned up");
        Feedback feedback = new Feedback();
        feedback.setId(3L);
        feedback.setStatus(FeedbackStatus.RESOLVED);
        feedback.setResolutionRemarks("Cleaned up");

        when(feedbackService.updateStatus(eq(3L), any(StatusUpdateRequest.class))).thenReturn(feedback);

        mockMvc.perform(put("/api/feedback/3/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }
}
