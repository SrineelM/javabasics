package basics.mastery.java.collections.fundamentals;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.SequencedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import basics.mastery.collections.CollectionDemo;

/**
 * ============================================================================
 * JAVA COACH TUTORIAL: MAP COLLECTIONS MASTERCLASS (JDK 17 - 21)
 * ============================================================================
 * 
 * Target Audience: Beginner -> Intermediate -> Senior Java Engineers
 * Objectives:
 *   1. Deep understanding of Map interfaces, internal mechanics, and time complexities.
 *   2. Master modern JDK 17-21 idioms (SequencedMap, Records, Collectors.teeing).
 *   3. Thorough preparation for Technical Interviews & Architectural Reviews.
 * 
 * Map Hierarchy at a glance:
 * 
 *                     java.util.Map<K, V>
 *                       /             \
 *       java.util.SequencedMap<K, V>   HashMap<K, V> ── EnumMap<K, V>
 *             (JDK 21+)                /          \
 *            /         \      LinkedHashMap<K, V>  ConcurrentHashMap<K, V>
 *      SortedMap<K, V>  \             │
 *            │     SequencedMap       │
 *     NavigableMap<K, V>              │
 *            │                        │
 *      TreeMap<K, V> ─────────────────┘
 * 
 * @author Srineel with Copilot & Expert Java Coach
 * @version 3.0
 * @since Java 17 (Enhanced for Java 21)
 */
public class MapCollectionsDemo extends CollectionDemo {

    public static void main(String[] args) {
        new MapCollectionsDemo().demonstrateAll();
    }

    @Override
    public void demonstrateAll() {
        printHeader("MAP COLLECTIONS: BEGINNER TO ADVANCED MASTERCLASS (JDK 17-21)");

        // 1. Core Map Implementations & Internal Mechanics
        demonstrateHashMap();
        demonstrateLinkedHashMap();
        demonstrateTreeMap();
        demonstrateEnumMap();
        demonstrateConcurrentHashMapBasics();
        demonstrateProperties();

        // 2. Modern Java 17-21 Features (SequencedMap, Records, Pattern Matching)
        demonstrateSequencedMapJava21();
        demonstrateModernFeatures();

        // 3. Functional Operations & Stream Transformations
        demonstrateAdvancedOperations();

        // 4. Performance Benchmarks
        performanceComparison();

        // 5. Best Practices, Anti-patterns, and Top Interview Questions
        demonstrateBestPracticesAndInterviewQuestions();

        printSectionComplete("Map Collections Masterclass");
    }

