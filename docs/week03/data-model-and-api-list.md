# Week 3 — Data Model & REST API Specification

## 1. Relational Data Model (Entities)

### Entity: `Feedback` (Table: `feedbacks`)
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `BIGINT` | `PRIMARY KEY, AUTO_INCREMENT` | Unique database identifier |
| `ticket_id` | `VARCHAR(32)` | `UNIQUE, NOT NULL` | Formatted tracking code (e.g., `PTFP-20260908-4821`) |
| `route_id` | `BIGINT` | `NOT NULL, FK -> routes.id` | Associated transit route |
| `category` | `VARCHAR(32)` | `NOT NULL` | `DELAY`, `CLEANLINESS`, `SAFETY`, `STAFF`, `OTHER` |
| `passenger_name` | `VARCHAR(100)`| `NULLABLE` | Commuter name (optional) |
| `passenger_email`| `VARCHAR(100)`| `NULLABLE` | Commuter contact email |
| `description` | `TEXT` | `NOT NULL` | Grievance description |
| `status` | `VARCHAR(32)` | `NOT NULL, DEFAULT 'SUBMITTED'` | `SUBMITTED`, `IN_REVIEW`, `RESOLVED`, `CLOSED` |
| `resolution_remarks` | `TEXT` | `NULLABLE` | Staff/Officer notes upon resolution |
| `created_at` | `TIMESTAMP` | `NOT NULL` | Submission timestamp |
| `updated_at` | `TIMESTAMP` | `NOT NULL` | Last status modification timestamp |

### Entity: `Route` (Table: `routes`)
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `BIGINT` | `PRIMARY KEY, AUTO_INCREMENT` | Unique route ID |
| `route_code` | `VARCHAR(32)` | `UNIQUE, NOT NULL` | e.g. `BUS-101`, `METRO-BLUE` |
| `route_name` | `VARCHAR(100)`| `NOT NULL` | e.g. `Downtown Express`, `Airport Shuttle` |
| `transport_mode` | `VARCHAR(32)` | `NOT NULL` | `BUS`, `METRO`, `TRAIN` |

---

## 2. REST API Specification

| HTTP Method | Endpoint | Description | Request Body | Response (Status & Body) |
|---|---|---|---|---|
| `POST` | `/api/feedback` | Create a new feedback | `{ routeCode, category, passengerName, passengerEmail, description }` | `201 Created` &rarr; `FeedbackDTO` |
| `GET` | `/api/feedback` | List/search feedbacks | Query params: `?status=&category=&search=` | `200 OK` &rarr; `List<FeedbackDTO>` |
| `GET` | `/api/feedback/{id}` | Get feedback by ID or Ticket ID | — | `200 OK` &rarr; `FeedbackDTO` |
| `PUT` | `/api/feedback/{id}/status` | Update feedback status & remarks | `{ status, remarks }` | `200 OK` &rarr; `FeedbackDTO` |
| `GET` | `/api/dashboard/summary` | Get aggregated dashboard metrics | — | `200 OK` &rarr; `{ total, submitted, inReview, resolved, closed, categoryCounts: {...} }` |
| `GET` | `/api/routes` | Get available routes | — | `200 OK` &rarr; `List<RouteDTO>` |
| `GET` | `/actuator/health` | Application health check for DevOps probes | — | `200 OK` &rarr; `{ status: "UP" }` |
