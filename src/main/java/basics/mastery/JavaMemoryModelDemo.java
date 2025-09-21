package basics.mastery;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Demonstrates Java Memory Model concepts:
 * - Visibility issues without proper synchronization
 * - volatile keyword effects
 * - Happens-before relationships
 * - Atomic operations and CAS
 */
public class JavaMemoryModelDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java Memory Model Demonstrations ===\n");
        
        // 1. Visibility problem demonstration
        demonstrateVisibilityProblem();
        
        // 2. volatile solution
        demonstrateVolatileSolution();
        
        // 3. Atomic operations
        demonstrateAtomicOperations();
        
        // 4. Compare-and-Swap (CAS) mechanics
        demonstrateCASOperations();
        
        // 5. ABA problem
        demonstrateABAProblem();
    }
    
    /**
     * Shows the infamous infinite loop caused by visibility issues
     */
    private static void demonstrateVisibilityProblem() throws InterruptedException {
        System.out.println("1. Visibility Problem Demonstration:");
        System.out.println("Starting reader thread that polls a shared variable...");
        
        VisibilityProblemExample example = new VisibilityProblemExample();
        
        // Start reader thread
        Thread readerThread = new Thread(example::readLoop, "Reader-Thread");
        readerThread.start();
        
        // Give reader time to start polling
        Thread.sleep(100);
        
        System.out.println("Main thread: Setting flag to true...");
        example.setFlag(true);
        
        // Wait a bit to see if reader notices the change
        Thread.sleep(2000);
        
        if (readerThread.isAlive()) {
            System.out.println("❌ Reader thread is still running - visibility problem demonstrated!");
            System.out.println("The reader thread may not see the flag change due to CPU caching.");
            readerThread.interrupt(); // Force stop
        } else {
            System.out.println("✅ Reader thread stopped - no visibility issue (JVM optimized this case)");
        }
        
        System.out.println();
    }
    
    /**
     * Shows how volatile solves visibility issues
     */
    private static void demonstrateVolatileSolution() throws InterruptedException {
        System.out.println("2. Volatile Solution:");
        System.out.println("Using volatile keyword to ensure visibility...");
        
        VolatileSolutionExample example = new VolatileSolutionExample();
        
        Thread readerThread = new Thread(example::readLoop, "Volatile-Reader");
        readerThread.start();
        
        Thread.sleep(100);
        
        System.out.println("Main thread: Setting volatile flag to true...");
        example.setFlag(true);
        
        // Wait for reader to finish
        readerThread.join(1000);
        
        if (!readerThread.isAlive()) {
            System.out.println("✅ Reader thread stopped immediately - volatile ensures visibility!");
        } else {
            System.out.println("❌ Something went wrong with volatile demonstration");
            readerThread.interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * Demonstrates atomic operations for thread-safe counters
     */
    private static void demonstrateAtomicOperations() throws InterruptedException {
        System.out.println("3. Atomic Operations Demonstration:");
        
        // Compare non-atomic vs atomic counter
        System.out.println("Testing thread-safe counter implementations...");
        
        int threadCount = 10;
        int incrementsPerThread = 1000;
        int expectedTotal = threadCount * incrementsPerThread;
        
        // Test non-atomic counter
        NonAtomicCounter nonAtomic = new NonAtomicCounter();
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    nonAtomic.increment();
                }
            });
        }
        
        long start = System.nanoTime();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long nonAtomicTime = System.nanoTime() - start;
        
        System.out.printf("Non-atomic counter result: %d (expected: %d) - %s%n",
            nonAtomic.getValue(), expectedTotal,
            nonAtomic.getValue() == expectedTotal ? "✅ Correct" : "❌ Race condition!");
        
        // Test atomic counter
        java.util.concurrent.atomic.AtomicInteger atomic = new java.util.concurrent.atomic.AtomicInteger(0);
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    atomic.incrementAndGet();
                }
            });
        }
        
        start = System.nanoTime();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long atomicTime = System.nanoTime() - start;
        
        System.out.printf("Atomic counter result: %d (expected: %d) - %s%n",
            atomic.get(), expectedTotal,
            atomic.get() == expectedTotal ? "✅ Correct" : "❌ Something's wrong!");
        
        System.out.printf("Performance: Non-atomic %.2f ms, Atomic %.2f ms%n%n",
            nonAtomicTime / 1_000_000.0, atomicTime / 1_000_000.0);
    }
    
    /**
     * Demonstrates Compare-and-Swap (CAS) mechanics
     */
    private static void demonstrateCASOperations() {
        System.out.println("4. Compare-and-Swap (CAS) Operations:");
        
        AtomicInteger atomicInt = new AtomicInteger(10);
        
        // Successful CAS
        boolean success = atomicInt.compareAndSet(10, 20);
        System.out.printf("CAS(10 -> 20): %s, value is now: %d%n", success, atomicInt.get());
        
        // Failed CAS (current value is 20, not 10)
        success = atomicInt.compareAndSet(10, 30);
        System.out.printf("CAS(10 -> 30): %s, value is still: %d%n", success, atomicInt.get());
        
        // Demonstrate atomic increment using CAS
        System.out.println("\nImplementing increment using CAS:");
        int oldValue, newValue;
        do {
            oldValue = atomicInt.get();
            newValue = oldValue + 1;
            System.out.printf("Attempting CAS(%d -> %d)... ", oldValue, newValue);
        } while (!atomicInt.compareAndSet(oldValue, newValue));
        
        System.out.printf("Success! Value is now: %d%n%n", atomicInt.get());
    }
    
    /**
     * Demonstrates the ABA problem in concurrent programming
     */
    private static void demonstrateABAProblem() throws InterruptedException {
        System.out.println("5. ABA Problem Demonstration:");
        System.out.println("Simulating a scenario where value changes A -> B -> A");
        
        AtomicReference<String> sharedRef = new AtomicReference<>("A");
        
        // Thread 1: Will try to change A to X, but gets delayed
        Thread thread1 = new Thread(() -> {
            String currentValue = sharedRef.get();
            System.out.printf("Thread1: Read value '%s', preparing to change to 'X'%n", currentValue);
            
            // Simulate delay (context switch, GC, etc.)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            
            // Try to update using the old value
            boolean success = sharedRef.compareAndSet(currentValue, "X");
            System.out.printf("Thread1: CAS('%s' -> 'X') = %s, actual value: '%s'%n", 
                currentValue, success, sharedRef.get());
            
            if (success) {
                System.out.println("❌ ABA problem occurred! Thread1 succeeded despite intermediate changes.");
            }
        }, "ABA-Thread1");
        
        // Thread 2: Will change A -> B -> A quickly
        Thread thread2 = new Thread(() -> {
            // Small delay to let thread1 read the initial value
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            
            // Change A to B
            sharedRef.compareAndSet("A", "B");
            System.out.println("Thread2: Changed A -> B");
            
            // Change B back to A
            sharedRef.compareAndSet("B", "A");
            System.out.println("Thread2: Changed B -> A");
            
        }, "ABA-Thread2");
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        System.out.println("\n💡 ABA Problem Solution: Use AtomicStampedReference or version numbers");
        System.out.println("   This adds a 'stamp' or version that prevents false CAS success.\n");
    }
}

