package cs2110;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static cs2110.DataUtilities.SearchPolicy.*;
import static cs2110.DataUtilities.DedupPolicy.*;

/**
 * Utilities for deduplicating, sorting, and searching `View` array data.
 */
public class DataUtilities {

    /* ***************************************************************************************
     * Define the record, comparators, and policy enums used to store and process View data  *
     *****************************************************************************************/

    /**
     * Models a single view by the individual with the given `userID` of the video with the given
     * `videoID` at the given `timestamp`.
     */
    record View(String userID, String videoID, LocalDateTime timestamp) { }

    /**
     * Indicates which index of `arr` should be returned during a binary search for `key` under
     * a given Comparator `cmp`:
     * <p> LEFT : Return the index `i` such that `arr[..i)` are all deemed less than `key`
     * by `cmp` and `arr[i..]` are all deemed equivalent to or greater than `key`.
     * <p> RIGHT : Return the index `i` such that `arr[..i)` are all deemed less than or
     * equivalent to `key` and `arr[i..]` are all deemed greater than `key`.
     */
    enum SearchPolicy {LEFT, RIGHT}

    /**
     * Indicates how equivalent (per the given Comparator) entries are handled during sorting:
     * <p> KEEP_ALL : Preserve all entries; the relative order of equivalent entries is preserved.
     * <p> KEEP_FIRST : Preserve only the first (in the original order) occurrence of each set of
     * equivalent entries.
     * <p> KEEP_LAST : Preserve only the last (in the original order) occurrence of each set of
     * equivalent entries.
     */
    enum DedupPolicy {KEEP_ALL, KEEP_FIRST, KEEP_LAST}

    /**
     * A Comparator object that is used to compare Views by `timestamp`.
     * The `BY_TIMESTAMP.compare()` method guarantees O(1) worst-case runtime and space complexities.
     */
    static final Comparator<View> BY_TIMESTAMP = Comparator.comparing(View::timestamp);

    /**
     * A Comparator object that is used to compare Views by `userID`.
     * The `BY_USER_ID.compare()` method guarantees O(1) worst-case runtime and space complexities.
     */
    static final Comparator<View> BY_USER_ID = Comparator.comparing(View::userID);

    /**
     * A Comparator object that is used to compare Views by `videoID`.
     * The `BY_VIDEO_ID.compare()` method guarantees O(1) worst-case runtime and space complexities.
     */
    static final Comparator<View> BY_VIDEO_ID = Comparator.comparing(View::videoID);

    /* ***************************************************************************************
     * Data processing methods                                                               *
     *****************************************************************************************/

    /**
     * Performs a binary search on the given `views` array for the given `key`. Returns the index
     * `i` with `0 <= i <= views.length` consistent with the given SearchPolicy `policy` using the
     * given Comparator `cmp`. No modifications are made to the array `views` as a result of this
     * method. Requires that `views` is sorted according to `cmp`.
     */
    static int binarySearch(View[] views, View key, Comparator<View> cmp, SearchPolicy policy) {
        return binaryHelper(views, key,  cmp, policy, 0, views.length);
        // TODO 1: Implement this method according to its specifications. Your implementation must
        //  be recursive, include no loops, and have O(log N) worst-case runtime and space
        //  complexities, where N = `views.length`. Consider delegating work to a helper method.
    }

    /**
     * Returns the maximum value in array `nums[begin..]`. Requires that
     * `0 <= begin < nums.length`.
     */
    static int binaryHelper(View[] views, View key, Comparator<View> cmp, SearchPolicy policy, int l, int r) {
        if (policy == LEFT) {
            if (l < r) {
                int mid = l + (r - l) / 2;
                if (cmp.compare(views[mid], key) >= 0) {
                    return binaryHelper(views, key, cmp, policy, l, mid);
                }
                if (cmp.compare(views[mid], key) < 0) {
                    return binaryHelper(views, key, cmp, policy, mid + 1, r);
                }
            } else {
                return l;
            }
        } else if (policy == RIGHT) {
            if (l < r) {
                int mid = l + (r - l) / 2;
                if (cmp.compare(views[mid], key) > 0) {
                    return binaryHelper(views, key, cmp, policy, l, mid);
                }
                else if (cmp.compare(views[mid], key) <= 0) {
                    return binaryHelper(views, key, cmp, policy, mid+1 , r);
                }
            } else {
                return l;
            }
        }
        return 1010101100;
    }


    /**
     * Returns a reference to a *new* array that is a copy of the range `views[begin..end)`. The
     * length of the returned array is exactly `end - begin`. No modifications are made to the
     * `views` array as a result of this method. Requires `0 <= begin <= end <= views.length`.
     * This method guarantees O(`end - begin`) worst-case runtime and space complexities.
     */
    static View[] copyOfRange(View[] views, int begin, int end) {
        return Arrays.copyOfRange(views, begin, end);
    }

