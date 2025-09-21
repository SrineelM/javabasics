package io.mastery;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
please re * ConcurrencyPitfallsDemo
 *
 * Comprehensive demonstration of the "Big Four" concurrency bugs:
 * 1. Deadlock
 * 2. Livelock
 * 3. Race Conditions
 * 4. Starvation
 *
 * Includes detection and resolution strategies, plus advanced debugging techniques.
 *
 * This file merges logic from both concurrency_pitfalls.java and concurrency_pitfalls(1).java.
 *
 * Each section is clearly commented for educational purposes.
 */
public class ConcurrencyPitfallsDemo {
    // Formatter for time-stamped logs
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Concurrency Pitfalls Demonstration ===\n");

        // 1. Deadlock demonstration and detection
        demonstrateDeadlock();

        // 2. Livelock demonstration
        demonstrateLivelock();

        // 3. Race condition examples
        demonstrateRaceConditions();

        // 4. Starvation scenarios
        demonstrateStarvation();

        // 5. Advanced debugging techniques
        demonstrateDebuggingTechniques();
    }

    /**
     * 1. Deadlock: Two threads each holding one lock and waiting for the other
     */
    private static void demonstrateDeadlock() throws InterruptedException {
        System.out.println("1. Deadlock Demonstration:");
        System.out.println("Two threads will acquire locks in different orders...\n");

        DeadlockScenario scenario = new DeadlockScenario();
        Thread thread1 = new Thread(scenario::method1, "DeadlockThread-1");
        Thread thread2 = new Thread(scenario::method2, "DeadlockThread-2");
        thread1.start();
        thread2.start();
        Thread.sleep(2000);

        // Detect deadlock programmatically
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads != null) {
            System.out.println("\uD83D\uDEA8 DEADLOCK DETECTED!");
            System.out.println("Deadlocked thread IDs: " + Arrays.toString(deadlockedThreads));
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreads);
            for (ThreadInfo info : threadInfos) {
                System.out.printf("Thread '%s' is blocked waiting for lock owned by thread '%s'%n",
                        info.getThreadName(), info.getLockOwnerName());
            }
            // Force termination
            thread1.interrupt();
            thread2.interrupt();
        }
        thread1.join(1000);
        thread2.join(1000);
        System.out.println("\uD83D\uDCA1 Solution: Always acquire locks in the same order across all threads\n");
    }

    /**
     * 2. Livelock: Threads keep yielding to each other, making no progress
     */
    private static void demonstrateLivelock() throws InterruptedException {
        System.out.println("2. Livelock Demonstration:");
        System.out.println("Two overly polite threads trying to let each other go first...\n");
        LivelockScenario scenario = new LivelockScenario();
        Thread politeThread1 = new Thread(() -> scenario.attemptWork("Thread-1"), "LivelockThread-1");
        Thread politeThread2 = new Thread(() -> scenario.attemptWork("Thread-2"), "LivelockThread-2");
        politeThread1.start();
        politeThread2.start();
        Thread.sleep(3000);
        scenario.stop();
        politeThread1.join();
        politeThread2.join();
        System.out.println("\uD83D\uDCA1 Solution: Add randomization to break symmetry or use proper coordination\n");
    }

    /**
     * 3. Race Condition Demonstrations
     */
    private static void demonstrateRaceConditions() throws InterruptedException {
        System.out.println("3. Race Condition Demonstrations:");
        // 3a. Check-then-act race condition
        demonstrateCheckThenActRace();
        // 3b. Non-atomic compound operations
        demonstrateCompoundOperationRace();
        // 3c. Time-of-check vs time-of-use
        demonstrateTOCTOURace();
        System.out.println("\uD83D\uDCA1 Solutions: Use atomic operations, proper synchronization, or immutable objects\n");
    }

    // 3a. Check-then-act race
    private static void demonstrateCheckThenActRace() throws InterruptedException {
        System.out.println("3a. Check-Then-Act Race Condition:");
        CheckThenActExample example = new CheckThenActExample();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    example.problematicCheckThenAct(threadId);
                }
            }, "CheckThenAct-" + i);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        System.out.printf("Final counter value: %d (race conditions likely occurred)%n", example.getCounter());
        // Show the correct implementation
        example.demonstrateCorrectImplementation();
    }

    // 3b. Compound operation race
    private static void demonstrateCompoundOperationRace() throws InterruptedException {
        System.out.println("\n3b. Compound Operation Race Condition:");
        CompoundOperationExample example = new CompoundOperationExample();
        Thread incrementer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.incrementBoth();
            }
        }, "Incrementer");
        Thread checker = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.checkConsistency();
                try {
                    Thread.sleep(1); // Give incrementer chances to run
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Checker");
        incrementer.start();
        checker.start();
        incrementer.join();
        checker.join();
    }

    // 3c. Time-of-check vs time-of-use race
    private static void demonstrateTOCTOURace() throws InterruptedException {
        System.out.println("\n3c. Time-of-Check vs Time-of-Use Race:");
        TOCTOUExample example = new TOCTOUExample();
        Thread[] users = new Thread[3];
        for (int i = 0; i < users.length; i++) {
            final int userId = i;
            users[i] = new Thread(() -> {
                example.problematicWithdraw(userId, 300); // Try to withdraw more than balance
            }, "User-" + i);
        }
        for (Thread user : users) user.start();
        for (Thread user : users) user.join();
        System.out.printf("Final balance: %.2f (should be >= 0)%n", example.getBalance());
    }

    /**
     * 4. Starvation scenarios - threads unable to make progress
     */
    private static void demonstrateStarvation() throws InterruptedException {
        System.out.println("4. Starvation Demonstration:");
        System.out.println("High priority readers starving writers...\n");
        StarvationScenario scenario = new StarvationScenario();
        // Start one writer
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                scenario.write("Data-" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Writer");
        // Start many readers that keep the read lock busy
        Thread[] readers = new Thread[5];
        for (int i = 0; i < readers.length; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    scenario.read(readerId);
                    try {
                        Thread.sleep(50); // Brief pause, but readers keep coming
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "Reader-" + i);
        }
        writer.start();
        Thread.sleep(50); // Let writer try to start
        for (Thread reader : readers) reader.start();
        writer.join();
        for (Thread reader : readers) reader.join();
        System.out.println("\uD83D\uDCA1 Solution: Use fair locks or implement write priority policies\n");
    }

    /**
     * 5. Advanced debugging techniques demonstration
     */
    private static void demonstrateDebuggingTechniques() {
        System.out.println("5. Advanced Debugging Techniques:");
        // Thread dump analysis
        System.out.println("5a. Thread Dump Analysis:");
        printThreadDump();
        // Lock contention monitoring
        System.out.println("\n5b. Lock Contention Detection:");
        demonstrateLockContentionDetection();
        System.out.println("\uD83D\uDCA1 Use jstack, VisualVM, JFR, and ThreadMXBean for production debugging\n");
    }

    // Print a thread dump (first 3 threads)
    private static void printThreadDump() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        System.out.println("Current thread dump (showing first 3 threads):");
        for (int i = 0; i < Math.min(3, threadInfos.length); i++) {
            ThreadInfo info = threadInfos[i];
            System.out.printf("Thread: %s (ID: %d, State: %s)%n",
                    info.getThreadName(), info.getThreadId(), info.getThreadState());
            if (info.getLockName() != null) {
                System.out.printf("  Waiting for: %s (owned by: %s)%n",
                        info.getLockName(), info.getLockOwnerName());
            }
        }
    }

    // Demonstrate lock contention detection
    private static void demonstrateLockContentionDetection() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        if (threadMXBean.isThreadContentionMonitoringSupported()) {
            threadMXBean.setThreadContentionMonitoringEnabled(true);
            // Simulate some lock contention
            Object lock = new Object();
            Thread[] threads = new Thread[3];
            for (int i = 0; i < threads.length; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    synchronized (lock) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }, "ContentionThread-" + i);
                threads[i].start();
            }
            try {
                for (Thread t : threads) t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Check contention statistics
            for (Thread t : threads) {
                ThreadInfo info = threadMXBean.getThreadInfo(t.threadId());
                if (info != null) {
                    System.out.printf("Thread %s: Blocked count: %d, Blocked time: %d ms%n",
                            info.getThreadName(), info.getBlockedCount(), info.getBlockedTime());
                }
            }
        } else {
            System.out.println("Thread contention monitoring not supported on this JVM");
        }
    }

    // Utility log method
    private static void log(String message) {
        System.out.printf("[%s] %s: %s%n",
                LocalTime.now().format(TIME_FORMAT),
                Thread.currentThread().getName(),
                message);
    }
}

