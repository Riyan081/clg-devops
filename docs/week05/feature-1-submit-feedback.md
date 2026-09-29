# Week 5 — Feature 1: Feedback Submission & Tracking

## 1. Feature Overview
Feature 1 (`feature/12-submit-feedback`) delivers the core commuter capability: submitting categorized grievances with route codes, receiving a persistent Ticket ID, and saving records with `SUBMITTED` status.

## 2. Implemented Components
- **REST Endpoint**: `POST /api/feedback` in `FeedbackController.java`
- **Validation**: `@NotBlank` on routeCode & description, `@NotNull` on category in `FeedbackRequest.java`
- **UI Form**: `src/main/resources/static/feedback-form.html` & `feedback-form.js`
- **Ticket ID Generator**: `PTFP-YYYYMMDD-XXXX` (e.g. `PTFP-20260908-7241`)

## 3. Git Command Log
```bash
git checkout develop
git checkout -b feature/12-submit-feedback
git add src/main/java/com/ptfp/controller/FeedbackController.java \
        src/main/java/com/ptfp/service/FeedbackService.java \
        src/main/resources/static/feedback-form.html \
        src/main/resources/static/js/feedback-form.js
git commit -m "feat(feedback): implement feedback submission endpoint, validation, and web form"
git push -u origin feature/12-submit-feedback
```
