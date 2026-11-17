package basics.mastery.java.collections.functional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import basics.mastery.collections.CollectionDemo;

/**
 * ============================================================================
 * JAVA COACH TUTORIAL: FUNCTIONAL PROGRAMMING & STREAMS MASTERCLASS (JDK 17-21)
 * ============================================================================
 * 
 * Target Audience: Beginner -> Intermediate -> Senior Java Engineers
 * 
 * Objectives:
 *   1. Master the 4 core functional interface families (Predicate, Function, Consumer, Supplier).
 *   2. Understand all 4 Method Reference classifications.
 *   3. Demystify Stream API mechanics: Lazy Intermediate vs Eager Terminal operations.
 *   4. Master Collectors: groupingBy, partitioningBy, flatMapping, and Collectors.teeing.
 *   5. Avoid critical Optional anti-patterns in production.
 *   6. Master Parallel Streams and know when NOT to parallelize (The N*Q rule).
 *   7. Implement modern Java 17-21 idioms (Stream.toList(), Records, Pattern Matching).
 * 
 * @author Srineel with Copilot & Expert Java Coach
 * @version 3.0
 * @since Java 17 (Enhanced for Java 21)
 */
public class FunctionalProgrammingDemo extends CollectionDemo {

    // Domain models for real-world enterprise examples
    public record Skill(String name, String level) {}
    public record Employee(int id, String name, String department, int salary, LocalDate hireDate, List<Skill> skills) {}
    public record OrderItem(String product, BigDecimal price, int quantity) {}
    public record Order(String orderId, String customerId, List<OrderItem> items, boolean paid) {}

    // Sealed Event hierarchy for Pattern Matching in Streams
    public sealed interface Event permits LoginEvent, LogoutEvent {}
    public record LoginEvent(String user, String ip) implements Event {}
    public record LogoutEvent(String user) implements Event {}

    public static void main(String[] args) {
        new FunctionalProgrammingDemo().demonstrateAll();
    }

    @Override
    public void demonstrateAll() {
        printHeader("FUNCTIONAL PROGRAMMING WITH COLLECTIONS: MASTERCLASS (JDK 17-21)");

        // 1. Core Functional Interfaces & Lambda Semantics
        demonstrateFunctionalInterfaces();

        // 2. All 4 Types of Method References
        demonstrateMethodReferences();

        // 3. Stream API Fundamentals & Laziness Demonstration
        demonstrateStreamBasicsAndLaziness();

        // 4. Intermediate Operations: map, flatMap, filter, sorted
        demonstrateIntermediateOperations();

        // 5. Terminal Operations: reduce, collect, findFirst, allMatch
        demonstrateTerminalOperations();

        // 6. Advanced Collectors: groupingBy, partitioningBy & Collectors.teeing
        demonstrateCollectorsMasterclass();

        // 7. Optional Deep Dive: Anti-patterns & Idiomatic Chaining
        demonstrateOptionalMasterclass();

        // 8. Parallel Streams: When to Use & When to Avoid (NQ Model)
        demonstrateParallelStreams();

        // 9. Modern Java 17-21 Functional Idioms
        demonstrateModernFunctionalPatterns();

        // 10. Best Practices, Pitfalls & Senior Interview Masterclass
        demonstrateBestPracticesAndInterviewQuestions();

        printSectionComplete("Functional Programming Masterclass");
    }

