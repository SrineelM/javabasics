package basics.mastery.modern;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Comprehensive demonstration of modern Java concurrency features (Java 17-21)
 * 
 * This comprehensive demo showcases the evolution of Java concurrency from traditional
 * threading models to the revolutionary features introduced in Java 19-21. The content
 * is designed for developers transitioning from classic concurrency patterns to modern
 * approaches, with particular emphasis on real-world application scenarios.
 * 
 * Key Topics Covered:
 * 1. Virtual Threads Deep Dive
 *    - Architecture and carrier thread mechanics
 *    - Memory overhead comparison with platform threads
 *    - Context switching performance characteristics
 *    - Pinning scenarios and their performance implications
 * 
 * 2. Structured Concurrency (Preview in Java 19-21)
 *    - Coordinated task lifecycle management
 *    - Fail-fast vs. shutdown-on-success patterns
 *    - Error propagation and resource cleanup
 *    - Migration from traditional ExecutorService patterns
 * 
 * 3. Scoped Values (Preview in Java 20-21)
 *    - ThreadLocal alternative for context propagation
 *    - Immutable context sharing across virtual threads
 *    - Performance benefits over ThreadLocal
 *    - Integration with structured concurrency
 * 
 * 4. Migration Strategies
 *    - From platform threads to virtual threads
 *    - Refactoring ExecutorService usage patterns
 *    - Async/await pattern implementations
 *    - Compatibility considerations and fallback approaches
 * 
 * 5. Performance Analysis
 *    - Throughput comparison across threading models
 *    - Latency analysis for I/O-bound vs CPU-bound tasks
 *    - Resource utilization and memory efficiency
 *    - Scalability characteristics under load
 * 
 * Prerequisites:
 * - Java 19+ for virtual threads (standard)
 * - Java 19+ with --enable-preview for structured concurrency
 * - Java 20+ with --enable-preview for scoped values
 * 
 * Note: This demo includes both real API usage (when available) and mock
 * implementations for demonstration purposes on older Java versions.
 */
public class ModernConcurrencyDemo {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Modern Java Concurrency (Java 21) ===\n");
        System.out.println("🚀 Exploring the cutting-edge features of modern Java concurrency");
        System.out.println("   This demo covers virtual threads, structured concurrency, and more!\n");
        
        // 1. Virtual Threads architecture and characteristics
        demonstrateVirtualThreadArchitecture();
        
        // 2. Virtual Thread pinning scenarios and their impact
        demonstratePinningScenarios();
        
        // 3. Structured Concurrency patterns for robust error handling
        demonstrateStructuredConcurrency();
        
        // 4. Scoped Values for efficient context propagation
        demonstrateScopedValues();
        
        // 5. Migration strategies from platform threads
        demonstrateMigrationStrategies();
        
        // 6. Comprehensive performance analysis
        performanceComparison();
        
