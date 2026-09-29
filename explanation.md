# Automated Public Transport Feedback Portal
## Complete Project Explanation Document

---

## 1. What Is This Project?

The **Automated Public Transport Feedback Portal (PTFP)** is a full-stack web application that solves a real-world problem — **commuters using buses, metros, and trains have no structured way to report issues** like delays, dirty coaches, safety concerns, or rude staff behavior. Currently, complaints are either verbal, on paper, or scattered across random social media posts. Transport authorities have no single system to track, resolve, or analyze these complaints.

This application provides:
- **For Commuters** → A web form to submit grievances with route, category, and description. They get an instant tracking ticket ID (e.g., `PTFP-20260929-4521`).
- **For Transport Officers** → A list view where they can triage complaints through a status workflow: `SUBMITTED → IN_REVIEW → RESOLVED → CLOSED`.
- **For Administrators** → An analytics dashboard showing total grievances, counts by status, resolution SLA percentage, and category-wise breakdown.

But this project is **not just about the application** — it is an **end-to-end DevOps lifecycle demonstration**. The real focus is showing how modern software is built, tested, containerized, deployed, and managed using industry-standard DevOps tools and practices.

---

## 2. The Problem We Are Solving

### Pain Points (Before This System)
| Problem | Impact |
|---|---|
| No single channel for complaints | Commuters complain on Twitter, phone, in person — nothing is tracked |
| No tracking or status updates | Commuters never know if their complaint was even seen |
| No analytics or trends | Transport authority can't identify which routes have the most delays |
| Manual paper-based process | Slow, error-prone, no accountability |
| No SLA tracking | No way to measure how fast complaints are resolved |

### How Our System Fixes It
| Feature | Solution |
|---|---|
| Web-based feedback form | Single digital channel for all complaints |
| Unique ticket ID per complaint | Commuters can track status |
| Status workflow (SUBMITTED → CLOSED) | Officers process complaints systematically |
| Dashboard with metrics | Admins see trends, hotspot routes, category breakdown |
| REST API backend | Any future mobile app can plug into the same API |

---

## 3. Technology Stack — What We Used and WHY

### 3.1 Backend: Java 17 + Spring Boot 3.2.3b

**What is it?**
Java is the programming language. Spring Boot is a framework built on top of Java that makes it extremely fast to create web applications. It gives you an embedded Tomcat server, auto-configuration, and built-in support for REST APIs, databases, and testing.

**Why we chose it:**
- **Java 17 is LTS** (Long Term Support) — stable, widely used in enterprise, supported until 2029.
- **Spring Boot** eliminates boilerplate code. Without it, you'd need hundreds of lines of XML config. With Spring Boot, you just add `@RestController` and you have an API.
- **Embedded Tomcat** — the app runs with a single command (`mvn spring-boot:run`), no separate server installation needed for development.
- **WAR packaging** — we can also deploy to an external Tomcat server for production.
- **Works perfectly with Maven, Jenkins, Docker, and Selenium** — the entire DevOps toolchain assumes Java/Maven compatibility.

**How it works in our project:**
```
PtfpApplication.java  →  The main entry point. Starts the embedded Tomcat server.
                          Extends SpringBootServletInitializer so it also works as a WAR.
```

### 3.2 Build Tool: Apache Maven 3.9

**What is it?**
Maven is a build automation tool for Java projects. It handles dependency management (downloading libraries), compilation, testing, and packaging into JAR/WAR files — all from a single `pom.xml` configuration file.

**Why we chose it:**
- **Standard in the Java world** — most Jenkins tutorials, most CI/CD pipelines assume Maven.
- **pom.xml is declarative** — you list what libraries you need, Maven downloads them automatically from Maven Central Repository.
- **Lifecycle phases** — `compile → test → package → deploy` are built-in. Running `mvn package` does everything in order.
- **Surefire plugin** — automatically discovers and runs JUnit/Selenium tests and generates XML reports that Jenkins can parse.

**How it works in our project:**
```
pom.xml  →  Lists all dependencies: Spring Boot, JPA, Selenium, H2, MySQL, Actuator
         →  Configures WAR packaging (not JAR)
         →  Sets final artifact name to "ptfp" (so output is ptfp.war)
         →  Configures Surefire plugin for test discovery
```

