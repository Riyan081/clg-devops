# Week 2 — 15-Week Agile Sprint Plan

The 15-week timeline is structured into 5 cohesive, three-week sprints.

```
+---------------------------------------------------------------------------------------+
|                                15-WEEK SPRINT SCHEDULE                                |
+-------------------+-------------------+-------------------+-------------------+-------+
| Sprint 1 (W1-W3)  | Sprint 2 (W4-W6)  | Sprint 3 (W7-W9)  | Sprint 4 (W10-W12)| S5(13-15)
| Planning & Arch   | Feature Dev & Git | CI & Test Design  | Testing, Docker/CD| IaC & Rel 
+-------------------+-------------------+-------------------+-------------------+-------+
```

---

## Sprint Breakdown

### Sprint 1: Problem Definition, Architecture & Technology Setup (Weeks 1 – 3)
- **Week 1**: Problem Statement, Stakeholders, MVP Scope Definition.
- **Week 2**: User Stories, Product Backlog, Definition of Done, DevOps Lifecycle Diagram.
- **Week 3**: SRS Summary, Use Case & Layered Architecture Diagrams, Data Model, Maven Skeleton.
- **Milestone 1**: Local Spring Boot + Static Web environment verified running on `localhost:8080`.

### Sprint 2: Core Feature Development & Git Collaboration (Weeks 4 – 6)
- **Week 4**: Git repo initialization, `.gitignore`, branch policy, GitHub Issue templates.
- **Week 5**: Feature 1 branch (`feature/12-submit-feedback`), REST API + HTML form, PR review, merge to `develop`.
- **Week 6**: Complete MVP (View/Search, Status Workflow, Dashboard), intentional merge conflict resolution, release tag `v0.1.0-mvp`.
- **Milestone 2**: Feature-complete MVP baseline tagged and ready for CI integration.

### Sprint 3: Continuous Integration & Test Suite Design (Weeks 7 – 9)
- **Week 7**: Jenkins installation in Docker, JDK 17 & Maven 3.9 configuration, Freestyle CI build job, artifact archiving.
- **Week 8**: Declarative `Jenkinsfile` (Pipeline-as-Code) with checkout, build, package, parameterized deployment.
- **Week 9**: Selenium WebDriver automated UI test suite covering 5 critical journeys, failure screenshot capture.
- **Milestone 3**: Automated Maven CI pipeline + local Selenium test suite executing against UI.

### Sprint 4: Continuous Testing, Dockerization & Automated CD (Weeks 10 – 12)
- **Week 10**: Integrate Selenium into Jenkinsfile, JUnit test reports, quality gate (failed tests abort deploy), defect fix cycle.
- **Week 11**: Multi-stage `Dockerfile`, image build, container run, port mapping (9090:8080), full container lifecycle commands.
- **Week 12**: Jenkins-Docker automated CD: auto-build versioned image, push to Docker Hub, automated container redeployment.
- **Milestone 4**: Zero-touch commit-to-container pipeline operating automatically.

### Sprint 5: Configuration Management, Reliability & Final Release (Weeks 13 – 15)
- **Week 13**: Ansible configuration management (`inventory.ini`, `playbook.yml`) for server prerequisites.
- **Week 14**: Automated provisioning, idempotency proof (`changed=0`), health-check endpoint verification, rollback recovery.
- **Week 15**: Live end-to-end demonstration, final technical documentation, troubleshooting guide, viva defense.
- **Milestone 5**: Production-ready, fully automated release tagged `v1.0.0`.
