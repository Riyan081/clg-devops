# Week 3 — Software Requirements Specification (SRS) Summary

## 1. Introduction
The **Automated Public Transport Feedback Portal** is a web-based management system enabling transit passengers to file grievances regarding bus and rail services, and empowering transport authorities to track, update, and resolve complaints with full operational visibility.

---

## 2. Functional Requirements (FR)

| ID | Requirement Description | Actor | Priority |
|---|---|---|---|
| **FR-01** | Create Feedback: Submit route, category, description, and contact info. | Commuter | High |
| **FR-02** | Unique Ticket ID: Generate formatted tracking ID (`PTFP-<timestamp>-<rand>`). | System | High |
| **FR-03** | Search & Filter: Filter complaints by status, category, or search by keyword/route. | Commuter / Staff | High |
| **FR-04** | Status Workflow: Update complaint status from `SUBMITTED → IN_REVIEW → RESOLVED → CLOSED`. | Officer / Admin | High |
| **FR-05** | Real-time Dashboard: View total feedback count, resolution rate, and category distribution. | Admin | Medium |
| **FR-06** | Data Validation: Enforce required fields, string lengths, and valid status transitions. | System | High |
| **FR-07** | Health Monitoring: Expose `/actuator/health` endpoint for DevOps automated probes. | DevOps / System | High |

---

## 3. Non-Functional Requirements (NFR)

1. **Performance**:
   - Web interface page load time $< 2\text{ seconds}$ on standard broadband/local connection.
   - REST API latency $< 200\text{ ms}$ for search and submission operations.
2. **Reliability & Idempotency**:
   - System state must remain deterministic under concurrent feedback submissions.
   - Infrastructure playbooks (Ansible) must be $100\%$ idempotent (`changed=0` on re-execution).
3. **Usability & Portability**:
   - Responsive HTML5 UI compatible across modern desktop and mobile browsers.
   - Zero client-side installation requirements.
4. **Maintainability & CI/CD**:
   - Fully automated build, test, and container packaging triggered via Git push.