    /**
     * Returns a reference to *new* array comprising the sorted (and possibly deduplicated) entries
     * of the given `views` array. No modifications are made to the `views` array as a result of
     * this method. The entries of the returned array are sorted in ascending order (according to
     * the given Comparator `cmp`) and deduplicated (according to the given DedupPolicy `policy`).
     * The length of the returned array is chosen to exactly store its contents with no trailing
     * empty entries.
     */
    static View[] deduplicatingSort(View[] views, Comparator<View> cmp,
                                    DedupPolicy policy) {
        View[] copy = copyOfRange(views,0, views.length);
        View[] work = new View[views.length];
        int x = dedupMergeSortRecursive(copy, work ,0, views.length, cmp, policy);
        return copyOfRange(copy,0,x);
        // TODO 4a: Call dedupMergeSortRecursive(), passing in a copy of the `views` array. Use its
        //  return value to obtain the return value for this method.
    }

    /**
     * Uses the merge sort algorithm to recursively sort `views[begin..end)` in ascending order
     * (according to the given Comparator `cmp`) and deduplicate these entries according to the
     * given DedupPolicy `policy`. Stores the sorted (and possibly deduplicated) data in
     * `views[begin..k)` and returns `k`. No entries of `views` outside `views[begin..end)` are
     * modified as a result of this method. Requires that `work.length > (end - begin) / 2`, and
     * `0 <= begin <= end <= views.length`. This method uses the `work` array to guarantee an
     * O(log(`end - begin`)) space complexity.
     */
    static int dedupMergeSortRecursive(View[] views, View[] work, int begin, int end,
                                       Comparator<View> cmp, DedupPolicy policy) {
        // TODO 4b: Implement recursive merge sort
        if (end-begin <= 1)
            return end-begin;

        if (policy != KEEP_ALL) {
            for (int i = begin + 1; i < end; i++) {
                if (cmp.compare(views[i - 1], views[i]) == 0) {
                    if (i == end - 1)
                        return 1;
                    continue;
                }
                break;
            }
        }


        int mid = begin + (end - begin) / 2;
        int i = dedupMergeSortRecursive(views, work, begin, mid, cmp, policy);
        int j = dedupMergeSortRecursive(views, work, mid, end, cmp, policy);
        return merge(views, work, begin, begin + i, mid, mid + j, cmp, policy);
    }

    /**
     * Merges the sorted/deduplicated ranges `views[leftBegin..leftEnd)` and
     * `views[rightBegin..rightEnd)` in ascending order (according to the given Comparator `cmp`),
     * applying the given DedupPolicy `policy`. Stores the merged (and possibly deduplicated) data
     * in `views[leftBegin..k)` and returns `k`. No entries of `views` outside `views[leftBegin..k)`
     * are modified as a result of this method. Requires:
     * <p> `work.length >= leftEnd - leftBegin`
     * <p> `0 <= leftBegin < leftEnd <= rightBegin < rightEnd <= views.length`
     * <p> `views[leftBegin..leftEnd)` is sorted/deduplicated according to given `cmp`/`policy`
     * <p> `views[rightBegin..rightEnd)` is sorted/deduplicated according to given `cmp`/policy`
     */
    @SuppressWarnings("SameParameterValue")
    static int merge(View[] views, View[] work, int leftBegin, int leftEnd,
                     int rightBegin, int rightEnd, Comparator<View> cmp, DedupPolicy policy) {
        int i = 0;
        // Copies all the elements in the left array into the work array
        for (int l = leftBegin; l < leftEnd; l++){
            work[i] = views[l];
            i++;
        }
        i = 0;
        int j = rightBegin;
        int k = leftBegin;
        int endOfArray = leftEnd+rightEnd-rightBegin;

        if(policy == KEEP_ALL){
            while(k < endOfArray){
                // If all the elements in the work array have not been used and the element in the left array
                // is less than or equal to the element in the right array or all the elements in the right array
                // have been used the method adds the element in the left array to the sorted array
                if(i != leftEnd - leftBegin && (j==rightEnd || cmp.compare(work[i],views[j]) <= 0)){
                    views[k] = work[i];
                    i++;
                }
                else {
                    views[k] = views[j];
                    j++;
                }
                k++;
            }
        }
        if(policy == KEEP_LAST){
            while(k<endOfArray){
                if(i != leftEnd - leftBegin && (j==rightEnd || cmp.compare(work[i],views[j]) <= 0)){
                    if (k-leftBegin > 0 && cmp.compare(work[i], views[k-1]) == 0) {
                        k--;
                        endOfArray--;
                    }
                    views[k] = work[i];
                    i++;
                }
                else {
                    if (k-leftBegin > 0 && cmp.compare(views[j], views[k-1]) == 0) {
                        k--;
                        endOfArray--;
                    }
                    views[k] = views[j];
                    j++;
                }
                k++;
            }

        }
        if(policy == KEEP_FIRST){
            while(k<endOfArray){
                if(i != leftEnd - leftBegin && (j==rightEnd || cmp.compare(work[i], views[j]) <= 0)){
                    if (k-leftBegin > 0 && cmp.compare(views[k-1], work[i]) == 0) {
                        i++;
                        endOfArray--;
                        continue;
                    }

                    views[k] = work[i];
                    i++;
                }
                else{
                    if(k-leftBegin > 0 && cmp.compare(views[k-1], views[j]) == 0) {
                        j++;
                        endOfArray--;
                        continue;
                    }
                    views[k] = views[j];
                    j++;
                }
                k++;
            }
        }
        return endOfArray-leftBegin;
    }

}