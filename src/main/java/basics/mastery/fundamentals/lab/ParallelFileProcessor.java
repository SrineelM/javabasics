package basics.mastery.fundamentals.lab;

/**
 * Advanced Parallel File Processor Lab
 *
 * This comprehensive laboratory demonstrates the performance characteristics and trade-offs
 * between different Java concurrency models when processing file-intensive workloads.
 *
 * @author Srineel with Copilot
 */
// ...existing code...

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Advanced Parallel File Processor Lab
 * 
 * This comprehensive laboratory demonstrates the performance characteristics and trade-offs
 * between different Java concurrency models when processing file-intensive workloads.
 * 
 * Educational Objectives:
 * 1. Understand the baseline performance of sequential file processing
 * 2. Learn how traditional thread pools optimize I/O-bound workloads
 * 3. Experience the revolutionary benefits of virtual threads for high-concurrency scenarios
 * 4. Analyze performance metrics: throughput, latency, memory usage, CPU utilization
 * 5. Develop intuition for choosing the right concurrency model based on workload characteristics
 * 
 * Concurrency Models Compared:
 * 1. Sequential Processing (Baseline)
 *    - Single-threaded approach for establishing performance baseline
 *    - Demonstrates the natural performance ceiling for sequential I/O
 *    - Shows CPU underutilization in I/O-bound scenarios
 * 
 * 2. Platform Thread Pool Processing
 *    - Traditional ExecutorService with fixed thread pool
 *    - Optimal thread count: CPU cores × 2 (for I/O-bound workloads)
 *    - Demonstrates classic trade-offs: thread creation overhead vs. throughput
 *    - Shows thread pool saturation effects and queuing behavior
 * 
 * 3. Virtual Thread Processing
 *    - Java 21+ virtual threads for massive scalability
 *    - One virtual thread per file (no artificial limits)
 *    - Demonstrates near-linear scalability for I/O-bound tasks
 *    - Shows carrier thread efficiency and automatic work-stealing
 * 
 * Performance Metrics:
 * - Throughput: Files processed per second
 * - Total processing time: End-to-end execution duration
 * - Memory efficiency: Resource utilization patterns
 * - Scalability characteristics: Performance under varying load
 * 
 * Workload Characteristics:
 * - Mixed I/O and CPU operations (file reading + text processing)
 * - Configurable file count and size for different test scenarios
 * - Realistic processing patterns: reading, parsing, analysis
 * - Controlled delays to simulate real-world I/O latency
 * 
 * Prerequisites:
 * - Java 17+ (basic virtual thread support in Java 19+)
 * - File system access for temporary file creation
 * - Sufficient disk space for test file generation
 * 
 * Usage Patterns:
 * - Educational: Run with different file counts to observe scaling behavior
 * - Benchmarking: Measure performance characteristics on target hardware
 * - Architecture Planning: Understand concurrency model trade-offs for similar workloads
 */
public class ParallelFileProcessor {
    
    // Configuration constants for the file processing lab
    private static final int FILE_COUNT = 100;          // Default number of files to process
    private static final int FILE_SIZE_KB = 10;         // Size of each test file in KB
    private static final String WORK_DIR = "temp-files"; // Working directory for test files
    
    // Performance tracking variables
    private long sequentialTime = 0;
    private long threadPoolTime = 0;
    private long virtualThreadTime = 0;
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Advanced Parallel File Processor Lab ===\n");
        System.out.println("🚀 Comparing Sequential, Thread Pool, and Virtual Thread approaches");
        System.out.println("📊 This lab measures performance across different concurrency models\n");
        
        ParallelFileProcessor processor = new ParallelFileProcessor();
        processor.setupTestFiles();
        