    // =========================================================================
    // 1. HASHMAP: ARCHITECTURE, RESIZING, AND HASH COLLISIONS
    // =========================================================================
    /**
     * Demonstrates {@link HashMap} - the workhorse of Java key-value structures.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Under the hood: Array of Node<K,V>[] table (default initial capacity = 16).
     * - Bucket Index Calculation: index = (n - 1) & hash(key)
     *   * Note: Bitwise AND with (n - 1) is a blazing-fast equivalent to modulo (hash % n),
     *     which is why HashMap capacity MUST always be a power of 2!
     * - Collision Resolution: Chaining with Singly Linked List.
     * - Treeification (Java 8+):
     *   * When a single bucket reaches TREEIFY_THRESHOLD (8 entries) AND total capacity >= MIN_TREEIFY_CAPACITY (64),
     *     the bucket transforms into a Red-Black Tree (TreeNode<K,V>).
     *   * This guarantees worst-case lookup improves from O(n) to O(log n), guarding against Hash DoS attacks.
     *   * If bucket size drops to UNTREEIFY_THRESHOLD (6), it converts back to a linked list.
     * - Load Factor: Default is 0.75f. Resizing doubles the table (n * 2) when size > capacity * loadFactor.
     */
    private void demonstrateHashMap() {
        printSubHeader("1. HashMap - Mechanics, Compute API & Collision Management");

        printInfo("HashMap provides average O(1) time complexity for get() and put().");
        printInfo("It is NOT thread-safe, permits ONE null key, and MULTIPLE null values.");

        Map<String, Integer> salaryMap = new HashMap<>();

        // [COACH: BEGINNER] Basic insertion and retrieval
        salaryMap.put("Alice", 95000);
        salaryMap.put("Bob", 82000);
        salaryMap.put("Charlie", 74000);
        salaryMap.put("Diana", 110000);

        printResult("Initial Map", salaryMap);
        printResult("Bob's Salary", salaryMap.get("Bob"));
        printResult("Contains key 'Alice'", salaryMap.containsKey("Alice"));
        printResult("Contains value 110000", salaryMap.containsValue(110000));

        // [COACH: NULL HANDLING]
        salaryMap.put(null, 50000); // Stored always at bucket index 0 because hash(null) == 0
        salaryMap.put("Intern", null); // Null values are legal
        printResult("Map with null key and null value", salaryMap);

        // [COACH: INTERMEDIATE] Safe access and avoidance of NullPointerException
        printResult("getOrDefault('Eve', 0)", salaryMap.getOrDefault("Eve", 0));

        // [COACH: JAVA 8+ COMPUTE API]
        // computeIfAbsent: Ideal for cache lookups or initializing collection values
        salaryMap.computeIfAbsent("Eve", k -> 88000);
        printResult("After computeIfAbsent('Eve')", salaryMap.get("Eve"));

        // computeIfPresent: Safely update existing records
        salaryMap.computeIfPresent("Bob", (k, v) -> v + 5000); // 5k raise
        printResult("After computeIfPresent for Bob (raise)", salaryMap.get("Bob"));

        // merge: Combines existing and new values. Extremely useful for frequency counting!
        salaryMap.merge("Charlie", 6000, Integer::sum);
        printResult("After merge (Charlie bonus)", salaryMap.get("Charlie"));

        // Conditional remove & replace
        boolean removed = salaryMap.remove("Alice", 90000); // Won't remove (value mismatch)
        printResult("Conditional remove (Alice with wrong salary)", removed);
        salaryMap.remove("Alice", 95000); // Will remove
        printResult("Map after correct conditional remove", salaryMap);

        printTip("[INTERVIEW TIP] Pre-sizing HashMap avoids expensive rehashing: initialCapacity = (int) (expectedEntries / 0.75f) + 1");
        printWarning("[PITFALL] HashMap iteration order is non-deterministic and can change across JVM executions or rehashes!");
    }

    // =========================================================================
    // 2. LINKEDHASHMAP: INSERTION ORDER, ACCESS ORDER & LRU CACHES
    // =========================================================================
    /**
     * Demonstrates {@link LinkedHashMap}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Inherits from HashMap. Extends HashMap.Node with `before` and `after` pointers.
     * - Maintains a doubly-linked list through all its entries.
     * - Two ordering modes:
     *   1. Insertion-order (default): entries appear in the exact sequence added.
     *   2. Access-order (`accessOrder = true` in constructor): get() or put() moves accessed entry to the tail!
     * - Perfect foundation for bounded In-Memory Least Recently Used (LRU) Caches!
     */
    private void demonstrateLinkedHashMap() {
        printSubHeader("2. LinkedHashMap - Insertion-Order, Access-Order & LRU Cache");

        printInfo("LinkedHashMap maintains a doubly-linked list running through all entries.");

        // Mode A: Insertion-Order (default)
        Map<String, String> insertOrderMap = new LinkedHashMap<>();
        insertOrderMap.put("Step 1", "Parse Input");
        insertOrderMap.put("Step 2", "Validate Schema");
        insertOrderMap.put("Step 3", "Execute Business Logic");
        insertOrderMap.put("Step 4", "Commit Transaction");
        printResult("Insertion-order iteration", insertOrderMap.keySet());

        // Mode B: Access-Order (LRU Mode)
        // Constructor: initialCapacity, loadFactor, accessOrder (true)
        Map<String, String> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);
        accessOrderMap.put("ID_101", "User A");
        accessOrderMap.put("ID_102", "User B");
        accessOrderMap.put("ID_103", "User C");

