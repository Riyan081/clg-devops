# Automated Public Transport Feedback Portal
## Complete 15-Week Implementation Plan (Problem → Agile → Git → Jenkins → Selenium → Docker → Ansible → Release)

---

## 0. How to use this plan

Each week below has: **Objective**, **Step-by-step tasks**, **Exact commands / config snippets** (copy-paste ready), and **Deliverable checklist** (matches your grading sheet). Follow it in order — each week's output is the input to the next week, so don't skip ahead. Keep every deliverable (screenshots, logs, files) in a `docs/weekXX/` folder inside the repo so Week 15's final report is just an assembly job.

---

## 1. Locked Technology Stack (decide this once, in Week 1, then never change it)

| Layer | Choice | Why |
|---|---|---|
| Language | Java 17 (LTS) | Works cleanly with Maven, Tomcat, Jenkins, Docker base images |
| Framework | Spring Boot 3.x + Spring Web (REST controllers, `@RestController`, returns JSON) | Fast CRUD scaffolding, single embedded Tomcat for dev, WAR-able for external Tomcat, easy to unit-test and Selenium-test |
| Build tool | **Maven** | Most Jenkins/Selenium tutorials assume Maven; simplest `pom.xml` dependency management |
| Database | MySQL 8 (or H2 in-memory for quick demos) | Simple JDBC/JPA, easy to containerize |
| Persistence | Spring Data JPA + Hibernate | Less boilerplate for CRUD |
| Frontend | Plain HTML5 + CSS3 + Vanilla JavaScript (`fetch()` calling the REST API) | No Thymeleaf, no build step, no framework to learn — HTML files just live under `src/main/resources/static/` and Spring Boot serves them for free. Same origin as the API, so **no CORS config needed**. Still ONE build artifact (jar/war) since static files ship inside it. |
| Version control | Git + GitHub | Required by spec |
| CI/CD | Jenkins (Freestyle in Week 7, Pipeline-as-Code from Week 8) | Required by spec |
| Testing | Selenium WebDriver + JUnit 5 + WebDriverManager | Required by spec |
| Containerization | Docker + Docker Hub | Required by spec |
| Config Mgmt / Provisioning | **Ansible** (choose this over Puppet — agentless, only needs SSH, far less setup pain for a 2-week window) | Required by spec (either is allowed) |
| Deployment target | Tomcat 10 (WAR) *or* Spring Boot embedded jar behind Nginx reverse proxy — pick Tomcat/WAR if your course explicitly grades "Tomcat deployment" | Required by spec |

> Write this table into `docs/week03/tech-stack-decision.md` verbatim with a one-line justification each — this alone satisfies half of Week 3's SRS deliverable.

---

## 2. Target Repository Structure (create in Week 4, keep stable after)

```
public-transport-feedback-portal/
├── src/main/java/com/ptfp/
│   ├── controller/        (REST controllers, @RestController, return JSON)
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── PtfpApplication.java
├── src/main/resources/
│   ├── static/            (plain frontend: HTML/CSS/JS, served as-is by Spring Boot)
│   │   ├── index.html
│   │   ├── feedback-form.html
│   │   ├── feedback-list.html
│   │   ├── dashboard.html
│   │   ├── css/style.css
│   │   └── js/
│   │       ├── api.js         (shared fetch() helper functions)
│   │       ├── feedback-form.js
│   │       ├── feedback-list.js
│   │       └── dashboard.js
│   └── application.yml
├── src/test/java/com/ptfp/
│   ├── unit/
│   └── selenium/
├── docker/
│   └── Dockerfile
├── ansible/
│   ├── inventory.ini
│   ├── playbook.yml
│   └── roles/
├── docs/
│   ├── week01/ ... week15/
├── Jenkinsfile
├── .gitignore
├── README.md
└── pom.xml
```

---

## WEEK 1 — Problem Definition & Scope

**Objective:** Freeze what "MVP" means so scope never creeps for 15 weeks.

