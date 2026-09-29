package com.ptfp.controller;

import com.ptfp.dto.DashboardSummaryDTO;
import com.ptfp.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final FeedbackService feedbackService;

    public DashboardController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(feedbackService.getDashboardSummary());
    }
}
