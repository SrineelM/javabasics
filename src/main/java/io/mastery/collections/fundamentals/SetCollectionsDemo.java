package io.mastery.collections.fundamentals;

import io.mastery.collections.CollectionDemo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of Set collection implementations.
 * 
 * This class provides interactive examples covering:
 * - HashSet: Fast hash-based set implementation
 * - LinkedHashSet: Hash set with insertion order preservation
 * - TreeSet: Sorted set implementation using Red-Black tree
 * - EnumSet: Specialized set for enum types
 * - ConcurrentHashMap.newKeySet(): Thread-safe set implementation
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class SetCollectionsDemo extends CollectionDemo {
    
    @Override
    public void demonstrateAll() {
        printHeader("SET COLLECTIONS COMPREHENSIVE GUIDE");
        
        // Core Set implementations
        demonstrateHashSet();
        demonstrateLinkedHashSet();
        demonstrateTreeSet();
        demonstrateEnumSet();
        
        // Performance analysis
        performanceComparison();
        
        // Advanced operations
        demonstrateSetOperations();
        demonstrateThreadSafeOperations();
        demonstrateModernFeatures();
        
        // Best practices
        demonstrateBestPractices();
        
        printSectionComplete("Set Collections");
    }
    
    /**
     * Demonstrates HashSet - the most commonly used Set implementation
     */
    private void demonstrateHashSet() {
        printSubHeader("HashSet - Hash-Based Set Implementation");
        
        printInfo("HashSet provides O(1) average time complexity for basic operations");
        printInfo("Best for: Fast lookups, no ordering requirements");
        
        // Basic operations
        Set<String> programmingLanguages = new HashSet<>();
        programmingLanguages.add("Java");
        programmingLanguages.add("Python");
        programmingLanguages.add("JavaScript");
        programmingLanguages.add("C++");
        programmingLanguages.add("Java"); // Duplicate - will be ignored
        
        printResult("HashSet contents", programmingLanguages);
        printResult("Size (duplicates ignored)", programmingLanguages.size());
        printResult("Contains 'Python'", programmingLanguages.contains("Python"));
        printResult("Contains 'Ruby'", programmingLanguages.contains("Ruby"));
        
        // Bulk operations
        Set<String> modernLanguages = Set.of("Kotlin", "Swift", "Rust", "Go");
        programmingLanguages.addAll(modernLanguages);
        printResult("After adding modern languages", programmingLanguages);
        
        // Removal operations
        programmingLanguages.remove("C++");
        programmingLanguages.removeIf(lang -> lang.length() > 10);
        printResult("After removals", programmingLanguages);
        
        printInfo("HashSet does not guarantee any specific iteration order");
        printWarning("Elements must have proper hashCode() and equals() implementations");
    }
    
    /**
     * Demonstrates LinkedHashSet - hash set with insertion order
     */
    private void demonstrateLinkedHashSet() {
        printSubHeader("LinkedHashSet - Hash Set with Insertion Order");
        
        printInfo("LinkedHashSet maintains insertion order while providing HashSet performance");
        printInfo("Best for: When you need Set semantics with predictable iteration order");
        
        Set<String> orderedLanguages = new LinkedHashSet<>();
        orderedLanguages.add("First");
        orderedLanguages.add("Second");
        orderedLanguages.add("Third");
        orderedLanguages.add("Second"); // Duplicate
        orderedLanguages.add("Fourth");
        
        printResult("LinkedHashSet (insertion order)", orderedLanguages);
        
        // Compare with HashSet
        Set<String> unorderedLanguages = new HashSet<>(orderedLanguages);
        printResult("Same elements in HashSet", unorderedLanguages);
        
        printInfo("Iteration through LinkedHashSet:");
        int index = 1;
        for (String lang : orderedLanguages) {
            System.out.println("  " + index++ + ". " + lang);
        }
        
        printTip("Use LinkedHashSet when you need Set behavior with predictable ordering");
        printInfo("Slightly higher memory overhead than HashSet due to linked list maintenance");
    }
    
    /**
     * Demonstrates TreeSet - sorted set implementation
     */
    private void demonstrateTreeSet() {
        printSubHeader("TreeSet - Sorted Set Implementation");
        
        printInfo("TreeSet maintains elements in sorted order using Red-Black tree");
        printInfo("Best for: When you need sorted set with O(log n) operations");
        
        // Basic TreeSet with natural ordering
        Set<Integer> numbers = new TreeSet<>();
        numbers.addAll(List.of(5, 2, 8, 1, 9, 3, 7, 4, 6));
        printResult("TreeSet (natural order)", numbers);
        
        // String TreeSet
        Set<String> words = new TreeSet<>();
        words.addAll(List.of("zebra", "apple", "banana", "cherry", "date"));
        printResult("String TreeSet (alphabetical)", words);
        
        // Custom comparator
        Set<String> lengthSorted = new TreeSet<>(Comparator.comparing(String::length));
        lengthSorted.addAll(List.of("a", "abc", "ab", "abcd", "abcde"));
        printResult("TreeSet by length", lengthSorted);
        
        // TreeSet specific methods
        TreeSet<Integer> treeSet = new TreeSet<>(numbers);
        printResult("First element", treeSet.first());
        printResult("Last element", treeSet.last());
        printResult("Elements < 5", treeSet.headSet(5));
        printResult("Elements >= 5", treeSet.tailSet(5));
        printResult("Elements between 3 and 7", treeSet.subSet(3, 8));
        
        printTip("TreeSet elements must be Comparable or provide a Comparator");
        printWarning("O(log n) performance vs O(1) for HashSet");
    }
    
    /**
     * Demonstrates EnumSet - specialized set for enum types
     */
    private void demonstrateEnumSet() {
        printSubHeader("EnumSet - Specialized Set for Enums");
        
        // Define enum for demonstration
        enum DaysOfWeek {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
        }
        
        printInfo("EnumSet is highly optimized for enum types using bit vectors");
        printInfo("Best for: Working with enum types, extremely memory efficient");
        
        // Different ways to create EnumSets
        Set<DaysOfWeek> weekdays = EnumSet.of(
            DaysOfWeek.MONDAY, DaysOfWeek.TUESDAY, DaysOfWeek.WEDNESDAY,
            DaysOfWeek.THURSDAY, DaysOfWeek.FRIDAY
        );
        printResult("Weekdays", weekdays);
        
        Set<DaysOfWeek> weekend = EnumSet.of(DaysOfWeek.SATURDAY, DaysOfWeek.SUNDAY);
        printResult("Weekend", weekend);
        
        Set<DaysOfWeek> allDays = EnumSet.allOf(DaysOfWeek.class);
        printResult("All days", allDays);
        
        Set<DaysOfWeek> noDays = EnumSet.noneOf(DaysOfWeek.class);
        printResult("No days", noDays);
        
        Set<DaysOfWeek> range = EnumSet.range(DaysOfWeek.TUESDAY, DaysOfWeek.THURSDAY);
        printResult("Tuesday to Thursday", range);
        
        // EnumSet operations
        Set<DaysOfWeek> workingDays = EnumSet.copyOf(weekdays);
        workingDays.remove(DaysOfWeek.FRIDAY); // Half day or holiday
        printResult("Working days (no Friday)", workingDays);
        
        printTip("Always use EnumSet instead of HashSet for enum types");
        printInfo("EnumSet operations are extremely fast and memory efficient");
    }
    
    /**
     * Performance comparison between Set implementations
     */
    private void performanceComparison() {
        printSubHeader("Performance Comparison Analysis");
        
        final int elements = 10000;
        printInfo("Comparing Set implementations with " + formatNumber(elements) + " elements");
        
        // Prepare test data
        List<Integer> testData = IntStream.range(0, elements).boxed().toList();
        
        // HashSet performance
        double hashSetTime = measureTime(() -> {
            Set<Integer> set = new HashSet<>();
            testData.forEach(set::add);
        });
        
        // LinkedHashSet performance  
        double linkedHashSetTime = measureTime(() -> {
            Set<Integer> set = new LinkedHashSet<>();
            testData.forEach(set::add);
        });
        
        // TreeSet performance
        double treeSetTime = measureTime(() -> {
            Set<Integer> set = new TreeSet<>();
            testData.forEach(set::add);
        });
        
        printBenchmark("HashSet insertion", hashSetTime);
        printBenchmark("LinkedHashSet insertion", linkedHashSetTime);
        printBenchmark("TreeSet insertion", treeSetTime);
        
        // Lookup performance
        Set<Integer> hashSet = new HashSet<>(testData);
        Set<Integer> treeSet = new TreeSet<>(testData);
        
        final int lookups = 1000;
        double hashLookupTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < lookups; i++) {
                hashSet.contains(rnd.nextInt(elements));
            }
        });
        
        double treeLookupTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < lookups; i++) {
                treeSet.contains(rnd.nextInt(elements));
            }
        });
        
        printComparison("Lookup operations (" + lookups + " ops)",
                       treeLookupTime, hashLookupTime,
                       "TreeSet", "HashSet");
    }
    
    /**
     * Demonstrates set mathematical operations
     */
    private void demonstrateSetOperations() {
        printSubHeader("Set Mathematical Operations");
        
        Set<Integer> setA = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> setB = new HashSet<>(Set.of(4, 5, 6, 7, 8));
        
        printResult("Set A", setA);
        printResult("Set B", setB);
        
        // Union (A ∪ B)
        Set<Integer> union = new HashSet<>(setA);
        union.addAll(setB);
        printResult("Union (A ∪ B)", union);
        
        // Intersection (A ∩ B)
        Set<Integer> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);
        printResult("Intersection (A ∩ B)", intersection);
        
        // Difference (A - B)
        Set<Integer> difference = new HashSet<>(setA);
        difference.removeAll(setB);
        printResult("Difference (A - B)", difference);
        
        // Symmetric difference ((A ∪ B) - (A ∩ B))
        Set<Integer> symmetricDiff = new HashSet<>(union);
        symmetricDiff.removeAll(new HashSet<>(setA) {{ retainAll(setB); }});
        printResult("Symmetric Difference", symmetricDiff);
        
        // Subset and superset checks
        Set<Integer> subset = Set.of(1, 2, 3);
        printResult("Is {1,2,3} subset of A", setA.containsAll(subset));
        printResult("Is A superset of {1,2,3}", setA.containsAll(subset));
        
        printTip("Use these operations for filtering, data analysis, and mathematical computations");
    }
    
    /**
     * Demonstrates thread-safe Set operations
     */
    private void demonstrateThreadSafeOperations() {
        printSubHeader("Thread-Safe Set Operations");
        
        // ConcurrentHashMap.newKeySet() - modern thread-safe set
        printInfo("ConcurrentHashMap.newKeySet() - Modern thread-safe Set");
        Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
        concurrentSet.addAll(Set.of("Thread", "Safe", "Set", "Implementation"));
        printResult("Concurrent Set", concurrentSet);
        
        // Synchronized Set wrapper
        printInfo("Collections.synchronizedSet() - Wrapper approach");
        Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
        syncSet.addAll(Set.of("Synchronized", "Set", "Wrapper"));
        
        // Safe iteration requires synchronization
        synchronized (syncSet) {
            printResult("Synchronized Set", syncSet);
        }
        
        // Immutable Sets (Java 9+)
        Set<String> immutableSet = Set.of("Immutable", "Set", "Java", "9+");
        printResult("Immutable Set", immutableSet);
        
        try {
            immutableSet.add("This will fail");
        } catch (UnsupportedOperationException e) {
            printInfo("Immutable sets throw UnsupportedOperationException on modification");
        }
        
        printTip("Use ConcurrentHashMap.newKeySet() for thread-safe Set operations");
        printWarning("Synchronized wrappers require manual synchronization for iteration");
    }
    
    /**
     * Demonstrates modern Java features with Sets
     */
    private void demonstrateModernFeatures() {
        printSubHeader("Modern Java Features with Sets");
        
        // Using Sets with Records
        record Technology(String name, String category, int yearIntroduced) {}
        
        Set<Technology> technologies = Set.of(
            new Technology("Java", "Programming Language", 1995),
            new Technology("Spring", "Framework", 2003),
            new Technology("Docker", "Containerization", 2013),
            new Technology("Kubernetes", "Orchestration", 2014)
        );
        
        printResult("Technologies Set", technologies);
        
        // Collectors.toSet() with Streams
        Set<String> categories = technologies.stream()
                .map(Technology::category)
                .collect(Collectors.toSet());
        printResult("Unique Categories", categories);
        
        // Modern technologies (after 2010)
        Set<Technology> modernTech = technologies.stream()
                .filter(tech -> tech.yearIntroduced() > 2010)
                .collect(Collectors.toSet());
        printResult("Modern Technologies (after 2010)", modernTech);
        
        // Using var with type inference (Java 10+)
        var languageSet = Set.of("Java", "Kotlin", "Scala", "Groovy");
        printResult("JVM Languages (using var)", languageSet);
        
        printTip("Use Records with Sets for immutable data structures");
        printInfo("Stream operations with Sets maintain uniqueness automatically");
    }
    
    /**
     * Demonstrates best practices and common patterns
     */
    private void demonstrateBestPractices() {
        printSubHeader("Best Practices and Common Patterns");
        
        printInfo("🎯 Set Selection Guidelines:");
        System.out.println("  • HashSet: Default choice for most use cases");
        System.out.println("  • LinkedHashSet: When insertion order matters");
        System.out.println("  • TreeSet: When sorted order is required");
        System.out.println("  • EnumSet: Always use for enum types");
        System.out.println("  • ConcurrentHashMap.newKeySet(): Thread-safe scenarios");
        
        printSeparator();
        
        printInfo("⚡ Performance Optimization Tips:");
        System.out.println("  • Pre-size HashSet if you know approximate capacity");
        System.out.println("  • Use EnumSet for enum types (extremely efficient)");
        System.out.println("  • Consider TreeSet only when sorting is essential");
        System.out.println("  • Use Set.of() for small immutable sets");
        
        printSeparator();
        
        printInfo("🚨 Common Pitfalls to Avoid:");
        System.out.println("  • Mutable objects as Set elements without proper equals/hashCode");
        System.out.println("  • Modifying objects after adding them to HashSet/LinkedHashSet");
        System.out.println("  • Using TreeSet with non-comparable objects without Comparator");
        System.out.println("  • Forgetting that Set.of() creates immutable sets");
        
        // Demonstrate equals/hashCode importance
        printInfo("Importance of proper equals/hashCode implementation:");
        
        class BadPerson {
            String name;
            BadPerson(String name) { this.name = name; }
            @Override
            public String toString() { return name; }
            // Missing equals() and hashCode() - uses Object's implementation
        }
        
        class GoodPerson {
            final String name;
            GoodPerson(String name) { this.name = name; }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof GoodPerson other)) return false;
                return Objects.equals(name, other.name);
            }
            
            @Override
            public int hashCode() {
                return Objects.hash(name);
            }
            
            @Override
            public String toString() { return name; }
        }
        
        Set<BadPerson> badSet = new HashSet<>();
        badSet.add(new BadPerson("John"));
        badSet.add(new BadPerson("John")); // Duplicate!
        printResult("Bad Set (no proper equals/hashCode)", badSet.size() + " elements: " + badSet);
        
        Set<GoodPerson> goodSet = new HashSet<>();
        goodSet.add(new GoodPerson("John"));
        goodSet.add(new GoodPerson("John")); // Properly detected as duplicate
        printResult("Good Set (proper equals/hashCode)", goodSet.size() + " elements: " + goodSet);
        
        printTip("Always implement equals() and hashCode() for objects used in Sets");
        printWarning("Use Records for automatic equals/hashCode implementation");
    }
}