**Key Maven commands used:**
| Command | What It Does |
|---|---|
| `mvn clean compile` | Deletes old build output, compiles Java source files |
| `mvn test` | Compiles + runs all unit and Selenium tests |
| `mvn package -DskipTests` | Compiles + packages into ptfp.war (skips tests for speed) |
| `mvn spring-boot:run` | Compiles + starts the app on localhost:8080 |

### 3.3 Database: H2 (Development) + MySQL 8 (Production)

**What is H2?**
H2 is an in-memory Java database. It runs entirely inside the application's memory — no separate database server installation needed. When the app stops, the data disappears.

**What is MySQL?**
MySQL is a full relational database server that stores data permanently on disk.

**Why we use both:**
- **H2 for development and CI/CD** — zero setup. You clone the project, run `mvn spring-boot:run`, and it works immediately. No "install MySQL first" step. Jenkins CI can run tests without needing a database server.
- **MySQL for production** — real data needs to persist. When the app runs in Docker on a real server, it connects to MySQL.
- **Profile switching** — Spring Boot supports profiles. The `application.yml` file has two sections:
  - Default (`dev` profile) → uses H2
  - `prod` profile → uses MySQL

```yaml
# Default H2 (dev)
spring.datasource.url: jdbc:h2:mem:ptfpdb

# Production MySQL (activated with SPRING_PROFILES_ACTIVE=prod)
spring.datasource.url: jdbc:mysql://localhost:3306/ptfp_db
```

**How it works in our project:**
- When you run `mvn spring-boot:run` → H2 starts automatically, tables are created via JPA auto-DDL.
- When Docker runs with `SPRING_PROFILES_ACTIVE=prod` → it connects to MySQL instead.
- The H2 web console is accessible at `http://localhost:8080/h2-console` for debugging.

### 3.4 Persistence: Spring Data JPA + Hibernate

**What is JPA?**
JPA (Java Persistence API) is a specification that maps Java objects to database tables. Instead of writing raw SQL, you define Java classes with annotations like `@Entity`, `@Table`, `@Column`, and JPA automatically creates tables and handles CRUD operations.

**What is Hibernate?**
Hibernate is the actual implementation of JPA. Spring Boot includes it by default.

**What is Spring Data JPA?**
A Spring library that goes even further — you define an interface (like `FeedbackRepository extends JpaRepository`), and Spring automatically generates the implementation with methods like `findById()`, `save()`, `delete()`, `count()`, etc. Zero SQL for basic operations.

**Why we chose it:**
- **No SQL boilerplate** — `feedbackRepository.save(feedback)` does an INSERT. `feedbackRepository.findById(id)` does a SELECT. No raw SQL needed.
- **Custom queries** — for complex searches, we use JPQL (Java Persistence Query Language) with the `@Query` annotation.
- **Database-agnostic** — the same code works with H2, MySQL, PostgreSQL, or any database. Just change the driver and dialect in config.

**How it works in our project:**
```
Feedback.java (Entity)           →  Maps to "feedbacks" table in database
  @Id, @GeneratedValue           →  Auto-increment primary key
  @Enumerated(EnumType.STRING)   →  Stores enum as string (e.g., "SUBMITTED")
  @PrePersist, @PreUpdate        →  Auto-sets createdAt/updatedAt timestamps

FeedbackRepository.java          →  Interface that Spring auto-implements
  findByTicketId(String)         →  Auto-generated: SELECT * FROM feedbacks WHERE ticket_id = ?
  countByStatus(FeedbackStatus)  →  Auto-generated: SELECT COUNT(*) FROM feedbacks WHERE status = ?
  searchFeedbacks(...)           →  Custom @Query with JPQL for filtered search
```

### 3.5 Frontend: Plain HTML5 + CSS3 + Vanilla JavaScript

**What is it?**
The frontend is built with plain HTML files, a CSS stylesheet, and vanilla JavaScript using the `fetch()` API to call the backend REST endpoints. No React, no Angular, no Vue — just raw web technologies.

**Why we chose this approach:**
- **No separate build step** — React/Angular need `npm install` + `npm run build`. Our HTML files just exist and work.
- **Ships inside the same WAR** — the HTML/CSS/JS files live in `src/main/resources/static/`. Spring Boot serves them automatically. Same origin as the API = **no CORS problems**.
- **Single deployable artifact** — everything (backend + frontend) goes into one `ptfp.war` file.
- **Easy to understand** — no JSX, no component trees, no state management. Just DOM manipulation with `document.getElementById()`.

