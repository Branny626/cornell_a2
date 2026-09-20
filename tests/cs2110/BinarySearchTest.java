package cs2110;

import static cs2110.DataUtilities.*;
import static cs2110.DataUtilities.DedupPolicy.*;
import static cs2110.DataUtilities.SearchPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BinarySearchTest {

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

    @DisplayName("WHEN the userID of the `key` is alphabetically before the userIDs of all of the "
            + "`view`s, THEN `binarySearch()` with the BY_USER_ID Comparator returns 0")
    @Test
    public void testBinarySearchKeyLessThanAll() {
        View[] views = new View[]{
                new View("B", "V", LocalDateTime.now()),
                new View("C", "V", LocalDateTime.now()),
                new View("D", "V", LocalDateTime.now()),
                new View("E", "V", LocalDateTime.now()),
                new View("F", "V", LocalDateTime.now()),
                new View("G", "V", LocalDateTime.now())
        };
        View key = new View("A", "V", LocalDateTime.now());
        assertEquals(0, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(0, binarySearch(views, key, BY_USER_ID, RIGHT));
    }

    @DisplayName("WHEN multiple of the `views` records has the target videoID, THEN `binarySearch()` "
            + "with the BY_VIDEO_ID Comparator and LEFT search policy returns the index of the left most "
            + "view.")
    @Test
    public void testBinarySearchLeft() {
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
        assertEquals(0, binarySearch(views, key, BY_VIDEO_ID, LEFT));
        assertEquals(3, binarySearch(views, key, BY_TIMESTAMP, LEFT));
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
        assertEquals(2, binarySearch(views, key, BY_VIDEO_ID, RIGHT));
    }

    @DisplayName("WHEN no `views` record has the target dateTime, THEN `binarySearch()` "
            + "with the DATETIME Comparator returns the index of the right most view with"
            + "a dateTime less than or equal to the target.")
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
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, LEFT));
    }

    @DisplayName("WHEN the array is empty, THEN `binarySearch()` returns 0 with any comparatpr and"
            + " any search policy ")
    @Test
    public void testBinarySearchEmpty() {
        View[] views = new View[]{};
        View key = new View("A", "A", LocalDateTime.now());
        assertEquals(0, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(0, binarySearch(views, key, BY_USER_ID, RIGHT));
    }

}
