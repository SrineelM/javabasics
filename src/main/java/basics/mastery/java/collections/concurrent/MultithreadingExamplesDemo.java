package basics.mastery.java.collections.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import basics.mastery.collections.CollectionDemo;

/**
 * ============================================================================
 * JAVA COACH TUTORIAL: MULTITHREADING & CONCURRENCY MASTERCLASS (JDK 17 - 21)
 * ============================================================================
 * 
 * Target Audience: Beginner -> Intermediate -> Senior Java Engineers
 * 
 * Objectives:
 *   1. Understand Thread Lifecycles, States, and Interruption mechanics.
 *   2. Demystify Race Conditions, the Java Memory Model (JMM), and Happens-Before.
 *   3. Contrast synchronized vs ReentrantLock vs ReadWriteLock.
 *   4. Master ExecutorService and non-blocking asynchronous CompletableFuture pipelines.
 *   5. Embrace the Java 21 revolution: Virtual Threads (JEP 444) & Structured Concurrency.
 *   6. Master synchronization coordination: CountDownLatch, CyclicBarrier, Semaphore.
 *   7. Identify and resolve Deadlocks, Livelocks, and Thread Starvation.
 * 
 * Concurrency Hierarchy at a glance:
 * 
 *   [Low Level]         Thread, Runnable, synchronized, volatile, wait()/notify()
 *         │
 *   [Explicit Locks]    Lock, ReentrantLock, ReadWriteLock, StampedLock, Condition
 *         │
 *   [Coordination]      CountDownLatch, CyclicBarrier, Semaphore, Phaser, Exchanger
 *         │
 *   [Executors/Tasks]   ExecutorService, ThreadPoolExecutor, Future, Callable, ForkJoinPool
 *         │
 *   [Async Reactive]    CompletableFuture (Pipeline orchestration, join, combine)
 *         │
 *   [Java 21 Modern]    Virtual Threads (Thread.ofVirtual()), StructuredTaskScope
 * 
 * @author Srineel with Copilot & Expert Java Coach
 * @version 3.0
 * @since Java 17 (Enhanced for Java 21)
 */
public class MultithreadingExamplesDemo extends CollectionDemo {

    public static void main(String[] args) {
        new MultithreadingExamplesDemo().demonstrateAll();
    }

    @Override
    public void demonstrateAll() {
        printHeader("MULTITHREADING & CONCURRENCY: FROM ZERO TO HERO (JDK 17-21)");

        // 1. Thread Basics, States, Lifecycle & Proper Interruption Handling
        demonstrateThreadBasicsAndInterruption();

        // 2. Race Conditions: Unsynchronized vs Synchronized vs AtomicInteger
        demonstrateRaceConditions();

        // 3. Volatile vs Atomic: Visibility, CPU Caches & Happens-Before
        demonstrateVolatileAndMemoryVisibility();

        // 4. Explicit Locks: ReentrantLock & ReentrantReadWriteLock
        demonstrateExplicitLocks();

        // 5. ExecutorService & Thread Pool Mechanics
        demonstrateExecutorService();

        // 6. Asynchronous Pipelines with CompletableFuture
        demonstrateCompletableFuturePipelines();

        // 7. Java 21 Revolution: Virtual Threads (JEP 444)
        demonstrateVirtualThreadsJava21();

        // 8. Synchronization Primitives: CountDownLatch, CyclicBarrier & Semaphore
        demonstrateCoordinationPrimitives();

        // 9. Deadlock Demonstration & Lock-Ordering Prevention
        demonstrateDeadlockAndAvoidance();

        // 10. Senior Interview Masterclass & Production Checklist
        demonstrateInterviewMasterclass();

        printSectionComplete("Multithreading & Concurrency Masterclass");
    }

