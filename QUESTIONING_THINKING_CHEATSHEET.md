# Questioning & Thinking Framework (Cheatsheet)

## Quick Summary
**Why it matters:** In a 45-minute interview, the first 5 minutes (questioning) = 30% of your score. You failed at Walmart because you asked ZERO questions and coded wrong assumptions.

**Your fix:** Build a questioning habit. Practice daily. It becomes automatic.

---

## The 5 Critical Questions Framework (APPLY EVERY TIME)

Use these 5 buckets to clarify ANY machine-coding problem in 2 minutes:

### 1. **WHAT** → Inputs & Outputs
- What are the exact inputs (format, types)?
- What is the exact output format?
- Example: Input = `List<CompletionEvent>`, Output = `List<CourseTrend>`

### 2. **CONSTRAINTS** → Limits & Boundaries
- Size limits? (1 event or 1 million?)
- Time windows? (1 second, 1 day, or unbounded?)
- Boundary rules? (Inclusive or exclusive?)
- Example: Timeframe boundaries inclusive (`>= windowStart && <= now`)?

### 3. **RANKING/SORTING** → How to Prioritize
- How do I rank results?
- By count? By recency? By rating?
- Example: Count unique users per course?

### 4. **TIES** → What Happens if Equal
- If two items have same score, how to break tie?
- Alphabetically? By timestamp? By ID?
- Example: Java and Python both have 3 completions → rank by course name A-Z?

### 5. **INVALIDS** → Edge Cases
- What to do for invalid inputs? (null, empty, negative, zero)
- Skip them? Return empty? Crash?
- Example: If `topN = 0` → return empty list?

---

## Pre-Coding Checklist (Do This EVERY Time)

Before you touch keyboard:

- [ ] **Read problem 3 times slowly** (2 min)
- [ ] **Answer the 5 Critical Questions** (using problem statement) (2 min)
- [ ] **List 5 edge cases to handle** (1 min)
- [ ] **Sketch class diagram** (not code, boxes + arrows) (2 min)
- [ ] **Write pseudocode** (1-2 min)
- [ ] **Then start coding** (remaining time)

**Total pre-coding time: 8-10 minutes.** This saves 20 minutes of rework.

---

## Drill 1: "What Am I Assuming?" (Daily, 2 min)

For any problem you see, write 5 assumptions within 2 minutes.

**Example (P1):**
1. Unique user count per course (same user = 1, not multiple)
2. Boundary inclusive (`>= start && <= end`)
3. topN = 0 returns empty
4. Only courses with >= 1 completion shown
5. Unknown courseId events are skipped

**Your turn:** Do this for EVERY problem before coding.

---

## Drill 2: "The 5 Critical Questions" (Every Project, 5 min)

Write these down FOR REAL (not in your head):

```
1. WHAT?
   Input: 
   Output: 

2. CONSTRAINTS?
   Size limits: 
   Time limits: 
   Boundaries: 

3. RANKING/SORTING?
   Primary: 
   Secondary: 

4. TIES?
   If equal: 

5. INVALIDS?
   null input: 
   empty input: 
   negative/zero: 
```

**For P1:**
```
1. WHAT?
   Input: List<CompletionEvent> + Timeframe + topN + now
   Output: List<CourseTrend> (Course + completion count)

2. CONSTRAINTS?
   Timeframe: ONE_DAY, ONE_WEEK, ONE_MONTH
   Boundaries: Inclusive (>= start && <= now)

3. RANKING/SORTING?
   Primary: Completion count DESC
   Secondary: Course name ASC

4. TIES?
   If Java & Python both have 3: rank by name (Java < Python alphabetically)

5. INVALIDS?
   topN <= 0: return empty list
   Unknown courseId: skip event
   Empty events: return empty list
```

---

## Drill 3: "Ask Out Loud" (3 min, 2x per week)

Record yourself asking clarifying questions ON AUDIO for 3 minutes. Listen back.

**Bad delivery:**
- "Um... what's... trending?"
- "Do I like... handle... errors?"

**Good delivery:**
- "So a completion event is recorded when a user finishes a course. When I count 'completion', does the same user completing the same course twice count as one unique user or two?"
- "For tie-breaking, if Java and Python both have 3 completions, I rank Java first because it comes before Python alphabetically, correct?"

**Why it matters:** Tone + clarity = confidence in interviews.

---

## Drill 4: "Read 3 Times" (2 min per read)

Every problem, read like this:

**Read 1 (Fast):** Get the gist in 30 seconds.

**Read 2 (Slow):** Highlight every constraint, assumption, edge case with a pen/marker.
- Look for keywords: "unique", "multiple times", "inclusive", "invalid", "zero", "empty"

**Read 3 (Critical):** Find contradictions or unclear phrasing.
- Re-read highlighted parts.
- Ask yourself: "Is there any ambiguity here?"

