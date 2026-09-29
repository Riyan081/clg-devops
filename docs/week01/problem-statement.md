# Week 1 — Problem Statement

## 1. Executive Summary
Urban public transportation networks (buses, metros, suburban trains) are critical lifelines for daily commuters. However, commuters currently face severe friction when reporting service disruptions, chronic delays, unsanitary conditions, safety hazards, and staff misconduct.

Existing grievance channels are disjointed, relying on paper logbooks, manual complaint counters, or unmonitored general email inboxes. Consequently:
- Commuters receive no acknowledgment, tracking reference, or estimated resolution timeline.
- Grievances are frequently lost or ignored, eroding public trust in transit authorities.
- Transit management lacks aggregated visibility into recurring failure points (e.g., specific bus routes experiencing frequent breakdowns or safety incidents).

## 2. Problem Statement
> **"Commuters lack a transparent, centralized, and accessible digital portal to log, categorize, and track public transport feedback in real time, while transit operators and administrators lack an automated system to triage issues, enforce resolution workflows, and analyze operational performance trends."**

## 3. Core Objectives
1. **Empower Commuters**: Provide a lightweight, zero-install web interface to submit categorized complaints with route identifiers and instantly receive a trackable reference.
2. **Streamline Operations**: Automate complaint routing and status transitions (`SUBMITTED → IN_REVIEW → RESOLVED → CLOSED`) with audit timestamps.
3. **Actionable Insights**: Deliver an executive dashboard with category distributions, average resolution times, and route hotspot metrics.
4. **DevOps Automation**: Ensure the entire software lifecycle from code commit to container deployment and infrastructure provisioning is 100% automated and reproducible.
