# Week 1 — Objectives & Measurable Success Criteria

## 1. Functional Objectives
1. **Seamless Feedback Capture**: Enable commuters to submit structured feedback with route ID, category, priority, and description in under 30 seconds.
2. **Deterministic Status Workflow**: Implement state machine enforcing legitimate status progressions:
   $$\text{SUBMITTED} \longrightarrow \text{IN\_REVIEW} \longrightarrow \text{RESOLVED} \longrightarrow \text{CLOSED}$$
3. **Real-time Query & Search**: Allow filtering of complaints by status, category, and transit route with sub-second response times.
4. **Summary & Analytics Dashboard**: Render visual metrics showing total complaints, resolved percentages, and category distribution charts.

---

## 2. DevOps & Engineering Objectives
1. **Version Control & Quality Gates**: 100% of code integrated via pull requests with automated test execution.
2. **Continuous Integration (CI)**: Maven build and test automation triggered on every push or schedule in Jenkins.
3. **Automated Quality Gate**: Selenium UI regression suite blocks deployment if any critical path fails.
4. **Containerization & Continuous Deployment (CD)**: Multi-stage Docker image published to registry and deployed with zero manual host configuration.
5. **Infrastructure as Code (IaC)**: Target node fully provisioned and validated using idempotent Ansible playbooks.

---

## 3. Measurable Success Criteria (Baseline for Week 15 Evaluation)

| Metric | Target Goal | Verification Method |
|---|---|---|
| **CRUD & Workflow Completeness** | 100% of core entities (User, Route, Feedback) fully functional | Automated JUnit integration tests |
| **UI Automated Test Pass Rate** | $\ge 90\%$ (Target: 100%) on critical user paths | Selenium WebDriver automated suite |
| **Commit-to-Deploy Duration** | $< 10\text{ minutes}$ | Jenkins pipeline execution timeline |
| **Deployment Idempotency** | `changed=0` on repeated playbook execution | Ansible run verification log |
| **Application Response Latency** | $< 2\text{ seconds}$ on local dev/test environment | Chrome DevTools network panel |
| **Zero-Downtime Rollback** | Automated recovery to previous stable container $(N-1)$ on failure | Ansible health-check & rollback execution |