**Tasks**
1. Write the problem statement: commuters have no structured channel to report bus/train delays, cleanliness, safety, or driver conduct; transport authorities lack a dashboard to see complaint trends.
2. List target users: **Commuter (submits feedback)**, **Transport Officer (triages/resolves)**, **Admin (manages routes/users, views dashboard)**.
3. List stakeholders: Commuters, Transport Authority staff, IT/DevOps team (you), College evaluator.
4. List pain points: no single channel, no status tracking, no analytics, manual paper complaints.
5. List constraints: 15-week timeline, single-developer/small-team capacity, must run on Tomcat, must be fully automatable (no manual deploy steps allowed by Week 12).
6. Define measurable success criteria (write these down — you'll reuse them in Week 15's report):
   - MVP supports Create/View/Update/Search on feedback records.
   - Role-based workflow: `SUBMITTED → IN_REVIEW → RESOLVED → CLOSED`.
   - End-to-end pipeline: commit → build → test → containerize → deploy in under 10 minutes.
   - ≥90% of Selenium critical-path tests pass before any deploy.
7. Freeze MVP scope (explicitly OUT of scope): no mobile app, no payment/ticketing, no real-time GPS tracking, no multi-language UI, no notifications/SMS.

**Deliverable checklist**
- [ ] `docs/week01/problem-statement.md`
- [ ] `docs/week01/stakeholders.md`
- [ ] `docs/week01/objectives-and-success-criteria.md`
- [ ] `docs/week01/mvp-scope.md` (in-scope vs out-of-scope table, "approved" with a date)

---

## WEEK 2 — Agile Planning & DevOps Workflow

**Objective:** Turn Week 1's scope into a backlog and a visual DevOps lifecycle.

**Tasks**
1. Write user stories in the format: *"As a [role], I want [action], so that [benefit]."* Minimum set:
   - As a Commuter, I want to submit feedback with route/date/category, so my complaint is logged.
   - As a Commuter, I want to search my past feedback by status, so I can track resolution.
   - As an Officer, I want to update feedback status, so commuters know progress.
   - As an Admin, I want a summary dashboard (counts by status/category), so I can spot trends.
   - As an Admin, I want to manage routes/users, so the system stays current.
2. Write acceptance criteria (Given/When/Then) for each story.
3. Build the product backlog (spreadsheet or GitHub Projects board): Epic → Story → Task, with story points (Fibonacci: 1,2,3,5,8).
4. Set up a Kanban board with columns: `Backlog → To Do → In Progress → Code Review → Testing → Done`. GitHub Projects (free, tied to your repo) is the easiest choice.
5. Map the 15 weeks onto 5 sprints of 3 weeks each (or weekly sprints — your call, but write it down):
   - Sprint 1 (Wk 1-3): Planning + Architecture
   - Sprint 2 (Wk 4-6): Core feature dev + Git workflow
   - Sprint 3 (Wk 7-9): CI + pipeline + test design
   - Sprint 4 (Wk 10-12): Continuous testing + Docker + CD
   - Sprint 5 (Wk 13-15): Config mgmt + provisioning + release
6. Write Definition of Done: code merged to `develop`, unit tests pass, Selenium smoke test passes, reviewed via PR, deployed to staging, documented.
7. Draw the DevOps lifecycle diagram: **Plan → Code → Build → Test → Release → Deploy → Operate → Monitor** (feedback loop back to Plan), annotated with your actual tools at each stage (Git/GitHub → Maven → Jenkins → Selenium → Jenkins → Docker/Tomcat → Ansible → (future) monitoring).

**Deliverable checklist**
- [ ] `docs/week02/backlog.xlsx` or GitHub Projects board link
- [ ] `docs/week02/user-stories-acceptance-criteria.md`
- [ ] `docs/week02/sprint-plan.md`
- [ ] `docs/week02/definition-of-done.md`
- [ ] `docs/week02/devops-lifecycle-diagram.png` (draw.io / Lucidchart / Excalidraw)

---

## WEEK 3 — Requirements, Architecture & Tech Setup

**Objective:** Produce the SRS + architecture + a working local dev environment.