    // =========================================================================
    // 1. THREAD BASICS, LIFECYCLE & INTERRUPTION
    // =========================================================================
    /**
     * Demonstrates Thread creation, thread states, and the correct way to handle interruptions.
     * 
     * [COACH: THREAD LIFECYCLE & STATES]
     * A Java thread exists in one of 6 states (defined in {@link Thread.State}):
     * 1. NEW: Instantiated, before start() is invoked.
     * 2. RUNNABLE: Executing in JVM or waiting for OS CPU scheduling.
     * 3. BLOCKED: Waiting to acquire an intrinsic monitor lock (`synchronized`).
     * 4. WAITING: Waiting indefinitely for another thread (e.g. `wait()`, `join()`, `LockSupport.park()`).
     * 5. TIMED_WAITING: Waiting with a specified timeout (e.g. `sleep(ms)`, `join(ms)`, `await(ms)`).
     * 6. TERMINATED: Completed execution or unhandled exception thrown.
     * 
     * [COACH: PROPER INTERRUPTION HANDLING]
     * Never swallow an `InterruptedException` with an empty catch block!
     * When `InterruptedException` is thrown, the JVM CLEARS the thread's interrupt flag.
     * You MUST either:
     *   A) Re-throw the exception, OR
     *   B) Call `Thread.currentThread().interrupt()` to restore the interrupt status!
     */
    private void demonstrateThreadBasicsAndInterruption() {
        printSubHeader("1. Thread Basics, States & Clean Interruption");

        printInfo("Creating and starting a thread with lambda:");
        Thread workerThread = new Thread(() -> {
            String name = Thread.currentThread().getName();
            System.out.println("     [" + name + "] Started. Current state: " + Thread.currentThread().getState());
            try {
                // TIMED_WAITING state
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("     [" + name + "] Interrupted while sleeping! Restoring interrupt flag.");
                Thread.currentThread().interrupt(); // Restore interrupt status!
                return;
            }
            System.out.println("     [" + name + "] Completed task normally.");
        }, "Worker-1");

        printResult("Thread State before start()", workerThread.getState()); // NEW
        workerThread.start();
        printResult("Thread State after start()", workerThread.getState());  // RUNNABLE

        try {
            workerThread.join(); // Main thread waits for workerThread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        printResult("Thread State after join()", workerThread.getState());   // TERMINATED

        // Clean Interruption Demonstration
        printInfo("Demonstrating clean graceful thread interruption:");
        Thread interruptibleTask = new Thread(() -> {
            long count = 0;
            // Check interrupt flag in CPU-bound loops
            while (!Thread.currentThread().isInterrupted()) {
                count++;
                if (count % 50_000_000 == 0) {
                    // Small pause
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("     [InterruptedTask] Caught InterruptedException! Exiting cleanly.");
                        Thread.currentThread().interrupt(); // Crucial: restore flag!
                        break;
                    }
                }
            }
            System.out.println("     [InterruptedTask] Clean shutdown confirmed.");
        }, "InterruptibleTask");

        interruptibleTask.start();
        try {
            Thread.sleep(50);
            interruptibleTask.interrupt(); // Signal thread to stop
            interruptibleTask.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printTip("[COACH BEST PRACTICE] Thread.stop() and Thread.suspend() are deprecated and dangerously unsafe! Always use cooperative interruption with interrupt() and isInterrupted().");
    }

    // =========================================================================
    // 2. RACE CONDITIONS: UNSYNCHRONIZED VS SYNCHRONIZED VS ATOMIC
    // =========================================================================
    /**
     * Demonstrates an actual Race Condition and shows the three progressive ways to solve it.
     * 
     * [COACH: WHAT IS A RACE CONDITION?]
     * `count++` looks like 1 line of Java, but in CPU assembly it is THREE operations:
     *   1. READ: Load current value of count from memory into CPU register.
     *   2. MODIFY: Increment the register by 1.
     *   3. WRITE: Store the register back into memory.
     * When two threads interleave these 3 steps, one thread overwrites the other's increment!
     */
    private void demonstrateRaceConditions() {
        printSubHeader("2. Race Conditions: The Danger of Unsynchronized Mutations");

        final int targetCount = 20_000;
        final int halfCount = targetCount / 2;

        // Test A: Unsynchronized Counter (RACE CONDITION!)
        class UnsafeCounter {
            int count = 0;
            void increment() { count++; } // Non-atomic read-modify-write!
        }
        UnsafeCounter unsafe = new UnsafeCounter();
        Thread t1 = new Thread(() -> { for (int i = 0; i < halfCount; i++) unsafe.increment(); });
        Thread t2 = new Thread(() -> { for (int i = 0; i < halfCount; i++) unsafe.increment(); });
        t1.start(); t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        printResult("Unsafe Counter (Expected " + targetCount + ")", unsafe.count + " (LOST UPDATES!)");

        // Test B: Synchronized Method (Mutual Exclusion Lock)
        class SynchronizedCounter {
            int count = 0;
            synchronized void increment() { count++; } // Acquires monitor on 'this'
        }
        SynchronizedCounter syncCounter = new SynchronizedCounter();
        Thread t3 = new Thread(() -> { for (int i = 0; i < halfCount; i++) syncCounter.increment(); });
        Thread t4 = new Thread(() -> { for (int i = 0; i < halfCount; i++) syncCounter.increment(); });
        t3.start(); t4.start();
        try { t3.join(); t4.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        printResult("Synchronized Counter (Expected " + targetCount + ")", syncCounter.count + " (ACCURATE)");

        // Test C: Lock-Free AtomicInteger (Hardware CAS)
        AtomicInteger atomicCounter = new AtomicInteger(0);
        Thread t5 = new Thread(() -> { for (int i = 0; i < halfCount; i++) atomicCounter.incrementAndGet(); });
        Thread t6 = new Thread(() -> { for (int i = 0; i < halfCount; i++) atomicCounter.incrementAndGet(); });
        t5.start(); t6.start();
        try { t5.join(); t6.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        printResult("AtomicInteger Counter (Expected " + targetCount + ")", atomicCounter.get() + " (ACCURATE & LOCK-FREE)");
    }

    // =========================================================================
    // 3. VOLATILE & JAVA MEMORY MODEL (JMM)
    // =========================================================================
    /**
     * Demonstrates the `volatile` keyword and memory visibility.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Modern CPUs have multi-level caches (L1, L2, L3).
     * - Without `volatile`, a thread may cache a variable in an L1 register indefinitely,
     *   never observing writes made by another CPU core!
     * - `volatile` guarantees:
     *   1. Visibility: Reads and writes go directly to main memory (L3 / RAM).
     *   2. Ordering: Inserts CPU memory barriers (LoadLoad, StoreStore) preventing instruction reordering.
     * - [CRITICAL] `volatile` does NOT guarantee atomicity for compound operations like `count++`!
     */
    private void demonstrateVolatileAndMemoryVisibility() {
        printSubHeader("3. Volatile & Memory Visibility (Happens-Before Relationship)");

        printInfo("Testing volatile termination flag between producer and watcher threads:");

        class WorkerTask {
            // volatile guarantees that when writer sets running = false, reader immediately sees it!
            volatile boolean running = true;

            void stop() { running = false; }

            void doWork() {
                long iterations = 0;
                while (running) {
                    iterations++;
                }
                System.out.println("     [Worker] Observed running = false after " + iterations + " iterations. Stopped.");
            }
        }

        WorkerTask task = new WorkerTask();
        Thread worker = new Thread(task::doWork, "Volatile-Worker");
        worker.start();

        try {
            Thread.sleep(50); // Let worker spin
            task.stop();     // Main thread writes to volatile field
            worker.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printResult("Worker termination", !worker.isAlive() ? "Terminated cleanly" : "Hung (Visibility failure!)");
        printWarning("[PITFALL] volatile is NOT a replacement for synchronized! It provides visibility, NOT atomicity.");
    }

    // =========================================================================
    // 4. EXPLICIT LOCKS: REENTRANTLOCK & READWRITELOCK
    // =========================================================================
    /**
     * Demonstrates {@link ReentrantLock} and {@link ReentrantReadWriteLock}.
     * 
     * [COACH: REENTRANTLOCK VS SYNCHRONIZED]
     * Why use ReentrantLock over synchronized?
     * 1. tryLock(timeout): Avoid deadlocks by abandoning the lock attempt if not acquired in time!
     * 2. lockInterruptibly(): Allows responding to thread interruptions while waiting for a lock.
     * 3. Fairness option: `new ReentrantLock(true)` guarantees FIFO lock grant order (avoids starvation).
     * 4. Multiple Condition variables: e.g., `notFull` and `notEmpty` conditions on the same lock.
     */
    private void demonstrateExplicitLocks() {
        printSubHeader("4. Explicit Locks: ReentrantLock & ReentrantReadWriteLock");

        // A. ReentrantLock with tryLock() timeout
        Lock reentrantLock = new ReentrantLock();

        Thread holder = new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("     [Holder] Lock acquired. Holding for 150ms...");
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                reentrantLock.unlock(); // Always unlock in finally!
                System.out.println("     [Holder] Lock released.");
            }
        });

        Thread requester = new Thread(() -> {
            try {
                Thread.sleep(20); // Let holder lock first
                System.out.println("     [Requester] Attempting tryLock(50ms)...");
                boolean acquired = reentrantLock.tryLock(50, TimeUnit.MILLISECONDS);
                if (acquired) {
                    try {
                        System.out.println("     [Requester] Acquired lock!");
                    } finally {
                        reentrantLock.unlock();
                    }
                } else {
                    System.out.println("     [Requester] Timed out! Did not block indefinitely. Avoiding deadlock!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        holder.start();
        requester.start();
        try { holder.join(); requester.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // B. ReentrantReadWriteLock: High read concurrency
        printInfo("ReentrantReadWriteLock allows multiple simultaneous readers, but exclusive writes:");
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        Lock readLock = rwLock.readLock();
        Lock writeLock = rwLock.writeLock();

        // Simulate 3 concurrent readers
        List<Thread> readers = IntStream.range(1, 4).mapToObj(i -> new Thread(() -> {
            readLock.lock();
            try {
                System.out.println("     [Reader " + i + "] Reading shared data concurrently...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                readLock.unlock();
            }
        })).toList();

        readers.forEach(Thread::start);
        for (Thread r : readers) {
            try { r.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        printTip("[COACH BEST PRACTICE] ALWAYS structure explicit locks with `lock.lock(); try { ... } finally { lock.unlock(); }`. Forgetting finally causes permanent deadlocks!");
    }

    // =========================================================================
    // 5. EXECUTOR SERVICE & THREAD POOL MECHANICS
    // =========================================================================
    /**
     * Demonstrates {@link ExecutorService} and thread pools.
     * 
     * [COACH: WHY NEVER USE 'new Thread()' IN PRODUCTION]
     * Creating an OS thread is expensive (~1MB stack memory, kernel context switch).
     * Thread pools reuse a fixed set of worker threads to execute thousands of tasks.
     * 
     * Standard Pool Types:
     * - `newFixedThreadPool(n)`: Fixed number of threads with unbounded LinkedBlockingQueue.
     * - `newCachedThreadPool()`: Dynamic pool (0 to Integer.MAX_VALUE) with SynchronousQueue.
     * - `newScheduledThreadPool(n)`: Supports delayed and periodic task scheduling.
     */
    private void demonstrateExecutorService() {
        printSubHeader("5. ExecutorService - Thread Pools, Callable & Future");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        printInfo("Submitting 5 tasks to a 3-thread fixed pool:");

        List<Future<String>> futures = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            Callable<String> task = () -> {
                String threadName = Thread.currentThread().getName();
                Thread.sleep(50);
                return "Task_" + taskId + " executed by [" + threadName + "]";
            };
            futures.add(executor.submit(task));
        }

        // Collect results
        for (Future<String> future : futures) {
            try {
                // future.get() blocks until the task completes
                String result = future.get(500, TimeUnit.MILLISECONDS);
                System.out.println("     " + result);
            } catch (Exception e) {
                printWarning("Task failed or timed out: " + e.getMessage());
            }
        }

        // Graceful shutdown pattern
        executor.shutdown(); // Reject new tasks, complete existing
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel executing tasks
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        printResult("Executor shutdown status", executor.isTerminated() ? "Cleanly Terminated" : "Running");
    }

    // =========================================================================
    // 6. ASYNCHRONOUS PIPELINES WITH COMPLETABLEFUTURE
    // =========================================================================
    /**
     * Demonstrates non-blocking reactive pipelines using {@link CompletableFuture}.
     * 
     * Pipeline pattern:
     * 1. supplyAsync: Fetch User profile
     * 2. supplyAsync: Fetch User credit score (parallel!)
     * 3. thenCombine: Combine profile and score to make loan decision
     * 4. exceptionally: Handle errors without crashing
     */
    private void demonstrateCompletableFuturePipelines() {
        printSubHeader("6. Asynchronous Pipelines with CompletableFuture");

        printInfo("Assembling a parallel asynchronous pipeline with CompletableFuture:");

        CompletableFuture<String> fetchUser = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(60); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "User(Alice, Age=30)";
        });

        CompletableFuture<Integer> fetchCreditScore = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(40); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return 780; // High score
        });

        // Combine both independent async tasks without blocking!
        CompletableFuture<String> loanApproval = fetchUser.thenCombine(fetchCreditScore, (user, score) -> {
            boolean approved = score > 700;
            return user + " -> Loan Approved? " + (approved ? "YES (Tier 1)" : "NO");
        }).exceptionally(ex -> "Pipeline Error: " + ex.getMessage());

        // join() gets the final result
        String decision = loanApproval.join();
        printResult("Loan Pipeline Result", decision);

        // Chaining transformations with thenApply:
        CompletableFuture<Integer> pipeline = CompletableFuture.supplyAsync(() -> "42")
                .thenApply(Integer::parseInt)
                .thenApply(n -> n * 2);
        printResult("Chained Transformations result", pipeline.join());

        printTip("[INTERVIEW TIP] CompletableFuture by default executes on the shared ForkJoinPool.commonPool(). For IO-heavy tasks, always provide a custom ExecutorService!");
    }

    // =========================================================================
    // 7. JAVA 21 VIRTUAL THREADS (JEP 444)
    // =========================================================================
    /**
     * Demonstrates Java 21 Virtual Threads.
     * 
     * [COACH: JAVA 21 GAME CHANGER]
     * - Platform Thread (traditional): 1-to-1 mapping with an OS kernel thread. Heavy (~1MB stack, limited to thousands).
     * - Virtual Thread (Java 21): M-to-N mapping. Millions can run simultaneously. Stack is stored in heap memory.
     * - When a Virtual Thread encounters blocking IO (database query, HTTP call, `Thread.sleep`),
     *   the JVM unmounts it from its OS carrier thread!
     * - [PINNING PITFALL] If a virtual thread enters a `synchronized` block or native method, it becomes "pinned"
     *   to its carrier thread. Always replace `synchronized` with `ReentrantLock` in virtual thread code!
     */
    private void demonstrateVirtualThreadsJava21() {
        printSubHeader("7. [JDK 21] Virtual Threads (JEP 444) - Millions of Threads");

        printInfo("Demonstrating Virtual Thread creation and executor per-task pattern:");

        // A. Basic Virtual Thread creation
        Thread vThread = Thread.ofVirtual().name("MyVirtualThread").start(() -> {
            System.out.println("     [Virtual Thread] Running: " + Thread.currentThread() + ", isVirtual=" + Thread.currentThread().isVirtual());
        });
        try { vThread.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // B. New Virtual Thread Per Task Executor
        final int taskCount = 10_000;
        printInfo("Launching " + formatNumber(taskCount) + " concurrent virtual threads doing simulated IO:");

        double duration = measureTime(() -> {
            try (ExecutorService vExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < taskCount; i++) {
                    vExecutor.submit(() -> {
                        try {
                            Thread.sleep(10); // Simulated non-blocking IO wait
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
            } // Auto-closes and waits for all virtual threads to finish!
        });

        printBenchmark(taskCount + " Virtual Threads completed in", duration);
        printTip("[COACH: JDK 21 BEST PRACTICE] Never pool Virtual Threads! They are cheap and disposable. Use Executors.newVirtualThreadPerTaskExecutor() instead.");
    }

    // =========================================================================
    // 8. COORDINATION PRIMITIVES: COUNTDOWNLATCH, CYCLICBARRIER & SEMAPHORE
    // =========================================================================
    /**
     * Demonstrates synchronization coordination tools.
     */
    private void demonstrateCoordinationPrimitives() {
        printSubHeader("8. Coordination Primitives: CountDownLatch, CyclicBarrier & Semaphore");

        // A. CountDownLatch: One-shot gate (wait for 3 worker threads to finish)
        printInfo("A. CountDownLatch: Waiting for 3 subsystem initialization tasks:");
        CountDownLatch startupLatch = new CountDownLatch(3);

        for (int i = 1; i <= 3; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    Thread.sleep(30 * id);
                    System.out.println("     [Subsystem " + id + "] Initialized.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    startupLatch.countDown(); // Decrement counter
                }
            }).start();
        }

        try {
            startupLatch.await(1, TimeUnit.SECONDS); // Block until count reaches 0
            System.out.println("     [Main Engine] All subsystems ready. Server started!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // B. Semaphore: Rate-limiting access to 2 simultaneous permits
        printInfo("B. Semaphore: Rate-limiting resource to max 2 concurrent users:");
        Semaphore dbConnectionPool = new Semaphore(2);

        List<Thread> clients = IntStream.range(1, 5).mapToObj(i -> new Thread(() -> {
            try {
                dbConnectionPool.acquire(); // Acquire permit (blocks if 0 available)
                System.out.println("     [Client " + i + "] Acquired DB connection permit.");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("     [Client " + i + "] Releasing permit.");
                dbConnectionPool.release(); // Return permit
            }
        })).toList();

        clients.forEach(Thread::start);
        for (Thread c : clients) {
            try { c.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    // =========================================================================
    // 9. DEADLOCK DEMONSTRATION & AVOIDANCE
    // =========================================================================
    /**
     * Demonstrates how Deadlocks occur (Coffman conditions: circular wait)
     * and shows the universal solution: Consistent Lock Ordering.
     */
    private void demonstrateDeadlockAndAvoidance() {
        printSubHeader("9. Deadlock Analysis & Universal Prevention (Lock Ordering)");

        printInfo("How deadlocks happen: Thread 1 holds Resource A and requests B; Thread 2 holds B and requests A.");

        Object lockA = new Object();
        Object lockB = new Object();

        // Safe Lock Ordering Implementation:
        // Always acquire lockA before lockB across all threads!
        Runnable safeTransfer = () -> {
            synchronized (lockA) {
                System.out.println("     [" + Thread.currentThread().getName() + "] Acquired Lock A.");
                synchronized (lockB) {
                    System.out.println("     [" + Thread.currentThread().getName() + "] Acquired Lock B. Performing transfer.");
                }
            }
        };

        Thread t1 = new Thread(safeTransfer, "TransferThread-1");
        Thread t2 = new Thread(safeTransfer, "TransferThread-2");
        t1.start(); t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        printResult("Deadlock Prevention", "Consistent lock hierarchy eliminates circular wait!");
    }

    // =========================================================================
    // 10. SENIOR INTERVIEW MASTERCLASS & PRODUCTION CHECKLIST
    // =========================================================================
    private void demonstrateInterviewMasterclass() {
        printSubHeader("10. Best Practices, Pitfalls & Senior Interview Q&A Masterclass");

        printInfo("======================= TOP INTERVIEW QUESTIONS & ANSWERS =======================");
        System.out.println("""
          Q1: What is the difference between synchronized and ReentrantLock?
          A1: `synchronized` is an intrinsic language keyword that automatically acquires and releases
              locks with block scope. `ReentrantLock` is an explicit API offering advanced capabilities:
              timed attempts (tryLock), interruptible lock acquisition (lockInterruptibly), fairness policy,
              and multiple condition variables (Condition).

          Q2: What is the purpose of the volatile keyword in Java?
          A2: `volatile` ensures memory visibility and prevents instruction reordering. Writes to a volatile
              variable are immediately flushed to main memory, establishing a happens-before relationship
              with subsequent reads. It does NOT make compound operations (e.g. count++) atomic.

          Q3: How do Virtual Threads in Java 21 differ from Platform Threads?
          A3: Platform Threads are 1:1 wrappers around OS kernel threads, costing ~1MB stack space.
              Virtual Threads (JEP 444) are managed by the JVM, allocated on the heap, and cost only a few
              hundred bytes. When blocked on IO, the JVM unmounts the virtual thread from its carrier OS thread.

          Q4: What causes Virtual Thread 'Pinning' and how do you avoid it?
          A4: Pinning occurs when a virtual thread enters a `synchronized` block/method or executes a native
              method (JNI). The virtual thread cannot unmount from its carrier thread. To avoid pinning,
              replace `synchronized` blocks with `ReentrantLock` in high-throughput virtual thread code.

          Q5: How should you handle an InterruptedException properly?
          A5: Never swallow it! Either propagate it up the call stack by re-throwing it, or restore the
              interrupted status by invoking `Thread.currentThread().interrupt()` before returning.
        """);
    }
}
