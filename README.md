# 🚀 Java Concurrency Mastery Training Program

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-Educational-blue)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-green)](README.md)

A comprehensive, hands-on training program designed to master Java concurrency concepts from fundamental threading to modern Java 21+ features.

## 📚 Overview

This interactive training program provides a structured learning path through Java's concurrency landscape, featuring real-world examples, performance comparisons, and best practices. Perfect for developers looking to build robust, scalable applications.

## ✨ Features

### 🎯 **Interactive Learning Experience**
- Menu-driven interface for easy navigation
- Step-by-step progression through complexity levels
- Real-time demonstrations with detailed explanations
- Practical examples with performance metrics

### 📖 **Comprehensive Coverage**
- **Thread Fundamentals**: Creation, lifecycle, and basic coordination
- **Synchronization Primitives**: 8+ different locking mechanisms
- **Memory Model**: Visibility, ordering, and happens-before relationships
- **Modern Concurrency**: Virtual threads, structured concurrency (Java 21+)
- **Performance Analysis**: Benchmarking and optimization techniques
- **Real-World Patterns**: Microservice gateway implementations

### 🔬 **Hands-On Labs**
- Parallel file processing performance comparison
- Counter service benchmarking across different implementations
- Concurrency pitfall demonstrations and solutions
- Microservice coordination patterns

## 🎓 Learning Objectives

By completing this training, you will:

- ✅ Understand fundamental threading concepts and best practices
- ✅ Master various synchronization mechanisms and when to use each
- ✅ Recognize and avoid common concurrency pitfalls
- ✅ Leverage modern Java concurrency features effectively
- ✅ Design thread-safe, high-performance concurrent applications
- ✅ Debug and troubleshoot concurrency issues with confidence

## 🏗️ Project Structure

```
src/main/java/io/mastery/
├── 📱 MainApplication.java                    # Interactive training interface
├── 🧠 fundamentals/lab/
│   ├── ParallelFileProcessor.java            # Performance comparison lab
│   └── SynchronizationPrimitivesDemo.java    # 8 synchronization mechanisms
├── 🚀 modern/
│   └── ModernConcurrencyDemo.java            # Java 21+ virtual threads
├── 🔒 synchronization/
│   └── SynchronizationPrimitivesDemo.java    # Advanced coordination
├── 📊 CounterServiceBenchmark.java           # Performance benchmarking
├── ⚠️  ConcurrencyPitfallsDemo.java           # Common mistakes & solutions
├── 🧪 JavaMemoryModelDemo.java               # Memory model deep dive
└── 🌐 MicroserviceGatewayLab.java            # Real-world patterns
```

## 🚀 Quick Start

### Prerequisites
- Java 17+ (Java 21+ recommended for full feature support)
- Gradle (included via wrapper)

### 1. Clone and Build
```bash
# Navigate to project directory
cd /Volumes/Work/JavaBasics/javasample1

# Build the project
./gradlew build
```

### 2. Run the Training Program
```bash
# Start the interactive training
java -cp build/classes/java/main io.mastery.MainApplication
```

### 3. Follow the Learning Path
```
📚 Recommended Learning Sequence:
1️⃣  Thread Fundamentals
2️⃣  Synchronization Primitives  
3️⃣  Parallel File Processing Lab
4️⃣  Modern Concurrency (Java 21) ⭐
5️⃣  Microservice Gateway Lab
6️⃣  Java Memory Model Demo
7️⃣  Concurrency Pitfalls Demo
8️⃣  Counter Service Benchmark
```

## 📋 Module Descriptions

### 🔒 **Synchronization Primitives**
Deep dive into 8 different synchronization mechanisms:
- `synchronized` keyword and intrinsic locks
- `ReentrantLock` with timeout and fairness
- `ReadWriteLock` for read-heavy workloads
- `StampedLock` with optimistic reading
- `CountDownLatch` for service coordination
- `CyclicBarrier` for multi-phase computation
- `Semaphore` for resource pool management
- `Phaser` for dynamic participant coordination

### 🚀 **Modern Concurrency (Java 21+)**
Explore cutting-edge concurrency features:
- **Virtual Threads**: Lightweight, efficient threading
- **Structured Concurrency**: Coordinated task lifecycle
- **Scoped Values**: Context propagation without ThreadLocal
- **Performance Comparisons**: Virtual vs Platform threads

### 📁 **Parallel File Processing Lab**
Performance comparison across different models:
- Sequential processing baseline
- Traditional thread pool implementation  
- Virtual thread implementation
- Memory usage and throughput analysis

