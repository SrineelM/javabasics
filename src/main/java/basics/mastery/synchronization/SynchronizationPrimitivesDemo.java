package basics.mastery.advanced;.synchronization;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comprehensive Java Synchronization Primitives Masterclass
 * 
 * This extensive demonstration covers the complete spectrum of Java synchronization 
 * mechanisms, from basic synchronized blocks to advanced coordination primitives.
 * Each section provides practical examples with real-world scenarios to illustrate
 * when and how to use different synchronization approaches.
 * 
 * Educational Structure:
 * 
 * Level 1: Fundamental Locking Mechanisms
 * ========================================
 * 1. synchronized keyword - Basic mutual exclusion with monitor locks
 * 2. ReentrantLock - Advanced lock with timeout, fairness, and interruptibility
 * 3. ReentrantReadWriteLock - Optimized for read-heavy workloads
 * 4. StampedLock - High-performance lock with optimistic reads (Java 8+)
 * 
 * Level 2: Advanced Coordination Primitives
 * ==========================================
 * 5. CountDownLatch - One-shot coordination for startup/shutdown scenarios
 * 6. CyclicBarrier - Multi-phase coordination for iterative algorithms
 * 7. Semaphore - Resource pooling and rate limiting
 * 8. Phaser - Dynamic participant coordination with phase advancement
 * 
 * Key Learning Objectives:
 * - Understand performance characteristics of different locking mechanisms
 * - Learn when to choose each synchronization primitive based on use case
 * - Master thread coordination patterns for complex concurrent systems
 * - Develop intuition for deadlock-free concurrent algorithm design
 * - Experience the evolution from basic synchronization to modern patterns
 * 
 * Performance Considerations:
 * - synchronized: JVM-optimized, but limited flexibility
 * - ReentrantLock: More features, slightly higher overhead
 * - ReadWriteLock: Excellent for read-heavy scenarios
 * - StampedLock: Best performance for read-heavy with occasional writes
 * - CountDownLatch: Single-use, very efficient for startup coordination
 * - CyclicBarrier: Reusable, ideal for iterative parallel algorithms
 * - Semaphore: Flexible resource management with configurable fairness
 * - Phaser: Most flexible, supports dynamic participation
 * 
 * Thread Safety Patterns Demonstrated:
 * - Monitor pattern (synchronized methods/blocks)
 * - Lock-based protection with timeout handling
 * - Reader-writer patterns for concurrent data structures
 * - Optimistic concurrency with validation
 * - Producer-consumer coordination
 * - Barrier synchronization for parallel algorithms
 * - Resource pooling with admission control
 * - Dynamic phase-based coordination
 * 
 * Prerequisites:
 * - Understanding of Java threading fundamentals
 * - Familiarity with race conditions and thread safety concepts
 * - Basic knowledge of concurrent programming challenges
 * 
 * Advanced Topics Covered:
 * - Lock fairness and its impact on performance
 * - Optimistic vs pessimistic locking strategies
 * - Interruption handling in blocking operations
 * - Dynamic participant management in coordination primitives
 * - Performance implications of different synchronization choices
 */
public class SynchronizationPrimitivesDemo {
    
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Advanced Java Synchronization Primitives Masterclass ===\n");
        System.out.println("🚀 Exploring the complete spectrum of Java concurrency synchronization");
        System.out.println("📚 From basic synchronized blocks to advanced coordination patterns\n");
        
        // Level 1: Fundamental Locking Mechanisms
        System.out.println("🔒 LEVEL 1: Fundamental Locking Mechanisms");
        System.out.println("=" .repeat(50));
        demonstrateSynchronizedKeyword();
        demonstrateReentrantLock();
        demonstrateReadWriteLock();
        demonstrateStampedLock();
        
        // Level 2: Advanced Coordination Primitives
        System.out.println("🤝 LEVEL 2: Advanced Coordination Primitives");
        System.out.println("=" .repeat(50));
        demonstrateCountDownLatch();
        demonstrateCyclicBarrier();
        demonstrateSemaphore();
        demonstratePhaser();
        
