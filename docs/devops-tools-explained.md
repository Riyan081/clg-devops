# DevOps Tools Explained — Simple Language Guide
### For: Public Transport Feedback Portal (PTFP) — TransitPulse

---

## 1. JENKINS

### What is Jenkins?
Jenkins is like an **automatic worker** that sits and watches your code. Every time you push code to GitHub, Jenkins automatically:
- Downloads your code
- Compiles it (checks for errors)
- Runs tests (checks if everything works)
- Packages it (creates the final `.war` file)
- Builds a Docker image
- Deploys it to a server

Without Jenkins, you'd have to do ALL of this manually every single time you change even one line of code. Jenkins does it all in **one click** or even **automatically on every push**.

### Why We Used Jenkins
- **Saves time** — No manual build/test/deploy steps
- **Catches bugs early** — If tests fail, Jenkins stops the deployment (quality gate)
- **Required by the course** — CI/CD pipeline is part of the DevOps lifecycle
- **Professional practice** — Every real company uses CI/CD tools like Jenkins

### Role of Jenkins in Our Project
Jenkins runs our **8-stage pipeline** defined in the `Jenkinsfile`:

```
Code Push → Checkout → Compile → Unit Tests → Package WAR 
→ Selenium Tests → Docker Build → Deploy → Health Check
```

- If **unit tests fail** → Jenkins stops. No broken code gets deployed.
- If **Selenium tests fail** → Jenkins stops. No buggy UI goes live.
- If **everything passes** → Jenkins builds a Docker image and deploys the app automatically.

**Key Point for Viva:** Jenkins is the "brain" of our CI/CD pipeline. It automates the entire process from code commit to deployment.

---

## 2. SELENIUM

### What is Selenium?
Selenium is a **robot that pretends to be a human using your website**. It opens a real browser (Chrome), clicks buttons, fills forms, reads text — exactly like a real user would. But it does it automatically and checks if everything works correctly.

Think of it like this: Instead of YOU manually opening the website, filling the feedback form, clicking submit, and checking if it worked — **Selenium does all of that for you automatically in seconds**.

### Why We Used Selenium
- **Automated UI testing** — Tests the frontend (HTML pages) automatically
- **Catches UI bugs** — If a button stops working or a form breaks, Selenium catches it
- **Quality Gate** — Jenkins runs Selenium tests BEFORE deploying. If the UI is broken, deployment is blocked.
- **Required by the course** — Selenium testing is part of the Week 9-10 deliverables

### Role of Selenium in Our Project
We have **5 Selenium test classes** that test the most critical user flows:

| Test | What It Does |
|------|-------------|
| `SubmitFeedbackSeleniumTest` | Opens the feedback form → fills in route, category, description → clicks submit → checks if success message appears |
| `SearchFeedbackSeleniumTest` | Opens the feedback list → uses search/filter → checks if correct results show up |
| `StatusWorkflowSeleniumTest` | Opens admin page → changes ticket status from SUBMITTED to IN_REVIEW → checks if it updated |
| `ValidationSeleniumTest` | Tries to submit an empty form → checks if validation error messages appear |
| `DashboardSeleniumTest` | Opens admin dashboard → checks if metrics (total, submitted, resolved counts) are displayed |

**Key Point for Viva:** Selenium tests the actual user interface in a real browser. It's different from unit tests (which test Java code logic). Selenium tests the full flow — frontend HTML/JS → API call → backend → database → response back to UI.

---

## 3. DOCKER

### What is Docker?
Imagine you built your app on your laptop and everything works perfectly. Now you want to run it on a server. Problem: the server has a different Java version, different OS, missing libraries — **your app breaks**.

Docker solves this by putting your app inside a **container** — think of it like a **shipping container for software**. Everything your app needs (Java, libraries, your WAR file) is packed inside one container. This container runs **exactly the same** on your laptop, on a server, on the cloud — anywhere.

