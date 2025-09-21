package io.mastery.collections.fundamentals;

import io.mastery.collections.CollectionDemo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Comprehensive demonstration of Map collection implementations.
 * 
 * This class provides interactive examples covering:
 * - HashMap: Fast hash-based key-value mapping
 * - LinkedHashMap: HashMap with insertion/access order preservation
 * - TreeMap: Sorted map implementation using Red-Black tree
 * - ConcurrentHashMap: Thread-safe hash map implementation
 * - EnumMap: Specialized map for enum keys
 * - Properties: String-based map for configuration
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class MapCollectionsDemo extends CollectionDemo {
    
    @Override
    public void demonstrateAll() {
        printHeader("MAP COLLECTIONS COMPREHENSIVE GUIDE");
        
        // Core Map implementations
        demonstrateHashMap();
        demonstrateLinkedHashMap();
        demonstrateTreeMap();
        demonstrateEnumMap();
        demonstrateConcurrentHashMap();
        demonstrateProperties();
        
        // Performance analysis
        performanceComparison();
        
        // Advanced operations
        demonstrateAdvancedOperations();
        demonstrateModernFeatures();
        
        // Best practices
        demonstrateBestPractices();
        
        printSectionComplete("Map Collections");
    }
    
    /**
     * Demonstrates HashMap - the most commonly used Map implementation
     */
    private void demonstrateHashMap() {
        printSubHeader("HashMap - Hash-Based Key-Value Mapping");
        
        printInfo("HashMap provides O(1) average time complexity for basic operations");
        printInfo("Best for: Fast lookups, no ordering requirements");
        
        // Basic operations
        Map<String, Integer> languageRanking = new HashMap<>();
        languageRanking.put("Java", 1);
        languageRanking.put("Python", 2);
        languageRanking.put("JavaScript", 3);
        languageRanking.put("C++", 4);
        languageRanking.put("C#", 5);
        
        printResult("Language Rankings", languageRanking);
        printResult("Java's ranking", languageRanking.get("Java"));
        printResult("Contains 'Python'", languageRanking.containsKey("Python"));
        printResult("Contains ranking 3", languageRanking.containsValue(3));
        
        // Null values and keys
        languageRanking.put("TypeScript", null); // Null value allowed
        languageRanking.put(null, 999); // Null key allowed (only one)
        printResult("With null key/value", languageRanking);
        
        // Safe access methods (Java 8+)
        printResult("Safe get (existing)", languageRanking.getOrDefault("Java", 0));
        printResult("Safe get (non-existing)", languageRanking.getOrDefault("Ruby", 0));
        
        // Conditional operations
        languageRanking.putIfAbsent("Go", 6);
        languageRanking.putIfAbsent("Java", 999); // Won't replace existing
        printResult("After putIfAbsent", languageRanking);
        
        // Remove operations
        languageRanking.remove("C++");
        languageRanking.remove("TypeScript", null); // Remove only if value matches
        printResult("After removals", languageRanking);
        
        printTip("HashMap allows one null key and multiple null values");
        printWarning("Iteration order is not guaranteed in HashMap");
    }
    
    /**
     * Demonstrates LinkedHashMap - HashMap with ordering
     */
    private void demonstrateLinkedHashMap() {
        printSubHeader("LinkedHashMap - HashMap with Insertion/Access Order");
        
        printInfo("LinkedHashMap maintains insertion order or access order");
        printInfo("Best for: When you need Map semantics with predictable ordering");
        
        // Insertion order (default)
        Map<String, String> insertionOrderMap = new LinkedHashMap<>();
        insertionOrderMap.put("First", "1st");
        insertionOrderMap.put("Second", "2nd");
        insertionOrderMap.put("Third", "3rd");
        insertionOrderMap.put("Fourth", "4th");
        
        printResult("Insertion order map", insertionOrderMap);
        
        // Access order LinkedHashMap
        Map<String, String> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);
        accessOrderMap.putAll(insertionOrderMap);
        
        printResult("Before access", accessOrderMap);
        
        // Access some elements (moves them to end)
        accessOrderMap.get("First");
        accessOrderMap.get("Third");
        printResult("After accessing 'First' and 'Third'", accessOrderMap);
        
        // LRU Cache implementation using LinkedHashMap
        printInfo("LRU Cache implementation:");
        Map<String, String> lruCache = new LinkedHashMap<String, String>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3; // Max 3 elements
            }
        };
        
        lruCache.put("A", "Value A");
        lruCache.put("B", "Value B");
        lruCache.put("C", "Value C");
        printResult("LRU Cache (3 elements)", lruCache);
        
        lruCache.put("D", "Value D"); // Should remove A
        printResult("After adding D (A removed)", lruCache);
        
        lruCache.get("B"); // Access B (moves to end)
        lruCache.put("E", "Value E"); // Should remove C
        printResult("After accessing B and adding E", lruCache);
        
        printTip("Use LinkedHashMap for LRU cache implementations");
        printInfo("Access-order mode is perfect for cache scenarios");
    }
    
    /**
     * Demonstrates TreeMap - sorted map implementation
     */
    private void demonstrateTreeMap() {
        printSubHeader("TreeMap - Sorted Map Implementation");
        
        printInfo("TreeMap maintains entries in sorted order of keys using Red-Black tree");
        printInfo("Best for: When you need sorted key-value mapping with O(log n) operations");
        
        // Natural ordering
        Map<Integer, String> numberWords = new TreeMap<>();
        numberWords.put(5, "Five");
        numberWords.put(2, "Two");
        numberWords.put(8, "Eight");
        numberWords.put(1, "One");
        numberWords.put(9, "Nine");
        numberWords.put(3, "Three");
        
        printResult("TreeMap (natural order)", numberWords);
        
        // String keys (alphabetical order)
        Map<String, Integer> wordLengths = new TreeMap<>();
        wordLengths.put("zebra", 5);
        wordLengths.put("apple", 5);
        wordLengths.put("banana", 6);
        wordLengths.put("cherry", 6);
        
        printResult("String TreeMap (alphabetical)", wordLengths);
        
        // Custom comparator (reverse order)
        Map<String, Integer> reverseMap = new TreeMap<>(Collections.reverseOrder());
        reverseMap.putAll(wordLengths);
        printResult("Reverse order TreeMap", reverseMap);
        
        // TreeMap specific methods
        TreeMap<Integer, String> treeMap = new TreeMap<>(numberWords);
        printResult("First entry", treeMap.firstEntry());
        printResult("Last entry", treeMap.lastEntry());
        printResult("Lower key than 5", treeMap.lowerKey(5));
        printResult("Higher key than 5", treeMap.higherKey(5));
        printResult("Entries < 5", treeMap.headMap(5));
        printResult("Entries >= 5", treeMap.tailMap(5));
        printResult("Entries between 2 and 8", treeMap.subMap(2, 9));
        
        // NavigableMap operations
        printResult("Ceiling key >= 4", treeMap.ceilingKey(4));
        printResult("Floor key <= 4", treeMap.floorKey(4));
        printResult("Descending map", treeMap.descendingMap());
        
        printTip("TreeMap keys must be Comparable or provide a Comparator");
        printWarning("TreeMap does not allow null keys (but allows null values)");
    }
    
    /**
     * Demonstrates EnumMap - specialized map for enum keys
     */
    private void demonstrateEnumMap() {
        printSubHeader("EnumMap - Specialized Map for Enum Keys");
        
        // Define enum for demonstration
        enum Priority {
            LOW, MEDIUM, HIGH, CRITICAL
        }
        
        printInfo("EnumMap is highly optimized for enum keys using arrays internally");
        printInfo("Best for: When all keys are enum values, extremely memory efficient");
        
        // Create EnumMap
        Map<Priority, List<String>> tasksByPriority = new EnumMap<>(Priority.class);
        tasksByPriority.put(Priority.HIGH, List.of("Fix security bug", "Deploy hotfix"));
        tasksByPriority.put(Priority.MEDIUM, List.of("Code review", "Update documentation"));
        tasksByPriority.put(Priority.LOW, List.of("Refactor legacy code", "Update dependencies"));
        tasksByPriority.put(Priority.CRITICAL, List.of("Server down", "Data corruption"));
        
        printResult("Tasks by Priority", tasksByPriority);
        
        // Iteration maintains enum declaration order
        printInfo("Tasks in priority order:");
        tasksByPriority.forEach((priority, tasks) -> {
            System.out.println("  " + priority + ": " + tasks);
        });
        
        // Performance characteristics
        printResult("Size", tasksByPriority.size());
        printResult("Contains HIGH priority", tasksByPriority.containsKey(Priority.HIGH));
        printResult("Get CRITICAL tasks", tasksByPriority.get(Priority.CRITICAL));
        
        // All enum values as keys
        Map<Priority, Integer> priorityCount = new EnumMap<>(Priority.class);
        for (Priority p : Priority.values()) {
            priorityCount.put(p, tasksByPriority.getOrDefault(p, List.of()).size());
        }
        printResult("Task count by priority", priorityCount);
        
        printTip("Always use EnumMap when all keys are enum values");
        printInfo("EnumMap is faster and more memory efficient than HashMap for enum keys");
    }
    
    /**
     * Demonstrates ConcurrentHashMap - thread-safe hash map
     */
    private void demonstrateConcurrentHashMap() {
        printSubHeader("ConcurrentHashMap - Thread-Safe Hash Map");
        
        printInfo("ConcurrentHashMap provides thread-safe operations with high concurrency");
        printInfo("Best for: Multi-threaded environments with frequent reads/writes");
        
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("Users", 1000);
        concurrentMap.put("Sessions", 150);
        concurrentMap.put("Requests", 5000);
        
        printResult("Concurrent Map", concurrentMap);
        
        // Atomic operations
        concurrentMap.compute("Users", (key, val) -> val != null ? val + 50 : 50);
        printResult("After computing Users + 50", concurrentMap.get("Users"));
        
        concurrentMap.computeIfAbsent("NewMetric", key -> 42);
        printResult("After computeIfAbsent", concurrentMap);
        
        concurrentMap.computeIfPresent("Sessions", (key, val) -> val * 2);
        printResult("After doubling Sessions", concurrentMap.get("Sessions"));
        
        // Merge operation
        concurrentMap.merge("Requests", 1000, Integer::sum);
        printResult("After merging Requests + 1000", concurrentMap.get("Requests"));
        
        // Atomic replace operations
        boolean replaced = concurrentMap.replace("Users", 1050, 1100);
        printResult("Replace 1050 with 1100", replaced + " (Users: " + concurrentMap.get("Users") + ")");
        
        // Bulk operations (parallel) - cast to ConcurrentHashMap for parallel operations
        printInfo("Parallel operations:");
        ((ConcurrentHashMap<String, Integer>) concurrentMap).forEach(1, (key, value) -> 
            System.out.println("  " + key + " -> " + value));
        
        Integer totalRequests = ((ConcurrentHashMap<String, Integer>) concurrentMap).reduceValues(1, 
            Integer.class::cast, 
            Integer::sum);
        printResult("Sum of all values", totalRequests);
        
        printTip("ConcurrentHashMap is preferred over synchronized wrappers");
        printInfo("No external synchronization needed for compound operations");
    }
    
    /**
     * Demonstrates Properties - specialized map for configuration
     */
    private void demonstrateProperties() {
        printSubHeader("Properties - String-Based Configuration Map");
        
        printInfo("Properties extends Hashtable<Object,Object> but typically used for String pairs");
        printInfo("Best for: Configuration files, system properties");
        
        Properties props = new Properties();
        props.setProperty("app.name", "Java Collections Tutorial");
        props.setProperty("app.version", "2.0");
        props.setProperty("app.author", "Java Tutorial Team");
        props.setProperty("server.port", "8080");
        props.setProperty("database.url", "jdbc:h2:mem:testdb");
        
        printResult("Application Properties", props);
        
        // Get with default values
        printResult("Server port", props.getProperty("server.port"));
        printResult("Debug mode", props.getProperty("debug.enabled", "false"));
        
        // System properties example
        printInfo("Some system properties:");
        printResult("Java version", System.getProperty("java.version"));
        printResult("OS name", System.getProperty("os.name"));
        printResult("User home", System.getProperty("user.home"));
        
        // Property names as enumeration
        printInfo("All property names:");
        props.propertyNames().asIterator().forEachRemaining(name -> 
            System.out.println("  " + name + " = " + props.getProperty(name.toString())));
        
        printTip("Use Properties for configuration that might be externalized");
        printWarning("Properties is synchronized (legacy) - consider using regular Map for new code");
    }
    
    /**
     * Performance comparison between Map implementations
     */
    private void performanceComparison() {
        printSubHeader("Performance Comparison Analysis");
        
        final int elements = 10000;
        printInfo("Comparing Map implementations with " + formatNumber(elements) + " elements");
        
        // Prepare test data
        List<String> keys = IntStream.range(0, elements)
                .mapToObj(i -> "key" + i)
                .toList();
        List<Integer> values = IntStream.range(0, elements)
                .boxed()
                .toList();
        
        // HashMap performance
        double hashMapTime = measureTime(() -> {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < elements; i++) {
                map.put(keys.get(i), values.get(i));
            }
        });
        
        // LinkedHashMap performance
        double linkedHashMapTime = measureTime(() -> {
            Map<String, Integer> map = new LinkedHashMap<>();
            for (int i = 0; i < elements; i++) {
                map.put(keys.get(i), values.get(i));
            }
        });
        
        // TreeMap performance
        double treeMapTime = measureTime(() -> {
            Map<String, Integer> map = new TreeMap<>();
            for (int i = 0; i < elements; i++) {
                map.put(keys.get(i), values.get(i));
            }
        });
        
        // ConcurrentHashMap performance
        double concurrentMapTime = measureTime(() -> {
            Map<String, Integer> map = new ConcurrentHashMap<>();
            for (int i = 0; i < elements; i++) {
                map.put(keys.get(i), values.get(i));
            }
        });
        
        printBenchmark("HashMap insertion", hashMapTime);
        printBenchmark("LinkedHashMap insertion", linkedHashMapTime);
        printBenchmark("TreeMap insertion", treeMapTime);
        printBenchmark("ConcurrentHashMap insertion", concurrentMapTime);
        
        // Lookup performance comparison
        Map<String, Integer> hashMap = new HashMap<>();
        Map<String, Integer> treeMap = new TreeMap<>();
        for (int i = 0; i < elements; i++) {
            hashMap.put(keys.get(i), values.get(i));
            treeMap.put(keys.get(i), values.get(i));
        }
        
        final int lookups = 1000;
        double hashLookupTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < lookups; i++) {
                hashMap.get(keys.get(rnd.nextInt(elements)));
            }
        });
        
        double treeLookupTime = measureTime(() -> {
            Random rnd = new Random(42);
            for (int i = 0; i < lookups; i++) {
                treeMap.get(keys.get(rnd.nextInt(elements)));
            }
        });
        
        printComparison("Lookup operations (" + lookups + " ops)",
                       treeLookupTime, hashLookupTime,
                       "TreeMap", "HashMap");
    }
    
    /**
     * Demonstrates advanced Map operations
     */
    private void demonstrateAdvancedOperations() {
        printSubHeader("Advanced Map Operations");
        
        Map<String, List<String>> employeesByDepartment = new HashMap<>();
        employeesByDepartment.put("Engineering", new ArrayList<>(List.of("Alice", "Bob")));
        employeesByDepartment.put("Marketing", new ArrayList<>(List.of("Charlie")));
        employeesByDepartment.put("Sales", new ArrayList<>(List.of("David", "Eve")));
        
        printResult("Initial departments", employeesByDepartment);
        
        // computeIfAbsent for creating default values
        employeesByDepartment.computeIfAbsent("HR", k -> new ArrayList<>()).add("Frank");
        printResult("After adding Frank to HR", employeesByDepartment);
        
        // merge for combining values
        Map<String, Integer> departmentSizes = new HashMap<>();
        employeesByDepartment.forEach((dept, employees) -> 
            departmentSizes.merge(dept, employees.size(), Integer::sum));
        printResult("Department sizes", departmentSizes);
        
        // replaceAll for transforming all values
        Map<String, String> uppercaseDepts = new HashMap<>();
        employeesByDepartment.keySet().forEach(dept -> 
            uppercaseDepts.put(dept, dept.toUpperCase()));
        printResult("Uppercase departments", uppercaseDepts);
        
        // Filtering and transforming with streams
        Map<String, Integer> largeDepartments = employeesByDepartment.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().size()
                ));
        printResult("Large departments (>1 employee)", largeDepartments);
        
        printTip("Use computeIfAbsent for lazy initialization of map values");
        printInfo("Stream operations provide powerful map transformation capabilities");
    }
    
    /**
     * Demonstrates modern Java features with Maps
     */
    private void demonstrateModernFeatures() {
        printSubHeader("Modern Java Features with Maps");
        
        // Records as map values
        record Employee(String name, String department, int salary) {}
        
        Map<Integer, Employee> employees = Map.of(
            1, new Employee("Alice Johnson", "Engineering", 95000),
            2, new Employee("Bob Smith", "Engineering", 87000),
            3, new Employee("Charlie Brown", "Marketing", 72000),
            4, new Employee("Diana Prince", "Sales", 78000)
        );
        
        printResult("Employee Map", employees);
        
        // Grouping by department using Collectors.groupingBy
        Map<String, List<Employee>> byDepartment = employees.values().stream()
                .collect(Collectors.groupingBy(Employee::department));
        printResult("Grouped by department", byDepartment);
        
        // Average salary by department
        Map<String, Double> avgSalaryByDept = employees.values().stream()
                .collect(Collectors.groupingBy(
                    Employee::department,
                    Collectors.averagingInt(Employee::salary)
                ));
        printResult("Average salary by department", avgSalaryByDept);
        
        // Partitioning (boolean-based grouping)
        Map<Boolean, List<Employee>> salaryPartition = employees.values().stream()
                .collect(Collectors.partitioningBy(emp -> emp.salary() > 80000));
        printResult("High earners (>80k)", salaryPartition.get(true));
        printResult("Regular earners (<=80k)", salaryPartition.get(false));
        
        // Using var with type inference (Java 10+)
        var topEarners = employees.values().stream()
                .filter(emp -> emp.salary() > 85000)
                .collect(Collectors.toMap(
                    Employee::name,
                    Employee::salary
                ));
        printResult("Top earners (using var)", topEarners);
        
        // Text blocks for complex keys (Java 15+)
        Map<String, String> queries = Map.of(
            "simple", "SELECT * FROM users",
            "complex", """
                SELECT u.name, u.email, d.name as department
                FROM users u
                JOIN departments d ON u.dept_id = d.id
                WHERE u.active = true
                ORDER BY u.name
                """
        );
        printResult("SQL Queries Map", queries.keySet());
        
        printTip("Use Records with Maps for clean, immutable data structures");
        printInfo("Collectors.groupingBy and partitioningBy are powerful for data analysis");
    }
    
    /**
     * Demonstrates best practices and common patterns
     */
    private void demonstrateBestPractices() {
        printSubHeader("Best Practices and Common Patterns");
        
        printInfo("🗺️ Map Selection Guidelines:");
        System.out.println("  • HashMap: Default choice for most use cases");
        System.out.println("  • LinkedHashMap: When insertion/access order matters");
        System.out.println("  • TreeMap: When sorted keys are required");
        System.out.println("  • EnumMap: Always use for enum keys");
        System.out.println("  • ConcurrentHashMap: Thread-safe scenarios");
        
        printSeparator();
        
        printInfo("⚡ Performance Optimization Tips:");
        System.out.println("  • Pre-size HashMap if you know approximate capacity");
        System.out.println("  • Use EnumMap for enum keys (array-based, very fast)");
        System.out.println("  • Consider TreeMap only when sorting is essential");
        System.out.println("  • Use Map.of() for small immutable maps");
        System.out.println("  • Use computeIfAbsent for lazy value initialization");
        
        printSeparator();
        
        printInfo("🚨 Common Pitfalls to Avoid:");
        System.out.println("  • Mutable objects as keys without proper equals/hashCode");
        System.out.println("  • Modifying key objects after insertion");
        System.out.println("  • Using TreeMap with non-comparable keys without Comparator");
        System.out.println("  • Forgetting that Map.of() creates immutable maps");
        System.out.println("  • Using synchronized wrappers instead of ConcurrentHashMap");
        
        // Demonstrate safe key practices
        printInfo("Safe key practices demonstration:");
        
        // Bad: mutable key
        class MutableKey {
            private String value;
            MutableKey(String value) { this.value = value; }
            void setValue(String value) { this.value = value; }
            
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof MutableKey other)) return false;
                return Objects.equals(value, other.value);
            }
            
            @Override
            public int hashCode() { return Objects.hash(value); }
            
            @Override
            public String toString() { return value; }
        }
        
        Map<MutableKey, String> dangerousMap = new HashMap<>();
        MutableKey key = new MutableKey("original");
        dangerousMap.put(key, "some value");
        
        printResult("Before key mutation", dangerousMap.containsKey(key));
        key.setValue("modified"); // Mutating the key!
        printResult("After key mutation", dangerousMap.containsKey(key)); // May be false!
        
        // Good: immutable key (using record)
        record ImmutableKey(String value) {}
        
        Map<ImmutableKey, String> safeMap = new HashMap<>();
        safeMap.put(new ImmutableKey("safe"), "value");
        printResult("Safe immutable key lookup", safeMap.containsKey(new ImmutableKey("safe")));
        
        // Modern patterns
        printInfo("Modern usage patterns:");
        
        // Factory methods for map creation
        var configMap = Map.of(
            "timeout", "30000",
            "retries", "3",
            "debug", "false"
        );
        
        // Defensive copying for mutable maps (demonstration purpose)
        @SuppressWarnings("unused")
        Map<String, String> publicConfig = new HashMap<>(configMap);
        
        // Using streams for map operations
        Map<String, Integer> configAsInts = configMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("debug"))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> Integer.parseInt(entry.getValue())
                ));
        
        printResult("Config as integers", configAsInts);
        
        printTip("Use immutable keys (Records, Strings, primitives) to avoid issues");
        printWarning("Always consider thread safety requirements when choosing Map implementations");
    }
}