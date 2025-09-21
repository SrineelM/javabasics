package basics.mastery.advanced;.java.collections.fundamentals;

import io.mastery.java.collections.CollectionDemo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of Set collection implementations.
 * 
 * This class provides interactive examples covering:
 * - HashSet: Hash table implementation with O(1) basic operations
 * - LinkedHashSet: Hash table + linked list maintaining insertion order
 * - TreeSet: Red-Black tree implementation with sorted order
 * - EnumSet: Specialized Set for enum types
 * - ConcurrentHashMap.newKeySet(): Thread-safe Set implementation
 * 
 * Key Learning Points:
 * - Understanding when to use each Set implementation
 * - Performance characteristics and trade-offs
 * - Set operations (union, intersection, difference)
 * - Thread safety considerations
 * - Modern Java features with Sets
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
        
        // Set operations and algorithms
        demonstrateSetOperations();
        
        // Performance and thread safety
        performanceComparison();
        demonstrateThreadSafety();
        
        // Modern features
        demonstrateFunctionalOperations();
        demonstrateBestPractices();
        
        printSectionComplete("Set Collections");
    }
    
    /**
     * Demonstrates HashSet - the most commonly used Set implementation
     */
    private void demonstrateHashSet() {
        printSubHeader("HashSet - Hash Table Implementation");
        
        printInfo("HashSet provides O(1) performance for basic operations (add, remove, contains)");
        printInfo("No ordering guarantees - iteration order may vary");
        
        Set<String> languages = new HashSet<>();
        languages.add("Java");
        languages.add("Python");
        languages.add("JavaScript");
        languages.add("Java"); // Duplicate - won't be added
        
        printResult("HashSet contents", languages);
        printResult("Size", languages.size());
        printResult("Contains 'Java'", languages.contains("Java"));
        printResult("Is empty", languages.isEmpty());
        
        // Bulk operations
        Set<String> moreLanguages = Set.of("Go", "Rust", "Kotlin");
        languages.addAll(moreLanguages);
        printResult("After addAll", languages);
        
        // Removal operations
        languages.remove("Python");
        languages.removeIf(lang -> lang.startsWith("J"));
        printResult("After removals", languages);
        
        // Iteration (order not guaranteed)
        System.out.print("  Iteration order: ");
        languages.forEach(lang -> System.out.print(lang + " "));
        System.out.println();
        
        printTip("Use HashSet as default Set implementation for best performance");
        printWarning("HashSet order is not guaranteed and may change between runs");
    }
    
    /**
     * Demonstrates LinkedHashSet - maintains insertion order
     */
    private void demonstrateLinkedHashSet() {
        printSubHeader("LinkedHashSet - Ordered Hash Set");
        
        printInfo("LinkedHashSet maintains insertion order with slight performance cost");
        printInfo("Useful when you need predictable iteration order");
        
        Set<Integer> orderedSet = new LinkedHashSet<>();
        orderedSet.add(3);
        orderedSet.add(1);
        orderedSet.add(4);
        orderedSet.add(1); // Duplicate
        orderedSet.add(5);
        
        printResult("LinkedHashSet (insertion order)", orderedSet);
        
        // Demonstrate order preservation
        System.out.print("  Ordered iteration: ");
        orderedSet.forEach(num -> System.out.print(num + " "));
        System.out.println();
        
        // Convert list to unique ordered set
        List<String> duplicateList = List.of("a", "b", "c", "a", "b", "d");
        Set<String> uniqueOrdered = new LinkedHashSet<>(duplicateList);
        printResult("Duplicates removed (order preserved)", uniqueOrdered);
        
        printTip("Use LinkedHashSet when you need Set semantics with predictable iteration order");
    }
    
    /**
     * Demonstrates TreeSet - sorted Set implementation
     */
    private void demonstrateTreeSet() {
        printSubHeader("TreeSet - Sorted Set Implementation");
        
        printInfo("TreeSet maintains elements in sorted order using Red-Black tree");
        printInfo("Operations are O(log n) - slower than HashSet but provides ordering");
        
        TreeSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(List.of(5, 2, 8, 1, 9, 3, 7, 4, 6));
        
        printResult("TreeSet (automatically sorted)", sortedSet);
        
        // NavigableSet methods
        printResult("First (smallest)", sortedSet.first());
        printResult("Last (largest)", sortedSet.last());
        printResult("Lower than 5", sortedSet.lower(5));
        printResult("Higher than 5", sortedSet.higher(5));
        printResult("Floor of 4.5", sortedSet.floor(5));
        printResult("Ceiling of 4.5", sortedSet.ceiling(5));
        
        // Subset operations
        printResult("Head set (< 5)", sortedSet.headSet(5));
        printResult("Tail set (>= 5)", sortedSet.tailSet(5));
        printResult("Sub set (3-7)", sortedSet.subSet(3, 8));
        
        // Custom comparator example
        TreeSet<String> customSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        customSet.addAll(List.of("banana", "Apple", "cherry", "DATE"));
        printResult("Case-insensitive TreeSet", customSet);
        
        // Reverse order
        NavigableSet<String> reversedSet = customSet.descendingSet();
        printResult("Reversed order", reversedSet);
        
        printTip("Use TreeSet when you need sorted elements or range operations");
        printInfo("TreeSet elements must be Comparable or provide a Comparator");
    }
    
    /**
     * Demonstrates EnumSet - specialized Set for enum types
     */
    private void demonstrateEnumSet() {
        printSubHeader("EnumSet - Specialized Set for Enums");
        
        // Sample enum for demonstration
        enum Day {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
        }
        
        printInfo("EnumSet is extremely efficient - uses bit vectors internally");
        printInfo("All operations are O(1) and very fast");
        
        // Create EnumSet with specific values
        EnumSet<Day> weekdays = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY);
        printResult("Weekdays", weekdays);
        
        // Create EnumSet with all values
        EnumSet<Day> allDays = EnumSet.allOf(Day.class);
        printResult("All days", allDays);
        
        // Create empty EnumSet and add weekend
        EnumSet<Day> weekend = EnumSet.noneOf(Day.class);
        weekend.add(Day.SATURDAY);
        weekend.add(Day.SUNDAY);
        printResult("Weekend", weekend);
        
        // Range operations
        EnumSet<Day> midWeek = EnumSet.range(Day.TUESDAY, Day.THURSDAY);
        printResult("Mid-week (Tuesday to Thursday)", midWeek);
        
        // Complement operation
        EnumSet<Day> notWeekend = EnumSet.complementOf(weekend);
        printResult("Not weekend", notWeekend);
        
        // Set operations with EnumSet
        EnumSet<Day> workingDays = EnumSet.copyOf(weekdays);
        workingDays.retainAll(notWeekend);
        printResult("Working days", workingDays);
        
        printTip("Always use EnumSet for Sets of enum values - it's extremely efficient");
        printInfo("EnumSet operations are much faster than regular Sets for enums");
    }
    
    /**
     * Demonstrates mathematical set operations
     */
    private void demonstrateSetOperations() {
        printSubHeader("Mathematical Set Operations");
        
        Set<Integer> set1 = new HashSet<>(List.of(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(List.of(4, 5, 6, 7, 8));
        
        printResult("Set A", set1);
        printResult("Set B", set2);
        
        // Union (A ∪ B)
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        printResult("Union (A ∪ B)", union);
        
        // Intersection (A ∩ B)
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        printResult("Intersection (A ∩ B)", intersection);
        
        // Difference (A - B)
        Set<Integer> difference1 = new HashSet<>(set1);
        difference1.removeAll(set2);
        printResult("Difference (A - B)", difference1);
        
        // Difference (B - A)
        Set<Integer> difference2 = new HashSet<>(set2);
        difference2.removeAll(set1);
        printResult("Difference (B - A)", difference2);
        
        // Symmetric difference ((A ∪ B) - (A ∩ B))
        Set<Integer> symmetricDiff = new HashSet<>(union);
        symmetricDiff.removeAll(intersection);
        printResult("Symmetric difference", symmetricDiff);
        
        // Using streams for set operations (functional approach)
        Set<Integer> streamUnion = set1.stream()
                .collect(Collectors.toCollection(HashSet::new));
        streamUnion.addAll(set2);
        
        Set<Integer> streamIntersection = set1.stream()
                .filter(set2::contains)
                .collect(Collectors.toSet());
        
        printResult("Stream-based intersection", streamIntersection);
        
        // Set relationship testing
        printResult("A is subset of union", union.containsAll(set1));
        printResult("A and B are disjoint", Collections.disjoint(difference1, difference2));
        
        printTip("Use retainAll() for intersection, removeAll() for difference");
        printInfo("Set operations create new sets - original sets remain unchanged");
    }
    
    /**
     * Performance comparison between Set implementations
     */
    private void performanceComparison() {
        printSubHeader("Performance Analysis");
        
        final int operations = 100000;
        printInfo("Comparing Set implementations with " + formatNumber(operations) + " operations");
        
        // Add performance test
        compareAddPerformance(operations);
        compareLookupPerformance(operations);
        compareIterationPerformance(operations);
        compareMemoryUsage(operations);
    }
    
    private void compareAddPerformance(int operations) {
        printInfo("Add performance comparison:");
        
        double hashSetTime = measureTime(() -> {
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < operations; i++) {
                set.add(i);
            }
        });
        
        double linkedHashSetTime = measureTime(() -> {
            Set<Integer> set = new LinkedHashSet<>();
            for (int i = 0; i < operations; i++) {
                set.add(i);
            }
        });
        
        double treeSetTime = measureTime(() -> {
            Set<Integer> set = new TreeSet<>();
            for (int i = 0; i < operations; i++) {
                set.add(i);
            }
        });
        
        printBenchmark("HashSet add", hashSetTime);
        printBenchmark("LinkedHashSet add", linkedHashSetTime);
        printBenchmark("TreeSet add", treeSetTime);
        
        printComparison("Add operations", treeSetTime, hashSetTime, "TreeSet", "HashSet");
    }
    
    private void compareLookupPerformance(int operations) {
        printInfo("Lookup performance (10,000 random lookups):");
        
        // Prepare test data
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        IntStream.range(0, operations).forEach(i -> {
            hashSet.add(i);
            treeSet.add(i);
        });
        
        final int lookups = 10000;
        
        double hashSetTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < lookups; i++) {
                hashSet.contains(rnd.nextInt(operations));
            }
        });
        
        double treeSetTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < lookups; i++) {
                treeSet.contains(rnd.nextInt(operations));
            }
        });
        
        printBenchmark("HashSet lookup", hashSetTime);
        printBenchmark("TreeSet lookup", treeSetTime);
        
        printComparison("Lookup operations", treeSetTime, hashSetTime, "TreeSet", "HashSet");
    }
    
    private void compareIterationPerformance(int operations) {
        printInfo("Iteration performance:");
        
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        IntStream.range(0, operations).forEach(i -> {
            hashSet.add(i);
            linkedHashSet.add(i);
            treeSet.add(i);
        });
        
        double hashSetTime = measureTime(() -> {
            for (@SuppressWarnings("unused") Integer value : hashSet) {
                // Minimal work to avoid JVM optimizations
            }
        });
        
        double linkedHashSetTime = measureTime(() -> {
            for (@SuppressWarnings("unused") Integer value : linkedHashSet) {
                // Minimal work
            }
        });
        
        double treeSetTime = measureTime(() -> {
            for (@SuppressWarnings("unused") Integer value : treeSet) {
                // Minimal work
            }
        });
        
        printBenchmark("HashSet iteration", hashSetTime);
        printBenchmark("LinkedHashSet iteration", linkedHashSetTime);
        printBenchmark("TreeSet iteration", treeSetTime);
    }
    
    private void compareMemoryUsage(int operations) {
        printInfo("Memory usage comparison:");
        
        // Force garbage collection before measurement
        System.gc();
        long baseline = getUsedMemory();
        
        Set<Integer> hashSet = new HashSet<>();
        IntStream.range(0, operations).forEach(hashSet::add);
        long hashSetMemory = getUsedMemory();
        
        hashSet = null;
        System.gc();
        
        Set<Integer> treeSet = new TreeSet<>();
        IntStream.range(0, operations).forEach(treeSet::add);
        long treeSetMemory = getUsedMemory();
        
        printResult("HashSet memory", formatBytes(hashSetMemory - baseline));
        printResult("TreeSet memory", formatBytes(treeSetMemory - baseline));
        printInfo("TreeSet typically uses more memory due to tree node overhead");
    }
    
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Demonstrates thread-safe Set operations
     */
    private void demonstrateThreadSafety() {
        printSubHeader("Thread-Safe Set Operations");
        
        // ConcurrentHashMap.newKeySet() - modern thread-safe Set
        printInfo("ConcurrentHashMap.newKeySet() - Modern thread-safe Set");
        Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
        concurrentSet.addAll(List.of("Thread", "Safe", "Set"));
        printResult("Concurrent Set", concurrentSet);
        
        // Synchronized wrapper
        printInfo("Collections.synchronizedSet() - Synchronized wrapper");
        Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
        syncSet.addAll(List.of(1, 2, 3, 4, 5));
        
        // Manual synchronization required for iteration
        synchronized (syncSet) {
            printResult("Synchronized Set", syncSet);
        }
        
        // Immutable Sets (Java 9+)
        Set<String> immutableSet = Set.of("Immutable", "Set", "Java", "9+");
        printResult("Immutable Set", immutableSet);
        
        try {
            immutableSet.add("This will fail");
        } catch (UnsupportedOperationException e) {
            printInfo("Immutable Sets throw UnsupportedOperationException on modification");
        }
        
        // Copy of existing set to immutable
        Set<String> mutableSet = new HashSet<>(List.of("A", "B", "C"));
        Set<String> copyOfSet = Set.copyOf(mutableSet);
        printResult("Immutable copy", copyOfSet);
        
        printTip("Use ConcurrentHashMap.newKeySet() for thread-safe Sets");
        printWarning("Synchronized wrappers need manual synchronization for iteration");
    }
    
    /**
     * Demonstrates functional programming with Sets
     */
    private void demonstrateFunctionalOperations() {
        printSubHeader("Functional Programming with Sets");
        
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Filter operations
        Set<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toSet());
        printResult("Even numbers", evenNumbers);
        
        // Collect to different Set types
        Set<Integer> evenTreeSet = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toCollection(TreeSet::new));
        printResult("Even numbers (TreeSet)", evenTreeSet);
        
        // Transform and collect
        Set<String> numberStrings = numbers.stream()
                .map(n -> "Number: " + n)
                .collect(Collectors.toSet());
        printResult("Transformed strings", numberStrings.stream().limit(3).collect(Collectors.toSet()));
        
        // Partitioning
        Map<Boolean, Set<Integer>> partitioned = numbers.stream()
                .collect(Collectors.partitioningBy(
                    n -> n % 2 == 0,
                    Collectors.toSet()
                ));
        printResult("Even partition", partitioned.get(true));
        printResult("Odd partition", partitioned.get(false));
        
        // Grouping by criteria
        Set<String> words = Set.of("apple", "banana", "apricot", "cherry", "avocado");
        Map<Character, Set<String>> groupedByFirstLetter = words.stream()
                .collect(Collectors.groupingBy(
                    word -> word.charAt(0),
                    Collectors.toSet()
                ));
        printResult("Grouped by first letter", groupedByFirstLetter);
        
        printTip("Use collect(Collectors.toSet()) for Set collection from streams");
        printInfo("Collectors.toCollection() allows specifying exact Set implementation");
    }
    
    /**
     * Demonstrates best practices and guidelines
     */
    private void demonstrateBestPractices() {
        printSubHeader("Best Practices and Guidelines");
        
        printInfo("🎯 Set Implementation Selection Guide:");
        System.out.println("  • HashSet: Default choice - fastest for most operations");
        System.out.println("  • LinkedHashSet: When insertion order matters");
        System.out.println("  • TreeSet: When sorted order or range operations needed");
        System.out.println("  • EnumSet: Always use for enum values - extremely efficient");
        System.out.println("  • ConcurrentHashMap.newKeySet(): Thread-safe scenarios");
        
        printSeparator();
        
        printInfo("⚡ Performance Tips:");
        System.out.println("  • HashSet provides O(1) average case performance");
        System.out.println("  • TreeSet provides O(log n) but with ordering guarantees");
        System.out.println("  • LinkedHashSet has slight overhead for maintaining order");
        System.out.println("  • EnumSet operations are extremely fast (bit operations)");
        
        printSeparator();
        
        printInfo("⚠️  Common Pitfalls:");
        System.out.println("  • Don't use TreeSet if you don't need sorting");
        System.out.println("  • Remember HashSet iteration order is not guaranteed");
        System.out.println("  • Ensure proper equals() and hashCode() for custom objects");
        System.out.println("  • Use Set.of() for small immutable sets");
        
        printSeparator();
        
        // Demonstrate proper custom object handling
        printInfo("Proper custom object handling in Sets:");
        
        record Product(String name, double price) {
            // Records automatically provide correct equals() and hashCode()
        }
        
        Set<Product> products = new HashSet<>();
        products.add(new Product("Laptop", 999.99));
        products.add(new Product("Mouse", 29.99));
        products.add(new Product("Laptop", 999.99)); // Duplicate - won't be added
        
        printResult("Products set size", products.size());
        printInfo("Records provide correct equals() and hashCode() automatically");
        
        printTip("Always override equals() and hashCode() together for custom classes in Sets");
        printTip("Use Set.of() for small immutable sets, HashSet for mutable ones");
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}