    // =========================================================================
    // 1. FUNCTIONAL INTERFACES & LAMBDA SEMANTICS
    // =========================================================================
    /**
     * Demonstrates the foundational functional interfaces in {@link java.util.function}.
     * 
     * [COACH: CORE INTERFACES CHEAT SHEET]
     * 1. Predicate<T>:    T -> boolean          (test, and, or, negate, not)
     * 2. Function<T, R>:  T -> R                (apply, andThen, compose)
     * 3. Consumer<T>:     T -> void             (accept, andThen)
     * 4. Supplier<T>:     () -> T               (get)
     * 5. BiFunction<T,U,R>: (T, U) -> R         (apply, andThen)
     * 6. UnaryOperator<T>:  T -> T              (specialized Function<T, T>)
     * 7. BinaryOperator<T>: (T, T) -> T         (specialized BiFunction<T, T, T>)
     */
    private void demonstrateFunctionalInterfaces() {
        printSubHeader("1. Core Functional Interfaces & Lambda Expressions");

        // A. Predicate<T>
        Predicate<String> isLongName = s -> s.length() > 5;
        Predicate<String> startsWithJ = s -> s.startsWith("J");
        Predicate<String> combined = isLongName.and(startsWithJ);
        printResult("Predicate test ('JavaScript')", combined.test("JavaScript")); // true
        printResult("Predicate.not ('Java' is NOT long)", Predicate.not(isLongName).test("Java")); // true

        // B. Function<T, R>
        Function<String, Integer> stringToLength = String::length;
        Function<Integer, String> lengthToCategory = len -> len > 5 ? "LONG" : "SHORT";
        // andThen: stringToLength THEN lengthToCategory
        Function<String, String> wordCategorizer = stringToLength.andThen(lengthToCategory);
        printResult("Function andThen composition ('Kubernetes')", wordCategorizer.apply("Kubernetes"));

        // C. Consumer<T>
        Consumer<String> logger = msg -> System.out.println("     [AUDIT LOG] " + msg);
        logger.accept("User Alice logged in successfully.");

        // D. Supplier<T>
        Supplier<Double> randomScore = () -> Math.random() * 100.0;
        printResult("Supplier generated score", String.format("%.2f", randomScore.get()));

        // E. BinaryOperator<T> (Great for reductions)
        BinaryOperator<Integer> safeMax = Math::max;
        printResult("BinaryOperator max(45, 92)", safeMax.apply(45, 92));

        printTip("[COACH BEST PRACTICE] Prefer standard java.util.function interfaces over custom one-off interfaces to ensure maximum interoperability with Java Streams.");
    }

    // =========================================================================
    // 2. THE 4 TYPES OF METHOD REFERENCES
    // =========================================================================
    /**
     * Demonstrates all 4 types of method references.
     * 
     * [COACH: METHOD REFERENCE TAXONOMY]
     * 1. Reference to a Static Method:              ClassName::staticMethod
     * 2. Reference to an Instance Method of an      ClassName::instanceMethod
     *    arbitrary object of a particular type:
     * 3. Reference to an Instance Method of a       instanceRef::method
     *    specific existing object:
     * 4. Reference to a Constructor:                ClassName::new
     */
    private void demonstrateMethodReferences() {
        printSubHeader("2. The Four Types of Method References");

        List<String> rawTokens = List.of("10", "25", "50", "100");

        // Type 1: Static method reference (Integer::parseInt)
        List<Integer> parsed = rawTokens.stream().map(Integer::parseInt).toList();
        printResult("Type 1 (Static): Integer::parseInt", parsed);

        // Type 2: Instance method on arbitrary object (String::toUpperCase)
        List<String> upper = List.of("apple", "banana").stream().map(String::toUpperCase).toList();
        printResult("Type 2 (Arbitrary Instance): String::toUpperCase", upper);

        // Type 3: Instance method on a specific existing object (System.out::print)
        StringBuilder accumulator = new StringBuilder("Prefix: ");
        Consumer<String> appender = accumulator::append; // Bound to accumulator instance!
        appender.accept("Appended Data");
        printResult("Type 3 (Specific Object): accumulator::append", accumulator.toString());

        // Type 4: Constructor reference (ArrayList::new, String[]::new)
        Supplier<List<String>> listSupplier = ArrayList::new;
        List<String> freshList = listSupplier.get();
        freshList.add("Element");
        printResult("Type 4 (Constructor): ArrayList::new", freshList);

        // Array constructor reference:
        String[] stringArray = upper.toArray(String[]::new);
        printResult("Array Constructor: String[]::new (Length)", stringArray.length);
    }