        System.out.println("🎯 Synchronization primitives masterclass completed!");
        System.out.println("💡 Choose the right primitive based on your specific coordination needs");
    }
    
    /**
     * Traditional synchronized keyword demonstration
     * 
     * The synchronized keyword provides the most basic form of mutual exclusion
     * in Java, using intrinsic locks (monitor locks) built into every object.
     * 
     * Key Characteristics:
     * - Built into the JVM, highly optimized
     * - Automatic lock acquisition and release (via try-finally semantics)
     * - Reentrant (same thread can acquire the same lock multiple times)
     * - No timeout or interrupt support
     * - Block-structured (must release in reverse order of acquisition)
     * 
     * Use Cases:
     * - Simple mutual exclusion scenarios
     * - When you need guaranteed lock release (exception safety)
     * - Performance-critical code where JVM optimizations matter
     * - Legacy code or when simplicity is preferred
     * 
     * Performance Notes:
     * - JVM can apply lock elision, biased locking, and other optimizations
     * - Generally faster than explicit locks for uncontended scenarios
     * - Can lead to blocking if fairness is required
     */
    private static void demonstrateSynchronizedKeyword() throws InterruptedException {
        System.out.println("1. Synchronized Keyword Demo:");
        System.out.println("   Demonstrating basic mutual exclusion with monitor locks");
        
        SynchronizedCounter counter = new SynchronizedCounter();
        int threadCount = 5;
        int incrementsPerThread = 1000;
        
        System.out.printf("   Creating %d threads, each performing %d increments%n", 
            threadCount, incrementsPerThread);
        
        Thread[] threads = new Thread[threadCount];
        
        // Create worker threads that increment the shared counter
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                    // Periodic progress reporting (every 200 increments)
                    if (j % 200 == 0) {
                        log("Thread-" + threadId + " progress: " + counter.getValue() + " total increments");
                    }
                }
                log("Thread-" + threadId + " completed all " + incrementsPerThread + " increments");
            }, "Sync-Thread-" + i);
        }
        
        // Measure execution time and thread coordination
        long start = System.currentTimeMillis();
        
        // Start all threads simultaneously
        for (Thread t : threads) t.start();
        
        // Wait for all threads to complete
        for (Thread t : threads) t.join();
        
        long elapsed = System.currentTimeMillis() - start;
        
        // Verify thread safety and report results
        int expectedTotal = threadCount * incrementsPerThread;
        int actualTotal = counter.getValue();
        
        System.out.printf("✅ Synchronized execution completed%n");
        System.out.printf("   📊 Final count: %,d (expected: %,d)%n", actualTotal, expectedTotal);
        System.out.printf("   ⏱️  Execution time: %d ms%n", elapsed);
        System.out.printf("   🎯 Thread safety: %s%n", 
            actualTotal == expectedTotal ? "VERIFIED ✅" : "FAILED ❌");
        System.out.printf("   🚀 Throughput: %.1f operations/ms%n%n", 
            (double) expectedTotal / elapsed);
    }
    
    /**
     * ReentrantLock with advanced features demonstration
     * 
     * ReentrantLock provides all the capabilities of synchronized with additional
     * features for more sophisticated locking scenarios.
     * 
     * Advanced Features:
     * - Timeout-based lock acquisition (tryLock with time limit)
     * - Interruptible lock acquisition (lockInterruptibly)
     * - Fair vs unfair locking policies
     * - Lock condition variables (await/signal)
     * - Lock state monitoring (isLocked, hasQueuedThreads, etc.)
     * 
     * Use Cases:
     * - When you need timeout-based lock acquisition
     * - Interruptible blocking operations
     * - Complex locking patterns that can't be expressed with synchronized
     * - When fairness is required to prevent thread starvation
     * - Producer-consumer patterns with multiple conditions
     * 
     * Performance Considerations:
     * - Slightly higher overhead than synchronized for simple cases
     * - Better scalability under high contention with fair locking
     * - Explicit lock/unlock calls (must be in try-finally)
     */
    private static void demonstrateReentrantLock() throws InterruptedException {
        System.out.println("2. ReentrantLock Demo (Advanced Locking Features):");
        System.out.println("   Showcasing timeout, fairness, and interruptibility");
        
        ReentrantLockExample example = new ReentrantLockExample();
        
        // Thread that holds lock for an extended period
        Thread longRunningThread = new Thread(() -> {
            example.doLongWork();
        }, "LongRunner");
        
        // Thread that attempts lock acquisition with timeout
        Thread timeoutThread = new Thread(() -> {
            example.doWorkWithTimeout();
        }, "TimeoutWorker");
        
        // Thread that demonstrates interruptible locking
        Thread interruptibleThread = new Thread(() -> {
            example.doInterruptibleWork();
        }, "InterruptibleWorker");
        
        System.out.println("   Starting long-running thread (will hold lock for 2 seconds)...");
        longRunningThread.start();
        
        Thread.sleep(100); // Ensure long runner acquires lock first
        
        System.out.println("   Starting timeout thread (will try with 1 second timeout)...");
        timeoutThread.start();
        
        Thread.sleep(50);
        
        System.out.println("   Starting interruptible thread (will be interrupted)...");
        interruptibleThread.start();
        
        Thread.sleep(500);
        
        // Demonstrate interruption
        System.out.println("   Interrupting the interruptible thread...");
        interruptibleThread.interrupt();
        
        // Wait for all threads to complete
        longRunningThread.join();
        timeoutThread.join();
        interruptibleThread.join();
        
        System.out.println("   💡 Key ReentrantLock advantages:");
        System.out.println("     • Timeout prevents indefinite blocking");
        System.out.println("     • Fairness prevents thread starvation");
        System.out.println("     • Interruptibility enables responsive cancellation");
        System.out.println();
    }
    
    /**
     * ReentrantReadWriteLock for read-heavy scenarios
     * 
     * ReadWriteLock optimizes for scenarios where reads are much more frequent
     * than writes by allowing multiple concurrent readers while ensuring exclusive
     * access for writers.
     * 
     * Locking Policy:
     * - Multiple threads can hold read locks simultaneously
     * - Only one thread can hold the write lock
     * - Read and write locks are mutually exclusive
     * - Write lock downgrades to read lock are supported
     * - Read lock upgrades to write lock are NOT supported (to prevent deadlock)
     * 
     * Use Cases:
     * - Caches and read-heavy data structures
     * - Configuration objects that are read frequently, updated rarely
     * - Shared data structures in high-throughput applications
     * - Statistical counters and metrics collection
     * 
     * Performance Benefits:
     * - Dramatically improves throughput for read-heavy workloads
     * - Reduces contention compared to exclusive locking
     * - Maintains strong consistency guarantees
     */
    private static void demonstrateReadWriteLock() throws InterruptedException {
        System.out.println("3. ReentrantReadWriteLock Demo:");
        System.out.println("   Optimizing for read-heavy concurrent access patterns");
        
        ReadWriteExample example = new ReadWriteExample();
        
        System.out.println("   Creating 4 reader threads and 1 writer thread...");
        
        // Create multiple concurrent readers
        Thread[] readers = new Thread[4];
        for (int i = 0; i < readers.length; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    example.readData(readerId);
                    try {
                        Thread.sleep(100); // Pause between reads
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "Reader-" + i);
        }
        
        // Create a single writer that updates data periodically
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                example.writeData("Update-" + i + "-" + System.currentTimeMillis());
                try {
                    Thread.sleep(200); // Pause between writes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Writer");
        
        // Start all threads
        System.out.println("   Starting concurrent readers and writer...");
        for (Thread reader : readers) reader.start();
        writer.start();
        
        // Wait for all operations to complete
        for (Thread reader : readers) reader.join();
        writer.join();
        
        System.out.println("   💡 ReadWriteLock benefits:");
        System.out.println("     • Multiple readers can access data concurrently");
        System.out.println("     • Writers get exclusive access for consistency");
        System.out.println("     • Dramatically improves read-heavy workload performance");
        System.out.println();
    }
    
    /**
     * StampedLock with optimistic reads (Java 8+)
     * 
     * StampedLock is the most advanced and performant lock in Java, introducing
     * optimistic reading capabilities that can eliminate blocking for readers
     * in many scenarios.
     * 
     * Lock Modes:
     * - Optimistic Read: Non-blocking read that validates data consistency
     * - Pessimistic Read: Traditional blocking read lock
     * - Write: Exclusive write lock
     * 
     * Optimistic Read Pattern:
     * 1. Acquire optimistic read stamp
     * 2. Read data non-atomically
     * 3. Validate stamp (check if writes occurred)
     * 4. If invalid, fall back to pessimistic read
     * 
     * Use Cases:
     * - High-performance data structures with frequent reads
     * - When read latency is critical
     * - Scenarios where reads rarely conflict with writes
     * - Performance-sensitive concurrent algorithms
     * 
     * Performance Characteristics:
     * - Best-in-class performance for read-heavy workloads
     * - Optimistic reads are essentially lock-free
     * - Automatic fallback to pessimistic reads when needed
     */
    private static void demonstrateStampedLock() throws InterruptedException {
        System.out.println("4. StampedLock Demo (Optimistic Reading):");
        System.out.println("   High-performance locking with lock-free optimistic reads");
        
        StampedLockExample example = new StampedLockExample();
        
        System.out.println("   Creating 3 optimistic readers and 1 occasional writer...");
        
        // Multiple readers using optimistic read pattern
        Thread[] readers = new Thread[3];
        for (int i = 0; i < readers.length; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    double result = example.optimisticRead(readerId);
                    log("Reader-" + readerId + " computed result: " + String.format("%.2f", result));
                    try {
                        Thread.sleep(50); // Brief pause between reads
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "StampedReader-" + i);
        }
        
        // Writer that occasionally updates data
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                double newValue = Math.random() * 100;
                example.write(newValue);
                try {
                    Thread.sleep(150); // Pause between writes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "StampedWriter");
        
        // Start all threads
        for (Thread reader : readers) reader.start();
        writer.start();
        
        // Wait for completion
        for (Thread reader : readers) reader.join();
        writer.join();
        
        System.out.println("   💡 StampedLock advantages:");
        System.out.println("     • Optimistic reads are essentially lock-free");
        System.out.println("     • Automatic fallback to pessimistic reads when needed");
        System.out.println("     • Best performance for read-heavy workloads");
        System.out.println("     • Validation-based consistency without blocking");
        System.out.println();
    }
    
    /**
     * CountDownLatch for one-shot coordination
     * 
     * CountDownLatch provides a simple but powerful mechanism for coordinating
     * the startup and shutdown of multiple threads or services.
     * 
     * Core Concepts:
     * - Initialized with a count (number of events to wait for)
     * - countDown() decrements the count
     * - await() blocks until count reaches zero
     * - One-shot mechanism (cannot be reused)
     * 
     * Common Patterns:
     * - Service startup coordination (all services ready before accepting requests)
     * - Parallel task completion (wait for all workers to finish)
     * - Batch processing coordination (wait for all batches to complete)
     * - Testing synchronization (coordinate test execution)
     * 
     * Use Cases:
     * - Microservice startup orchestration
     * - Parallel initialization of components
     * - Coordinated shutdown sequences
     * - Test harness synchronization
     */
    private static void demonstrateCountDownLatch() throws InterruptedException {
        System.out.println("5. CountDownLatch Demo (Service Startup Coordination):");
        System.out.println("   Coordinating multiple services before system becomes operational");
        
        int serviceCount = 3;
        CountDownLatch startupLatch = new CountDownLatch(serviceCount);
        CountDownLatch completionLatch = new CountDownLatch(serviceCount);
        
        // Simulate critical services that must all be ready before system starts
        String[] serviceNames = {"Database", "Cache", "MessageQueue"};
        int[] startupTimes = {150, 100, 200}; // Different realistic startup times
        
        System.out.println("   Starting critical services...");
        
        for (int i = 0; i < serviceCount; i++) {
            final String serviceName = serviceNames[i];
            final int startupTime = startupTimes[i];
            
            new Thread(() -> {
                try {
                    log(serviceName + " service: Initializing...");
                    Thread.sleep(startupTime); // Simulate startup work
                    log(serviceName + " service: ✅ Ready!");
                    startupLatch.countDown(); // Signal this service is ready
                    
                    // Wait for ALL services to be ready before starting main work
                    startupLatch.await();
                    log(serviceName + " service: All services ready, starting main operations");
                    
                    // Simulate main service work
                    Thread.sleep(200 + new Random().nextInt(100));
                    log(serviceName + " service: Main operations completed");
                    completionLatch.countDown();
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log(serviceName + " service: Interrupted during startup");
                }
            }, serviceName + "-Service").start();
        }
        
        log("System Controller: Waiting for all critical services to be ready...");
        long waitStart = System.currentTimeMillis();
        
        startupLatch.await(); // Block until all services signal readiness
        
        long waitTime = System.currentTimeMillis() - waitStart;
        log("System Controller: ✅ All services ready! System operational after " + waitTime + "ms");
        log("System Controller: Accepting client requests...");
        
        // Wait for all services to complete their main work
        completionLatch.await();
        log("System Controller: All services completed their work. System shutting down gracefully.");
        
        System.out.println("   💡 CountDownLatch benefits:");
        System.out.println("     • Ensures all critical services are ready before operation");
        System.out.println("     • Prevents race conditions during system startup");
        System.out.println("     • Simple, efficient coordination primitive");
        System.out.println("     • Fail-fast behavior if any service fails to start");
        System.out.println();
    }
    
    /**
     * CyclicBarrier for multi-phase coordination
     * 
     * CyclicBarrier enables multiple threads to synchronize at common barrier points,
     * making it ideal for parallel algorithms that proceed in phases.
     * 
     * Key Features:
     * - All threads must reach the barrier before any can proceed
     * - Reusable (cyclic) - can be used for multiple phases
     * - Optional barrier action runs when all threads arrive
     * - Broken barrier state handling for error scenarios
     * 
     * Common Patterns:
     * - Parallel algorithms with synchronization points
     * - Multi-phase computations (collect, process, aggregate)
     * - Iterative algorithms requiring synchronization between iterations
     * - Simulation systems with discrete time steps
     * 
     * Use Cases:
     * - MapReduce-style parallel processing
     * - Scientific computing with synchronized phases
     * - Game engines with synchronized frame processing
     * - Distributed consensus algorithms
     */
    private static void demonstrateCyclicBarrier() throws InterruptedException {
        System.out.println("6. CyclicBarrier Demo (Multi-Phase Parallel Computation):");
        System.out.println("   Synchronizing workers across multiple computation phases");
        
        int workerCount = 4;
        
        // Barrier action runs when all workers reach the barrier
        CyclicBarrier barrier = new CyclicBarrier(workerCount, () -> {
            log("🚧 Barrier Controller: All workers completed phase, advancing to next phase");
        });
        
        System.out.printf("   Creating %d workers for 3-phase computation...%n", workerCount);
        
        Thread[] workers = new Thread[workerCount];
        
        for (int i = 0; i < workerCount; i++) {
            final int workerId = i;
            workers[i] = new Thread(() -> {
                try {
                    // Phase 1: Data Collection
                    log("Worker-" + workerId + " starting Phase 1 (data collection)");
                    Thread.sleep(100 + workerId * 30); // Simulate varying work times
                    log("Worker-" + workerId + " completed Phase 1, waiting at barrier");
                    barrier.await(); // Synchronize before proceeding to next phase
                    
                    // Phase 2: Data Processing
                    log("Worker-" + workerId + " starting Phase 2 (data processing)");
                    Thread.sleep(150 + workerId * 20); // Different processing times
                    log("Worker-" + workerId + " completed Phase 2, waiting at barrier");
                    barrier.await(); // Synchronize before final phase
                    
                    // Phase 3: Result Aggregation
                    log("Worker-" + workerId + " starting Phase 3 (result aggregation)");
                    Thread.sleep(80 + workerId * 10); // Final phase work
                    log("Worker-" + workerId + " completed Phase 3 - ✅ ALL PHASES DONE!");
                    
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    log("Worker-" + workerId + " interrupted or barrier broken");
                }
            }, "Worker-" + i);
        }
        
        // Start all workers
        for (Thread worker : workers) worker.start();
        
        // Wait for all workers to complete
        for (Thread worker : workers) worker.join();
        
        System.out.println("   💡 CyclicBarrier benefits:");
        System.out.println("     • Ensures all workers complete each phase before proceeding");
        System.out.println("     • Reusable for multiple synchronization points");
        System.out.println("     • Barrier action enables phase transition logic");
        System.out.println("     • Ideal for parallel algorithms with dependencies between phases");
        System.out.println();
    }
    
    /**
     * Semaphore for resource pooling and rate limiting
     * 
     * Semaphore controls access to a finite set of resources, making it perfect
     * for implementing connection pools, rate limiters, and resource quotas.
     * 
     * Core Concepts:
     * - Permits represent available resources
     * - acquire() takes a permit (blocks if none available)
     * - release() returns a permit to the pool
     * - Fair vs unfair permit allocation
     * 
     * Common Patterns:
     * - Database connection pooling
     * - HTTP client connection limits
     * - Rate limiting for API calls
     * - Thread pool size control
     * - Bandwidth throttling
     * 
     * Use Cases:
     * - Resource management in microservices
     * - Protecting external service from overload
     * - Memory-bound operation throttling
     * - License-based feature access control
     */
    private static void demonstrateSemaphore() throws InterruptedException {
        System.out.println("7. Semaphore Demo (Database Connection Pool Simulation):");
        System.out.println("   Managing limited resources with admission control");
        
        // Simulate a database connection pool with 3 available connections
        Semaphore connectionPool = new Semaphore(3, true); // Fair semaphore
        AtomicInteger connectionId = new AtomicInteger(0);
        
        System.out.println("   Database connection pool: 3 connections available");
        System.out.println("   6 clients will compete for connections...");
        
        // 6 clients trying to use database connections
        Thread[] clients = new Thread[6];
        
        for (int i = 0; i < clients.length; i++) {
            final int clientId = i;
            clients[i] = new Thread(() -> {
                try {
                    log("Client-" + clientId + " requesting database connection...");
                    
                    // Request a connection from the pool
                    connectionPool.acquire(); // Will block if no connections available
                    
                    int connId = connectionId.incrementAndGet();
                    log("Client-" + clientId + " ✅ acquired connection-" + connId + 
                        " (" + (3 - connectionPool.availablePermits()) + "/3 in use)");
                    
                    // Simulate database operations with varying duration
                    int workTime = 200 + new Random().nextInt(200);
                    Thread.sleep(workTime);
                    
                    log("Client-" + clientId + " completed database work (" + workTime + "ms), " +
                        "releasing connection-" + connId);
                    
                    // Return connection to pool
                    connectionPool.release();
                    
                    log("Client-" + clientId + " ✅ released connection (" + 
                        connectionPool.availablePermits() + "/3 available)");
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log("Client-" + clientId + " interrupted while waiting for connection");
                }
            }, "Client-" + i);
        }
        
        // Start all clients
        for (Thread client : clients) client.start();
        
        // Wait for all clients to complete
        for (Thread client : clients) client.join();
        
        System.out.printf("   Final connection pool state: %d/3 connections available%n", 
            connectionPool.availablePermits());
        
        System.out.println("   💡 Semaphore benefits:");
        System.out.println("     • Prevents resource exhaustion through admission control");
        System.out.println("     • Fair queuing prevents client starvation");
        System.out.println("     • Configurable resource limits without hardcoded pools");
        System.out.println("     • Excellent for rate limiting and throttling");
        System.out.println();
    }
    
    /**
     * Phaser for dynamic multi-phase coordination
     * 
     * Phaser is the most flexible coordination primitive, supporting dynamic
     * participant registration/deregistration and multiple phases.
     * 
     * Advanced Features:
     * - Dynamic participant management (register/deregister at runtime)
     * - Multiple phases with automatic advancement
     * - Hierarchical phasers for complex coordination
     * - Termination detection when no participants remain
     * 
     * Key Operations:
     * - register(): Add a new participant
     * - arriveAndAwaitAdvance(): Complete current phase and wait for others
     * - arriveAndDeregister(): Complete current phase and leave
     * - arrive(): Complete current phase without waiting
     * 
     * Use Cases:
     * - Dynamic work scheduling with varying participant counts
     * - Multi-stage pipelines with optional stages
     * - Hierarchical parallel computations
     * - Adaptive algorithms that scale participant count
     */
    private static void demonstratePhaser() throws InterruptedException {
        System.out.println("8. Phaser Demo (Dynamic Participant Coordination):");
        System.out.println("   Flexible multi-phase coordination with dynamic participation");
        
        Phaser phaser = new Phaser(1); // Start with main thread registered
        
        System.out.println("   Creating workers that join/leave dynamically across phases...");
        
        // Create workers that can join and leave dynamically
        List<Thread> workers = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            final int workerId = i;
            Thread worker = new Thread(() -> {
                // Register with phaser (dynamic participation)
                phaser.register();
                log("Worker-" + workerId + " joined the phaser (phase " + phaser.getPhase() + ")");
                
                try {
                    // Phase 0: Initialization (all workers participate)
                    log("Worker-" + workerId + " doing initialization work");
                    Thread.sleep(100 + workerId * 30);
                    log("Worker-" + workerId + " completed initialization");
                    phaser.arriveAndAwaitAdvance(); // Complete phase 0, wait for others
                    
                    // Phase 1: Main work (all workers participate)
                    log("Worker-" + workerId + " doing main work in phase " + phaser.getPhase());
                    Thread.sleep(150 + workerId * 20);
                    log("Worker-" + workerId + " completed main work");
                    phaser.arriveAndAwaitAdvance(); // Complete phase 1, wait for others
                    
                    // Phase 2: Cleanup (only even-numbered workers participate)
                    if (workerId % 2 == 0) {
                        log("Worker-" + workerId + " doing cleanup work in phase " + phaser.getPhase());
                        Thread.sleep(100);
                        log("Worker-" + workerId + " completed cleanup");
                        phaser.arriveAndAwaitAdvance(); // Complete phase 2, wait for others
                        log("Worker-" + workerId + " finished all phases, staying registered");
                    } else {
                        log("Worker-" + workerId + " skipping cleanup, leaving phaser after phase 1");
                        phaser.arriveAndDeregister(); // Leave after phase 1
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log("Worker-" + workerId + " interrupted");
                }
            }, "PhaserWorker-" + i);
            
            workers.add(worker);
        }
        
        // Start workers at staggered times to demonstrate dynamic joining
        for (int i = 0; i < workers.size(); i++) {
            workers.get(i).start();
            Thread.sleep(50); // Stagger start times
        }
        
        // Main thread coordinates phases
        log("Main Controller: Starting Phase 0 (all workers initializing)");
        phaser.arriveAndAwaitAdvance(); // Wait for phase 0 completion
        log("Main Controller: Phase 0 complete, " + phaser.getRegisteredParties() + " participants remain");
        
        log("Main Controller: Starting Phase 1 (all workers doing main work)");
        phaser.arriveAndAwaitAdvance(); // Wait for phase 1 completion
        log("Main Controller: Phase 1 complete, " + phaser.getRegisteredParties() + " participants remain");
        
        log("Main Controller: Starting Phase 2 (only even workers doing cleanup)");
        phaser.arriveAndAwaitAdvance(); // Wait for phase 2 completion
        log("Main Controller: Phase 2 complete, " + phaser.getRegisteredParties() + " participants remain");
        
        log("Main Controller: All phases completed! Final participant count: " + 
            phaser.getRegisteredParties());
        
        // Wait for all workers to finish
        for (Thread worker : workers) worker.join();
        
        System.out.println("   💡 Phaser advantages:");
        System.out.println("     • Dynamic participant management (join/leave at runtime)");
        System.out.println("     • Multiple coordination phases with automatic advancement");
        System.out.println("     • Flexible participation patterns (not all workers in all phases)");
        System.out.println("     • Most powerful coordination primitive for complex scenarios");
        System.out.println();
    }
    
    /**
     * Utility method for timestamped logging
     */
    private static void log(String message) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(TIME_FORMAT), message);
    }
}