**Tasks**
1. Write a short SRS covering functional requirements (CRUD + search + role workflow + dashboard) and non-functional requirements (usability, ≤2s page load locally, basic auth security).
2. Draw a use-case diagram: actors (Commuter, Officer, Admin) vs use cases (Submit Feedback, View Feedback, Update Status, Search, View Dashboard, Manage Users).
3. Draw a layered architecture diagram: Browser (static HTML/CSS/JS, `fetch()` calls) → Tomcat → Spring REST Controllers (`@RestController`, JSON) → Service layer → Spring Data JPA → MySQL. Add Jenkins/Docker/Ansible as a side "DevOps toolchain" box. Note in the diagram that the static frontend files are served by the *same* Spring Boot app (from `src/main/resources/static/`) — there is no separate frontend server, so it's still a single deployable artifact.
4. Define the data model:
   - `User(id, name, email, password_hash, role[COMMUTER|OFFICER|ADMIN])`
   - `Route(id, name, code)`
   - `Feedback(id, user_id, route_id, category, description, status, created_at, updated_at)`
5. Define the REST/API list even if server-rendered (useful for later automated tests):
   - `POST /feedback`, `GET /feedback/{id}`, `GET /feedback?status=&category=`, `PUT /feedback/{id}/status`, `GET /dashboard/summary`
6. Install local dev tools: JDK 17, Maven 3.9+, MySQL 8, IntelliJ/VSCode, Git, Docker Desktop, Postman.
7. Scaffold the Spring Boot project locally (start.spring.io: **Web**, Spring Data JPA, MySQL Driver, Validation, DevTools — no Thymeleaf needed). Drop a plain `index.html` into `src/main/resources/static/` and confirm `mvn spring-boot:run` serves it at `localhost:8080` untouched (Spring Boot auto-serves anything in `static/`). Add one throwaway `@RestController` endpoint (e.g. `GET /api/ping` → `"pong"`) and confirm `index.html` can call it with `fetch('/api/ping')` and show the result on the page — this proves the whole HTML↔JSON↔Java loop works before you build real features.

**Deliverable checklist**
- [ ] `docs/week03/srs-summary.md`
- [ ] `docs/week03/use-case-diagram.png`
- [ ] `docs/week03/architecture-diagram.png`
- [ ] `docs/week03/data-model-and-api-list.md`
- [ ] Screenshot of app running locally on `localhost:8080`

---

## WEEK 4 — Git & GitHub Repository Initialization

**Objective:** Repo exists, is documented, and has team/branch rules before any real feature work starts.

**Tasks & commands**
```bash
# 1. Create repo on GitHub UI first (public-transport-feedback-portal), then locally:
git init
git remote add origin https://github.com/<your-username>/public-transport-feedback-portal.git

# 2. Add the folder structure from Section 2 above, plus:
echo "target/\n*.class\n.idea/\n*.iml\n.env\napplication-local.yml" > .gitignore

# 3. Write README.md with: project description, tech stack table, how to run locally,
#    branching strategy, link to backlog board.

# 4. Commit the skeleton
git add .
git commit -m "chore: initial project skeleton with Spring Boot, Maven, folder structure"
git branch -M main
git push -u origin main

# 5. Create the long-lived develop branch
git checkout -b develop
git push -u origin develop
```
5. Set branch protection on `main` (require PR + 1 review) in GitHub repo Settings → Branches.
6. Define and document a branch naming policy in README:
   `feature/<ticket-id>-<short-desc>`, `bugfix/<ticket-id>-<short-desc>`, `release/<version>`, `hotfix/<issue>`.
7. Create GitHub Issue templates (`.github/ISSUE_TEMPLATE/bug_report.md`, `feature_request.md`).
8. Open 5-6 GitHub Issues from Week 2's backlog (one per user story), label them (`feature`, `priority:high`, etc.).

**Deliverable checklist**
- [ ] Repository URL (public or shared with evaluator)
- [ ] README.md rendered on GitHub
- [ ] `.gitignore`, folder skeleton committed
- [ ] Issue templates + at least 5 issues created
- [ ] Branch naming policy documented, `main` protected

---

## WEEK 5 — Feature Development with Branching

**Objective:** Prove the full feature-branch → PR → review → merge loop with ONE real feature.

