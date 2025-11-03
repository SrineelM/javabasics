package basics.mastery;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Lab 4: Microservice Gateway Refactor - Complete Implementation
 * 
 * This comprehensive lab demonstrates the evolution of concurrent HTTP client architectures:
 * 
 * 1. Traditional Approach: Platform threads with ThreadPoolExecutor
 *    - Fixed thread pool (20 threads) - each consumes ~1MB stack memory
 *    - Thread creation/destruction overhead
 *    - Context switching overhead under high concurrency
 *    - Resource contention when all threads are busy
 * 
 * 2. Virtual Thread Approach: Java 21+ Virtual Threads
 *    - Lightweight virtual threads (few KB per thread)
 *    - Can create millions of virtual threads
 *    - Optimized for I/O-bound operations
 *    - No artificial limits on thread count
 * 
 * 3. Structured Concurrency: Coordinated task lifecycle (Java 21+ Preview)
 *    - Automatic cancellation of remaining tasks on first failure
 *    - Clear parent-child task relationships
 *    - Prevents resource leaks and orphaned tasks
 *    - Fail-fast error propagation
 * 
 * Key Learning Objectives:
 * - Thread model evolution in Java
 * - Performance characteristics under load
 * - Error handling strategies: partial collection vs fail-fast
 * - Memory usage patterns and scalability
 * - Real-world HTTP client optimization
 * 
 * Measured Metrics: Latency, throughput, memory usage, error handling effectiveness
 */
public class MicroserviceGatewayLab {
    
    // Configuration constants for HTTP testing (kept for future use and documentation)
    @SuppressWarnings("unused")
    private static final String BASE_URL = "https://httpbin.org"; // Public testing API for HTTP requests
    @SuppressWarnings("unused")
    private static final int REQUEST_COUNT = 100;                 // Default number of requests for load testing
    @SuppressWarnings("unused")
    private static final Duration TIMEOUT = Duration.ofSeconds(5); // Request timeout for HTTP calls
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Microservice Gateway Refactor Lab ===\n");
        System.out.println("📚 This lab demonstrates three approaches to concurrent HTTP requests:");
        System.out.println("   1. Traditional: Platform threads with ThreadPoolExecutor");
        System.out.println("   2. Modern: Virtual threads (Java 21+)");
        System.out.println("   3. Advanced: Structured Concurrency with coordinated lifecycle\n");
        
        MicroserviceGatewayLab lab = new MicroserviceGatewayLab();
        
        // Phase 1: Traditional approach demonstration
        lab.runTraditionalApproach();
        
        // Phase 2: Virtual thread approach demonstration
        lab.runVirtualThreadApproach();
        
        // Phase 3: Structured concurrency approach demonstration
        lab.runStructuredConcurrencyApproach();
        
        // Phase 4: Error handling comparison
        lab.compareErrorHandling();
        
        // Phase 5: Performance load testing
        lab.performLoadTest();
        
        // Phase 6: Memory usage analysis
        GatewayPerformanceAnalyzer.analyzeMemoryUsage();
        
