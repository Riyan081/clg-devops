package com.ptfp.unit;

import com.ptfp.dto.DashboardSummaryDTO;
import com.ptfp.dto.FeedbackRequest;
import com.ptfp.dto.StatusUpdateRequest;
import com.ptfp.model.Feedback;
import com.ptfp.model.FeedbackCategory;
import com.ptfp.model.FeedbackStatus;
import com.ptfp.repository.FeedbackRepository;
import com.ptfp.repository.RouteRepository;
import com.ptfp.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private RouteRepository routeRepository;

    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackService(feedbackRepository, routeRepository);
    }

    @Test
    void testCreateFeedback_GeneratesTicketIdAndSaves() {
        FeedbackRequest req = new FeedbackRequest();
        req.setRouteCode("ROUTE-101");
        req.setCategory(FeedbackCategory.DELAY);
        req.setDescription("Bus arrived late by 15 mins");
        req.setPassengerName("Alice");

        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> {
            Feedback f = invocation.getArgument(0);
            f.setId(1L);
            return f;
        });

        Feedback created = feedbackService.createFeedback(req);

        assertNotNull(created);
        assertNotNull(created.getTicketId());
        assertTrue(created.getTicketId().startsWith("PTFP-"));
        assertEquals(FeedbackStatus.SUBMITTED, created.getStatus());
        assertEquals("ROUTE-101", created.getRouteCode());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testUpdateStatus_ValidTransition() {
        Feedback existing = new Feedback();
        existing.setId(10L);
        existing.setTicketId("PTFP-20260908-1001");
        existing.setStatus(FeedbackStatus.SUBMITTED);

        when(feedbackRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StatusUpdateRequest updateReq = new StatusUpdateRequest(FeedbackStatus.IN_REVIEW, "Triage in progress");
        Feedback updated = feedbackService.updateStatus(10L, updateReq);

        assertEquals(FeedbackStatus.IN_REVIEW, updated.getStatus());
        assertEquals("Triage in progress", updated.getResolutionRemarks());
    }

    @Test
    void testUpdateStatus_DisallowTransitionFromClosed() {
        Feedback existing = new Feedback();
        existing.setId(20L);
        existing.setStatus(FeedbackStatus.CLOSED);

        when(feedbackRepository.findById(20L)).thenReturn(Optional.of(existing));

        StatusUpdateRequest updateReq = new StatusUpdateRequest(FeedbackStatus.IN_REVIEW, "Reopening");
        assertThrows(IllegalStateException.class, () -> feedbackService.updateStatus(20L, updateReq));
    }

    @Test
    void testDashboardSummary_CalculatesMetrics() {
        when(feedbackRepository.count()).thenReturn(10L);
        when(feedbackRepository.countByStatus(FeedbackStatus.SUBMITTED)).thenReturn(4L);
        when(feedbackRepository.countByStatus(FeedbackStatus.IN_REVIEW)).thenReturn(2L);
        when(feedbackRepository.countByStatus(FeedbackStatus.RESOLVED)).thenReturn(3L);
        when(feedbackRepository.countByStatus(FeedbackStatus.CLOSED)).thenReturn(1L);

        DashboardSummaryDTO summary = feedbackService.getDashboardSummary();

        assertEquals(10L, summary.getTotalFeedbacks());
        assertEquals(4L, summary.getSubmittedCount());
        assertEquals(2L, summary.getInReviewCount());
        assertEquals(3L, summary.getResolvedCount());
        assertEquals(1L, summary.getClosedCount());
        assertEquals(40.0, summary.getResolutionRatePercentage()); // (3+1)/10 = 40%
    }
}
