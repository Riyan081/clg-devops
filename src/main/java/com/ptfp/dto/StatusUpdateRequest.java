package com.ptfp.dto;

import com.ptfp.model.FeedbackStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StatusUpdateRequest {

    @NotNull(message = "New status is required")
    private FeedbackStatus status;

    @Size(max = 2000, message = "Remarks cannot exceed 2000 characters")
    private String resolutionRemarks;

    public StatusUpdateRequest() {}

    public StatusUpdateRequest(FeedbackStatus status, String resolutionRemarks) {
        this.status = status;
        this.resolutionRemarks = resolutionRemarks;
    }

    public FeedbackStatus getStatus() { return status; }
    public void setStatus(FeedbackStatus status) { this.status = status; }

    public String getResolutionRemarks() { return resolutionRemarks; }
    public void setResolutionRemarks(String resolutionRemarks) { this.resolutionRemarks = resolutionRemarks; }
}
