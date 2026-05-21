package com.lld.practice.day_02_map_set.submission;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * ============================================================
 * DAY 2 - EXERCISE 5: Trending Courses by Unique Users
 * ============================================================
 * CONTEXT:
 *   A course is trending if unique users in last 7 days are more than
 *   30% higher than unique users in previous 7 days.
 *
 * METHOD:
 *   trendingCoursesByUniqueUsers(events, now)
 *
 * INPUT:
 *   List<ViewEvent> where each event has (courseId, userId, viewedAt)
 *
 * OUTPUT:
 *   List<String> of trending courseIds in ascending order.
 *
 * WINDOW RULES:
 *   - Recent window:   (now - 7 days, now]
 *   - Previous window: (now - 14 days, now - 7 days]
 *
 * GOAL:
 *   Maintain two maps: course -> unique users in recent and previous windows,
 *   then compare counts with formula: recent > previous * 1.3
 * ============================================================
 */
public class ExcerciseFive {

    public static List<String> trendingCoursesByUniqueUsers(List<ViewEvent> events, Instant now) {
        // TODO: Implement with two Map<String, Set<String>> windows, compare unique sizes, sort output
        throw new UnsupportedOperationException("TODO: implement trendingCoursesByUniqueUsers");
    }

    public static void main(String[] args) {
        Instant now = Instant.parse("2026-05-21T00:00:00Z");

        List<ViewEvent> events = new ArrayList<>();

        // C1 recent unique users: 10 (U1..U10), previous unique users: 3 (P1..P3) -> trending
        for (int i = 1; i <= 10; i++) {
            events.add(new ViewEvent("C1", "U" + i, now.minusSeconds(i * 3600L)));
        }
        for (int i = 1; i <= 3; i++) {
            events.add(new ViewEvent("C1", "P" + i, now.minusSeconds((8L + i) * 24 * 3600)));
        }

        // C2 recent unique users: 4 (A1..A4), previous unique users: 5 (B1..B5) -> not trending
        for (int i = 1; i <= 4; i++) {
            events.add(new ViewEvent("C2", "A" + i, now.minusSeconds(i * 5400L)));
        }
        for (int i = 1; i <= 5; i++) {
            events.add(new ViewEvent("C2", "B" + i, now.minusSeconds((9L + i) * 24 * 3600)));
        }

        List<String> expected = Collections.singletonList("C1");

        System.out.println("Expected: " + expected);

        try {
            List<String> actual = trendingCoursesByUniqueUsers(events, now);
            System.out.println("Actual:   " + actual);
            System.out.println("Expected sorted order example: " + Arrays.asList("C1"));
        } catch (UnsupportedOperationException ex) {
            System.out.println("Actual:   TODO not implemented yet");
        }
    }
}

