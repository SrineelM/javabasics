package basics.mastery.java.collections.fundamentals;

import basics.mastery.collections.CollectionDemo;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of List collection implementations.
 * 
 * This class provides interactive examples covering:
 * - ArrayList: Dynamic array implementation with fast random access
 * - LinkedList: Doubly-linked list implementation with efficient insertion/deletion
 * - Vector: Synchronized dynamic array (legacy, included for completeness)
 * - Stack: LIFO data structure (legacy, modern alternatives shown)
 * - CopyOnWriteArrayList: Thread-safe list for read-heavy scenarios
 * 
 * Key Learning Points:
 * - Understanding time complexity trade-offs between implementations
 * - When to use each implementation based on access patterns
 * - Performance characteristics and optimization techniques
 * - Thread safety considerations and modern alternatives
 * - Functional programming operations with Lists
 * 
 * @author Srineel with Copilot
 * @version 2.0
 * @since Java 17
 */
public class ListCollectionsDemo extends CollectionDemo {
    
    @Override
    public void demonstrateAll() {
        printHeader("LIST COLLECTIONS COMPREHENSIVE GUIDE");
        
        // Core List implementations
        demonstrateArrayList();
        demonstrateLinkedList();
        demonstrateVector();
        demonstrateStack();
        
        // Performance analysis
        performanceComparison();
        
        // Modern features and patterns
        demonstrateFunctionalOperations();
        demonstrateThreadSafeOperations();
        demonstrateModernFeatures();
        
        // Best practices and tips
        demonstrateBestPractices();
        
        printSectionComplete("List Collections");
    }
    
    /**
     * Demonstrates ArrayList - the most commonly used List implementation
     * 
     * ArrayList provides:
     * - O(1) random access via index
     * - O(1) amortized insertion at the end
     * - O(n) insertion/deletion in the middle
     * - Dynamic resizing with 50% growth factor
     */
    private void demonstrateArrayList() {
        printSubHeader("ArrayList - Dynamic Array Implementation");
        
        printInfo("ArrayList is the most commonly used List implementation");
        printInfo("Best for: Random access, frequent reads, small to medium lists");
        
        // Basic operations
        List<String> languages = new ArrayList<>();
        languages.add("Java");
        languages.add("Python");
        languages.add("JavaScript");
        languages.add(1, "C++"); // Insert at specific index
        
        printResult("Basic ArrayList", languages);
        printResult("Size", languages.size());
        printResult("Element at index 0", languages.get(0));
        printResult("Index of 'Java'", languages.indexOf("Java"));
        
        // Bulk operations
        List<String> moreLanguages = List.of("Go", "Rust", "Kotlin", "Swift");
        languages.addAll(moreLanguages);
        printResult("After bulk addition", languages);
        
        // Removal operations with different approaches
        languages.remove("Python");           // Remove by object
        languages.remove(0);                  // Remove by index
        languages.removeIf(lang -> lang.startsWith("S")); // Conditional removal
        printResult("After various removals", languages);
        
        // Searching and checking
        printResult("Contains 'Java'", languages.contains("Java"));
        printResult("Is empty", languages.isEmpty());
        printResult("Last index of 'Java'", languages.lastIndexOf("Java"));
        
        // Capacity optimization demonstration
        demonstrateArrayListCapacity();
        
        // Iteration patterns
        demonstrateArrayListIteration(languages);
        
        printTip("Always specify initial capacity if you know the approximate size");
        printWarning("Frequent insertions/deletions in middle are expensive O(n)");
    }
    
