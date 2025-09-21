package basics.mastery.advanced;.modern;

/**
 * VirtualThreadPinningDemo
 *
 * Demonstrates scenarios where virtual threads can be pinned to their carrier threads,
 * reducing scalability.
 *
 * Scenarios:
 * 1) synchronized block holds monitor during blocking -> pinning
 * 2) native blocking operations may pin (simulated via sleep inside synchronized)
 * 3) proper design: avoid holding monitors across blocking operations
 *
 * Note: Requires Java 21+ to use virtual thread executors effectively.
 * Additive educational demo; does not modify existing logic.
 */
public class VirtualThreadPinningDemo {
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws Exception {
        System.out.println("=== Virtual Thread Pinning Demo ===\n");
        demoPinnedSynchronizedBlock();
        demoNonPinnedDesign();
    }

    // Example: Holding a monitor while blocking (sleep) can pin the virtual thread
    static void demoPinnedSynchronizedBlock() throws InterruptedException {
        System.out.println("1) Pinned scenario: synchronized + blocking call");
        Thread vt = Thread.startVirtualThread(() -> {
            synchronized (LOCK) {
                log("Inside synchronized; going to sleep (this may pin the carrier)");
                try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                log("Woke up; exiting synchronized");
            }
        });

        vt.join();
        System.out.println();
    }

    // Example: Release monitor before blocking to avoid pinning
    static void demoNonPinnedDesign() throws InterruptedException {
        System.out.println("2) Non-pinned design: release lock before blocking");
        Thread vt = Thread.startVirtualThread(() -> {
            // Do minimal work under synchronized
            synchronized (LOCK) {
                log("Minimal critical section; not blocking while holding the lock");
            }
            // Block after leaving the monitor
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            log("Slept outside synchronized; carrier was not pinned by this lock");
        });

        vt.join();
    }

    private static void log(String msg) {
        System.out.printf("[%s] %s%n", Thread.currentThread(), msg);
    }
}
