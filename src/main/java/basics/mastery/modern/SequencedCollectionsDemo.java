package basics.mastery.modern;

import java.util.*;

/**
 * Comprehensive demonstration of JDK 21 Collections features
 * 
 * This class showcases the new collection interfaces introduced in JDK 21:
 * - SequencedCollection (JDK 21)
 * - SequencedSet (JDK 21)
 * - SequencedMap (JDK 21)
 * 
 * These new interfaces provide methods for accessing first and last elements,
 * and creating reversed views of collections with defined encounter orders.
 * 
 * Key benefits:
 * - Unified API for accessing elements at both ends
 * - Efficient reversed views without copying
 * - Consistent behavior across List, Deque, SortedSet, and LinkedHashSet
 * 
 * @author Srineel with Copilot
 * @since Java 21
 */
public class SequencedCollectionsDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK 21 Sequenced Collections Demo ===\n");
        
        demonstrateSequencedCollection();
        demonstrateSequencedSet();
        demonstrateSequencedMap();
        demonstrateReversedViews();
        demonstrateFirstLastOperations();
        demonstrateMigrationPatterns();
        
        System.out.println("\n🎯 Sequenced Collections provide consistent access patterns!");
    }

    /**
     * Demonstrates SequencedCollection - the root interface for collections
     * with a defined encounter order
     */
    private static void demonstrateSequencedCollection() {
        System.out.println("1. SequencedCollection Interface:");
        System.out.println("   Provides: addFirst(), addLast(), getFirst(), getLast(), reversed()");
        System.out.println();
        
        // List implements SequencedCollection
        List<String> list = new ArrayList<>();
        addSequencedElements(list);
        
        System.out.println("ArrayList as SequencedCollection:");
        System.out.println("  Elements: " + list);
        System.out.println("  First: " + list.getFirst());
        System.out.println("  Last: " + list.getLast());
        System.out.println("  Reversed: " + list.reversed());
        
        // Deque also implements SequencedCollection
        Deque<String> deque = new ArrayDeque<>();
        addSequencedElements(deque);
        
        System.out.println("\nArrayDeque as SequencedCollection:");
        System.out.println("  Elements: " + deque);
        System.out.println("  First: " + deque.getFirst());
        System.out.println("  Last: " + deque.getLast());
        System.out.println();
    }

    /**
     * Demonstrates SequencedSet - a Set with a defined encounter order
     */
    private static void demonstrateSequencedSet() {
        System.out.println("2. SequencedSet Interface:");
        System.out.println("   Combines Set uniqueness with encounter order");
        System.out.println();
        
        // LinkedHashSet implements SequencedSet
        Set<Integer> linkedSet = new LinkedHashSet<>();
        linkedSet.add(10);
        linkedSet.add(20);
        linkedSet.add(30);
        linkedSet.add(40);
        linkedSet.add(20); // Duplicate, won't be added
        
        System.out.println("LinkedHashSet as SequencedSet:");
        System.out.println("  Elements: " + linkedSet);
        System.out.println("  First: " + ((SequencedSet<Integer>)linkedSet).getFirst());
        System.out.println("  Last: " + ((SequencedSet<Integer>)linkedSet).getLast());
        System.out.println("  Reversed: " + ((SequencedSet<Integer>)linkedSet).reversed());
        
        // SortedSet implements SequencedSet
        SortedSet<String> treeSet = new TreeSet<>();
        treeSet.add("Charlie");
        treeSet.add("Alice");
        treeSet.add("Bob");
        
        System.out.println("\nTreeSet as SequencedSet:");
        System.out.println("  Elements (sorted): " + treeSet);
        System.out.println("  First (smallest): " + treeSet.first());
        System.out.println("  Last (largest): " + treeSet.last());
        System.out.println();
    }

    /**
     * Demonstrates SequencedMap - a Map with a defined entry encounter order
     */
    private static void demonstrateSequencedMap() {
        System.out.println("3. SequencedMap Interface:");
        System.out.println("   Provides ordered access to map entries");
        System.out.println();
        
        // LinkedHashMap implements SequencedMap
        Map<String, Integer> linkedMap = new LinkedHashMap<>();
        linkedMap.put("First", 1);
        linkedMap.put("Second", 2);
        linkedMap.put("Third", 3);
        linkedMap.put("Fourth", 4);
        
        System.out.println("LinkedHashMap as SequencedMap:");
        System.out.println("  Entries: " + linkedMap);
        System.out.println("  First entry: " + ((SequencedMap<String, Integer>)linkedMap).firstEntry());
        System.out.println("  Last entry: " + ((SequencedMap<String, Integer>)linkedMap).lastEntry());
        System.out.println("  Reversed: " + ((SequencedMap<String, Integer>)linkedMap).reversed());
        
        // SortedMap implements SequencedMap
        SortedMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(30, "Thirty");
        treeMap.put(10, "Ten");
        treeMap.put(20, "Twenty");
        
        System.out.println("\nTreeMap as SequencedMap:");
        System.out.println("  Entries (sorted by key): " + treeMap);
        System.out.println("  First entry: " + treeMap.firstKey() + "=" + treeMap.get(treeMap.firstKey()));
        System.out.println("  Last entry: " + treeMap.lastKey() + "=" + treeMap.get(treeMap.lastKey()));
        System.out.println();
    }

    /**
     * Demonstrates efficient reversed views without copying
     */
    private static void demonstrateReversedViews() {
        System.out.println("4. Efficient Reversed Views:");
        System.out.println("   reversed() creates a view, not a copy");
        System.out.println();
        
        List<String> original = new ArrayList<>(List.of("A", "B", "C", "D"));
        List<String> reversedView = original.reversed();
        
        System.out.println("Original list: " + original);
        System.out.println("Reversed view: " + reversedView);
        
        // Modifications to original reflect in reversed view
        original.add("E");
        System.out.println("\nAfter adding 'E' to original:");
        System.out.println("Original list: " + original);
        System.out.println("Reversed view: " + reversedView);
        
        // Can iterate in reverse efficiently
        System.out.println("\nIterating reversed view:");
        for (String item : reversedView) {
            System.out.print(item + " ");
        }
        System.out.println("\n");
    }

    /**
     * Demonstrates first/last operations across different collections
     */
    private static void demonstrateFirstLastOperations() {
        System.out.println("5. Unified First/Last Operations:");
        System.out.println("   Consistent API across different collection types");
        System.out.println();
        
        // Before JDK 21 - inconsistent API
        System.out.println("Before JDK 21 (inconsistent):");
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("  List first: list.get(0) = " + list.get(0));
        System.out.println("  List last: list.get(size-1) = " + list.get(list.size() - 1));
        
        Deque<String> deque = new ArrayDeque<>(List.of("A", "B", "C"));
        System.out.println("  Deque first: deque.getFirst() = " + deque.getFirst());
        System.out.println("  Deque last: deque.getLast() = " + deque.getLast());
        
        // With JDK 21 - consistent API
        System.out.println("\nWith JDK 21 (consistent):");
        List<String> modernList = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("  List first: list.getFirst() = " + modernList.getFirst());
        System.out.println("  List last: list.getLast() = " + modernList.getLast());
        
        Deque<String> modernDeque = new ArrayDeque<>(List.of("A", "B", "C"));
        System.out.println("  Deque first: deque.getFirst() = " + modernDeque.getFirst());
        System.out.println("  Deque last: deque.getLast() = " + modernDeque.getLast());
        System.out.println();
    }

    /**
     * Demonstrates migration patterns from pre-JDK 21 code
     */
    private static void demonstrateMigrationPatterns() {
        System.out.println("6. Migration Patterns:");
        System.out.println("   How to modernize your collections code");
        System.out.println();
        
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        
        // Old way - verbose and error-prone
        System.out.println("Old pattern:");
        System.out.println("  First: list.get(0) = " + numbers.get(0));
        System.out.println("  Last: list.get(list.size()-1) = " + numbers.get(numbers.size() - 1));
        
        // Create reversed copy (inefficient)
        List<Integer> reversedCopy = new ArrayList<>(numbers);
        Collections.reverse(reversedCopy);
        System.out.println("  Reversed (copy): " + reversedCopy);
        
        // New way - clean and efficient
        System.out.println("\nNew pattern (JDK 21):");
        System.out.println("  First: list.getFirst() = " + numbers.getFirst());
        System.out.println("  Last: list.getLast() = " + numbers.getLast());
        System.out.println("  Reversed (view): " + numbers.reversed());
        
        // Remove first and last
        List<Integer> mutableList = new ArrayList<>(List.of(10, 20, 30, 40, 50));
        System.out.println("\nOriginal: " + mutableList);
        mutableList.removeFirst();
        System.out.println("After removeFirst(): " + mutableList);
        mutableList.removeLast();
        System.out.println("After removeLast(): " + mutableList);
        
        // Add at both ends
        mutableList.addFirst(5);
        mutableList.addLast(45);
        System.out.println("After addFirst(5) and addLast(45): " + mutableList);
        System.out.println();
    }

    /**
     * Helper method to add elements to a SequencedCollection
     */
    private static void addSequencedElements(SequencedCollection<String> collection) {
        collection.addLast("Alpha");
        collection.addLast("Beta");
        collection.addLast("Gamma");
        collection.addFirst("Omega"); // Add at beginning
    }

    /**
     * Demonstrates practical use cases for SequencedCollections
     */
    public static class UseCases {
        
        /**
         * LRU (Least Recently Used) cache using SequencedMap
         */
        static class LRUCache<K, V> {
            private final int capacity;
            private final LinkedHashMap<K, V> cache = new LinkedHashMap<>();
            
            public LRUCache(int capacity) {
                this.capacity = capacity;
            }
            
            public void put(K key, V value) {
                cache.put(key, value);
                if (cache.size() > capacity) {
                    // Remove least recently used (first) entry
                    K firstKey = ((SequencedMap<K, V>)cache).firstEntry().getKey();
                    cache.remove(firstKey);
                }
            }
            
            public V get(K key) {
                V value = cache.get(key);
                if (value != null) {
                    // Move to end (most recently used)
                    cache.remove(key);
                    cache.put(key, value);
                }
                return value;
            }
        }
        
        /**
         * Undo/Redo stack using SequencedCollection
         */
        static class UndoRedoManager<T> {
            private final Deque<T> undoStack = new ArrayDeque<>();
            private final Deque<T> redoStack = new ArrayDeque<>();
            
            public void execute(T action) {
                undoStack.addLast(action);
                redoStack.clear();
            }
            
            public T undo() {
                if (!undoStack.isEmpty()) {
                    T action = undoStack.removeLast();
                    redoStack.addLast(action);
                    return action;
                }
                return null;
            }
            
            public T redo() {
                if (!redoStack.isEmpty()) {
                    T action = redoStack.removeLast();
                    undoStack.addLast(action);
                    return action;
                }
                return null;
            }
            
            public T peek() {
                return undoStack.isEmpty() ? null : undoStack.getLast();
            }
        }
    }
}
