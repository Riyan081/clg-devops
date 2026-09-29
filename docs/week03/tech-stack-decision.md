# Week 3 — Technology Stack Decision Matrix & Justification

| Layer | Choice | Justification |
|---|---|---|
| **Language** | **Java 17 (LTS)** | Industry-standard LTS release, supported natively by Maven, Tomcat 10, Jenkins agents, and Docker Eclipse Temurin images. |
| **Framework** | **Spring Boot 3.x + Spring Web** (`@RestController`) | Scaffolds REST endpoints in seconds, provides embedded Tomcat for local dev, packages cleanly into a `.war` for external Tomcat, and enables easy integration testing. |
| **Build Tool** | **Apache Maven** | Direct Jenkins pipeline plugin support, standardized `pom.xml` lifecycle (`clean`, `test`, `package`), and seamless Selenium test execution. |
| **Database** | **MySQL 8** (Staging/Production) / **H2 Database** (CI & In-Memory Unit/Integration Testing) | Supports production relational persistence while providing an embedded, zero-dependency database for headless CI/CD runs. |
| **Persistence** | **Spring Data JPA + Hibernate** | Eliminates boilerplate SQL/JDBC code, provides declarative repository interfaces, and supports automatic schema creation (`ddl-auto: update`). |
| **Frontend** | **Plain HTML5 + CSS3 + Vanilla JavaScript** (`fetch()` API) | Stored directly in `src/main/resources/static/` and auto-served by Spring Boot. Avoids heavy Node/React build tools, removes CORS complexities (same origin), and packages the entire portal into **one single build artifact**. |
| **Version Control** | **Git & GitHub** | Branch protection policies, pull request workflows with code review comments, and GitHub Issue tracking. |
| **CI/CD** | **Jenkins** | Freestyle CI job in Week 7, migrating to version-controlled declarative `Jenkinsfile` in Week 8+. |
| **Testing** | **Selenium WebDriver + JUnit 5 + WebDriverManager** | Automates end-to-end browser user journeys with automated ChromeDriver management and screenshot capture on failure. |
| **Containerization** | **Docker + Docker Hub** | Multi-stage build (`maven:3.9-eclipse-temurin-17` → `tomcat:10.1-jdk17-temurin`), image versioning by build number, and registry distribution. |
| **Configuration Management** | **Ansible** | Agentless, YAML-based configuration over SSH. Far lower friction and overhead than Puppet master-agent infrastructure. |
| **Deployment Target** | **Apache Tomcat 10 / Dockerized Tomcat** | Meets deployment criteria and cleanly hosts the packaged `.war` archive. |
