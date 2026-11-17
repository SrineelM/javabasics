package basics.mastery.java.collections.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import basics.mastery.collections.CollectionDemo;

/**
 * ============================================================================
 * JAVA COACH TUTORIAL: CONCURRENT COLLECTIONS MASTERCLASS (JDK 17 - 21)
 * ============================================================================
 * 
 * Target Audience: Intermediate -> Senior -> Staff Java Engineers
 * 
 * Objectives:
 *   1. Understand the evolution from synchronized wrappers to lock-free concurrent structures.
 *   2. Deeply understand ConcurrentHashMap internal mechanics (CAS + Bin-Level locking).
 *   3. Master CopyOnWrite snapshot semantics and memory cost trade-offs.
 *   4. Leverage LongAdder over AtomicLong under high thread contention.
 *   5. Build production patterns: Cache Memoization, Producer-Consumer with Poison Pills.
 *   6. Understand JDK 21 Virtual Threads with concurrent collections.
 * 
 * Concurrency Evolution in Java Collections:
 * 
 *   [Java 1.0 - 1.2] Hashtable / Vector
 *         │  (Every method is synchronized on 'this' -> Bottleneck)
 *         ▼
 *   [Java 1.2] Collections.synchronizedMap() / synchronizedList()
 *         │  (Wrappers locking on a mutex object -> Still a single global lock)
 *         ▼
 *   [Java 5.0] java.util.concurrent (JSR 166: Doug Lea)
 *         │  • ConcurrentHashMap (Segmented Locks: 16 Lock Strips)
 *         │  • CopyOnWriteArrayList (Snapshot iterations)
 *         │  • BlockingQueue variants
 *         ▼
 *   [Java 8+] Modern Concurrent Collections
 *         │  • ConcurrentHashMap refactored: CAS on empty bins + sync on bin head!
 *         │  • Lock-free reads (volatile Node.val)
 *         │  • LongAdder (Striped 64-bit cells to avoid cache coherence contention)
 *         ▼
 *   [Java 21] Virtual Threads (JEP 444)
 *            • Millions of concurrent tasks without thread exhaustion
 * 
 * @author Srineel with Copilot & Expert Java Coach
 * @version 3.0
 * @since Java 17 (Enhanced for Java 21)
 */
public class ConcurrentCollectionsDemo extends CollectionDemo {

    public static void main(String[] args) {
        new ConcurrentCollectionsDemo().demonstrateAll();
    }

    @Override
    public void demonstrateAll() {
        printHeader("CONCURRENT COLLECTIONS: MULTITHREADED MASTERCLASS (JDK 17-21)");

        // 1. ConcurrentHashMap Deep Dive (CAS, Bin Locks, Atomic Operations)
        demonstrateConcurrentHashMap();

        // 2. CopyOnWrite Collections: Snapshot Iteration vs Mutation Overhead
        demonstrateCopyOnWriteCollections();

        // 3. ConcurrentSkipList Collections: Lock-Free Sorted Maps & Sets
        demonstrateConcurrentSkipListCollections();

        // 4. Thread-Safe Counters: AtomicLong vs LongAdder (Striping under Contention)
        demonstrateAtomicVsLongAdderContention();

        // 5. Classic Concurrency Pattern: Producer-Consumer with Poison Pill Shutdown
        demonstrateProducerConsumerWithPoisonPill();

        // 6. Enterprise Pattern: High-Performance Cache Memoization
        demonstrateCachePattern();

        // 7. Modern Java (JDK 21): Virtual Threads with Concurrent Collections
        demonstrateVirtualThreadsWithCollections();

        // 8. Performance Benchmark: ConcurrentHashMap vs Collections.synchronizedMap
        performanceComparison();

        // 9. Best Practices, Pitfalls & Senior Interview Masterclass
        demonstrateBestPracticesAndInterviewQuestions();

        printSectionComplete("Concurrent Collections Masterclass");
    }