**How it works in our project:**
```
Browser loads index.html
  → HTML renders the page structure
  → CSS (style.css) applies styling
  → JavaScript (api.js + page-specific JS) runs on load
  → JS calls fetch('/api/feedback') to get data from backend
  → JS receives JSON response
  → JS dynamically inserts data into the HTML DOM
```

**The 4 pages:**
| File | Purpose |
|---|---|
| `index.html` | Landing page with navigation and feature cards |
| `feedback-form.html` | Form to submit new feedback (route, category, description) |
| `feedback-list.html` | Table showing all feedbacks with search, filter, and status update |
| `dashboard.html` | Analytics metrics: totals, status breakdown, category distribution |

**The JavaScript files:**
| File | Purpose |
|---|---|
| `api.js` | Shared helper — wraps all `fetch()` calls to the REST API |
| `feedback-form.js` | Loads routes dropdown, handles form submit via POST |
| `feedback-list.js` | Loads feedbacks table, handles search/filter, status change via PUT |
| `dashboard.js` | Fetches dashboard summary and renders metrics on page |

### 3.6 REST API Architecture

**What is REST?**
REST (Representational State Transfer) is an architectural style for web APIs. Each URL represents a resource. You use HTTP methods (GET, POST, PUT, DELETE) to perform operations on those resources. Data is exchanged as JSON.

**Why REST?**
- **Standard** — any client (browser, mobile app, Postman, curl) can call the same API.
- **Stateless** — each request contains all the information needed. No server-side session needed.
- **Testable** — Selenium tests, unit tests, and manual Postman testing all use the same endpoints.

**Our API endpoints:**
| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/feedback` | Create new feedback (commuter submits) |
| `GET` | `/api/feedback` | List all feedbacks (with optional filters) |
| `GET` | `/api/feedback/{id}` | Get single feedback by database ID |
| `GET` | `/api/feedback/ticket/{ticketId}` | Get feedback by ticket ID |
| `PUT` | `/api/feedback/{id}/status` | Update status (officer workflow) |
| `GET` | `/api/feedback?status=X&category=Y&search=Z` | Search and filter feedbacks |
| `GET` | `/api/dashboard/summary` | Get dashboard analytics |
| `GET` | `/api/routes` | Get list of transit routes |
| `GET` | `/actuator/health` | Application health check (DevOps) |

### 3.7 Spring Boot Actuator

**What is it?**
Actuator is a Spring Boot module that automatically adds production-ready endpoints for monitoring. The most important one is `/actuator/health` which returns `{"status": "UP"}` if the application and database are working.

**Why we use it:**
- **Health checks** — the Ansible playbook calls this endpoint after deployment to verify the app is actually running, not just that the container started.
- **Docker healthcheck** — the Dockerfile includes `HEALTHCHECK` that curls this endpoint.
- **Jenkins pipeline** — the final stage curls health to confirm deployment success.

---

## 4. DevOps Tools — What They Are and Why We Use Them

### 4.1 Git + GitHub

**What is Git?**
Git is a distributed version control system. It tracks every change to every file, who made the change, and when. You can go back to any previous version at any time.

**What is GitHub?**
GitHub is a cloud platform that hosts Git repositories and adds collaboration features: pull requests, code reviews, issue tracking, project boards.

**Why we use them:**
- **Version history** — every change is recorded with a commit message. You can see exactly what code existed at any point in time.
- **Branching** — work on features in isolation without breaking the main code. Multiple developers can work simultaneously.
- **Pull requests** — before merging code, someone reviews it. This catches bugs early.
- **Integration with Jenkins** — Jenkins can automatically trigger a build whenever code is pushed to GitHub.

**How they work in our project:**
```
Branching Strategy:
  main     →  Production-ready code. Protected — requires PR + review to merge.
  develop  →  Integration branch. Features merge here first.
  feature/12-submit-feedback  →  Individual feature work
  bugfix/fix-validation       →  Bug fixes
  release/v1.0.0              →  Release preparation
  hotfix/urgent-fix           →  Emergency production fixes