        printResult("Access-order initial", accessOrderMap.keySet());
        // Access ID_101: moves ID_101 to the tail of the linked list
        accessOrderMap.get("ID_101");
        printResult("After accessing ID_101 (moved to end)", accessOrderMap.keySet());

        // Mode C: Production-grade bounded LRU Cache using removeEldestEntry()
        printInfo("Building an LRU Cache (Capacity = 3) using removeEldestEntry:");
        class LruCache<K, V> extends LinkedHashMap<K, V> {
            private final int maxCapacity;

            public LruCache(int capacity) {
                super(capacity, 0.75f, true);
                this.maxCapacity = capacity;
            }

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                // [COACH: ADVANCED] Called by put() and putAll() after inserting a new entry.
                // When size exceeds maxCapacity, the eldest (least recently used) is evicted!
                return size() > maxCapacity;
            }
        }

        LruCache<String, String> sessionCache = new LruCache<>(3);
        sessionCache.put("Session_1", "Token_A");
        sessionCache.put("Session_2", "Token_B");
        sessionCache.put("Session_3", "Token_C");
        printResult("LRU Cache at capacity (3 items)", sessionCache.keySet());

        // Access Session_1 -> makes Session_2 the oldest
        sessionCache.get("Session_1");
        printResult("Accessed Session_1 (Session_2 is now eldest)", sessionCache.keySet());

        // Adding Session_4 triggers eviction of Session_2
        sessionCache.put("Session_4", "Token_D");
        printResult("After adding Session_4 (Session_2 evicted)", sessionCache.keySet());

        printTip("[INTERVIEW TIP] For concurrent LRU caches, LinkedHashMap is NOT thread-safe. Use Caffeine or wrap in Collections.synchronizedMap.");
    }

    // =========================================================================
    // 3. TREEMAP: RED-BLACK TREES & NAVIGABLEMAP
    // =========================================================================
    /**
     * Demonstrates {@link TreeMap} and {@link NavigableMap}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Implemented via self-balancing Red-Black Tree.
     * - Guarantees O(log n) time cost for containsKey, get, put, and remove.
     * - Keys MUST implement {@link Comparable} or a custom {@link java.util.Comparator} must be supplied.
     * - Throws {@link NullPointerException} if null key is inserted (under natural ordering).
     */
    private void demonstrateTreeMap() {
        printSubHeader("3. TreeMap - Red-Black Tree, Range Queries & NavigableMap");

        printInfo("TreeMap keeps keys strictly sorted. Perfect for range queries, ranking, and intervals.");

        NavigableMap<Integer, String> leaderBoard = new TreeMap<>();
        leaderBoard.put(100, "Novice");
        leaderBoard.put(500, "Adept");
        leaderBoard.put(1200, "Expert");
        leaderBoard.put(2500, "Master");
        leaderBoard.put(5000, "Grandmaster");

        printResult("TreeMap natural key order", leaderBoard);
        printResult("Lowest Rank (firstEntry)", leaderBoard.firstEntry());
        printResult("Highest Rank (lastEntry)", leaderBoard.lastEntry());

        // Navigable query operations
        printResult("lowerKey(1200) [< 1200]", leaderBoard.lowerKey(1200));   // 500
        printResult("floorKey(1200) [<= 1200]", leaderBoard.floorKey(1200));   // 1200
        printResult("ceilingKey(1500) [>= 1500]", leaderBoard.ceilingKey(1500)); // 2500
        printResult("higherKey(2500) [> 2500]", leaderBoard.higherKey(2500));  // 5000

        // Range Views (subMap, headMap, tailMap)
        printResult("Scores between 500 and 2500 (inclusive)", leaderBoard.subMap(500, true, 2500, true));
        printResult("Scores below 1200 (headMap)", leaderBoard.headMap(1200, false));
        printResult("Scores 1200 and above (tailMap)", leaderBoard.tailMap(1200, true));

        // Descending view
        printResult("Reverse Order View (descendingMap)", leaderBoard.descendingMap());

        printWarning("[PITFALL] TreeMap uses compareTo() or compare() for equality, NOT equals()! If compare(a,b) == 0, keys are considered identical.");
    }

    // =========================================================================
    // 4. ENUMMAP: ULTRA-FAST BIT/ARRAY-BACKED MAPPING
    // =========================================================================
    /**
     * Demonstrates {@link EnumMap}.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Backed internally by a simple, flat Java array: Object[] vals, sized exactly to Enum.values().length.
     * - Bucket lookup is a zero-collision array index: vals[key.ordinal()].
     * - Cache-line friendly, minimal heap footprint, and strictly outperforms HashMap in every metric.
     */
    private void demonstrateEnumMap() {
        printSubHeader("4. EnumMap - Array-Backed O(1) Zero-Collision Mapping");

        enum ServerEnvironment { DEV, QA, STAGING, PROD, DR }

        printInfo("EnumMap uses key.ordinal() as direct array index. No hashing overhead, zero collisions.");

        Map<ServerEnvironment, String> hostMap = new EnumMap<>(ServerEnvironment.class);
        hostMap.put(ServerEnvironment.DEV, "dev.internal.net");
        hostMap.put(ServerEnvironment.STAGING, "stage.internal.net");
        hostMap.put(ServerEnvironment.PROD, "prod.global.corp");

        printResult("EnumMap entries", hostMap);
        printResult("Prod host", hostMap.get(ServerEnvironment.PROD));

        // Iteration order always matches enum declaration order
        printInfo("EnumMap iterates strictly in enum declaration order (DEV -> QA -> STAGING -> PROD -> DR):");
        hostMap.forEach((env, host) -> System.out.println("     [" + env + "] => " + host));

        printTip("[COACH BEST PRACTICE] If keys are an Enum, NEVER use HashMap. Always use EnumMap!");
    }

    // =========================================================================
    // 5. CONCURRENT HASH MAP: THREAD-SAFE CONCURRENCY BASICS
    // =========================================================================
    /**
     * Demonstrates fundamentals of {@link ConcurrentHashMap}.
     */
    private void demonstrateConcurrentHashMapBasics() {
        printSubHeader("5. ConcurrentHashMap - Lock-Free Reads & Bin-Level Locking");

        printInfo("Java 8+ ConcurrentHashMap replaced Segment locks with CAS on empty bins + synchronized on bin head.");
        printInfo("It NEVER locks during reads (get() is completely lock-free).");

        Map<String, Integer> pageHitCounter = new ConcurrentHashMap<>();
        pageHitCounter.put("/home", 100);
        pageHitCounter.put("/login", 45);

        // Atomic compound operations:
        pageHitCounter.compute("/home", (url, count) -> count == null ? 1 : count + 1);
        pageHitCounter.merge("/dashboard", 1, Integer::sum);

        printResult("ConcurrentHashMap after atomic updates", pageHitCounter);
        printWarning("[PITFALL] ConcurrentHashMap forbids null keys AND null values! Attempting to put null throws NullPointerException.");
    }

    // =========================================================================
    // 6. PROPERTIES: CONFIGURATION & LEGACY SYSTEM PROPERTIES
    // =========================================================================
    /**
     * Demonstrates {@link Properties}.
     */
    private void demonstrateProperties() {
        printSubHeader("6. Properties - Key-Value Configuration Store");

        printInfo("Properties extends Hashtable<Object,Object>, conventionally restricted to String keys and values.");

        Properties appConfig = new Properties();
        appConfig.setProperty("database.pool.size", "20");
        appConfig.setProperty("app.timeout.ms", "5000");
        appConfig.setProperty("app.env", "PRODUCTION");

        printResult("Property 'app.timeout.ms'", appConfig.getProperty("app.timeout.ms"));
        printResult("Property with default fallback", appConfig.getProperty("app.retry.count", "3"));
        printResult("System Property 'java.version'", System.getProperty("java.version"));
    }

    // =========================================================================
    // 7. JDK 21 SEQUENCED MAP (JEP 431)
    // =========================================================================
    /**
     * Demonstrates {@link SequencedMap} introduced in Java 21 via JEP 431.
     * 
     * [COACH: JDK 21 SPOTLIGHT]
     * Prior to Java 21, getting the first or last entry of an ordered map was inconsistent:
     * - LinkedHashMap: required map.entrySet().iterator().next() (clunky and O(1) only for first)
     * - TreeMap: treeMap.firstEntry(), treeMap.lastEntry()
     * Java 21 introduces SequencedMap with uniform APIs across LinkedHashMap and TreeMap:
     * - firstEntry(), lastEntry()
     * - pollFirstEntry(), pollLastEntry()
     * - putFirst(K, V), putLast(K, V)
     * - reversed() -> returns a live, reverse-ordered view!
     */
    private void demonstrateSequencedMapJava21() {
        printSubHeader("7. [JDK 21] SequencedMap (JEP 431) - Unified First/Last & Reversed Views");

        printInfo("SequencedMap provides standardized methods for maps with defined encounter order.");

        // LinkedHashMap implements SequencedMap in JDK 21+
        SequencedMap<String, Integer> releaseTrain = new LinkedHashMap<>();
        releaseTrain.put("Java 8", 2014);
        releaseTrain.put("Java 11", 2018);
        releaseTrain.put("Java 17", 2021);
        releaseTrain.put("Java 21", 2023);

        printResult("Standard Order", releaseTrain);
        printResult("firstEntry()", releaseTrain.firstEntry());
        printResult("lastEntry()", releaseTrain.lastEntry());

        // Prepend and Append directly:
        releaseTrain.putFirst("Java 1.0", 1996);
        releaseTrain.putLast("Java 25", 2025);
        printResult("After putFirst('Java 1.0') and putLast('Java 25')", releaseTrain);

        // Live Reversed View (no data is cloned!)
        SequencedMap<String, Integer> reversedView = releaseTrain.reversed();
        printResult("reversed() live view", reversedView);

        // Poll first / poll last
        Map.Entry<String, Integer> polledFirst = releaseTrain.pollFirstEntry();
        printResult("pollFirstEntry() [removes and returns head]", polledFirst);
        printResult("Map after polling first", releaseTrain);

        printTip("[COACH: JDK 21 TIP] Both LinkedHashMap and TreeMap implement SequencedMap. HashMap does NOT, because it has no encounter order.");
    }

    // =========================================================================
    // 8. MODERN FEATURES: RECORDS, PATTERN MATCHING & ADVANCED COLLECTORS
    // =========================================================================
    /**
     * Demonstrates combining Records (Java 16+), Pattern Matching (Java 17-21),
     * and Collectors.teeing (Java 12+).
     */
    private void demonstrateModernFeatures() {
        printSubHeader("8. Modern Java Features with Maps (Records, Teeing, Pattern Matching)");

        // A. Records as Map Keys and Values
        record Employee(int id, String name, String department, int salary) {}

        Map<Integer, Employee> employees = Map.of(
            101, new Employee(101, "Alice", "Engineering", 120000),
            102, new Employee(102, "Bob", "Engineering", 95000),
            103, new Employee(103, "Charlie", "Marketing", 80000),
            104, new Employee(104, "Diana", "Finance", 110000)
        );

        printResult("Immutable Map.of() with Records", employees.size() + " employees registered.");

        // B. Advanced Grouping and Partitioning with Streams
        Map<String, List<Employee>> byDept = employees.values().stream()
                .collect(Collectors.groupingBy(Employee::department));
        printResult("Grouped by Department", byDept);

        // C. Collectors.teeing (Calculate count AND average salary in a single pass)
        record DeptStats(long count, double averageSalary) {}

        Map<String, DeptStats> departmentStats = employees.values().stream()
                .collect(Collectors.groupingBy(
                    Employee::department,
                    Collectors.teeing(
                        Collectors.counting(),
                        Collectors.averagingInt(Employee::salary),
                        DeptStats::new
                    )
                ));

        printResult("Department Stats via Collectors.teeing", departmentStats);

        // D. Pattern Matching on Map Entries (Java 21 Record Patterns)
        printInfo("Pattern matching on Map values using Record Patterns:");
        for (var entry : employees.entrySet()) {
            if (entry.getValue() instanceof Employee(int id, String name, String dept, int sal) && sal > 100000) {
                System.out.println("     [HIGH EARNER] ID=" + id + ", Name=" + name + " (" + dept + "), Salary=$" + sal);
            }
        }
    }

    // =========================================================================
    // 9. ADVANCED STREAM TRANSFORMATIONS
    // =========================================================================
    /**
     * Demonstrates advanced stream operations on Map entries.
     */
    private void demonstrateAdvancedOperations() {
        printSubHeader("9. Advanced Map Stream Operations (Inversion & Filtering)");

        Map<String, String> capitalToCountry = Map.of(
            "Paris", "France",
            "Berlin", "Germany",
            "Rome", "Italy",
            "Madrid", "Spain"
        );

        // Inverting a Map: Keys become Values, Values become Keys
        Map<String, String> countryToCapital = capitalToCountry.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getValue,
                    Map.Entry::getKey
                ));
        printResult("Original (Capital -> Country)", capitalToCountry);
        printResult("Inverted (Country -> Capital)", countryToCapital);

        // Filtering entries by key length
        Map<String, String> filtered = capitalToCountry.entrySet().stream()
                .filter(e -> e.getKey().length() > 5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        printResult("Capitals with length > 5", filtered);
    }

    // =========================================================================
    // 10. PERFORMANCE COMPARISON BENCHMARKS
    // =========================================================================
    /**
     * Benchmarks insertion and lookup across HashMap, LinkedHashMap, and TreeMap.
     */
    private void performanceComparison() {
        printSubHeader("10. Performance Benchmarking Analysis");

        final int elements = 50_000;
        printInfo("Benchmarking with " + formatNumber(elements) + " operations:");

        List<String> keys = IntStream.range(0, elements).mapToObj(i -> "key_" + i).toList();

        // Insertion: HashMap
        double hashInsertTime = measureTime(() -> {
            Map<String, Integer> map = new HashMap<>(elements);
            for (int i = 0; i < elements; i++) map.put(keys.get(i), i);
        });

        // Insertion: LinkedHashMap
        double linkedInsertTime = measureTime(() -> {
            Map<String, Integer> map = new LinkedHashMap<>(elements);
            for (int i = 0; i < elements; i++) map.put(keys.get(i), i);
        });

        // Insertion: TreeMap
        double treeInsertTime = measureTime(() -> {
            Map<String, Integer> map = new TreeMap<>();
            for (int i = 0; i < elements; i++) map.put(keys.get(i), i);
        });

        printBenchmark("HashMap insertion (" + elements + " items)", hashInsertTime);
        printBenchmark("LinkedHashMap insertion (" + elements + " items)", linkedInsertTime);
        printBenchmark("TreeMap insertion (" + elements + " items)", treeInsertTime);

        // Comparison
        printComparison("Insertion Speed: HashMap vs TreeMap", treeInsertTime, hashInsertTime, "TreeMap", "HashMap");

        // Lookup: HashMap vs TreeMap
        Map<String, Integer> testHashMap = new HashMap<>();
        Map<String, Integer> testTreeMap = new TreeMap<>();
        for (int i = 0; i < elements; i++) {
            testHashMap.put(keys.get(i), i);
            testTreeMap.put(keys.get(i), i);
        }

        final int lookups = 20_000;
        Random random = new Random(42);
        List<String> lookupKeys = IntStream.range(0, lookups)
                .mapToObj(i -> keys.get(random.nextInt(elements)))
                .toList();

        double hashLookupTime = measureTime(() -> {
            for (String k : lookupKeys) testHashMap.get(k);
        });

        double treeLookupTime = measureTime(() -> {
            for (String k : lookupKeys) testTreeMap.get(k);
        });

        printBenchmark("HashMap random lookups (" + lookups + ")", hashLookupTime);
        printBenchmark("TreeMap random lookups (" + lookups + ")", treeLookupTime);
        printComparison("Lookup Speed: HashMap vs TreeMap", treeLookupTime, hashLookupTime, "TreeMap", "HashMap");
    }

    // =========================================================================
    // 11. BEST PRACTICES & INTERVIEW MASTERCLASS
    // =========================================================================
    /**
     * Demonstrates crucial Map pitfalls and provides senior interview answers.
     */
    private void demonstrateBestPracticesAndInterviewQuestions() {
        printSubHeader("11. Best Practices, Pitfalls & Interview Q&A Masterclass");

        // PITFALL 1: MUTABLE KEYS IN HASHMAP
        printInfo("CRITICAL PITFALL: Mutating a Key Object after insertion into HashMap");
        class MutableAccount {
            private String accountId;
            public MutableAccount(String id) { this.accountId = id; }
            public void setAccountId(String id) { this.accountId = id; }
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof MutableAccount that)) return false;
                return Objects.equals(accountId, that.accountId);
            }
            @Override
            public int hashCode() { return Objects.hash(accountId); }
            @Override
            public String toString() { return accountId; }
        }

        Map<MutableAccount, String> accountMap = new HashMap<>();
        MutableAccount acc = new MutableAccount("ACC_100");
        accountMap.put(acc, "VIP Customer");

        printResult("Lookup before key mutation", accountMap.get(acc));
        // Mutate key
        acc.setAccountId("ACC_999");
        // HashCode changes! Lookup now hashes to a different bucket!
        printResult("Lookup AFTER mutating key (returns null!)", accountMap.get(acc));
        printResult("Contains key after mutation?", accountMap.containsKey(acc));
        printWarning("[MEMORY LEAK RISK] The entry is now stranded inside the HashMap and cannot be retrieved or garbage collected!");

        // Safe Alternative: Records or Strings (immutable by definition)
        record SafeAccount(String accountId) {}
        Map<SafeAccount, String> safeMap = new HashMap<>();
        safeMap.put(new SafeAccount("ACC_100"), "VIP Customer");
        printResult("Safe lookup with Immutable Record Key", safeMap.get(new SafeAccount("ACC_100")));

        printSeparator();

        printInfo("======================= TOP INTERVIEW QUESTIONS & ANSWERS =======================");
        System.out.println("""
          Q1: How does HashMap handle hash collisions in Java 8+?
          A1: It uses separate chaining. Up to 8 elements in a bucket, it uses a singly-linked list.
              When a bucket reaches 8 elements AND the total map capacity is at least 64, the bucket is
              treeified into a Red-Black Tree (O(log n) worst-case search). If elements drop to 6,
              it untreeifies back to a linked list.

          Q2: Why must HashMap capacity always be a power of 2?
          A2: It allows calculating the bucket index using bitwise AND: `(capacity - 1) & hash`.
              Bitwise operations are significantly faster than modulo division (`hash % capacity`).
              When capacity is 2^n, `capacity - 1` has all lower bits set to 1, guaranteeing even bit distribution.

          Q3: What is the contract between equals() and hashCode()?
          A3: If two objects are equal according to equals(), their hashCode() MUST be identical.
              If two objects have the same hashCode(), they are NOT necessarily equal (hash collision).
              Violating this contract breaks HashMap and HashSet lookups.

          Q4: What is the difference between HashMap, LinkedHashMap, and TreeMap?
          A4: • HashMap: Unordered, O(1) avg ops, permits null key/values.
              • LinkedHashMap: Preserves insertion or access order via internal doubly-linked list, O(1) ops.
              • TreeMap: Sorted order via Red-Black Tree, O(log n) ops, implements NavigableMap & SequencedMap.

          Q5: How does SequencedMap in Java 21 improve Map APIs?
          A5: It unifies maps with defined encounter order (LinkedHashMap, TreeMap) under a common interface,
              providing firstEntry(), lastEntry(), pollFirstEntry(), pollLastEntry(), putFirst(), putLast(),
              and a live reversed() view without cloning.
        """);
    }
}
