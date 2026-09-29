# Week 4 — Git Branching Policy & Contribution Workflow

## 1. Branch Hierarchy

```
[ main ]  (Production-ready releases only; protected; tagged releases v0.1.0, v1.0.0)
   ▲
   │ Merge PR (Release candidate)
[ develop ] (Active integration branch; protected; CI builds triggered here)
   ▲
   ├───────────────────────────┬───────────────────────────┐
   │                           │                           │
[ feature/12-submit-feedback ] [ feature/13-search-feedback] [ bugfix/24-null-route ]
```

---

## 2. Branch Naming Conventions

| Branch Type | Format Pattern | Example |
|---|---|---|
| **Feature** | `feature/<issue-id>-<short-description>` | `feature/12-submit-feedback` |
| **Bug Fix** | `bugfix/<issue-id>-<short-description>` | `bugfix/31-fix-validation` |
| **Release** | `release/v<major>.<minor>.<patch>` | `release/v1.0.0` |
| **Hotfix** | `hotfix/<incident-id>-<short-desc>` | `hotfix/42-db-reconnect` |

---

## 3. Pull Request & Code Review Rules
1. **Target Branch**: All feature and bugfix PRs must target `develop`.
2. **Reviewers**: At least one peer review approval required before merge.
3. **Automated Checks**: Jenkins CI build + JUnit tests must pass.
4. **Merge Strategy**: Squash and merge or rebase merge to keep a clean, linear Git history.