**Tasks & commands**
```bash
git checkout develop
git pull origin develop
git checkout -b feature/12-submit-feedback

# ... implement: Feedback entity, FeedbackRepository, FeedbackController (@RestController,
#     POST /api/feedback, returns JSON), basic server-side validation ...
# ... plus the frontend: src/main/resources/static/feedback-form.html (plain form)
#     + static/js/feedback-form.js (fetch('/api/feedback', {method:'POST', body: JSON.stringify(...)}))

git add .
git commit -m "feat(feedback): add submit-feedback REST endpoint, entity, repository, and static form page"
git log --oneline -5          # show meaningful commit history
git push -u origin feature/12-submit-feedback
```
Then on GitHub: open a **Pull Request** `feature/12-submit-feedback → develop`, add a description linking the Issue (`Closes #12`), request a review (self-review with documented comments is fine solo — add at least 2 inline review comments and resolve them), then **Merge** (use "Squash and merge" or "Create a merge commit" — document your choice).

**Deliverable checklist**
- [ ] Feature 1 (Submit Feedback) working locally, screenshot attached
- [ ] Feature branch pushed with ≥3 meaningful commits
- [ ] PR opened, review comments visible, PR merged into `develop`
- [ ] `docs/week05/merge-evidence.png`

---

## WEEK 6 — MVP Completion & Git Collaboration (branching, conflicts, tags)

**Objective:** Finish all core MVP functions and demonstrate a real merge conflict resolution.

**Tasks**
1. Implement remaining MVP functions on new feature branches, one PR each:
   - `feature/13-view-and-search-feedback` (GET list + search by status/category/route)
   - `feature/14-update-status-workflow` (Officer/Admin can transition `SUBMITTED→IN_REVIEW→RESOLVED→CLOSED`, enforce role checks)
   - `feature/15-summary-dashboard` (`GET /api/dashboard/summary` returns aggregate JPA query as JSON; `static/dashboard.html` + `dashboard.js` fetch it and render a simple table or a lightweight chart, e.g. Chart.js loaded from a CDN `<script>` tag)
2. **Deliberately create a merge conflict** to demonstrate resolution: from two branches both edit the same line in `FeedbackController.java` (e.g., both add a different import or method at the same line), push both, try merging the second — Git will conflict.
```bash
git checkout develop
git merge feature/14-update-status-workflow   # merges clean
git checkout feature/15-summary-dashboard
git rebase develop                             # <-- conflict appears here intentionally
# resolve manually in the editor, keep both changes correctly
git add FeedbackController.java
git rebase --continue
git push --force-with-lease
```
Screenshot the conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`) BEFORE resolving, and the clean file AFTER — this is graded evidence.

3. Tag a release-ready baseline:
```bash
git checkout main
git merge develop
git tag -a v0.1.0-mvp -m "MVP: CRUD + role workflow + dashboard complete"
git push origin main --tags
```
4. Update the Kanban board — move all MVP stories to Done.

**Deliverable checklist**
- [ ] Functional MVP (all 3 remaining features working, screenshots)
- [ ] Merge conflict screenshot (before) + resolution screenshot (after)
- [ ] `git tag` showing `v0.1.0-mvp` pushed to GitHub (visible under Releases/Tags)
- [ ] Backlog board updated to reflect Done items

---

## WEEK 7 — Jenkins Installation & CI Job

**Objective:** Jenkins builds your Maven project automatically on every commit.

**Tasks**
1. Install Jenkins (recommended: Docker, so it doesn't fight your OS Java version):
```bash
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```
2. Open `http://localhost:8080`, unlock, install suggested plugins + `Git plugin`, `Maven Integration plugin`, `Pipeline plugin`.
3. Configure a JDK 17 + Maven 3.9 tool under **Manage Jenkins → Tools**.
4. Add GitHub credentials (Personal Access Token) under **Manage Jenkins → Credentials**.
5. Create a **Freestyle project** `ptfp-ci-build`:
   - Source Code Management → Git → your repo URL, branch `develop`
   - Build Triggers → "GitHub hook trigger for GITScm polling" (webhook) OR "Poll SCM" with schedule `H/5 * * * *` if no public webhook is possible from localhost
   - Build step → Invoke top-level Maven targets → `clean package`
   - Post-build Actions → "Archive the artifacts" → `target/*.jar` (or `*.war`)
6. Trigger a build by pushing a trivial commit; confirm it auto-triggers and goes green.

