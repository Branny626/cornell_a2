package cs2110;

import java.time.LocalDateTime;

import static cs2110.DataUtilities.*;
import static cs2110.DataUtilities.DedupPolicy.*;
import static cs2110.DataUtilities.SearchPolicy.*;

/**
 * Methods utilizing tools from `DataUtilities` to enable interesting queries on `View` array data.
 */
public class DataAnalysis {

    /**
     * Returns an array comprising the first view recorded for each video.
     */
    static View[] firstVideoViews(View[] views) {
        View[] byTime = deduplicatingSort(views, BY_TIMESTAMP, KEEP_ALL);
        return deduplicatingSort(byTime, BY_VIDEO_ID, KEEP_FIRST);
    }

    /**
     * Returns the total number of views that the video with the given `videoID` has had.
     */
    static int totalViews(View[] views, String videoID) {
        View[] byVidID = deduplicatingSort(views, BY_VIDEO_ID, KEEP_ALL);
        View key = new View(null, videoID, null); // can't search for videoID directly; stick in "dummy" View object
        int first = binarySearch(byVidID, key, BY_VIDEO_ID, LEFT);
        int last = binarySearch(byVidID, key, BY_VIDEO_ID, RIGHT);
        return last - first;
    }

    /**
     * Returns the number of distinct users who viewed at least one video at a timestamp `t` with
     * `start <= t <= end`.
     */
    @SuppressWarnings("SameParameterValue")
    static int countDistinctUsersInTimeInterval(View[] views, LocalDateTime start, LocalDateTime end) {
    // Runtime complexity of O(N)
            View[] work = copyOfRange(views,0, views.length);
    // Runtime complexity of O(NlogN)
            work = deduplicatingSort(work,BY_TIMESTAMP,KEEP_ALL);
    // Runtime complexity of O(1)
            View key = new View(null,null, end);
    // Runtime complexity of O(1)
            View key1 = new View(null,null, start );
    // Runtime complexity of O(logN)
            int i = binarySearch(work,key1,BY_TIMESTAMP,LEFT);
    // Runtime complexity of O(logN)
            int j = binarySearch(work,key,BY_TIMESTAMP,RIGHT);
    // Runtime complexity of O(N)
            View[] anotherWork = copyOfRange(work,i,j);
    // Runtime complexity of O(NlogU)
            return deduplicatingSort(anotherWork,BY_USER_ID,KEEP_FIRST).length;
        /**
         * Its overall runtime complexity is O(NlogN), because O(N) + O(NlogN) + O(logN)
         * + O(N) + O(NlogU) = O(NlogN).
         */
    }

    /**
     * Returns an array of length `k` containing the Views of the last `k` distinct videos that the
     * given `userID` has watched (in any order). If that video has been watched more than once by
     * the user, then the View corresponding to the latest watch is included. More formally (to
     * account for possible ties), this method returns an array of `k` Views such that (1) the user
     * of each View has the given `userID`, (2) the `videoID`s of these Views are distinct, and (3)
     * for each videoID `v1` in this array, if this user viewed `v2` strictly after `v1`, then a
     * view of `v2` will also be present in the array. If `userID` has viewed fewer than `k`
     * distinct videos, then a shorter array containing their latest View of each video is returned.
     */
    @SuppressWarnings("SameParameterValue")
    static View[] lastKViewedByUser(View[] views, String userID, int k) {
        // Runtime complexity of O(NlogN)
        View[] work = deduplicatingSort(views, BY_USER_ID, KEEP_ALL);
        // Runtime complexity of O(1)
        View key = new View(userID,null, null);
        // Runtime complexity of O(logN)
        int i = binarySearch(work,key,BY_USER_ID,LEFT);
        // Runtime complexity of O(logN)
        int j = binarySearch(work,key,BY_USER_ID,RIGHT);
        // Runtime complexity of O(M)
        work = copyOfRange(work,i,j);
        // Runtime complexity of O(MlogM)
        work = deduplicatingSort(work,BY_TIMESTAMP,KEEP_ALL);
        // Runtime complexity of O(MlogU)
        work = deduplicatingSort(work,BY_VIDEO_ID,KEEP_LAST);
        // Runtime complexity of O(UlogU)
        work = deduplicatingSort(work,BY_TIMESTAMP,KEEP_ALL);
        // Runtime Complexity of O(1)
        if (work.length < k){
        // Runtime Complexity of O(1)
            return work;
        }
        else {
            // Runtime complexity of O(K)
            return copyOfRange(work, work.length - k, work.length);
        }
        /**
         * Its overall runtime complexity is O(NlogN), because O(NLogN) + O(1) + O(logN)
         * + O(log N) + O(N) +  O(MlogM) + O(MlogU) + O(UlogU) + O(1) + O(1) + O(K) = O(NlogN).
         */
    }

    /**
     * Returns the `userID` of an individual who has the most recorded views of the video with
     * the given `videoID` in the `views` array. Returns `null` if there are no recorded views for
     * that video. The contents of `views` are not modified by this method.
     */
    @SuppressWarnings("SameParameterValue")
    static String mostObsessedViewer(View[] views, String videoID) {
        int workIndex = 0;
        View[] work = new View[views.length];
        for (int i = 0; i < views.length; i++){
//      Loop INV: work[..workIndex-1] contains exactly those elements of views[..i-1] whose videoID() matches videoID.
            if(views[i].videoID().compareTo(videoID) == 0){
                work[workIndex] = views[i];
                workIndex++;
            }
        }
        work = copyOfRange(work,0,workIndex);
        if (work.length == 0){
            return null;
        }
        else {
            work = deduplicatingSort(work, BY_USER_ID, KEEP_ALL);
            String max = work[0].userID();
            int count = 1;
            int maxCount =1;
            for (int j = 1; j < work.length; j++){
/**      Loop INV: Given that work[0..work.length-1] is sorted: max has the most occurrences in work[0..j-1] with maxCount views,
 *       and count is the number of consecutive occurrences of work[j-1].userID() in work[0..j-1],
 */
                if (work[j].userID().compareTo(work[j-1].userID())==0 && work[j].userID().compareTo(max)==0 ){
                    count++;
                    maxCount++;
                }
                else if (work[j].userID().compareTo(work[j-1].userID())==0){
                    count++;
                }
                else{
                    count=1;
                }
                if(count>maxCount){
                    maxCount = count;
                    max = work[j].userID();
                }
            }
            return max;
        }


        // TODO 7: Implement this method according to its specifications. Make sure to add a comment
        //  documenting the invariant of each loop that you write. Your definition must have a
        //  worst-case runtime complexity of `O(N + M log M)`, where `N = views.length` and `M` is
        //  the number of entries of `views` with the given `videoID`.
    }
}