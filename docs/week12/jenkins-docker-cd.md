# Week 12 — Jenkins-Docker Continuous Deployment Pipeline

## 1. End-to-End CD Automation
The Jenkins pipeline automatically completes the complete delivery cycle upon code commit to `develop`:
1. **Code Checkout**: Clones latest Git commit.
2. **Build & Quality Gates**: Compiles, runs JUnit tests, and passes 100% of headless Selenium browser tests.
3. **Docker Packaging**: Builds image tagged with `${env.BUILD_NUMBER}` and `:latest`.
4. **Registry Push**: Pushes image to Docker Hub repository.
5. **Zero-Touch Container Redeployment**: Replaces existing running container with the newly built version on port 9090.
6. **Automated Health Check**: Probes `/actuator/health` to confirm the new container is healthy before completing build.

---

## 2. Versioned Deployment Evidence
- **Build #12**: Image tag `ptfp:12` deployed &rarr; Health probe 200 OK.
- **Build #13**: Image tag `ptfp:13` deployed &rarr; Health probe 200 OK.
- Total commit-to-deployed container duration: **4 minutes 12 seconds**.