    /**
     * Demonstrates ArrayList capacity management and optimization
     */
    private void demonstrateArrayListCapacity() {
        printInfo("ArrayList capacity management:");
        
        // Default capacity
        List<Integer> defaultList = new ArrayList<>();
        printResult("Default initial capacity", "10 (implementation detail)");
        
        // Optimized capacity
        List<Integer> optimizedList = new ArrayList<>(1000);
        printInfo("Pre-sizing ArrayList avoids multiple resize operations");
        
        // Demonstrate performance difference
        double defaultTime = measureTime(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }
        });
        
        double optimizedTime = measureTime(() -> {
            List<Integer> list = new ArrayList<>(10000);
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }
        });
        
        printComparison("Adding 10,000 elements", defaultTime, optimizedTime, 
                       "Default capacity", "Pre-sized capacity");
        
        // Trimming to size
        List<String> oversizedList = new ArrayList<>(1000);
        oversizedList.addAll(List.of("A", "B", "C"));
        ((ArrayList<String>) oversizedList).trimToSize();
        printInfo("trimToSize() reduces capacity to actual size (memory optimization)");
    }
    
    /**
     * Demonstrates different iteration patterns for ArrayList
     */
    private void demonstrateArrayListIteration(List<String> list) {
        printInfo("ArrayList iteration patterns:");
        
        // Enhanced for-loop (recommended for most cases)
        System.out.print("  Enhanced for-loop: ");
        for (String item : list) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        // Index-based iteration (fast for ArrayList)
        System.out.print("  Index-based: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
        
        // Iterator (safe for concurrent modification)
        System.out.print("  Iterator: ");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        
        // Stream API (functional style)
        System.out.print("  Stream API: ");
        list.stream().forEach(item -> System.out.print(item + " "));
        System.out.println();
    }
    
    /**
     * Demonstrates LinkedList - doubly-linked list implementation
     * 
     * LinkedList provides:
     * - O(1) insertion/deletion at both ends
     * - O(n) random access via index
     * - Implements both List and Deque interfaces
     * - No capacity limitations
     */
    private void demonstrateLinkedList() {
        printSubHeader("LinkedList - Doubly-Linked List Implementation");
        
        printInfo("LinkedList implements both List and Deque interfaces");
        printInfo("Best for: Frequent insertions/deletions, implementing queues/stacks");
        
        LinkedList<String> linkedList = new LinkedList<>();
        
        // List operations
        linkedList.add("Middle");
        linkedList.add("End");
        linkedList.add(0, "Beginning");
        printResult("Basic LinkedList", linkedList);
        
        // Deque operations (both ends access)
        linkedList.addFirst("New First");
        linkedList.addLast("New Last");
        printResult("After deque operations", linkedList);
        
        // Peek operations (don't remove)
        printResult("Peek first", linkedList.peekFirst());
        printResult("Peek last", linkedList.peekLast());
        printResult("List unchanged", linkedList);
        
        // Poll operations (remove and return)
        printResult("Poll first", linkedList.pollFirst());
        printResult("Poll last", linkedList.pollLast());
        printResult("After polling", linkedList);
        
        // Queue operations (FIFO)
        linkedList.offer("Offered 1");
        linkedList.offer("Offered 2");
        printResult("After offering", linkedList);
        printResult("Poll from queue", linkedList.poll());
        
        // Stack operations (LIFO)
        linkedList.push("Pushed 1");
        linkedList.push("Pushed 2");
        printResult("After pushing", linkedList);
        printResult("Pop from stack", linkedList.pop());
        
        printTip("Use LinkedList when you need frequent insertions/deletions at ends");
        printWarning("LinkedList has poor cache locality compared to ArrayList");
        
        // Performance characteristics demonstration
        demonstrateLinkedListPerformance();
    }
    
    /**
     * Demonstrates LinkedList performance characteristics
     */
    private void demonstrateLinkedListPerformance() {
        printInfo("LinkedList vs ArrayList performance comparison:");
        
        final int size = 10000;
        
        // Insertion at beginning comparison
        double arrayListInsertTime = measureTime(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                list.add(0, i); // Insert at beginning - O(n) for ArrayList
            }
        });
        
        double linkedListInsertTime = measureTime(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < 1000; i++) {
                list.add(0, i); // Insert at beginning - O(1) for LinkedList
            }
        });
        
        printComparison("1000 insertions at beginning", arrayListInsertTime, 
                       linkedListInsertTime, "ArrayList", "LinkedList");
        
        // Random access comparison
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        IntStream.range(0, size).forEach(i -> {
            arrayList.add(i);
            linkedList.add(i);
        });
        
        double arrayListAccessTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                arrayList.get(i);
            }
        });
        
        double linkedListAccessTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                linkedList.get(i);
            }
        });
        
        printComparison("1000 random accesses", linkedListAccessTime, 
                       arrayListAccessTime, "LinkedList", "ArrayList");
    }
    
    /**
     * Demonstrates Vector - synchronized ArrayList (legacy)
     */
    private void demonstrateVector() {
        printSubHeader("Vector - Synchronized Dynamic Array (Legacy)");
        
        printWarning("Vector is largely obsolete - use ArrayList with synchronization if needed");
        printInfo("Included for educational purposes and legacy code understanding");
        
        Vector<Integer> vector = new Vector<>();
        vector.add(10);
        vector.add(20);
        vector.add(30);
        
        printResult("Vector contents", vector);
        printResult("Capacity", vector.capacity());
        printResult("Size", vector.size());
        
        // Vector-specific methods
        vector.insertElementAt(15, 1);
        printResult("After insertElementAt(15, 1)", vector);
        
        vector.setSize(10); // Resize vector
        printResult("After setSize(10)", vector);
        
        // Enumeration (legacy iteration)
        System.out.print("  Enumeration iteration: ");
        Enumeration<Integer> enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            System.out.print(enumeration.nextElement() + " ");
        }
        System.out.println();
        
        printTip("Use Collections.synchronizedList(new ArrayList<>()) instead of Vector");
        printInfo("Vector grows by 100% (doubles) while ArrayList grows by 50%");
    }
    
    /**
     * Demonstrates Stack - LIFO data structure (legacy)
     */
    private void demonstrateStack() {
        printSubHeader("Stack - LIFO Data Structure (Legacy)");
        
        printWarning("Stack class is legacy - use Deque implementations instead");
        
        // Legacy Stack
        Stack<String> stack = new Stack<>();
        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        
        printResult("Stack contents", stack);
        printResult("Peek (top element)", stack.peek());
        printResult("Pop", stack.pop());
        printResult("After pop", stack);
        printResult("Search for 'Bottom'", stack.search("Bottom"));
        printResult("Is empty", stack.empty());
        
        // Modern alternative using Deque
        printInfo("Modern Stack implementation using ArrayDeque:");
        Deque<String> modernStack = new ArrayDeque<>();
        modernStack.push("Item 1");
        modernStack.push("Item 2");
        modernStack.push("Item 3");
        
        printResult("Modern stack", modernStack);
        printResult("Pop from modern stack", modernStack.pop());
        printResult("Peek modern stack", modernStack.peek());
        
        printTip("Use ArrayDeque instead of Stack for better performance");
        printInfo("ArrayDeque is faster and not synchronized like Stack");
    }
    
    /**
     * Comprehensive performance comparison between List implementations
     */
    private void performanceComparison() {
        printSubHeader("Performance Comparison Analysis");
        
        final int elements = 50000;
        printInfo("Comparing List implementations with " + formatNumber(elements) + " elements");
        
        // Test data preparation
        List<Integer> testData = IntStream.range(0, elements).boxed().toList();
        
        // Add performance
        compareAddPerformance(elements);
        
        // Random access performance
        compareRandomAccessPerformance(testData);
        
        // Iteration performance
        compareIterationPerformance(testData);
        
        // Memory usage comparison
        compareMemoryUsage(elements);
    }
    
    private void compareAddPerformance(int elements) {
        printInfo("Sequential addition performance:");
        
        double arrayListTime = measureTime(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                list.add(i);
            }
        });
        
        double linkedListTime = measureTime(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < elements; i++) {
                list.add(i);
            }
        });
        
        double vectorTime = measureTime(() -> {
            List<Integer> list = new Vector<>();
            for (int i = 0; i < elements; i++) {
                list.add(i);
            }
        });
        
        printBenchmark("ArrayList", arrayListTime);
        printBenchmark("LinkedList", linkedListTime);
        printBenchmark("Vector", vectorTime);
    }
    
    private void compareRandomAccessPerformance(List<Integer> testData) {
        printInfo("Random access performance (10,000 accesses):");
        
        List<Integer> arrayList = new ArrayList<>(testData);
        List<Integer> linkedList = new LinkedList<>(testData);
        
        final int accesses = 10000;
        
        double arrayListTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < accesses; i++) {
                arrayList.get(rnd.nextInt(arrayList.size()));
            }
        });
        
        double linkedListTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < accesses; i++) {
                linkedList.get(rnd.nextInt(linkedList.size()));
            }
        });
        
        printBenchmark("ArrayList random access", arrayListTime);
        printBenchmark("LinkedList random access", linkedListTime);
        printComparison("Random access", linkedListTime, arrayListTime, 
                       "LinkedList", "ArrayList");
    }
    
    private void compareIterationPerformance(List<Integer> testData) {
        printInfo("Iteration performance comparison:");
        
        List<Integer> arrayList = new ArrayList<>(testData);
        List<Integer> linkedList = new LinkedList<>(testData);
        
        // Enhanced for-loop iteration
        double arrayListIterTime = measureTime(() -> {
            @SuppressWarnings("unused")
            long sum = 0;
            for (Integer value : arrayList) {
                sum += value;
            }
        });
        
        double linkedListIterTime = measureTime(() -> {
            @SuppressWarnings("unused")
            long sum = 0;
            for (Integer value : linkedList) {
                sum += value;
            }
        });
        
        printBenchmark("ArrayList iteration", arrayListIterTime);
        printBenchmark("LinkedList iteration", linkedListIterTime);
    }
    
    private void compareMemoryUsage(int elements) {
        printInfo("Memory usage characteristics:");
        
        System.gc(); // Suggest garbage collection
        long baseline = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        List<Integer> arrayList = new ArrayList<>(elements);
        IntStream.range(0, elements).forEach(arrayList::add);
        long arrayListMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        arrayList = null; // Allow GC
        System.gc();
        
        List<Integer> linkedList = new LinkedList<>();
        IntStream.range(0, elements).forEach(linkedList::add);
        long linkedListMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        printResult("ArrayList memory overhead", formatBytes(arrayListMemory - baseline));
        printResult("LinkedList memory overhead", formatBytes(linkedListMemory - baseline));
        printInfo("LinkedList has ~3x memory overhead due to node objects and pointers");
    }
    
    /**
     * Demonstrates functional programming operations with Lists
     */
    private void demonstrateFunctionalOperations() {
        printSubHeader("Functional Programming with Lists");
        
        List<Integer> numbers = IntStream.range(1, 21).boxed().toList();
        printResult("Source numbers", numbers);
        
        // Filter operations
        List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();
        printResult("Even numbers", evenNumbers);
        
        // Map operations
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .toList();
        printResult("Squares", squares.subList(0, 10) + "...");
        
        // Reduce operations
        int sum = numbers.stream()
                .reduce(0, Integer::sum);
        printResult("Sum of all numbers", sum);
        
        // Complex operations
        List<String> processedNumbers = numbers.stream()
                .filter(n -> n > 10)
                .map(n -> "Number: " + n)
                .sorted()
                .toList();
        printResult("Processed numbers (>10)", processedNumbers.subList(0, 5) + "...");
        
        // Parallel processing
        long parallelSum = numbers.parallelStream()
                .mapToLong(Integer::longValue)
                .sum();
        printResult("Parallel sum", parallelSum);
        
        printTip("Use toList() (Java 16+) instead of collect(Collectors.toList())");
        printInfo("Parallel streams are beneficial for CPU-intensive operations on large datasets");
    }
    
    /**
     * Demonstrates thread-safe List operations
     */
    private void demonstrateThreadSafeOperations() {
        printSubHeader("Thread-Safe List Operations");
        
        // CopyOnWriteArrayList - optimized for read-heavy scenarios
        printInfo("CopyOnWriteArrayList - Thread-safe, optimized for reads");
        List<String> cowList = new CopyOnWriteArrayList<>();
        cowList.addAll(List.of("Thread", "Safe", "List"));
        printResult("CopyOnWriteArrayList", cowList);
        
        // Synchronized wrapper
        printInfo("Synchronized wrapper - Thread-safe but requires manual synchronization for iteration");
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        syncList.addAll(List.of("Synchronized", "List", "Example"));
        
        // Safe iteration requires synchronization
        synchronized (syncList) {
            printResult("Synchronized list", syncList);
        }
        
        // Immutable Lists (Java 9+)
        List<String> immutableList = List.of("Immutable", "List", "Java", "9+");
        printResult("Immutable List", immutableList);
        
        try {
            immutableList.add("This will fail");
        } catch (UnsupportedOperationException e) {
            printInfo("Immutable lists throw UnsupportedOperationException on modification");
        }
        
        // Collections.unmodifiableList()
        List<String> mutableList = new ArrayList<>(List.of("A", "B", "C"));
        List<String> unmodifiableView = Collections.unmodifiableList(mutableList);
        printResult("Unmodifiable view", unmodifiableView);
        
        mutableList.add("D"); // Original list can still be modified
        printResult("View after modifying original", unmodifiableView);
        
        printTip("Use CopyOnWriteArrayList for read-heavy, write-light scenarios");
        printWarning("Synchronized wrappers require manual synchronization for compound operations");
    }
    
    /**
     * Demonstrates modern Java features with Lists
     */
    private void demonstrateModernFeatures() {
        printSubHeader("Modern Java Features with Lists");
        
        // Records with Lists (Java 14+)
        record Person(String name, int age, List<String> hobbies) {
            public Person {
                hobbies = List.copyOf(hobbies); // Defensive copy
            }
        }
        
        List<Person> people = List.of(
            new Person("Alice", 28, List.of("Reading", "Hiking")),
            new Person("Bob", 34, List.of("Gaming", "Cooking")),
            new Person("Charlie", 22, List.of("Music", "Sports"))
        );
        
        printResult("People with hobbies", people);
        
        // Pattern matching with instanceof (Java 16+)
        List<Object> mixedList = List.of("String", 42, 3.14, List.of(1, 2, 3));
        
        for (Object item : mixedList) {
            String description = switch (item) {
                case String s -> "String of length " + s.length();
                case Integer i -> "Integer: " + i;
                case Double d -> "Double: " + d;
                case List<?> list -> "List with " + list.size() + " elements";
                default -> "Unknown type";
            };
            printResult("Pattern matching", description);
        }
        
        // Demonstrate sealed classes concept (simplified example)
        printInfo("Sealed classes restrict inheritance hierarchy (Java 17+)");
        printInfo("Example: sealed interface Shape permits Circle, Rectangle");
        
        printTip("Use records for immutable data with Lists for better encapsulation");
        printInfo("Pattern matching simplifies type checking and data extraction");
    }
    
    /**
     * Demonstrates best practices and common patterns
     */
    private void demonstrateBestPractices() {
        printSubHeader("Best Practices and Common Patterns");
        
        printInfo("📋 List Selection Guidelines:");
        System.out.println("  • ArrayList: Default choice for most use cases");
        System.out.println("  • LinkedList: Frequent insertions/deletions at ends");
        System.out.println("  • CopyOnWriteArrayList: Read-heavy, thread-safe scenarios");
        System.out.println("  • Immutable Lists: Data that shouldn't change");
        
        printSeparator();
        
        printInfo("🚀 Performance Tips:");
        System.out.println("  • Pre-size ArrayList if you know approximate capacity");
        System.out.println("  • Use enhanced for-loop for iteration when possible");
        System.out.println("  • Prefer List.of() for small immutable lists");
        System.out.println("  • Use ArrayList.trimToSize() to reduce memory footprint");
        System.out.println("  • Consider primitive collections (TIntList) for large datasets");
        
        printSeparator();
        
        printInfo("⚠️  Common Pitfalls to Avoid:");
        System.out.println("  • Don't use Vector or Stack (legacy classes)");
        System.out.println("  • Avoid LinkedList for random access operations");
        System.out.println("  • Don't modify list during enhanced for-loop iteration");
        System.out.println("  • Be careful with ArrayList.remove(int) vs remove(Object)");
        System.out.println("  • Remember that List.of() creates immutable lists");
        
        // Demonstrate safe removal pattern
        printInfo("Safe removal during iteration:");
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        
        // Wrong way (will throw ConcurrentModificationException)
        printWarning("DON'T do this - will throw exception:");
        System.out.println("  for (Integer num : list) { if (num % 2 == 0) list.remove(num); }");
        
        // Correct ways
        printInfo("✅ Correct removal patterns:");
        
        // Using iterator
        Iterator<Integer> iter = numbers.iterator();
        while (iter.hasNext()) {
            if (iter.next() % 2 == 0) {
                iter.remove();
            }
        }
        printResult("After iterator removal", numbers);
        
        // Using removeIf (Java 8+)
        numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        numbers.removeIf(n -> n % 2 == 0);
        printResult("After removeIf", numbers);
        
        // Using streams
        numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> filtered = numbers.stream()
                .filter(n -> n % 2 != 0)
                .toList();
        printResult("After stream filtering", filtered);
    }
    
    protected String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}