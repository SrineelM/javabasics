package io.mastery.collections.performance;

import io.mastery.collections.CollectionDemo;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;

/**
 * Comprehensive performance analysis of Java Collections Framework.
 * 
 * This class provides detailed benchmarking, memory analysis, and optimization
 * strategies for different collection types. It demonstrates how to choose
 * the right collection for specific use cases based on performance characteristics.
 * 
 * Key concepts covered:
 * - Time complexity analysis of operations
 * - Memory usage patterns
 * - Performance benchmarking techniques
 * - Optimization strategies
 * - Real-world performance considerations
 * 
 * @author Java Collections Tutorial
 * @since Java 21
 */
public class PerformanceAnalysisDemo extends CollectionDemo {

    private static final int SMALL_SIZE = 1_000;
    private static final int MEDIUM_SIZE = 100_000;
    private static final int LARGE_SIZE = 1_000_000;

    /**
     * Demonstrates all performance analysis examples
     */
    public void demonstrateAll() {
        printHeader("Performance Analysis Demo");
        
        demonstrateListPerformance();
        demonstrateSetPerformance();
        demonstrateMapPerformance();
        demonstrateMemoryUsage();
        demonstrateOptimizationStrategies();
        demonstrateBulkOperations();
        demonstrateConcurrentCollectionPerformance();
        
        printSeparator();
    }

    /**
     * Comprehensive performance analysis of List implementations
     */
    private void demonstrateListPerformance() {
        printSubHeader("List Performance Analysis");
        
        printInfo("Comparing ArrayList vs LinkedList performance characteristics");
        
        // Test data preparation
        List<Integer> testData = IntStream.range(0, MEDIUM_SIZE)
                .boxed()
                .toList();
        
        // ArrayList performance tests
        printInfo("ArrayList Performance:");
        var arrayList = new ArrayList<Integer>();
        benchmarkListOperations("ArrayList", arrayList, testData);
        
        // LinkedList performance tests
        printInfo("LinkedList Performance:");
        var linkedList = new LinkedList<Integer>();
        benchmarkListOperations("LinkedList", linkedList, testData);
        
        // Vector performance tests (synchronized)
        printInfo("Vector Performance (synchronized):");
        var vector = new Vector<Integer>();
        benchmarkListOperations("Vector", vector, testData);
        
        printTip("ArrayList: O(1) random access, O(n) insertion/deletion in middle");
        printTip("LinkedList: O(1) insertion/deletion at ends, O(n) random access");
        printWarning("Vector is legacy - use ArrayList with Collections.synchronizedList() instead");
    }

    /**
     * Benchmarks common list operations
     */
    private void benchmarkListOperations(String listType, List<Integer> list, List<Integer> testData) {
        System.out.println("  " + listType + " Operations:");
        
        // Add operations
        double addTime = measureTime(() -> {
            for (int i = 0; i < SMALL_SIZE; i++) {
                list.add(i);
            }
        });
        System.out.printf("    Add %d elements: %s%n", SMALL_SIZE, formatTime(addTime));
        
        // Random access
        double accessTime = measureTime(() -> {
            Random random = new Random(42); // Fixed seed for consistency
            for (int i = 0; i < 1000; i++) {
                @SuppressWarnings("unused")
                int value = list.get(random.nextInt(list.size()));
            }
        });
        System.out.printf("    Random access 1000 times: %s%n", formatTime(accessTime));
        
        // Insert at beginning
        double insertTime = measureTime(() -> {
            for (int i = 0; i < 100; i++) {
                list.add(0, -i);
            }
        });
        System.out.printf("    Insert 100 elements at beginning: %s%n", formatTime(insertTime));
        
        // Contains operation
        double containsTime = measureTime(() -> {
            for (int i = 0; i < 100; i++) {
                @SuppressWarnings("unused")
                boolean exists = list.contains(i);
            }
        });
        System.out.printf("    Contains check 100 times: %s%n", formatTime(containsTime));
        
        list.clear();
        System.out.println();
    }