```

**Key Git concepts demonstrated:**
- **Feature branching** — each feature developed on its own branch
- **Pull requests** — code reviewed before merging
- **Merge conflicts** — demonstrated and resolved (Week 6)
- **Tags** — `v0.1.0-mvp`, `v1.0.0` mark release points
- **Issue templates** — standardized bug reports and feature requests

### 4.2 Jenkins (CI/CD Pipeline)

**What is Jenkins?**
Jenkins is an open-source automation server. It automates the entire software delivery pipeline: pulling code from Git, compiling it, running tests, building Docker images, and deploying containers.

**What is CI/CD?**
- **CI (Continuous Integration)** — every code change is automatically built and tested. If tests fail, the developer is notified immediately.
- **CD (Continuous Deployment)** — after tests pass, the application is automatically deployed to staging/production without manual intervention.

**Why we use Jenkins:**
- **Automation** — no human needs to manually compile, test, and deploy. Push code → everything happens automatically.
- **Quality gate** — if Selenium tests fail, deployment is blocked. Broken code never reaches production.
- **Reproducibility** — every build follows the exact same steps. No "it works on my machine" problems.
- **Pipeline as Code** — the entire pipeline is defined in a `Jenkinsfile` stored in Git. The pipeline itself is version-controlled.

**How it works in our project:**

```
Developer pushes code to GitHub
        ↓
Jenkins detects the change (webhook or polling)
        ↓
Stage 1: CHECKOUT      →  Pulls latest code from Git
        ↓
Stage 2: COMPILE       →  mvn clean compile (checks for syntax errors)
        ↓
Stage 3: UNIT TESTS    →  Runs JUnit + Mockito tests
        ↓                  Publishes test reports
        ↓                  ❌ If tests fail → PIPELINE STOPS HERE
        ↓
Stage 4: PACKAGE       →  mvn package → creates ptfp.war
        ↓                  Archives the artifact in Jenkins
        ↓
Stage 5: SELENIUM      →  Runs 5 headless browser tests
        ↓                  Screenshots on failure
        ↓                  ❌ If tests fail → PIPELINE STOPS HERE (Quality Gate)
        ↓
Stage 6: DOCKER BUILD  →  Builds Docker image with BUILD_NUMBER tag
        ↓
Stage 7: DOCKER PUSH   →  Pushes image to Docker Hub registry
        ↓
Stage 8: DEPLOY        →  Stops old container, starts new one
        ↓
Stage 9: HEALTH CHECK  →  Curls /actuator/health to verify deployment
        ↓
        ✅ SUCCESS — App is live!
