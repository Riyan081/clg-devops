package com.ptfp.service;

import com.ptfp.dto.DashboardSummaryDTO;
import com.ptfp.dto.FeedbackRequest;
import com.ptfp.dto.StatusUpdateRequest;
import com.ptfp.model.Feedback;
import com.ptfp.model.FeedbackCategory;
import com.ptfp.model.FeedbackStatus;
import com.ptfp.model.Route;
import com.ptfp.repository.FeedbackRepository;
import com.ptfp.repository.RouteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RouteRepository routeRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, RouteRepository routeRepository) {
        this.feedbackRepository = feedbackRepository;
        this.routeRepository = routeRepository;
    }

    @PostConstruct
    public void initSeedData() {
        if (routeRepository.count() == 0) {
            routeRepository.saveAll(List.of(
                    new Route("ROUTE-101", "Downtown Express Bus", "BUS"),
                    new Route("ROUTE-204", "Uptown & University Bus", "BUS"),
                    new Route("METRO-BLUE", "Blue Line Metro (Airport - Central)", "METRO"),
                    new Route("METRO-RED", "Red Line Metro (North - South)", "METRO"),
                    new Route("TRAIN-SUB-1", "Suburban Line 1 East", "TRAIN")
            ));
        }

        if (feedbackRepository.count() == 0) {
            Feedback f1 = new Feedback();
            f1.setTicketId("PTFP-20260901-1001");
            f1.setRouteCode("ROUTE-101");
            f1.setCategory(FeedbackCategory.DELAY);
            f1.setPassengerName("John Doe");
            f1.setPassengerEmail("john@example.com");
            f1.setDescription("Bus arrived 25 minutes late during morning peak hours.");
            f1.setStatus(FeedbackStatus.IN_REVIEW);
            f1.setCreatedAt(LocalDateTime.now().minusDays(2));
            f1.setUpdatedAt(LocalDateTime.now().minusDays(1));
            feedbackRepository.save(f1);

            Feedback f2 = new Feedback();
            f2.setTicketId("PTFP-20260902-1002");
            f2.setRouteCode("METRO-BLUE");
            f2.setCategory(FeedbackCategory.CLEANLINESS);
            f2.setPassengerName("Alice Smith");
            f2.setPassengerEmail("alice@example.com");
            f2.setDescription("Spill in Coach 3 was cleaned up promptly by cleaning staff.");
            f2.setStatus(FeedbackStatus.RESOLVED);
            f2.setResolutionRemarks("Station cleaning crew dispatched and completed sanitation.");
            f2.setCreatedAt(LocalDateTime.now().minusDays(3));
            f2.setUpdatedAt(LocalDateTime.now().minusDays(2));
            feedbackRepository.save(f2);
        }
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Transactional
    public Feedback createFeedback(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setTicketId(generateTicketId());
        feedback.setRouteCode(request.getRouteCode().trim());
        feedback.setCategory(request.getCategory());
        feedback.setPassengerName(request.getPassengerName() != null ? request.getPassengerName().trim() : null);
        feedback.setPassengerEmail(request.getPassengerEmail() != null ? request.getPassengerEmail().trim() : null);
        feedback.setDescription(request.getDescription().trim());
        feedback.setStatus(FeedbackStatus.SUBMITTED);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> searchFeedbacks(FeedbackStatus status, FeedbackCategory category, String search) {
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return feedbackRepository.searchFeedbacks(status, category, cleanSearch);
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Optional<Feedback> getFeedbackByTicketId(String ticketId) {
        return feedbackRepository.findByTicketId(ticketId.trim());
    }

    @Transactional
    public Feedback updateStatus(Long id, StatusUpdateRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));

        validateStatusTransition(feedback.getStatus(), request.getStatus());

        feedback.setStatus(request.getStatus());
        if (request.getResolutionRemarks() != null && !request.getResolutionRemarks().trim().isEmpty()) {
            feedback.setResolutionRemarks(request.getResolutionRemarks().trim());
        }
        return feedbackRepository.save(feedback);
    }

    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        long total = feedbackRepository.count();
        long submitted = feedbackRepository.countByStatus(FeedbackStatus.SUBMITTED);
        long inReview = feedbackRepository.countByStatus(FeedbackStatus.IN_REVIEW);
        long resolved = feedbackRepository.countByStatus(FeedbackStatus.RESOLVED);
        long closed = feedbackRepository.countByStatus(FeedbackStatus.CLOSED);

        dto.setTotalFeedbacks(total);
        dto.setSubmittedCount(submitted);
        dto.setInReviewCount(inReview);
        dto.setResolvedCount(resolved);
        dto.setClosedCount(closed);

        double rate = (total > 0) ? ((double) (resolved + closed) / total) * 100.0 : 0.0;
        dto.setResolutionRatePercentage(Math.round(rate * 10.0) / 10.0);

        Map<String, Long> categoryMap = new LinkedHashMap<>();
        for (FeedbackCategory cat : FeedbackCategory.values()) {
            categoryMap.put(cat.name(), feedbackRepository.countByCategory(cat));
        }
        dto.setCategoryDistribution(categoryMap);

        return dto;
    }

    private String generateTicketId() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int rand = 1000 + new Random().nextInt(9000);
        return "PTFP-" + datePart + "-" + rand;
    }

    private void validateStatusTransition(FeedbackStatus current, FeedbackStatus next) {
        if (current == next) return;
        if (current == FeedbackStatus.CLOSED) {
            throw new IllegalStateException("Cannot change status of a CLOSED ticket.");
        }
    }
}