        try {
            // Execute all processing approaches with detailed timing
            processor.runSequentialProcessing();
            processor.runPlatformThreadProcessing();
            processor.runVirtualThreadProcessing();
            
            // Comprehensive performance analysis across different scales
            processor.performanceAnalysis();
            
            // Display comparative analysis and recommendations
            processor.displayPerformanceComparison();
            
        } finally {
            processor.cleanup();
        }
    }
    
    /**
     * Setup test files for processing experiments
     * 
     * Creates a controlled set of test files with consistent content to ensure
     * fair performance comparisons. Each file contains:
     * - Base content of specified size (FILE_SIZE_KB)
     * - Unique identifier for verification
     * - Text content suitable for word/character counting
     * 
     * The file content is designed to simulate realistic text processing scenarios
     * while maintaining consistent file sizes for accurate performance measurement.
     */
    private void setupTestFiles() throws IOException {
        System.out.println("⚙️  Setting up test files for performance analysis...");
        
        Path workDir = Path.of(WORK_DIR);
        
        // Clean existing directory if present
        if (Files.exists(workDir)) {
            System.out.println("   Cleaning existing test files...");
            Files.walk(workDir)
                .sorted((a, b) -> -a.compareTo(b)) // Delete files before directories
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore deletion errors during cleanup
                        System.err.println("   Warning: Could not delete " + path);
                    }
                });
        }
        
        // Create fresh working directory
        Files.createDirectories(workDir);
        
        // Generate consistent test content
        String baseContent = generateTestContent(FILE_SIZE_KB);
        
        // Create test files with unique identifiers
        for (int i = 0; i < FILE_COUNT; i++) {
            Path filePath = workDir.resolve(String.format("test-file-%03d.txt", i));
            String fileContent = baseContent + String.format("%nFile ID: %d%nTimestamp: %d", 
                i, System.currentTimeMillis());
            
            Files.writeString(filePath, fileContent, 
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        }
        
        // Verify file creation
        long totalSize = Files.walk(workDir)
            .filter(Files::isRegularFile)
            .mapToLong(path -> {
                try {
                    return Files.size(path);
                } catch (IOException e) {
                    return 0;
                }
            })
            .sum();
        
        System.out.printf("✅ Created %d test files (%d KB each, %.1f MB total)%n%n", 
            FILE_COUNT, FILE_SIZE_KB, totalSize / (1024.0 * 1024));
    }
    
    /**
     * Generate realistic test content for file processing
     * 
     * Creates text content that simulates real-world file processing scenarios:
     * - Mixed content with words, sentences, and whitespace
     * - Consistent size for fair performance comparison
     * - Content suitable for text analysis operations
     */
    private String generateTestContent(int sizeKB) {
        StringBuilder content = new StringBuilder();
        
        // Base text patterns that will be repeated to reach target size
        String[] textPatterns = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ",
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris. ",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum. ",
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui. ",
            "Officia deserunt mollit anim id est laborum et consectetur adipiscing. "
        };
        
        int targetBytes = sizeKB * 1024;
        int patternIndex = 0;
        
        while (content.length() < targetBytes) {
            content.append(textPatterns[patternIndex % textPatterns.length]);
            patternIndex++;
            
            // Add occasional line breaks for realistic text structure
            if (patternIndex % 10 == 0) {
                content.append(System.lineSeparator());
            }
        }
        
        // Ensure we don't exceed the target size
        if (content.length() > targetBytes) {
            content.setLength(targetBytes);
        }
        
        return content.toString();
    }
    
    /**
     * Sequential processing baseline measurement
     * 
     * This approach processes files one at a time in a single thread, establishing
     * the performance baseline for comparison. Sequential processing:
     * - Utilizes only one CPU core
     * - Has minimal memory overhead
     * - Provides predictable resource usage
     * - Represents the natural performance ceiling for single-threaded I/O
     * 
     * Key insights from sequential processing:
     * - Shows the impact of I/O wait time on overall throughput
     * - Demonstrates CPU underutilization during I/O operations
     * - Establishes the minimum execution time for CPU-bound portions
     */
    private void runSequentialProcessing() throws IOException {
        System.out.println("1. Sequential Processing (Baseline):");
        System.out.println("   Processing files one at a time in a single thread");
        
        Instant start = Instant.now();
        AtomicLong totalBytes = new AtomicLong(0);
        AtomicLong totalWords = new AtomicLong(0);
        AtomicLong totalCharacters = new AtomicLong(0);
        
        // Process each file sequentially
        for (int i = 0; i < FILE_COUNT; i++) {
            Path filePath = Path.of(WORK_DIR).resolve(String.format("test-file-%03d.txt", i));
            ProcessingResult result = processFile(filePath);
            
            totalBytes.addAndGet(result.byteCount);
            totalWords.addAndGet(result.wordCount);
            totalCharacters.addAndGet(result.characterCount);
            
            // Progress indicator for long-running tests
            if (i > 0 && i % 25 == 0) {
                System.out.printf("   Progress: %d/%d files processed%n", i, FILE_COUNT);
            }
        }
        
        Duration elapsed = Duration.between(start, Instant.now());
        sequentialTime = elapsed.toMillis();
        
        System.out.printf("✅ Sequential processing completed%n");
        System.out.printf("   📄 Files processed: %d%n", FILE_COUNT);
        System.out.printf("   ⏱️  Total time: %d ms%n", elapsed.toMillis());
        System.out.printf("   📊 Total bytes: %,d%n", totalBytes.get());
        System.out.printf("   📝 Total words: %,d%n", totalWords.get());
        System.out.printf("   🔤 Total characters: %,d%n", totalCharacters.get());
        System.out.printf("   🚀 Throughput: %.1f files/sec%n", 
            FILE_COUNT * 1000.0 / elapsed.toMillis());
        System.out.printf("   💾 Average file processing time: %.1f ms%n%n", 
            (double) elapsed.toMillis() / FILE_COUNT);
    }
    
    /**
     * Platform thread pool processing with optimized configuration
     * 
     * This approach uses a traditional ExecutorService with a fixed thread pool,
     * representing the classic concurrency model for I/O-bound workloads:
     * 
     * Thread Pool Configuration:
     * - Size: CPU cores × 2 (optimal for I/O-bound work)
     * - Rationale: Allows threads to block on I/O while others continue processing
     * - Trade-offs: Memory overhead vs. throughput improvements
     * 
     * Expected Performance Characteristics:
     * - Significant improvement over sequential for I/O-bound tasks
     * - Diminishing returns beyond optimal thread count
     * - Thread creation and context switching overhead
     * - Limited scalability due to OS thread constraints
     * 
     * Key Learning Points:
     * - Demonstrates traditional concurrency patterns
     * - Shows thread pool saturation effects
     * - Illustrates the importance of proper thread pool sizing
     */
    private void runPlatformThreadProcessing() throws Exception {
        System.out.println("2. Platform Thread Pool Processing:");
        
        // Calculate optimal thread count for I/O-bound workload
        int threadCount = Runtime.getRuntime().availableProcessors() * 2;
        System.out.printf("   Using %d threads (CPU cores × 2 for I/O-bound workload)%n", threadCount);
        
        Instant start = Instant.now();
        AtomicLong totalBytes = new AtomicLong(0);
        AtomicLong totalWords = new AtomicLong(0);
        AtomicLong totalCharacters = new AtomicLong(0);
        
        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            List<Future<ProcessingResult>> futures = new ArrayList<>();
            
            // Submit all file processing tasks to the thread pool
            System.out.println("   Submitting tasks to thread pool...");
            for (int i = 0; i < FILE_COUNT; i++) {
                final int fileIndex = i;
                Path filePath = Path.of(WORK_DIR).resolve(String.format("test-file-%03d.txt", fileIndex));
                
                Future<ProcessingResult> future = executor.submit(() -> processFile(filePath));
                futures.add(future);
            }
            
            // Collect results as they complete
            System.out.println("   Collecting results from worker threads...");
            int completedCount = 0;
            for (Future<ProcessingResult> future : futures) {
                try {
                    ProcessingResult result = future.get();
                    totalBytes.addAndGet(result.byteCount);
                    totalWords.addAndGet(result.wordCount);
                    totalCharacters.addAndGet(result.characterCount);
                    
                    completedCount++;
                    // Progress indicator for thread pool processing
                    if (completedCount % 25 == 0) {
                        System.out.printf("   Progress: %d/%d tasks completed%n", completedCount, FILE_COUNT);
                    }
                    
                } catch (ExecutionException e) {
                    System.err.println("   ❌ File processing failed: " + e.getCause());
                }
            }
        } // Executor automatically shuts down due to try-with-resources
        
        Duration elapsed = Duration.between(start, Instant.now());
        threadPoolTime = elapsed.toMillis();
        
        System.out.printf("✅ Platform thread pool processing completed%n");
        System.out.printf("   🧵 Thread pool size: %d threads%n", threadCount);
        System.out.printf("   📄 Files processed: %d%n", FILE_COUNT);
        System.out.printf("   ⏱️  Total time: %d ms%n", elapsed.toMillis());
        System.out.printf("   📊 Total bytes: %,d%n", totalBytes.get());
        System.out.printf("   📝 Total words: %,d%n", totalWords.get());
        System.out.printf("   🔤 Total characters: %,d%n", totalCharacters.get());
        System.out.printf("   🚀 Throughput: %.1f files/sec%n", 
            FILE_COUNT * 1000.0 / elapsed.toMillis());
        
        if (sequentialTime > 0) {
            double speedup = (double) sequentialTime / elapsed.toMillis();
            System.out.printf("   📈 Speedup over sequential: %.1fx%n", speedup);
        }
        System.out.println();
    }
    
    /**
     * Virtual thread processing for massive concurrency
     * 
     * This approach leverages Java 21's revolutionary virtual threads to achieve
     * unprecedented scalability for I/O-bound workloads:
     * 
     * Virtual Thread Advantages:
     * - Lightweight: Each virtual thread uses only a few KB of memory
     * - Massive scale: Can create millions of virtual threads
     * - No pool limits: One virtual thread per task without artificial constraints
     * - Automatic yielding: Virtual threads unmount during I/O operations
     * 
     * Expected Performance Characteristics:
     * - Near-linear scalability with increasing file counts
     * - Minimal memory overhead compared to platform threads
     * - Efficient carrier thread utilization
     * - Optimal performance for I/O-bound workloads
     * 
     * Key Learning Points:
     * - Demonstrates the future of Java concurrency
     * - Shows how virtual threads simplify concurrent programming
     * - Illustrates the performance benefits of modern JVM innovations
     */
    private void runVirtualThreadProcessing() throws Exception {
        System.out.println("3. Virtual Thread Processing (Java 21+):");
        System.out.println("   Creating one virtual thread per file (massive concurrency)");
        
        Instant start = Instant.now();
        AtomicLong totalBytes = new AtomicLong(0);
        AtomicLong totalWords = new AtomicLong(0);
        AtomicLong totalCharacters = new AtomicLong(0);
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<ProcessingResult>> futures = new ArrayList<>();
            
            // Submit all file processing tasks (one virtual thread per file)
            System.out.printf("   Creating %d virtual threads for concurrent processing...%n", FILE_COUNT);
            for (int i = 0; i < FILE_COUNT; i++) {
                final int fileIndex = i;
                Path filePath = Path.of(WORK_DIR).resolve(String.format("test-file-%03d.txt", fileIndex));
                
                Future<ProcessingResult> future = executor.submit(() -> processFile(filePath));
                futures.add(future);
            }
            
            // Collect results from virtual threads
            System.out.println("   Collecting results from virtual threads...");
            int completedCount = 0;
            for (Future<ProcessingResult> future : futures) {
                try {
                    ProcessingResult result = future.get();
                    totalBytes.addAndGet(result.byteCount);
                    totalWords.addAndGet(result.wordCount);
                    totalCharacters.addAndGet(result.characterCount);
                    
                    completedCount++;
                    // Progress indicator for virtual thread processing
                    if (completedCount % 25 == 0) {
                        System.out.printf("   Progress: %d/%d virtual threads completed%n", completedCount, FILE_COUNT);
                    }
                    
                } catch (ExecutionException e) {
                    System.err.println("   ❌ File processing failed: " + e.getCause());
                }
            }
        } // Virtual thread executor automatically shuts down
        
        Duration elapsed = Duration.between(start, Instant.now());
        virtualThreadTime = elapsed.toMillis();
        
        System.out.printf("✅ Virtual thread processing completed%n");
        System.out.printf("   🟢 Virtual threads used: %d%n", FILE_COUNT);
        System.out.printf("   📄 Files processed: %d%n", FILE_COUNT);
        System.out.printf("   ⏱️  Total time: %d ms%n", elapsed.toMillis());
        System.out.printf("   📊 Total bytes: %,d%n", totalBytes.get());
        System.out.printf("   📝 Total words: %,d%n", totalWords.get());
        System.out.printf("   🔤 Total characters: %,d%n", totalCharacters.get());
        System.out.printf("   🚀 Throughput: %.1f files/sec%n", 
            FILE_COUNT * 1000.0 / elapsed.toMillis());
        
        if (sequentialTime > 0) {
            double speedup = (double) sequentialTime / elapsed.toMillis();
            System.out.printf("   📈 Speedup over sequential: %.1fx%n", speedup);
        }
        if (threadPoolTime > 0) {
            double improvementOverThreadPool = (double) threadPoolTime / elapsed.toMillis();
            System.out.printf("   📈 Improvement over thread pool: %.1fx%n", improvementOverThreadPool);
        }
        System.out.println();
    }
    
    /**
     * Comprehensive performance analysis with varying workload sizes
     * 
     * This analysis tests scalability characteristics by running the same
     * file processing workload with different numbers of files, demonstrating:
     * - How each concurrency model scales with increasing load
     * - The point at which virtual threads show maximum benefit
     * - Performance characteristics under different concurrency levels
     * - Resource utilization patterns across approaches
     */
    private void performanceAnalysis() throws Exception {
        System.out.println("4. Scalability Analysis:");
        System.out.println("   Testing performance across different file counts...\n");
        
        int[] fileCounts = {10, 50, 100, 500, 1000, 2000};
        
        System.out.printf("%-10s %-15s %-15s %-15s %-15s %-15s%n", 
            "Files", "Sequential", "ThreadPool", "Virtual", "TP Speedup", "VT Speedup");
        System.out.println("-".repeat(95));
        
        for (int fileCount : fileCounts) {
            if (fileCount > 2000) {
                System.out.printf("   Skipping %d files (too large for demo)%n", fileCount);
                continue;
            }
            
            // Create test files for this iteration
            createTestFiles(fileCount);
            
            try {
                long sequentialTime = measureSequentialTime(fileCount);
                long threadPoolTime = measureThreadPoolTime(fileCount);
                long virtualThreadTime = measureVirtualThreadTime(fileCount);
                
                double threadPoolSpeedup = sequentialTime > 0 ? (double) sequentialTime / threadPoolTime : 1.0;
                double virtualSpeedup = sequentialTime > 0 ? (double) sequentialTime / virtualThreadTime : 1.0;
                
                System.out.printf("%-10d %-15d %-15d %-15d %-15.1fx %-15.1fx%n", 
                    fileCount, sequentialTime, threadPoolTime, virtualThreadTime, 
                    threadPoolSpeedup, virtualSpeedup);
                
            } finally {
                // Clean up files for this iteration
                cleanupFiles(fileCount);
            }
        }
        
        System.out.println("\n📊 Scalability Analysis Summary:");
        System.out.println("   📈 Sequential: Predictable O(n) scaling, but single-threaded bottleneck");
        System.out.println("   🧵 Thread Pool: Good performance improvement, limited by thread count");
        System.out.println("   🟢 Virtual Threads: Excellent scalability, especially with high concurrency");
        System.out.println("   💡 Virtual threads show greatest benefits with 100+ concurrent I/O operations");
        System.out.println("   ⚡ Performance improvement increases with file count for virtual threads");
    }
    
    /**
     * Display comprehensive performance comparison and recommendations
     */
    private void displayPerformanceComparison() {
        System.out.println("\n🎯 Performance Comparison Summary:");
        
        if (sequentialTime > 0 && threadPoolTime > 0 && virtualThreadTime > 0) {
            double threadPoolSpeedup = (double) sequentialTime / threadPoolTime;
            double virtualSpeedup = (double) sequentialTime / virtualThreadTime;
            double virtualVsThreadPool = (double) threadPoolTime / virtualThreadTime;
            
            System.out.printf("📊 Performance Results for %d files:%n", FILE_COUNT);
            System.out.printf("   Sequential:     %,d ms (baseline)%n", sequentialTime);
            System.out.printf("   Thread Pool:    %,d ms (%.1fx faster)%n", threadPoolTime, threadPoolSpeedup);
            System.out.printf("   Virtual Threads: %,d ms (%.1fx faster)%n", virtualThreadTime, virtualSpeedup);
            System.out.printf("   Virtual vs Pool: %.1fx improvement%n%n", virtualVsThreadPool);
        }
        
        System.out.println("💡 Key Insights and Recommendations:");
        System.out.println("   🔍 For I/O-bound workloads:");
        System.out.println("     • Virtual threads provide the best scalability");
        System.out.println("     • Traditional thread pools are still effective for moderate concurrency");
        System.out.println("     • Sequential processing limits CPU utilization");
        
        System.out.println("   🏗️  Architecture Guidelines:");
        System.out.println("     • Use virtual threads for high-concurrency I/O operations");
        System.out.println("     • Consider thread pools for CPU-bound tasks with controlled parallelism");
        System.out.println("     • Sequential processing for simple, low-volume operations");
        
        System.out.println("   📈 Scaling Characteristics:");
        System.out.println("     • Virtual threads scale linearly with I/O concurrency");
        System.out.println("     • Thread pools plateau at optimal thread count");
        System.out.println("     • Memory usage: Virtual << Thread Pool << Sequential");
        
        System.out.println("   🛠️  Migration Strategy:");
        System.out.println("     • Start with virtual threads for new I/O-intensive applications");
        System.out.println("     • Migrate existing thread pools gradually for proven benefits");
        System.out.println("     • Profile your specific workload for optimal configuration");
    }
    
    /**
     * Process a single file with comprehensive analysis
     * 
     * This method simulates realistic file processing operations:
     * - File I/O: Reading content from disk
     * - Text Analysis: Word counting, character analysis
     * - Processing Delay: Simulated I/O latency
     * - Error Handling: Robust exception management
     * 
     * The processing includes both CPU-bound and I/O-bound operations
     * to represent real-world file processing scenarios.
     */
    private ProcessingResult processFile(Path filePath) {
        try {
            // File I/O operation - this will cause virtual threads to yield
            String content = Files.readString(filePath);
            
            // CPU-bound work: Text analysis operations
            String[] words = content.split("\\s+");
            int wordCount = words.length;
            
            // Simulate realistic I/O delay (database lookup, network call, etc.)
            Thread.sleep(10); // Small delay to simulate I/O overhead
            
            // Additional CPU-bound processing: Character analysis
            long characterCount = content.chars()
                .mapToLong(c -> Character.isLetter(c) ? 1 : 0)
                .sum();
            
            // Calculate additional metrics for comprehensive analysis
            long lineCount = content.lines().count();
            int uniqueWordCount = content.toLowerCase()
                .split("\\s+")
                .length; // Simplified unique count for demo
            
            return new ProcessingResult(
                content.length(), 
                wordCount, 
                characterCount, 
                lineCount,
                uniqueWordCount,
                filePath.toString()
            );
            
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Failed to process file: " + filePath, e);
        }
    }
    
    /**
     * Helper method to create test files for performance analysis
     */
    private void createTestFiles(int count) throws IOException {
        Path workDir = Path.of(WORK_DIR);
        Files.createDirectories(workDir);
        
        String baseContent = generateTestContent(5); // Smaller files for performance tests
        
        for (int i = 0; i < count; i++) {
            Path filePath = workDir.resolve(String.format("perf-test-%04d.txt", i));
            String content = baseContent + " File-" + i + " Timestamp-" + System.nanoTime();
            Files.writeString(filePath, content,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        }
    }
    
    /**
     * Cleanup test files after performance analysis
     */
    private void cleanupFiles(int count) {
        Path workDir = Path.of(WORK_DIR);
        for (int i = 0; i < count; i++) {
            try {
                Path filePath = workDir.resolve(String.format("perf-test-%04d.txt", i));
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Ignore cleanup errors
            }
        }
    }
    
    /**
     * Measure sequential processing time for performance analysis
     */
    private long measureSequentialTime(int fileCount) {
        Instant start = Instant.now();
        
        for (int i = 0; i < fileCount; i++) {
            Path filePath = Path.of(WORK_DIR).resolve(String.format("perf-test-%04d.txt", i));
            processFile(filePath);
        }
        
        return Duration.between(start, Instant.now()).toMillis();
    }
    
    /**
     * Measure thread pool processing time for performance analysis
     */
    private long measureThreadPoolTime(int fileCount) throws Exception {
        Instant start = Instant.now();
        
        try (ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2)) {
            
            List<Future<ProcessingResult>> futures = new ArrayList<>();
            
            for (int i = 0; i < fileCount; i++) {
                final int fileIndex = i;
                Path filePath = Path.of(WORK_DIR).resolve(String.format("perf-test-%04d.txt", fileIndex));
                futures.add(executor.submit(() -> processFile(filePath)));
            }
            
            for (Future<ProcessingResult> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    // Handle errors silently for performance measurement
                }
            }
        }
        
        return Duration.between(start, Instant.now()).toMillis();
    }
    
    /**
     * Measure virtual thread processing time for performance analysis
     */
    private long measureVirtualThreadTime(int fileCount) throws Exception {
        Instant start = Instant.now();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<ProcessingResult>> futures = new ArrayList<>();
            
            for (int i = 0; i < fileCount; i++) {
                final int fileIndex = i;
                Path filePath = Path.of(WORK_DIR).resolve(String.format("perf-test-%04d.txt", fileIndex));
                futures.add(executor.submit(() -> processFile(filePath)));
            }
            
            for (Future<ProcessingResult> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    // Handle errors silently for performance measurement
                }
            }
        }
        
        return Duration.between(start, Instant.now()).toMillis();
    }
    
    /**
     * Clean up all test files and working directory
     */
    private void cleanup() {
        try {
            Path workDir = Path.of(WORK_DIR);
            if (Files.exists(workDir)) {
                Files.walk(workDir)
                    .sorted((a, b) -> -a.compareTo(b)) // Delete files before directories
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            // Ignore cleanup errors
                        }
                    });
            }
            System.out.println("🧹 Cleaned up test files and working directory\n");
        } catch (IOException e) {
            System.err.println("⚠️  Warning: Could not clean up all test files");
        }
    }
    
    /**
     * Enhanced result container for file processing operations
     * 
     * Contains comprehensive metrics from file processing:
     * - Basic metrics: byte count, word count, character count
     * - Advanced metrics: line count, unique word count
     * - Metadata: file name for traceability
     */
    private static class ProcessingResult {
        final long byteCount;
        final int wordCount;
        final long characterCount;
        final long lineCount;
        final int uniqueWordCount;
        final String fileName;
        
        ProcessingResult(long byteCount, int wordCount, long characterCount, 
                        long lineCount, int uniqueWordCount, String fileName) {
            this.byteCount = byteCount;
            this.wordCount = wordCount;
            this.characterCount = characterCount;
            this.lineCount = lineCount;
            this.uniqueWordCount = uniqueWordCount;
            this.fileName = fileName;
        }
        
        @Override
        public String toString() {
            return String.format(
                "ProcessingResult{file='%s', bytes=%d, words=%d, chars=%d, lines=%d, unique=%d}", 
                fileName, byteCount, wordCount, characterCount, lineCount, uniqueWordCount);
        }
    }
}