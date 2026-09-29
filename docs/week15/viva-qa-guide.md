# Week 15 — DevOps Viva Voce & Presentation Defense Guide

## Core Concept Q&A for Viva Defense

### Q1: Why did you choose Java + Spring Boot + Plain HTML/JS instead of a separate React/Node frontend?
> **Answer**: Placing plain HTML/JS in `src/main/resources/static/` allows Spring Boot to serve both the REST API and the web UI from the exact same origin. This packages the application into **a single build artifact (`.war`/`.jar`)**, avoids CORS issues, eliminates Node.js build overhead, and makes automated testing and container deployment significantly simpler and more robust.

### Q2: Why did you choose Ansible over Puppet for configuration management?
> **Answer**: Ansible is agentless and operates over standard SSH using readable YAML playbooks. Puppet requires maintaining a separate Puppet Master daemon and installing agents on every target node, adding substantial infrastructure friction for small to medium deployments.

### Q3: How does your quality gate function in the Jenkins CI/CD pipeline?
> **Answer**: In the `Jenkinsfile`, the `Selenium UI Quality Gate` runs right after the unit tests. If any Selenium regression test fails, the stage exits with a non-zero code, immediately halting the pipeline and preventing defective code from reaching the `Docker Build`, `Docker Push`, or `Deploy Container` stages.

### Q4: How do you prove idempotency in your Ansible automation?
> **Answer**: By running `ansible-playbook` a second consecutive time against the target host. When all tasks return `ok` and `changed=0`, it demonstrates that the system already matches the declared state and no redundant changes are made.

### Q5: How is rollback achieved if a container deployment fails?
> **Answer**: The Jenkins pipeline tags each Docker image with the unique `${BUILD_NUMBER}`. If health checks fail on version $N$, the Ansible rollback playbook (`rollback.yml`) is triggered with `rollback_version=N-1`, pulling and spinning up the prior known good container within 15 seconds.