**Deliverable checklist**
- [ ] Jenkins running (screenshot of dashboard)
- [ ] `ptfp-ci-build` Freestyle job configured
- [ ] Successful build console log (screenshot)
- [ ] Trigger evidence (commit → webhook/poll → build fired)
- [ ] Archived artifact visible in Jenkins job page

---

## WEEK 8 — Pipeline as Code & Server Deployment

**Objective:** Replace the Freestyle job with a versioned `Jenkinsfile`, deploy to Tomcat.

**Jenkinsfile (place at repo root)**
```groovy
pipeline {
    agent any
    parameters {
        string(name: 'DEPLOY_ENV', defaultValue: 'staging', description: 'Target environment')
    }
    tools { maven 'Maven-3.9'; jdk 'JDK-17' }
    stages {
        stage('Checkout') {
            steps { git branch: 'develop', url: 'https://github.com/<you>/public-transport-feedback-portal.git' }
        }
        stage('Build') {
            steps { sh 'mvn -B clean compile' }
        }
        stage('Package') {
            steps { sh 'mvn -B package -DskipTests' }
        }
        stage('Deploy') {
            steps {
                echo "Deploying to ${params.DEPLOY_ENV}"
                sh 'cp target/*.war /opt/tomcat/webapps/ptfp.war'
                // or, for embedded jar: sh 'nohup java -jar target/*.jar --spring.profiles.active=${DEPLOY_ENV} &'
            }
        }
    }
    post {
        success { echo 'Pipeline succeeded' }
        failure { echo 'Pipeline failed' }
    }
}
```
1. Create a Jenkins **Pipeline** job → "Pipeline script from SCM" → point at the Jenkinsfile in your repo.
2. Install/configure Tomcat 10 (or use `tomcat:10-jdk17` Docker image) and confirm Jenkins' agent user can write to `webapps/`.
3. Run the pipeline, confirm all 4 stages go green, visit the deployed app URL.
4. Parameterize at least one setting (shown above via `DEPLOY_ENV`) and demonstrate running it twice with different values.

**Deliverable checklist**
- [ ] `Jenkinsfile` committed to repo
- [ ] Successful pipeline run (all stages green, screenshot)
- [ ] Deployed application URL/screenshot (e.g. `http://localhost:8081/ptfp`)
- [ ] Evidence of the parameter being changed between two runs

---

## WEEK 9 — Selenium Test Design & Local Execution

**Objective:** Automated browser tests for the 3-5 most critical journeys, runnable via `mvn test`.

**Tasks**
1. Pick critical journeys:
   1. Commuter submits feedback successfully.
   2. Commuter search returns correct filtered results.
   3. Officer updates status `SUBMITTED → IN_REVIEW`.
   4. Invalid feedback submission (missing required field) shows validation error.
   5. Admin dashboard shows correct aggregate counts.
2. Add dependencies to `pom.xml`:
```xml
<dependency><groupId>org.seleniumhq.selenium</groupId><artifactId>selenium-java</artifactId><version>4.23.0</version><scope>test</scope></dependency>
<dependency><groupId>io.github.bonigarcia</groupId><artifactId>webdrivermanager</artifactId><version>5.9.2</version><scope>test</scope></dependency>
<dependency><groupId>org.junit.jupiter</groupId><artifactId>junit-jupiter</artifactId><version>5.10.2</version><scope>test</scope></dependency>
```
3. Example test (`src/test/java/com/ptfp/selenium/SubmitFeedbackTest.java`):
```java
class SubmitFeedbackTest {
    WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    void commuterCanSubmitFeedback() {
        driver.get("http://localhost:8080/feedback-form.html");   // static HTML file, not a Thymeleaf route
        driver.findElement(By.id("route")).sendKeys("Route 42");
        driver.findElement(By.id("category")).sendKeys("Delay");
        driver.findElement(By.id("description")).sendKeys("Bus was 20 minutes late");
        driver.findElement(By.id("submit-btn")).click();
        // IMPORTANT: with plain HTML+JS, the submit button triggers an async fetch() call —
        // the success message only appears AFTER the JS callback runs, so you MUST wait for it
        // (never assert immediately after click()).
        WebElement confirmation = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("success-msg")));
        assertTrue(confirmation.getText().contains("submitted"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            // On failure, capture screenshot
            driver.quit();
        }
    }
}
```
4. Add a JUnit `TestWatcher`/extension that captures a screenshot (`((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)`) into `target/selenium-screenshots/` on failure.
5. **Because the frontend is plain JS calling `fetch()` asynchronously (not server-rendered), every test that reads data (list page, dashboard) needs an explicit `WebDriverWait` on the element that JS inserts after the fetch resolves — never rely on `Thread.sleep()`, and never assert right after `driver.get()` or `.click()` for anything JS renders.**
6. Run locally: `mvn test -Dtest=*Selenium* ` or a dedicated `selenium` Maven profile so normal unit tests aren't slowed down.

