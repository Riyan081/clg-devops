# Week 11 — Docker Image Build & Container Lifecycle Log

## 1. Container Lifecycle Commands & Execution Log

```bash
# 1. Build Multi-Stage Docker Image
docker build -t ptfp/public-transport-feedback-portal:1.0 -f docker/Dockerfile .

# 2. Inspect Image Details & Size
docker images | grep ptfp
# Output: ptfp/public-transport-feedback-portal   1.0   743a129ef10c   2 minutes ago   285MB

# 3. Run Container with Port Mapping (Host: 9090 -> Container: 8080)
docker run -d --name ptfp-container -p 9090:8080 ptfp/public-transport-feedback-portal:1.0

# 4. Check Container Process & Health Status
docker ps --filter "name=ptfp-container"
# Output: Up 45 seconds (healthy)   0.0.0.0:9090->8080/tcp   ptfp-container

# 5. Inspect Container Application Logs
docker logs ptfp-container | head -n 25

# 6. Verify Container Reachability
curl -I http://localhost:9090/actuator/health
# HTTP/1.1 200 OK
# Content-Type: application/vnd.spring-boot.actuator.v3+json

# 7. Stop and Restart Container
docker stop ptfp-container
docker start ptfp-container

# 8. Clean up Container
docker rm -f ptfp-container
```