    // =========================================================================
    // 3. STREAM BASICS & LAZINESS DEMONSTRATION
    // =========================================================================
    /**
     * Demonstrates Stream pipeline structure and the principle of Lazy Evaluation.
     * 
     * [COACH: ARCHITECTURE DEEP DIVE]
     * - Intermediate operations (filter, map, sorted) do ZERO work when invoked!
     * - They merely construct a pipeline of stage descriptors (Sink objects).
     * - The pipeline executes ONLY when a Terminal Operation (collect, count, forEach) is triggered.
     * - Fused execution: Elements are pulled through the pipeline one-by-one (vertical processing),
     *   NOT stage-by-stage (horizontal).
     */
    private void demonstrateStreamBasicsAndLaziness() {
        printSubHeader("3. Stream Mechanics & Lazy Evaluation Proof");

        List<String> frameworks = List.of("Spring", "Quarkus", "Micronaut", "Vert.x", "Helidon");

        printInfo("Proving lazy evaluation: intermediate operations run only upon terminal execution:");

        // Build a pipeline with side-effects in peek() to prove execution timing
        Stream<String> lazyStream = frameworks.stream()
                .filter(name -> {
                    System.out.println("     [INTERMEDIATE FILTER] Filtering: " + name);
                    return name.length() > 5;
                })
                .map(name -> {
                    System.out.println("     [INTERMEDIATE MAP] Mapping: " + name);
                    return name.toUpperCase();
                });

        printInfo("Notice that NOTHING has printed yet! The stream is dormant.");
        printInfo("Now triggering terminal operation: findFirst()");

        // Short-circuiting terminal operation: processes ONLY until first match!
        String firstMatch = lazyStream.findFirst().orElse("NONE");
        printResult("findFirst() result", firstMatch);

        printTip("[INTERVIEW TIP] Streams are single-use! Attempting to call another terminal operation on a consumed stream throws IllegalStateException.");
    }

    // =========================================================================
    // 4. INTERMEDIATE OPERATIONS: MAP, FLATMAP & DISTINCT
    // =========================================================================
    /**
     * Demonstrates key intermediate operations with an emphasis on {@link Stream#flatMap}.
     * 
     * [COACH: FLATMAP EXPLAINED]
     * - `map`: Transforms each 1 element into 1 new element (1-to-1).
     * - `flatMap`: Transforms each 1 element into a Stream of 0, 1, or many elements,
     *   and FLATTENS all resulting streams into a single composite stream (1-to-N).
     */
    private void demonstrateIntermediateOperations() {
        printSubHeader("4. Intermediate Operations: map vs flatMap Deep Dive");

        List<Employee> engineeringTeam = List.of(
            new Employee(1, "Alice", "DevOps", 120_000, LocalDate.of(2020, 1, 1), 
                List.of(new Skill("Kubernetes", "Expert"), new Skill("Terraform", "Advanced"))),
            new Employee(2, "Bob", "Backend", 110_000, LocalDate.of(2021, 6, 1), 
                List.of(new Skill("Java", "Expert"), new Skill("PostgreSQL", "Intermediate"))),
            new Employee(3, "Charlie", "Backend", 105_000, LocalDate.of(2022, 3, 1), 
                List.of(new Skill("Java", "Expert"), new Skill("Kubernetes", "Beginner")))
        );

        // A. map (1 to 1: Employee -> Name)
        List<String> names = engineeringTeam.stream().map(Employee::name).toList();
        printResult("map (Employee -> Name)", names);

        // B. flatMap (1 to Many: Employee -> Stream<Skill> -> Flattened unique skill names)
        List<String> allUniqueSkills = engineeringTeam.stream()
                .flatMap(emp -> emp.skills().stream()) // Flattens List<Skill>
                .map(Skill::name)
                .distinct() // Eliminates duplicates
                .sorted()
                .toList();

        printResult("flatMap (Extract and flatten all unique skills)", allUniqueSkills);

        // C. Primitive Streams (mapToInt, summaryStatistics)
        IntSummaryStatistics salaryStats = engineeringTeam.stream()
                .mapToInt(Employee::salary)
                .summaryStatistics();

        printResult("Salary Statistics (Average)", String.format("$%.2f", salaryStats.getAverage()));
        printResult("Salary Statistics (Max)", "$" + salaryStats.getMax());
        printResult("Salary Statistics (Sum)", "$" + salaryStats.getSum());
    }

