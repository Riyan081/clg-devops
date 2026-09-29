# Week 2 — Definition of Done (DoD)

A user story or feature is considered **"DONE"** and ready for production release only when it meets all the following criteria:

## 1. Code Quality & Standards
- [ ] Code adheres to clean Java 17 and Spring Boot conventions.
- [ ] No hardcoded configuration values; all environment-specific configs reside in `application.yml` or environment variables.
- [ ] No compilation warnings or unresolved lint errors.

## 2. Testing & Quality Assurance
- [ ] Unit & integration tests written for all Service and Controller endpoints using JUnit 5 and Mockito.
- [ ] Selenium WebDriver browser tests written and passing for all user interactions.
- [ ] All automated tests pass locally via `mvn test`.
- [ ] Code coverage $\ge 80\%$ on business logic layers.

## 3. Version Control & Peer Review
- [ ] Work done on a dedicated feature branch (`feature/<issue-id>-<short-description>`).
- [ ] Pull Request opened targeting `develop` branch with clear description and issue link (`Closes #XX`).
- [ ] Code review completed with at least 2 constructive review comments and resolutions documented.
- [ ] Branch merged cleanly without broken commits.

## 4. CI/CD & Deployment
- [ ] Jenkins pipeline triggers automatically and completes with `SUCCESS` status (all stages green).
- [ ] Docker image builds successfully and is tagged with the corresponding build number.
- [ ] Container launches and passes health check (`/actuator/health` returns HTTP 200).

## 5. Documentation & Artifacts
- [ ] Markdown documentation updated in `docs/weekXX/`.
- [ ] Screenshots/logs of feature operation and test execution archived.
