package basics.mastery.java.collections.fundamentals;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SequencedCollection;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import basics.mastery.collections.CollectionDemo;

/**
 * ============================================================================
 * JAVA COACH TUTORIAL: QUEUE & DEQUE COLLECTIONS MASTERCLASS (JDK 17 - 21)
 * ============================================================================
 * 
 * Target Audience: Beginner -> Intermediate -> Senior Java Engineers
 * 
 * Objectives:
 *   1. Understand the contract difference between Queue (FIFO) and Deque (Double-Ended).
 *   2. Master the dual API design: Exception-throwing vs Value-returning.
 *   3. Understand internal data structures (Circular Array vs Heap vs Doubly-Linked List).
 *   4. Grasp concurrency patterns with BlockingQueue and Concurrent queues.
 *   5. Explore JDK 21 SequencedCollection integration and Virtual Threads producer-consumer.
 * 
 * Queue Hierarchy at a glance:
 * 
 *                      Collection<E>
 *                           │
 *                       Queue<E> ───────────────── SequencedCollection<E> (JDK 21)
 *                      /    │   \                          │
 *                     /     │    \                         │
 *          BlockingQueue<E> │     Deque<E> ────────────────┘
 *          /     \          │      /      \
 *  ArrayBlocking  Linked    │  ArrayDeque  LinkedList
 *     Queue      Blocking   │
 *                 Queue     │
 *                           │
 *                      PriorityQueue<E>
 * 
 * @author Srineel with Copilot & Expert Java Coach
 * @version 3.0
 * @since Java 17 (Enhanced for Java 21)
 */
public class QueueCollectionsDemo extends CollectionDemo {

    public static void main(String[] args) {
        new QueueCollectionsDemo().demonstrateAll();
    }

    @Override
    public void demonstrateAll() {
        printHeader("QUEUE & DEQUE COLLECTIONS: MASTERCLASS (JDK 17-21)");

        // 1. Fundamentals: ArrayDeque (Queue & Stack) & The Dual API
        demonstrateArrayDeque();

        // 2. LinkedList as Queue & Architectural Trade-offs
        demonstrateLinkedListAsQueue();

        // 3. PriorityQueue & Binary Heap Mechanics
        demonstratePriorityQueue();

        // 4. Concurrency: BlockingQueues in Producer-Consumer Architectures
        demonstrateBlockingQueues();
        demonstrateConcurrentQueues();

        // 5. Classic Patterns: BFS Graph Traversal & Priority Task Scheduling
        demonstrateQueuePatterns();

        // 6. Modern Java (JDK 21): SequencedCollection & Virtual Threads
        demonstrateJava21Features();

        // 7. Performance Benchmarking: ArrayDeque vs LinkedList vs PriorityQueue
        performanceComparison();

        // 8. Best Practices, Anti-patterns & Interview Masterclass
        demonstrateBestPracticesAndInterviewQuestions();

        printSectionComplete("Queue & Deque Collections Masterclass");
    }

