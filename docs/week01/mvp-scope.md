# Week 1 — MVP Scope Definition & Boundaries

**Date Approved:** September 8, 2026  
**Scope Freeze Status:** APPROVED & LOCKED (v1.0.0-MVP)

---

## 1. Scope Matrix (In-Scope vs. Out-of-Scope)

| Feature Area | In-Scope for 15-Week MVP | Explicitly Out-of-Scope | Justification for Out-of-Scope |
|---|---|---|---|
| **Feedback Capture** | Web-based submission form with Route, Category, Description, and Contact info | Native iOS/Android apps, offline paper sync | Web works across all devices without app store distribution overhead. |
| **Tracking & Search** | Query feedback by Ticket ID, status filter, and category filter | Live GPS tracking of transport fleet | Avoids hardware/telematics integration complexity. |
| **Workflow Management** | Role-based status transition (`SUBMITTED → IN_REVIEW → RESOLVED → CLOSED`) | Multi-tier bureaucratic approval hierarchies | Focuses on core DevOps lifecycle rather than enterprise hierarchy. |
| **Dashboard** | Web dashboard with count cards, status bars, and category breakdown chart (Chart.js) | Complex multi-tenant enterprise BI engines | Lightweight client-side charting provides immediate insights. |
| **User Authentication** | Role-based access control (`COMMUTER`, `OFFICER`, `ADMIN`) | OAuth2/SSO with Google/Apple, SMS OTP | Simplifies automated Selenium login scripts while proving RBAC. |
| **Notifications** | In-browser instant confirmation and status alerts | Twilio SMS gateways, push notifications | Eliminates paid third-party API dependencies in CI pipeline. |
| **Deployment** | Single Spring Boot `.jar`/`.war` serving REST API and static frontend | Microservices distributed across multiple clusters | Single deployable artifact satisfies Tomcat/Docker/Ansible objectives cleanly. |

---

## 2. MVP Feature Sign-Off
- [x] Create Feedback (Commuter)
- [x] View & Search Feedback (Commuter / Staff)
- [x] Update Status Workflow (Officer / Admin)
- [x] Summary Dashboard (Admin)
- [x] Automated CI/CD, Selenium, Docker, Ansible DevOps Pipeline (DevOps Team)
