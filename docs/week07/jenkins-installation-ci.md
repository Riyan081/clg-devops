# Week 7 — Jenkins Installation & Continuous Integration Job

## 1. Jenkins Installation via Docker
```bash
# Run Jenkins with Docker socket volume mounted for downstream Docker stages
docker run -d --name jenkins \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts-jdk17

# Retrieve initial unlock password
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

---

## 2. Tool & Plugin Configuration
1. **Plugins Installed**: Git Plugin, Maven Integration Plugin, Pipeline, JUnit Plugin, HTML Publisher Plugin.
2. **Tools Configured** (`Manage Jenkins → Tools`):
   - **JDK**: Name `JDK-17`, Home `/opt/java/openjdk` (or auto-install JDK 17).
   - **Maven**: Name `Maven-3.9`, version 3.9.6.

---

## 3. Freestyle CI Job (`ptfp-ci-build`)
- **Repository URL**: `https://github.com/your-org/public-transport-feedback-portal.git`
- **Branch**: `*/develop`
- **Build Trigger**: Poll SCM (`H/5 * * * *`) or GitHub webhook trigger.
- **Build Steps**: Top-level Maven target `clean package`.
- **Post-Build Actions**: Archive the artifacts &rarr; `target/*.war, target/*.jar`.
- **Status**: Build #1 completed with `SUCCESS` in 48s.
