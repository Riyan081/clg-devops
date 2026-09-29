# 🎯 Live Demonstration Flow — Step-by-Step Guide
## Automated Public Transport Feedback Portal

> **Use this guide to show your teacher/evaluator the complete working project.**
> Follow each step in order. Every command is copy-paste ready.

---

## 🔧 PART 1: Prerequisites Check (2 minutes)

### Step 1.1 — Verify Java is installed
Open **Terminal / PowerShell** and type:
```bash
java -version
```
✅ You should see: `java version "25.0.1"` (or any 17+ version)

### Step 1.2 — Verify Maven is installed
```bash
mvn -version
```
✅ You should see: `Apache Maven 3.9.x`

> **If Maven is not found**, run this first:
> ```powershell
> $env:PATH = "C:\tools\apache-maven-3.9.9\bin;$env:PATH"
> ```

### Step 1.3 — Verify Docker is installed (for Weeks 11-12)
```bash
docker --version
```

### Step 1.4 — Verify Git is installed
```bash
git --version
```

---

## 📁 PART 2: Show Project Structure (3 minutes)

### Step 2.1 — Open the project folder
Navigate to the project directory:
```bash
cd C:\Users\riyan\OneDrive\Desktop\Devops
```

### Step 2.2 — Show the folder structure
```bash
tree /F /A
```
**Tell the evaluator:** *"This is the complete project structure with:*
- *`src/main/java/com/ptfp/` — Java backend (controllers, services, models, DTOs)*
- *`src/main/resources/static/` — Plain HTML/CSS/JS frontend*
- *`src/test/` — Unit tests + Selenium tests*
- *`docker/` — Dockerfile for containerization*
- *`ansible/` — Ansible playbooks for provisioning*
- *`docs/week01 to week15/` — All weekly deliverables*
- *`Jenkinsfile` — Pipeline-as-Code*
- *`pom.xml` — Maven build file"*

### Step 2.3 — Show key files to the evaluator
Open and briefly show these files:
1. **pom.xml** — Show dependencies (Spring Boot, JPA, Selenium, H2, MySQL, Actuator)
2. **application.yml** — Show H2 dev profile + MySQL prod profile
3. **Jenkinsfile** — Show all 8 pipeline stages
4. **docker/Dockerfile** — Show multi-stage build
5. **ansible/playbook.yml** — Show 7 provisioning tasks
6. **ansible/rollback.yml** — Show rollback automation

---

## 🔨 PART 3: Build the Project with Maven (2 minutes)

### Step 3.1 — Clean compile
```bash
mvn -B clean compile
```
✅ **Expected:** `BUILD SUCCESS`

**Tell the evaluator:** *"Maven compiles 14 Java source files using JDK 17."*

### Step 3.2 — Package the WAR artifact
```bash
mvn -B package -DskipTests
```
✅ **Expected:** `BUILD SUCCESS` and a file `target/ptfp.war` is created.

### Step 3.3 — Show the generated artifact
```bash
dir target\ptfp.war
```
**Tell the evaluator:** *"This is the deployable WAR artifact. Same file goes into Docker and Tomcat."*

---

## 🚀 PART 4: Run the Application Locally (3 minutes)

### Step 4.1 — Start Spring Boot
```bash
mvn spring-boot:run
```
✅ **Wait until you see:**
```
Started PtfpApplication in X.XX seconds
Tomcat started on port 8080 (http) with context path ''
```

### Step 4.2 — Open browser and show all 4 pages

**Page 1: Home Page**
👉 Go to: **http://localhost:8080**

**Tell the evaluator:** *"This is the landing page. It has navigation to all features: Submit Feedback, Feedback List, and Analytics Dashboard."*

---

**Page 2: Submit Feedback Form**
👉 Go to: **http://localhost:8080/feedback-form.html**

**LIVE DEMO — Fill the form:**
1. **Transit Route** → Select `ROUTE-101 - Downtown Express Bus (BUS)`
2. **Grievance Category** → Select `Delay / Schedule Deviation`
3. **Your Name** → Type: `Demo User`
4. **Your Email** → Type: `demo@test.com`
5. **Issue Description** → Type: `Bus was delayed by 20 minutes during morning rush hour at Central Station`
6. Click **"Submit Feedback"**

✅ **Expected:** Green success message appears with a ticket ID like `PTFP-20260929-XXXX`

**Tell the evaluator:** *"The frontend sends a fetch() POST request to /api/feedback. The backend generates a unique ticket ID, saves to database, and returns the record."*

---

**Page 3: Feedback List & Status Workflow**
👉 Go to: **http://localhost:8080/feedback-list.html**