**Deliverable checklist**
- [ ] `docs/week09/test-plan.md` (5 journeys, steps, expected results)
- [ ] 5 Selenium test classes under `src/test/java/com/ptfp/selenium/`
- [ ] Local test execution report (`target/surefire-reports/`, screenshot of console)
- [ ] Failure-screenshot mechanism demonstrated (force one test to fail once, show the captured PNG)

---

## WEEK 10 — Continuous Testing in Jenkins

**Objective:** Selenium runs inside the pipeline; failing tests block deployment.

**Updated Jenkinsfile stages**
```groovy
stage('Unit & Selenium Tests') {
    steps { sh 'mvn -B test' }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/selenium-screenshots/**', allowEmptyArchive: true
        }
    }
}
stage('Deploy') {
    when { expression { currentBuild.currentResult == 'SUCCESS' } }
    steps { sh 'cp target/*.war /opt/tomcat/webapps/ptfp.war' }
}
```
1. Install/verify the **JUnit** and **HTML Publisher** Jenkins plugins to visualize test results.
2. Run Chrome headless inside Jenkins' environment: `--headless=new --no-sandbox --disable-dev-shm-usage` (needed if Jenkins runs in Docker/CI without a display).
3. **Deliberately introduce a defect** (e.g., change the required-field validation to accept empty description), commit, push — watch the pipeline fail at the Test stage and NOT reach Deploy. Screenshot the red pipeline stage view.
4. Fix the defect in a new commit (`fix: restore required validation on description field`), push again, confirm pipeline goes fully green and deploy stage runs.

**Deliverable checklist**
- [ ] Jenkins-published JUnit test report (screenshot of trend graph)
- [ ] Screenshot: pipeline RED at Test stage (deploy blocked)
- [ ] Defect-fix commit hash/log
- [ ] Screenshot: pipeline GREEN end-to-end after fix, app redeployed

---

## WEEK 11 — Docker Image & Container Lifecycle

**Objective:** Containerize the app; know every lifecycle command cold.

**Dockerfile (multi-stage, `docker/Dockerfile`)**
```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM tomcat:10.1-jdk17-temurin
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ptfp.war
EXPOSE 8080
```
**Full lifecycle commands to run and log verbatim**
```bash
docker build -t ptfp:1.0 -f docker/Dockerfile .
docker images | grep ptfp
docker run -d --name ptfp-container -p 9090:8080 ptfp:1.0
docker ps
docker logs -f ptfp-container
docker exec -it ptfp-container /bin/bash    # inspect inside
docker stop ptfp-container
docker start ptfp-container
docker restart ptfp-container
docker rm -f ptfp-container
```

**Deliverable checklist**
- [ ] `docker/Dockerfile` committed
- [ ] `docker images` output showing tagged image + size
- [ ] Full command log (build → run → logs → stop/restart/remove) saved to `docs/week11/docker-command-log.txt`
- [ ] Screenshot of the app reachable at `http://localhost:9090/ptfp`

---

## WEEK 12 — Jenkins–Docker Continuous Deployment

**Objective:** Pipeline builds a versioned image, pushes it, and redeploys a fresh container automatically — only after tests pass.

