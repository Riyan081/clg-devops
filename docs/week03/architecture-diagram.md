# Week 3 — Layered System Architecture & DevOps Toolchain

The diagram below details the runtime application architecture and its integration with the surrounding DevOps automation toolchain.

```mermaid
flowchart TD
    subgraph ClientLayer ["1. Presentation Layer (Browser)"]
        UI1["feedback-form.html"]
        UI2["feedback-list.html"]
        UI3["dashboard.html"]
        JS["Vanilla JS fetch() + Chart.js"]
    end

    subgraph ServerLayer ["2. Web & Application Tier (Spring Boot 3 / Tomcat 10)"]
        TC["Embedded / Standalone Tomcat 10"]
        RC["REST Controllers (@RestController)"]
        SVC["Service Layer (Business Logic & Workflow)"]
        SEC["Validation & Role Filters"]
        ACT["Spring Boot Actuator (/actuator/health)"]
    end

    subgraph DataLayer ["3. Persistence Tier"]
        JPA["Spring Data JPA / Hibernate"]
        DB[("MySQL 8 / H2 In-Memory")]
    end

    subgraph DevOpsToolchain ["4. DevOps CI/CD & Infrastructure Toolchain"]
        GIT["Git / GitHub Repo"]
        MVN["Maven 3.9 Build Engine"]
        JENKINS["Jenkins Declarative Pipeline"]
        SEL["Selenium WebDriver Test Suite"]
        DOCKER["Docker Engine & Docker Hub"]
        ANSIBLE["Ansible Automation Engine"]
    end

    UI1 & UI2 & UI3 --> JS
    JS -->|HTTP REST JSON Calls| TC
    TC --> RC
    RC --> SEC --> SVC
    SVC --> JPA
    JPA --> DB
    TC --> ACT

    GIT --> JENKINS
    JENKINS --> MVN
    JENKINS --> SEL
    JENKINS --> DOCKER
    DOCKER --> ANSIBLE
    ANSIBLE -->|Provisions & Deploys| TC
```

> **Single Deployable Unit Note**: The static frontend (`.html`, `.css`, `.js`) resides in `src/main/resources/static/` inside the Spring Boot application package. There is no separate frontend node server; Spring Boot serves the static files and the REST API from the exact same origin, eliminating CORS configurations and ensuring a single unified `.war`/`.jar` artifact.