**Tell the evaluator:** *"This shows all feedback records with ticket ID, route, category, status, and date."*

**LIVE DEMO — Search & Filter:**
1. Click the **Status** dropdown → Select `SUBMITTED` → Records filter automatically
2. Clear filter → Select `All Statuses`
3. Type `ROUTE-101` in the **Search box** → Click **Search** → Shows only Route-101 records

**LIVE DEMO — Status Workflow Update (Role-based):**
1. Find the feedback you just submitted (status = `SUBMITTED`)
2. In the **Action (Workflow)** column → Change dropdown from `SUBMITTED` to `IN_REVIEW`
3. ✅ Status badge changes to `IN_REVIEW`
4. Now change it to `RESOLVED`
5. ✅ Status badge changes to `RESOLVED`

**Tell the evaluator:** *"This demonstrates the role-based workflow: SUBMITTED → IN_REVIEW → RESOLVED → CLOSED. An officer triages, resolves, and closes tickets."*

---

**Page 4: Analytics Dashboard**
👉 Go to: **http://localhost:8080/dashboard.html**

**Tell the evaluator:** *"The dashboard shows:*
- *Total Grievances count*
- *Breakdown by status: Submitted, In Review, Resolved, Closed*
- *Resolution SLA percentage*
- *Category distribution: Delay, Cleanliness, Safety, Staff, Overcrowding, Other"*

**Tell the evaluator:** *"The dashboard fetches data from GET /api/dashboard/summary which runs aggregate JPA queries on the database."*

---

**Page 5: Health Check (DevOps Endpoint)**
👉 Go to: **http://localhost:8080/actuator/health**

✅ **Expected JSON:**
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP", "details": { "database": "H2" } },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

**Tell the evaluator:** *"Spring Boot Actuator provides this health endpoint. Ansible playbook uses this to verify deployment success."*

---

**Page 6: H2 Database Console (Optional)**
👉 Go to: **http://localhost:8080/h2-console**

- JDBC URL: `jdbc:h2:mem:ptfpdb`
- Username: `sa`
- Password: *(leave blank)*
- Click **Connect**

Run this SQL:
```sql
SELECT * FROM FEEDBACKS;
```
✅ Shows all feedback records in the database

**Tell the evaluator:** *"H2 is used for development and CI testing. Production uses MySQL via profile switching."*

---

## 🧪 PART 5: Run Unit Tests (2 minutes)

### Step 5.1 — Stop the running app first
Press `Ctrl + C` in the terminal where Spring Boot is running.

### Step 5.2 — Run unit tests with Maven
```bash
mvn test -Dtest="com.ptfp.unit.*"
```

✅ **Expected:** All tests pass:
```
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

**Tell the evaluator:** *"We have 4 unit tests using JUnit 5 and Mockito:*
1. *testCreateFeedback_GeneratesTicketIdAndSaves — Verifies ticket ID generation*
2. *testUpdateStatus_ValidTransition — Tests SUBMITTED → IN_REVIEW transition*
3. *testUpdateStatus_DisallowTransitionFromClosed — Tests that CLOSED tickets can't be reopened*
4. *testDashboardSummary_CalculatesMetrics — Verifies dashboard calculation logic"*

---

## 🌐 PART 6: Show REST API via Terminal (2 minutes)

> **Do this while the app is running.** Open a **second terminal** and run:

### Step 6.1 — Health Check API
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" | ConvertTo-Json
```