    /**
     * Performance analysis of Set implementations
     */
    private void demonstrateSetPerformance() {
        printSubHeader("Set Performance Analysis");
        
        printInfo("Comparing HashSet vs TreeSet vs LinkedHashSet performance");
        
        List<String> testStrings = generateTestStrings(MEDIUM_SIZE);
        
        // HashSet performance
        printInfo("HashSet Performance:");
        benchmarkSetOperations("HashSet", new HashSet<>(), testStrings);
        
        // TreeSet performance
        printInfo("TreeSet Performance:");
        benchmarkSetOperations("TreeSet", new TreeSet<>(), testStrings);
        
        // LinkedHashSet performance
        printInfo("LinkedHashSet Performance:");
        benchmarkSetOperations("LinkedHashSet", new LinkedHashSet<>(), testStrings);
        
        printTip("HashSet: O(1) average for add/remove/contains");
        printTip("TreeSet: O(log n) for add/remove/contains, maintains sorted order");
        printTip("LinkedHashSet: O(1) average operations + insertion order maintenance");
    }

    /**
     * Benchmarks common set operations
     */
    private void benchmarkSetOperations(String setType, Set<String> set, List<String> testData) {
        System.out.println("  " + setType + " Operations:");
        
        // Add operations
        double addTime = measureTime(() -> {
            for (int i = 0; i < SMALL_SIZE; i++) {
                set.add(testData.get(i));
            }
        });
        System.out.printf("    Add %d elements: %s%n", SMALL_SIZE, formatTime(addTime));
        
        // Contains operations
        double containsTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                @SuppressWarnings("unused")
                boolean exists = set.contains(testData.get(i % SMALL_SIZE));
            }
        });
        System.out.printf("    Contains check 1000 times: %s%n", formatTime(containsTime));
        
        // Remove operations
        double removeTime = measureTime(() -> {
            for (int i = 0; i < 100; i++) {
                set.remove(testData.get(i));
            }
        });
        System.out.printf("    Remove 100 elements: %s%n", formatTime(removeTime));
        
        set.clear();
        System.out.println();
    }

    /**
     * Performance analysis of Map implementations
     */
    private void demonstrateMapPerformance() {
        printSubHeader("Map Performance Analysis");
        
        printInfo("Comparing HashMap vs TreeMap vs LinkedHashMap performance");
        
        List<String> keys = generateTestStrings(MEDIUM_SIZE);
        List<Integer> values = IntStream.range(0, MEDIUM_SIZE).boxed().toList();
        
        // HashMap performance
        printInfo("HashMap Performance:");
        benchmarkMapOperations("HashMap", new HashMap<>(), keys, values);
        
        // TreeMap performance
        printInfo("TreeMap Performance:");
        benchmarkMapOperations("TreeMap", new TreeMap<>(), keys, values);
        
        // LinkedHashMap performance
        printInfo("LinkedHashMap Performance:");
        benchmarkMapOperations("LinkedHashMap", new LinkedHashMap<>(), keys, values);
        
        // ConcurrentHashMap performance (single-threaded)
        printInfo("ConcurrentHashMap Performance (single-threaded):");
        benchmarkMapOperations("ConcurrentHashMap", new ConcurrentHashMap<>(), keys, values);
        
        printTip("HashMap: O(1) average performance, not thread-safe");
        printTip("TreeMap: O(log n) performance, maintains sorted key order");
        printTip("ConcurrentHashMap: Slightly slower than HashMap but thread-safe");
    }

    /**
     * Benchmarks common map operations
     */
    private void benchmarkMapOperations(String mapType, Map<String, Integer> map, 
                                       List<String> keys, List<Integer> values) {
        System.out.println("  " + mapType + " Operations:");
        
        // Put operations
        double putTime = measureTime(() -> {
            for (int i = 0; i < SMALL_SIZE; i++) {
                map.put(keys.get(i), values.get(i));
            }
        });
        System.out.printf("    Put %d entries: %s%n", SMALL_SIZE, formatTime(putTime));
        
        // Get operations
        double getTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                @SuppressWarnings("unused")
                Integer value = map.get(keys.get(i % SMALL_SIZE));
            }
        });
        System.out.printf("    Get 1000 times: %s%n", formatTime(getTime));
        
        // ContainsKey operations
        double containsTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                @SuppressWarnings("unused")
                boolean exists = map.containsKey(keys.get(i % SMALL_SIZE));
            }
        });
        System.out.printf("    ContainsKey 1000 times: %s%n", formatTime(containsTime));
        
        map.clear();
        System.out.println();
    }

    /**
     * Demonstrates memory usage analysis
     */
    private void demonstrateMemoryUsage() {
        printSubHeader("Memory Usage Analysis");
        
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        printInfo("Comparing memory footprint of different collections");
        
        // Memory usage before creating collections
        long beforeMemory = getUsedMemory(memoryBean);
        
        // ArrayList memory usage
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < LARGE_SIZE; i++) {
            arrayList.add(i);
        }
        long arrayListMemory = getUsedMemory(memoryBean) - beforeMemory;
        printResult("ArrayList memory usage", formatMemory(arrayListMemory));
        
        // Clear and reset
        arrayList.clear();
        arrayList = null;
        System.gc();
        beforeMemory = getUsedMemory(memoryBean);
        
        // LinkedList memory usage
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < LARGE_SIZE; i++) {
            linkedList.add(i);
        }
        long linkedListMemory = getUsedMemory(memoryBean) - beforeMemory;
        printResult("LinkedList memory usage", formatMemory(linkedListMemory));
        
        // Clear and reset
        linkedList.clear();
        linkedList = null;
        System.gc();
        beforeMemory = getUsedMemory(memoryBean);
        
        // HashSet memory usage
        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < LARGE_SIZE; i++) {
            hashSet.add(i);
        }
        long hashSetMemory = getUsedMemory(memoryBean) - beforeMemory;
        printResult("HashSet memory usage", formatMemory(hashSetMemory));
        
        printTip("ArrayList has lower memory overhead per element than LinkedList");
        printTip("HashSet has additional overhead for hash table maintenance");
        printWarning("Memory measurements are approximate and JVM-dependent");
    }

    /**
     * Demonstrates various optimization strategies
     */
    private void demonstrateOptimizationStrategies() {
        printSubHeader("Optimization Strategies");
        
        printInfo("Collection optimization techniques");
        
        // Initial capacity optimization
        demonstrateInitialCapacityOptimization();
        
        // Bulk operations optimization
        demonstrateBulkOperationOptimization();
        
        // Collection choice optimization
        demonstrateCollectionChoiceOptimization();
        
        printTip("Always specify initial capacity when size is known");
        printTip("Use bulk operations for better performance");
        printTip("Choose collections based on access patterns");
    }

    /**
     * Shows the impact of setting initial capacity
     */
    private void demonstrateInitialCapacityOptimization() {
        printInfo("Initial Capacity Impact:");
        
        // Without initial capacity
        double timeWithoutCapacity = measureTime(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < LARGE_SIZE; i++) {
                list.add(i);
            }
        });
        
        // With initial capacity
        double timeWithCapacity = measureTime(() -> {
            List<Integer> list = new ArrayList<>(LARGE_SIZE);
            for (int i = 0; i < LARGE_SIZE; i++) {
                list.add(i);
            }
        });
        
        System.out.printf("  Without initial capacity: %s%n", formatTime(timeWithoutCapacity));
        System.out.printf("  With initial capacity: %s%n", formatTime(timeWithCapacity));
        System.out.printf("  Improvement: %.2fx%n", (double) timeWithoutCapacity / timeWithCapacity);
    }

    /**
     * Demonstrates bulk operation performance benefits
     */
    private void demonstrateBulkOperationOptimization() {
        printInfo("Bulk Operations vs Individual Operations:");
        
        List<Integer> sourceList = IntStream.range(0, LARGE_SIZE).boxed().toList();
        
        // Individual add operations
        double individualTime = measureTime(() -> {
            List<Integer> target = new ArrayList<>();
            for (Integer item : sourceList) {
                target.add(item);
            }
        });
        
        // Bulk add operation
        double bulkTime = measureTime(() -> {
            List<Integer> target = new ArrayList<>();
            target.addAll(sourceList);
        });
        
        System.out.printf("  Individual add operations: %s%n", formatTime(individualTime));
        System.out.printf("  Bulk addAll operation: %s%n", formatTime(bulkTime));
        System.out.printf("  Improvement: %.2fx%n", (double) individualTime / bulkTime);
    }

    /**
     * Shows performance differences based on collection choice
     */
    private void demonstrateCollectionChoiceOptimization() {
        printInfo("Collection Choice for Frequent Contains Operations:");
        
        List<String> testData = generateTestStrings(SMALL_SIZE);
        
        // ArrayList contains performance
        List<String> arrayList = new ArrayList<>(testData);
        double listContainsTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                @SuppressWarnings("unused")
                boolean found = arrayList.contains(testData.get(i % 100));
            }
        });
        
        // HashSet contains performance
        Set<String> hashSet = new HashSet<>(testData);
        double setContainsTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                @SuppressWarnings("unused")
                boolean found = hashSet.contains(testData.get(i % 100));
            }
        });
        
        System.out.printf("  ArrayList contains (1000 ops): %s%n", formatTime(listContainsTime));
        System.out.printf("  HashSet contains (1000 ops): %s%n", formatTime(setContainsTime));
        System.out.printf("  HashSet is %.2fx faster%n", (double) listContainsTime / setContainsTime);
    }

    /**
     * Demonstrates bulk operations performance
     */
    private void demonstrateBulkOperations() {
        printSubHeader("Bulk Operations Performance");
        
        printInfo("Comparing bulk vs individual operations");
        
        List<Integer> sourceData = IntStream.range(0, MEDIUM_SIZE).boxed().toList();
        Set<Integer> filterSet = new HashSet<>(IntStream.range(0, MEDIUM_SIZE / 2).boxed().toList());
        
        // Individual operations
        double individualTime = measureTime(() -> {
            List<Integer> result = new ArrayList<>();
            for (Integer item : sourceData) {
                if (!filterSet.contains(item)) {
                    result.add(item);
                }
            }
        });
        
        // Stream bulk operations
        double streamTime = measureTime(() -> {
            @SuppressWarnings("unused")
            List<Integer> result = sourceData.stream()
                    .filter(item -> !filterSet.contains(item))
                    .toList();
        });
        
        // Parallel stream operations
        double parallelTime = measureTime(() -> {
            @SuppressWarnings("unused")
            List<Integer> result = sourceData.parallelStream()
                    .filter(item -> !filterSet.contains(item))
                    .toList();
        });
        
        System.out.printf("  Individual operations: %s%n", formatTime(individualTime));
        System.out.printf("  Stream operations: %s%n", formatTime(streamTime));
        System.out.printf("  Parallel stream: %s%n", formatTime(parallelTime));
        
        printTip("Parallel streams can improve performance for CPU-intensive operations");
        printWarning("Parallel streams have overhead - use for large datasets only");
    }

    /**
     * Performance analysis of concurrent collections
     */
    private void demonstrateConcurrentCollectionPerformance() {
        printSubHeader("Concurrent Collection Performance");
        
        printInfo("Comparing thread-safe vs non-thread-safe collections under concurrent access");
        
        int numThreads = Runtime.getRuntime().availableProcessors();
        int operationsPerThread = 10_000;
        
        // Test ConcurrentHashMap vs synchronized HashMap
        compareConcurrentMapPerformance(numThreads, operationsPerThread);
        
        // Test CopyOnWriteArrayList vs synchronized ArrayList
        compareConcurrentListPerformance(numThreads, operationsPerThread);
        
        printTip("Use concurrent collections for high-contention scenarios");
        printTip("ConcurrentHashMap allows concurrent reads and writes");
        printWarning("CopyOnWriteArrayList is best for read-heavy workloads");
    }

    /**
     * Compares concurrent map performance
     */
    private void compareConcurrentMapPerformance(int numThreads, int operationsPerThread) {
        printInfo("Map Concurrent Access Comparison:");
        
        // ConcurrentHashMap test
        ConcurrentHashMap<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        long concurrentTime = measureConcurrentMapOperations(concurrentMap, numThreads, operationsPerThread);
        
        // Synchronized HashMap test
        Map<Integer, String> syncMap = Collections.synchronizedMap(new HashMap<>());
        long syncTime = measureConcurrentMapOperations(syncMap, numThreads, operationsPerThread);
        
        System.out.printf("  ConcurrentHashMap (%d threads): %s%n", numThreads, formatTime(concurrentTime));
        System.out.printf("  Synchronized HashMap (%d threads): %s%n", numThreads, formatTime(syncTime));
        System.out.printf("  ConcurrentHashMap is %.2fx faster%n", (double) syncTime / concurrentTime);
    }

    /**
     * Measures concurrent map operations
     */
    private long measureConcurrentMapOperations(Map<Integer, String> map, int numThreads, int operationsPerThread) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        int key = threadId * operationsPerThread + j;
                        map.put(key, "Value" + key);
                        @SuppressWarnings("unused")
                        String value = map.get(key);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.nanoTime();
        executor.shutdown();
        
        return endTime - startTime;
    }

    /**
     * Compares concurrent list performance
     */
    private void compareConcurrentListPerformance(int numThreads, int operationsPerThread) {
        printInfo("List Concurrent Access Comparison:");
        
        // CopyOnWriteArrayList test (read operations)
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 1000; i++) {
            cowList.add("Initial" + i);
        }
        long cowTime = measureConcurrentListReadOperations(cowList, numThreads, operationsPerThread);
        
        // Synchronized ArrayList test (read operations)
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 1000; i++) {
            syncList.add("Initial" + i);
        }
        long syncListTime = measureConcurrentListReadOperations(syncList, numThreads, operationsPerThread);
        
        System.out.printf("  CopyOnWriteArrayList reads (%d threads): %s%n", numThreads, formatTime(cowTime));
        System.out.printf("  Synchronized ArrayList reads (%d threads): %s%n", numThreads, formatTime(syncListTime));
        System.out.printf("  CopyOnWriteArrayList is %.2fx faster for reads%n", (double) syncListTime / cowTime);
    }

    /**
     * Measures concurrent list read operations
     */
    private long measureConcurrentListReadOperations(List<String> list, int numThreads, int operationsPerThread) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    Random random = new Random();
                    for (int j = 0; j < operationsPerThread; j++) {
                        int index = random.nextInt(list.size());
                        @SuppressWarnings("unused")
                        String value = list.get(index);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.nanoTime();
        executor.shutdown();
        
        return endTime - startTime;
    }

    // Helper methods
    
    /**
     * Generates test strings for performance testing
     */
    private List<String> generateTestStrings(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> "TestString" + i + "_" + (i % 1000))
                .toList();
    }

    /**
     * Gets used memory from MemoryMXBean
     */
    private long getUsedMemory(MemoryMXBean memoryBean) {
        return memoryBean.getHeapMemoryUsage().getUsed();
    }

    /**
     * Formats memory size in human-readable format
     */
    private String formatMemory(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * Formats time in human-readable format
     */
    private String formatTime(double millis) {
        if (millis < 1.0) return String.format("%.1f μs", millis * 1000);
        if (millis < 1000.0) return String.format("%.1f ms", millis);
        return String.format("%.2f s", millis / 1000.0);
    }

    /**
     * Main method for standalone execution
     */
    public static void main(String[] args) {
        new PerformanceAnalysisDemo().demonstrateAll();
    }
}