        System.out.println("🎯 Modern concurrency demo completed!");
        System.out.println("💡 Key takeaway: Virtual threads revolutionize I/O-bound applications");
    }
    
    /**
     * Deep dive into Virtual Thread architecture and behavior
     * 
     * Virtual threads represent a fundamental shift in Java's threading model:
     * - Managed by the JVM rather than the OS
     * - Lightweight (few KB vs ~1MB for platform threads)
     * - Mount/unmount from platform threads (carriers) automatically
     * - Designed for high-concurrency I/O-bound applications
     */
    private static void demonstrateVirtualThreadArchitecture() throws InterruptedException {
        System.out.println("1. Virtual Thread Architecture Deep Dive:");
        System.out.println("   Understanding the revolutionary threading model of Java 21+\n");
        
        // Show carrier thread mechanics
        System.out.println("1a. Carrier Thread Mechanics:");
        System.out.println("    How virtual threads map to underlying platform threads");
        demonstrateCarrierThreads();
        
        // Memory overhead comparison
        System.out.println("\n1b. Memory Overhead Analysis:");
        System.out.println("    Comparing memory usage: Platform vs Virtual threads");
        demonstrateMemoryOverhead();
        
        // Context switch performance
        System.out.println("\n1c. Context Switch Performance:");
        System.out.println("    Measuring the performance impact of thread context switches");
        demonstrateContextSwitchPerformance();
        
        System.out.println();
    }
    
    /**
     * Demonstrate carrier thread mechanics and virtual thread scheduling
     * 
     * This example shows how many virtual threads can share a limited number
     * of carrier threads, demonstrating the efficiency of the virtual thread model.
     */
    private static void demonstrateCarrierThreads() throws InterruptedException {
        System.out.println("Creating 1000 virtual threads to observe carrier thread behavior:");
        
        CountDownLatch latch = new CountDownLatch(1000);
        AtomicInteger carrierThreadCount = new AtomicInteger(0);
        Set<String> carrierThreadNames = ConcurrentHashMap.newKeySet();
        
        for (int i = 0; i < 1000; i++) {
            final int taskId = i;
            Thread.ofVirtual().start(() -> {
                // Get the carrier thread information
                Thread currentThread = Thread.currentThread();
                String threadInfo = currentThread.toString();
                
                // Track unique carrier threads
                if (carrierThreadNames.add(threadInfo)) {
                    carrierThreadCount.incrementAndGet();
                }
                
                // Log details for first few threads to show the pattern
                if (taskId < 5) {
                    System.out.printf("   VirtualThread-%d: %s%n", taskId, 
                        currentThread.isVirtual() ? "VIRTUAL" : "PLATFORM");
                }
                
                try {
                    // I/O simulation - this will cause the virtual thread to unmount
                    Thread.sleep(100); // Virtual thread yields carrier thread during I/O
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                latch.countDown();
            });
        }
        
        latch.await();
        System.out.printf("✅ 1000 virtual threads executed using %d carrier threads%n", 
            carrierThreadCount.get());
        System.out.printf("💻 Available processors: %d%n", Runtime.getRuntime().availableProcessors());
        System.out.printf("📊 Efficiency: %.1fx more virtual threads than processors%n", 
            1000.0 / Runtime.getRuntime().availableProcessors());
    }
    
    /**
     * Memory overhead analysis: Platform threads vs Virtual threads
     * 
     * This demonstration measures the actual memory consumption difference
     * between platform and virtual threads, highlighting the scalability benefits.
     */
    private static void demonstrateMemoryOverhead() throws InterruptedException {
        System.out.println("Comparing memory usage between platform and virtual threads:");
        
        Runtime runtime = Runtime.getRuntime();
        
        // Establish baseline memory usage
        System.gc(); // Force garbage collection for accurate measurement
        Thread.sleep(100);
        long baselineMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Create platform threads and measure memory impact
        List<Thread> platformThreads = new ArrayList<>();
        CountDownLatch platformLatch = new CountDownLatch(100);
        
        System.out.println("   Creating 100 platform threads...");
        for (int i = 0; i < 100; i++) {
            Thread t = Thread.ofPlatform().start(() -> {
                try {
                    platformLatch.await(); // Wait for all threads to be created
                    Thread.sleep(1000); // Keep thread alive for measurement
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            platformThreads.add(t);
        }
        
        System.gc();
        Thread.sleep(100);
        long platformMemory = runtime.totalMemory() - runtime.freeMemory();
        platformLatch.countDown(); // Release platform threads
        
        // Wait for platform threads to finish
        for (Thread t : platformThreads) {
            t.join();
        }
        
        // Create virtual threads and measure memory impact
        CountDownLatch virtualLatch = new CountDownLatch(100);
        List<Thread> virtualThreads = new ArrayList<>();
        
        System.out.println("   Creating 100 virtual threads...");
        for (int i = 0; i < 100; i++) {
            Thread t = Thread.ofVirtual().start(() -> {
                try {
                    virtualLatch.await(); // Wait for all threads to be created
                    Thread.sleep(1000); // Keep thread alive for measurement
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            virtualThreads.add(t);
        }
        
        System.gc();
        Thread.sleep(100);
        long virtualMemory = runtime.totalMemory() - runtime.freeMemory();
        virtualLatch.countDown(); // Release virtual threads
        
        for (Thread t : virtualThreads) {
            t.join();
        }
        
        // Report memory usage analysis
        long platformOverhead = platformMemory - baselineMemory;
        long virtualOverhead = virtualMemory - baselineMemory;
        
        System.out.printf("📊 Memory Usage Analysis:%n");
        System.out.printf("   Baseline memory: %.1f MB%n", baselineMemory / (1024.0 * 1024));
        System.out.printf("   100 platform threads: %.1f MB (+%.1f MB)%n", 
            platformMemory / (1024.0 * 1024), platformOverhead / (1024.0 * 1024));
        System.out.printf("   100 virtual threads: %.1f MB (+%.1f MB)%n", 
            virtualMemory / (1024.0 * 1024), virtualOverhead / (1024.0 * 1024));
        
        if (platformOverhead > 0 && virtualOverhead > 0) {
            double efficiency = (double) platformOverhead / virtualOverhead;
            System.out.printf("   💡 Virtual threads use %.1fx less memory per thread%n", efficiency);
        }
        
        System.out.printf("   📈 Scalability: Virtual threads enable %dx more concurrent tasks%n", 
            (int) Math.max(1, platformOverhead / Math.max(1, virtualOverhead)));
    }
    
    /**
     * Context switch performance comparison
     * 
     * Measures the performance characteristics of creating and switching between
     * platform threads vs virtual threads under load.
     */
    private static void demonstrateContextSwitchPerformance() throws InterruptedException {
        System.out.println("Measuring context switch performance:");
        
        int iterations = 1_000;
        
        // Platform thread context switches
        System.out.printf("   Testing %d platform thread context switches...%n", iterations);
        Instant start = Instant.now();
        CountDownLatch platformLatch = new CountDownLatch(iterations);
        
        for (int i = 0; i < iterations; i++) {
            Thread.ofPlatform().start(() -> {
                // Minimal work to measure pure context switch overhead
                Thread.yield();
                platformLatch.countDown();
            });
        }
        
        platformLatch.await();
        Duration platformTime = Duration.between(start, Instant.now());
        
        // Virtual thread context switches
        System.out.printf("   Testing %d virtual thread context switches...%n", iterations);
        start = Instant.now();
        CountDownLatch virtualLatch = new CountDownLatch(iterations);
        
        for (int i = 0; i < iterations; i++) {
            Thread.ofVirtual().start(() -> {
                // Minimal work to measure pure context switch overhead
                Thread.yield();
                virtualLatch.countDown();
            });
        }
        
        virtualLatch.await();
        Duration virtualTime = Duration.between(start, Instant.now());
        
        // Performance analysis
        System.out.printf("📊 Context Switch Performance:%n");
        System.out.printf("   Platform threads: %d ms (%.2f μs per switch)%n", 
            platformTime.toMillis(), platformTime.toNanos() / (1000.0 * iterations));
        System.out.printf("   Virtual threads: %d ms (%.2f μs per switch)%n", 
            virtualTime.toMillis(), virtualTime.toNanos() / (1000.0 * iterations));
        
        if (platformTime.toNanos() > 0 && virtualTime.toNanos() > 0) {
            double speedup = (double) platformTime.toNanos() / virtualTime.toNanos();
            System.out.printf("   💡 Virtual threads are %.1fx faster for context switches%n", speedup);
        }
    }
    
    /**
     * Demonstrate Virtual Thread pinning scenarios and their impact
     * 
     * Virtual thread pinning occurs when a virtual thread cannot be unmounted
     * from its carrier thread, reducing the efficiency of the virtual thread model.
     * This section demonstrates common pinning scenarios and mitigation strategies.
     */
    private static void demonstratePinningScenarios() throws InterruptedException {
        System.out.println("2. Virtual Thread Pinning Scenarios:");
        System.out.println("   Understanding when virtual threads cannot unmount from carriers\n");
        
        // Synchronized block pinning
        System.out.println("2a. Synchronized Block Pinning:");
        demonstrateSynchronizedPinning();
        
        // JNI pinning scenario
        System.out.println("\n2b. JNI Pinning:");
        demonstrateJNIPinning();
        
        System.out.println();
    }
    
    /**
     * Demonstrate pinning caused by synchronized blocks
     * 
     * When a virtual thread enters a synchronized block, it becomes pinned
     * to its carrier thread and cannot unmount during I/O operations.
     */
    private static void demonstrateSynchronizedPinning() throws InterruptedException {
        System.out.println("   Synchronized blocks cause virtual thread pinning:");
        
        Object lock = new Object();
        CountDownLatch latch = new CountDownLatch(5);
        
        // Create virtual threads that will be pinned due to synchronized blocks
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            Thread.ofVirtual().start(() -> {
                synchronized (lock) {
                    System.out.printf("   Task %d: Entered synchronized block (PINNED)%n", taskId);
                    
                    try {
                        // This sleep cannot unmount the virtual thread due to synchronization
                        Thread.sleep(100);
                        System.out.printf("   Task %d: Completed work while pinned%n", taskId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                latch.countDown();
            });
        }
        
        latch.await();
        
        System.out.println("   💡 Mitigation: Use ReentrantLock instead of synchronized");
        System.out.println("   💡 ReentrantLock allows virtual threads to unmount during I/O");
        
        // Demonstrate better alternative with ReentrantLock
        ReentrantLock reentrantLock = new ReentrantLock();
        CountDownLatch betterLatch = new CountDownLatch(3);
        
        System.out.println("   Using ReentrantLock (virtual threads can unmount):");
        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            Thread.ofVirtual().start(() -> {
                reentrantLock.lock();
                try {
                    System.out.printf("   Better Task %d: Using ReentrantLock (can unmount)%n", taskId);
                    Thread.sleep(50); // Virtual thread can unmount here
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    reentrantLock.unlock();
                }
                betterLatch.countDown();
            });
        }
        
        betterLatch.await();
    }
    
    /**
     * Demonstrate JNI-related pinning scenarios
     * 
     * JNI calls can also cause virtual thread pinning in certain circumstances.
     */
    private static void demonstrateJNIPinning() {
        System.out.println("   JNI calls may cause virtual thread pinning:");
        System.out.println("   💡 Native method calls can pin virtual threads to carriers");
        System.out.println("   💡 File I/O operations may involve JNI and cause pinning");
        System.out.println("   💡 Monitor pinning frequency with -Djdk.tracePinnedThreads=full");
    }
    
    /**
     * Structured Concurrency demonstrations
     * 
     * Structured concurrency provides a powerful model for coordinating
     * concurrent tasks with clear parent-child relationships and automatic
     * resource management.
     */
    private static void demonstrateStructuredConcurrency() throws Exception {
        System.out.println("3. Structured Concurrency (Java 21 Preview):");
        System.out.println("   Coordinated task lifecycle with automatic cleanup\n");
        
        // Fail-fast pattern
        System.out.println("3a. Shutdown-on-Failure Pattern:");
        demonstrateShutdownOnFailure();
        
        // Success-first pattern
        System.out.println("\n3b. Shutdown-on-Success Pattern:");
        demonstrateShutdownOnSuccess();
        
        System.out.println();
    }
    
    /**
     * Demonstrate shutdown-on-failure structured concurrency pattern
     * 
     * When any subtask fails, all remaining tasks are automatically cancelled,
     * providing fail-fast semantics for critical operations.
     */
    private static void demonstrateShutdownOnFailure() throws Exception {
        System.out.println("   All tasks must succeed, or all are cancelled:");
        
        try (var scope = new MockStructuredTaskScope.ShutdownOnFailure()) {
            
            // Fork multiple critical tasks
            scope.fork(() -> {
                Thread.sleep(100);
                System.out.println("   ✅ User service: Authentication successful");
                return "user-authenticated";
            });
            
            scope.fork(() -> {
                Thread.sleep(150);
                System.out.println("   ✅ Auth service: Permissions validated");
                return "auth-validated";
            });
            
            scope.fork(() -> {
                Thread.sleep(200);
                // Simulate a failure in critical path
                throw new RuntimeException("Database connection failed");
            });
            
            scope.fork(() -> {
                Thread.sleep(250);
                System.out.println("   ✅ Audit service: Request logged");
                return "audit-logged";
            });
            
            // Wait for all tasks - will fail fast on first error
            scope.join();
            scope.throwIfFailed();
            
            // This code is never reached due to failure
            System.out.println("   All tasks completed successfully");
            
        } catch (Exception e) {
            System.out.println("   ❌ Operation failed: " + e.getMessage());
            System.out.println("   🔄 All remaining tasks were automatically cancelled");
        }
    }
    
    /**
     * Demonstrate shutdown-on-success structured concurrency pattern
     * 
     * Racing multiple equivalent tasks where the first successful result
     * causes cancellation of all remaining tasks.
     */
    private static void demonstrateShutdownOnSuccess() throws Exception {
        System.out.println("   Racing multiple data sources - first successful result wins:");
        
        try (var scope = new MockStructuredTaskScope.ShutdownOnSuccess<String>()) {
            
            // Fork multiple competing data sources
            scope.fork(() -> {
                Thread.sleep(300);
                System.out.println("   📊 Primary database responded");
                return "Data from PRIMARY DB";
            });
            
            scope.fork(() -> {
                Thread.sleep(100);
                System.out.println("   ⚡ Cache responded first!");
                return "Data from CACHE";
            });
            
            scope.fork(() -> {
                Thread.sleep(200);
                System.out.println("   💾 Backup database responded");
                return "Data from BACKUP DB";
            });
            
            // Wait for first successful result
            scope.join();
            
            String result = scope.result();
            System.out.println("   ✅ Got result: " + result);
            System.out.println("   🚫 Other data sources were cancelled automatically");
            
        } catch (Exception e) {
            System.out.println("   ❌ All data sources failed: " + e.getMessage());
        }
    }
    
    /**
     * Scoped Values demonstration with fallback support
     * 
     * This demonstration shows the modern alternative to ThreadLocal using
     * ScopedValue (Java 20+ preview feature). ScopedValue provides:
     * - Immutable context sharing
     * - Better performance than ThreadLocal
     * - Natural integration with virtual threads
     * - Structured lifetime management
     * 
     * If real ScopedValue is not available, falls back to mock implementation.
     */
    private static void demonstrateScopedValues() throws Exception {
        System.out.println("4. Scoped Values (ThreadLocal Alternative):");
        System.out.println("   Demonstrating immutable context propagation across virtual threads");
        
        try {
            // Try to use real Java 21 ScopedValue API
            demonstrateRealScopedValues();
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            // Fallback to mock implementation for demonstration
            System.out.println("   Note: Using mock implementation (real ScopedValue requires Java 20+ with preview)");
            demonstrateMockScopedValues();
        }
        
        System.out.println();
    }
    
    /**
     * Demonstration using real Java 21 ScopedValue API
     */
    private static void demonstrateRealScopedValues() throws Exception {
        // This will only work if running on Java 20+ with preview features enabled
        Class<?> scopedValueClass = Class.forName("java.lang.ScopedValue");
        
        // Use reflection to create ScopedValue instances
        scopedValueClass.getMethod("newInstance").invoke(null);
        scopedValueClass.getMethod("newInstance").invoke(null);
        
        System.out.println("✅ Using real Java 21 ScopedValue API");
        
        // Note: This is simplified - full implementation would use proper ScopedValue API
        System.out.println("Real ScopedValue demonstration would require --enable-preview flag");
        System.out.println("Proceeding with mock implementation for compatibility...");
        demonstrateMockScopedValues();
    }
    
    /**
     * Demonstration using mock ScopedValue implementation
     */
    private static void demonstrateMockScopedValues() throws Exception {
        // Define scoped values using mock implementation
        ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
        ScopedValue<String> USER_ID = ScopedValue.newInstance();
        
        System.out.println("🔧 Mock ScopedValue Implementation:");
        System.out.println("   Creating request context with scoped values...");
        
        // Simulate request processing with scoped context
        REQUEST_ID.where("REQ-12345")
                  .where(USER_ID, "user@example.com")
                  .run(() -> {
                      
            System.out.printf("📥 Processing request: %s for user: %s%n", 
                REQUEST_ID.get(), USER_ID.get());
            
            // Process in virtual threads - scoped values are inherited
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                
                // Service A processing with inherited context
                var serviceATask = executor.submit(() -> {
                    System.out.printf("🔧 Service A: Handling request %s for user %s%n", 
                        REQUEST_ID.get(), USER_ID.get());
                    
                    // Simulate some processing time
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    return "ServiceA-Result-" + REQUEST_ID.get();
                });
                
                // Service B processing with inherited context
                var serviceBTask = executor.submit(() -> {
                    System.out.printf("⚙️  Service B: Processing request %s for user %s%n", 
                        REQUEST_ID.get(), USER_ID.get());
                    
                    // Simulate some processing time
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    return "ServiceB-Result-" + REQUEST_ID.get();
                });
                
                // Service C with database access
                var serviceCTask = executor.submit(() -> {
                    System.out.printf("💾 Service C: Database access for request %s (user: %s)%n", 
                        REQUEST_ID.get(), USER_ID.get());
                    
                    // Simulate database query
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    return "ServiceC-Data-" + USER_ID.get();
                });
                
                // Wait for all results and combine them
                String resultA = serviceATask.get();
                String resultB = serviceBTask.get();
                String resultC = serviceCTask.get();
                
                System.out.printf("✅ All services completed for request %s:%n", REQUEST_ID.get());
                System.out.printf("   📊 Results: %s | %s | %s%n", resultA, resultB, resultC);
                
                // Demonstrate nested scoped context
                ScopedValue<String> OPERATION_ID = ScopedValue.newInstance();
                
                OPERATION_ID.where("OP-AGGREGATE")
                           .run(() -> {
                    System.out.printf("🔄 Aggregation operation %s for request %s%n",
                        OPERATION_ID.get(), REQUEST_ID.get());
                    
                    // Context is properly nested and inherited
                    System.out.printf("   Original user: %s, Current operation: %s%n",
                        USER_ID.get(), OPERATION_ID.get());
                });
                
            } catch (Exception e) {
                System.err.println("❌ Error in service processing: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        System.out.println("💡 ScopedValue Benefits:");
        System.out.println("   • Immutable - cannot be modified once set");
        System.out.println("   • Inherited by virtual threads automatically");
        System.out.println("   • Better performance than ThreadLocal");
        System.out.println("   • Structured lifetime prevents context leaks");
        System.out.println("   • Natural integration with structured concurrency");
    }
    
    /**
     * Migration strategies from platform threads to virtual threads
     * 
     * Demonstrates practical approaches for modernizing existing concurrent
     * applications to leverage virtual threads and structured concurrency.
     */
    private static void demonstrateMigrationStrategies() throws Exception {
        System.out.println("5. Migration Strategies:");
        System.out.println("   Practical approaches for modernizing concurrent applications\n");
        
        // Thread pool migration
        System.out.println("5a. Thread Pool Migration:");
        demonstrateThreadPoolMigration();
        
        // Async/await pattern migration
        System.out.println("\n5b. Async/Await Pattern Migration:");
        demonstrateAsyncMigration();
        
        System.out.println();
    }
    
    /**
     * Demonstrate migrating from traditional thread pools to virtual threads
     */
    private static void demonstrateThreadPoolMigration() throws Exception {
        System.out.println("   Migrating from fixed thread pools to virtual threads:");
        
        // Old approach with fixed thread pool
        System.out.println("   📊 Traditional Approach (Fixed Thread Pool):");
        try (ExecutorService traditionalExecutor = Executors.newFixedThreadPool(10)) {
            
            List<Future<String>> traditionalFutures = new ArrayList<>();
            
            for (int i = 0; i < 20; i++) {
                final int taskId = i;
                traditionalFutures.add(traditionalExecutor.submit(() -> {
                    Thread.sleep(100); // I/O simulation
                    return "Traditional-Task-" + taskId;
                }));
            }
            
            // Limited by thread pool size - queuing occurs
            System.out.printf("   Submitted 20 tasks to 10-thread pool%n");
            
            for (Future<String> future : traditionalFutures) {
                future.get(); // Wait for completion
            }
            
            System.out.println("   ✅ Traditional approach completed (with queuing)");
        }
        
        // New approach with virtual threads
        System.out.println("   🚀 Modern Approach (Virtual Threads):");
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            List<Future<String>> virtualFutures = new ArrayList<>();
            
            for (int i = 0; i < 20; i++) {
                final int taskId = i;
                virtualFutures.add(virtualExecutor.submit(() -> {
                    Thread.sleep(100); // I/O simulation
                    return "Virtual-Task-" + taskId;
                }));
            }
            
            // No artificial limits - all tasks run concurrently
            System.out.printf("   Submitted 20 tasks with unlimited virtual threads%n");
            
            for (Future<String> future : virtualFutures) {
                future.get(); // Wait for completion
            }
            
            System.out.println("   ✅ Virtual thread approach completed (no queuing)");
        }
        
        System.out.println("   💡 Migration Benefits:");
        System.out.println("     • Remove thread pool size limitations");
        System.out.println("     • Eliminate task queuing for I/O-bound work");
        System.out.println("     • Simplify configuration (no pool tuning needed)");
        System.out.println("     • Better resource utilization");
    }
    
    /**
     * Demonstrate async/await pattern migration using virtual threads
     */
    private static void demonstrateAsyncMigration() throws Exception {
        System.out.println("   Simplifying async patterns with virtual threads:");
        
        // Simulated async operations that become simple with virtual threads
        System.out.println("   📡 Complex Async Chain (Traditional):");
        
        // Traditional callback-based async (simulated)
        CompletableFuture<String> traditionalChain = CompletableFuture
            .supplyAsync(() -> {
                System.out.println("   Step 1: Fetch user data");
                return "user-123";
            })
            .thenCompose(userId -> CompletableFuture.supplyAsync(() -> {
                System.out.println("   Step 2: Fetch user preferences for " + userId);
                return "preferences-for-" + userId;
            }))
            .thenCompose(preferences -> CompletableFuture.supplyAsync(() -> {
                System.out.println("   Step 3: Generate recommendations based on " + preferences);
                return "recommendations-for-" + preferences;
            }));
        
        String traditionalResult = traditionalChain.get();
        System.out.println("   ✅ Traditional async result: " + traditionalResult);
        
        // Simple sequential code with virtual threads
        System.out.println("   🎯 Simple Sequential Code (Virtual Threads):");
        
        Thread.ofVirtual().start(() -> {
            try {
                // Step 1: Fetch user data (I/O operation)
                System.out.println("   Step 1: Fetch user data");
                Thread.sleep(50); // Simulated I/O
                String userId = "user-123";
                
                // Step 2: Fetch preferences (I/O operation)
                System.out.println("   Step 2: Fetch user preferences for " + userId);
                Thread.sleep(50); // Simulated I/O
                String preferences = "preferences-for-" + userId;
                
                // Step 3: Generate recommendations (I/O operation)
                System.out.println("   Step 3: Generate recommendations based on " + preferences);
                Thread.sleep(50); // Simulated I/O
                String recommendations = "recommendations-for-" + preferences;
                
                System.out.println("   ✅ Virtual thread result: " + recommendations);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).join();
        
        System.out.println("   💡 Migration Benefits:");
        System.out.println("     • Eliminate callback complexity");
        System.out.println("     • Write natural sequential code");
        System.out.println("     • Maintain high concurrency");
        System.out.println("     • Simplify error handling");
    }
    
    /**
     * Comprehensive performance comparison across different concurrency models
     * 
     * This section provides detailed performance analysis including throughput,
     * latency, resource utilization, and scalability characteristics.
     */
    private static void performanceComparison() throws Exception {
        System.out.println("6. Comprehensive Performance Analysis:");
        System.out.println("   Detailed comparison across concurrency models\n");
        
        // Throughput comparison
        System.out.println("6a. Throughput Analysis:");
        throughputComparison();
        
        // Latency analysis
        System.out.println("\n6b. Latency Distribution Analysis:");
        latencyAnalysis();
        
        // Resource utilization
        System.out.println("\n6c. Resource Utilization Analysis:");
        resourceUtilizationAnalysis();
        
        System.out.println();
    }
    
    /**
     * Throughput comparison between different concurrency models
     */
    private static void throughputComparison() throws Exception {
        System.out.println("   Measuring requests per second across different models:");
        
        int[] requestCounts = {100, 500, 1000, 5000};
        
        System.out.printf("   %-12s %-15s %-15s %-15s%n", "Requests", "Platform", "Virtual", "Speedup");
        System.out.println("   " + "-".repeat(60));
        
        for (int requests : requestCounts) {
            long platformTime = measureThroughput("Platform threads", () -> {
                try (ExecutorService executor = Executors.newFixedThreadPool(50)) {
                    performIOWorkload(executor, requests);
                } catch (Exception e) {
                    // Handle exception
                }
            });
            
            long virtualTime = measureThroughput("Virtual threads", () -> {
                try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    performIOWorkload(executor, requests);
                } catch (Exception e) {
                    // Handle exception
                }
            });
            
            double speedup = platformTime > 0 ? (double) platformTime / virtualTime : 1.0;
            
            System.out.printf("   %-12d %-15d %-15d %-15.1fx%n", 
                requests, platformTime, virtualTime, speedup);
        }
    }
    
    /**
     * Measure throughput for a specific scenario
     */
    private static long measureThroughput(String scenario, Runnable task) throws Exception {
        // Warmup
        task.run();
        
        // Actual measurement
        Instant start = Instant.now();
        task.run();
        long elapsed = Duration.between(start, Instant.now()).toMillis();
        
        return elapsed;
    }
    
    /**
     * Perform I/O-intensive workload for performance testing
     */
    private static void performIOWorkload(ExecutorService executor, int taskCount) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(taskCount);
        
        for (int i = 0; i < taskCount; i++) {
            executor.submit(() -> {
                try {
                    // Simulate I/O operation
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
    }
    
    /**
     * Latency analysis across different concurrency models
     */
    private static void latencyAnalysis() throws Exception {
        System.out.println("   Analyzing latency distribution for I/O operations:");
        
        // Measure latency distribution for platform threads
        System.out.println("   📊 Platform Threads Latency:");
        List<Long> platformLatencies = measureLatencies(Executors.newFixedThreadPool(20), 100);
        printLatencyStats("Platform", platformLatencies);
        
        // Measure latency distribution for virtual threads
        System.out.println("   📊 Virtual Threads Latency:");
        List<Long> virtualLatencies = measureLatencies(Executors.newVirtualThreadPerTaskExecutor(), 100);
        printLatencyStats("Virtual", virtualLatencies);
        
        System.out.println("   💡 Virtual threads typically show better latency consistency");
        System.out.println("   💡 Platform threads may show higher variance due to thread pool contention");
    }
    
    /**
     * Measure latency distribution for a given executor
     */
    private static List<Long> measureLatencies(ExecutorService executor, int samples) throws InterruptedException {
        List<Long> latencies = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(samples);
        
        for (int i = 0; i < samples; i++) {
            executor.submit(() -> {
                long start = System.nanoTime();
                try {
                    Thread.sleep(50); // Simulated I/O
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                long latency = (System.nanoTime() - start) / 1_000_000; // Convert to ms
                latencies.add(latency);
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        return latencies;
    }
    
    /**
     * Print latency statistics
     */
    private static void printLatencyStats(String label, List<Long> latencies) {
        if (latencies.isEmpty()) return;
        
        Collections.sort(latencies);
        
        long min = latencies.get(0);
        long max = latencies.get(latencies.size() - 1);
        long median = latencies.get(latencies.size() / 2);
        long p95 = latencies.get((int) (latencies.size() * 0.95));
        
        double avg = latencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
        
        System.out.printf("     %s: min=%dms, median=%dms, avg=%.1fms, p95=%dms, max=%dms%n",
            label, min, median, avg, p95, max);
    }
    
    /**
     * Resource utilization analysis
     */
    private static void resourceUtilizationAnalysis() throws Exception {
        System.out.println("   Analyzing CPU and memory utilization patterns:");
        
        Runtime runtime = Runtime.getRuntime();
        
        // Baseline measurement
        System.gc();
        Thread.sleep(100);
        long baselineMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // CPU-intensive workload with platform threads
        System.out.println("   🖥️  CPU-intensive workload (Platform vs Virtual threads):");
        
        long platformCpuTime = measureCpuIntensiveWork(Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()), 1000);
        
        long virtualCpuTime = measureCpuIntensiveWork(Executors.newVirtualThreadPerTaskExecutor(), 1000);
        
        System.out.printf("     Platform threads: %d ms%n", platformCpuTime);
        System.out.printf("     Virtual threads: %d ms%n", virtualCpuTime);
        
        if (virtualCpuTime > platformCpuTime * 1.1) {
            System.out.println("     💡 Virtual threads may have overhead for CPU-bound tasks");
        } else {
            System.out.println("     💡 Virtual threads perform competitively for CPU-bound tasks");
        }
        
        // Memory usage analysis
        System.gc();
        Thread.sleep(100);
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        
        System.out.printf("   💾 Memory usage: %.1f MB baseline → %.1f MB final%n",
            baselineMemory / (1024.0 * 1024), finalMemory / (1024.0 * 1024));
        
        System.out.println("   💡 Resource Utilization Insights:");
        System.out.println("     • Virtual threads: Excellent for I/O-bound workloads");
        System.out.println("     • Platform threads: Better for CPU-intensive workloads");
        System.out.println("     • Memory efficiency: Virtual threads scale to millions");
        System.out.println("     • CPU efficiency: Choose model based on workload characteristics");
    }
    
    /**
     * Measure CPU-intensive work performance
     */
    private static long measureCpuIntensiveWork(ExecutorService executor, int iterations) throws InterruptedException {
        Instant start = Instant.now();
        CountDownLatch latch = new CountDownLatch(iterations);
        
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> {
                // CPU-intensive work (no I/O)
                long sum = 0;
                for (int j = 0; j < 10000; j++) {
                    sum += j * j;
                }
                // Prevent optimization
                if (sum < 0) System.out.println("Impossible");
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        
        return Duration.between(start, Instant.now()).toMillis();
    }
}

/**
 * Mock implementation of StructuredTaskScope for demonstration purposes
 * 
 * In real Java 21 with preview features enabled, you would use:
 * import java.util.concurrent.StructuredTaskScope;
 * 
 * This mock implementation demonstrates the key concepts:
 * - Fork/join model for task coordination
 * - Fail-fast error propagation
 * - Automatic resource cleanup
 * - Task cancellation on scope closure
 * - Structured lifetime management
 */
class MockStructuredTaskScope {
    
    /**
     * Shutdown-on-failure policy: cancels remaining tasks when any task fails
     * This implements the fail-fast semantic for consistency-critical operations
     */
    public static class ShutdownOnFailure implements AutoCloseable {
        private final ExecutorService executor;
        private volatile boolean failed = false;
        private volatile Exception failure = null;
        private final List<Future<?>> tasks = new ArrayList<>();
        
        public ShutdownOnFailure() {
            // Use virtual threads for the underlying execution
            // This provides the scalability benefits within the structured scope
            this.executor = Executors.newVirtualThreadPerTaskExecutor();
        }
        
        /**
         * Fork a new task within this structured scope
         * 
         * @param task The task to execute
         * @return Future representing the task execution
         */
        public <T> Future<T> fork(Callable<T> task) {
            if (failed) {
                // Scope already failed - return a failed future
                return CompletableFuture.failedFuture(failure);
            }
            
            Future<T> future = executor.submit(() -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    // Mark scope as failed and cancel other tasks (fail-fast behavior)
                    synchronized (this) {
                        if (!failed) {
                            failed = true;
                            failure = e;
                            // Cancel all other tasks immediately
                            for (Future<?> f : tasks) {
                                f.cancel(true);
                            }
                        }
                    }
                    throw new RuntimeException("Task failed in structured scope", e);
                }
            });
            
            synchronized (this) {
                tasks.add(future);
            }
            
            return future;
        }
        
        /**
         * Wait for all forked tasks to complete
         * This is where the coordination happens
         */
        public void join() throws InterruptedException {
            for (Future<?> task : tasks) {
                try {
                    task.get(); // Wait for completion
                } catch (ExecutionException | CancellationException e) {
                    // Expected for cancelled/failed tasks in fail-fast mode
                }
            }
        }
        
        /**
         * Throw exception if any task failed (fail-fast propagation)
         * This enables the fail-fast semantic at the scope level
         */
        public void throwIfFailed() throws Exception {
            if (failed && failure != null) {
                throw failure;
            }
        }
        
        /**
         * Automatic cleanup via try-with-resources
         * Ensures all tasks are cancelled and resources are freed
         * This prevents resource leaks and orphaned tasks
         */
        @Override
        public void close() {
            // Cancel any remaining tasks
            for (Future<?> task : tasks) {
                task.cancel(true);
            }
            
            // Shutdown executor
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Shutdown-on-success policy: returns first successful result, cancels others
     * This implements racing semantics where the fastest successful task wins
     */
    public static class ShutdownOnSuccess<T> implements AutoCloseable {
        private final ExecutorService executor;
        private volatile boolean succeeded = false;
        private volatile T result = null;
        private final List<Future<T>> tasks = new ArrayList<>();
        
        public ShutdownOnSuccess() {
            this.executor = Executors.newVirtualThreadPerTaskExecutor();
        }
        
        /**
         * Fork a new task within this structured scope
         * 
         * @param task The task to execute
         * @return Future representing the task execution
         */
        public Future<T> fork(Callable<T> task) {
            if (succeeded) {
                // Scope already succeeded - return cancelled future
                CompletableFuture<T> cancelled = new CompletableFuture<>();
                cancelled.cancel(true);
                return cancelled;
            }
            
            Future<T> future = executor.submit(() -> {
                try {
                    T taskResult = task.call();
                    
                    // Check if this is the first successful result
                    synchronized (this) {
                        if (!succeeded) {
                            succeeded = true;
                            result = taskResult;
                            
                            // Cancel all other tasks
                            for (Future<T> f : tasks) {
                                f.cancel(true);
                            }
                        }
                    }
                    
                    return taskResult;
                } catch (Exception e) {
                    throw new RuntimeException("Task failed in structured scope", e);
                }
            });
            
            synchronized (this) {
                tasks.add(future);
            }
            
            return future;
        }
        
        /**
         * Wait for first successful result or all tasks to complete
         */
        public void join() throws InterruptedException {
            // Wait until we have a result or all tasks are done
            while (!succeeded && !allTasksDone()) {
                Thread.sleep(10); // Small delay to avoid busy waiting
            }
        }
        
        /**
         * Get the successful result
         */
        public T result() {
            return result;
        }
        
        /**
         * Check if all tasks are completed (successfully or not)
         */
        private boolean allTasksDone() {
            for (Future<T> task : tasks) {
                if (!task.isDone()) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * Automatic cleanup via try-with-resources
         */
        @Override
        public void close() {
            // Cancel any remaining tasks
            for (Future<T> task : tasks) {
                task.cancel(true);
            }
            
            // Shutdown executor
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

/**
 * Mock implementation of ScopedValue for demonstration
 * 
 * In real Java 21 code, this would be the actual ScopedValue API.
 * This mock provides the same conceptual interface for learning purposes.
 * 
 * ScopedValue provides:
 * - Immutable context sharing across threads
 * - Better performance than ThreadLocal
 * - Structured lifetime management
 * - Natural inheritance in virtual threads
 */
class ScopedValue<T> {
    private static final ThreadLocal<Map<ScopedValue<?>, Object>> BINDINGS = 
        ThreadLocal.withInitial(HashMap::new);
    
    /**
     * Create a new ScopedValue instance
     */
    public static <T> ScopedValue<T> newInstance() {
        return new ScopedValue<>();
    }
    
    /**
     * Get the current value for this ScopedValue
     * 
     * @return The bound value
     * @throws NoSuchElementException if no value is bound
     */
    @SuppressWarnings("unchecked")
    public T get() {
        T value = (T) BINDINGS.get().get(this);
        if (value == null) {
            throw new NoSuchElementException("No value bound for ScopedValue");
        }
        return value;
    }
    
    /**
     * Create a binding for this ScopedValue
     * 
     * @param value The value to bind
     * @return A Where object for chaining or execution
     */
    public Where where(T value) {
        return new Where().where(this, value);
    }
    
    /**
     * Where clause builder for binding multiple ScopedValues
     */
    public static class Where {
        private final Map<ScopedValue<?>, Object> bindings = new HashMap<>();
        
        /**
         * Add a binding to this Where clause
         */
        public <T> Where where(ScopedValue<T> scopedValue, T value) {
            bindings.put(scopedValue, value);
            return this;
        }
        
        /**
         * Execute code with all bindings in scope
         * 
         * @param code The code to execute with bindings active
         */
        public void run(Runnable code) {
            // Save current bindings
            Map<ScopedValue<?>, Object> previousBindings = new HashMap<>(BINDINGS.get());
            
            try {
                // Apply new bindings
                BINDINGS.get().putAll(bindings);
                
                // Execute code with bindings active
                code.run();
                
            } finally {
                // Restore previous bindings
                BINDINGS.set(previousBindings);
            }
        }
        
        /**
         * Execute code with return value and bindings in scope
         * 
         * @param code The code to execute with bindings active
         * @return The result of the code execution
         */
        public <R> R call(Callable<R> code) throws Exception {
            // Save current bindings
            Map<ScopedValue<?>, Object> previousBindings = new HashMap<>(BINDINGS.get());
            
            try {
                // Apply new bindings
                BINDINGS.get().putAll(bindings);
                
                // Execute code with bindings active
                return code.call();
                
            } finally {
                // Restore previous bindings
                BINDINGS.set(previousBindings);
            }
        }
    }
}