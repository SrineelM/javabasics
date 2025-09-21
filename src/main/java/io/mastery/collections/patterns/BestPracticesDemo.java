package io.mastery.collections.patterns;

import io.mastery.collections.CollectionDemo;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of Java Collections best practices and design patterns.
 * 
 * This class provides expert-level guidance on how to effectively use Java Collections
 * in real-world applications. It covers design patterns, common pitfalls, performance
 * considerations, and enterprise-grade practices.
 * 
 * Key concepts covered:
 * - Collection design patterns
 * - Defensive programming practices
 * - Thread-safety patterns
 * - Memory management best practices
 * - Error handling strategies
 * - Performance optimization patterns
 * - Code maintainability principles
 * 
 * @author Java Collections Tutorial
 * @since Java 21
 */
public class BestPracticesDemo extends CollectionDemo {

    /**
     * Demonstrates all best practices and patterns
     */
    public void demonstrateAll() {
        printHeader("Collections Best Practices and Patterns Demo");
        
        demonstrateDefensiveProgramming();
        demonstrateFactoryPatterns();
        demonstrateBuilderPattern();
        demonstrateImmutableCollections();
        demonstrateThreadSafetyPatterns();
        demonstrateMemoryManagement();
        demonstrateErrorHandling();
        demonstratePerformancePatterns();
        demonstrateCodeMaintainability();
        demonstrateCommonPitfalls();
        
        printSeparator();
    }

    /**
     * Demonstrates defensive programming practices with collections
     */
    private void demonstrateDefensiveProgramming() {
        printSubHeader("Defensive Programming Practices");
        
        printInfo("Protecting against external modifications and ensuring data integrity");
        
        // 1. Defensive copying in constructors
        demonstrateDefensiveCopying();
        
        // 2. Immutable views
        demonstrateImmutableViews();
        
        // 3. Parameter validation
        demonstrateParameterValidation();
        
        printTip("Always make defensive copies of mutable collections when exposing them");
        printTip("Use Collections.unmodifiableXxx() to provide read-only views");
        printWarning("Shallow copies only protect the collection structure, not the elements");
    }

    /**
     * Shows defensive copying techniques
     */
    private void demonstrateDefensiveCopying() {
        printInfo("Defensive Copying Patterns:");
        
        // Good practice: Defensive copying
        class UserAccountGood {
            private final List<String> roles;
            
            public UserAccountGood(List<String> roles) {
                // Defensive copy in constructor
                this.roles = new ArrayList<>(roles);
            }
            
            public List<String> getRoles() {
                // Defensive copy in getter
                return new ArrayList<>(roles);
            }
            
            public void addRole(String role) {
                if (role != null && !role.trim().isEmpty()) {
                    roles.add(role);
                }
            }
        }
        
        List<String> originalRoles = new ArrayList<>(List.of("USER", "ADMIN"));
        UserAccountGood account = new UserAccountGood(originalRoles);
        
        // External modification doesn't affect internal state
        originalRoles.add("HACKER");
        List<String> exposedRoles = account.getRoles();
        exposedRoles.add("MALICIOUS");
        
        printResult("Original roles after modification", originalRoles);
        printResult("Account roles (protected)", account.getRoles());
        
        // Bad practice example (for demonstration)
        class UserAccountBad {
            private List<String> roles;
            
            public UserAccountBad(List<String> roles) {
                this.roles = roles; // Direct reference - dangerous!
            }
            
            public List<String> getRoles() {
                return roles; // Direct exposure - dangerous!
            }
        }
        
        printWarning("Bad practice: Direct reference sharing allows external modifications");
    }

    /**
     * Demonstrates immutable views
     */
    private void demonstrateImmutableViews() {
        printInfo("Immutable Views Pattern:");
        
        class ConfigurationManager {
            private final Map<String, String> config = new HashMap<>();
            
            public ConfigurationManager() {
                config.put("database.url", "jdbc:mysql://localhost:3306/mydb");
                config.put("cache.size", "1000");
                config.put("debug.enabled", "false");
            }
            
            public Map<String, String> getConfiguration() {
                return Collections.unmodifiableMap(config);
            }
            
            public void updateConfig(String key, String value) {
                if (key != null && value != null) {
                    config.put(key, value);
                }
            }
        }
        
        ConfigurationManager configManager = new ConfigurationManager();
        Map<String, String> readOnlyConfig = configManager.getConfiguration();
        
        printResult("Read-only configuration", readOnlyConfig);
        
        try {
            readOnlyConfig.put("malicious.key", "malicious.value");
        } catch (UnsupportedOperationException e) {
            printResult("Attempted modification blocked", "UnsupportedOperationException caught");
        }
    }

