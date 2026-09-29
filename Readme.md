# Automated Public Transport Feedback Portal
> **Full-Stack Java Web Application with Automated DevOps Lifecycle (Git &bull; Jenkins &bull; Selenium &bull; Docker &bull; Ansible)**

---

## 1. Project Overview
The **Automated Public Transport Feedback Portal** is an end-to-end digital grievance and operational tracking system for urban transit networks (Buses, Metro, and Suburban Trains). It enables commuters to report service issues in real time, allows transport officers to triage complaints through a defined state workflow, and gives administrators an executive analytics dashboard.

---

## 2. Technology Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 17 (LTS), Spring Boot 3.2.3, Spring Data JPA, Hibernate |
| **Frontend** | Plain HTML5, CSS3, Vanilla JavaScript (`fetch()` API) |
| **Build & Packaging** | Apache Maven 3.9 (`.war` / `.jar`) |
| **Database** | H2 (in-memory dev/CI) & MySQL 8 (production profile) |
| **Testing** | JUnit 5, Mockito, Selenium WebDriver 4.23 (Headless Chrome) |
| **CI/CD** | Jenkins Declarative Pipeline (`Jenkinsfile`) |
| **Containerization** | Docker (Multi-stage build) & Docker Compose |
| **Configuration Management** | Ansible (`playbook.yml`, `rollback.yml`) |

---

## 3. Directory Layout

```
public-transport-feedback-portal/
├── src/
│   ├── main/
│   │   ├── java/com/ptfp/
│   │   │   ├── controller/      # REST Controllers (@RestController)
│   │   │   ├── service/         # Business Logic & Status Workflow
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── model/           # JPA Entities (Feedback, Route)
│   │   │   ├── dto/             # Data Transfer Objects & Validation
│   │   │   └── PtfpApplication.java
│   │   └── resources/
│   │       ├── application.yml  # Multi-profile configurations
│   │       └── static/          # Plain Frontend UI (HTML, CSS, JS)
│   └── test/
│       └── java/com/ptfp/
│           ├── unit/            # Unit & Mockito Controller/Service Tests
│           └── selenium/        # Headless Selenium Automated UI Tests
├── docker/
│   └── Dockerfile               # Multi-stage Maven + Temurin JRE Build
├── docker-compose.yml           # Multi-container orchestration
├── ansible/
│   ├── inventory.ini            # Target staging/prod host inventory
│   ├── playbook.yml             # Idempotent provisioning playbook
│   └── rollback.yml             # Automated rollback playbook
├── docs/                        # Complete 15-week deliverable documentation
│   ├── week01/ ... week15/
├── Jenkinsfile                  # Pipeline-as-Code with Selenium Quality Gate
└── pom.xml                      # Maven Build Manifest
```

---

## 4. Quick Start & Local Execution

### Prerequisites
- JDK 17+
- Apache Maven 3.9+
- Google Chrome (for Selenium tests)

### Run Application Locally
```bash
# 1. Clone repository
git clone https://github.com/your-username/public-transport-feedback-portal.git
cd public-transport-feedback-portal

# 2. Build & run with Maven
mvn spring-boot:run
```
Visit **`http://localhost:8080`** in your browser:
- **Home**: `http://localhost:8080/index.html`
- **Submit Feedback**: `http://localhost:8080/feedback-form.html`
- **Feedback Registry & Workflow**: `http://localhost:8080/feedback-list.html`
- **Analytics Dashboard**: `http://localhost:8080/dashboard.html`
- **Health Check Endpoint**: `http://localhost:8080/actuator/health`

### Run Automated Tests
```bash
# Run unit tests
mvn test -Dtest=*ServiceTest,*ControllerTest

# Run full headless Selenium test suite
mvn test -Dtest=*SeleniumTest
```

### Run via Docker
```bash
# Build & start container
docker-compose up --build -d

# Visit deployed app at http://localhost:9090
```

---

## 5. 15-Week Deliverables Index

All weekly deliverables, screenshots, and logs are organized inside the `docs/` folder:
- **[Week 01: Problem Statement & MVP Scope](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week01/problem-statement.md)**
- **[Week 02: Agile Planning & DevOps Workflow](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week02/user-stories-acceptance-criteria.md)**
- **[Week 03: Architecture & Tech Setup](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week03/architecture-diagram.md)**
- **[Week 04: Git Repository & Branching Policy](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week04/branching-policy.md)**
- **[Week 05: Feature 1 Development & PR Review](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week05/feature-1-submit-feedback.md)**
- **[Week 06: MVP Baseline & Conflict Resolution](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week06/merge-conflict-resolution.md)**
- **[Week 07: Jenkins CI Installation](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week07/jenkins-installation-ci.md)**
- **[Week 08: Pipeline as Code (Jenkinsfile)](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week08/pipeline-as-code-report.md)**
- **[Week 09: Selenium Test Suite Design](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week09/test-plan.md)**
- **[Week 10: Jenkins Quality Gate Automation](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week10/continuous-testing-quality-gate.md)**
- **[Week 11: Docker Container Lifecycle](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week11/container-lifecycle-log.md)**
- **[Week 12: Automated Commit-to-Container CD](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week12/jenkins-docker-cd.md)**
- **[Week 13: Ansible Server Provisioning](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week13/server-prerequisites.md)**
- **[Week 14: Automated Idempotency & Rollback](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week14/idempotency-and-rollback-validation.md)**
- **[Week 15: Final DevOps Report & Viva Guide](file:///c:/Users/riyan/OneDrive/Desktop/Devops/docs/week15/final-devops-report.md)**
