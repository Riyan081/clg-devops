# Week 5 — Pull Request & Code Review Evidence

## Pull Request Details
- **Title**: `feat: Commuter feedback submission and validation (#12)`
- **Source Branch**: `feature/12-submit-feedback`
- **Target Branch**: `develop`
- **Status**: Merged

---

## Inline Code Review Log

### Review Comment 1 (Input Validation):
> **Reviewer (Lead)**: *Ensure description length is bounded to avoid SQL column overflows if payload size increases.*  
> **Author Response**: *Added `@Size(min = 5, max = 2000)` constraint in `FeedbackRequest.java` and `TEXT` column mapping in `Feedback.java`.* [Resolved]

### Review Comment 2 (Async UI Wait):
> **Reviewer (QA)**: *In `feedback-form.js`, ensure submit button is disabled during submission to prevent duplicate double-clicks.*  
> **Author Response**: *Implemented button state toggle during active fetch request.* [Resolved]

---

## Merge Command
```bash
git checkout develop
git merge --squash feature/12-submit-feedback
git commit -m "feat(feedback): merge commuter feedback submission form and REST endpoint (#12)"
git push origin develop
```
