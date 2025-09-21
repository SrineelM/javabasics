package io.mastery.collections.concurrent;

import io.mastery.collections.CollectionDemo;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of thread-safe collection implementations.
 * 
 * This class provides interactive examples covering:
 * - ConcurrentHashMap: High-performance thread-safe map
 * - CopyOnWriteArrayList: Thread-safe list optimized for reads
 * - CopyOnWriteArraySet: Thread-safe set optimized for reads
 * - BlockingQueue implementations: Thread-safe queues with blocking operations
 * - ConcurrentSkipListMap/Set: Thread-safe sorted collections
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class ConcurrentCollectionsDemo extends CollectionDemo {
    
    @Override
    public void demonstrateAll() {
        printHeader("CONCURRENT COLLECTIONS COMPREHENSIVE GUIDE");
        
        // Core concurrent collections
        demonstrateConcurrentHashMap();
        demonstrateCopyOnWriteCollections();
        demonstrateBlockingQueues();
        demonstrateConcurrentSkipListCollections();
        
        // Performance analysis
        performanceComparison();
        
        // Concurrent patterns and synchronization
        demonstrateConcurrentPatterns();
        demonstrateAtomicOperations();
        
        // Best practices
        demonstrateBestPractices();
        
        printSectionComplete("Concurrent Collections");
    }
    
    /**
     * Demonstrates ConcurrentHashMap - high-performance thread-safe map
     */
    private void demonstrateConcurrentHashMap() {
        printSubHeader("ConcurrentHashMap - High-Performance Thread-Safe Map");
        
        printInfo("ConcurrentHashMap provides thread-safe operations without external synchronization");
        printInfo("Best for: High-concurrency scenarios with frequent reads and writes");
        
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        
        // Basic thread-safe operations
        concurrentMap.put("users", 1000);
        concurrentMap.put("sessions", 150);
        concurrentMap.put("requests", 5000);
        
        printResult("Initial concurrent map", concurrentMap);
        
        // Atomic computation operations
        printInfo("Atomic computation operations:");
        
        // compute - always computes new value
        concurrentMap.compute("users", (key, val) -> val != null ? val + 50 : 50);
        printResult("After compute (users + 50)", concurrentMap.get("users"));
        
        // computeIfAbsent - computes only if key is absent
        concurrentMap.computeIfAbsent("newMetric", key -> 42);
        printResult("After computeIfAbsent", concurrentMap.get("newMetric"));
        
        // computeIfPresent - computes only if key is present
        concurrentMap.computeIfPresent("sessions", (key, val) -> val * 2);
        printResult("After computeIfPresent (sessions * 2)", concurrentMap.get("sessions"));
        
        // merge - combines existing value with new value
        concurrentMap.merge("requests", 1000, Integer::sum);
        printResult("After merge (requests + 1000)", concurrentMap.get("requests"));
        
        // Atomic replace operations
        boolean replaced = concurrentMap.replace("users", 1050, 1100);
        printResult("Replace 1050 with 1100", replaced + " (users: " + concurrentMap.get("users") + ")");
        
        // Bulk parallel operations
        printInfo("Parallel bulk operations:");
        
        // forEach with parallelism threshold
        System.out.print("  Parallel forEach: ");
        concurrentMap.forEach(1, (key, value) -> System.out.print(key + "=" + value + " "));
        System.out.println();
        
        // Parallel search
        String foundKey = concurrentMap.search(1, (key, value) -> 
            value > 200 ? key : null);
        printResult("Search for value > 200", foundKey);
        
        // Parallel reduce
        Integer sum = concurrentMap.reduceValues(1, Integer.class::cast, Integer::sum);
        printResult("Sum of all values", sum);
        
        // Thread-safe key set as concurrent set
        Set<String> keySet = concurrentMap.keySet();
        keySet.add("newKey"); // Adds to original map
        printResult("KeySet as concurrent set", keySet);
        printResult("Original map after keySet.add", concurrentMap);
        
        printTip("ConcurrentHashMap operations are atomic and thread-safe");
        printInfo("Use parallelismThreshold = 1 for parallel operations on any size");
    }
    
    /**
     * Demonstrates CopyOnWrite collections
     */
    private void demonstrateCopyOnWriteCollections() {
        printSubHeader("CopyOnWrite Collections - Read-Optimized Thread-Safe Collections");
        
        printInfo("CopyOnWrite collections are optimized for scenarios with many reads, few writes");
        printInfo("Best for: Event listener lists, configuration data, read-heavy scenarios");
        
        // CopyOnWriteArrayList
        printInfo("CopyOnWriteArrayList demonstration:");
        List<String> cowList = new CopyOnWriteArrayList<>();
        cowList.addAll(List.of("Java", "Python", "JavaScript"));
        
        printResult("Initial CopyOnWriteArrayList", cowList);
        
        // Safe iteration during modification
        printInfo("Safe iteration during concurrent modification:");
        Thread iterator = new Thread(() -> {
            System.out.print("  Iterator thread: ");
            for (String lang : cowList) {
                System.out.print(lang + " ");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println();
        });
        
        Thread modifier = new Thread(() -> {
            try {
                Thread.sleep(50);
                cowList.add("Kotlin");
                System.out.println("  Modifier thread: Added Kotlin");
                Thread.sleep(100);
                cowList.add("Scala");
                System.out.println("  Modifier thread: Added Scala");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        iterator.start();
        modifier.start();
        
        try {
            iterator.join();
            modifier.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        printResult("Final CopyOnWriteArrayList", cowList);
        
        // CopyOnWriteArraySet
        printInfo("CopyOnWriteArraySet demonstration:");
        Set<Integer> cowSet = new CopyOnWriteArraySet<>();
        cowSet.addAll(List.of(1, 2, 3, 2, 4, 1)); // Duplicates ignored
        printResult("CopyOnWriteArraySet", cowSet);
        
        // Memory usage warning demonstration
        printInfo("Memory usage characteristics:");
        List<String> normalList = new ArrayList<>();
        List<String> cowList2 = new CopyOnWriteArrayList<>();
        
        // Add elements to both
        for (int i = 0; i < 1000; i++) {
            normalList.add("Item " + i);
            cowList2.add("Item " + i);
        }
        
        printResult("Normal list size", normalList.size());
        printResult("CopyOnWrite list size", cowList2.size());
        
        printWarning("CopyOnWrite collections use more memory due to copying on writes");
        printTip("Use CopyOnWrite collections for read-heavy, write-light scenarios");
    }
    
    /**
     * Demonstrates various BlockingQueue implementations
     */
    private void demonstrateBlockingQueues() {
        printSubHeader("BlockingQueue Implementations - Thread-Safe Queues with Blocking");
        
        printInfo("BlockingQueues provide thread-safe operations with blocking capabilities");
        printInfo("Best for: Producer-consumer patterns, thread pools, work distribution");
        
        // ArrayBlockingQueue - bounded
        printInfo("ArrayBlockingQueue - Bounded blocking queue:");
        BlockingQueue<String> arrayQueue = new ArrayBlockingQueue<>(3);
        
        try {
            arrayQueue.put("Item1");
            arrayQueue.put("Item2");
            arrayQueue.put("Item3");
            printResult("Full ArrayBlockingQueue", arrayQueue);
            
            // Demonstrate blocking behavior with timeout
            boolean added = arrayQueue.offer("Item4", 100, TimeUnit.MILLISECONDS);
            printResult("Offer with 100ms timeout", added);
            
            String taken = arrayQueue.take();
            printResult("Take from queue", taken);
            printResult("Queue after take", arrayQueue);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // LinkedBlockingQueue - optionally bounded
        printInfo("LinkedBlockingQueue - Optionally bounded:");
        BlockingQueue<Integer> linkedQueue = new LinkedBlockingQueue<>(5);
        
        // Producer-consumer with multiple threads
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    linkedQueue.put(i);
                    produced.incrementAndGet();
                    System.out.println("  Produced: " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                while (consumed.get() < 5) {
                    Integer item = linkedQueue.take();
                    consumed.incrementAndGet();
                    System.out.println("  Consumed: " + item);
                    Thread.sleep(75);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        printResult("Total produced", produced.get());
        printResult("Total consumed", consumed.get());
        
        // SynchronousQueue - no storage capacity
        printInfo("SynchronousQueue - Zero capacity, direct handoff:");
        BlockingQueue<String> syncQueue = new SynchronousQueue<>();
        
        Thread sender = new Thread(() -> {
            try {
                System.out.println("  Sender: Putting item...");
                syncQueue.put("HandoffItem");
                System.out.println("  Sender: Item handed off successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread receiver = new Thread(() -> {
            try {
                Thread.sleep(100); // Small delay
                System.out.println("  Receiver: Taking item...");
                String item = syncQueue.take();
                System.out.println("  Receiver: Received " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        sender.start();
        receiver.start();
        
        try {
            sender.join();
            receiver.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        printTip("Choose BlockingQueue implementation based on capacity and performance needs");
        printInfo("BlockingQueues are fundamental for implementing thread-safe producer-consumer patterns");
    }
    
    /**
     * Demonstrates ConcurrentSkipList collections
     */
    private void demonstrateConcurrentSkipListCollections() {
        printSubHeader("ConcurrentSkipList Collections - Thread-Safe Sorted Collections");
        
        printInfo("ConcurrentSkipList collections provide thread-safe sorted access");
        printInfo("Best for: Concurrent scenarios requiring sorted data with frequent updates");
        
        // ConcurrentSkipListMap
        printInfo("ConcurrentSkipListMap - Thread-safe sorted map:");
        ConcurrentSkipListMap<Integer, String> skipMap = new ConcurrentSkipListMap<>();
        
        // Add elements concurrently
        List<Thread> writers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            writers.add(new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    int key = threadId * 10 + j;
                    skipMap.put(key, "Value" + key);
                }
            }));
        }
        
        writers.forEach(Thread::start);
        writers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        printResult("ConcurrentSkipListMap (sorted)", skipMap);
        
        // NavigableMap operations
        printResult("First key", skipMap.firstKey());
        printResult("Last key", skipMap.lastKey());
        printResult("Keys < 15", skipMap.headMap(15).keySet());
        printResult("Keys >= 15", skipMap.tailMap(15).keySet());
        
        // ConcurrentSkipListSet
        printInfo("ConcurrentSkipListSet - Thread-safe sorted set:");
        ConcurrentSkipListSet<Integer> skipSet = new ConcurrentSkipListSet<>();
        
        // Concurrent additions
        skipSet.addAll(List.of(5, 2, 8, 1, 9, 3, 7, 4, 6));
        printResult("ConcurrentSkipListSet", skipSet);
        
        printResult("Lower than 5", skipSet.lower(5));
        printResult("Higher than 5", skipSet.higher(5));
        printResult("Ceiling of 5", skipSet.ceiling(5));
        printResult("Floor of 5", skipSet.floor(5));
        
        // Subset operations
        printResult("SubSet [3, 7)", skipSet.subSet(3, 7));
        printResult("HeadSet < 5", skipSet.headSet(5));
        printResult("TailSet >= 6", skipSet.tailSet(6));
        
        printTip("Use ConcurrentSkipList collections when you need thread-safe sorted access");
        printInfo("Skip lists provide O(log n) performance for most operations");
    }
    
    /**
     * Performance comparison between concurrent collections
     */
    private void performanceComparison() {
        printSubHeader("Performance Comparison Analysis");
        
        final int elements = 10000;
        final int threads = 4;
        
        printInfo("Comparing concurrent collections with " + formatNumber(elements) + 
                 " elements using " + threads + " threads");
        
        // ConcurrentHashMap vs Collections.synchronizedMap
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        Map<Integer, String> syncMap = Collections.synchronizedMap(new HashMap<>());
        
        // ConcurrentHashMap performance
        double concurrentMapTime = measureTime(() -> {
            List<Thread> threadList = new ArrayList<>();
            for (int t = 0; t < threads; t++) {
                final int threadId = t;
                threadList.add(new Thread(() -> {
                    for (int i = threadId; i < elements; i += threads) {
                        concurrentMap.put(i, "Value" + i);
                    }
                }));
            }
            
            threadList.forEach(Thread::start);
            threadList.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        
        // Synchronized map performance
        double syncMapTime = measureTime(() -> {
            List<Thread> threadList = new ArrayList<>();
            for (int t = 0; t < threads; t++) {
                final int threadId = t;
                threadList.add(new Thread(() -> {
                    for (int i = threadId; i < elements; i += threads) {
                        syncMap.put(i, "Value" + i);
                    }
                }));
            }
            
            threadList.forEach(Thread::start);
            threadList.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        
        printBenchmark("ConcurrentHashMap parallel writes", concurrentMapTime);
        printBenchmark("Synchronized HashMap parallel writes", syncMapTime);
        
        printComparison("Concurrent vs Synchronized Map",
                       syncMapTime, concurrentMapTime,
                       "Synchronized", "Concurrent");
    }
    
    /**
     * Demonstrates concurrent programming patterns with collections
     */
    private void demonstrateConcurrentPatterns() {
        printSubHeader("Concurrent Programming Patterns");
        
        // Producer-Consumer pattern with BlockingQueue
        printInfo("Producer-Consumer pattern:");
        BlockingQueue<String> workQueue = new LinkedBlockingQueue<>(10);
        AtomicInteger taskCount = new AtomicInteger(0);
        
        // Multiple producers
        List<Thread> producers = IntStream.range(0, 2)
                .mapToObj(i -> new Thread(() -> {
                    try {
                        for (int j = 0; j < 3; j++) {
                            String task = "Task-P" + i + "-" + j;
                            workQueue.put(task);
                            taskCount.incrementAndGet();
                            System.out.println("  Produced: " + task);
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }))
                .toList();
        
        // Multiple consumers
        List<Thread> consumers = IntStream.range(0, 2)
                .mapToObj(i -> new Thread(() -> {
                    try {
                        while (taskCount.get() > 0 || !workQueue.isEmpty()) {
                            String task = workQueue.poll(100, TimeUnit.MILLISECONDS);
                            if (task != null) {
                                System.out.println("  Consumer " + i + " processed: " + task);
                                taskCount.decrementAndGet();
                                Thread.sleep(75);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }))
                .toList();
        
        producers.forEach(Thread::start);
        consumers.forEach(Thread::start);
        
        try {
            for (Thread t : producers) t.join();
            for (Thread t : consumers) t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Cache pattern with ConcurrentHashMap
        printInfo("Cache pattern with ConcurrentHashMap:");
        ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
        
        // Simulate cache operations
        String getValue = cache.computeIfAbsent("key1", key -> {
            System.out.println("  Computing value for " + key);
            return "computed-" + key;
        });
        printResult("First access (computed)", getValue);
        
        String getCachedValue = cache.computeIfAbsent("key1", key -> {
            System.out.println("  This won't be called - value exists");
            return "should-not-see-this";
        });
        printResult("Second access (cached)", getCachedValue);
        
        printTip("Use BlockingQueue for producer-consumer, ConcurrentHashMap for caching");
        printInfo("These patterns form the foundation of concurrent application design");
    }
    
    /**
     * Demonstrates atomic operations and thread-safe counters
     */
    private void demonstrateAtomicOperations() {
        printSubHeader("Atomic Operations and Thread-Safe Counters");
        
        printInfo("ConcurrentHashMap provides atomic operations for thread-safe updates");
        
        ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
        
        // Initialize counters
        counters.put("requests", new AtomicInteger(0));
        counters.put("errors", new AtomicInteger(0));
        counters.put("successes", new AtomicInteger(0));
        
        // Simulate concurrent counter updates
        List<Thread> workers = IntStream.range(0, 4)
                .mapToObj(i -> new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        counters.get("requests").incrementAndGet();
                        
                        // Simulate some errors
                        if (j % 10 == 0) {
                            counters.get("errors").incrementAndGet();
                        } else {
                            counters.get("successes").incrementAndGet();
                        }
                    }
                }))
                .toList();
        
        workers.forEach(Thread::start);
        workers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        printResult("Total requests", counters.get("requests").get());
        printResult("Total errors", counters.get("errors").get());
        printResult("Total successes", counters.get("successes").get());
        
        // Using merge for atomic counter updates
        printInfo("Using merge for atomic updates:");
        ConcurrentHashMap<String, Integer> simpleCounts = new ConcurrentHashMap<>();
        
        List<Thread> mergeWorkers = IntStream.range(0, 3)
                .mapToObj(i -> new Thread(() -> {
                    for (int j = 0; j < 50; j++) {
                        simpleCounts.merge("counter", 1, Integer::sum);
                    }
                }))
                .toList();
        
        mergeWorkers.forEach(Thread::start);
        mergeWorkers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        printResult("Merged counter value", simpleCounts.get("counter"));
        
        printTip("Use AtomicInteger for simple counters, merge() for more complex atomic updates");
        printInfo("Atomic operations eliminate the need for explicit synchronization");
    }
    
    /**
     * Demonstrates best practices for concurrent collections
     */
    private void demonstrateBestPractices() {
        printSubHeader("Best Practices for Concurrent Collections");
        
        printInfo("🔧 Concurrent Collection Selection Guide:");
        System.out.println("  • ConcurrentHashMap: High-concurrency key-value access");
        System.out.println("  • CopyOnWriteArrayList: Read-heavy, write-light scenarios");
        System.out.println("  • BlockingQueue: Producer-consumer patterns");
        System.out.println("  • ConcurrentSkipList: Thread-safe sorted collections");
        System.out.println("  • Collections.synchronized*: Legacy thread-safety");
        
        printSeparator();
        
        printInfo("⚡ Performance Best Practices:");
        System.out.println("  • Use ConcurrentHashMap over Collections.synchronizedMap()");
        System.out.println("  • Size collections appropriately to minimize resizing");
        System.out.println("  • Use parallelismThreshold wisely in bulk operations");
        System.out.println("  • Avoid CopyOnWrite for write-heavy scenarios");
        System.out.println("  • Choose blocking vs non-blocking based on requirements");
        
        printSeparator();
        
        printInfo("🚨 Common Pitfalls to Avoid:");
        System.out.println("  • Using synchronized wrappers instead of concurrent collections");
        System.out.println("  • Not handling InterruptedException properly");
        System.out.println("  • Assuming iteration order in concurrent collections");
        System.out.println("  • Modifying collections during iteration without proper synchronization");
        System.out.println("  • Using CopyOnWrite collections for frequent writes");
        
        // Demonstrate proper exception handling
        printInfo("Proper InterruptedException handling:");
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        
        Thread interruptibleWorker = new Thread(() -> {
            try {
                String item = queue.take(); // Blocking operation
                System.out.println("  Processed: " + item);
            } catch (InterruptedException e) {
                System.out.println("  Worker interrupted - cleaning up");
                Thread.currentThread().interrupt(); // Restore interrupt status
                return;
            }
        });
        
        interruptibleWorker.start();
        
        try {
            Thread.sleep(100);
            interruptibleWorker.interrupt(); // Interrupt the worker
            interruptibleWorker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Demonstrate atomic operations pattern
        printInfo("Atomic operations pattern:");
        ConcurrentHashMap<String, Long> timestamps = new ConcurrentHashMap<>();
        
        // Atomic timestamp update
        String key = "lastAccess";
        long currentTime = System.currentTimeMillis();
        
        // Race-condition free update
        timestamps.compute(key, (k, v) -> currentTime);
        printResult("Atomic timestamp update", timestamps.get(key));
        
        // Compare-and-swap pattern
        long expectedValue = timestamps.get(key);
        long newValue = currentTime + 1000;
        boolean updated = timestamps.replace(key, expectedValue, newValue);
        printResult("Compare-and-swap result", updated);
        
        printTip("Always restore interrupt status when catching InterruptedException");
        printWarning("Test concurrent code thoroughly - race conditions can be subtle");
    }
}