    // =========================================================================
    // 1. ARRAYDEQUE: CIRCULAR ARRAY, QUEUE & STACK OPERATIONS
    // =========================================================================
    /**
     * Demonstrates {@link ArrayDeque}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Backed by a circular resizable array `Object[] elements` with head and tail pointers.
     * - Powers of two capacity: wrap-around uses fast bitwise `(tail + 1) & (elements.length - 1)`.
     * - Forbids null elements (throws NullPointerException).
     * - Why ArrayDeque replaces Stack:
     *   * `java.util.Stack` extends `Vector`. Every method is synchronized (massive thread contention overhead).
     *   * `ArrayDeque` is unsynchronized, allocates no node objects, and is significantly faster!
     * 
     * [COACH: DUAL API CONTRACT]
     * ---------------------------------------------------------
     * Operation   Throws Exception      Returns Special Value
     * ---------------------------------------------------------
     * Insert      add(e) / addLast(e)   offer(e) / offerLast(e) [returns false if bounded/full]
     * Remove      remove() / removeFirst() poll() / pollFirst()   [returns null if empty]
     * Examine     element() / getFirst()   peek() / peekFirst()   [returns null if empty]
     * ---------------------------------------------------------
     */
    private void demonstrateArrayDeque() {
        printSubHeader("1. ArrayDeque - FIFO Queue, LIFO Stack & The Dual API");

        printInfo("ArrayDeque is the recommended default for both Queue and Stack in single-threaded contexts.");

        Deque<String> deque = new ArrayDeque<>();

        // Part A: Queue Semantics (FIFO - First-In, First-Out)
        printInfo("A. Queue Semantics (FIFO: offer at tail, poll from head):");
        deque.offer("Customer_1");
        deque.offer("Customer_2");
        deque.offer("Customer_3");
        printResult("Queue after offering 3 elements", deque);

        printResult("peek() [inspect head without removing]", deque.peek());
        printResult("poll() [removes head Customer_1]", deque.poll());
        printResult("Queue after poll", deque);

        // Part B: Stack Semantics (LIFO - Last-In, First-Out)
        printInfo("B. Stack Semantics (LIFO: push onto top, pop from top):");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Page_1");
        stack.push("Page_2");
        stack.push("Page_3 (Current)");
        printResult("Stack after pushes", stack);

        printResult("pop() [removes top Page_3]", stack.pop());
        printResult("peek() [new top is Page_2]", stack.peek());
        printResult("Stack after pop", stack);

        // Part C: Double-Ended Queue (Add/Remove from both ends)
        printInfo("C. Double-Ended Queue (Both ends access):");
        Deque<String> doubleEnded = new ArrayDeque<>();
        doubleEnded.addFirst("Front_B");
        doubleEnded.addFirst("Front_A");
        doubleEnded.addLast("Back_Y");
        doubleEnded.addLast("Back_Z");
        printResult("Deque with elements at both ends", doubleEnded);

        printResult("removeFirst()", doubleEnded.removeFirst());
        printResult("removeLast()", doubleEnded.removeLast());
        printResult("Remaining Deque", doubleEnded);

        // Safe vs Exception demonstration
        Deque<String> emptyQueue = new ArrayDeque<>();
        printResult("Safe poll() on empty queue (returns null)", emptyQueue.poll());
        try {
            emptyQueue.remove(); // Throws!
        } catch (NoSuchElementException e) {
            printInfo("Exception API: remove() on empty queue threw NoSuchElementException.");
        }

        printTip("[COACH BEST PRACTICE] Always use offer()/poll()/peek() in queues instead of add()/remove()/element().");
    }

    // =========================================================================
    // 2. LINKEDLIST AS QUEUE: CACHE LOCALITY & MEMORY TRADEOFFS
    // =========================================================================
    /**
     * Demonstrates {@link LinkedList} as a Queue.
     * 
     * [COACH: ARCHITECTURAL COMPARISON]
     * - LinkedList implements both List and Deque.
     * - Node overhead: Every node in LinkedList is a heap object with 3 pointers (item, next, prev)
     *   consuming 24-32 bytes of heap PER element!
     * - Cache Locality: Nodes are scattered randomly in heap memory, causing CPU L1/L2/L3 cache misses.
     * - ArrayDeque stores elements in contiguous memory: vastly superior CPU cache line utilization!
     */
    private void demonstrateLinkedListAsQueue() {
        printSubHeader("2. LinkedList as Queue - Trade-offs vs ArrayDeque");

        printInfo("LinkedList implements Deque using a doubly-linked list of Node objects.");

        Queue<String> linkedQueue = new LinkedList<>();
        linkedQueue.offer("Job_Alpha");
        linkedQueue.offer("Job_Beta");
        linkedQueue.offer("Job_Gamma");

        printResult("LinkedList queue", linkedQueue);
        printResult("Polled from LinkedList", linkedQueue.poll());

        printWarning("[PERFORMANCE CAUTION] In almost every production benchmark, ArrayDeque outperforms LinkedList for queues and stacks.");
        printTip("[USE CASE] Use LinkedList only when you require positional List indexing AND Deque operations on the exact same instance.");
    }

