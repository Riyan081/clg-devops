# Week 6 — Intentional Merge Conflict Creation & Resolution

## 1. Conflict Simulation Setup
To demonstrate team collaboration and conflict resolution, two parallel branches modified `FeedbackController.java` at the same line:
- Branch `feature/14-update-status-workflow` added `@PutMapping("/{id}/status")`.
- Branch `feature/15-summary-dashboard` added `@GetMapping("/summary")` directly at the same location.

---

## 2. Conflict Evidence (Before Resolution)
When rebasing `feature/15-summary-dashboard` onto `develop` after `feature/14` was merged, Git stopped with:

```java
<<<<<<< HEAD (develop)
    @PutMapping("/{id}/status")
    public ResponseEntity<Feedback> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(feedbackService.updateStatus(id, request));
    }
=======
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(feedbackService.getDashboardSummary());
    }
>>>>>>> feature/15-summary-dashboard
```

---

## 3. Resolution Steps & Final Clean Code
1. Open `FeedbackController.java` in editor.
2. Maintain both endpoints cleanly in their respective logical blocks.
3. Remove conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`).
4. Stage and complete rebase:
```bash
git add src/main/java/com/ptfp/controller/FeedbackController.java
git rebase --continue
git push --force-with-lease origin feature/15-summary-dashboard
```
Both endpoints now coexist cleanly and all tests pass.
