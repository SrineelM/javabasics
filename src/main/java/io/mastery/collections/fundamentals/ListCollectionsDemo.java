package io.mastery.collections.fundamentals;

import io.mastery.collections.CollectionDemo;
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
 * @author Java Collections Tutorial Team
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
        
        printTip("Always specify initial capacity if you know the approximate size");
        printWarning("Frequent insertions/deletions in middle are expensive O(n)");
    }
    
    /**
     * Demonstrates LinkedList - doubly-linked list implementation
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
        
        printTip("Use LinkedList when you need frequent insertions/deletions at ends");
        printWarning("LinkedList has poor cache locality compared to ArrayList");
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
        
        final int elements = 10000; // Reduced for faster demo
        printInfo("Comparing List implementations with " + formatNumber(elements) + " elements");
        
        // Add performance comparison
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
        
        printBenchmark("ArrayList sequential add", arrayListTime);
        printBenchmark("LinkedList sequential add", linkedListTime);
        
        // Random access comparison
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        IntStream.range(0, elements).forEach(i -> {
            arrayList.add(i);
            linkedList.add(i);
        });
        
        final int accesses = 1000;
        double arrayListAccessTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < accesses; i++) {
                arrayList.get(rnd.nextInt(arrayList.size()));
            }
        });
        
        double linkedListAccessTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < accesses; i++) {
                linkedList.get(rnd.nextInt(linkedList.size()));
            }
        });
        
        printComparison("Random access (" + accesses + " ops)", 
                       linkedListAccessTime, arrayListAccessTime, 
                       "LinkedList", "ArrayList");
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
        printResult("Squares (first 5)", squares.subList(0, 5) + "...");
        
        // Reduce operations
        int sum = numbers.stream()
                .reduce(0, Integer::sum);
        printResult("Sum of all numbers", sum);
        
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
        
        // Correct way using removeIf (Java 8+)
        numbers.removeIf(n -> n % 2 == 0);
        printResult("After removeIf (odds only)", numbers);
        
        printTip("Use removeIf() for conditional removal during iteration");
    }
}