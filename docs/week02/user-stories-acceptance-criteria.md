# Week 2 — User Stories & Acceptance Criteria

## User Story Matrix

### Story 1: Commuter Feedback Submission
- **ID**: `US-01` (Priority: High | Story Points: 5)
- **User Story**: *As a Commuter, I want to submit feedback with transit route, issue category, and description, so that my grievance is logged in the system.*
- **Acceptance Criteria (Given / When / Then)**:
  - **Given** I am on the feedback submission form (`/feedback-form.html`),
  - **When** I fill in a valid route, select a category (Delay, Cleanliness, Safety, Staff), and enter a description,
  - **And** click the "Submit Feedback" button,
  - **Then** the system creates a feedback record with status `SUBMITTED`, generates a unique Ticket ID, and displays a success alert containing the Ticket ID.
  - **Given** I leave mandatory fields blank (e.g. route or description),
  - **When** I submit the form,
  - **Then** client-side and server-side validation error messages appear, and no record is persisted.

---

### Story 2: Feedback Search & Resolution Tracking
- **ID**: `US-02` (Priority: High | Story Points: 3)
- **User Story**: *As a Commuter, I want to search feedback by status, category, or Ticket ID, so that I can track the progress of my complaint.*
- **Acceptance Criteria**:
  - **Given** I am on the feedback list page (`/feedback-list.html`),
  - **When** I filter by status (e.g. `IN_REVIEW`) or search by keyword,
  - **Then** the table updates dynamically to display only matching feedback records with their current status and timestamp.

---

### Story 3: Role-Based Status Workflow Transition
- **ID**: `US-03` (Priority: High | Story Points: 5)
- **User Story**: *As a Transport Officer, I want to update a complaint's status from `SUBMITTED` to `IN_REVIEW`, `RESOLVED`, or `CLOSED`, so that commuters and administrators have an accurate picture of resolution.*
- **Acceptance Criteria**:
  - **Given** an officer views a feedback record,
  - **When** they select a valid next status from the dropdown and submit the change,
  - **Then** the database updates `status` and `updated_at`, and the record reflects the new state immediately in the UI.

---

### Story 4: Executive Analytics Dashboard
- **ID**: `US-04` (Priority: Medium | Story Points: 5)
- **User Story**: *As an Admin, I want to view a real-time summary dashboard with aggregate complaint counts and category charts, so that I can spot service trends.*
- **Acceptance Criteria**:
  - **Given** an admin opens the dashboard (`/dashboard.html`),
  - **When** the page loads,
  - **Then** metric cards show total complaints, total open, total resolved, and a breakdown chart renders complaint distribution across categories.

---

### Story 5: Route Master Management
- **ID**: `US-05` (Priority: Medium | Story Points: 3)
- **User Story**: *As an Admin, I want to view and manage available transit routes, so that commuters can only select valid transport routes.*
- **Acceptance Criteria**:
  - **Given** the application initializes,
  - **When** a user accesses the feedback form route dropdown,
  - **Then** active transit routes (e.g., Route 101, Metro Blue Line) are loaded from the database.