/**
 * Thread-safe counter using synchronized methods
 * 
 * Demonstrates the monitor pattern with synchronized methods.
 * This is the simplest form of thread-safe shared state.
 */
class SynchronizedCounter {
    private int count = 0;
    
    /**
     * Synchronized increment operation
     * Multiple threads can safely call this method concurrently
     */
    public synchronized void increment() {
        count++; // Atomic increment under monitor lock
    }
    
    /**
     * Synchronized read operation
     * Ensures consistent read of the current value
     */
    public synchronized int getValue() {
        return count;
    }
    
    /**
     * Synchronized batch increment
     * Demonstrates method-level synchronization for compound operations
     */
    public synchronized void incrementBy(int amount) {
        count += amount; // Compound operation protected by single lock acquisition
    }
}

/**
 * ReentrantLock example demonstrating advanced locking features
 * 
 * Shows timeout-based acquisition, interruptible locking, and fairness.
 * These features are not available with synchronized blocks.
 */
class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock(true); // Fair lock prevents starvation
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    /**
     * Long-running operation that holds the lock
     * Simulates a scenario where one thread needs extended exclusive access
     */
    public void doLongWork() {
        log("Acquiring lock for long-running operation...");
        lock.lock();
        try {
            log("✅ Acquired lock, starting long operation (2 seconds)");
            Thread.sleep(2000); // Simulate extended work requiring exclusive access
            log("Long operation completed successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("Long operation interrupted");
        } finally {
            log("Releasing lock from long operation");
            lock.unlock();
        }
    }
    
    /**
     * Timeout-based lock acquisition
     * Prevents indefinite blocking when lock is not available
     */
    public void doWorkWithTimeout() {
        log("Attempting lock acquisition with 1 second timeout...");
        try {
            // Try to acquire lock with timeout
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    log("✅ Acquired lock within timeout! Doing quick work...");
                    Thread.sleep(100); // Quick operation
                    log("Quick work completed");
                } finally {
                    lock.unlock();
                    log("Released lock from timeout work");
                }
            } else {
                log("❌ Timeout expired! Could not acquire lock within 1 second");
                log("   Proceeding with alternative strategy...");
                // Could implement fallback logic here
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("Interrupted while waiting for lock");
        }
    }
    
    /**
     * Interruptible lock acquisition
     * Allows thread to respond to interruption while waiting for lock
     */
    public void doInterruptibleWork() {
        log("Attempting interruptible lock acquisition...");
        try {
            lock.lockInterruptibly(); // Can be interrupted while waiting
            try {
                log("✅ Acquired lock via interruptible wait");
                Thread.sleep(1000); // Simulate work
                log("Interruptible work completed");
            } finally {
                lock.unlock();
                log("Released lock from interruptible work");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("❌ Interrupted while waiting for lock (responsive cancellation)");
        }
    }
    
    private void log(String message) {
        System.out.printf("[%s] %s: %s%n", 
            LocalTime.now().format(timeFormat), 
            Thread.currentThread().getName(), 
            message);
    }
}