**P1 Example (Read 2 highlights):**
- "**unique users** completed the course" ← means count once per user
- "same user completes same course **multiple times**, count **once**" ← confirms dedup
- "boundary is **inclusive**" ← exact wording
- "**topN <= 0** returns empty" ← handle invalid

---

## Drill 5: "Practice Flow Every Time" (Discipline)

This is your MANDATORY flow for every project:

```
Step 1: Read problem 3 times          (2 min)
Step 2: Answer 5 Critical Questions   (2 min)
Step 3: Draw design (no code)         (3 min)
Step 4: Write pseudocode              (2 min)
Step 5: Start coding                  (30-35 min)
Step 6: Test & edge cases             (5-10 min)
```

**Total: 45 minutes exactly.**

DO NOT SKIP STEPS 1-4.

---

## Why You're Bad at Questioning (Real Talk)

Check which habit YOU have:

- [ ] **Habit 1:** Don't read carefully → miss clues in problem
- [ ] **Habit 2:** Assume you understand → don't ask for clarity
- [ ] **Habit 3:** Rush to code → think coding proves intelligence (it doesn't)
- [ ] **Habit 4:** Panic under time → skip thinking, start typing
- [ ] **Habit 5:** Never asked questions in school → uncomfortable asking

**Fix:** Intentionally spend 10 minutes thinking before typing. It feels slow at first. After 3-4 projects, it's automatic.

---

## Weekly Habit Tracking

Add to your project tracker:

| Project | Attempt | Questions Asked (Y/N) | 5 Assumptions Written (Y/N) | Design Sketch Done (Y/N) | Score /100 |
|---------|---------|----------------------|----------------------------|-----------------------|-----------|
| P1      | 1       |                      |                            |                       |           |

**Target:** 100% "Y" across all three columns = better scores.

---

## Quick Reference: Common Patterns

### Pattern 1: Ranking Problem
Always ask:
- Primary sort key?
- Secondary sort key (tie-break)?
- Sort ascending or descending?

### Pattern 2: Counting/Aggregation
Always ask:
- Count unique or total?
- Deduplication rule?
- Missing/invalid values?

### Pattern 3: Filtering by Time
Always ask:
- Timeframe boundaries inclusive or exclusive?
- Relative to what timestamp?
- Handle "before all events" case?

### Pattern 4: Top N Results
Always ask:
- If N > available items, return all or empty?
- If N <= 0, return what?

---

## For Each Problem (Copy This Section)

**Problem:** _______________

**1. WHAT (Input/Output)?**
   Input: 
   Output: 

**2. CONSTRAINTS (Limits/Boundaries)?**
   Size: 
   Time: 
   Boundaries: 

**3. RANKING/SORTING?**
   Primary: 
   Secondary: 

**4. TIES?**
   Resolution: 

**5. INVALIDS?**
   Null: 
   Empty: 
   Invalid: 

**5 Key Assumptions:**
   1. 
   2. 
   3. 
   4. 
   5. 

**Design (pseudocode):**
   ```
   function solve(...):
      step 1:
      step 2:
      step 3:
      ...
   ```

---

## Interview Day (Apply This)

**First 5 minutes:**
- Read problem (1 min)
- Ask 5 clarifying questions clearly (2-3 min)
- Listen to answers
- Confirm assumptions (1 min)

**Next 7 minutes:**
- Sketch design (3-4 min)
- Walk interviewer through it (3-4 min)

**Then code** (remaining time safely)

This approach shows:
1. You think before coding ✓
2. You're not afraid to ask for clarity ✓
3. You align with requirements ✓
4. You're communicative ✓

---

## Mentor Accountability

Add this row to your tracker EVERY project:

| Project | Attempt | Questions Score (0-5) | Notes |
|---------|---------|----------------------|-------|
| P1      | 1       | /5                   |       |

**Scoring:**
- 5 = Asked 5+ relevant questions
- 4 = Asked 3-4 questions
- 3 = Asked 1-2 questions
- 0 = Asked zero questions

Target: Average >= 4 across 10 projects.

---

## Red Flags (You're Regressing)

If ANY of these happen, **STOP immediately**:

- [ ] You start coding before writing assumptions
- [ ] You skip the 5 Critical Questions
- [ ] You don't read the problem statement fully
- [ ] You're typing in minute 2 (should be thinking/sketching)
- [ ] You realize midway through you misunderstood the problem

When this happens: **PAUSE, read again, reset.** This is a learning moment, not failure.

---

## Daily Practice (Just 10 min)

Every morning, pick ANY problem (LeetCode, your project spec, whiteboard prompt) and:

1. Read it 3 times (2 min)
2. Write down the 5 Critical Questions (2 min)
3. Sketch the design (3 min)
4. Do NOT code

This trains your brain to think first, code second.

---

**Print this. Refer to it before EVERY project. After 6 weeks, asking questions will be your instinct.**