    // =========================================================================
    // 1. CONCURRENTHASHMAP: CAS, BIN-LEVEL LOCKING & ATOMIC API
    // =========================================================================
    /**
     * Demonstrates {@link ConcurrentHashMap}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Java 8+ completely discarded Segment[] locks!
     * - Current Structure: Array of Node<K,V>[] table.
     * - Writing to an EMPTY bucket: Uses Lock-Free CAS (Compare-And-Swap).
     * - Writing to an OCCUPIED bucket: Uses `synchronized(firstNode)` on that specific bucket head only!
     *   * Other threads writing to different buckets proceed with ZERO lock contention!
     * - Reads (`get()`): Completely LOCK-FREE. `Node.val` and `Node.next` are declared `volatile`,
     *   ensuring immediate memory visibility across CPU cores (happens-before edge).
     * 
     * [COACH: WHY NULL KEYS & VALUES ARE FORBIDDEN]
     * In a single-threaded HashMap: `map.get(key) == null` could mean:
     *   A) Key was never added, OR
     *   B) Key was added with an explicit value of null.
     * In HashMap, you check `map.containsKey(key)` to disambiguate.
     * IN A MULTITHREADED MAP, checking `containsKey(key)` then calling `get(key)` is NOT atomic!
     * Another thread could remove the key between your two calls. Thus, Doug Lea strictly banned
     * null keys and null values in all concurrent collections.
     */
    private void demonstrateConcurrentHashMap() {
        printSubHeader("1. ConcurrentHashMap - Lock-Free Reads, CAS & Atomic Operations");

        printInfo("ConcurrentHashMap provides thread-safe operations without a global lock.");

        ConcurrentHashMap<String, Integer> activeSessions = new ConcurrentHashMap<>();
        activeSessions.put("US_East", 1200);
        activeSessions.put("EU_West", 850);
        activeSessions.put("AP_South", 640);

        printResult("Initial ConcurrentHashMap", activeSessions);

        // Atomic compound operations:
        // compute: Safely computes value based on key and existing value
        activeSessions.compute("US_East", (region, count) -> count == null ? 100 : count + 300);
        printResult("After compute() on 'US_East' (+300)", activeSessions.get("US_East"));

        // computeIfAbsent: Ideal for thread-safe lazy initialization
        activeSessions.computeIfAbsent("SA_East", region -> 500);
        printResult("After computeIfAbsent('SA_East')", activeSessions.get("SA_East"));

        // merge: Atomic accumulation (safe frequency counter)
        activeSessions.merge("EU_West", 150, Integer::sum);
        printResult("After merge('EU_West', +150)", activeSessions.get("EU_West"));

        // CAS replace (Compare-And-Swap atomic update)
        // Only updates if the current value is EXACTLY 1500
        boolean replaced = activeSessions.replace("US_East", 1500, 1600);
        printResult("Atomic replace (expected=1500, new=1600)", replaced + " -> Value: " + activeSessions.get("US_East"));

        // Parallel Bulk Operations (Java 8+)
        // parallelismThreshold = 1 means parallelize across ForkJoinPool if size >= 1
        long totalSessions = activeSessions.reduceValues(1, Integer::longValue, Long::sum);
        printResult("Parallel reduceValues across all regions", totalSessions);

        // Thread-safe Set from ConcurrentHashMap
        Set<String> threadSafeSet = ConcurrentHashMap.newKeySet();
        threadSafeSet.add("Worker_1");
        threadSafeSet.add("Worker_2");
        printResult("Thread-safe Set via ConcurrentHashMap.newKeySet()", threadSafeSet);

        printTip("[INTERVIEW TIP] ConcurrentHashMap.size() does NOT lock the map; it uses a counter cell array (similar to LongAdder) to aggregate size with minimal contention.");
    }

