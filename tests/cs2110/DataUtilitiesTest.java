package cs2110;

import static cs2110.DataUtilities.*;
import static cs2110.DataUtilities.DedupPolicy.*;
import static cs2110.DataUtilities.SearchPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DataUtilitiesTest {
    /* Note: These tests are meant to serve as basic correctness checks and examples
     * of how to set up unit tests for these methods. They do NOT come close to offering
     * good coverage of the `DataUtilities` class. We encourage you to do additional
     * testing to gain confidence in the correctness of your submission.
     */

    @DisplayName("WHEN one of the `views` records has the target userID, THEN `binarySearch()` "
            + "with the BY_USER_ID Comparator and LEFT search policy returns the index of that "
            + "view.")
    @Test
    public void testBinarySearchFindsUniqueMatch() {
        View[] views = new View[]{

            new View("A", "V", LocalDateTime.of(2026,1,1,0,0)),
            new View("B", "V", LocalDateTime.of(2026,1,2,0,0)),
            new View("C", "V", LocalDateTime.of(2026,1,3,0,0)),
            new View("D", "V", LocalDateTime.of(2026,1,4,0,0)),
            new View("E", "V", LocalDateTime.of(2026,1,5,0,0)),
            new View("F", "V", LocalDateTime.of(2026,1,6,0,0)),
            new View("G", "V", LocalDateTime.of(2026,1,7,0,0)),
        };
        View key = new View("C", "V", LocalDateTime.of(2026,1,8,0,0));
        assertEquals(2, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(3, binarySearch(views, key, BY_USER_ID, RIGHT));
    }

    @DisplayName("WHEN the userID of the `key` is alphabetically after the userIDs of all of the "
            + "`view`s, THEN `binarySearch()` with the BY_USER_ID Comparator returns the length "
            + "of the array.")
    @Test
    public void testBinarySearchNotPresent() {
        View[] views = new View[]{
                new View("A", "V", LocalDateTime.now()),
                new View("B", "V", LocalDateTime.now()),
                new View("C", "V", LocalDateTime.now()),
                new View("D", "V", LocalDateTime.now()),
                new View("E", "V", LocalDateTime.now()),
                new View("F", "V", LocalDateTime.now())
        };
        View key = new View("G", "V", LocalDateTime.now());
        assertEquals(6, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(6, binarySearch(views, key, BY_USER_ID, RIGHT));
    }

    @DisplayName("WHEN multiple of the `views` records has the target videoID, THEN `binarySearch()` "
            + "with the BY_VIDEO_ID Comparator and LEFT search policy returns the index of the left most "
            + "view.")
    @Test
    public void testBinarySearchLeft() {
        View[] views = new View[]{
                new View("S", "A", LocalDateTime.now()),
                new View("B", "A", LocalDateTime.now()),
                new View("C", "C", LocalDateTime.now()),
                new View("D", "D", LocalDateTime.now()),
                new View("E", "D", LocalDateTime.now()),
                new View("F", "F", LocalDateTime.now())
        };
        View key = new View("A", "A", LocalDateTime.now());
        assertEquals(0, binarySearch(views, key, BY_VIDEO_ID, LEFT));
    }

    @DisplayName("WHEN multiple of the `views` records has the target dateTime, THEN `binarySearch()` "
            + "with the DATETIME Comparator and RIGHT search policy returns the index of the right most "
            + "view.")
    @Test
    public void testBinarySearchRight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        View[] views = new View[]{
                new View("A", "A", oneDayAgo),
                new View("B", "A", oneDayAgo),
                new View("C", "C", oneDayAgo),
                new View("D", "D", now),
                new View("E", "D", now),
                new View("F", "F", now)
        };
        View key = new View("A", "A", now);
        assertEquals(6, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
    }

    @DisplayName("WHEN no `views` record has the target dateTime, THEN `binarySearch()` "
            + "with the DATETIME Comparator and RIGHT search policy returns the index of the "
            + "right most view with a dateTime less than or equal to the target.")
    @Test
    public void testBinarySearchRightNoExactMatch() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourDaysAgo = now.minusDays(4);
        LocalDateTime threeDaysAgo = now.minusDays(3);
        LocalDateTime twoDaysAgo = now.minusDays(2);
        LocalDateTime oneDayAgo = now.minusDays(1);
        View[] views = new View[]{
                new View("A", "A", fourDaysAgo),
                new View("B", "B", threeDaysAgo),
                new View("C", "C", oneDayAgo),
                new View("D", "D", now)
        };
        View key = new View("X", "X", twoDaysAgo);
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
    }


    /**
     * Asserts that `views[l..r)` is sorted according to `cmp`
     */
    @SuppressWarnings("SameParameterValue")
    void assertSorted(View[] views, int l, int r, Comparator<View> cmp) {
        for (int i = l; i < r - 1; i++) {
            assertTrue(cmp.compare(views[i], views[i + 1]) <= 0);
        }
    }

    @DisplayName("WHEN we merge on timestamps using the KEEP_ALL deduplication policy AND the "
            + "records are interleaved between the subarrays and have unique timestamps, THEN the "
            + "merged subarray is correctly sorted.")
    @Test
    void testMergeInterleavedUnique() {
        View[] views = new View[]{
                new View("A", "V", LocalDateTime.of(2025, 1, 1, 0, 0)),
                new View("B", "V", LocalDateTime.of(2025, 1, 4, 0, 0)),
                new View("C", "V", LocalDateTime.of(2025, 1, 6, 0, 0)),
                new View("D", "V", LocalDateTime.of(2025, 1, 2, 0, 0)),
                new View("E", "V", LocalDateTime.of(2025, 1, 3, 0, 0)),
                new View("F", "V", LocalDateTime.of(2025, 1, 5, 0, 0)),
                new View("G", "V", LocalDateTime.of(2025, 1, 7, 0, 0)),
        };
        View[] work = new View[3];
        merge(views, work, 0, 3, 4, 7, BY_TIMESTAMP, KEEP_ALL);
        assertSorted(views, 0, 6, BY_TIMESTAMP);
//        assertEquals(7, result);
    }


    @Test
    void testMatching() {
        View[] views = new View[]{
                new View("A", "V", LocalDateTime.of(2025, 1, 1, 0, 0)),
                new View("B", "V", LocalDateTime.of(2025, 1, 3, 0, 0)),
                new View("C", "V", LocalDateTime.of(2025, 1, 6, 0, 0)),
                new View("D", "V", LocalDateTime.of(2025, 1, 1, 0, 0)),
                new View("E", "V", LocalDateTime.of(2025, 1, 3, 0, 0)),
                new View("F", "V", LocalDateTime.of(2025, 1, 5, 0, 0)),
                new View("G", "V", LocalDateTime.of(2025, 1, 7, 0, 0)),
        };
        View[] work = new View[3];
        merge(views, work, 0, 3, 3, 7, BY_TIMESTAMP, KEEP_ALL);
        int result = merge(views, work, 0, 3, 3, 7, BY_TIMESTAMP, KEEP_ALL);
        assertEquals(5, result);
        assertSorted(views, 0, 5, BY_TIMESTAMP);
    }

    @DisplayName("WHEN we call `deduplicatingSort()` with KEEP_FIRST on two equivalent and one "
            + "distinct records, THEN the output contains the correct two elements in the correct "
            + "order.")
    @Test
    void testSortThreeEquivalentPairKeepFirst() {
        View[] views = new View[]{
                new View("A", "V", LocalDateTime.of(2026,1,2,0,0)),
                new View("B", "V", LocalDateTime.of(2026,1,1,0,0)),
                new View("C", "V", LocalDateTime.of(2026,1,1,0,0)),
        };
        View[] sorted = deduplicatingSort(views, BY_TIMESTAMP, KEEP_FIRST);
        assertEquals(2, sorted.length);
        assertEquals("B", sorted[0].userID());
        assertEquals("A", sorted[1].userID());
    }
}
