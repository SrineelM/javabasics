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

    /**
     * The main entry point for the demonstration.
     * This method orchestrates the execution of various thread-related demonstrations.
     * @param args Command line arguments (not used).
     * @throws InterruptedException if any thread has interrupted the current thread.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Basics Demo ===\n");

        // Demonstrate different ways of creating threads.
        demoThreadVsRunnable();
        // Demonstrate how to wait for threads and interrupt them.
        demoJoinAndInterrupt();
        // Demonstrate the difference between CPU-bound and IO-bound tasks.
        demoCPUVsIO();

        System.out.println("\nDone.");
    }

    /**
     * Demonstrates two primary ways to create a thread in Java:
     * 1. Subclassing the Thread class.
     * 2. Implementing the Runnable interface and passing it to a Thread's constructor.
     * It also implicitly shows the difference between calling start() and run().
     * @throws InterruptedException if a thread is interrupted while waiting.
     */
    private static void demoThreadVsRunnable() throws InterruptedException {
        System.out.println("1) Creating threads: Thread subclass vs Runnable");

        // Method 1: Create a thread by subclassing the Thread class and overriding run().
        Thread t1 = new Thread() {
            @Override
            public void run() {
                // This code will be executed in a new thread.
                System.out.println("[" + Thread.currentThread().getName() + "] Hello from Thread subclass");
            }
        };

        // Method 2: Create a thread by providing a Runnable task.
        // A Runnable is a functional interface, so we can use a lambda expression.
        Runnable task = () -> System.out.println("[" + Thread.currentThread().getName() + "] Hello from Runnable");
        Thread t2 = new Thread(task, "Runnable-Thread");

        // Set a custom name for the first thread for better identification.
        t1.setName("Subclassed-Thread");
        
        // Start the threads. This is crucial.
        // start() allocates a new thread and makes it eligible to be run by the scheduler.
        // The JVM then calls the run() method in that new thread.
        // Calling run() directly would just execute the code in the *current* thread.
        t1.start();
        t2.start();

        // The join() method makes the current thread (main) wait until the specified thread terminates.
        // This is important to ensure we see the output from the threads before the main method exits.
        t1.join();
        t2.join();
        System.out.println();
    }

    /**
     * Demonstrates how to gracefully interrupt a running thread and how to wait for a thread to finish.
     * It showcases cooperative interruption where the running thread periodically checks if it has been interrupted.
     * @throws InterruptedException if the main thread is interrupted while sleeping or joining.
     */
    private static void demoJoinAndInterrupt() throws InterruptedException {
        System.out.println("2) join() and interrupt()");

        // Create a "worker" thread that simulates doing work in a loop.
        Thread worker = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    // A thread should cooperatively check its interrupted status.
                    // This allows the thread to shut down gracefully.
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("[Worker] Interrupted, cleaning up and exiting...");
                        return; // Exit the run() method, terminating the thread.
                    }
                    System.out.println("[Worker] Working step " + i);
                    // Thread.sleep() is a blocking operation that can be interrupted.
                    Thread.sleep(200);
                }
                System.out.println("[Worker] Completed work");
            } catch (InterruptedException e) {
                // When sleep() is interrupted, it throws InterruptedException and clears the interrupted status.
                // It's good practice to restore the interrupted status so that other code can be aware of the interruption.
                Thread.currentThread().interrupt();
                System.out.println("[Worker] Interrupted during sleep; exiting");
            }
        }, "Worker");

        // Start the worker thread.
        worker.start();
        // Let the worker run for a short period.
        Thread.sleep(450); 
        
        System.out.println("[Main] Interrupting worker and waiting with join()...");
        // The interrupt() method sets the interrupted flag on the target thread.
        // It does not forcefully stop the thread; the thread must check the flag itself.
        worker.interrupt();
        // The join() method waits for the worker thread to die.
        worker.join();
        System.out.println();
    }

    /**
     * Demonstrates the conceptual difference between CPU-bound and I/O-bound tasks.
     * - CPU-bound tasks are limited by the CPU speed. They perform intensive calculations.
     * - I/O-bound tasks are limited by the speed of an I/O device (e.g., network, disk). They spend most of their time waiting.
     * @throws InterruptedException if a thread is interrupted while waiting.
     */
    private static void demoCPUVsIO() throws InterruptedException {
        System.out.println("3) CPU-bound vs IO-bound tasks (conceptual)");

        // A CPU-bound task: performs a heavy computation (summing numbers in a tight loop).
        // This task will keep one CPU core very busy.
        Runnable cpuTask = () -> {
            long sum = 0;
            for (int i = 0; i < 5_000_000; i++) {
                sum += i;
            }
            System.out.println("[CPU Task] Done with sum=" + sum);
        };

        // An I/O-bound task: simulates waiting for an external resource like a network or disk.
        // Thread.sleep() is used here to simulate this wait. While sleeping, the thread is not consuming CPU cycles.
        Runnable ioTask = () -> {
            try {
                Thread.sleep(300); // Simulate a 300ms I/O operation.
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
        
        // Wait for both threads to complete.
        cpu.join();
        io.join();
        long elapsed = System.currentTimeMillis() - start;
        
        // The total elapsed time will be roughly the time of the longer task, not the sum of both.
        // This is because while the I/O task is "waiting" (sleeping), the CPU-bound task can run.
        System.out.println("[Main] Both tasks finished in ~" + elapsed + " ms");
    }
}
