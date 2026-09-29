# Week 10 — Continuous Testing & Quality Gate in Jenkins

## 1. Quality Gate Configuration
In `Jenkinsfile`, the `Selenium UI Quality Gate` stage executes the automated browser test suite before container build and deployment. If any Selenium assertion fails:
1. The test stage aborts with status `FAILED`.
2. Downstream stages (`Docker Build`, `Docker Push`, `Deploy Container`) are bypassed.
3. The build is marked `RED` (Unstable/Failed).

---

## 2. Intentional Defect Injection & Fix Cycle

### Step 1: Defect Introduced (Broken Validation)
- In `FeedbackRequest.java`, removed `@NotBlank` on description and changed category validation logic.
- Committed and pushed to `develop`.
- **Result**: `ValidationSeleniumTest` failed because invalid submission succeeded without showing error.
- **Pipeline Status**: `FAILED` at `Selenium UI Quality Gate` stage; container deployment blocked.

### Step 2: Defect Resolution
- Reinstated validation constraints in `FeedbackRequest.java` and added client-side validation fallback in `feedback-form.js`.
- Committed: `fix(validation): restore required field checks to pass Selenium regression gate`.
- Pushed to `develop`.
- **Result**: All 5 tests passed; pipeline progressed to `Docker Build` and deployed successfully.
