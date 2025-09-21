package basics.mastery.advanced;.collections.fundamentals;

import io.mastery.collections.CollectionDemo;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of Queue and Deque collection implementations.
 * 
 * This class provides interactive examples covering:
 * - ArrayDeque: Resizable array implementation of Deque interface
 * - LinkedList: Doubly-linked list implementation (also implements Deque)
 * - PriorityQueue: Heap-based priority queue implementation
 * - BlockingQueue implementations: Thread-safe queues for concurrent scenarios
 * - Concurrent queue implementations for high-performance scenarios
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class QueueCollectionsDemo extends CollectionDemo {
    
    @Override
    public void demonstrateAll() {
        printHeader("QUEUE & DEQUE COLLECTIONS COMPREHENSIVE GUIDE");
        
        // Basic Queue implementations
        demonstrateArrayDeque();
        demonstrateLinkedListAsQueue();
        demonstratePriorityQueue();
        
        // Blocking queues for concurrent scenarios
        demonstrateBlockingQueues();
        demonstrateConcurrentQueues();
        
        // Performance analysis
        performanceComparison();
        
        // Advanced patterns and use cases
        demonstrateQueuePatterns();
        demonstrateModernFeatures();
        
        // Best practices
        demonstrateBestPractices();
        
        printSectionComplete("Queue & Deque Collections");
    }
    
    /**
     * Demonstrates ArrayDeque - the preferred Deque implementation
     */
    private void demonstrateArrayDeque() {
        printSubHeader("ArrayDeque - Resizable Array Deque Implementation");
        
        printInfo("ArrayDeque is the preferred implementation for both Queue and Deque operations");
        printInfo("Best for: General-purpose queue/deque operations, better than LinkedList");
        
        Deque<String> deque = new ArrayDeque<>();
        
        // Queue operations (FIFO)
        printInfo("Queue operations (FIFO - First In, First Out):");
        deque.offer("First");
        deque.offer("Second");
        deque.offer("Third");
        printResult("After offering elements", deque);
        
        printResult("Poll (remove head)", deque.poll());
        printResult("Peek (view head)", deque.peek());
        printResult("Queue after poll", deque);
        
        // Stack operations (LIFO)
        printInfo("Stack operations (LIFO - Last In, First Out):");
        deque.push("Top");
        deque.push("New Top");
        printResult("After pushing elements", deque);
        
        printResult("Pop (remove top)", deque.pop());
        printResult("Peek top", deque.peek());
        printResult("Stack after pop", deque);
        
        // Deque-specific operations (both ends)
        printInfo("Deque operations (both ends access):");
        deque.clear();
        deque.addFirst("A");
        deque.addLast("B");
        deque.addFirst("Start");
        deque.addLast("End");
        printResult("After adding to both ends", deque);
        
        printResult("Remove first", deque.removeFirst());
        printResult("Remove last", deque.removeLast());
        printResult("Final deque", deque);
        
        // Safe operations (return null instead of throwing exceptions)
        Deque<String> emptyDeque = new ArrayDeque<>();
        printResult("Poll from empty deque", emptyDeque.poll()); // Returns null
        printResult("Peek empty deque", emptyDeque.peek()); // Returns null
        
        try {
            emptyDeque.remove(); // Throws exception
        } catch (NoSuchElementException e) {
            printInfo("remove() throws NoSuchElementException on empty deque");
        }
        
        printTip("Use ArrayDeque instead of Stack class and LinkedList for queue operations");
        printInfo("ArrayDeque is faster than LinkedList for queue operations");
    }
    
    /**
     * Demonstrates LinkedList as a Queue implementation
     */
    private void demonstrateLinkedListAsQueue() {
        printSubHeader("LinkedList - Doubly-Linked List as Queue/Deque");
        
        printInfo("LinkedList implements both List and Deque interfaces");
        printInfo("Use case: When you need both list and queue operations on same data structure");
        
        LinkedList<Integer> linkedQueue = new LinkedList<>();
        
        // Queue operations
        linkedQueue.offer(10);
        linkedQueue.offer(20);
        linkedQueue.offer(30);
        printResult("LinkedList as Queue", linkedQueue);
        
        // List operations on the same object
        linkedQueue.add(1, 15); // Insert at index 1
        printResult("After list insertion", linkedQueue);
        
        printResult("Get element at index 2", linkedQueue.get(2));
        printResult("Index of 20", linkedQueue.indexOf(20));
        
        // Queue and List operations combined
        printResult("Poll from queue", linkedQueue.poll());
        linkedQueue.set(0, 99); // Replace first element
        printResult("Final state", linkedQueue);
        
        printWarning("LinkedList has worse cache performance than ArrayDeque");
        printTip("Use LinkedList only when you need both List and Queue operations");
    }
    
    /**
     * Demonstrates PriorityQueue - heap-based priority queue
     */
    private void demonstratePriorityQueue() {
        printSubHeader("PriorityQueue - Heap-Based Priority Queue");
        
        printInfo("PriorityQueue orders elements based on natural ordering or provided Comparator");
        printInfo("Best for: When you need elements processed in priority order");
        
        // Natural ordering (min-heap for integers)
        Queue<Integer> naturalPQ = new PriorityQueue<>();
        naturalPQ.addAll(List.of(5, 2, 8, 1, 9, 3));
        printResult("PriorityQueue (natural order)", naturalPQ);
        
        printInfo("Polling elements (always gets minimum):");
        while (!naturalPQ.isEmpty()) {
            System.out.println("  Polled: " + naturalPQ.poll() + ", Remaining: " + naturalPQ);
        }
        
        // Custom priority with Comparator (max-heap)
        Queue<Integer> maxPQ = new PriorityQueue<>(Collections.reverseOrder());
        maxPQ.addAll(List.of(5, 2, 8, 1, 9, 3));
        printResult("Max PriorityQueue", maxPQ);
        
        printInfo("Polling from max queue:");
        System.out.println("  First poll (max): " + maxPQ.poll());
        System.out.println("  Second poll: " + maxPQ.poll());
        
        // Complex objects with priority
        record Task(String name, int priority) implements Comparable<Task> {
            @Override
            public int compareTo(Task other) {
                return Integer.compare(this.priority, other.priority); // Lower number = higher priority
            }
        }
        
        Queue<Task> taskQueue = new PriorityQueue<>();
        taskQueue.offer(new Task("Low priority task", 10));
        taskQueue.offer(new Task("High priority task", 1));
        taskQueue.offer(new Task("Medium priority task", 5));
        taskQueue.offer(new Task("Critical task", 0));
        
        printResult("Task queue", taskQueue);
        
        printInfo("Processing tasks by priority:");
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println("  Processing: " + task.name() + " (priority: " + task.priority() + ")");
        }
        
        printWarning("PriorityQueue is NOT thread-safe and does NOT maintain sorted order in iteration");
        printTip("Use PriorityQueue when you need to process elements in priority order");
    }
    
    /**
     * Demonstrates BlockingQueue implementations for concurrent scenarios
     */
    private void demonstrateBlockingQueues() {
        printSubHeader("BlockingQueue - Thread-Safe Queues for Concurrent Scenarios");
        
        printInfo("BlockingQueue implementations provide thread-safe operations with blocking behavior");
        printInfo("Best for: Producer-consumer scenarios, thread pools, concurrent programming");
        
        // ArrayBlockingQueue - bounded blocking queue
        printInfo("ArrayBlockingQueue - Bounded capacity with blocking:");
        BlockingQueue<String> boundedQueue = new ArrayBlockingQueue<>(3);
        
        try {
            boundedQueue.put("Item 1");
            boundedQueue.put("Item 2");
            boundedQueue.put("Item 3");
            printResult("Bounded queue (full)", boundedQueue);
            
            // Try with timeout
            boolean added = boundedQueue.offer("Item 4", 1, TimeUnit.SECONDS);
            printResult("Offer with timeout (1s)", added + " - Queue: " + boundedQueue);
            
            // Take elements
            printResult("Take from queue", boundedQueue.take());
            printResult("Queue after take", boundedQueue);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            printInfo("Operation interrupted");
        }
        
        // LinkedBlockingQueue - optionally bounded
        printInfo("LinkedBlockingQueue - Optionally bounded:");
        BlockingQueue<Integer> linkedQueue = new LinkedBlockingQueue<>();
        
        // Producer-consumer simulation
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    linkedQueue.put(i);
                    System.out.println("  Produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Integer item = linkedQueue.take();
                    System.out.println("  Consumed: " + item);
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        printInfo("Producer-Consumer simulation:");
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // PriorityBlockingQueue - unbounded priority queue
        printInfo("PriorityBlockingQueue - Thread-safe priority queue:");
        BlockingQueue<Integer> priorityQueue = new PriorityBlockingQueue<>();
        priorityQueue.addAll(List.of(5, 2, 8, 1, 9, 3));
        printResult("Priority blocking queue", priorityQueue);
        
        try {
            printResult("Take min element", priorityQueue.take());
            printResult("Take next min", priorityQueue.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        printTip("Use ArrayBlockingQueue for bounded scenarios, LinkedBlockingQueue for unbounded");
        printInfo("BlockingQueues are essential for implementing producer-consumer patterns");
    }
    
    /**
     * Demonstrates high-performance concurrent queue implementations
     */
    private void demonstrateConcurrentQueues() {
        printSubHeader("High-Performance Concurrent Queues");
        
        printInfo("Non-blocking concurrent queues for high-performance scenarios");
        
        // ConcurrentLinkedQueue - unbounded lock-free queue
        printInfo("ConcurrentLinkedQueue - Lock-free thread-safe queue:");
        Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        
        // Simulate concurrent access
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            threads.add(new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    concurrentQueue.offer("T" + threadId + "-Item" + j);
                }
            }));
        }
        
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        printResult("Concurrent queue after parallel additions", concurrentQueue);
        
        // ConcurrentLinkedDeque - lock-free deque
        printInfo("ConcurrentLinkedDeque - Lock-free thread-safe deque:");
        Deque<Integer> concurrentDeque = new ConcurrentLinkedDeque<>();
        concurrentDeque.addFirst(1);
        concurrentDeque.addLast(2);
        concurrentDeque.addFirst(0);
        concurrentDeque.addLast(3);
        
        printResult("Concurrent deque", concurrentDeque);
        printResult("Poll first", concurrentDeque.pollFirst());
        printResult("Poll last", concurrentDeque.pollLast());
        printResult("Final concurrent deque", concurrentDeque);
        
        printTip("Use ConcurrentLinkedQueue for high-throughput, low-latency scenarios");
        printInfo("These implementations use lock-free algorithms for better performance");
    }
    
    /**
     * Performance comparison between Queue implementations
     */
    private void performanceComparison() {
        printSubHeader("Performance Comparison Analysis");
        
        final int elements = 10000;
        printInfo("Comparing Queue implementations with " + formatNumber(elements) + " operations");
        
        // ArrayDeque performance
        double arrayDequeTime = measureTime(() -> {
            Queue<Integer> queue = new ArrayDeque<>();
            for (int i = 0; i < elements; i++) {
                queue.offer(i);
            }
            for (int i = 0; i < elements; i++) {
                queue.poll();
            }
        });
        
        // LinkedList performance
        double linkedListTime = measureTime(() -> {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < elements; i++) {
                queue.offer(i);
            }
            for (int i = 0; i < elements; i++) {
                queue.poll();
            }
        });
        
        // PriorityQueue performance
        double priorityQueueTime = measureTime(() -> {
            Queue<Integer> queue = new PriorityQueue<>();
            for (int i = 0; i < elements; i++) {
                queue.offer(i);
            }
            for (int i = 0; i < elements; i++) {
                queue.poll();
            }
        });
        
        // ConcurrentLinkedQueue performance
        double concurrentQueueTime = measureTime(() -> {
            Queue<Integer> queue = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < elements; i++) {
                queue.offer(i);
            }
            for (int i = 0; i < elements; i++) {
                queue.poll();
            }
        });
        
        printBenchmark("ArrayDeque offer/poll", arrayDequeTime);
        printBenchmark("LinkedList offer/poll", linkedListTime);
        printBenchmark("PriorityQueue offer/poll", priorityQueueTime);
        printBenchmark("ConcurrentLinkedQueue offer/poll", concurrentQueueTime);
        
        printComparison("LinkedList vs ArrayDeque",
                       linkedListTime, arrayDequeTime,
                       "LinkedList", "ArrayDeque");
    }
    
    /**
     * Demonstrates common queue patterns and use cases
     */
    private void demonstrateQueuePatterns() {
        printSubHeader("Common Queue Patterns and Use Cases");
        
        // Breadth-First Search pattern
        printInfo("BFS (Breadth-First Search) pattern:");
        Queue<String> bfsQueue = new ArrayDeque<>();
        Map<String, List<String>> graph = Map.of(
            "A", List.of("B", "C"),
            "B", List.of("D", "E"),
            "C", List.of("F"),
            "D", List.of(),
            "E", List.of("F"),
            "F", List.of()
        );
        
        Set<String> visited = new HashSet<>();
        bfsQueue.offer("A");
        visited.add("A");
        
        System.out.print("  BFS traversal: ");
        while (!bfsQueue.isEmpty()) {
            String node = bfsQueue.poll();
            System.out.print(node + " ");
            
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    bfsQueue.offer(neighbor);
                }
            }
        }
        System.out.println();
        
        // Level-order processing pattern
        printInfo("Level-order processing with size tracking:");
        Queue<Integer> levelQueue = new ArrayDeque<>();
        levelQueue.addAll(List.of(1, 2, 3, 4, 5, 6, 7));
        
        int level = 1;
        while (!levelQueue.isEmpty()) {
            int levelSize = levelQueue.size();
            System.out.print("  Level " + level++ + ": ");
            
            for (int i = 0; i < levelSize; i++) {
                Integer item = levelQueue.poll();
                System.out.print(item + " ");
                
                // Add next level items (simulation)
                if (item <= 3) {
                    // Only add for first few to avoid infinite growth
                }
            }
            System.out.println();
            
            if (level > 3) break; // Stop simulation
        }
        
        // Task scheduling with priority
        printInfo("Task scheduling with PriorityQueue:");
        record ScheduledTask(String name, long executeTime) implements Comparable<ScheduledTask> {
            @Override
            public int compareTo(ScheduledTask other) {
                return Long.compare(this.executeTime, other.executeTime);
            }
        }
        
        PriorityQueue<ScheduledTask> scheduler = new PriorityQueue<>();
        long currentTime = System.currentTimeMillis();
        
        scheduler.offer(new ScheduledTask("Task A", currentTime + 1000));
        scheduler.offer(new ScheduledTask("Task B", currentTime + 500));
        scheduler.offer(new ScheduledTask("Task C", currentTime + 2000));
        scheduler.offer(new ScheduledTask("Task D", currentTime + 100));
        
        System.out.println("  Scheduled tasks (execution order):");
        while (!scheduler.isEmpty()) {
            ScheduledTask task = scheduler.poll();
            long delay = task.executeTime - currentTime;
            System.out.println("    " + task.name + " (delay: " + delay + "ms)");
        }
        
        printTip("Queues are fundamental for BFS, task scheduling, and buffering");
        printInfo("Choose implementation based on thread safety and performance requirements");
    }
    
    /**
     * Demonstrates modern Java features with Queues
     */
    private void demonstrateModernFeatures() {
        printSubHeader("Modern Java Features with Queues");
        
        // Using Queues with Streams
        printInfo("Stream operations with Queues:");
        Queue<String> wordsQueue = new ArrayDeque<>(
            List.of("java", "python", "javascript", "kotlin", "scala", "clojure")
        );
        
        // Process queue with streams
        List<String> processedWords = wordsQueue.stream()
                .filter(word -> word.length() > 4)
                .map(String::toUpperCase)
                .sorted()
                .toList();
        
        printResult("Original queue", wordsQueue);
        printResult("Processed (length > 4, uppercase, sorted)", processedWords);
        
        // Transfer queue elements using streams
        Queue<Integer> numbersQueue = new ArrayDeque<>();
        IntStream.range(1, 11).forEach(numbersQueue::offer);
        
        Queue<Integer> evenQueue = numbersQueue.stream()
                .filter(n -> n % 2 == 0)
                .collect(ArrayDeque::new, Queue::offer, (q1, q2) -> {
                    while (!q2.isEmpty()) q1.offer(q2.poll());
                });
        
        printResult("Even numbers queue", evenQueue);
        
        // Using var with queues (Java 10+)
        var taskQueue = new PriorityQueue<String>();
        taskQueue.addAll(List.of("Charlie", "Alpha", "Bravo", "Delta"));
        printResult("Task queue (using var)", taskQueue);
        
        // Pattern matching in switch expressions (Java 17+)
        Queue<?> testQueue = new ArrayDeque<>(List.of("test", 123, 45.6));
        
        while (!testQueue.isEmpty()) {
            Object item = testQueue.poll();
            String description = switch (item) {
                case String s -> "String: " + s;
                case Integer i -> "Integer: " + i;
                case Double d -> "Double: " + d;
                case null -> "Null value";
                default -> "Unknown type: " + item.getClass().getSimpleName();
            };
            System.out.println("  " + description);
        }
        
        printTip("Combine Queues with Streams for powerful data processing pipelines");
        printInfo("Modern Java features make queue processing more expressive");
    }
    
    /**
     * Demonstrates best practices and common patterns
     */
    private void demonstrateBestPractices() {
        printSubHeader("Best Practices and Common Patterns");
        
        printInfo("🚀 Queue Implementation Selection Guide:");
        System.out.println("  • ArrayDeque: Default choice for queue/deque operations");
        System.out.println("  • LinkedList: Only when you need List + Queue operations");
        System.out.println("  • PriorityQueue: When elements need priority-based processing");
        System.out.println("  • ArrayBlockingQueue: Bounded producer-consumer scenarios");
        System.out.println("  • LinkedBlockingQueue: Unbounded producer-consumer scenarios");
        System.out.println("  • ConcurrentLinkedQueue: High-performance concurrent access");
        
        printSeparator();
        
        printInfo("⚡ Performance Optimization Tips:");
        System.out.println("  • Use ArrayDeque instead of LinkedList for queue operations");
        System.out.println("  • Pre-size queues when approximate capacity is known");
        System.out.println("  • Use offer/poll instead of add/remove for safe operations");
        System.out.println("  • Consider blocking vs non-blocking based on use case");
        System.out.println("  • Use appropriate timeout values for blocking operations");
        
        printSeparator();
        
        printInfo("🚨 Common Pitfalls to Avoid:");
        System.out.println("  • Using LinkedList when ArrayDeque would be better");
        System.out.println("  • Not handling InterruptedException in blocking operations");
        System.out.println("  • Assuming PriorityQueue maintains sorted order in iteration");
        System.out.println("  • Using null elements (not supported by most queue implementations)");
        System.out.println("  • Modifying elements after adding to PriorityQueue");
        
        // Demonstrate safe queue operations
        printInfo("Safe queue operation patterns:");
        
        Queue<String> safeQueue = new ArrayDeque<>();
        
        // Safe adding
        boolean added = safeQueue.offer("item");
        printResult("Safe offer result", added);
        
        // Safe removal with null check
        String polled = safeQueue.poll();
        printResult("Safe poll result", polled != null ? polled : "null (empty queue)");
        
        // Safe peeking
        String peeked = safeQueue.peek();
        printResult("Safe peek result", peeked != null ? peeked : "null (empty queue)");
        
        // Exception vs safe operations comparison
        printInfo("Exception vs Safe operations:");
        Queue<String> testQueue = new ArrayDeque<>();
        testQueue.offer("test");
        
        try {
            printResult("remove() on queue with 1 element", testQueue.remove());
            printResult("remove() on empty queue", testQueue.remove()); // Will throw
        } catch (NoSuchElementException e) {
            printInfo("remove() throws NoSuchElementException on empty queue");
        }
        
        printResult("poll() on empty queue", testQueue.poll()); // Returns null safely
        
        // Producer-consumer best practices
        printInfo("Producer-consumer best practices:");
        BlockingQueue<String> pcQueue = new ArrayBlockingQueue<>(10);
        
        // Producer with timeout
        try {
            boolean offered = pcQueue.offer("item", 1, TimeUnit.SECONDS);
            printResult("Producer with timeout", offered);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            printInfo("Producer interrupted - proper cleanup");
        }
        
        printTip("Always use appropriate queue type for your specific use case");
        printWarning("Handle InterruptedException properly in concurrent queue operations");
    }
}