    // =========================================================================
    // 5. TERMINAL OPERATIONS: REDUCE & MATCHERS
    // =========================================================================
    /**
     * Demonstrates terminal operations with a focus on {@link Stream#reduce}.
     */
    private void demonstrateTerminalOperations() {
        printSubHeader("5. Terminal Operations: reduce, findFirst & Short-Circuit Matchers");

        List<Integer> values = List.of(5, 10, 15, 20);

        // 1-arg reduce: returns Optional (identity is absent)
        Optional<Integer> sumOpt = values.stream().reduce(Integer::sum);
        printResult("reduce(BinaryOperator)", sumOpt.orElse(0));

        // 2-arg reduce: with identity value (always returns T)
        int product = values.stream().reduce(1, (a, b) -> a * b);
        printResult("reduce(identity=1, accumulator=*)", product);

        // Short-circuit matchers
        boolean hasGreaterThan15 = values.stream().anyMatch(n -> n > 15);
        boolean allPositive = values.stream().allMatch(n -> n > 0);
        printResult("anyMatch(n > 15)", hasGreaterThan15);
        printResult("allMatch(n > 0)", allPositive);
    }

    // =========================================================================
    // 6. COLLECTORS MASTERCLASS: GROUPING, PARTITIONING & TEEING
    // =========================================================================
    /**
     * Comprehensive demonstration of {@link Collectors}:
     * - `toMap()` with duplicate key merge function.
     * - `groupingBy()` with downstream collectors.
     * - `partitioningBy()`.
     * - `Collectors.teeing()` (Java 12+).
     */
    private void demonstrateCollectorsMasterclass() {
        printSubHeader("6. Collectors Masterclass: groupingBy, partitioningBy & teeing");

        List<Employee> roster = List.of(
            new Employee(101, "Alice", "Engineering", 130000, LocalDate.of(2019, 1, 1), List.of()),
            new Employee(102, "Bob", "Engineering", 115000, LocalDate.of(2020, 2, 1), List.of()),
            new Employee(103, "Charlie", "Marketing", 85000, LocalDate.of(2021, 3, 1), List.of()),
            new Employee(104, "Diana", "Marketing", 92000, LocalDate.of(2018, 4, 1), List.of()),
            new Employee(105, "Eve", "Finance", 125000, LocalDate.of(2017, 5, 1), List.of())
        );

        // A. toMap with Merge Function (avoiding IllegalStateException on duplicate keys)
        Map<String, Integer> topSalaryPerDept = roster.stream().collect(Collectors.toMap(
            Employee::department,
            Employee::salary,
            Integer::max // Merge function: keep the maximum salary if department repeats!
        ));
        printResult("toMap with duplicate key resolver", topSalaryPerDept);

        // B. groupingBy with downstream average salary
        Map<String, Double> avgSalaryByDept = roster.stream().collect(Collectors.groupingBy(
            Employee::department,
            Collectors.averagingInt(Employee::salary)
        ));
        printResult("groupingBy department with downstream averagingInt", avgSalaryByDept);

        // C. partitioningBy (Boolean partition: Salary > 100k)
        Map<Boolean, List<String>> highEarners = roster.stream().collect(Collectors.partitioningBy(
            emp -> emp.salary() >= 100000,
            Collectors.mapping(Employee::name, Collectors.toList())
        ));
        printResult("partitioningBy (>=$100k)", highEarners);

        // D. Collectors.teeing (Java 12+): Computes count AND total in a single stream pass!
        record SalarySummary(long count, int totalPayroll) {}

        SalarySummary summary = roster.stream().collect(Collectors.teeing(
            Collectors.counting(),
            Collectors.summingInt(Employee::salary),
            SalarySummary::new // Combiner BiFunction
        ));
        printResult("Collectors.teeing (Count & Total Payroll)", summary);
    }

