package basics.mastery.collections.patterns;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * FailSafeIterationDemo
 *
 * Demonstrates fail-fast vs fail-safe iteration semantics in Java collections.
 * - Shows ConcurrentModificationException with ArrayList iterator
 * - Shows safe modification with CopyOnWriteArrayList
 * - Explains performance trade-offs and when to choose each
 *
 * Additive educational example; does not modify existing logic.
 */
public class FailSafeIterationDemo {
    public static void main(String[] args) {
        System.out.println("=== Fail-Fast vs Fail-Safe Iteration ===\n");
        failFastExample();
        System.out.println();
        failSafeExample();
    }

    private static void failFastExample() {
        System.out.println("1) Fail-Fast (ArrayList)");
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        try {
            for (String s : list) {
                System.out.println("  Visiting: " + s);
                if (s.equals("B")) {
                    // [coach] Structural modification (add/remove) bumps modCount.
                    // The iterator's next() checks modCount against its expected value
                    // and throws ConcurrentModificationException on the NEXT call.
                    list.add("D"); // Triggers ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException ex) {
            System.out.println("  Caught: " + ex);
            System.out.println("  Lesson: Don't structurally modify a collection while iterating it.");
            // [coach] Alternative: use iterator.remove() for removals, or removeIf();
            // for additions, collect into a temp list and addAll after the loop.
        }
    }

    private static void failSafeExample() {
        System.out.println("2) Fail-Safe (CopyOnWriteArrayList)");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));
        for (String s : list) {
            System.out.println("  Visiting: " + s);
            if (s.equals("B")) {
                // [coach] Iterator snapshots the internal array reference at creation.
                // add() builds a new array and republishes a volatile reference;
                // the current iterator keeps iterating the old snapshot.
                list.add("D"); // Safe: iterator works on a snapshot
            }
        }
        System.out.println("  After iteration: " + list);
        System.out.println("  Trade-off: Writes are O(n) due to copy; great for read-heavy workloads.");
        // [coach] "Fail-safe" is not a formal Java term; the Javadoc calls these
        // "snapshot" iterators. Fail-safe is the colloquial/Apache-commons phrasing.
    }
}