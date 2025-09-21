package io.mastery.advanced;

import java.util.concurrent.atomic.AtomicInteger;

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

    private static class CASCounter {
        private final AtomicInteger value = new AtomicInteger(0);

        public int increment() {
            // Classic CAS retry loop
            while (true) {
                int current = value.get();
                int next = current + 1;
                if (value.compareAndSet(current, next)) {
                    return next;
                }
                // Optional: brief on-CPU backoff to reduce contention
                Thread.onSpinWait();
            }
        }

        public int get() { return value.get(); }
    }

    private static class SyncCounter {
        private int value = 0;
        public synchronized int increment() { return ++value; }
        public synchronized int get() { return value; }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock-Free Counter Demo ===\n");

        runBenchmark("CASCounter", () -> new CASCounter(), 8, 200_000);
        runBenchmark("SyncCounter", () -> new SyncCounter(), 8, 200_000);
        runBenchmark("AtomicInteger.incrementAndGet", AtomicInteger::new, 8, 200_000);
    }

    // Simple micro-benchmark (educational only; not a substitute for JMH)
    private static <T> void runBenchmark(String name, java.util.function.Supplier<?> supplier,
                                         int threads, int iterations) throws InterruptedException {
        System.out.printf("Benchmark: %s (%d threads x %d iters)\n", name, threads, iterations);

        Object counterObj = supplier.get();
        Thread[] ts = new Thread[threads];
        long start = System.nanoTime();

        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(() -> {
                if (counterObj instanceof CASCounter c) {
                    for (int j = 0; j < iterations; j++) c.increment();
                } else if (counterObj instanceof SyncCounter c) {
                    for (int j = 0; j < iterations; j++) c.increment();
                } else if (counterObj instanceof AtomicInteger a) {
                    for (int j = 0; j < iterations; j++) a.incrementAndGet();
                }
            }, name + "-T" + i);
            ts[i].start();
        }

        for (Thread t : ts) t.join();
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        int result = (counterObj instanceof CASCounter c) ? c.get()
                    : (counterObj instanceof SyncCounter c) ? c.get()
                    : (counterObj instanceof AtomicInteger a) ? a.get()
                    : -1;

        System.out.printf("  Result: %,d | Time: %d ms\n\n", result, elapsedMs);
    }
}
