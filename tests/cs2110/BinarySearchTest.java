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

    @DisplayName("WHEN one of the `views` records has the target attribute, THEN `binarySearch()` "
            + "with any Comparator and any search policy returns the correct index")
    @Test
    public void testBinarySearchFindsKey() {
        View[] views = new View[]{

                new View("B", "E", LocalDateTime.of(2026,1,1,0,0)),
                new View("B", "G", LocalDateTime.of(2026,2,3,0,0)),
                new View("D", "O", LocalDateTime.of(2026,2,3,0,0)),
                new View("F", "O", LocalDateTime.of(2026,4,4,0,0)),
                new View("H", "Q", LocalDateTime.of(2026,6,5,0,0)),
                new View("I", "S", LocalDateTime.of(2026,7,6,0,0)),
                new View("P", "V", LocalDateTime.of(2026,8,7,0,0)),
                new View("S", "X", LocalDateTime.of(2026,8,9,0,0)),
                new View("T", "Z", LocalDateTime.of(2026,8,19,0,0)),
        };
        View key = new View("I", "Q", LocalDateTime.of(2026,4,4,0,0));
        assertEquals(5, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(3, binarySearch(views, key, BY_TIMESTAMP, LEFT));
        assertEquals(4, binarySearch(views, key, BY_VIDEO_ID, LEFT));
        assertEquals(6, binarySearch(views, key, BY_USER_ID, RIGHT));
        assertEquals(4, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(5, binarySearch(views, key, BY_VIDEO_ID, RIGHT));
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

    @DisplayName("WHEN the attributes of the `key` is alphabetically or numerically after the attributes of "
            + "all of the `view`s, THEN `binarySearch()` with any Comparator returns the length "
            + "of the array.")
    @Test
    public void testBinarySearchNotPresentAllComparators() {
        View[] views = new View[]{
                new View("A", "A", LocalDateTime.of(2026,1,1,0,0)),
                new View("B", "B", LocalDateTime.of(2026,1,2,0,0)),
                new View("C", "B", LocalDateTime.of(2026,1,3,0,0)),
                new View("D", "D", LocalDateTime.of(2026,1,4,0,0)),
                new View("E", "E", LocalDateTime.of(2026,1,5,0,0)),
                new View("F", "F", LocalDateTime.of(2026,1,6,0,0)),
                new View("G", "G", LocalDateTime.of(2026,1,7,0,0)),
                new View("G", "G", LocalDateTime.of(2026,1,7,0,0)),
                new View("G", "H", LocalDateTime.of(2026,1,7,0,0)),
                new View("G", "I", LocalDateTime.of(2026,1,7,0,0)),
        };
        View key = new View("Z", "Z", LocalDateTime.of(2026,2,7,0,0));
        assertEquals(10, binarySearch(views, key, BY_TIMESTAMP, LEFT));
        assertEquals(10, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(10, binarySearch(views, key, BY_VIDEO_ID, LEFT));
        assertEquals(10, binarySearch(views, key, BY_VIDEO_ID, RIGHT));
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
            + "with all Comparators and LEFT search policy returns the index of the left most "
            + "view equal to the key.")
    @Test
    public void testBinarySearchLeft() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        View[] views = new View[]{
                new View("A", "A", oneDayAgo),
                new View("A", "A", oneDayAgo),
                new View("C", "C", oneDayAgo),
                new View("D", "D", now),
                new View("E", "D", now),
                new View("F", "F", now)
        };
        View key = new View("A", "A", now);
        assertEquals(0, binarySearch(views, key, BY_VIDEO_ID, LEFT));
        assertEquals(0, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(3, binarySearch(views, key, BY_TIMESTAMP, LEFT));
    }
    @DisplayName("WHEN multiple of the `views` records have the target, THEN `binarySearch()` "
            + "with all Comparators and RIGHT search policy returns the index of the left most "
            + "view greater than the key.")
    @Test
    public void testBinarySearchRight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        View[] views = new View[]{
                new View("A", "A", oneDayAgo),
                new View("B", "A", oneDayAgo),
                new View("C", "C", oneDayAgo),
                new View("D", "D", now),
                new View("D", "D", now),
                new View("F", "F", now)
        };
        View key = new View("D", "A", now);
        assertEquals(5, binarySearch(views, key, BY_USER_ID, RIGHT));
        assertEquals(6, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(2, binarySearch(views, key, BY_VIDEO_ID, RIGHT));
    }

    @DisplayName("WHEN no `views` record contains the key, THEN `binarySearch()` "
            + "with any Comparator returns the index of the left most view with"
            + "a dateTime greater than  the target.")
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
                new View("D", "G", oneDayAgo),
                new View("E", "H", now)
        };
        View key = new View("C", "C", twoDaysAgo);
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, LEFT));
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, LEFT));
        assertEquals(2, binarySearch(views, key, BY_TIMESTAMP, LEFT));
    }

    @DisplayName("WHEN the array is empty, THEN `binarySearch()` returns 0 with any comparatpr and"
            + " any search policy ")
    @Test
    public void testBinarySearchEmpty() {
        View[] views = new View[]{};
        View key = new View("A", "A", LocalDateTime.now());
        assertEquals(0, binarySearch(views, key, BY_USER_ID, LEFT));
        assertEquals(0, binarySearch(views, key, BY_TIMESTAMP, LEFT));
        assertEquals(0, binarySearch(views, key, BY_VIDEO_ID, LEFT));
        assertEquals(0, binarySearch(views, key, BY_USER_ID, RIGHT));
        assertEquals(0, binarySearch(views, key, BY_TIMESTAMP, RIGHT));
        assertEquals(0, binarySearch(views, key, BY_VIDEO_ID, RIGHT));
    }

}