### Step 6.2 — Get all feedbacks
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/feedback" | ConvertTo-Json -Depth 3
```

### Step 6.3 — Submit a new feedback via API
```powershell
$body = @{
    routeCode = "METRO-BLUE"
    category = "SAFETY"
    passengerName = "API Test User"
    description = "Safety concern at Metro platform during peak hours"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/feedback" -Method Post -Body $body -ContentType "application/json" | ConvertTo-Json
```
✅ **Expected:** Returns the created feedback with ticket ID

### Step 6.4 — Get dashboard summary
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/dashboard/summary" | ConvertTo-Json -Depth 3
```

### Step 6.5 — Update status (Officer workflow)
```powershell
$statusBody = @{
    status = "IN_REVIEW"
    resolutionRemarks = "Under investigation by transport authority"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/feedback/1/status" -Method Put -Body $statusBody -ContentType "application/json" | ConvertTo-Json
```

### Step 6.6 — Search feedbacks by status
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/feedback?status=IN_REVIEW" | ConvertTo-Json -Depth 3
```

**Tell the evaluator:** *"The REST API supports full CRUD: POST creates, GET retrieves/searches, PUT updates status. The frontend uses JavaScript fetch() to call these same endpoints."*

---

## 📂 PART 7: Show Git & GitHub (3 minutes)

### Step 7.1 — Show Git history
```bash
git log --oneline -10
```

### Step 7.2 — Show branches
```bash
git branch -a
```

### Step 7.3 — Show tags
```bash
git tag -l
```

### Step 7.4 — Show .gitignore
```bash
cat .gitignore
```

### Step 7.5 — Show GitHub issue templates
```bash
cat .github\ISSUE_TEMPLATE\bug_report.md
cat .github\ISSUE_TEMPLATE\feature_request.md
```

### Step 7.6 — Show branching policy
```bash
cat docs\week04\branching-policy.md
```

**Tell the evaluator:** *"We follow the Git branching strategy:*
- *`main` — production-ready, protected with PR reviews*
- *`develop` — integration branch*
- *`feature/*` — individual features, merged via PR*
- *`bugfix/*`, `release/*`, `hotfix/*` — standard Git Flow"*

---

## 🔁 PART 8: Show Jenkinsfile Pipeline (2 minutes)

### Step 8.1 — Open and show the Jenkinsfile
```bash
cat Jenkinsfile
```

**Walk through the 8 stages:**

| Stage | What It Does |
|---|---|
| 1. Checkout | Pulls code from Git |
| 2. Compile & Static Analysis | `mvn clean compile` |
| 3. Unit Tests | Runs unit + Mockito tests, publishes JUnit reports |
| 4. Package Artifact | `mvn package`, archives WAR |
| 5. Selenium UI Quality Gate | Runs 5 headless Selenium tests, blocks deploy on failure |
| 6. Docker Build & Tag | Builds image with `BUILD_NUMBER` tag |
| 7. Docker Push Registry | Pushes to Docker Hub (with credentials template) |
| 8. Deploy Container | Stops old container, starts new one on port 9090 |
| 9. Health Check | Curls `/actuator/health` to verify deployment |

**Tell the evaluator:** *"The pipeline has two parameters: `DEPLOY_ENV` (staging/production) and `RUN_SELENIUM_TESTS` (boolean). Failed tests block deployment — this is the quality gate."*

---

## 🐳 PART 9: Docker Demonstration (3 minutes)

### Step 9.1 — Show the Dockerfile
```bash
cat docker\Dockerfile
```
**Tell the evaluator:** *"Multi-stage build: Stage 1 uses Maven to compile and package. Stage 2 uses a minimal JRE image. Non-root user for security. Healthcheck built into the image."*

### Step 9.2 — Build the Docker image
```bash
docker build -t ptfp:1.0 -f docker/Dockerfile .
```

### Step 9.3 — Show the built image
```bash
docker images | findstr ptfp
```

### Step 9.4 — Run the container
```bash
docker run -d --name ptfp-demo -p 9090:8080 ptfp:1.0
```

### Step 9.5 — Verify container is running
```bash
docker ps
```

### Step 9.6 — Check container logs
```bash
docker logs ptfp-demo
```
✅ **Wait until you see:** `Started PtfpApplication`

### Step 9.7 — Access the containerized app
👉 Go to: **http://localhost:9090**
✅ Same app, now running inside Docker!

### Step 9.8 — Show full container lifecycle
```bash
docker stop ptfp-demo
docker start ptfp-demo
docker restart ptfp-demo
docker exec ptfp-demo ls /app
docker rm -f ptfp-demo
```

**Tell the evaluator:** *"This demonstrates the complete Docker container lifecycle: build, run, logs, stop, start, restart, exec, remove."*

---

### Step 9.9 — Docker Compose (Alternative)
```bash
docker-compose up --build -d
```
Then visit: **http://localhost:9090**

To stop:
```bash
docker-compose down
```

---

## 📋 PART 10: Ansible Configuration Management (2 minutes)

### Step 10.1 — Show the inventory
```bash
cat ansible\inventory.ini
```
**Tell the evaluator:** *"Defines staging (localhost) and production hosts."*

### Step 10.2 — Show the provisioning playbook
```bash
cat ansible\playbook.yml
```

**Walk through the 7 tasks:**

| Task | Purpose |
|---|---|
| 1. Install prerequisites | curl, git, JDK 17, ufw |
| 2. Create deploy user | Non-root user with sudo |
| 3. Create app directories | /opt/ptfp and /opt/ptfp/logs |
| 4. Start Docker service | Ensure Docker daemon is running |
| 5. Open port 9090 | Firewall rule for app port |
| 6. Deploy container | Pull image and run with restart policy |
| 7. Health check | Validate /actuator/health returns 200 |

**Tell the evaluator:** *"All tasks are idempotent — running the playbook a second time produces changed=0 because the desired state is already achieved."*

### Step 10.3 — Show the rollback playbook
```bash
cat ansible\rollback.yml
```
**Tell the evaluator:** *"Rollback stops the broken container, redeploys the previous stable version, and verifies health. Run with:*
```bash
ansible-playbook -i ansible/inventory.ini ansible/rollback.yml -e target_version=previous_stable
```
*"*

---

## 🧪 PART 11: Show Selenium Tests (2 minutes)

### Step 11.1 — Show the test plan
```bash
cat docs\week09\test-plan.md
```

### Step 11.2 — Show a Selenium test file
Open `src/test/java/com/ptfp/selenium/SubmitFeedbackSeleniumTest.java`

**Tell the evaluator:** *"5 Selenium test classes covering critical user journeys:*
1. *SubmitFeedbackSeleniumTest — Commuter submits feedback, verifies success message*
2. *SearchFeedbackSeleniumTest — Filtered search returns correct results*
3. *StatusWorkflowSeleniumTest — Officer changes status SUBMITTED → IN_REVIEW*
4. *ValidationSeleniumTest — Missing required fields show error message*
5. *DashboardSeleniumTest — Dashboard loads and displays metrics*

*All tests use headless Chrome with WebDriverWait (no Thread.sleep). On failure, screenshots are auto-captured to target/selenium-screenshots/."*

---

## 📚 PART 12: Show Documentation (2 minutes)

### Step 12.1 — Show all 15 weeks of docs exist
```bash
dir docs
```
✅ Shows: `week01` through `week15` — all 15 folders present

### Step 12.2 — Show key documents from each phase

**Planning (Weeks 1-3):**
```bash
cat docs\week01\problem-statement.md
cat docs\week02\user-stories-acceptance-criteria.md
cat docs\week03\architecture-diagram.md
```

**Development (Weeks 4-6):**
```bash
cat docs\week05\feature-1-submit-feedback.md
cat docs\week06\merge-conflict-resolution.md
```

**CI/CD (Weeks 7-10):**
```bash
cat docs\week07\jenkins-installation-ci.md
cat docs\week09\test-plan.md
```

**Docker & Ansible (Weeks 11-14):**
```bash
cat docs\week11\container-lifecycle-log.md
cat docs\week13\server-prerequisites.md
cat docs\week14\idempotency-and-rollback-validation.md
```

**Final Report (Week 15):**
```bash
cat docs\week15\final-devops-report.md
cat docs\week15\troubleshooting-guide.md
```

---

## 🗣️ PART 13: Quick Viva Talking Points

If the evaluator asks **WHY** questions, here are the answers:

| Question | Answer |
|---|---|
| Why Spring Boot? | Fast scaffolding, embedded Tomcat, single deployable artifact (WAR/JAR), easy REST APIs |
| Why Maven over Gradle? | Most Jenkins/Selenium tutorials use Maven; simpler XML-based dependency management |
| Why H2 + MySQL? | H2 for zero-setup dev/CI testing, MySQL for production via profile switching |
| Why Ansible over Puppet? | Agentless (only needs SSH), YAML-based (easier to read), faster setup |
| Why plain HTML+JS? | No framework dependency, ships inside the same WAR, no CORS issues, no separate build step |
| Why headless Chrome? | Required for Jenkins/Docker CI environments with no display |
| Why multi-stage Dockerfile? | Smaller final image — build tools stay in build stage, only JRE goes to production |
| Why WAR packaging? | Deployable to external Tomcat AND runnable standalone with `java -jar` |
| What is idempotency? | Running Ansible playbook twice gives same result — second run shows changed=0 |
| What is the quality gate? | Selenium tests must pass before Docker build/deploy stage executes |

---

## ⚡ QUICK DEMO CHECKLIST (if short on time)

If you only have **5 minutes**, do ONLY these:

- [ ] `mvn spring-boot:run` → Show app starts on localhost:8080
- [ ] Open **http://localhost:8080** → Show Home page
- [ ] Open **http://localhost:8080/feedback-form.html** → Submit a feedback live
- [ ] Open **http://localhost:8080/feedback-list.html** → Show records + change status
- [ ] Open **http://localhost:8080/dashboard.html** → Show analytics
- [ ] Show **Jenkinsfile** → Explain 8 stages
- [ ] Show **docker/Dockerfile** → Explain multi-stage build
- [ ] Show **ansible/playbook.yml** → Explain 7 tasks
- [ ] `dir docs` → Show all 15 weeks documented

---

*Good luck with your demo! 🎓*
