package basics.mastery.advanced;

/**
 * LockFreeCounterDemo
 *
 * Advanced demonstration of lock-free programming using CAS (compare-and-swap):
 * - Shows ABA-safe increment via a retry loop with backoff
 * - Contrasts with synchronized and AtomicInteger incrementAndGet()
 * - Explains contention impact and fairness considerations
 *
 * This class is additive and does not modify existing logic.
 *
 * @author Srineel with Copilot
 */
// [coach:fix] removed stray "// ...existing code..." placeholder that split the package from imports

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier; // [coach] import to avoid fully-qualified names

/**
 * LockFreeCounterDemo
 *
 * Advanced demonstration of lock-free programming using CAS (compare-and-swap):
 * - Shows ABA-safe increment via a retry loop with backoff
 * - Contrasts with synchronized and AtomicInteger incrementAndGet()
 * - Explains contention impact and fairness considerations
 *
 * This class is additive and does not modify existing logic.
 */
public class LockFreeCounterDemo {

    /**
     * [coach] Counter contract shared by all implementations so the benchmark
     * can be generic and polymorphic instead of an instanceof chain.
     * A functional interface works because each impl needs both increment() and get().
     */
    // [coach:fix] introduced interface to replace instanceof dispatch
    private interface Counter {
        int increment();
        int get();
    }

    private static class CASCounter implements Counter { // [coach:fix] implements Counter
        private final AtomicInteger value = new AtomicInteger(0);

        @Override // [coach]
        public int increment() {
            // Classic CAS retry loop.
            // Read current, compute next, and try to swap only if still == current.
            // The loop retries when another thread won the race.
            while (true) {
                int current = value.get();
                int next = current + 1;
                if (value.compareAndSet(current, next)) {
                    return next;
                }
                // Optional: brief on-CPU backoff to reduce contention.
                // Note: increment-only has no ABA problem (value is monotonic);
                // onSpinWait is a hint to the CPU, not a memory barrier.
                Thread.onSpinWait();
            }
        }

        @Override // [coach]
        public int get() { return value.get(); }
    }

    private static class SyncCounter implements Counter { // [coach:fix] implements Counter
        private int value = 0;
        // synchronized gives mutual exclusion + happens-before on the monitor.
        @Override // [coach]
        public synchronized int increment() { return ++value; }
        @Override // [coach]
        public synchronized int get() { return value; }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock-Free Counter Demo ===\n");

        // [coach] Supplier<Counter> makes runBenchmark type-safe and dispatch polymorphic.
        // [coach:fix] was Supplier<?> forcing instanceof checks inside the hot loop
        runBenchmark("CASCounter", CASCounter::new, 8, 200_000);
        runBenchmark("SyncCounter", SyncCounter::new, 8, 200_000);
        runBenchmark("AtomicInteger.incrementAndGet", AtomicIntegerCounter::new, 8, 200_000);
    }

    /**
     * [coach] Wrapper so a raw AtomicInteger fits the Counter contract.
     * Keeps the demo honest: we still benchmark AtomicInteger's own method.
     */
    private static final class AtomicIntegerCounter implements Counter {
        private final AtomicInteger value;
        AtomicIntegerCounter() { this.value = new AtomicInteger(0); }
        @Override public int increment() { return value.incrementAndGet(); }
        @Override public int get() { return value.get(); }
    }

    // Simple micro-benchmark (educational only; not a substitute for JMH).
    // [coach] Known limitations: no warmup, no JMH, possible DCE (results are used, so DCE is mitigated),
    // single-JVM, results not statistically validated. Treat numbers as indicative only.
    private static <T extends Counter> void runBenchmark(String name, Supplier<T> supplier,
                                                         int threads, int iterations) throws InterruptedException {
        System.out.printf("Benchmark: %s (%d threads x %d iters)\n", name, threads, iterations);

        // [coach:fix] typed as Counter; dispatch happens via virtual calls, not instanceof
        Counter counterObj = supplier.get();
        Thread[] ts = new Thread[threads];
        long start = System.nanoTime();

        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(() -> {
                // [coach:fix] removed instanceof chain; one virtual call per iteration
                for (int j = 0; j < iterations; j++) counterObj.increment();
            }, name + "-T" + i);
            ts[i].start();
        }

        for (Thread t : ts) t.join();
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        // [coach:fix] removed instanceof chain for result extraction
        int result = counterObj.get();

        System.out.printf("  Result: %,d | Time: %d ms\n\n", result, elapsedMs);
    }
}