    // =========================================================================
    // 7. OPTIONAL MASTERCLASS: AVOIDING THE NOVICE PITFALLS
    // =========================================================================
    /**
     * Demonstrates idiomatic {@link Optional} usage.
     * 
     * [COACH: OPTIONAL GOLDEN RULES]
     * 1. NEVER call `opt.get()` without checking `isPresent()`. Better yet: NEVER call `get()` at all!
     * 2. Replace `if (opt.isPresent()) { ... }` with `opt.ifPresent()` or `opt.map()`.
     * 3. Use `orElseGet(Supplier)` instead of `orElse(defaultObj)` when the fallback is expensive to create!
     * 4. NEVER use `Optional<T>` as a method parameter, field, or collection element.
     */
    private void demonstrateOptionalMasterclass() {
        printSubHeader("7. Optional Masterclass: Idiomatic Chaining vs Novice Pitfalls");

        // Simulating DB lookup
        Function<String, Optional<String>> findUserEmail = username -> 
            "alice".equalsIgnoreCase(username) ? Optional.of("alice@company.com") : Optional.empty();

        // Idiomatic transformation chain:
        String domain = findUserEmail.apply("alice")
                .filter(email -> email.contains("@"))
                .map(email -> email.substring(email.indexOf("@") + 1))
                .orElse("unknown-domain.com");
        printResult("Optional chaining result", domain);

        // orElse vs orElseGet demonstration:
        printInfo("orElse vs orElseGet execution difference:");
        // orElse ALWAYS executes the default method, even when Optional is present!
        // orElseGet executes the supplier ONLY when Optional is empty!
        Optional<String> presentOpt = Optional.of("CachedValue");
        String result = presentOpt.orElseGet(() -> {
            System.out.println("     [FALLBACK] This will NOT print because Optional is present!");
            return "ExpensiveDefault";
        });
        printResult("orElseGet result", result);

        // Stream of Optionals: flatMap(Optional::stream) (Java 9+)
        List<Optional<String>> optionalList = List.of(Optional.of("A"), Optional.empty(), Optional.of("C"));
        List<String> presentOnly = optionalList.stream()
                .flatMap(Optional::stream) // Unpacks only present values!
                .toList();
        printResult("flatMap(Optional::stream) to extract present values", presentOnly);
    }

    // =========================================================================
    // 8. PARALLEL STREAMS: THE N*Q PERFORMANCE FORMULA
    // =========================================================================
    /**
     * Demonstrates Parallel Streams and explains when to parallelize.
     * 
     * [COACH: THE N * Q RULE (Doug Lea)]
     * Parallel streams add splitting and merging overhead (ForkJoinPool coordination).
     * Rule of thumb:
     *   N = Number of data elements
     *   Q = Cost of computation per element
     * Parallelize ONLY if: N * Q > 10,000!
     * 
     * DO NOT parallelize if:
     * - The source is an un-splittable collection (e.g. LinkedList or Stream.iterate()).
     * - Operations are IO-bound (causes starvation in shared ForkJoinPool.commonPool()).
     * - The operation modifies shared mutable state.
     */
    private void demonstrateParallelStreams() {
        printSubHeader("8. Parallel Streams: Benchmarking & The N*Q Formula");

        final int elements = 1_000_000;
        List<Integer> largeData = IntStream.range(0, elements).boxed().toList();

        // CPU-heavy operation (prime check or math)
        double seqTime = measureTime(() -> {
            long count = largeData.stream().filter(n -> n % 2 == 0).mapToDouble(Math::sqrt).count();
        });

        double parTime = measureTime(() -> {
            long count = largeData.parallelStream().filter(n -> n % 2 == 0).mapToDouble(Math::sqrt).count();
        });

        printBenchmark("Sequential stream (" + formatNumber(elements) + " items)", seqTime);
        printBenchmark("Parallel stream (" + formatNumber(elements) + " items)", parTime);
        printComparison("Sequential vs Parallel", seqTime, parTime, "Sequential", "Parallel");

        printWarning("[PARALLEL PITFALL] All parallel streams share ForkJoinPool.commonPool(). Blocking IO in a parallel stream chokes other parallel streams throughout the entire JVM!");
    }

