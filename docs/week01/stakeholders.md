# Week 1 — Stakeholder Analysis & User Roles

## 1. Primary Actors & Users

| User Persona | Responsibilities & Needs | System Interaction |
|---|---|---|
| **Commuter** | • Log feedback regarding transit experience (delays, cleanliness, safety, staff).<br>• Search and track complaint resolution status via unique Ticket/Feedback ID.<br>• Expects instant submission feedback and quick response. | Accesses public web portal (`/feedback-form.html`, `/feedback-list.html`). Zero authentication needed for basic submission. |
| **Transport Officer / Field Operator** | • Review incoming commuter tickets.<br>• Update status (`SUBMITTED → IN_REVIEW → RESOLVED → CLOSED`).<br>• Add operational notes / resolution details. | Authenticated portal access (`/feedback-list.html`) with role-based status change permissions. |
| **Transport Administrator** | • Monitor overall transport network health and service trends.<br>• Analyze category breakdowns, SLA resolution metrics, and route performance.<br>• Manage route master lists and staff accounts. | Accesses executive analytics dashboard (`/dashboard.html`). |

---

## 2. Secondary & Organizational Stakeholders

1. **City Transport Authority (Management)**:
   - Evaluates fleet operational efficiency and vendor accountability.
   - Leverages aggregated reports to allocate maintenance resources and adjust bus schedules.
2. **DevOps Engineering Team**:
   - Maintains continuous integration, automated testing gates, container deployment, and server provisioning.
   - Ensures high availability and automated rollback on failure.
3. **Academic Evaluator / Review Panel**:
   - Assesses adherence to modern software engineering, automated CI/CD pipelines, Selenium test suites, Docker orchestration, and Ansible configuration management.