```

**Key Jenkins concepts:**
| Concept | Explanation |
|---|---|
| Freestyle job | Simple UI-configured build job (used in Week 7 for learning) |
| Pipeline job | Advanced job defined by Jenkinsfile (used from Week 8 onwards) |
| Jenkinsfile | Groovy script that defines the entire pipeline as code |
| Parameters | `DEPLOY_ENV` (staging/production) and `RUN_SELENIUM_TESTS` (on/off) |
| Post actions | `success {}` and `failure {}` blocks run after pipeline completes |
| Quality gate | Tests must pass before deploy stage executes |
| Artifact archiving | The WAR file is stored in Jenkins for download/rollback |

### 4.3 Selenium WebDriver

**What is Selenium?**
Selenium is a browser automation framework. It programmatically controls a real web browser (Chrome, Firefox) — navigating to pages, clicking buttons, filling forms, and reading page content. It's used to write automated UI tests that verify the application works from the user's perspective.

**Why we use Selenium:**
- **End-to-end testing** — unit tests check individual functions. Selenium tests check the entire flow: open browser → fill form → click submit → verify success message. This catches bugs that unit tests miss (e.g., JavaScript errors, broken HTML IDs, API integration issues).
- **Regression safety** — every time code changes, Selenium re-tests all critical user journeys. If someone accidentally breaks the form, the test catches it immediately.
- **CI integration** — Selenium runs in headless mode (no visible browser window) inside Jenkins, so it works in automated environments.

**How it works in our project:**
```
1. WebDriverManager.chromedriver().setup()  →  Auto-downloads correct ChromeDriver version
2. ChromeOptions → --headless=new           →  Runs Chrome without visible window (for CI)
3. driver.get("http://localhost:8080/...")   →  Opens the page
4. driver.findElement(By.id("route"))       →  Finds form elements by HTML id
5. element.sendKeys("text")                 →  Types into input fields
6. element.click()                          →  Clicks buttons
7. WebDriverWait + ExpectedConditions       →  Waits for async JavaScript to finish
8. assertTrue(element.getText().contains()) →  Asserts expected content appears
9. On failure → captureScreenshot()         →  Saves PNG to target/selenium-screenshots/
```

**Our 5 Selenium test classes:**
| Test Class | What It Verifies |
|---|---|
| `SubmitFeedbackSeleniumTest` | Commuter can fill and submit the feedback form, gets ticket ID |
| `SearchFeedbackSeleniumTest` | Feedback list filters correctly by status/category/search |
| `StatusWorkflowSeleniumTest` | Officer can change status from SUBMITTED to IN_REVIEW |
| `ValidationSeleniumTest` | Submitting with empty required fields shows error message |
| `DashboardSeleniumTest` | Dashboard page loads and displays metric numbers |

### 4.4 Docker

**What is Docker?**
Docker is a containerization platform. It packages your application along with everything it needs (JRE, config files, dependencies) into a single container image. This container runs identically on any machine — your laptop, a CI server, a cloud VM — eliminating "works on my machine" problems.

**What is a Docker Image?**
A read-only template containing the application and its environment. Like a snapshot of a configured machine.

**What is a Docker Container?**
A running instance of an image. You can start, stop, restart, and remove containers.

**Why we use Docker:**
- **Consistency** — the same image runs everywhere. Dev, staging, production — identical environment.
- **Isolation** — the app runs in its own container with its own filesystem, network, and process space. No conflicts with other applications on the same machine.
- **Easy deployment** — `docker run` and the app is live. No JDK installation, no Tomcat configuration on the target server.
- **Versioning** — every Jenkins build creates a new image tagged with the build number. Rollback = just run the previous tag.
- **Scalability** — need 10 instances? Run 10 containers from the same image.

**How our Dockerfile works (Multi-stage build):**
```dockerfile
# STAGE 1: BUILD (temporary, thrown away after build)
FROM maven:3.9-eclipse-temurin-17 AS builder
  → Uses a Maven+JDK image (large, ~800MB)
  → Copies pom.xml first, downloads dependencies (cached layer)
  → Copies source code, runs mvn package → creates ptfp.war

# STAGE 2: RUNTIME (final image, small)
FROM eclipse-temurin:17-jre-jammy
  → Uses only JRE (not full JDK) → much smaller image (~200MB)
  → Creates non-root user "ptfp" for security
  → Copies ptfp.war from Stage 1
  → Exposes port 8080
  → Adds HEALTHCHECK
  → Runs: java -jar /app/ptfp.war
```

**Why multi-stage?**
The build stage needs Maven + full JDK (~800MB). But to run the app, you only need JRE (~200MB). Multi-stage keeps the final image small by throwing away build tools.

**Docker lifecycle commands:**
| Command | What It Does |
|---|---|
| `docker build -t ptfp:1.0 -f docker/Dockerfile .` | Builds image from Dockerfile |
| `docker images` | Lists all local images |
| `docker run -d --name ptfp-container -p 9090:8080 ptfp:1.0` | Starts container, maps port 9090→8080 |
| `docker ps` | Lists running containers |
| `docker logs ptfp-container` | Shows application console output |
| `docker exec -it ptfp-container /bin/bash` | Opens shell inside running container |
| `docker stop ptfp-container` | Stops the container |
| `docker start ptfp-container` | Starts a stopped container |
| `docker restart ptfp-container` | Restarts the container |
| `docker rm -f ptfp-container` | Removes (deletes) the container |
| `docker push user/ptfp:1.0` | Uploads image to Docker Hub registry |

**Docker Compose:**
Docker Compose lets you define multi-container applications in a YAML file. Our `docker-compose.yml` defines the app service with port mapping, environment variables, restart policy, and health check — all in one file. Start everything with `docker-compose up`.

### 4.5 Ansible (Configuration Management)

**What is Ansible?**
Ansible is an agentless configuration management and automation tool. It connects to remote servers via SSH and ensures they are in the desired state — packages installed, users created, services running, firewall configured, containers deployed.

**What does "agentless" mean?**
Unlike Puppet or Chef, Ansible does NOT require installing any agent software on the target server. It just needs SSH access. This makes setup dramatically simpler.

**What is "idempotency"?**
A critical concept in configuration management. It means: **running the same playbook 10 times produces the exact same result as running it once.** If a package is already installed, Ansible skips it. If a user already exists, Ansible skips it. The second run shows `changed=0`.

**Why Ansible over Puppet:**
- **Agentless** — only needs SSH to the target. Puppet requires a Puppet agent installed on every server.
- **YAML-based** — easy to read and write. Puppet uses its own DSL (Domain Specific Language).
- **Faster setup** — install Ansible on one machine, write a playbook, run it. Puppet needs a Puppet master server.
- **Python under the hood** — uses Python modules, widely available on Linux servers.

**How it works in our project:**

**Inventory file (`inventory.ini`):**
```ini
[ptfp_staging]
staging-server ansible_host=127.0.0.1 ansible_connection=local

