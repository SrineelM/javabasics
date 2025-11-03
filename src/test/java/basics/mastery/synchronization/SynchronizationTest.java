package basics.mastery.synchronization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.*;

/**
 * Comprehensive test suite for Synchronization mechanisms
 * 
 * Tests cover various synchronization primitives including locks,
 * coordination tools, and thread-safe patterns.
 * 
 * @author Srineel with Copilot
 */
@DisplayName("Synchronization Tests")
class SynchronizationTest {

    @RepeatedTest(5) // Run multiple times to catch race conditions
    @DisplayName("Synchronized method prevents race conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSynchronizedCounter() throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        int threadCount = 10;
        int incrementsPerThread = 1000;
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        counter.increment();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        assertThat(counter.getCount()).isEqualTo(threadCount * incrementsPerThread);
    }

    @Test
    @DisplayName("ReentrantLock provides same thread reentrancy")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testReentrantLock() {
        ReentrantLock lock = new ReentrantLock();
        
        lock.lock();
        try {
            assertThat(lock.isHeldByCurrentThread()).isTrue();
            assertThat(lock.getHoldCount()).isEqualTo(1);
            
            // Reentrant call
            lock.lock();
            try {
                assertThat(lock.getHoldCount()).isEqualTo(2);
            } finally {
                lock.unlock();
            }
            
            assertThat(lock.getHoldCount()).isEqualTo(1);
        } finally {
            lock.unlock();
        }
        
        assertThat(lock.isLocked()).isFalse();
    }

    @Test
    @DisplayName("ReadWriteLock allows multiple readers")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testReadWriteLock() throws InterruptedException {
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        AtomicInteger concurrentReaders = new AtomicInteger(0);
        AtomicInteger maxConcurrentReaders = new AtomicInteger(0);
        
        int readerCount = 5;
        CountDownLatch latch = new CountDownLatch(readerCount);
        
        for (int i = 0; i < readerCount; i++) {
            new Thread(() -> {
                rwLock.readLock().lock();
                try {
                    int current = concurrentReaders.incrementAndGet();
                    maxConcurrentReaders.updateAndGet(max -> Math.max(max, current));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    concurrentReaders.decrementAndGet();
                    rwLock.readLock().unlock();
                    latch.countDown();
                }
            }).start();
        }
        
        latch.await();
        
        // Multiple readers should have been concurrent
        assertThat(maxConcurrentReaders.get()).isGreaterThan(1);
    }

    @Test
    @DisplayName("CountDownLatch coordinates thread startup")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCountDownLatch() throws InterruptedException {
        int threadCount = 5;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(threadCount);
        AtomicInteger started = new AtomicInteger(0);
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startSignal.await(); // Wait for start signal
                    started.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneSignal.countDown();
                }
            }).start();
        }
        
        Thread.sleep(100); // Give threads time to start waiting
        assertThat(started.get()).isEqualTo(0); // All should be waiting
        
        startSignal.countDown(); // Release all threads
        doneSignal.await(); // Wait for all to complete
        
        assertThat(started.get()).isEqualTo(threadCount);
    }

    @Test
    @DisplayName("CyclicBarrier coordinates phase completion")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCyclicBarrier() throws InterruptedException {
        int parties = 3;
        AtomicInteger phase1Complete = new AtomicInteger(0);
        AtomicInteger phase2Complete = new AtomicInteger(0);
        CyclicBarrier barrier = new CyclicBarrier(parties);
        CountDownLatch latch = new CountDownLatch(parties);
        
        for (int i = 0; i < parties; i++) {
            new Thread(() -> {
                try {
                    // Phase 1
                    Thread.sleep((long) (Math.random() * 100));
                    phase1Complete.incrementAndGet();
                    barrier.await(); // Wait for all to complete phase 1
                    
                    // Phase 2
                    Thread.sleep((long) (Math.random() * 100));
                    phase2Complete.incrementAndGet();
                    barrier.await(); // Wait for all to complete phase 2
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        
        latch.await();
        
        assertThat(phase1Complete.get()).isEqualTo(parties);
        assertThat(phase2Complete.get()).isEqualTo(parties);
    }

    @Test
    @DisplayName("Semaphore limits concurrent access")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testSemaphore() throws InterruptedException {
        int permits = 2;
        Semaphore semaphore = new Semaphore(permits);
        AtomicInteger concurrentAccess = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);
        
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    int current = concurrentAccess.incrementAndGet();
                    maxConcurrent.updateAndGet(max -> Math.max(max, current));
                    Thread.sleep(50);
                    concurrentAccess.decrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                    latch.countDown();
                }
            }).start();
        }
        
        latch.await();
        
        // Max concurrent access should not exceed permits
        assertThat(maxConcurrent.get()).isLessThanOrEqualTo(permits);
    }

    @Test
    @DisplayName("Phaser coordinates dynamic participants")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testPhaser() throws InterruptedException {
        Phaser phaser = new Phaser(1); // Register main thread
        AtomicInteger completedPhase0 = new AtomicInteger(0);
        
        int threadCount = 3;
        for (int i = 0; i < threadCount; i++) {
            phaser.register(); // Dynamically register participant
            new Thread(() -> {
                phaser.arriveAndAwaitAdvance(); // Phase 0
                completedPhase0.incrementAndGet();
                phaser.arriveAndDeregister(); // Complete and leave
            }).start();
        }
        
        phaser.arriveAndAwaitAdvance(); // Main thread arrives
        
        await().atMost(3, TimeUnit.SECONDS)
               .until(() -> completedPhase0.get() == threadCount);
        
        assertThat(phaser.getPhase()).isGreaterThan(0);
    }

    @RepeatedTest(5)
    @DisplayName("AtomicInteger provides lock-free operations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAtomicInteger() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 10;
        int incrementsPerThread = 1000;
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        counter.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        assertThat(counter.get()).isEqualTo(threadCount * incrementsPerThread);
    }

    // Helper class for synchronized counter test
    static class SynchronizedCounter {
        private int count = 0;
        
        public synchronized void increment() {
            count++;
        }
        
        public synchronized int getCount() {
            return count;
        }
    }
}
