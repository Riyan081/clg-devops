# Jenkins CI/CD Pipeline — Setup Credentials & Access Guide

> **Created:** October 13, 2026  
> **Project:** Public Transport Feedback Portal (PTFP) — TransitPulse  
> **Purpose:** Quick reference for all Jenkins credentials, URLs, and setup steps

---

## 🔑 Credentials & Access

| Item | Value |
|------|-------|
| **Jenkins URL** | `http://localhost:8080` |
| **Jenkins Admin Username** | `admin` |
| **Jenkins Admin Password** | `admin` |
| **Initial Unlock Password** | `973ccfa3bed04c8b875dd1a5ef005220` |
| **Pipeline Job Name** | `ptfp-pipeline` |
| **Pipeline Job URL** | `http://localhost:8080/job/ptfp-pipeline/` |
| **Deployed App URL** | `http://localhost:9090` (after successful deploy) |
| **Health Check URL** | `http://localhost:9090/actuator/health` |
| **H2 Console (dev)** | `http://localhost:8080/h2-console` (when app runs directly) |

---

## 🐳 Docker Container Details

| Item | Value |
|------|-------|
| **Jenkins Container Name** | `jenkins` |
| **Jenkins Docker Image** | `jenkins/jenkins:lts-jdk17` |
| **Jenkins Port Mapping** | `8080:8080` (web UI), `50000:50000` (agent) |
| **Jenkins Volume** | `jenkins_home` (persistent data) |
| **App Container Name** | `ptfp-container` (created by pipeline) |
| **App Docker Image** | `ptfp/public-transport-feedback-portal` |
| **App Port Mapping** | `9090:8080` |

---

## 🔧 Tools Configured in Jenkins

| Tool | Name (must match Jenkinsfile exactly) | Config |
|------|---------------------------------------|--------|
| **JDK** | `JDK-17` | JAVA_HOME: `/opt/java/openjdk` |
| **Maven** | `Maven-3.9` | Auto-install from Apache, version 3.9.x |

---

## 📋 Pipeline Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `DEPLOY_ENV` | Choice | `staging` | Target environment (staging / production) |
| `RUN_SELENIUM_TESTS` | Boolean | `true` | Run headless Selenium UI test suite |

---

## 🚀 Pipeline Stages (8 total)

```
1. Checkout              → Pulls code from GitHub
2. Compile & Static Analysis → mvn clean compile
3. Unit Tests            → Runs *ServiceTest, *ControllerTest
4. Package Artifact      → mvn package → ptfp.war
5. Selenium UI Quality Gate → Runs *SeleniumTest (if enabled)
6. Docker Build & Tag    → Builds Docker image from docker/Dockerfile
7. Docker Push Registry  → Pushes to registry (simulated)
8. Deploy Container      → Runs container on port 9090
9. Health Check          → curl actuator/health
```

---

## 📦 How It Was Set Up (Step-by-Step Record)

### Step 1: Started Jenkins Container
```powershell
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 `
  -v jenkins_home:/var/jenkins_home `
  -v //var/run/docker.sock:/var/run/docker.sock `
  jenkins/jenkins:lts-jdk17
```

### Step 2: Retrieved Initial Admin Password
```powershell
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
# Output: 973ccfa3bed04c8b875dd1a5ef005220
```

### Step 3: Unlocked Jenkins
- Opened `http://localhost:8080`
- Pasted the initial admin password
- Clicked "Continue"

### Step 4: Installed Plugins
- Selected "Install suggested plugins"
- Waited for all plugins to install (~3 minutes)
- Plugins installed: Git, Pipeline, JUnit, Maven Integration, etc.

### Step 5: Created Admin User
- Username: `admin`
- Password: `admin`
- Full name: `Admin`
- Email: `admin@ptfp.local`

### Step 6: Configured Tools
- **Manage Jenkins → Tools → JDK installations:**
  - Name: `JDK-17`
  - JAVA_HOME: `/opt/java/openjdk`
- **Maven installations:**
  - Name: `Maven-3.9`
  - Install automatically: ✅ (version 3.9.x from Apache)

### Step 7: Installed Docker Inside Jenkins Container
```powershell
docker exec -u root jenkins bash -c "apt-get update && apt-get install -y docker.io curl && usermod -aG docker jenkins"
docker exec -u root jenkins bash -c "chmod 666 /var/run/docker.sock"
```

### Step 8: Created Pipeline Job
- **New Item → Name:** `ptfp-pipeline` → **Type:** Pipeline
- **Pipeline section:**
  - Definition: `Pipeline script from SCM`
  - SCM: `Git`
  - Repository URL: `https://github.com/Riyan081/clg-devops.git`
  - Branch: `*/main`
  - Script Path: `Jenkinsfile`

### Step 9: Triggered First Build
- Build with Parameters → DEPLOY_ENV: staging, RUN_SELENIUM_TESTS: false
- Clicked "Build"

---

## 🔄 Common Commands (Quick Reference)

### Start/Stop Jenkins
```powershell
# Start Jenkins (if stopped)
docker start jenkins

# Stop Jenkins
docker stop jenkins

# Restart Jenkins
docker restart jenkins

# View Jenkins logs
docker logs -f jenkins
```

### Trigger a Build
- Go to `http://localhost:8080/job/ptfp-pipeline/`
- Click "Build with Parameters"
- Choose options → Click "Build"

### View Console Output
- `http://localhost:8080/job/ptfp-pipeline/<BUILD_NUMBER>/console`

### Check Deployed App
```powershell
# Check if container is running
docker ps --filter "name=ptfp-container"

# Check health
curl http://localhost:9090/actuator/health

# View app logs
docker logs -f ptfp-container
```

### Fix Docker Access (if permission denied after restart)
```powershell
docker exec -u root jenkins bash -c "chmod 666 /var/run/docker.sock"
```

---

## ⚠️ Important Notes

1. **Tool names are case-sensitive:** `JDK-17` and `Maven-3.9` must match exactly in Jenkins Tools and the Jenkinsfile
2. **Docker socket permission:** After restarting Jenkins container, you may need to re-run `chmod 666 /var/run/docker.sock`
3. **First build is slow:** Maven downloads all dependencies (~5-10 minutes). Subsequent builds are much faster
4. **Data is persistent:** The `jenkins_home` Docker volume keeps all data (jobs, builds, config) even if container is recreated
5. **GitHub repo:** Pipeline pulls from `https://github.com/Riyan081/clg-devops.git` branch `main`