[ptfp_production]
prod-server ansible_host=192.168.1.100 ansible_user=deploy
```
This tells Ansible which servers to configure and how to connect.

**Playbook (`playbook.yml`) — 7 tasks:**
| Task | What It Does | Why |
|---|---|---|
| 1. Install packages | Installs curl, git, JDK 17, ufw | Server needs these to run/manage the app |
| 2. Create deploy user | Creates `deploy` user with sudo | Security — never run apps as root |
| 3. Create directories | Creates /opt/ptfp and /opt/ptfp/logs | Standard app directory structure |
| 4. Start Docker | Ensures Docker daemon is running and enabled | Need Docker to run containers |
| 5. Open firewall port | Allows port 9090 through UFW | External access to the app |
| 6. Deploy container | Pulls Docker image, starts container | The actual application deployment |
| 7. Health check | Curls /actuator/health, retries 6 times | Verifies deployment success |

**Rollback playbook (`rollback.yml`):**
```
Step 1: Stop and remove the broken container
Step 2: Redeploy using the previous stable image version
Step 3: Health check to verify rollback success
```

Run with: `ansible-playbook -i ansible/inventory.ini ansible/rollback.yml -e target_version=5`

---

## 5. Application Architecture — How Everything Connects

```
┌─────────────────────────────────────────────────────────┐
│                       BROWSER                            │
│   index.html  feedback-form.html  feedback-list.html     │
│   dashboard.html                                         │
│         │           │              │            │        │
│         └─── JavaScript fetch() calls ──────────┘        │
│                        │                                 │
└────────────────────────┼─────────────────────────────────┘
                         │ HTTP (JSON)
                         ▼
┌─────────────────────────────────────────────────────────┐
│              SPRING BOOT APPLICATION (port 8080)         │
│                                                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │  REST Controllers (@RestController)               │   │
│  │  • FeedbackController  → /api/feedback            │   │
│  │  • DashboardController → /api/dashboard/summary   │   │
│  │  • RouteController     → /api/routes              │   │
│  └──────────────────┬───────────────────────────────┘   │
│                     │                                    │
│  ┌──────────────────▼───────────────────────────────┐   │
│  │  Service Layer (Business Logic)                   │   │
│  │  • FeedbackService                                │   │
│  │    - createFeedback() → generates ticket ID       │   │
│  │    - searchFeedbacks() → filter by status/cat     │   │
│  │    - updateStatus() → validates state transition  │   │
│  │    - getDashboardSummary() → aggregate metrics    │   │
│  └──────────────────┬───────────────────────────────┘   │
│                     │                                    │
│  ┌──────────────────▼───────────────────────────────┐   │
│  │  Repository Layer (Spring Data JPA)               │   │
│  │  • FeedbackRepository (auto-implemented CRUD)     │   │
│  │  • RouteRepository                                │   │
│  └──────────────────┬───────────────────────────────┘   │
│                     │                                    │
│  ┌──────────────────▼───────────────────────────────┐   │
│  │  Database                                         │   │
│  │  • H2 (dev/CI) or MySQL (production)              │   │
│  │  • Tables: feedbacks, routes                      │   │
│  └──────────────────────────────────────────────────┘   │
│                                                          │
│  Spring Boot Actuator → /actuator/health                 │
└─────────────────────────────────────────────────────────┘
```

### Request Flow Example: Commuter Submits Feedback

```
1. User fills the form on feedback-form.html and clicks "Submit"
       ↓