/**
 * Read-Write lock example for concurrent reads, exclusive writes
 * 
 * Demonstrates the reader-writer pattern where multiple readers can
 * access data concurrently, but writers need exclusive access.
 */
class ReadWriteExample {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true); // Fair policy
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    
    private String data = "Initial Data V1.0";
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private int readCount = 0;
    private int writeCount = 0;
    
    /**
     * Concurrent read operation
     * Multiple threads can read simultaneously
     */
    public void readData(int readerId) {
        readLock.lock();
        try {
            readCount++;
            log("Reader-" + readerId + " acquired read lock (#" + readCount + 
                "), current data: '" + data + "'");
            log("Reader-" + readerId + " concurrent readers: " + rwLock.getReadLockCount());
            
            // Simulate read processing time
            Thread.sleep(300);
            
            log("Reader-" + readerId + " finished reading after 300ms");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            readLock.unlock();
            log("Reader-" + readerId + " released read lock");
        }
    }
    
    /**
     * Exclusive write operation
     * Only one writer can modify data at a time, and no readers during write
     */
    public void writeData(String newData) {
        log("Writer requesting exclusive write lock...");
        writeLock.lock();
        try {
            writeCount++;
            String oldData = this.data;
            log("Writer acquired exclusive write lock (#" + writeCount + ")");
            log("Writer updating data from '" + oldData + "' to '" + newData + "'");
            
            // Simulate write processing time
            Thread.sleep(500);
            
            this.data = newData;
            log("Writer completed data update after 500ms");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
            log("Writer released exclusive write lock");
        }
    }
    
    private void log(String message) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(timeFormat), message);
    }
}

