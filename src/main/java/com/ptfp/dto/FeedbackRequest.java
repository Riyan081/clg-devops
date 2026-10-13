package com.ptfp.dto;

import com.ptfp.model.FeedbackCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FeedbackRequest {

    @NotBlank(message = "Route code is required")
    private String routeCode;

    @NotNull(message = "Category is required")
    private FeedbackCategory category;

    @Size(max = 100, message = "Passenger name cannot exceed 100 characters")
    private String passengerName;

    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String passengerEmail;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 5, max = 2000, message = "Description must be between 5 and 2000 characters")
    private String description;

    private String imageUrl;

    public FeedbackRequest() {}

    public String getRouteCode() { return routeCode; }
    public void setRouteCode(String routeCode) { this.routeCode = routeCode; }

    public FeedbackCategory getCategory() { return category; }
    public void setCategory(FeedbackCategory category) { this.category = category; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