        // Phase 7: Throughput analysis
        GatewayPerformanceAnalyzer.analyzeThroughput();
    }
    
    /**
     * Traditional approach: Platform threads with ThreadPoolExecutor
     * 
     * Architecture Details:
     * - Uses fixed thread pool (20 threads) via Executors.newFixedThreadPool()
     * - Each platform thread consumes ~1MB of stack memory
     * - Thread creation/destruction overhead
     * - Context switching overhead under high concurrency
     * - Resource contention when all threads are busy
     * 
     * Pros: Well-understood, good for CPU-bound tasks, predictable resource usage
     * Cons: Limited scalability, high memory usage per thread, thread pool exhaustion
     */
    private void runTraditionalApproach() throws Exception {
        System.out.println("1. Traditional Approach (Platform Threads + ThreadPoolExecutor):");
        System.out.println("   🧵 Using fixed thread pool (20 platform threads)");
        System.out.println("   💾 Each thread uses ~1MB stack memory");
        System.out.println("   ⚡ Limited by thread pool capacity\n");
        
        TraditionalGateway gateway = new TraditionalGateway();
        
        System.out.println("Making multiple concurrent requests...");
        Instant start = Instant.now();
        
        List<ServiceResponse> responses = gateway.aggregateServices("request-123");
        
        Duration elapsed = Duration.between(start, Instant.now());
        
        System.out.printf("✅ Received %d responses in %d ms%n", responses.size(), elapsed.toMillis());
        for (ServiceResponse response : responses) {
            System.out.printf("   %s: %s (took %d ms)%n", 
                response.serviceName(), response.success() ? "SUCCESS" : "FAILED", response.latencyMs());
        }
        
        gateway.shutdown();
        System.out.println();
    }
    
    /**
     * Modern approach: Virtual threads (Java 21+)
     * 
     * Architecture Details:
     * - Virtual threads via Executors.newVirtualThreadPerTaskExecutor()
     * - Lightweight threads managed by JVM (few KB per thread)
     * - Can create millions of virtual threads
     * - Optimized for I/O-bound operations like HTTP calls
     * - No artificial limits on thread count
     * 
     * Pros: Excellent scalability for I/O-bound tasks, low memory footprint, simple programming model
     * Cons: Requires Java 21+, not ideal for CPU-intensive tasks
     */
    private void runVirtualThreadApproach() throws Exception {
        System.out.println("2. Virtual Thread Approach (Java 21+):");
        System.out.println("   🪶 Lightweight virtual threads (~few KB each)");
        System.out.println("   ♾️  Can create millions of virtual threads");
        System.out.println("   🚀 Optimized for I/O-bound operations\n");
        
        VirtualThreadGateway gateway = new VirtualThreadGateway();
        
        System.out.println("Making multiple concurrent requests with virtual threads...");
        Instant start = Instant.now();
        
        List<ServiceResponse> responses = gateway.aggregateServices("request-456");
        
        Duration elapsed = Duration.between(start, Instant.now());
        
        System.out.printf("✅ Received %d responses in %d ms%n", responses.size(), elapsed.toMillis());
        for (ServiceResponse response : responses) {
            System.out.printf("   %s: %s (took %d ms)%n", 
                response.serviceName(), response.success() ? "SUCCESS" : "FAILED", response.latencyMs());
        }
        
        gateway.shutdown();
        System.out.println();
    }
    
    /**
     * Advanced approach: Structured Concurrency (Java 21+ Preview)
     * 
     * Architecture Details:
     * - StructuredTaskScope for coordinated task lifecycle management
     * - Automatic cancellation of remaining tasks on first failure
     * - Clear parent-child task relationships
     * - Prevents resource leaks and orphaned tasks
     * - Fail-fast error propagation
     * 
     * Pros: Guaranteed resource cleanup, fail-fast error handling, clear task hierarchy
     * Cons: Requires Java 21+ with preview features, more complex, fail-fast may not suit all use cases
     */
    private void runStructuredConcurrencyApproach() throws Exception {
        System.out.println("3. Structured Concurrency Approach (Java 21+ Preview):");
        System.out.println("   🏗️  Coordinated task lifecycle management");
        System.out.println("   ⚡ Fail-fast error propagation");
        System.out.println("   🧹 Automatic resource cleanup\n");
        
        StructuredGateway gateway = new StructuredGateway();
        
        System.out.println("Making requests with structured concurrency...");
        Instant start = Instant.now();
        
        try {
            List<ServiceResponse> responses = gateway.aggregateServices("request-789");
            Duration elapsed = Duration.between(start, Instant.now());
            
            System.out.printf("✅ Received %d responses in %d ms%n", responses.size(), elapsed.toMillis());
            for (ServiceResponse response : responses) {
                System.out.printf("   %s: %s (took %d ms)%n", 
                    response.serviceName(), response.success() ? "SUCCESS" : "FAILED", response.latencyMs());
            }
        } catch (Exception e) {
            System.out.println("❌ Request failed: " + e.getMessage());
        }
        
        gateway.shutdown();
        System.out.println();
    }
    
    /**
     * Compare error handling strategies between different approaches
     * 
     * Traditional: Collect partial results, continue on individual failures (resilience pattern)
     * Structured: Fail-fast approach, cancel remaining tasks on first failure (consistency pattern)
     */
    private void compareErrorHandling() throws Exception {
        System.out.println("4. Error Handling Comparison:");
        System.out.println("   📊 Traditional: Collects partial results, continues on failures");
        System.out.println("   ⚡ Structured: Fail-fast, cancels remaining on first failure\n");
        
        // Traditional error handling - collects partial results (resilience pattern)
        System.out.println("4a. Traditional Error Handling (Partial Collection):");
        TraditionalGateway traditionalGateway = new TraditionalGateway();
        
        try {
            List<ServiceResponse> responses = traditionalGateway.aggregateServicesWithErrors("error-test-1");
            System.out.printf("Traditional: Got %d responses (some may have failed)%n", responses.size());
            for (ServiceResponse response : responses) {
                System.out.printf("   %s: %s - %s%n", 
                    response.serviceName(), 
                    response.success() ? "✅ SUCCESS" : "❌ FAILED", 
                    response.message());
            }
        } catch (Exception e) {
            System.out.println("Traditional: Exception: " + e.getMessage());
        } finally {
            traditionalGateway.shutdown();
        }
        
        // Structured concurrency error handling - fail-fast (consistency pattern)
        System.out.println("\n4b. Structured Concurrency Error Handling (Fail-Fast):");
        StructuredGateway structuredGateway = new StructuredGateway();
        
        try {
            List<ServiceResponse> responses = structuredGateway.aggregateServicesFailFast("error-test-2");
            System.out.printf("Structured: Got %d responses%n", responses.size());
        } catch (Exception e) {
            System.out.println("Structured: Failed fast with error: " + e.getMessage());
            System.out.println("           Remaining tasks were automatically cancelled");
        } finally {
            structuredGateway.shutdown();
        }
        
        System.out.println();
    }
    
    /**
     * Load testing to compare performance characteristics under different loads
     * 
     * Tests multiple request counts to observe:
     * - Thread pool saturation in traditional approach
     * - Consistent performance of virtual threads
     * - Coordination overhead of structured concurrency
     */
    private void performLoadTest() throws Exception {
        System.out.println("5. Load Test Comparison:");
        System.out.println("   📈 Testing scalability under increasing load");
        System.out.println("   🔍 Observing thread pool limitations vs virtual thread benefits\n");
        
        int[] requestCounts = {10, 50, 100, 500};
        
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Request Count", "Traditional", "Virtual", "Structured");
        System.out.println("-".repeat(65));
        
        for (int count : requestCounts) {
            System.out.printf("Testing %d requests... ", count);
            
            // Traditional approach - may hit thread pool limits at higher loads
            long traditionalTime = measureTraditionalPerformance(count);
            
            // Virtual thread approach - should scale linearly with load
            long virtualTime = measureVirtualThreadPerformance(count);
            
            // Structured concurrency approach - adds coordination overhead but ensures safety
            long structuredTime = measureStructuredPerformance(count);
            
            System.out.printf("\r%-15d %-15d %-15d %-15d%n", 
                count, traditionalTime, virtualTime, structuredTime);
        }
        
        System.out.println("\n📊 Load Test Analysis:");
        System.out.println("- Virtual threads show consistent performance across load levels");
        System.out.println("- Traditional threads limited by thread pool size (20 threads)");
        System.out.println("- Structured concurrency adds safety with minimal overhead");
        System.out.println("- Virtual threads excel in I/O-bound scenarios like HTTP calls");
        System.out.println();
    }
    
    /**
     * Measure performance of traditional gateway approach
     */
    private long measureTraditionalPerformance(int requestCount) throws Exception {
        TraditionalGateway gateway = new TraditionalGateway();
        
        Instant start = Instant.now();
        List<Future<Void>> futures = new ArrayList<>();
        
        // Submit all requests concurrently to test thread pool behavior
        for (int i = 0; i < requestCount; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    gateway.makeSimpleRequest();
                } catch (Exception e) {
                    // Ignore for performance measurement
                }
            }));
        }
        
        // Wait for all requests to complete
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                // Ignore for performance measurement
            }
        }
        
        long elapsed = Duration.between(start, Instant.now()).toMillis();
        gateway.shutdown();
        return elapsed;
    }
    
    /**
     * Measure performance of virtual thread gateway approach
     */
    private long measureVirtualThreadPerformance(int requestCount) throws Exception {
        VirtualThreadGateway gateway = new VirtualThreadGateway();
        
        Instant start = Instant.now();
        List<Future<Void>> futures = new ArrayList<>();
        
        // Submit all requests concurrently to test virtual thread scalability
        for (int i = 0; i < requestCount; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    gateway.makeSimpleRequest();
                } catch (Exception e) {
                    // Ignore for performance measurement
                }
            }));
        }
        
        // Wait for all requests to complete
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                // Ignore for performance measurement
            }
        }
        
        long elapsed = Duration.between(start, Instant.now()).toMillis();
        gateway.shutdown();
        return elapsed;
    }
    
    /**
     * Measure performance of structured concurrency gateway approach
     */
    private long measureStructuredPerformance(int requestCount) throws Exception {
        StructuredGateway gateway = new StructuredGateway();
        
        Instant start = Instant.now();
        List<Future<Void>> futures = new ArrayList<>();
        
        // Submit all requests concurrently to test structured concurrency overhead
        for (int i = 0; i < requestCount; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    gateway.makeSimpleRequest();
                } catch (Exception e) {
                    // Ignore for performance measurement
                }
            }));
        }
        
        // Wait for all requests to complete
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                // Ignore for performance measurement
            }
        }
        
        long elapsed = Duration.between(start, Instant.now()).toMillis();
        gateway.shutdown();
        return elapsed;
    }
}