// --- Helper classes for each scenario ---

/**
 * Classic deadlock scenario with two locks
 */
class DeadlockScenario {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    public void method1() {
        synchronized (lock1) {
            log("Method1: Acquired lock1");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            log("Method1: Trying to acquire lock2...");
            synchronized (lock2) { log("Method1: Acquired lock2 - work completed!"); }
        }
    }
    public void method2() {
        synchronized (lock2) {
            log("Method2: Acquired lock2");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            log("Method2: Trying to acquire lock1...");
            synchronized (lock1) { log("Method2: Acquired lock1 - work completed!"); }
        }
    }
    private void log(String message) {
        System.out.printf("[%s] %s: %s%n",
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")),
                Thread.currentThread().getName(),
                message);
    }
}

/**
 * Livelock scenario - threads being overly polite
 */
class LivelockScenario {
    private volatile boolean thread1Working = false;
    private volatile boolean thread2Working = false;
    private volatile boolean shouldStop = false;
    public void attemptWork(String threadName) {
        while (!shouldStop) {
            if (threadName.equals("Thread-1")) {
                if (!thread2Working) {
                    thread1Working = true;
                    log(threadName + ": Starting work");
                    if (thread2Working) {
                        log(threadName + ": Oh, Thread-2 wants to work too, I'll step aside");
                        thread1Working = false;
                        continue;
                    }
                    try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
                    log(threadName + ": Finished work");
                    thread1Working = false;
                } else {
                    log(threadName + ": Thread-2 is working, I'll wait");
                }
            } else {
                if (!thread1Working) {
                    thread2Working = true;
                    log(threadName + ": Starting work");
                    if (thread1Working) {
                        log(threadName + ": Oh, Thread-1 wants to work too, I'll step aside");
                        thread2Working = false;
                        continue;
                    }
                    try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
                    log(threadName + ": Finished work");
                    thread2Working = false;
                } else {
                    log(threadName + ": Thread-1 is working, I'll wait");
                }
            }
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
        }
        log(threadName + ": Giving up (livelock detected)");
    }
    public void stop() { shouldStop = true; }
    private void log(String message) {
        System.out.printf("[%s] %s%n",
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")),
                message);
    }
}

