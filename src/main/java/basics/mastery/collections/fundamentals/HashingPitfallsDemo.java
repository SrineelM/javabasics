package basics.mastery.collections.fundamentals;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * HashingPitfallsDemo
 *
 * Demonstrates common pitfalls when using objects as keys in hash-based collections:
 * - Mutable keys break HashMap/HashSet semantics
 * - equals/hashCode contract
 * - Comparator consistency with equals for sorted structures
 *
 * Additive demo; does not modify existing logic.
 */
public class HashingPitfallsDemo {
    static class Person {
        String name; // mutable field used in equals/hashCode
        Person(String name) { this.name = name; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person p)) return false;
            return Objects.equals(name, p.name);
        }
        @Override public int hashCode() { return Objects.hash(name); }
        @Override public String toString() { return "Person(" + name + ")"; }
    }

    public static void main(String[] args) {
        System.out.println("=== Hashing Pitfalls Demo ===\n");
        mutableKeyPitfall();
        comparatorConsistency();
    }

    private static void mutableKeyPitfall() {
        System.out.println("1) Mutable key in HashSet/HashMap");
        Set<Person> set = new HashSet<>();
        Person p = new Person("Alice");
        set.add(p);
        System.out.println("  Contains Alice? " + set.contains(p));
        // Mutate key after insertion
        p.name = "Bob";
        System.out.println("  After mutation, contains original? " + set.contains(p)); // often false
        System.out.println("  Set: " + set);
        System.out.println("  Lesson: Keys should be immutable or fields used in equals/hashCode must not change while in a set/map.\n");
    }

    private static void comparatorConsistency() {
        System.out.println("2) Comparator consistency with equals");
        Comparator<Person> cmpByLen = Comparator.comparingInt(a -> a.name.length());
        TreeSet<Person> sorted = new TreeSet<>(cmpByLen);
        sorted.addAll(List.of(new Person("Al"), new Person("Bo"), new Person("Cat")));
        System.out.println("  Sorted by length: " + sorted);
        System.out.println("  Different objects with same length are considered equal by comparator => may be dropped");
        System.out.println("  Lesson: For sorted sets/maps, comparator must be consistent with equals to avoid surprises.\n");
    }
}
