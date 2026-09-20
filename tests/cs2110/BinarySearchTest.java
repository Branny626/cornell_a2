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
    
}
