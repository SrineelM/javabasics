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
                    list.add("D"); // Triggers ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException ex) {
            System.out.println("  Caught: " + ex);
            System.out.println("  Lesson: Don't structurally modify a collection while iterating it.");
        }
    }

    private static void failSafeExample() {
        System.out.println("2) Fail-Safe (CopyOnWriteArrayList)");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));
        for (String s : list) {
            System.out.println("  Visiting: " + s);
            if (s.equals("B")) {
                list.add("D"); // Safe: iterator works on a snapshot
            }
        }
        System.out.println("  After iteration: " + list);
        System.out.println("  Trade-off: Writes are O(n) due to copy; great for read-heavy workloads.");
    }
}