    // =========================================================================
    // 3. PRIORITYQUEUE: MIN-HEAP & CUSTOM PRIORITY
    // =========================================================================
    /**
     * Demonstrates {@link PriorityQueue}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Backed by a balanced binary min-heap stored in an array (`Object[] queue`).
     * - Children of index `i` are at `2*i + 1` and `2*i + 2`. Parent is at `(i - 1) / 2`.
     * - Insertion: O(log n) - siftUp().
     * - Removal (poll): O(log n) - root is removed, last leaf moved to root, siftDown().
     * - Peek: O(1) - returns `queue[0]`.
     * - CRITICAL: Iteration order of PriorityQueue is NOT sorted! Only successive poll() calls return sorted order.
     */
    private void demonstratePriorityQueue() {
        printSubHeader("3. PriorityQueue - Binary Heap & Priority Scheduling");

        printInfo("PriorityQueue orders elements based on natural order (min-heap) or custom Comparator.");

        // Part A: Natural ordering (Integers -> Min-Heap)
        Queue<Integer> minHeap = new PriorityQueue<>();
        minHeap.addAll(List.of(45, 12, 89, 3, 27, 6));
        printResult("PriorityQueue internal array order (NOT fully sorted!)", minHeap);

        System.out.print("     Polling in prioritized order (Min to Max): ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        // Part B: Max-Heap using Collections.reverseOrder()
        Queue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.addAll(List.of(45, 12, 89, 3, 27, 6));
        printResult("Max-Heap root (peek)", maxHeap.peek()); // 89

        // Part C: Real-World Task Scheduling with Records
        record ServerTask(String taskId, int priorityScore, String description) implements Comparable<ServerTask> {
            @Override
            public int compareTo(ServerTask other) {
                // Higher score = higher priority
                return Integer.compare(other.priorityScore, this.priorityScore);
            }
        }

        Queue<ServerTask> scheduler = new PriorityQueue<>();
        scheduler.offer(new ServerTask("TASK_101", 1, "Background index cleanup"));
        scheduler.offer(new ServerTask("TASK_999", 10, "EMERGENCY: Database failover"));
        scheduler.offer(new ServerTask("TASK_205", 5, "Send invoice notifications"));

        printInfo("Dispatching server tasks in priority order:");
        while (!scheduler.isEmpty()) {
            ServerTask task = scheduler.poll();
            System.out.println("     [DISPATCHING] Priority " + task.priorityScore() + " -> " + task.taskId() + ": " + task.description());
        }

        printWarning("[PITFALL] PriorityQueue is NOT thread-safe and does NOT allow null elements.");
    }

    // =========================================================================
    // 4. BLOCKING QUEUES: PRODUCER-CONSUMER CONCURRENCY
    // =========================================================================
    /**
     * Demonstrates {@link BlockingQueue} variants:
     * - {@link ArrayBlockingQueue}: Bounded, array-backed, single lock.
     * - {@link LinkedBlockingQueue}: Optionally bounded, node-backed, two separate locks (`takeLock` & `putLock`).
     * - {@link SynchronousQueue}: Zero-capacity rendezvous channel!
     */
    private void demonstrateBlockingQueues() {
        printSubHeader("4. BlockingQueue - Thread-Safe Concurrency & Producer-Consumer");

        printInfo("BlockingQueue blocks put() when full, and blocks take() when empty.");

        // A. ArrayBlockingQueue: Bounded with fixed capacity
        BlockingQueue<String> boundedQueue = new ArrayBlockingQueue<>(2);

        try {
            boundedQueue.put("Payload_A");
            boundedQueue.put("Payload_B");
            printResult("Bounded queue at capacity (2/2)", boundedQueue);

            // offer with timeout: doesn't block indefinitely
            boolean accepted = boundedQueue.offer("Payload_C", 100, TimeUnit.MILLISECONDS);
            printResult("offer('Payload_C', 100ms) when full", accepted ? "Accepted" : "Rejected (Timeout)");

            // take(): consumes an element
            String consumed = boundedQueue.take();
            printResult("take() retrieved", consumed);
            printResult("Queue after take()", boundedQueue);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // B. LinkedBlockingQueue: Producer-Consumer Simulation
        printInfo("Producer-Consumer pattern with LinkedBlockingQueue:");
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    sharedQueue.put(i);
                    System.out.println("     [PRODUCER] Put item " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer-Thread");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    Integer item = sharedQueue.take();
                    System.out.println("     [CONSUMER] Consumed item " + item);
                    Thread.sleep(70);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer-Thread");

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // C. SynchronousQueue: Zero Capacity Hand-off
        printInfo("SynchronousQueue: Has ZERO storage capacity. Each put() must wait for a take().");
        BlockingQueue<String> rendezvousQueue = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("     [HAND-OFF] Receiver taking item...");
                String item = rendezvousQueue.take();
                System.out.println("     [HAND-OFF] Received: " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        try {
            rendezvousQueue.put("DIRECT_HANDOFF_DATA");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printTip("[INTERVIEW TIP] LinkedBlockingQueue often has higher throughput than ArrayBlockingQueue because it uses two separate locks (putLock and takeLock), allowing producers and consumers to operate concurrently.");
    }

    // =========================================================================
    // 5. CONCURRENT LINKED QUEUES: LOCK-FREE CAS
    // =========================================================================
    /**
     * Demonstrates {@link ConcurrentLinkedQueue} and {@link ConcurrentLinkedDeque}.
     * 
     * [COACH: ADVANCED CONCURRENCY]
     * - Uses the Michael-Scott non-blocking queue algorithm.
     * - Entirely lock-free! Uses Compare-And-Swap (CAS) via VarHandles / Unsafe.
     * - size() is NOT constant-time: it traverses the entire linked list (O(n)).
     *   * In Java 8+, use isEmpty() to check for elements, NEVER size() == 0!
     */
    private void demonstrateConcurrentQueues() {
        printSubHeader("5. Lock-Free Concurrent Queues (CAS Mechanics)");

        printInfo("ConcurrentLinkedQueue uses lock-free Compare-And-Swap (CAS). No synchronized blocks.");

        Queue<String> lockFreeQueue = new ConcurrentLinkedQueue<>();
        lockFreeQueue.offer("Event_1");
        lockFreeQueue.offer("Event_2");
        lockFreeQueue.offer("Event_3");

        printResult("ConcurrentLinkedQueue contents", lockFreeQueue);
        printResult("Poll from lock-free queue", lockFreeQueue.poll());

        printTip("[PERFORMANCE TIP] Never call queue.size() == 0 on ConcurrentLinkedQueue in a loop. It is O(n)! Always call queue.isEmpty() which is O(1).");
    }

    // =========================================================================
    // 6. CLASSIC QUEUE PATTERNS: BFS & TASK SCHEDULING
    // =========================================================================
    /**
     * Demonstrates classic algorithmic and enterprise patterns using Queues.
     */
    private void demonstrateQueuePatterns() {
        printSubHeader("6. Classic Algorithmic Patterns: Breadth-First Search (BFS)");

        printInfo("Queues are the standard data structure for Breadth-First Search (BFS) on trees and graphs.");

        // Graph represented as Adjacency List:
        Map<String, List<String>> socialNetwork = Map.of(
            "Alice", List.of("Bob", "Charlie"),
            "Bob", List.of("David"),
            "Charlie", List.of("Eve", "Frank"),
            "David", List.of(),
            "Eve", List.of(),
            "Frank", List.of()
        );

        Queue<String> bfsQueue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        bfsQueue.offer("Alice");
        visited.add("Alice");

        System.out.print("     BFS Traversal Order: ");
        while (!bfsQueue.isEmpty()) {
            String person = bfsQueue.poll();
            System.out.print(person + " -> ");

            for (String friend : socialNetwork.getOrDefault(person, List.of())) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    bfsQueue.offer(friend);
                }
            }
        }
        System.out.println("DONE");
    }

    // =========================================================================
    // 7. JDK 21 FEATURES: SEQUENCED COLLECTIONS & VIRTUAL THREADS
    // =========================================================================
    /**
     * Demonstrates Java 21 SequencedCollection and Virtual Threads with Queues.
     */
    private void demonstrateJava21Features() {
        printSubHeader("7. [JDK 21] Sequenced Collections & Virtual Threads Integration");

        // A. SequencedCollection interface (Deque implements SequencedCollection)
        printInfo("A. In Java 21, Deque implements SequencedCollection:");
        Deque<String> pipeline = new ArrayDeque<>(List.of("Stage_1", "Stage_2", "Stage_3"));

        // Direct first and last access
        printResult("getFirst()", pipeline.getFirst());
        printResult("getLast()", pipeline.getLast());

        // Live reversed view without cloning:
        SequencedCollection<String> reversed = pipeline.reversed();
        printResult("pipeline.reversed() view", reversed);

        // B. Virtual Threads with BlockingQueue
        printInfo("B. Virtual Threads (JEP 444) in high-throughput Producer-Consumer:");
        BlockingQueue<String> vQueue = new LinkedBlockingQueue<>(10);

        // Virtual threads are extremely lightweight; blocking on a queue unmounts the virtual thread
        // from its underlying carrier platform thread, leaving the OS thread free to do other work!
        Thread vProducer = Thread.ofVirtual().name("vThread-Producer").start(() -> {
            try {
                vQueue.put("Payload from Virtual Thread");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        try {
            vProducer.join();
            String item = vQueue.take();
            printResult("Consumed from Virtual Thread producer", item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printTip("[COACH: JDK 21] Virtual threads turn blocking operations (queue.take(), socket read) into non-blocking JVM scheduling events!");
    }

    // =========================================================================
    // 8. PERFORMANCE BENCHMARKING ANALYSIS
    // =========================================================================
    /**
     * Compares ArrayDeque vs LinkedList vs PriorityQueue for 100,000 operations.
     */
    private void performanceComparison() {
        printSubHeader("8. Performance Benchmarking Analysis");

        final int ops = 100_000;
        printInfo("Benchmarking " + formatNumber(ops) + " offer() and poll() operations:");

        // ArrayDeque
        double arrayDequeTime = measureTime(() -> {
            Queue<Integer> q = new ArrayDeque<>(ops);
            for (int i = 0; i < ops; i++) q.offer(i);
            for (int i = 0; i < ops; i++) q.poll();
        });

        // LinkedList
        double linkedListTime = measureTime(() -> {
            Queue<Integer> q = new LinkedList<>();
            for (int i = 0; i < ops; i++) q.offer(i);
            for (int i = 0; i < ops; i++) q.poll();
        });

        // PriorityQueue
        double priorityQueueTime = measureTime(() -> {
            Queue<Integer> q = new PriorityQueue<>(ops);
            for (int i = 0; i < ops; i++) q.offer(i);
            for (int i = 0; i < ops; i++) q.poll();
        });

        printBenchmark("ArrayDeque offer + poll", arrayDequeTime);
        printBenchmark("LinkedList offer + poll", linkedListTime);
        printBenchmark("PriorityQueue offer + poll (with heap sifting)", priorityQueueTime);

        printComparison("ArrayDeque vs LinkedList", linkedListTime, arrayDequeTime, "LinkedList", "ArrayDeque");
    }

    // =========================================================================
    // 9. BEST PRACTICES & INTERVIEW MASTERCLASS
    // =========================================================================
    private void demonstrateBestPracticesAndInterviewQuestions() {
        printSubHeader("9. Best Practices, Pitfalls & Interview Q&A Masterclass");

        printInfo("======================= TOP INTERVIEW QUESTIONS & ANSWERS =======================");
        System.out.println("""
          Q1: Why should you use ArrayDeque instead of java.util.Stack in modern Java?
          A1: `java.util.Stack` extends `Vector`, meaning every method (push, pop, peek) acquires a lock
              on `this` (synchronized). This introduces unnecessary thread contention. ArrayDeque is
              unsynchronized, uses an array with no node overhead, and has superior CPU cache locality.

          Q2: What is the difference between add() and offer(), and remove() and poll()?
          A2: • add() and remove() belong to the Exception-throwing API: add() throws IllegalStateException
                if a bounded queue is full, and remove() throws NoSuchElementException if empty.
              • offer() and poll() belong to the Special-Value API: offer() returns false if full, and poll()
                returns null if empty. In robust production code, offer() and poll() are strongly preferred.

          Q3: How does ArrayBlockingQueue differ from LinkedBlockingQueue?
          A3: • ArrayBlockingQueue: Bounded by a fixed-size array. Uses a SINGLE ReentrantLock for both
                producers and consumers.
              • LinkedBlockingQueue: Optionally bounded node-based queue. Uses TWO independent ReentrantLocks
                (`putLock` and `takeLock`), allowing concurrent puts and takes, yielding higher throughput.

          Q4: Does iterating over a PriorityQueue return elements in sorted priority order?
          A4: NO! The iterator traverses the underlying binary heap array (`queue[0...n-1]`), which satisfies
              heap invariants but is NOT sorted. To retrieve elements in priority order, you must repeatedly
              call poll().

          Q5: What is a SynchronousQueue and where is it used?
          A5: A SynchronousQueue has zero capacity. A put() operation blocks until another thread calls take(),
              and vice-versa (a direct handoff). It is heavily used in Executors.newCachedThreadPool().
        """);
    }
}
