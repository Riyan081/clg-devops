package com.ptfp.dto;

import java.util.Map;

public class DashboardSummaryDTO {
    private long totalFeedbacks;
    private long submittedCount;
    private long inReviewCount;
    private long resolvedCount;
    private long closedCount;
    private double resolutionRatePercentage;
    private Map<String, Long> categoryDistribution;

    public DashboardSummaryDTO() {}

    public long getTotalFeedbacks() { return totalFeedbacks; }
    public void setTotalFeedbacks(long totalFeedbacks) { this.totalFeedbacks = totalFeedbacks; }

    public long getSubmittedCount() { return submittedCount; }
    public void setSubmittedCount(long submittedCount) { this.submittedCount = submittedCount; }

    public long getInReviewCount() { return inReviewCount; }
    public void setInReviewCount(long inReviewCount) { this.inReviewCount = inReviewCount; }

    public long getResolvedCount() { return resolvedCount; }
    public void setResolvedCount(long resolvedCount) { this.resolvedCount = resolvedCount; }

    public long getClosedCount() { return closedCount; }
    public void setClosedCount(long closedCount) { this.closedCount = closedCount; }

    public double getResolutionRatePercentage() { return resolutionRatePercentage; }
    public void setResolutionRatePercentage(double resolutionRatePercentage) { this.resolutionRatePercentage = resolutionRatePercentage; }

    public Map<String, Long> getCategoryDistribution() { return categoryDistribution; }
    public void setCategoryDistribution(Map<String, Long> categoryDistribution) { this.categoryDistribution = categoryDistribution; }
}