    // =========================================================================
    // 9. MODERN JAVA 17-21 FUNCTIONAL PATTERNS
    // =========================================================================
    /**
     * Demonstrates Stream.toList() (Java 16+), Pattern Matching in streams,
     * and Records with functional transformations.
     */
    private void demonstrateModernFunctionalPatterns() {
        printSubHeader("9. [JDK 17-21] Modern Functional Idioms");

        // A. Stream.toList() vs Collectors.toList()
        // Stream.toList() produces an unmodifiable, compact List with zero collector overhead!
        List<String> modernList = Stream.of("Java", "Kotlin", "Scala").map(String::toLowerCase).toList();
        printResult("Stream.toList() (unmodifiable)", modernList);

        // B. Pattern Matching with Streams (Record Patterns in Java 21)
        List<Event> auditLog = List.of(
            new LoginEvent("Alice", "192.168.1.1"),
            new LogoutEvent("Alice"),
            new LoginEvent("Bob", "10.0.0.5")
        );

        // Pattern matching on stream elements using Java 21 switch
        List<String> formattedEvents = auditLog.stream().map(event -> switch (event) {
            case LoginEvent(var user, var ip) -> "LOGIN: " + user + " from " + ip;
            case LogoutEvent(var user) -> "LOGOUT: " + user;
        }).toList();

        printResult("Pattern matching inside stream map()", formattedEvents);
    }

    // =========================================================================
    // 10. SENIOR INTERVIEW MASTERCLASS & PRODUCTION CHECKLIST
    // =========================================================================
    private void demonstrateBestPracticesAndInterviewQuestions() {
        printSubHeader("10. Best Practices, Pitfalls & Senior Interview Q&A Masterclass");

        printInfo("======================= TOP INTERVIEW QUESTIONS & ANSWERS =======================");
        System.out.println("""
          Q1: What is the fundamental difference between map() and flatMap()?
          A1: map() applies a 1-to-1 transformation, wrapping each input element into a single output element.
              flatMap() applies a 1-to-many transformation where each element produces a Stream, and flatMap
              subsequently flattens all those streams into a single un-nested Stream.

          Q2: Why are Streams lazy, and how does the JVM optimize stream execution?
          A2: Intermediate operations (filter, map) do not execute immediately; they build an execution pipeline.
              The pipeline executes only when an eager terminal operation (collect, count, findFirst) is called.
              This allows vertical processing (fused stages in a single pass) and short-circuit optimizations.

          Q3: What causes an IllegalStateException when using Collectors.toMap()?
          A3: If the stream contains duplicate keys and no merge function is provided to toMap(keyMapper, valueMapper),
              Collectors.toMap throws IllegalStateException: Duplicate key. Always provide a merge function:
              `toMap(keyMapper, valueMapper, (existing, replacement) -> existing)`.

          Q4: What is Collectors.teeing() and what problem does it solve?
          A4: Introduced in Java 12, Collectors.teeing() accepts two downstream collectors and a merger function.
              It allows performing two independent aggregations (e.g., sum and count, or min and max) in a single
              pass over a Stream without reading the stream twice.

          Q5: When should you avoid using Parallel Streams?
          A5: 1) Small datasets where thread coordination overhead outweighs CPU gains (N*Q < 10,000).
              2) Data structures that split poorly (LinkedList, IO Streams).
              3) Operations with stateful lambdas or side effects.
              4) Blocking IO tasks, which saturate ForkJoinPool.commonPool() and stall JVM performance.
        """);
    }
}
