package com.ptfp.repository;

import com.ptfp.model.Feedback;
import com.ptfp.model.FeedbackCategory;
import com.ptfp.model.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    Optional<Feedback> findByTicketId(String ticketId);
    
    List<Feedback> findByStatus(FeedbackStatus status);
    
    List<Feedback> findByCategory(FeedbackCategory category);
    
    long countByStatus(FeedbackStatus status);
    
    long countByCategory(FeedbackCategory category);

    @Query("SELECT f FROM Feedback f WHERE " +
           "(:status IS NULL OR f.status = :status) AND " +
           "(:category IS NULL OR f.category = :category) AND " +
           "(:search IS NULL OR LOWER(f.ticketId) LIKE LOWER(CONCAT('%', :search, '%')) " +
           " OR LOWER(f.routeCode) LIKE LOWER(CONCAT('%', :search, '%')) " +
           " OR LOWER(f.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> searchFeedbacks(
            @Param("status") FeedbackStatus status,
            @Param("category") FeedbackCategory category,
            @Param("search") String search
    );
}