    // =========================================================================
    // 2. COPYONWRITE COLLECTIONS: SNAPSHOT ITERATION
    // =========================================================================
    /**
     * Demonstrates {@link CopyOnWriteArrayList} and {@link CopyOnWriteArraySet}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Every write operation (`add()`, `set()`, `remove()`) makes a brand new copy of the internal array!
     * - `ReentrantLock` protects the write operations so only one thread clones the array at a time.
     * - Iteration (`iterator()`): Operates on an IMMUTABLE SNAPSHOT array captured at the instant the iterator was created.
     *   * Iterators NEVER throw `ConcurrentModificationException`!
     *   * Iterators do NOT reflect modifications made after the iterator was constructed.
     *   * Iterator's own `remove()` / `add()` methods throw `UnsupportedOperationException`.
     * - Best Use Case: Event listener lists in GUI/networking systems (99.9% reads, rare writes).
     */
    private void demonstrateCopyOnWriteCollections() {
        printSubHeader("2. CopyOnWriteArrayList - Snapshot Iterations & Safe Concurrency");

        printInfo("CopyOnWriteArrayList allows concurrent iteration without locking or ConcurrentModificationException.");

        List<String> eventListeners = new CopyOnWriteArrayList<>(List.of("AuditLogger", "MetricsCollector", "AlertService"));

        // Demonstrate safe concurrent modification during iteration:
        Thread reader = new Thread(() -> {
            System.out.print("     [READER THREAD] Dispatching events to listeners: ");
            for (String listener : eventListeners) {
                System.out.print(listener + " | ");
                try {
                    Thread.sleep(50); // Simulating work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println();
        }, "Reader-Thread");

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(30); // Let reader start
                System.out.println("     [WRITER THREAD] Dynamically registering 'SecuritySentinel'...");
                eventListeners.add("SecuritySentinel");
                System.out.println("     [WRITER THREAD] Registered successfully!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Writer-Thread");

        reader.start();
        writer.start();

        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printResult("Final listeners list (contains all 4)", eventListeners);
        printWarning("[PERFORMANCE CAUTION] Never use CopyOnWriteArrayList for write-heavy workloads! Clones the entire array on every single mutation.");
    }

    // =========================================================================
    // 3. CONCURRENTSKIPLIST: LOCK-FREE SORTED COLLECTIONS
    // =========================================================================
    /**
     * Demonstrates {@link ConcurrentSkipListMap} and {@link ConcurrentSkipListSet}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Why no "ConcurrentTreeMap"?
     *   * Balancing a Red-Black Tree concurrently requires re-coloring and rotating multiple nodes
     *     up the tree, which is notoriously difficult to make lock-free without locking wide swaths of the tree.
     * - The Solution: Skip Lists!
     *   * A probabilistic multi-level linked list.
     *   * Provides O(log n) average time for containsKey, get, put, and remove.
     *   * Can be implemented using lock-free CAS operations on individual node forward pointers!
     */
    private void demonstrateConcurrentSkipListCollections() {
        printSubHeader("3. ConcurrentSkipListMap - Lock-Free Concurrent Sorted Map");

        printInfo("ConcurrentSkipListMap provides thread-safe sorted NavigableMap capabilities via Skip Lists.");

        ConcurrentSkipListMap<Integer, String> tradingOrders = new ConcurrentSkipListMap<>();
        tradingOrders.put(100, "BUY  100 shares AAPL @ $170");
        tradingOrders.put(300, "SELL 50  shares MSFT @ $400");
        tradingOrders.put(200, "BUY  200 shares GOOG @ $150");

        printResult("SkipListMap natural sorted order", tradingOrders);
        printResult("Lowest order ID (firstKey)", tradingOrders.firstKey());
        printResult("Highest order ID (lastKey)", tradingOrders.lastKey());
        printResult("Orders <= 200 (headMap)", tradingOrders.headMap(200, true));

        // ConcurrentSkipListSet
        Set<String> sortedThreadSafeSet = new ConcurrentSkipListSet<>();
        sortedThreadSafeSet.addAll(List.of("Delta", "Alpha", "Charlie", "Bravo"));
        printResult("ConcurrentSkipListSet (alphabetical)", sortedThreadSafeSet);

        printTip("[COACH BEST PRACTICE] Whenever you need a thread-safe TreeMap or TreeSet, use ConcurrentSkipListMap or ConcurrentSkipListSet.");
    }

    // =========================================================================
    // 4. ATOMIC COUNTERS: ATOMICLONG VS LONGADDER
    // =========================================================================
    /**
     * Compares {@link AtomicLong} vs {@link LongAdder}.
     * 
     * [COACH: SENIOR INTERVIEW MUST-KNOW]
     * - `AtomicLong`: Relies on a single `volatile long` updated via a CAS loop (`compareAndSet`).
     *   * Under high concurrency (e.g., 64 threads incrementing simultaneously), threads repeatedly fail CAS
     *     and spin in busy-loops, causing CPU cache-line bouncing (cache coherency storm across CPU cores).
     * - `LongAdder` (Java 8+): Striped 64-bit cells.
     *   * When contention is detected, threads hash to independent `Cell` elements and increment their own cell.
     *   * Summing (`sum()`) aggregates across all cells.
     *   * Drastically outperforms AtomicLong under heavy write contention!
     */
    private void demonstrateAtomicVsLongAdderContention() {
        printSubHeader("4. AtomicLong vs LongAdder - High-Contention Striping");

        printInfo("Testing contention across 8 threads performing 500,000 increments each:");

        final int numThreads = 8;
        final int incrementsPerThread = 200_000;

        // Test A: AtomicLong
        AtomicLong atomicCounter = new AtomicLong(0);
        double atomicTime = measureTime(() -> {
            List<Thread> threads = new ArrayList<>();
            for (int t = 0; t < numThreads; t++) {
                threads.add(new Thread(() -> {
                    for (int i = 0; i < incrementsPerThread; i++) {
                        atomicCounter.incrementAndGet();
                    }
                }));
            }
            threads.forEach(Thread::start);
            for (Thread th : threads) {
                try { th.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        // Test B: LongAdder
        LongAdder adderCounter = new LongAdder();
        double adderTime = measureTime(() -> {
            List<Thread> threads = new ArrayList<>();
            for (int t = 0; t < numThreads; t++) {
                threads.add(new Thread(() -> {
                    for (int i = 0; i < incrementsPerThread; i++) {
                        adderCounter.increment();
                    }
                }));
            }
            threads.forEach(Thread::start);
            for (Thread th : threads) {
                try { th.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        printBenchmark("AtomicLong (" + formatNumber(numThreads * incrementsPerThread) + " increments)", atomicTime);
        printBenchmark("LongAdder  (" + formatNumber(numThreads * incrementsPerThread) + " increments)", adderTime);
        printComparison("Throughput under contention", atomicTime, adderTime, "AtomicLong", "LongAdder");

        printTip("[INTERVIEW TIP] Use AtomicLong when you require atomic compare-and-swap sequences or sequence generators. Use LongAdder for statistics, metrics, and high-frequency counters.");
    }

    // =========================================================================
    // 5. PRODUCER-CONSUMER WITH POISON PILL SHUTDOWN
    // =========================================================================
    /**
     * Demonstrates an enterprise Producer-Consumer pipeline using {@link BlockingQueue}
     * with graceful shutdown via the "Poison Pill" pattern.
     */
    private void demonstrateProducerConsumerWithPoisonPill() {
        printSubHeader("5. Enterprise Pattern: Producer-Consumer with Poison Pill Shutdown");

        printInfo("Poison Pill pattern: A sentinel object placed on the queue signals consumers to terminate cleanly.");

        final String POISON_PILL = "EOF_POISON_PILL";
        BlockingQueue<String> pipeline = new LinkedBlockingQueue<>(10);
        CountDownLatch finishLatch = new CountDownLatch(2); // 2 consumers

        // Producer
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 4; i++) {
                    pipeline.put("Transaction_" + i);
                    Thread.sleep(30);
                }
                // Send poison pill for each consumer to guarantee all terminate
                pipeline.put(POISON_PILL);
                pipeline.put(POISON_PILL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "TransactionProducer");

        // Consumer 1
        Runnable consumerTask = () -> {
            String name = Thread.currentThread().getName();
            try {
                while (true) {
                    String task = pipeline.take();
                    if (task.equals(POISON_PILL)) {
                        System.out.println("     [" + name + "] Received Poison Pill -> Shutting down cleanly.");
                        break;
                    }
                    System.out.println("     [" + name + "] Processed: " + task);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLatch.countDown();
            }
        };

        Thread consumerA = new Thread(consumerTask, "Consumer-1");
        Thread consumerB = new Thread(consumerTask, "Consumer-2");

        producer.start();
        consumerA.start();
        consumerB.start();

        try {
            finishLatch.await(5, TimeUnit.SECONDS);
            producer.join();
            consumerA.join();
            consumerB.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printResult("Pipeline status", "All tasks processed and consumers shut down gracefully.");
    }

    // =========================================================================
    // 6. CACHE MEMOIZATION PATTERN
    // =========================================================================
    /**
     * Demonstrates safe cache memoization with {@link ConcurrentHashMap#computeIfAbsent}.
     */
    private void demonstrateCachePattern() {
        printSubHeader("6. Cache Memoization Pattern (Avoiding Thundering Herd)");

        printInfo("computeIfAbsent ensures the computation function executes AT MOST ONCE per key, even under concurrent requests.");

        ConcurrentHashMap<String, String> computationCache = new ConcurrentHashMap<>();

        // Simulating 3 threads requesting the exact same expensive key simultaneously
        AtomicInteger calculationCalls = new AtomicInteger(0);

        List<Thread> callers = IntStream.range(1, 4).mapToObj(i -> new Thread(() -> {
            String val = computationCache.computeIfAbsent("API_REPORT_2026", key -> {
                calculationCalls.incrementAndGet();
                System.out.println("     [EXPENSIVE CALL] Generating report for: " + key);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return "REPORT_DATA_OK";
            });
            System.out.println("     [CALLER " + i + "] Got result: " + val);
        })).toList();

        callers.forEach(Thread::start);
        for (Thread t : callers) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        printResult("Total expensive calculation executions", calculationCalls.get());
        printTip("[COACH INSIGHT] computeIfAbsent provides built-in cache stampede / thundering herd protection for single-JVM applications.");
    }

    // =========================================================================
    // 7. JDK 21 VIRTUAL THREADS WITH CONCURRENT COLLECTIONS
    // =========================================================================
    /**
     * Demonstrates Virtual Threads (Java 21 JEP 444) working with concurrent collections.
     */
    private void demonstrateVirtualThreadsWithCollections() {
        printSubHeader("7. [JDK 21] Virtual Threads with Concurrent Collections");

        printInfo("Virtual threads allow spawning thousands of concurrent workers without thread pool exhaustion.");

        final int workerCount = 5_000;
        ConcurrentHashMap<String, LongAdder> endpointHits = new ConcurrentHashMap<>();
        endpointHits.put("/api/v1/orders", new LongAdder());
        endpointHits.put("/api/v1/users", new LongAdder());

        double executionTime = measureTime(() -> {
            List<Thread> virtualThreads = new ArrayList<>(workerCount);
            for (int i = 0; i < workerCount; i++) {
                final int idx = i;
                virtualThreads.add(Thread.ofVirtual().start(() -> {
                    String endpoint = (idx % 2 == 0) ? "/api/v1/orders" : "/api/v1/users";
                    endpointHits.get(endpoint).increment();
                }));
            }
            for (Thread vt : virtualThreads) {
                try { vt.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        printResult("Virtual Threads spawned & completed", formatNumber(workerCount));
        printResult("Hits for /api/v1/orders", endpointHits.get("/api/v1/orders").sum());
        printResult("Hits for /api/v1/users", endpointHits.get("/api/v1/users").sum());
        printBenchmark("Time to execute " + workerCount + " virtual threads updating ConcurrentHashMap", executionTime);
    }

    // =========================================================================
    // 8. PERFORMANCE BENCHMARK: CONCURRENT VS SYNCHRONIZED MAP
    // =========================================================================
    /**
     * Compares multi-threaded write performance: ConcurrentHashMap vs Collections.synchronizedMap.
     */
    private void performanceComparison() {
        printSubHeader("8. Performance Benchmark: ConcurrentHashMap vs Collections.synchronizedMap");

        final int totalEntries = 100_000;
        final int threadCount = 4;
        final int entriesPerThread = totalEntries / threadCount;

        printInfo("Benchmarking " + formatNumber(totalEntries) + " writes across " + threadCount + " threads:");

        // Test A: Collections.synchronizedMap (Global Lock Bottleneck)
        Map<Integer, String> syncMap = Collections.synchronizedMap(new HashMap<>());
        double syncMapTime = measureTime(() -> {
            List<Thread> threads = new ArrayList<>();
            for (int t = 0; t < threadCount; t++) {
                final int threadId = t;
                threads.add(new Thread(() -> {
                    for (int i = 0; i < entriesPerThread; i++) {
                        syncMap.put(threadId * entriesPerThread + i, "Val");
                    }
                }));
            }
            threads.forEach(Thread::start);
            for (Thread th : threads) {
                try { th.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        // Test B: ConcurrentHashMap (Bin-level Striping & CAS)
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>(totalEntries);
        double concurrentMapTime = measureTime(() -> {
            List<Thread> threads = new ArrayList<>();
            for (int t = 0; t < threadCount; t++) {
                final int threadId = t;
                threads.add(new Thread(() -> {
                    for (int i = 0; i < entriesPerThread; i++) {
                        concurrentMap.put(threadId * entriesPerThread + i, "Val");
                    }
                }));
            }
            threads.forEach(Thread::start);
            for (Thread th : threads) {
                try { th.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        printBenchmark("Collections.synchronizedMap (Global Lock)", syncMapTime);
        printBenchmark("ConcurrentHashMap (Striped / CAS)", concurrentMapTime);
        printComparison("Throughput comparison", syncMapTime, concurrentMapTime, "SynchronizedMap", "ConcurrentHashMap");
    }

    // =========================================================================
    // 9. BEST PRACTICES & INTERVIEW MASTERCLASS
    // =========================================================================
    private void demonstrateBestPracticesAndInterviewQuestions() {
        printSubHeader("9. Best Practices, Pitfalls & Interview Q&A Masterclass");

        printInfo("======================= TOP INTERVIEW QUESTIONS & ANSWERS =======================");
        System.out.println("""
          Q1: How does ConcurrentHashMap achieve thread safety without locking the entire map?
          A1: In Java 8+, ConcurrentHashMap uses an array of Node<K,V>[]. Inserting into an empty bucket
              uses lock-free CAS (Compare-And-Swap). Inserting into a populated bucket locks ONLY the first
              node of that specific bucket using intrinsic `synchronized(node)`. Reading (get()) is completely
              lock-free because Node.val and Node.next are volatile.

          Q2: Why does ConcurrentHashMap forbid null keys and null values?
          A2: In concurrent environments, returning null from get(key) creates an irresolvable ambiguity:
              did the key not exist, or was the value explicitly mapped to null? Checking containsKey()
              and then calling get() is NOT atomic, as another thread could modify the map in between.

          Q3: When should you use CopyOnWriteArrayList vs Collections.synchronizedList()?
          A3: Use CopyOnWriteArrayList when reads drastically outnumber writes (e.g. 99% reads, event listener
              lists). Iterators are lock-free and never throw ConcurrentModificationException. If writes are
              frequent, CopyOnWriteArrayList is disastrous because it clones the entire array on every mutation.

          Q4: What is the difference between AtomicLong and LongAdder?
          A4: AtomicLong uses a single volatile long with a CAS retry loop. Under high multi-threaded contention,
              threads repeatedly fail CAS and spin, creating CPU cache-coherence traffic. LongAdder maintains
              an array of striped Cell objects, allowing threads to increment distinct cells without contention.

          Q5: How do Virtual Threads in Java 21 change concurrent programming with collections?
          A5: When a Virtual Thread blocks (e.g., on BlockingQueue.take() or wait()), the JVM unmounts it from
              the underlying OS platform carrier thread. The OS thread is immediately free to run other virtual
              threads, drastically increasing throughput for IO-bound and pipelined workloads.
        """);
    }
}