### Why We Used Docker
- **"Works on my machine" problem solved** — The container runs identically everywhere
- **Easy deployment** — Just run `docker run` and the app starts. No installation needed.
- **Isolation** — The app runs in its own box. Doesn't conflict with other software on the server.
- **Versioning** — Each build gets a version tag (Build #1, #2, #3). You can **rollback** to any previous version.
- **Required by the course** — Containerization is Week 11-12 deliverable

### Role of Docker in Our Project

#### The Dockerfile (`docker/Dockerfile`)
This is the **recipe** that tells Docker how to build our container:

```
Step 1: Start with a Maven + JDK 17 base image (has tools to compile Java)
Step 2: Copy our pom.xml and source code into the container
Step 3: Run "mvn package" to build the ptfp.war file
Step 4: Switch to a slim JRE-only image (smaller, more secure)
Step 5: Copy the ptfp.war from Step 3 into this slim image
Step 6: Set up a non-root user (security)
Step 7: Expose port 8080 and add a health check
Step 8: Run the app with "java -jar ptfp.war"
```

This is called a **multi-stage build** — Stage 1 builds the app (big image with Maven), Stage 2 only keeps the final WAR file in a small image. Result: smaller, faster, more secure container.

#### The docker-compose.yml
This is a **shortcut file** so you don't have to type long `docker run` commands:

```yaml
# Instead of typing this long command:
docker run -d --name ptfp-app -p 9090:8080 -e SPRING_PROFILES_ACTIVE=dev ptfp/public-transport-feedback-portal:latest

# You just type:
docker-compose up -d
```

It also defines a **health check** that automatically monitors if the app is alive.

**Key Point for Viva:** Docker containerizes our application. The Dockerfile defines HOW to build the container. docker-compose makes it easy to run. Jenkins builds and deploys Docker containers automatically.

---

## 4. JENKINS + DOCKER LIFECYCLE (How They Work Together)

This is the complete flow of what happens when you push code:

```
    YOU push code to GitHub
            ↓
    JENKINS detects the change (polling/webhook)
            ↓
    Stage 1: CHECKOUT — Jenkins downloads your latest code from GitHub
            ↓
    Stage 2: COMPILE — Jenkins runs "mvn compile" to check for syntax errors
            ↓
    Stage 3: UNIT TESTS — Jenkins runs Java tests (UserServiceTest, FeedbackServiceTest, etc.)
            ↓                    ❌ If tests FAIL → Pipeline STOPS here. No deploy.
    Stage 4: PACKAGE — Jenkins runs "mvn package" → creates ptfp.war
            ↓
    Stage 5: SELENIUM TESTS — Jenkins opens a headless browser, runs UI tests
            ↓                    ❌ If UI tests FAIL → Pipeline STOPS. No deploy.
    Stage 6: DOCKER BUILD — Jenkins uses our Dockerfile to build a container image
            ↓                    Tags it with build number (e.g., ptfp:3)
    Stage 7: DOCKER PUSH — Jenkins pushes the image to Docker registry
            ↓                    (simulated in our project)
    Stage 8: DEPLOY — Jenkins removes old container, starts new one
            ↓                    "docker rm -f ptfp-container"
            ↓                    "docker run -d --name ptfp-container -p 9090:8080 ptfp:3"
    Stage 9: HEALTH CHECK — Jenkins checks http://localhost:9090/actuator/health
            ↓                    ✅ Returns 200 OK → BUILD SUCCESS
            ↓                    ❌ Returns error → BUILD FAILED

    🎉 App is now live at http://localhost:9090
```

### Why This Matters
- **Every version is a Docker image** — Build #1, #2, #3 are all saved. You can rollback anytime.
- **Quality Gates** — Bad code NEVER reaches production. Tests must pass first.
- **Zero downtime** — Old container is removed, new one starts immediately.
- **Fully automated** — From code push to running app, zero manual steps.

**Key Point for Viva:** Jenkins is the "orchestrator" that automates the entire lifecycle. Docker is the "packaging" that makes deployment consistent. Together, they enable true Continuous Integration and Continuous Deployment (CI/CD).

---

## 5. ANSIBLE

### What is Ansible?
Ansible is a **remote server setup tool**. Imagine you have 10 servers and you need to:
- Install Java on all of them
- Install Docker on all of them
- Create the same folders on all of them
- Open the same firewall ports on all of them

Doing this manually on 10 servers is painful and error-prone. **Ansible does it for you** — you write what you want ONCE in a file, and Ansible connects to all servers via SSH and configures them automatically.

Think of Ansible as a **checklist executor** — you write "I want Java installed, Docker running, port 9090 open" and Ansible makes sure ALL of that is done on every server.

### Why We Used Ansible
- **Infrastructure as Code** — Server setup is written in files (can be versioned in Git)
- **Idempotent** — Run it 100 times, result is the same. It won't install Java 100 times. It checks "is Java installed? Yes → skip. No → install."
- **Agentless** — Only needs SSH access to target servers. No special software needed on the servers.
- **Required by the course** — Configuration management is Week 13-14 deliverable

### Role of Ansible in Our Project
Ansible handles **server provisioning** — setting up a fresh server so it can run our Docker container.

---

### Ansible Files Explained

#### 1. `inventory.ini` — The Server Address Book

```ini
[ptfp_staging]
staging-server ansible_host=192.168.1.50 ...

[ptfp_production]
production-server ansible_host=192.168.1.100 ...
```

**What it does:** This is simply a **list of servers** Ansible should manage. Like a phone book — it tells Ansible "here are the servers, here's how to connect to them (IP address, username, SSH key)."

- `[ptfp_staging]` = Group name for staging servers
- `[ptfp_production]` = Group name for production servers
- `ansible_host` = IP address of the server
- `ansible_user` = Username to SSH with

**Simple explanation:** "Ansible, these are the servers you need to configure."

---

#### 2. `playbook.yml` — The Main Setup Script

**What it does:** This is the **main instruction file** that tells Ansible WHAT to configure on the servers. It runs 7 tasks in order:

| Task # | What It Does | Why |
|--------|-------------|-----|
| 1 | `apt install curl, git, openjdk-17, ufw` | Installs required software packages |
| 2 | Create `deploy` user | Security — don't run apps as root |
| 3 | Create `/opt/ptfp` and `/opt/ptfp/logs` directories | App needs folders for its files and logs |
| 4 | Ensure Docker service is running | Docker must be active to run containers |
| 5 | Open port 9090 in firewall | So users can access the app from outside |
| 6 | Deploy PTFP Docker container | Pulls and runs our app container |
| 7 | Health check — hit `/actuator/health` | Verifies the app actually started correctly |

**Simple explanation:** "Ansible, on those servers from the inventory, install these packages, create these folders, start Docker, run our app, and make sure it's healthy."

**Idempotency example:**
- First run: All 7 tasks show `changed` (it installed/created everything)
- Second run: All 7 tasks show `ok` (everything already exists, nothing to do)
- This proves the playbook is **idempotent** — safe to run repeatedly.

---

#### 3. `rollback.yml` — The Emergency Recovery Script

**What it does:** If a new deployment is **broken** (app crashes, bugs), this script **rolls back to a previous working version**. It does 3 things:

| Task # | What It Does | Why |
|--------|-------------|-----|
| 1 | Stop and remove the broken container | Get rid of the bad version |
| 2 | Start a container with the previous good version | Restore the old working version |
| 3 | Health check the rolled-back container | Verify the rollback actually fixed things |

**When would you use this?**
```
Situation: You deployed Build #5, but it has a bug.
Action:    Run rollback.yml with target_version=4
Result:    Build #5 container is removed, Build #4 container starts
Outcome:   App is back to the working version while you fix the bug
```

**Simple explanation:** "Ansible, the new version is broken. Remove it and put back the old working version."

---

## Quick Summary Table

| Tool | One-Line Description | Week |
|------|---------------------|------|
| **Jenkins** | Automates build → test → deploy pipeline | Week 7-8 |
| **Selenium** | Tests the website UI automatically in a real browser | Week 9-10 |
| **Docker** | Packages the app into a portable container | Week 11-12 |
| **Ansible** | Sets up and configures servers automatically | Week 13-14 |

### How They All Connect

```
Developer pushes code
     ↓
JENKINS pulls code, runs tests, builds DOCKER image
     ↓
JENKINS deploys DOCKER container
     ↓
SELENIUM tests verify the deployed app works
     ↓
ANSIBLE can provision new servers and deploy the same container
     ↓
If something breaks → ANSIBLE rollback.yml restores previous version
```

**The Big Picture:** Code → Jenkins (automate) → Docker (package) → Selenium (verify) → Ansible (provision/deploy/rollback). This is the **DevOps CI/CD lifecycle** in action.
