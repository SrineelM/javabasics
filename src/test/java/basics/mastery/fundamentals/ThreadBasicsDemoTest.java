package basics.mastery.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite for ThreadBasicsDemo
 * 
 * This test suite validates the fundamental threading concepts demonstrated
 * in ThreadBasicsDemo, including thread creation, lifecycle management,
 * and coordination mechanisms.
 * 
 * @author Srineel with Copilot
 */
@DisplayName("Thread Basics Demo Tests")
class ThreadBasicsDemoTest {

    @Test
    @DisplayName("Main demo should execute without errors")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMainExecution() throws Exception {
        // Capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            // Execute the demo
            ThreadBasicsDemo.main(new String[]{});
            
            // Verify output contains expected sections
            String output = outContent.toString();
            assertThat(output).contains("Thread Basics Demo");
            assertThat(output).contains("Creating threads");
            assertThat(output).contains("join() and interrupt()");
            assertThat(output).contains("CPU-bound vs IO-bound");
            assertThat(output).contains("Done");
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Thread creation using Thread class")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testThreadCreation() throws InterruptedException {
        final boolean[] executed = {false};
        
        Thread thread = new Thread(() -> {
            executed[0] = true;
        });
        
        thread.start();
        thread.join();
        
        assertThat(executed[0]).isTrue();
    }

    @Test
    @DisplayName("Thread creation using Runnable")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testRunnableCreation() throws InterruptedException {
        final boolean[] executed = {false};
        
        Runnable task = () -> executed[0] = true;
        Thread thread = new Thread(task);
        
        thread.start();
        thread.join();
        
        assertThat(executed[0]).isTrue();
    }

    @Test
    @DisplayName("Thread interrupt mechanism")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testThreadInterrupt() throws InterruptedException {
        final boolean[] wasInterrupted = {false};
        
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(10000); // Long sleep
            } catch (InterruptedException e) {
                wasInterrupted[0] = true;
                Thread.currentThread().interrupt();
            }
        });
        
        worker.start();
        Thread.sleep(100); // Let thread start sleeping
        worker.interrupt(); // Interrupt the sleep
        worker.join();
        
        assertThat(wasInterrupted[0]).isTrue();
    }

    @Test
    @DisplayName("Thread join waits for completion")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testThreadJoin() throws InterruptedException {
        final long[] completionTime = {0};
        
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(500);
                completionTime[0] = System.currentTimeMillis();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        long startTime = System.currentTimeMillis();
        worker.start();
        worker.join(); // Wait for completion
        long joinTime = System.currentTimeMillis();
        
        assertThat(completionTime[0]).isGreaterThan(0);
        assertThat(joinTime - startTime).isGreaterThanOrEqualTo(500);
    }

    @Test
    @DisplayName("Thread name can be set and retrieved")
    void testThreadNaming() {
        Thread thread = new Thread(() -> {});
        String customName = "CustomWorkerThread";
        thread.setName(customName);
        
        assertThat(thread.getName()).isEqualTo(customName);
    }

    @Test
    @DisplayName("Daemon threads don't prevent JVM shutdown")
    void testDaemonThread() {
        Thread daemon = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // Infinite loop
            }
        });
        daemon.setDaemon(true);
        
        assertThat(daemon.isDaemon()).isTrue();
    }

    @Test
    @DisplayName("Current thread can be identified")
    void testCurrentThread() {
        Thread currentThread = Thread.currentThread();
        
        assertThat(currentThread).isNotNull();
        assertThat(currentThread.getName()).isNotNull();
    }
}