**Extended Jenkinsfile**
```groovy
environment {
    IMAGE_NAME = "yourdockerhubuser/ptfp"
    IMAGE_TAG  = "${env.BUILD_NUMBER}"
}
stage('Docker Build') {
    steps { sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG -f docker/Dockerfile .' }
}
stage('Docker Push') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DUSER', passwordVariable: 'DPASS')]) {
            sh 'echo $DPASS | docker login -u $DUSER --password-stdin'
            sh 'docker push $IMAGE_NAME:$IMAGE_TAG'
        }
    }
}
stage('Deploy Container') {
    steps {
        sh 'docker rm -f ptfp-container || true'
        sh 'docker run -d --name ptfp-container -p 9090:8080 $IMAGE_NAME:$IMAGE_TAG'
    }
}
```
1. Create a Docker Hub repo (`yourdockerhubuser/ptfp`), add its credentials to Jenkins (`dockerhub-creds`).
2. Confirm `IMAGE_TAG` increments every build (`$BUILD_NUMBER`), so old versions stay in the registry — this is your rollback pool for Week 14.
3. Run the full pipeline end-to-end: commit → checkout → build → test (blocks on failure, per Week 10) → docker build → docker push → container redeploy.

**Deliverable checklist**
- [ ] Versioned images visible on Docker Hub (screenshot, e.g. tags `12`, `13`, `14`)
- [ ] Registry push log from Jenkins console
- [ ] Full green pipeline screenshot, all 6 stages
- [ ] Confirm redeployed container serves the newest code (small UI text change as proof)

---

## WEEK 13 — Configuration Management Script (Ansible)

**Objective:** Codify server prerequisites so any blank machine can be configured identically.

**Tasks**
1. List prerequisites: OS packages (`docker.io`, `openjdk-17-jre`), a non-root `deploy` user, folders (`/opt/ptfp`, `/opt/ptfp/logs`), files (`.env`/app config), ports (8080/9090 open), services (`docker` daemon enabled + running).
2. `ansible/inventory.ini`:
```ini
[ptfp_servers]
staging ansible_host=192.168.1.50 ansible_user=deploy ansible_ssh_private_key_file=~/.ssh/id_rsa
```
3. `ansible/playbook.yml`:
```yaml
- name: Configure PTFP target node
  hosts: ptfp_servers
  become: true
  tasks:
    - name: Install prerequisite packages
      apt:
        name: [docker.io, openjdk-17-jre, curl]
        state: present
        update_cache: true

    - name: Ensure deploy user exists
      user:
        name: deploy
        groups: docker
        append: true

    - name: Create application directories
      file:
        path: "{{ item }}"
        state: directory
        owner: deploy
        mode: '0755'
      loop:
        - /opt/ptfp
        - /opt/ptfp/logs

    - name: Ensure Docker service is running
      service:
        name: docker
        state: started
        enabled: true

    - name: Open application port (ufw)
      ufw:
        rule: allow
        port: '9090'
        proto: tcp
```
4. Run it: `ansible-playbook -i ansible/inventory.ini ansible/playbook.yml` and capture the first execution log (all tasks `changed`).

**Deliverable checklist**
- [ ] `docs/week13/server-prerequisites.md`
- [ ] `ansible/inventory.ini` + `ansible/playbook.yml` committed
- [ ] First-run execution log (screenshot/text, tasks mostly `changed`)

---

## WEEK 14 — Automated Provisioning & Reliability Validation

**Objective:** Prove idempotency, health, and rollback on a clean node.

**Tasks**
1. Provision a genuinely clean target (fresh VM / fresh Docker-based "VM" via Vagrant or a plain container acting as a host) using the Week 13 playbook.
2. Extend the playbook to also pull and run the app image:
```yaml
    - name: Pull latest PTFP image
      docker_image:
        name: "yourdockerhubuser/ptfp:{{ ptfp_version | default('latest') }}"
        source: pull

    - name: Run PTFP container
      docker_container:
        name: ptfp-container
        image: "yourdockerhubuser/ptfp:{{ ptfp_version | default('latest') }}"
        state: started
        ports: ["9090:8080"]
        restart_policy: always
```
3. **Idempotency proof:** run `ansible-playbook ...` a second time immediately — capture the output showing `changed=0` (or near-zero) because everything is already in the desired state.
4. **Health check:** add a task or separate script hitting `curl -f http://localhost:9090/ptfp/actuator/health` (add Spring Boot Actuator dependency for a real health endpoint) and fail the play if it doesn't return 200.
5. **Rollback/recovery demo:** deploy version N (broken on purpose or simulate a crash: `docker stop ptfp-container`), detect the failure via the health check, then re-run the playbook with `ptfp_version=N-1` (the previous good tag from Docker Hub) to restore service. Document the whole before/after.

