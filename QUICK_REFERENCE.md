# 🚀 JavaBasics Tutorial - Quick Reference Card

## Essential Commands

### Build & Test
```bash
cd /Volumes/Work/JavaBasics/javabasics

# Build
./gradlew build

# Test
./gradlew test

# Clean build
./gradlew clean build

# Run main tutorial
java -cp build/classes/java/main basics.mastery.MainApplication
```

## Module Quick Launch

### Beginner Level
```bash
# Thread Basics (START HERE)
java -cp build/classes/java/main basics.mastery.fundamentals.ThreadBasicsDemo

# Collections Fundamentals  
java -cp build/classes/java/main basics.mastery.java.collections.fundamentals.ListCollectionsDemo
java -cp build/classes/java/main basics.mastery.collections.fundamentals.SetCollectionsDemo
```

### Intermediate Level
```bash
# Synchronization (8 mechanisms)
java -cp build/classes/java/main basics.mastery.synchronization.SynchronizationPrimitivesDemo

# Concurrent Collections
java -cp build/classes/java/main basics.mastery.collections.concurrent.ConcurrentCollectionsDemo

# Generics PECS
java -cp build/classes/java/main basics.mastery.generics.advanced.GenericsPecosDemo
```

### Advanced Level
```bash
# Modern Concurrency (JDK 21)
java -cp build/classes/java/main basics.mastery.modern.ModernConcurrencyDemo

# Sequenced Collections (JDK 21 - NEW!)
java -cp build/classes/java/main basics.mastery.modern.SequencedCollectionsDemo

# Memory Model
java -cp build/classes/java/main basics.mastery.JavaMemoryModelDemo

# Best Practices (50+ patterns)
java -cp build/classes/java/main basics.mastery.collections.patterns.BestPracticesDemo
```

### Expert Level
```bash
# Virtual Thread Pinning
java -cp build/classes/java/main basics.mastery.modern.VirtualThreadPinningDemo

# Lock-Free Counter
java -cp build/classes/java/main basics.mastery.advanced.LockFreeCounterDemo

# Microservice Gateway
java -cp build/classes/java/main basics.mastery.MicroserviceGatewayLab

# Concurrency Pitfalls
java -cp build/classes/java/main basics.mastery.ConcurrencyPitfallsDemo
```

## Key Concepts Cheat Sheet

### Thread Creation
```java
// Method 1: Extends Thread
Thread t1 = new Thread() {
    public void run() { /* work */ }
};
t1.start();

// Method 2: Implements Runnable (PREFERRED)
Thread t2 = new Thread(() -> { /* work */ });
t2.start();

// Method 3: Virtual Thread (JDK 21)
Thread t3 = Thread.ofVirtual().start(() -> { /* work */ });
```

### Synchronization Quick Picks
```java
synchronized (lock) { }              // Basic
ReentrantLock lock = new ReentrantLock(); // Trylock, timeout
ReadWriteLock rwLock = ...;          // Multiple readers
CountDownLatch latch = ...;          // Wait for N threads
CyclicBarrier barrier = ...;         // Multi-phase
Semaphore sem = new Semaphore(N);    // Limit concurrent access
AtomicInteger counter = ...;         // Lock-free
```

### Collection Selection
```java
List<T>   -> ArrayList     // Random access, O(1)
          -> LinkedList    // Insert/delete, O(1)
Set<T>    -> HashSet       // Unique, unordered, O(1)
          -> TreeSet       // Sorted, O(log n)
          -> LinkedHashSet // Insertion order
Map<K,V>  -> HashMap       // O(1) lookup
          -> TreeMap       // Sorted keys
          -> LinkedHashMap // Insertion order
Queue<T>  -> ArrayDeque    // FIFO/LIFO
          -> PriorityQueue // Heap order
```

### JDK 21 Sequenced Collections
```java
List<String> list = List.of("A", "B", "C");
list.getFirst();          // "A"
list.getLast();           // "C"  
list.reversed();          // ["C", "B", "A"]
list.addFirst("Z");       // ["Z", "A", "B", "C"]
list.removeLast();        // ["Z", "A", "B"]
```

### Generics PECS Rule
```java
// Producer Extends - READ from
List<? extends Number> producer = ...;
Number n = producer.get(0);  // OK

// Consumer Super - WRITE to
List<? super Integer> consumer = ...;
consumer.add(42);  // OK

// Copy method using PECS
<T> void copy(List<? extends T> src, List<? super T> dest)
```

## Testing

### Run Specific Tests
```bash
./gradlew test --tests ThreadBasicsDemoTest
./gradlew test --tests CollectionsFundamentalsTest
./gradlew test --tests GenericsTest
./gradlew test --tests SynchronizationTest
```

### View Test Reports
```bash
./gradlew test
open build/reports/tests/test/index.html
```

## Performance

### Run Benchmarks
```bash
./gradlew jmh
cat build/reports/jmh/results.txt
```

## Documentation

- **README.md** - Project overview & features
- **INSTRUCTIONS.md** - Step-by-step learning
- **TUTORIAL_PLAN.md** - Architecture & structure
- **COMPLETE_REVIEW.md** - Best practices & patterns
- **FINAL_SUMMARY.md** - Delivery summary

## Interactive Tutorial Menu

Run `MainApplication` to access:
- 1️⃣ Thread Fundamentals
- 2️⃣ Synchronization Primitives
- 3️⃣ Parallel File Processing Lab
- 4️⃣ Modern Concurrency (Java 21) ⭐
- 5️⃣ Microservice Gateway Lab
- 6️⃣ Java Memory Model Demo
- 7️⃣ Concurrency Pitfalls Demo
- 8️⃣ Counter Service Benchmark
- 9️⃣ Thread Basics Demo
- 🔟 Virtual Thread Pinning Demo
- 1️⃣1️⃣ Fail-Safe Iteration Demo
- 1️⃣2️⃣ Hashing & Comparator Pitfalls
- 1️⃣3️⃣ Generics: PECS & Variance
- 1️⃣4️⃣ Lock-Free Counter (CAS)
- 1️⃣5️⃣ Thread Diagnostics Quickstart

## Recommended Learning Sequence

**Beginner:** 1 → 9 → Collections demos  
**Intermediate:** 2 → 6 → 11 → 12  
**Advanced:** 4 → 10 → 7 → 8  
**Expert:** 13 → 14 → 5 → Benchmarks

## Common Issues

**Build fails:** `./gradlew clean build --refresh-dependencies`  
**JDK 21 not found:** Verify with `java --version`  
**Tests timeout:** Increase timeout in test annotations  
**OutOfMemory:** `./gradlew test -Dorg.gradle.jvmargs="-Xmx4g"`

## Key Files

- `MainApplication.java` - Interactive launcher
- `build.gradle` - Build configuration
- `src/main/java/basics/mastery/` - All modules
- `src/test/java/basics/mastery/` - All tests

## Success Criteria ✅

- [x] Zero compilation errors
- [x] All tests passing
- [x] JDK 17-21 features covered
- [x] Comprehensive documentation
- [x] Production-ready code

---

**Status:** 🎉 READY FOR USE  
**Version:** 2.0  
**Last Updated:** November 3, 2025

**Quick Start:** `java -cp build/classes/java/main basics.mastery.MainApplication`