/**
 * Example class showing visibility problems
 */
class VisibilityProblemExample {
    private boolean flag = false; // NOT volatile - may cause visibility issues
    
    public void readLoop() {
        System.out.println("Reader: Starting to poll flag...");
        
        while (!flag) {
            // Busy wait - this loop might never terminate
            // if the flag change is not visible to this thread
        }
        
        System.out.println("Reader: Flag became true, exiting loop");
    }
    
    public void setFlag(boolean value) {
        this.flag = value;
    }
}

/**
 * Example class using volatile to fix visibility
 */
class VolatileSolutionExample {
    private volatile boolean flag = false; // volatile ensures visibility
    
    public void readLoop() {
        System.out.println("Volatile Reader: Starting to poll flag...");
        
        while (!flag) {
            // This loop WILL terminate when flag is set by another thread
            // volatile ensures immediate visibility of changes
        }
        
        System.out.println("Volatile Reader: Flag became true, exiting loop");
    }
    
    public void setFlag(boolean value) {
        this.flag = value;
    }
}

/**
 * Non-thread-safe counter (prone to race conditions)
 */
class NonAtomicCounter {
    private int count = 0;
    
    public void increment() {
        count++; // This is NOT atomic: read, increment, write
    }
    
    public int getValue() {
        return count;
    }
}