**Deliverable checklist**
- [ ] Provisioned node evidence (screenshot of `docker ps` on the clean target)
- [ ] Idempotency: two consecutive run logs, second shows `changed=0`
- [ ] Health-check result (curl output / Ansible task output, 200 OK)
- [ ] Rollback demo: broken-state screenshot → recovery command → healthy-state screenshot

---

## WEEK 15 — Final End-to-End Release, Documentation & Viva

**Objective:** Run everything together once, live, and package the final report.

**Tasks**
1. Do one clean end-to-end run in front of the evaluator (or recorded): `git commit` → GitHub webhook → Jenkins pipeline (build → unit/Selenium tests → Docker build/push → container redeploy) → Ansible provisioning of a fresh node pulling the just-published image → health check green.
2. Assemble the final technical documentation:
   - System architecture (reuse/refresh Week 3 diagram)
   - Full DevOps workflow diagram (reuse/refresh Week 2 diagram, now with real tool screenshots inline)
   - Troubleshooting guide (list the 3-5 real issues you hit across 15 weeks and how you fixed them — e.g., the merge conflict, the failed pipeline, a Docker networking issue)
   - Known limitations (no horizontal scaling, no real user auth beyond basic roles, no monitoring/alerting yet)
   - Future enhancement plan (add Prometheus/Grafana monitoring, add notifications, migrate Puppet/Ansible to Terraform+K8s, add SonarQube quality gate)
3. Prepare the presentation deck: Problem → Architecture → Live Demo → Pipeline Walkthrough → Lessons Learned → Q&A.
4. Record a 5-10 min video/screenshots of the live demo as a backup in case live infra hiccups during viva.
5. Do a final `git tag -a v1.0.0 -m "Final release" && git push origin v1.0.0`.

**Deliverable checklist**
- [ ] Final repository (tagged `v1.0.0`, clean `main` branch)
- [ ] Live end-to-end demonstration (or recorded video fallback)
- [ ] Complete report PDF/Word (architecture, troubleshooting, limitations, future plan)
- [ ] Full screenshot/video set organized by week
- [ ] Presentation deck ready
- [ ] Viva Q&A prep: be ready to explain **why** each tool choice was made, not just how it works

---

## 3. Master Tool Install Checklist (do once, ideally in Week 1-3)

- [ ] JDK 17, Maven 3.9+
- [ ] Git + GitHub account
- [ ] MySQL 8 (or rely on Docker for it)
- [ ] Docker Desktop / Docker Engine
- [ ] Jenkins (via Docker, `jenkins/jenkins:lts-jdk17`)
- [ ] Google Chrome + ChromeDriver (managed automatically via WebDriverManager)
- [ ] Ansible (`pip install ansible` or `apt install ansible`)
- [ ] Tomcat 10 (or the `tomcat:10.1-jdk17-temurin` Docker image)
- [ ] Docker Hub account
- [ ] Diagramming tool: draw.io / Lucidchart / Excalidraw

## 4. Weekly Risk Notes (read before each week, saves hours)

- **Wk 7-8:** Jenkins agent needs the exact same JDK/Maven versions as your local box, or builds pass locally and fail in Jenkins — pin versions in `pom.xml` and Jenkins Tools config.
- **Wk 9-10:** Headless Chrome inside Jenkins/Docker needs `--no-sandbox --disable-dev-shm-usage`, or tests hang silently.
- **Wk 11-12:** If Jenkins itself runs inside Docker, it needs the Docker socket mounted (`-v /var/run/docker.sock:/var/run/docker.sock`) to build/run other containers — a very common blocker.
- **Wk 13-14:** Ansible needs passwordless SSH (key-based) to the target; set that up before writing any playbook, not during.
- **Wk 15:** Rehearse the live demo at least once end-to-end the day before — pipelines fail in front of evaluators more than anywhere else.

---

*This plan is designed so that every week's deliverable is graded evidence you'll directly reuse in Week 15's final report — keep everything in the `docs/weekXX/` folders as you go rather than reconstructing it at the end.*
