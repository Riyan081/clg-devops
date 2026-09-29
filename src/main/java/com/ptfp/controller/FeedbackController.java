package com.ptfp.controller;

import com.ptfp.dto.FeedbackRequest;
import com.ptfp.dto.StatusUpdateRequest;
import com.ptfp.model.Feedback;
import com.ptfp.model.FeedbackCategory;
import com.ptfp.model.FeedbackStatus;
import com.ptfp.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody FeedbackRequest request) {
        Feedback created = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> searchFeedbacks(
            @RequestParam(required = false) FeedbackStatus status,
            @RequestParam(required = false) FeedbackCategory category,
            @RequestParam(required = false) String search) {
        List<Feedback> results = feedbackService.searchFeedbacks(status, category, search);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<Feedback> getFeedbackByTicketId(@PathVariable String ticketId) {
        return feedbackService.getFeedbackByTicketId(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Feedback> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        Feedback updated = feedbackService.updateStatus(id, request);
        return ResponseEntity.ok(updated);
    }
}