2. feedback-form.js runs: fetch('/api/feedback', {method: 'POST', body: JSON.stringify(data)})
       ↓
3. HTTP POST hits FeedbackController.createFeedback()
       ↓
4. @Valid annotation triggers bean validation (checks @NotBlank, @NotNull, @Size)
       ↓ If validation fails → returns 400 Bad Request with error details
       ↓ If validation passes ↓
5. FeedbackService.createFeedback() runs:
   - Generates ticket ID: "PTFP-20260929-4521"
   - Sets status to SUBMITTED
   - Calls feedbackRepository.save(feedback)
       ↓
6. Hibernate converts the Feedback object to SQL: INSERT INTO feedbacks (...)
       ↓
7. H2/MySQL stores the record
       ↓
8. Saved Feedback object (with generated ID) returned back up the chain
       ↓
9. Controller returns ResponseEntity with status 201 CREATED + JSON body
       ↓
10. JavaScript receives the JSON, shows green "Success!" message with ticket ID
```

---

## 6. Data Model

### Feedback Entity
| Column | Type | Constraints | Description |
|---|---|---|---|
| id | Long | Primary Key, Auto-increment | Database row ID |
| ticketId | String(32) | Unique, Not Null | Human-readable ID: PTFP-YYYYMMDD-XXXX |
| routeCode | String(32) | Not Null | Route code (e.g., ROUTE-101, METRO-BLUE) |
| category | Enum | Not Null | DELAY, CLEANLINESS, SAFETY, STAFF, OVERCROWDING, OTHER |
| passengerName | String(100) | Optional | Commuter's name |
| passengerEmail | String(100) | Optional | Commuter's email |
| description | String(2000) | Not Null, Min 5 chars | Issue description |
| status | Enum | Not Null | SUBMITTED, IN_REVIEW, RESOLVED, CLOSED |
| resolutionRemarks | String(2000) | Optional | Officer's notes when resolving |
| createdAt | DateTime | Not Null, Auto-set | Record creation timestamp |
| updatedAt | DateTime | Not Null, Auto-set | Last modification timestamp |

### Route Entity
| Column | Type | Description |
|---|---|---|
| id | Long | Primary Key |
| routeCode | String | Unique code (ROUTE-101) |
| routeName | String | Full name (Downtown Express Bus) |
| transportMode | String | BUS, METRO, or TRAIN |

### Status Workflow
```
SUBMITTED  →  IN_REVIEW  →  RESOLVED  →  CLOSED
   (New)      (Officer       (Fixed)     (Archived)
              reviewing)

Rules:
  ✅ Any forward transition is allowed
  ❌ CLOSED tickets cannot be reopened (throws IllegalStateException)
```

---

## 7. Complete DevOps Pipeline Flow

```
 PLAN          CODE          BUILD         TEST          RELEASE       DEPLOY        OPERATE
  │              │             │             │              │             │             │
  │  Agile       │  Git        │  Maven      │  JUnit       │  Docker     │  Ansible    │  Actuator
  │  Backlog     │  GitHub     │  pom.xml    │  Selenium    │  Hub        │  Playbook   │  Health
  │  Sprints     │  Branches   │  Compile    │  Headless    │  Registry   │  Provision  │  Endpoint
  │  Stories     │  PRs        │  Package    │  Quality     │  Tags       │  Rollback   │
  │              │             │  WAR        │  Gate        │             │             │
  └──────────────┴─────────────┴─────────────┴──────────────┴─────────────┴─────────────┘
                                    ↑                                              │
                                    └──────── Feedback Loop ───────────────────────┘
