# Machine Coding Mentorship Plan (6 Weeks)

## How to use this file
- Do **2 timed attempts** per week (45 minutes each).
- Do **1 deep review** and **1 reattempt** for your weakest problem.
- Update the tracker table after every attempt.
- Aim for progress, not perfection.

## Goals
By the end of 6 weeks, you should be able to:
1. Convert a problem statement into classes/interfaces in 8-12 minutes.
2. Ship a runnable Java solution in 45 minutes.
3. Handle edge cases confidently.
4. Explain tradeoffs clearly (time complexity, extensibility, compromises).
5. Score at least **80/100** consistently in mock rounds.

## Weekly Roadmap

### Week 1 - Foundations (Modeling + Clean Structure)
- Focus: entities, enums, service layer, in-memory storage, clean method boundaries.
- Projects:
  - P1: Ulearn Trending Courses
  - P2: Trending Instructors
- Deliverable: runnable solution + sample input/output + assumptions.

### Week 2 - Sorting/Ranking + Edge Cases
- Focus: ranking rules, tie-breakers, validation, predictable outputs.
- Projects:
  - P3: Leaderboard Service
  - P4: Top K Products by timeframe
- Deliverable: handle invalid timeframe, empty data, ties.

### Week 3 - Stateful Systems
- Focus: state transitions, invariants, encapsulation.
- Projects:
  - P5: Parking Lot Lite
  - P6: Movie Ticket Booking Lite
- Deliverable: no invalid transitions; clear status model.

### Week 4 - Modular LLD
- Focus: extensibility with strategy/factory, split responsibilities.
- Projects:
  - P7: Food Ordering Lite
  - P8: Splitwise Lite
- Deliverable: easy to add a new ranking/payment/split strategy.

### Week 5 - Performance + Concurrency Basics
- Focus: O(log n)/O(1) choices, thread-safe operations where required.
- Projects:
  - P9: Rate Limiter
  - P10: Notification Scheduler
- Deliverable: justify data structures and complexity.

### Week 6 - Interview Simulation
- Focus: real 45-minute mocks + 10-minute design defense.
- Projects:
  - Reattempt 4 weakest problems under strict timer.
- Deliverable: 3 consecutive attempts >= 80/100.

## Project Ladder (in order)
1. Ulearn Trending Courses (timeframe-based ranking)
2. Trending Instructors
3. Leaderboard Service
4. Top K Products by timeframe
5. Parking Lot Lite
6. Movie Ticket Booking Lite
7. Food Ordering Lite
8. Splitwise Lite
9. Rate Limiter
10. Notification Scheduler

## 45-Minute Interview Execution Template
- **0-5 min**: Clarify requirements and assumptions.
  - Inputs? output format? tie-breakers? invalid cases?
- **5-12 min**: Design quickly.
  - Classes, interfaces, relationships, key methods.
- **12-32 min**: Implement happy path end-to-end.
- **32-40 min**: Add edge-case handling + minimal tests/demo in `main`.
- **40-45 min**: Explain complexity, tradeoffs, and next improvements.

## Evaluation Rubric (100 points)
- Correctness: 40
- LLD quality (separation of concerns, extensibility): 25
- Code quality (readability, naming, validation): 20
- Communication (assumptions, tradeoffs, clarity): 15

## Self-Review Checklist (before submission)
- Does code compile and run?
- Is output deterministic for same input?
- Are tie-breakers implemented exactly?
- Are null/empty/invalid cases handled?
- Can a new requirement be added without rewriting core logic?
- Did I explain complexity and tradeoffs?

## Tracker

| Date | Project | Attempt # | Timed 45m (Y/N) | Score /100 | Top 3 Issues Found | Reattempt Date | Reattempt Score | Notes |
|------|---------|-----------|-----------------|------------|--------------------|----------------|-----------------|-------|
| 2026-05-19 | P1 Ulearn Trending Courses | 1 | N | — | 1) Overengineered w/ CourseDetails/UserDetails. 2) Broken stream logic. 3) No working implementation. | 2026-05-20 | — | Started design, need to clarify requirements first. Will restart with clean approach. |
|      | P2 Trending Instructors | 1 |  |  |  |  |  |  |
|      | P3 Leaderboard Service | 1 |  |  |  |  |  |  |
|      | P4 Top K Products | 1 |  |  |  |  |  |  |
|      | P5 Parking Lot Lite | 1 |  |  |  |  |  |  |
|      | P6 Movie Ticket Booking | 1 |  |  |  |  |  |  |
|      | P7 Food Ordering Lite | 1 |  |  |  |  |  |  |
|      | P8 Splitwise Lite | 1 |  |  |  |  |  |  |
|      | P9 Rate Limiter | 1 |  |  |  |  |  |  |
|      | P10 Notification Scheduler | 1 |  |  |  |  |  |  |

## Mentor Review Log
Use this section when you share code for review.

### Review Entry Template
- Project:
- What worked well:
- Critical bugs:
- LLD improvements:
- Interview communication feedback:
- Must-fix before next round:
- Target score for reattempt:

---

## Mentor-Led Accountability Mode (Strict)

This prep is mentor-led. I will actively drive your schedule, review quality, and consistency.

### Commitment Rules
- You commit to **2 timed attempts/week**, **1 review**, and **1 reattempt**.
- Every attempt must be logged in the `Tracker` on the same day.
- No skipping updates: if you miss a day, you still log it as missed.

### Escalation Policy (Firm)
- **Strike 1 (missed deliverable):** Immediate recovery task due in 24 hours.
- **Strike 2 (same week):** Extra timed mock added that weekend.
- **Strike 3 (same week):** Next week starts with reattempts only; no new project until score recovers.

### Score Gates
- You move to the next project only if:
  - latest attempt score is **>= 70/100**, and
  - all critical bugs from review are fixed.
- Week 6 simulation requires at least **2 projects with >= 80/100** beforehand.

### Miss Handling
- If you become inconsistent, feedback becomes stricter and more direct.
- We prioritize discipline over variety until consistency is restored.
- The goal is interview readiness, not comfort.

### Weekly Check-In Format
- `Planned:` what you committed this week
- `Done:` what you actually completed
- `Blocked:` what stopped you
- `Next:` exact date/time for next timed attempt

---

If you want, we can now start with **P1: Ulearn Trending Courses** and I will give you the exact interview-style problem statement, sample input, and expected output format.