    /**
     * Shows parameter validation patterns
     */
    private void demonstrateParameterValidation() {
        printInfo("Parameter Validation Patterns:");
        
        class DataProcessor {
            public List<String> processItems(Collection<String> items) {
                // Null check
                Objects.requireNonNull(items, "Items collection cannot be null");
                
                // Empty check with meaningful message
                if (items.isEmpty()) {
                    throw new IllegalArgumentException("Items collection cannot be empty");
                }
                
                // Process with validation
                return items.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> !item.trim().isEmpty())
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
            }
            
            public <T> List<T> safeCopy(Collection<T> source) {
                return source == null ? new ArrayList<>() : new ArrayList<>(source);
            }
        }
        
        DataProcessor processor = new DataProcessor();
        
        // Valid usage
        List<String> validItems = List.of("hello", "world", "java");
        List<String> processed = processor.processItems(validItems);
        printResult("Processed valid items", processed);
        
        // Safe copy with null
        List<String> safeCopy = processor.safeCopy(null);
        printResult("Safe copy of null", safeCopy);
    }

    /**
     * Demonstrates factory patterns for collections
     */
    private void demonstrateFactoryPatterns() {
        printSubHeader("Factory Patterns for Collections");
        
        printInfo("Using factory methods to create collections with specific behaviors");
        
        // Collection factory class
        class CollectionFactory {
            
            // Factory for thread-safe collections
            public static <T> List<T> createThreadSafeList() {
                return Collections.synchronizedList(new ArrayList<>());
            }
            
            public static <K, V> Map<K, V> createThreadSafeMap() {
                return new ConcurrentHashMap<>();
            }
            
            // Factory for bounded collections
            public static <T> Queue<T> createBoundedQueue(int capacity) {
                return new ArrayBlockingQueue<>(capacity);
            }
            
            // Factory for specialized collections
            public static <T extends Comparable<T>> Set<T> createSortedSet() {
                return new TreeSet<>();
            }
            
            // Factory with custom comparator
            public static <T> Set<T> createSortedSet(Comparator<T> comparator) {
                return new TreeSet<>(comparator);
            }
        }
        
        // Usage examples
        List<String> threadSafeList = CollectionFactory.createThreadSafeList();
        threadSafeList.addAll(List.of("Thread", "Safe", "List"));
        printResult("Thread-safe list", threadSafeList);
        
        Map<String, Integer> threadSafeMap = CollectionFactory.createThreadSafeMap();
        threadSafeMap.put("concurrent", 1);
        threadSafeMap.put("safe", 2);
        printResult("Thread-safe map", threadSafeMap);
        
        Queue<String> boundedQueue = CollectionFactory.createBoundedQueue(3);
        boundedQueue.offer("First");
        boundedQueue.offer("Second");
        boundedQueue.offer("Third");
        boolean overflow = boundedQueue.offer("Fourth"); // Should fail
        printResult("Bounded queue", boundedQueue);
        printResult("Overflow attempt succeeded", overflow);
        
        Set<String> sortedSet = CollectionFactory.createSortedSet();
        sortedSet.addAll(List.of("Zebra", "Apple", "Banana"));
        printResult("Sorted set", sortedSet);
        
        printTip("Use factory methods to encapsulate collection creation logic");
        printTip("Factories make it easy to change implementation details later");
    }

    /**
     * Demonstrates the Builder pattern for complex collections
     */
    private void demonstrateBuilderPattern() {
        printSubHeader("Builder Pattern for Complex Collections");
        
        printInfo("Building complex collection structures with fluent interfaces");
        
        // Collection builder
        class ListBuilder<T> {
            private final List<T> list = new ArrayList<>();
            
            public ListBuilder<T> add(T item) {
                if (item != null) {
                    list.add(item);
                }
                return this;
            }
            
            public ListBuilder<T> addAll(Collection<T> items) {
                if (items != null) {
                    list.addAll(items);
                }
                return this;
            }
            
            public ListBuilder<T> addIf(T item, Predicate<T> condition) {
                if (item != null && condition.test(item)) {
                    list.add(item);
                }
                return this;
            }
            
            public ListBuilder<T> filter(Predicate<T> predicate) {
                list.removeIf(predicate.negate());
                return this;
            }
            
            public List<T> build() {
                return new ArrayList<>(list);
            }
            
            public List<T> buildImmutable() {
                return Collections.unmodifiableList(new ArrayList<>(list));
            }
        }
        
        // Usage example
        List<String> builtList = new ListBuilder<String>()
                .add("Java")
                .add("Python")
                .add("JavaScript")
                .addAll(List.of("C++", "Go", "Rust"))
                .addIf("Cobol", lang -> lang.length() > 3)
                .filter(lang -> !lang.equals("JavaScript"))
                .build();
        
        printResult("Built list", builtList);
        
        // Map builder example
        class MapBuilder<K, V> {
            private final Map<K, V> map = new HashMap<>();
            
            public MapBuilder<K, V> put(K key, V value) {
                if (key != null && value != null) {
                    map.put(key, value);
                }
                return this;
            }
            
            public MapBuilder<K, V> putAll(Map<K, V> otherMap) {
                if (otherMap != null) {
                    map.putAll(otherMap);
                }
                return this;
            }
            
            public MapBuilder<K, V> putIf(K key, V value, Predicate<V> condition) {
                if (key != null && value != null && condition.test(value)) {
                    map.put(key, value);
                }
                return this;
            }
            
            public Map<K, V> build() {
                return new HashMap<>(map);
            }
            
            public Map<K, V> buildImmutable() {
                return Collections.unmodifiableMap(new HashMap<>(map));
            }
        }
        
        Map<String, Integer> builtMap = new MapBuilder<String, Integer>()
                .put("low", 1)
                .put("medium", 5)
                .put("high", 10)
                .putIf("extreme", 20, value -> value > 15)
                .build();
        
        printResult("Built map", builtMap);
        
        printTip("Builder pattern provides fluent, readable APIs for complex collection creation");
        printTip("Use builders when collections need conditional or multi-step construction");
    }

    /**
     * Demonstrates immutable collection patterns
     */
    private void demonstrateImmutableCollections() {
        printSubHeader("Immutable Collection Patterns");
        
        printInfo("Creating and working with immutable collections");
        
        // Factory methods (Java 9+)
        printInfo("Factory Method Immutables:");
        List<String> immutableList = List.of("Java", "Collections", "Immutable");
        Set<Integer> immutableSet = Set.of(1, 2, 3, 4, 5);
        Map<String, String> immutableMap = Map.of(
            "lang", "Java",
            "version", "21",
            "feature", "Collections"
        );
        
        printResult("Immutable List", immutableList);
        printResult("Immutable Set", immutableSet);
        printResult("Immutable Map", immutableMap);
        
        // Builder pattern for large immutable collections
        printInfo("Builder Pattern Immutables:");
        List<Integer> largeImmutableList = IntStream.range(1, 20)
                .boxed()
                .collect(Collectors.toUnmodifiableList());
        
        Map<String, Integer> largeImmutableMap = IntStream.range(1, 10)
                .boxed()
                .collect(Collectors.toUnmodifiableMap(
                    i -> "key" + i,
                    i -> i * i
                ));
        
        printResult("Large immutable list", largeImmutableList);
        printResult("Large immutable map", largeImmutableMap);
        
        // Immutable wrapper pattern
        printInfo("Wrapper Pattern Immutables:");
        List<String> mutableList = new ArrayList<>(List.of("Mutable", "List"));
        List<String> immutableView = Collections.unmodifiableList(mutableList);
        
        printResult("Original mutable list", mutableList);
        printResult("Immutable view", immutableView);
        
        // Modify original - view reflects changes
        mutableList.add("Modified");
        printResult("After modifying original", immutableView);
        
        printTip("Factory methods create truly immutable collections");
        printTip("Unmodifiable wrappers create read-only views, not immutable copies");
        printWarning("Immutable views can still change if the underlying collection changes");
    }

    /**
     * Demonstrates thread-safety patterns
     */
    private void demonstrateThreadSafetyPatterns() {
        printSubHeader("Thread-Safety Patterns");
        
        printInfo("Patterns for safe concurrent access to collections");
        
        // 1. Synchronized collections
        demonstrateSynchronizedCollections();
        
        // 2. Concurrent collections
        demonstrateConcurrentCollections();
        
        // 3. Copy-on-write patterns
        demonstrateCopyOnWritePatterns();
        
        // 4. Lock-based patterns
        demonstrateLockBasedPatterns();
        
        printTip("Choose thread-safety mechanism based on access patterns");
        printTip("Concurrent collections usually outperform synchronized ones");
        printWarning("Synchronized collections only protect individual operations, not compound ones");
    }

    /**
     * Shows synchronized collection patterns
     */
    private void demonstrateSynchronizedCollections() {
        printInfo("Synchronized Collections Pattern:");
        
        // Synchronized wrappers
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        @SuppressWarnings("unused")
        Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
        @SuppressWarnings("unused")
        Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
        
        // Safe iteration requires external synchronization
        syncList.addAll(List.of("Thread", "Safe", "Iteration"));
        
        // Correct way to iterate synchronized collections
        synchronized (syncList) {
            for (String item : syncList) {
                System.out.println("  Processing: " + item);
            }
        }
        
        printResult("Synchronized list", syncList);
        printWarning("Iteration over synchronized collections requires external synchronization");
    }

    /**
     * Shows concurrent collection patterns
     */
    private void demonstrateConcurrentCollections() {
        printInfo("Concurrent Collections Pattern:");
        
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
        @SuppressWarnings("unused")
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        
        // Atomic operations
        concurrentMap.put("initial", 1);
        Integer updated = concurrentMap.compute("initial", (key, value) -> value + 1);
        concurrentMap.putIfAbsent("new", 10);
        
        printResult("Concurrent map after operations", concurrentMap);
        printResult("Updated value", updated);
        
        // Copy-on-write for read-heavy scenarios
        cowList.addAll(List.of("Read", "Heavy", "Workload"));
        // Safe iteration without synchronization
        for (String item : cowList) {
            System.out.println("  COW item: " + item);
        }
        
        printTip("ConcurrentHashMap allows concurrent reads and writes");
        printTip("CopyOnWriteArrayList is perfect for read-heavy scenarios");
    }

    /**
     * Shows copy-on-write patterns
     */
    private void demonstrateCopyOnWritePatterns() {
        printInfo("Copy-on-Write Pattern:");
        
        // Custom copy-on-write implementation concept
        class CopyOnWriteExample<T> {
            private volatile List<T> data = new ArrayList<>();
            
            public void add(T item) {
                synchronized (this) {
                    List<T> newData = new ArrayList<>(data);
                    newData.add(item);
                    data = newData; // Atomic reference update
                }
            }
            
            public List<T> getSnapshot() {
                return data; // No need for synchronization on reads
            }
        }
        
        CopyOnWriteExample<String> cowExample = new CopyOnWriteExample<>();
        cowExample.add("First");
        cowExample.add("Second");
        
        List<String> snapshot = cowExample.getSnapshot();
        printResult("COW snapshot", snapshot);
        
        printTip("Copy-on-write trades write performance for read performance");
    }

    /**
     * Shows lock-based patterns
     */
    private void demonstrateLockBasedPatterns() {
        printInfo("Lock-based Patterns:");
        
        class ThreadSafeCounter {
            private final Map<String, Integer> counters = new HashMap<>();
            private final ReadWriteLock lock = new ReentrantReadWriteLock();
            
            public void increment(String key) {
                lock.writeLock().lock();
                try {
                    counters.put(key, counters.getOrDefault(key, 0) + 1);
                } finally {
                    lock.writeLock().unlock();
                }
            }
            
            public Integer get(String key) {
                lock.readLock().lock();
                try {
                    return counters.get(key);
                } finally {
                    lock.readLock().unlock();
                }
            }
            
            public Map<String, Integer> getSnapshot() {
                lock.readLock().lock();
                try {
                    return new HashMap<>(counters);
                } finally {
                    lock.readLock().unlock();
                }
            }
        }
        
        ThreadSafeCounter counter = new ThreadSafeCounter();
        counter.increment("requests");
        counter.increment("errors");
        counter.increment("requests");
        
        printResult("Counter snapshot", counter.getSnapshot());
        printTip("ReadWriteLock allows multiple concurrent readers");
    }

    /**
     * Demonstrates memory management best practices
     */
    private void demonstrateMemoryManagement() {
        printSubHeader("Memory Management Best Practices");
        
        printInfo("Efficient memory usage patterns for collections");
        
        // Initial capacity sizing
        demonstrateCapacitySizing();
        
        // Memory leak prevention
        demonstrateLeakPrevention();
        
        // Weak references
        demonstrateWeakReferences();
        
        printTip("Always specify initial capacity when size is known");
        printTip("Clear collections when they're no longer needed");
        printWarning("Listeners and callbacks can cause memory leaks");
    }

    /**
     * Shows capacity sizing best practices
     */
    private void demonstrateCapacitySizing() {
        printInfo("Capacity Sizing Patterns:");
        
        // Good practice: Right-sizing
        int expectedSize = 1000;
        List<Integer> rightSized = new ArrayList<>(expectedSize);
        Map<String, Integer> rightSizedMap = new HashMap<>(expectedSize * 4 / 3 + 1);
        Set<String> rightSizedSet = new HashSet<>(expectedSize * 4 / 3 + 1);
        
        // Fill collections
        for (int i = 0; i < expectedSize; i++) {
            rightSized.add(i);
            rightSizedMap.put("key" + i, i);
            rightSizedSet.add("item" + i);
        }
        
        printResult("Right-sized collections created", "Performance optimized");
        
        // Load factor considerations
        printInfo("Load Factor Considerations:");
        @SuppressWarnings("unused")
        Map<String, String> lowLoadFactor = new HashMap<>(100, 0.5f);
        @SuppressWarnings("unused")
        Map<String, String> highLoadFactor = new HashMap<>(100, 0.9f);
        
        printTip("Lower load factors reduce collisions but increase memory usage");
        printTip("Default load factor (0.75) is usually optimal");
    }

    /**
     * Shows memory leak prevention
     */
    private void demonstrateLeakPrevention() {
        printInfo("Memory Leak Prevention:");
        
        // Good practice: Clear collections
        class ResourceManager {
            private final List<String> resources = new ArrayList<>();
            private final Map<String, Object> cache = new HashMap<>();
            
            public void addResource(String resource) {
                resources.add(resource);
            }
            
            public void cleanup() {
                resources.clear();
                cache.clear();
                // Explicitly clear to help GC
            }
        }
        
        ResourceManager manager = new ResourceManager();
        manager.addResource("Resource1");
        manager.addResource("Resource2");
        manager.cleanup();
        
        printResult("Resource manager cleaned up", "Memory released");
        
        // Weak references for caches
        printInfo("Cache with Weak References:");
        Map<String, Object> cache = new WeakHashMap<>();
        cache.put("key1", "This may be garbage collected");
        cache.put("key2", "This too");
        
        printTip("Use WeakHashMap for caches to allow automatic cleanup");
        printWarning("WeakHashMap keys, not values, are weak references");
    }

    /**
     * Shows weak reference patterns
     */
    private void demonstrateWeakReferences() {
        printInfo("Weak Reference Patterns:");
        
        // Observer pattern with weak references
        class WeakObserver {
            private final List<String> observers = new ArrayList<>();
            
            public void addObserver(String observer) {
                observers.add(observer);
            }
            
            public void notifyObservers(String message) {
                observers.forEach(observer -> 
                    System.out.println("  Notifying " + observer + ": " + message));
            }
        }
        
        WeakObserver weakObserver = new WeakObserver();
        weakObserver.addObserver("Observer1");
        weakObserver.addObserver("Observer2");
        weakObserver.notifyObservers("Test message");
        
        printTip("Consider weak references for observer patterns");
        printTip("WeakHashMap automatically removes entries when keys are GC'd");
    }

    /**
     * Demonstrates error handling best practices
     */
    private void demonstrateErrorHandling() {
        printSubHeader("Error Handling Best Practices");
        
        printInfo("Robust error handling patterns for collections");
        
        // Null safety
        demonstrateNullSafety();
        
        // Exception handling
        demonstrateExceptionHandling();
        
        // Validation patterns
        demonstrateValidationPatterns();
        
        printTip("Fail fast with clear error messages");
        printTip("Use Optional for potentially null values");
        printWarning("Empty collections are usually better than null collections");
    }

    /**
     * Shows null safety patterns
     */
    private void demonstrateNullSafety() {
        printInfo("Null Safety Patterns:");
        
        class NullSafeProcessor {
            public List<String> processItems(List<String> items) {
                // Null-safe processing
                return Optional.ofNullable(items)
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(item -> !item.trim().isEmpty())
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
            }
            
            public <T> Collection<T> nullSafe(Collection<T> collection) {
                return collection == null ? Collections.emptyList() : collection;
            }
        }
        
        NullSafeProcessor processor = new NullSafeProcessor();
        
        // Test with null
        List<String> result1 = processor.processItems(null);
        printResult("Processing null input", result1);
        
        // Test with mixed content
        List<String> mixedInput = Arrays.asList("valid", null, "", "  ", "another");
        List<String> result2 = processor.processItems(mixedInput);
        printResult("Processing mixed input", result2);
    }

    /**
     * Shows exception handling patterns
     */
    private void demonstrateExceptionHandling() {
        printInfo("Exception Handling Patterns:");
        
        class SafeCollectionProcessor {
            public <T> Optional<T> safeGet(List<T> list, int index) {
                try {
                    if (list == null || index < 0 || index >= list.size()) {
                        return Optional.empty();
                    }
                    return Optional.ofNullable(list.get(index));
                } catch (Exception e) {
                    System.err.println("Error accessing list: " + e.getMessage());
                    return Optional.empty();
                }
            }
            
            public <K, V> Optional<V> safeMapGet(Map<K, V> map, K key) {
                return Optional.ofNullable(map)
                        .map(m -> m.get(key));
            }
        }
        
        SafeCollectionProcessor processor = new SafeCollectionProcessor();
        List<String> testList = List.of("first", "second", "third");
        
        Optional<String> validAccess = processor.safeGet(testList, 1);
        Optional<String> invalidAccess = processor.safeGet(testList, 10);
        
        printResult("Valid access", validAccess.orElse("Not found"));
        printResult("Invalid access", invalidAccess.orElse("Not found"));
    }

    /**
     * Shows validation patterns
     */
    private void demonstrateValidationPatterns() {
        printInfo("Validation Patterns:");
        
        class ValidationUtils {
            public static <T> void requireNonEmpty(Collection<T> collection, String message) {
                Objects.requireNonNull(collection, message);
                if (collection.isEmpty()) {
                    throw new IllegalArgumentException(message + " cannot be empty");
                }
            }
            
            public static <T> void requireValidIndex(List<T> list, int index) {
                Objects.requireNonNull(list, "List cannot be null");
                if (index < 0 || index >= list.size()) {
                    throw new IndexOutOfBoundsException(
                        String.format("Index %d out of bounds for list of size %d", index, list.size()));
                }
            }
        }
        
        try {
            ValidationUtils.requireNonEmpty(Collections.emptyList(), "Test list");
        } catch (IllegalArgumentException e) {
            printResult("Validation caught empty list", e.getMessage());
        }
        
        try {
            ValidationUtils.requireValidIndex(List.of("a", "b"), 5);
        } catch (IndexOutOfBoundsException e) {
            printResult("Validation caught invalid index", e.getMessage());
        }
    }

    /**
     * Demonstrates performance optimization patterns
     */
    private void demonstratePerformancePatterns() {
        printSubHeader("Performance Optimization Patterns");
        
        printInfo("Patterns for optimal collection performance");
        
        // Bulk operations
        demonstrateBulkOperationPatterns();
        
        // Lazy initialization
        demonstrateLazyInitialization();
        
        // Caching strategies
        demonstrateCachingStrategies();
        
        printTip("Use bulk operations instead of individual operations");
        printTip("Initialize collections lazily when possible");
        printTip("Choose appropriate caching strategies based on access patterns");
    }

    /**
     * Shows bulk operation patterns
     */
    private void demonstrateBulkOperationPatterns() {
        printInfo("Bulk Operation Patterns:");
        
        List<Integer> sourceData = IntStream.range(1, 1000).boxed().toList();
        
        // Efficient bulk operations
        List<Integer> targetList = new ArrayList<>();
        targetList.addAll(sourceData); // Better than individual adds
        
        Set<Integer> targetSet = new HashSet<>();
        targetSet.addAll(sourceData); // Better than individual adds
        
        // Bulk removal
        List<Integer> evenNumbers = new ArrayList<>(sourceData);
        evenNumbers.removeIf(n -> n % 2 != 0); // Better than iterator removal
        
        printResult("Bulk add result size", targetList.size());
        printResult("Bulk set result size", targetSet.size());
        printResult("Bulk filter result size", evenNumbers.size());
        
        printTip("addAll(), removeAll(), and retainAll() are optimized bulk operations");
    }

    /**
     * Shows lazy initialization patterns
     */
    private void demonstrateLazyInitialization() {
        printInfo("Lazy Initialization Patterns:");
        
        class LazyCollectionHolder {
            private List<String> expensiveList;
            private Map<String, Object> expensiveMap;
            
            public List<String> getExpensiveList() {
                if (expensiveList == null) {
                    expensiveList = new ArrayList<>();
                    // Simulate expensive initialization
                    for (int i = 0; i < 100; i++) {
                        expensiveList.add("Item " + i);
                    }
                }
                return expensiveList;
            }
            
            public Map<String, Object> getExpensiveMap() {
                if (expensiveMap == null) {
                    expensiveMap = new HashMap<>();
                    // Simulate expensive initialization
                    for (int i = 0; i < 100; i++) {
                        expensiveMap.put("key" + i, "value" + i);
                    }
                }
                return expensiveMap;
            }
        }
        
        LazyCollectionHolder holder = new LazyCollectionHolder();
        printResult("Holder created", "Collections not yet initialized");
        
        // Collections created on first access
        int listSize = holder.getExpensiveList().size();
        int mapSize = holder.getExpensiveMap().size();
        
        printResult("Lazy list size", listSize);
        printResult("Lazy map size", mapSize);
    }

    /**
     * Shows caching strategies
     */
    private void demonstrateCachingStrategies() {
        printInfo("Caching Strategy Patterns:");
        
        // LRU Cache implementation
        class LRUCache<K, V> extends LinkedHashMap<K, V> {
            private final int maxSize;
            
            public LRUCache(int maxSize) {
                super(16, 0.75f, true); // access-ordered
                this.maxSize = maxSize;
            }
            
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        }
        
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("first", "value1");
        cache.put("second", "value2");
        cache.put("third", "value3");
        cache.put("fourth", "value4"); // Should evict "first"
        
        printResult("LRU Cache after overflow", cache.keySet());
        
        printTip("LinkedHashMap with access-order makes excellent LRU cache");
    }

    /**
     * Demonstrates code maintainability principles
     */
    private void demonstrateCodeMaintainability() {
        printSubHeader("Code Maintainability Principles");
        
        printInfo("Writing maintainable collection-based code");
        
        // Interface-based programming
        demonstrateInterfaceBasedProgramming();
        
        // Generic programming
        demonstrateGenericProgramming();
        
        // Composition over inheritance
        demonstrateComposition();
        
        printTip("Program to interfaces, not implementations");
        printTip("Use generics for type safety and clarity");
        printTip("Favor composition over inheritance");
    }

    /**
     * Shows interface-based programming
     */
    private void demonstrateInterfaceBasedProgramming() {
        printInfo("Interface-based Programming:");
        
        // Good practice: Use interfaces
        class DataProcessorGood {
            public List<String> processData(Collection<String> input) {
                return input.stream()
                        .filter(Objects::nonNull)
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
            }
            
            public void storeResults(Map<String, Object> storage, String key, Object value) {
                storage.put(key, value);
            }
        }
        
        DataProcessorGood processor = new DataProcessorGood();
        
        // Can accept any Collection implementation
        List<String> listResult = processor.processData(List.of("hello", "world"));
        Set<String> setInput = Set.of("java", "collections");
        List<String> setResult = processor.processData(setInput);
        
        printResult("Processed list input", listResult);
        printResult("Processed set input", setResult);
        
        printTip("Using Collection interface allows flexibility in input types");
    }

    /**
     * Shows generic programming best practices
     */
    private void demonstrateGenericProgramming() {
        printInfo("Generic Programming Patterns:");
        
        class GenericProcessor {
            public <T> List<T> filterAndCollect(Collection<T> input, Predicate<T> filter) {
                return input.stream()
                        .filter(filter)
                        .collect(Collectors.toList());
            }
            
            public <K, V> Map<K, V> mergeMaps(Map<K, V> map1, Map<K, V> map2, 
                                              BinaryOperator<V> mergeFunction) {
                Map<K, V> result = new HashMap<>(map1);
                map2.forEach((key, value) -> 
                    result.merge(key, value, mergeFunction));
                return result;
            }
        }
        
        GenericProcessor processor = new GenericProcessor();
        
        // Type-safe operations
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> evenNumbers = processor.filterAndCollect(numbers, n -> n % 2 == 0);
        
        Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
        Map<String, Integer> map2 = Map.of("b", 3, "c", 4);
        Map<String, Integer> merged = processor.mergeMaps(map1, map2, Integer::sum);
        
        printResult("Filtered even numbers", evenNumbers);
        printResult("Merged maps", merged);
    }

    /**
     * Shows composition patterns
     */
    private void demonstrateComposition() {
        printInfo("Composition Patterns:");
        
        // Collection wrapper with additional behavior
        class AuditableList<T> {
            private final List<T> delegate = new ArrayList<>();
            private final List<String> auditLog = new ArrayList<>();
            
            public boolean add(T element) {
                boolean result = delegate.add(element);
                auditLog.add("Added: " + element);
                return result;
            }
            
            public boolean remove(Object element) {
                boolean result = delegate.remove(element);
                if (result) {
                    auditLog.add("Removed: " + element);
                }
                return result;
            }
            
            public List<T> getItems() {
                return new ArrayList<>(delegate);
            }
            
            public List<String> getAuditLog() {
                return new ArrayList<>(auditLog);
            }
        }
        
        AuditableList<String> auditableList = new AuditableList<>();
        auditableList.add("First");
        auditableList.add("Second");
        auditableList.remove("First");
        
        printResult("Auditable list items", auditableList.getItems());
        printResult("Audit log", auditableList.getAuditLog());
        
        printTip("Composition allows adding behavior without modifying existing classes");
    }

    /**
     * Demonstrates common pitfalls and how to avoid them
     */
    private void demonstrateCommonPitfalls() {
        printSubHeader("Common Pitfalls and How to Avoid Them");
        
        printInfo("Learning from common collection mistakes");
        
        // Pitfall 1: Modifying collections during iteration
        demonstrateIterationPitfalls();
        
        // Pitfall 2: Using wrong collection type
        demonstrateCollectionChoicePitfalls();
        
        // Pitfall 3: Memory leaks
        demonstrateMemoryLeakPitfalls();
        
        // Pitfall 4: Performance issues
        demonstratePerformancePitfalls();
        
        printWarning("Always understand the characteristics of your chosen collection");
        printWarning("Be careful when modifying collections during iteration");
        printWarning("Consider thread-safety requirements early in design");
    }

    /**
     * Shows iteration pitfalls
     */
    private void demonstrateIterationPitfalls() {
        printInfo("Iteration Pitfalls:");
        
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        
        // WRONG: Modifying during enhanced for loop
        printWarning("WRONG: Modifying during enhanced for loop (would cause ConcurrentModificationException)");
        
        // CORRECT: Using iterator
        printInfo("CORRECT: Using iterator for safe removal");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if ("B".equals(item)) {
                iterator.remove();
            }
        }
        printResult("After safe removal", list);
        
        // CORRECT: Using removeIf
        List<String> list2 = new ArrayList<>(List.of("A", "B", "C", "D"));
        list2.removeIf("C"::equals);
        printResult("After removeIf", list2);
        
        printTip("Use Iterator.remove() or Collection.removeIf() for safe modification");
    }

    /**
     * Shows collection choice pitfalls
     */
    private void demonstrateCollectionChoicePitfalls() {
        printInfo("Collection Choice Pitfalls:");
        
        // Wrong choice examples
        printWarning("WRONG: Using ArrayList for frequent contains() checks");
        printWarning("WRONG: Using TreeMap when insertion order matters");
        printWarning("WRONG: Using synchronized collections in single-threaded code");
        
        // Correct choices
        printInfo("CORRECT: Using HashSet for frequent contains() checks");
        printInfo("CORRECT: Using LinkedHashMap when insertion order matters");
        printInfo("CORRECT: Using regular collections in single-threaded scenarios");
        
        printTip("Choose collections based on primary operations: access, search, insertion, deletion");
    }

    /**
     * Shows memory leak pitfalls
     */
    private void demonstrateMemoryLeakPitfalls() {
        printInfo("Memory Leak Pitfalls:");
        
        printWarning("PITFALL: Static collections that grow indefinitely");
        printWarning("PITFALL: Listeners not removed from collections");
        printWarning("PITFALL: Caches without eviction policies");
        
        printInfo("SOLUTION: Use weak references, clear collections, implement eviction");
        
        // Example of proper cleanup
        class ProperResourceManager {
            private final List<String> resources = new ArrayList<>();
            
            public void addResource(String resource) {
                resources.add(resource);
            }
            
            public void shutdown() {
                resources.clear(); // Proper cleanup
            }
        }
        
        printTip("Always provide cleanup methods for long-lived objects with collections");
    }

    /**
     * Shows performance pitfalls
     */
    private void demonstratePerformancePitfalls() {
        printInfo("Performance Pitfalls:");
        
        printWarning("PITFALL: Not specifying initial capacity");
        printWarning("PITFALL: Using individual operations instead of bulk operations");
        printWarning("PITFALL: Unnecessary copying of collections");
        
        printInfo("SOLUTION: Size appropriately, use bulk operations, avoid unnecessary copies");
        
        // Good practice example
        int expectedSize = 1000;
        List<Integer> efficientList = new ArrayList<>(expectedSize);
        efficientList.addAll(IntStream.range(0, expectedSize).boxed().toList());
        
        printResult("Efficient list creation", "Proper initial capacity set");
        printTip("Small optimizations in collection usage can have significant performance impact");
    }

    /**
     * Main method for standalone execution
     */
    public static void main(String[] args) {
        new BestPracticesDemo().demonstrateAll();
    }
}