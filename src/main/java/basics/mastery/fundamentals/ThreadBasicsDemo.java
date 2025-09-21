package basics.mastery.fundamentals;

/**
 * ThreadBasicsDemo
 *
 * Beginner-friendly walkthrough of Java thread fundamentals:
 * - Creating threads (Thread, Runnable, ExecutorService)
 * - Thread lifecycle: NEW -> RUNNABLE -> TIMED_WAITING -> TERMINATED
 * - start() vs run()
 * - join(), interrupt(), and sleep()
 * - CPU-bound vs IO-bound tasks
 *
 * This file is additive and does not modify existing project logic.
 *
 * @author Srineel with Copilot
 */
// ...existing code...

/**
 * ThreadBasicsDemo
 *
 * Beginner-friendly walkthrough of Java thread fundamentals:
 * - Creating threads (Thread, Runnable, ExecutorService)
 * - Thread lifecycle: NEW -> RUNNABLE -> TIMED_WAITING -> TERMINATED
 * - start() vs run()
 * - join(), interrupt(), and sleep()
 * - CPU-bound vs IO-bound tasks
 *
 * This file is additive and does not modify existing project logic.
 */
public class ThreadBasicsDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Basics Demo ===\n");

        demoThreadVsRunnable();
        demoJoinAndInterrupt();
        demoCPUVsIO();

        System.out.println("\nDone.");
    }

    // Demonstrates creating a thread via subclassing vs providing a Runnable
    private static void demoThreadVsRunnable() throws InterruptedException {
        System.out.println("1) Creating threads: Thread subclass vs Runnable");

        // Thread subclass
        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println("[" + Thread.currentThread().getName() + "] Hello from Thread subclass");
            }
        };

        // Runnable
        Runnable task = () -> System.out.println("[" + Thread.currentThread().getName() + "] Hello from Runnable");
        Thread t2 = new Thread(task, "Runnable-Thread");

        // NOTE for students: start() schedules a new thread; run() just calls the method on current thread
        t1.setName("Subclassed-Thread");
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println();
    }

    // Demonstrates join() waiting and cooperative interruption
    private static void demoJoinAndInterrupt() throws InterruptedException {
        System.out.println("2) join() and interrupt()");

        Thread worker = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    // Check interrupt status to exit early
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("[Worker] Interrupted, cleaning up and exiting...");
                        return;
                    }
                    System.out.println("[Worker] Working step " + i);
                    Thread.sleep(200);
                }
                System.out.println("[Worker] Completed work");
            } catch (InterruptedException e) {
                // Restore interrupt status and exit gracefully
                Thread.currentThread().interrupt();
                System.out.println("[Worker] Interrupted during sleep; exiting");
            }
        }, "Worker");

        worker.start();
        Thread.sleep(450); // Let it do a couple of steps
        System.out.println("[Main] Interrupting worker and waiting with join()...");
        worker.interrupt();
        worker.join();
        System.out.println();
    }

    // Highlights different characteristics of CPU-bound vs IO-bound tasks
    private static void demoCPUVsIO() throws InterruptedException {
        System.out.println("3) CPU-bound vs IO-bound tasks (conceptual)");

        // CPU-bound: tight loop, heavy computation
        Runnable cpuTask = () -> {
            long sum = 0;
            for (int i = 0; i < 5_000_000; i++) {
                sum += i;
            }
            System.out.println("[CPU Task] Done with sum=" + sum);
        };

        // IO-bound: simulates waiting on external IO (sleep)
        Runnable ioTask = () -> {
            try {
                Thread.sleep(300);
                System.out.println("[IO Task] Done (simulated IO)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread cpu = new Thread(cpuTask, "CPU-Task");
        Thread io = new Thread(ioTask, "IO-Task");
        long start = System.currentTimeMillis();
        cpu.start();
        io.start();
        cpu.join();
        io.join();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("[Main] Both tasks finished in ~" + elapsed + " ms");
    }
}