/**
 * Check-then-act race condition example
 */
class CheckThenActExample {
    private int counter = 0;
    public void problematicCheckThenAct(int threadId) {
        if (counter < 100) { counter++; if (counter % 10 == 0) { System.out.printf("Thread-%d: Counter reached %d%n", threadId, counter); } }
    }
    public int getCounter() { return counter; }
    public void demonstrateCorrectImplementation() {
        System.out.println("\nCorrect implementation using synchronization:");
        AtomicCounter correctCounter = new AtomicCounter();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    correctCounter.increment(threadId);
                }
            }, "SyncCheckThenAct-" + i);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.printf("Final counter value (synchronized): %d%n", correctCounter.get());
    }
    // Synchronized counter helper
    static class AtomicCounter {
        private int value = 0;
        public synchronized void increment(int threadId) {
            if (value < 100) {
                value++;
                if (value % 10 == 0) {
                    System.out.printf("[Sync] Thread-%d: Counter reached %d%n", threadId, value);
                }
            }
        }
        public int get() { return value; }
    }
}

/**
 * Compound operation race condition example
 */
class CompoundOperationExample {
    private int a = 0, b = 0;
    public void incrementBoth() { a++; b++; }
    public void checkConsistency() { if (a != b) { System.out.printf("Inconsistency detected: a=%d, b=%d%n", a, b); } }
}

/**
 * Time-of-check vs time-of-use (TOCTOU) race example
 */
class TOCTOUExample {
    private double balance = 500.0;
    public void problematicWithdraw(int userId, double amount) {
        if (balance >= amount) {
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            balance -= amount;
            System.out.printf("User-%d withdrew %.2f, new balance: %.2f%n", userId, amount, balance);
        } else {
            System.out.printf("User-%d failed to withdraw %.2f, balance: %.2f%n", userId, amount, balance);
        }
    }
    public double getBalance() { return balance; }
}

/**
 * Starvation scenario - writer starved by readers
 */
class StarvationScenario {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public void write(String data) {
        lock.writeLock().lock();
        try {
            System.out.printf("Writer writing: %s%n", data);
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();
        }
    }
    public void read(int readerId) {
        lock.readLock().lock();
        try {
            System.out.printf("Reader-%d reading%n", readerId);
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.readLock().unlock();
        }
    }
}