/**
 * StampedLock example with optimistic reads
 * 
 * Demonstrates the highest-performance locking mechanism in Java,
 * with optimistic reads that can be completely lock-free.
 */
class StampedLockExample {
    private final StampedLock sl = new StampedLock();
    private double x = 0.0, y = 0.0;
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private int optimisticSuccessCount = 0;
    private int pessimisticFallbackCount = 0;
    
    /**
     * Exclusive write operation
     * Updates both coordinates atomically
     */
    public void write(double newValue) {
        long stamp = sl.writeLock();
        try {
            log("Writer acquired exclusive write lock (stamp: " + stamp + ")");
            log("Writer updating coordinates from (" + x + ", " + y + ") to (" + 
                newValue + ", " + (newValue * 2) + ")");
            
            x = newValue;
            y = newValue * 2;
            
            // Simulate write processing
            Thread.sleep(100);
            
            log("Writer completed coordinate update");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            sl.unlockWrite(stamp);
            log("Writer released write lock");
        }
    }
    
    /**
     * Optimistic read with automatic fallback to pessimistic read
     * This is the key feature that makes StampedLock so performant
     */
    public double optimisticRead(int readerId) {
        // Phase 1: Try optimistic read (lock-free)
        long stamp = sl.tryOptimisticRead();
        double currentX = x, currentY = y; // Non-atomic read
        
        log("Reader-" + readerId + " attempting optimistic read: x=" + currentX + 
            ", y=" + currentY + " (stamp: " + stamp + ")");
        
        try {
            // Simulate computation using the read values
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        }
        
        // Phase 2: Validate optimistic read
        if (!sl.validate(stamp)) {
            // Optimistic read failed - fall back to pessimistic read
            pessimisticFallbackCount++;
            log("Reader-" + readerId + " optimistic read FAILED (stamp invalidated), " +
                "falling back to pessimistic read (#" + pessimisticFallbackCount + ")");
            
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
                log("Reader-" + readerId + " pessimistic read: x=" + currentX + ", y=" + currentY);
            } finally {
                sl.unlockRead(stamp);
            }
        } else {
            optimisticSuccessCount++;
            log("Reader-" + readerId + " optimistic read SUCCEEDED! (#" + optimisticSuccessCount + 
                ") - no locking required");
        }
        
        // Return computed result
        double result = Math.sqrt(currentX * currentX + currentY * currentY);
        log("Reader-" + readerId + " computed distance: " + String.format("%.2f", result));
        return result;
    }
    
    private void log(String message) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(timeFormat), message);
    }
}