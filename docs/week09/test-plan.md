# Week 9 — Selenium Test Plan & Critical User Journeys

## 1. Test Suite Objective
Automate end-to-end browser interactions across 5 critical user journeys to guarantee functional integrity and prevent regressions before any production release.

---

## 2. Critical Journey Test Specifications

| Journey ID | Test Class | User Journey / Scenario | Assertions & Expected Result |
|---|---|---|---|
| **TC-01** | `SubmitFeedbackSeleniumTest` | Commuter fills feedback form with route, category (Delay), and description, then submits. | Success banner displays; unique Ticket ID (`PTFP-...`) generated. |
| **TC-02** | `SearchFeedbackSeleniumTest` | User filters feedback table by search keyword (`ROUTE-101`). | Table filters to matching route records. |
| **TC-03** | `StatusWorkflowSeleniumTest` | Officer updates complaint status from `SUBMITTED` to `RESOLVED` in table dropdown. | Status badge dynamically updates to `RESOLVED` via async REST call. |
| **TC-04** | `ValidationSeleniumTest` | User attempts to submit form with empty mandatory fields. | Validation alert appears; form submission blocked. |
| **TC-05** | `DashboardSeleniumTest` | Administrator opens summary analytics page. | Metric cards load non-zero values and category breakdown renders. |

---

## 3. Asynchronous Wait & Failure Screenshot Policy
- **Async Wait**: Because frontend utilizes `fetch()`, all assertions employ explicit `WebDriverWait` on DOM elements.
- **Failure Capture**: Screenshots on test failure automatically saved to `target/selenium-screenshots/<test_name>_<timestamp>.png`.
