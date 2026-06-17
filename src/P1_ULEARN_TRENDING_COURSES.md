# P1 - Ulearn Trending Courses (Interview Pack)

## Problem Statement (Interviewer Version)
Design and implement an in-memory backend module for Ulearn to fetch top trending courses for a given timeframe.

A course is considered trending based on the number of unique users who completed the course in the requested timeframe.

You are given completion events in the form:
- `userId`
- `courseId`
- `completedAt` (timestamp)

Implement a method:
- `List<CourseTrend> getTopTrendingCourses(Timeframe timeframe, int topN, Instant now)`

Return top `N` courses sorted by:
1. completion count (descending)
2. course name (ascending) as tie-breaker

The code must run from `main` with sample data and print output.

---

## 45-Minute Scope (What you must finish)
- Define core entities (`Course`, `CompletionEvent`, `Timeframe`, `CourseTrend`).
- Implement service layer for ranking.
- Filter events by timeframe window relative to `now`.
- Count **unique users per course** in that window.
- Sort with tie-breaker and return top N.
- Add demo data in `main` and print outputs for 1 day, 1 week, 1 month.

## Out of Scope (avoid in this round)
- Database integration
- REST APIs
- Authentication/authorization
- Distributed caching

---

## Functional Requirements
1. Support timeframes: `ONE_DAY`, `ONE_WEEK`, `ONE_MONTH`.
2. Ignore completion events outside timeframe.
3. If same user completes same course multiple times in timeframe, count once.
4. If `topN` is larger than available courses, return all available.
5. For invalid `topN` (<= 0), return empty list.

## Non-Functional Requirements
- Keep code modular and readable.
- Deterministic output for same input.
- Reasonable complexity for in-memory interview solution.

---

## Clarifying Questions You Should Ask (first 5 minutes)
1. Should duplicate completions by same user for same course count multiple times?
2. Is tie-breaker required when counts are equal?
3. Is timeframe inclusive of boundary timestamp?
4. Should courses with zero completions be returned?
5. What to do for invalid timeframe/topN?

Recommended assumptions if interviewer says "you decide":
- Count unique users per course.
- Boundary is inclusive (`>= windowStart && <= now`).
- Return only courses with at least one completion in window.
- Invalid `topN` returns empty list.

---

## Suggested LLD (minimum expected)

### Entities
- `Course`
  - `String id`
  - `String name`

- `CompletionEvent`
  - `String userId`
  - `String courseId`
  - `Instant completedAt`

- `CourseTrend`
  - `Course course`
  - `int completionCount`

- `Timeframe` (enum)
  - `ONE_DAY`, `ONE_WEEK`, `ONE_MONTH`
  - helper `Duration` or helper method to compute start time

### Service
- `TrendingService`
  - `List<CourseTrend> getTopTrendingCourses(Timeframe timeframe, int topN, Instant now)`

### Data Structures
- `Map<String, Course>` for course lookup.
- `Map<String, Set<String>> courseIdToUniqueUsers` for counting unique users.
- Final list sort comparator:
  - count desc
  - course name asc

---

## Complexity Target
Let `E` be total completion events, `C` be trending courses in range.
- Filtering + counting: `O(E)`
- Sorting: `O(C log C)`
- Space: `O(C + U)` where `U` is total unique user-course pairs in window.

---

## Sample Input Data (for `main`)
Assume `now = 2026-05-19T12:00:00Z`

Courses:
- `C1` -> Java
- `C2` -> Python
- `C3` -> Go

Completion events:
1. (`U1`, `C1`, `2026-05-19T10:00:00Z`)
2. (`U2`, `C1`, `2026-05-19T09:00:00Z`)
3. (`U3`, `C1`, `2026-05-18T11:00:00Z`)
4. (`U1`, `C2`, `2026-05-19T11:00:00Z`)
5. (`U4`, `C2`, `2026-05-15T10:00:00Z`)
6. (`U5`, `C2`, `2026-04-29T08:00:00Z`)
7. (`U6`, `C3`, `2026-05-18T09:00:00Z`)
8. (`U6`, `C3`, `2026-05-18T09:30:00Z`)  // duplicate same user-course
9. (`U7`, `C3`, `2026-03-10T09:30:00Z`)  // outside 1 month

---

## Expected Output
For `topN = 3`:

### ONE_DAY
- Java: 3
- Python: 1
- Go: 1

### ONE_WEEK
- Java: 3
- Python: 2
- Go: 1

### ONE_MONTH
- Java: 3
- Python: 3
- Go: 1

If counts tie, ascending course name should decide order.

---

## Edge Cases to Cover
- `topN = 0` or negative
- Empty events list
- Unknown `courseId` in completion event
- Duplicate completion by same user-course
- Events exactly at boundary start timestamp
- `now` before all events

---

## Hidden Interviewer Tests (do not skip)
1. **Tie-break check**: equal counts for Java and Python -> Java first by name.
2. **De-dup check**: same user completes same course 5 times in range -> count 1.
3. **Boundary check**: event exactly at `windowStart` must be included.
4. **Invalid topN**: `topN <= 0` -> empty.
5. **Unknown courseId**: event with missing course map entry should not crash.

---

## Scoring Rubric for This Project (100)
- Correct output across all timeframes: 40
- Proper LLD and separation of concerns: 20
- Edge-case handling: 15
- Code readability and naming: 10
- Complexity explanation: 10
- Communication and assumptions: 5

---

## Submission Checklist
- [ ] Code compiles and runs.
- [ ] `main` demonstrates all 3 timeframes.
- [ ] Uses unique-user completion counting.
- [ ] Tie-breaker implemented exactly.
- [ ] Handles invalid inputs safely.
- [ ] Includes short assumptions note.

---

## What to Send Me for Review
1. Your `src` files.
2. Console output from your run.
3. 4-6 lines on assumptions + tradeoffs.

I will review in this order: critical bugs -> design issues -> improvements for interview narration.