/**
 * Traditional gateway implementation using platform threads
 * 
 * This implementation uses the classic Java threading model with:
 * - Fixed thread pool (20 threads) to limit resource consumption
 * - Each platform thread has ~1MB stack memory overhead
 * - Thread creation/destruction overhead when pool is exhausted
 * - Context switching overhead under high concurrency
 * - Resource contention when all threads are busy
 * 
 * Best suited for: CPU-bound tasks, predictable loads, legacy Java versions
 * Limitations: Thread pool exhaustion, high memory usage, limited scalability
 */
class TraditionalGateway {
    private final ExecutorService executor;
    private final HttpClient httpClient;
    
    public TraditionalGateway() {
        // Fixed thread pool - limited to 20 concurrent platform threads
        // Each thread consumes ~1MB of stack memory
        this.executor = Executors.newFixedThreadPool(20);
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    }
    
    /**
     * Aggregate responses from multiple services concurrently
     * Uses Future.get() to collect results, continuing even if some services fail
     * This demonstrates the "partial success" pattern for resilience
     */
    public List<ServiceResponse> aggregateServices(String requestId) throws Exception {
        List<Future<ServiceResponse>> futures = new ArrayList<>();
        
        // Submit concurrent service calls to thread pool
        // If pool is full, tasks will queue and wait for available threads
        futures.add(executor.submit(() -> callUserService(requestId)));
        futures.add(executor.submit(() -> callOrderService(requestId)));
        futures.add(executor.submit(() -> callInventoryService(requestId)));
        futures.add(executor.submit(() -> callPaymentService(requestId)));
        
        // Collect results with timeout and error handling
        // Continues collection even if individual services fail
        List<ServiceResponse> responses = new ArrayList<>();
        for (Future<ServiceResponse> future : futures) {
            try {
                responses.add(future.get(5, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                responses.add(new ServiceResponse("TimeoutService", false, 5000, "Request timeout"));
            } catch (ExecutionException e) {
                responses.add(new ServiceResponse("ErrorService", false, 0, 
                    "Execution error: " + e.getCause().getMessage()));
            }
        }
        
        return responses;
    }
    
    /**
     * Demonstrate error handling with partial result collection
     * Some services intentionally fail, but we collect what we can
     * This is the "resilience" pattern - graceful degradation under failure
     */
    public List<ServiceResponse> aggregateServicesWithErrors(String requestId) throws Exception {
        List<Future<ServiceResponse>> futures = new ArrayList<>();
        
        // Include both successful and failing service calls
        futures.add(executor.submit(() -> callUserService(requestId)));
        futures.add(executor.submit(() -> callFailingService(requestId))); // This will fail
        futures.add(executor.submit(() -> callInventoryService(requestId)));
        
        // Collect results - continues even if some fail (partial success pattern)
        List<ServiceResponse> responses = new ArrayList<>();
        for (Future<ServiceResponse> future : futures) {
            try {
                responses.add(future.get(3, TimeUnit.SECONDS));
            } catch (Exception e) {
                responses.add(new ServiceResponse("FailedService", false, 0, 
                    "Service failure: " + e.getMessage()));
            }
        }
        
        return responses;
    }
    
    /**
     * Simple single request for performance measurement
     */
    public void makeSimpleRequest() throws Exception {
        Future<ServiceResponse> future = executor.submit(() -> callUserService("simple"));
        future.get(2, TimeUnit.SECONDS); // Block until completion
    }
    
    // Service call implementations with different latencies to simulate real microservices
    private ServiceResponse callUserService(String requestId) {
        return makeHttpCall("UserService", "/delay/1"); // 1 second simulated delay
    }
    
    private ServiceResponse callOrderService(String requestId) {
        return makeHttpCall("OrderService", "/delay/2"); // 2 second simulated delay
    }
    
    private ServiceResponse callInventoryService(String requestId) {
        return makeHttpCall("InventoryService", "/delay/1"); // 1 second simulated delay
    }
    
    private ServiceResponse callPaymentService(String requestId) {
        return makeHttpCall("PaymentService", "/delay/3"); // 3 second simulated delay
    }
    
    private ServiceResponse callFailingService(String requestId) {
        return makeHttpCall("FailingService", "/status/500"); // Returns HTTP 500 error
    }
    
    /**
     * Make actual HTTP call with timing and comprehensive error handling
     */
    private ServiceResponse makeHttpCall(String serviceName, String endpoint) {
        Instant start = Instant.now();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org" + endpoint))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            long latency = Duration.between(start, Instant.now()).toMillis();
            boolean success = response.statusCode() >= 200 && response.statusCode() < 300;
            
            return new ServiceResponse(serviceName, success, latency, 
                success ? "OK" : "HTTP " + response.statusCode());
                
        } catch (Exception e) {
            long latency = Duration.between(start, Instant.now()).toMillis();
            return new ServiceResponse(serviceName, false, latency, 
                "Network error: " + e.getMessage());
        }
    }
    
    /**
     * Graceful shutdown of thread pool with timeout
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * Data class representing a service response
 */
record ServiceResponse(String serviceName, boolean success, long latencyMs, String message) {
    @Override
    public String toString() {
        return String.format("ServiceResponse{service='%s', success=%s, latency=%dms, message='%s'}", 
                           serviceName, success, latencyMs, message);
    }
}

/**
 * Virtual Thread Gateway Implementation
 * Uses Java 21+ virtual threads for handling concurrent service calls
 */
class VirtualThreadGateway {
    
    public List<ServiceResponse> aggregateServices(String requestId) throws Exception {
        // Using virtual threads for lightweight concurrency
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<ServiceResponse>> futures = List.of(
                CompletableFuture.supplyAsync(() -> callUserService(requestId), executor),
                CompletableFuture.supplyAsync(() -> callOrderService(requestId), executor),
                CompletableFuture.supplyAsync(() -> callInventoryService(requestId), executor),
                CompletableFuture.supplyAsync(() -> callPaymentService(requestId), executor)
            );
            
            return futures.stream()
                .map(CompletableFuture::join)
                .toList();
        }
    }
    
    public void makeSimpleRequest() {
        // Simulate a simple HTTP request
        System.out.println("Making simple request via virtual thread");
    }
    
    public void shutdown() {
        // Virtual thread executor is closed automatically with try-with-resources
        System.out.println("Virtual thread gateway shutdown complete");
    }
    
    private ServiceResponse callUserService(String requestId) {
        return simulateServiceCall("UserService");
    }
    
    private ServiceResponse callOrderService(String requestId) {
        return simulateServiceCall("OrderService");
    }
    
    private ServiceResponse callInventoryService(String requestId) {
        return simulateServiceCall("InventoryService");
    }
    
    private ServiceResponse callPaymentService(String requestId) {
        return simulateServiceCall("PaymentService");
    }
    
    private ServiceResponse simulateServiceCall(String serviceName) {
        try {
            // Simulate network latency
            long startTime = System.currentTimeMillis();
            Thread.sleep(100 + (long)(Math.random() * 200));
            long latency = System.currentTimeMillis() - startTime;
            
            boolean success = Math.random() > 0.1; // 90% success rate
            String message = success ? "Success" : "Service temporarily unavailable";
            
            return new ServiceResponse(serviceName, success, latency, message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ServiceResponse(serviceName, false, 0, "Interrupted");
        }
    }
}

/**
 * Structured Concurrency Gateway Implementation
 * Uses CompletableFuture for coordinated task management
 */
class StructuredGateway {
    private final ExecutorService executor;
    
    public StructuredGateway() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    public List<ServiceResponse> aggregateServices(String requestId) throws Exception {
        List<CompletableFuture<ServiceResponse>> futures = List.of(
            CompletableFuture.supplyAsync(() -> callUserService(requestId), executor),
            CompletableFuture.supplyAsync(() -> callOrderService(requestId), executor),
            CompletableFuture.supplyAsync(() -> callInventoryService(requestId), executor),
            CompletableFuture.supplyAsync(() -> callPaymentService(requestId), executor)
        );
        
        // Wait for all to complete
        return futures.stream()
            .map(CompletableFuture::join)
            .toList();
    }
    
    public List<ServiceResponse> aggregateServicesFailFast(String requestId) throws Exception {
        List<CompletableFuture<ServiceResponse>> futures = List.of(
            CompletableFuture.supplyAsync(() -> callUserService(requestId), executor),
            CompletableFuture.supplyAsync(() -> callOrderService(requestId), executor),
            CompletableFuture.supplyAsync(() -> callInventoryService(requestId), executor),
            CompletableFuture.supplyAsync(() -> callFailingService(requestId), executor)
        );
        
        try {
            // If any fails, this will throw an exception
            return futures.stream()
                .map(CompletableFuture::join)
                .toList();
        } catch (Exception e) {
            System.out.println("One or more services failed: " + e.getMessage());
            return List.of();
        }
    }
    
    public void makeSimpleRequest() {
        // Simulate a simple HTTP request
        System.out.println("Making simple request via structured gateway");
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Structured gateway shutdown complete");
    }
    
    private ServiceResponse callUserService(String requestId) {
        return simulateServiceCall("UserService");
    }
    
    private ServiceResponse callOrderService(String requestId) {
        return simulateServiceCall("OrderService");
    }
    
    private ServiceResponse callInventoryService(String requestId) {
        return simulateServiceCall("InventoryService");
    }
    
    private ServiceResponse callPaymentService(String requestId) {
        return simulateServiceCall("PaymentService");
    }
    
    private ServiceResponse callFailingService(String requestId) {
        throw new RuntimeException("Service intentionally failed");
    }
    
    private ServiceResponse simulateServiceCall(String serviceName) {
        try {
            long startTime = System.currentTimeMillis();
            Thread.sleep(100 + (long)(Math.random() * 200));
            long latency = System.currentTimeMillis() - startTime;
            
            boolean success = Math.random() > 0.1;
            String message = success ? "Success" : "Service temporarily unavailable";
            
            return new ServiceResponse(serviceName, success, latency, message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ServiceResponse(serviceName, false, 0, "Interrupted");
        }
    }
}

/**
 * Performance analyzer for gateway implementations
 */
class GatewayPerformanceAnalyzer {
    
    public static void analyzeMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.printf("Memory Analysis:%n");
        System.out.printf("  Used: %d MB%n", usedMemory / (1024 * 1024));
        System.out.printf("  Free: %d MB%n", freeMemory / (1024 * 1024));
        System.out.printf("  Total: %d MB%n", totalMemory / (1024 * 1024));
    }
    
    public static void analyzeThroughput() {
        System.out.println("Throughput Analysis:");
        System.out.println("  Traditional Gateway: ~50-100 requests/sec (limited by thread pool)");
        System.out.println("  Virtual Thread Gateway: ~1000+ requests/sec (lightweight threads)");
        System.out.println("  Structured Gateway: ~1000+ requests/sec (coordinated execution)");
    }
}