### ⚡ **Microservice Gateway Lab**
Real-world concurrency patterns:
- Traditional blocking gateway
- Virtual thread-based gateway
- Structured concurrency coordination
- Error handling and timeout management

## 🎯 Best Practices Demonstrated

- **Thread Safety**: Proper synchronization without over-synchronization
- **Performance**: Choosing the right tool for specific scenarios
- **Resource Management**: Preventing leaks and ensuring cleanup
- **Error Handling**: Graceful failure and recovery patterns
- **Testing**: Verification techniques for concurrent code

## 🔧 System Requirements

- **Java Version**: 17+ (21+ for full virtual thread support)
- **Memory**: 2GB+ RAM recommended
- **CPU**: Multi-core processor for meaningful concurrency demonstrations
- **OS**: Any Java-supported operating system

## 📈 Performance Insights

The training includes actual performance measurements:
- **Throughput**: Operations per second across different implementations
- **Latency**: Response time analysis under various loads
- **Memory Usage**: Heap and native memory consumption patterns
- **Scalability**: Behavior under increasing thread counts

## 🤝 Contributing

This is an educational project. If you find issues or have suggestions:
1. Document the specific scenario
2. Include Java version and system details
3. Provide reproduction steps

## 📄 License

This project is created for educational purposes. Feel free to use for learning and teaching Java concurrency concepts.

## 🎓 Next Steps

After completing this training:
1. **Practice**: Implement these patterns in your own projects
2. **Explore**: Dive deeper into specific areas of interest
3. **Share**: Teach others what you've learned
4. **Contribute**: Help improve the Java concurrency ecosystem

---

**Happy Learning! 🎉**

> *"Concurrency is not a feature you add to a program. Concurrency is a way of thinking about the problem."*

For detailed setup instructions and module-specific guidance, see [INSTRUCTIONS.md](INSTRUCTIONS.md).

## 🧭 Beginner Path (Quick Start)

Start here if you're new to threads and concurrency:

1. Thread Basics Demo — creation, start vs run, join, interrupt
	- Class: `io.mastery.fundamentals.ThreadBasicsDemo`
2. Synchronization Primitives — core coordination tools
	- Class: `io.mastery.synchronization.SynchronizationPrimitivesDemo`
3. Java Memory Model — visibility, volatile, atomics
	- Class: `io.mastery.JavaMemoryModelDemo`
4. Concurrency Pitfalls — deadlock, livelock, race, starvation
	- Class: `io.mastery.ConcurrencyPitfallsDemo`

Tip: You can also launch these from `MainApplication` menu options 1, 2, 6, 7, or the supplemental options 9–15.

## 📚 Supplemental Demos Index

Targeted, small demos to explore specific topics (menu options 9–15):

- 9 — `io.mastery.fundamentals.ThreadBasicsDemo`: Thread creation, lifecycle, join/interrupt, CPU vs IO
- 10 — `io.mastery.modern.VirtualThreadPinningDemo`: Virtual thread pinning caveats and safe patterns
- 11 — `io.mastery.collections.patterns.FailSafeIterationDemo`: Fail-fast vs fail-safe iteration; CopyOnWrite trade-offs
- 12 — `io.mastery.collections.fundamentals.HashingPitfallsDemo`: Mutable keys, equals/hashCode, comparator consistency
- 13 — `io.mastery.generics.advanced.GenericsPecosDemo`: PECS, invariance, and higher-order generics
- 14 — `io.mastery.advanced.LockFreeCounterDemo`: CAS loop vs synchronized vs AtomicInteger
- 15 — `io.mastery.tools.ThreadDiagnosticsQuickstart`: Thread dump and deadlock detection

## ▶️ Try it (commands)

Run any demo directly after building:

```bash
# Thread basics
java -cp build/classes/java/main io.mastery.fundamentals.ThreadBasicsDemo

# Virtual thread pinning caveats
java -cp build/classes/java/main io.mastery.modern.VirtualThreadPinningDemo

# Collections: fail-safe iteration
java -cp build/classes/java/main io.mastery.collections.patterns.FailSafeIterationDemo

# Collections: hashing & comparator pitfalls
java -cp build/classes/java/main io.mastery.collections.fundamentals.HashingPitfallsDemo

# Generics: PECS & variance
java -cp build/classes/java/main io.mastery.generics.advanced.GenericsPecosDemo

# Lock-free counter (CAS)
java -cp build/classes/java/main io.mastery.advanced.LockFreeCounterDemo

# Thread diagnostics quickstart
java -cp build/classes/java/main io.mastery.tools.ThreadDiagnosticsQuickstart
```