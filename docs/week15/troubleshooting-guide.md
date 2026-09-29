# Week 15 — DevOps Troubleshooting & Runbook Guide

## Top 5 Issues Encountered & Resolutions

### 1. Selenium Headless Chrome Hanging in CI / Docker
- **Symptom**: Selenium tests timeout when executed in Jenkins container or headless environments.
- **Root Cause**: Linux containers lack shared memory and display servers.
- **Fix**: Added essential Chrome options in test setup:
  ```java
  options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
  ```

### 2. Async `fetch()` UI Assertion Race Conditions
- **Symptom**: Selenium asserting table or success alerts immediately after click failed intermittently.
- **Root Cause**: Client-side `fetch()` resolves asynchronously; DOM is not populated synchronously.
- **Fix**: Replaced static waits with explicit `WebDriverWait` targeting `ExpectedConditions.presenceOfElementLocated`.

### 3. Docker-in-Docker Socket Permission in Jenkins
- **Symptom**: Jenkins pipeline failed at `docker build` stage with `Cannot connect to the Docker daemon`.
- **Root Cause**: Jenkins container was isolated from host Docker engine.
- **Fix**: Mounted host Docker socket when launching Jenkins container:
  ```bash
  -v /var/run/docker.sock:/var/run/docker.sock
  ```

### 4. Git Merge Conflict on Rebase
- **Symptom**: Merge conflict markers in `FeedbackController.java` when integrating parallel feature branches.
- **Root Cause**: Concurrent endpoint additions on identical line numbers.
- **Fix**: Rebased branch, manually retained both method signatures, staged file, and completed `git rebase --continue`.

### 5. Tomcat 10 vs Spring Boot 3 Jakarta Servlet Spec
- **Symptom**: ClassNotFoundException on `javax.servlet.Filter` when deploying to external Tomcat.
- **Root Cause**: Spring Boot 3 migrated to Jakarta EE 10 (`jakarta.servlet.*`).
- **Fix**: Extended `SpringBootServletInitializer` and targeted Tomcat 10.1.x base image.