```

This is the **DevOps Lifecycle**: Plan → Code → Build → Test → Release → Deploy → Operate → Monitor → back to Plan. Our project implements every stage with real tools.

---

## 8. File-by-File Explanation

### Backend Java Files
| File | Layer | Purpose |
|---|---|---|
| `PtfpApplication.java` | Entry Point | Boots Spring, extends ServletInitializer for WAR support |
| `FeedbackController.java` | Controller | REST endpoints for CRUD on feedbacks |
| `DashboardController.java` | Controller | REST endpoint for dashboard analytics summary |
| `RouteController.java` | Controller | REST endpoint to list transit routes |
| `FeedbackService.java` | Service | Business logic: create, search, update, dashboard, validation |
| `FeedbackRepository.java` | Repository | JPA interface with custom search query |
| `RouteRepository.java` | Repository | JPA interface for route CRUD |
| `Feedback.java` | Model | JPA entity mapped to feedbacks table |
| `Route.java` | Model | JPA entity mapped to routes table |
| `FeedbackStatus.java` | Model | Enum: SUBMITTED, IN_REVIEW, RESOLVED, CLOSED |
| `FeedbackCategory.java` | Model | Enum: DELAY, CLEANLINESS, SAFETY, STAFF, OVERCROWDING, OTHER |
| `FeedbackRequest.java` | DTO | Validated input for creating feedback |
| `StatusUpdateRequest.java` | DTO | Validated input for status change |
| `DashboardSummaryDTO.java` | DTO | Output format for dashboard metrics |

### Frontend Files
| File | Purpose |
|---|---|
| `index.html` | Landing page with navigation and feature highlights |
| `feedback-form.html` | Form to submit feedback (route select, category, description) |
| `feedback-list.html` | Table of all feedbacks with filter, search, status update |
| `dashboard.html` | Analytics display: totals, status counts, category breakdown |
| `css/style.css` | Dark theme styling for all pages |
| `js/api.js` | Shared API helper (fetch wrapper for all endpoints) |
| `js/feedback-form.js` | Form logic: load routes, submit via POST, show success |
| `js/feedback-list.js` | Table logic: load data, filter, search, status change |
| `js/dashboard.js` | Dashboard logic: fetch summary, render metrics |

### DevOps Files
| File | Purpose |
|---|---|
| `pom.xml` | Maven build config with all dependencies |
| `application.yml` | Spring config: H2 (dev) + MySQL (prod) profiles, Actuator |
| `Jenkinsfile` | 8-stage CI/CD pipeline definition |
| `docker/Dockerfile` | Multi-stage build: Maven→JRE, non-root, healthcheck |
| `docker-compose.yml` | Single-command app startup with port + env config |
| `ansible/inventory.ini` | Target server definitions (staging + production) |
| `ansible/playbook.yml` | Server provisioning: packages, users, dirs, Docker, firewall, deploy |
| `ansible/rollback.yml` | Automated rollback to previous stable version |
| `.gitignore` | Files Git should ignore (target/, .idea/, *.class) |
| `.github/ISSUE_TEMPLATE/` | Standardized templates for bug reports and feature requests |

---

## 9. Seed Data

When the application starts for the first time, `FeedbackService.initSeedData()` automatically inserts sample data so the app isn't empty during demos:

**Routes (5 transit routes):**
| Route Code | Name | Mode |
|---|---|---|
| ROUTE-101 | Downtown Express Bus | BUS |
| ROUTE-204 | Uptown & University Bus | BUS |
| METRO-BLUE | Blue Line Metro (Airport - Central) | METRO |
| METRO-RED | Red Line Metro (North - South) | METRO |
| TRAIN-SUB-1 | Suburban Line 1 East | TRAIN |

**Sample Feedbacks (2 records):**
| Ticket | Route | Category | Status |
|---|---|---|---|
| PTFP-20260901-1001 | ROUTE-101 | DELAY | IN_REVIEW |
| PTFP-20260902-1002 | METRO-BLUE | CLEANLINESS | RESOLVED |

---

## 10. Key Design Decisions Summary

| Decision | Why |
|---|---|
| Spring Boot over plain Java Servlets | 10x less boilerplate, embedded server, auto-config |
| Maven over Gradle | Industry standard for Jenkins integration, simpler XML |
| H2 + MySQL dual profile | Zero-setup dev + production-ready persistence |
| Plain HTML+JS over React/Angular | No build step, ships in same WAR, no CORS |
| WAR packaging over JAR | Deployable to external Tomcat AND runnable standalone |
| Ansible over Puppet | Agentless, YAML, SSH-only, faster setup |
| Headless Chrome for Selenium | Works in CI/Docker without display |
| Multi-stage Dockerfile | Small final image (~200MB vs ~800MB) |
| Spring Boot Actuator | Free health endpoint for Docker + Ansible + Jenkins |
| Ticket ID with date+random | Human-readable, sortable, unique enough for MVP |

---

*This document covers every technology, tool, and design decision in the project. Use it for your viva preparation and report writing.* 📝
