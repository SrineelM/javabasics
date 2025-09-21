package io.mastery.collections.functional;

import io.mastery.collections.CollectionDemo;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

/**
 * Comprehensive demonstration of functional programming with Java Collections.
 * 
 * This class provides interactive examples covering:
 * - Stream API fundamentals and intermediate operations
 * - Terminal operations and collectors
 * - Parallel streams and performance considerations
 * - Custom collectors and advanced stream patterns
 * - Functional interfaces and method references
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class FunctionalProgrammingDemo extends CollectionDemo {
    
    // Sample data for demonstrations
    record Employee(String name, String department, int salary, LocalDate hireDate, List<String> skills) {}
    record Product(String name, String category, BigDecimal price, int quantity, boolean available) {}
    record Order(String id, String customerId, List<Product> products, LocalDate orderDate) {}
    
    @Override
    public void demonstrateAll() {
        printHeader("FUNCTIONAL PROGRAMMING WITH COLLECTIONS");
        
        // Stream fundamentals
        demonstrateStreamBasics();
        demonstrateIntermediateOperations();
        demonstrateTerminalOperations();
        
        // Advanced stream operations
        demonstrateCollectors();
        demonstrateGroupingAndPartitioning();
        demonstrateParallelStreams();
        
        // Functional interfaces and method references
        demonstrateFunctionalInterfaces();
        demonstrateMethodReferences();
        
        // Advanced patterns
        demonstrateAdvancedStreamPatterns();
        demonstrateCustomCollectors();
        
        // Best practices
        demonstrateBestPractices();
        
        printSectionComplete("Functional Programming");
    }
    
    /**
     * Demonstrates basic Stream API concepts
     */
    private void demonstrateStreamBasics() {
        printSubHeader("Stream API Basics");
        
        printInfo("Streams provide a functional approach to processing collections");
        printInfo("Streams are lazy, immutable, and can be processed sequentially or in parallel");
        
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        printResult("Source list", numbers);
        
        // Basic stream operations
        List<Integer> evenSquares = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .toList();
        printResult("Even numbers squared", evenSquares);
        
        // Stream from different sources
        printInfo("Creating streams from different sources:");
        
        // From array
        String[] words = {"java", "stream", "functional", "programming"};
        List<String> uppercaseWords = Arrays.stream(words)
                .map(String::toUpperCase)
                .toList();
        printResult("From array (uppercase)", uppercaseWords);
        
        // From range
        List<Integer> range = IntStream.range(1, 6)
                .boxed()
                .toList();
        printResult("From range (1-5)", range);
        
        // From generator
        List<String> generated = Stream.generate(() -> "Hello")
                .limit(3)
                .toList();
        printResult("From generator", generated);
        
        // From iterate
        List<Integer> fibonacci = Stream.iterate(new int[]{0, 1}, 
                arr -> new int[]{arr[1], arr[0] + arr[1]})
                .limit(10)
                .map(arr -> arr[0])
                .toList();
        printResult("Fibonacci sequence", fibonacci);
        
        printTip("Streams are one-time use - create a new stream for each operation chain");
        printInfo("Use appropriate primitive streams (IntStream, LongStream, DoubleStream) for performance");
    }
    
    /**
     * Demonstrates intermediate stream operations
     */
    private void demonstrateIntermediateOperations() {
        printSubHeader("Intermediate Stream Operations");
        
        printInfo("Intermediate operations are lazy and return a new stream");
        
        List<String> languages = List.of("Java", "Python", "JavaScript", "Kotlin", "Scala", "Java", "Python");
        
        // filter - select elements based on predicate
        List<String> longNames = languages.stream()
                .filter(lang -> lang.length() > 4)
                .toList();
        printResult("Languages with > 4 characters", longNames);
        
        // map - transform elements
        List<Integer> nameLengths = languages.stream()
                .map(String::length)
                .toList();
        printResult("Name lengths", nameLengths);
        
        // flatMap - flatten nested structures
        List<List<String>> nestedLists = List.of(
            List.of("Java", "Kotlin"),
            List.of("Python", "Django"),
            List.of("JavaScript", "React", "Node.js")
        );
        
        List<String> flattened = nestedLists.stream()
                .flatMap(List::stream)
                .toList();
        printResult("Flattened lists", flattened);
        
        // distinct - remove duplicates
        List<String> uniqueLanguages = languages.stream()
                .distinct()
                .toList();
        printResult("Unique languages", uniqueLanguages);
        
        // sorted - sort elements
        List<String> sortedLanguages = languages.stream()
                .distinct()
                .sorted()
                .toList();
        printResult("Sorted unique languages", sortedLanguages);
        
        // sorted with custom comparator
        List<String> sortedByLength = languages.stream()
                .distinct()
                .sorted(Comparator.comparing(String::length).thenComparing(String::compareTo))
                .toList();
        printResult("Sorted by length then alphabetically", sortedByLength);
        
        // limit and skip
        List<String> limitedLanguages = languages.stream()
                .distinct()
                .sorted()
                .skip(1)
                .limit(3)
                .toList();
        printResult("Skip 1, limit 3", limitedLanguages);
        
        // peek - debug/side effects (use sparingly)
        List<String> processed = languages.stream()
                .filter(lang -> lang.length() > 4)
                .peek(lang -> System.out.println("  Processing: " + lang))
                .map(String::toLowerCase)
                .toList();
        printResult("Processed with peek", processed);
        
        printWarning("Avoid using peek() for business logic - use it only for debugging");
        printTip("Chain operations efficiently - order matters for performance");
    }
    
    /**
     * Demonstrates terminal stream operations
     */
    private void demonstrateTerminalOperations() {
        printSubHeader("Terminal Stream Operations");
        
        printInfo("Terminal operations trigger stream processing and produce a result");
        
        List<Integer> numbers = IntStream.range(1, 21).boxed().toList();
        printResult("Numbers 1-20", numbers);
        
        // forEach - perform action on each element
        System.out.print("  forEach squares: ");
        numbers.stream()
                .filter(n -> n <= 5)
                .map(n -> n * n)
                .forEach(n -> System.out.print(n + " "));
        System.out.println();
        
        // collect - accumulate elements into a collection
        List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        printResult("Even numbers (collect)", evenNumbers);
        
        // reduce - combine elements into single result
        Optional<Integer> sum = numbers.stream()
                .reduce(Integer::sum);
        printResult("Sum using reduce", sum.orElse(0));
        
        // reduce with identity
        Integer product = numbers.stream()
                .limit(4)
                .reduce(1, (a, b) -> a * b);
        printResult("Product of first 4 numbers", product);
        
        // min/max
        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        printResult("Min", min.orElse(0));
        printResult("Max", max.orElse(0));
        
        // count
        long evenCount = numbers.stream()
                .filter(n -> n % 2 == 0)
                .count();
        printResult("Count of even numbers", evenCount);
        
        // anyMatch, allMatch, noneMatch
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        printResult("Has even numbers", hasEven);
        printResult("All positive", allPositive);
        printResult("None negative", noneNegative);
        
        // findFirst, findAny
        Optional<Integer> firstEven = numbers.stream()
                .filter(n -> n % 2 == 0)
                .findFirst();
        printResult("First even number", firstEven.orElse(-1));
        
        // toArray
        Integer[] evenArray = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toArray(Integer[]::new);
        printResult("Even numbers as array length", evenArray.length);
        
        printTip("Use Optional properly - avoid get() without checking isPresent()");
        printInfo("Terminal operations are eager and consume the stream");
    }
    
    /**
     * Demonstrates Collectors and advanced collecting
     */
    private void demonstrateCollectors() {
        printSubHeader("Collectors - Advanced Collection Operations");
        
        printInfo("Collectors provide powerful ways to accumulate stream elements");
        
        // Sample employee data
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 95000, LocalDate.of(2020, Month.JANUARY, 15), 
                        List.of("Java", "Python", "AWS")),
            new Employee("Bob", "Engineering", 87000, LocalDate.of(2021, Month.MARCH, 20), 
                        List.of("JavaScript", "React", "Node.js")),
            new Employee("Charlie", "Marketing", 72000, LocalDate.of(2019, Month.JUNE, 10), 
                        List.of("Analytics", "SEO", "Content")),
            new Employee("Diana", "Sales", 78000, LocalDate.of(2020, Month.NOVEMBER, 5), 
                        List.of("CRM", "Negotiation", "Presentation")),
            new Employee("Eve", "Engineering", 92000, LocalDate.of(2018, Month.SEPTEMBER, 25), 
                        List.of("Java", "Microservices", "Docker"))
        );
        
        printResult("Employee count", employees.size());
        
        // Basic collectors
        List<String> names = employees.stream()
                .map(Employee::name)
                .collect(Collectors.toList());
        printResult("Employee names", names);
        
        Set<String> departments = employees.stream()
                .map(Employee::department)
                .collect(Collectors.toSet());
        printResult("Unique departments", departments);
        
        // Joining strings
        String namesList = employees.stream()
                .map(Employee::name)
                .collect(Collectors.joining(", "));
        printResult("Names joined", namesList);
        
        String departmentList = employees.stream()
                .map(Employee::department)
                .distinct()
                .collect(Collectors.joining(" | ", "[", "]"));
        printResult("Departments with delimiters", departmentList);
        
        // Numerical collectors
        double averageSalary = employees.stream()
                .collect(Collectors.averagingInt(Employee::salary));
        printResult("Average salary", String.format("$%.2f", averageSalary));
        
        IntSummaryStatistics salaryStats = employees.stream()
                .collect(Collectors.summarizingInt(Employee::salary));
        printResult("Salary statistics", salaryStats);
        
        // Mapping collector
        List<Integer> salariesInThousands = employees.stream()
                .collect(Collectors.mapping(
                    emp -> emp.salary() / 1000,
                    Collectors.toList()
                ));
        printResult("Salaries in thousands", salariesInThousands);
        
        // Filtering collector (Java 9+)
        List<String> seniorEngineers = employees.stream()
                .collect(Collectors.filtering(
                    emp -> emp.department().equals("Engineering") && emp.salary() > 90000,
                    Collectors.mapping(Employee::name, Collectors.toList())
                ));
        printResult("Senior engineers (>90k)", seniorEngineers);
        
        // FlatMapping collector (Java 9+)
        List<String> allSkills = employees.stream()
                .collect(Collectors.flatMapping(
                    emp -> emp.skills().stream(),
                    Collectors.toSet()
                ))
                .stream()
                .sorted()
                .toList();
        printResult("All unique skills", allSkills);
        
        printTip("Use built-in collectors when possible - they're optimized and readable");
        printInfo("Combine collectors for complex data transformations");
    }
    
    /**
     * Demonstrates grouping and partitioning operations
     */
    private void demonstrateGroupingAndPartitioning() {
        printSubHeader("Grouping and Partitioning");
        
        printInfo("Grouping and partitioning enable powerful data analysis");
        
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 95000, LocalDate.of(2020, Month.JANUARY, 15), 
                        List.of("Java", "Python")),
            new Employee("Bob", "Engineering", 87000, LocalDate.of(2021, Month.MARCH, 20), 
                        List.of("JavaScript", "React")),
            new Employee("Charlie", "Marketing", 72000, LocalDate.of(2019, Month.JUNE, 10), 
                        List.of("Analytics")),
            new Employee("Diana", "Sales", 78000, LocalDate.of(2020, Month.NOVEMBER, 5), 
                        List.of("CRM")),
            new Employee("Eve", "Engineering", 92000, LocalDate.of(2018, Month.SEPTEMBER, 25), 
                        List.of("Java", "Docker"))
        );
        
        // Basic grouping by department
        Map<String, List<Employee>> byDepartment = employees.stream()
                .collect(Collectors.groupingBy(Employee::department));
        
        printResult("Grouped by department", byDepartment.keySet());
        byDepartment.forEach((dept, empList) -> 
            System.out.println("  " + dept + ": " + empList.size() + " employees"));
        
        // Grouping with downstream collector
        Map<String, Double> avgSalaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::department,
                    Collectors.averagingInt(Employee::salary)
                ));
        printResult("Average salary by department", avgSalaryByDept);
        
        // Grouping by multiple criteria
        Map<String, Map<String, List<Employee>>> byDeptAndSalaryRange = employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::department,
                    Collectors.groupingBy(emp -> emp.salary() > 85000 ? "High" : "Regular")
                ));
        
        printInfo("Employees by department and salary range:");
        byDeptAndSalaryRange.forEach((dept, salaryGroups) -> {
            System.out.println("  " + dept + ":");
            salaryGroups.forEach((range, empList) -> 
                System.out.println("    " + range + ": " + empList.size() + " employees"));
        });
        
        // Grouping with counting
        Map<String, Long> employeeCountByDept = employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::department,
                    Collectors.counting()
                ));
        printResult("Employee count by department", employeeCountByDept);
        
        // Partitioning (boolean-based grouping)
        Map<Boolean, List<Employee>> partitionedBySalary = employees.stream()
                .collect(Collectors.partitioningBy(emp -> emp.salary() > 85000));
        
        printResult("High earners (>85k)", partitionedBySalary.get(true).size());
        printResult("Regular earners", partitionedBySalary.get(false).size());
        
        // Partitioning with downstream collector
        Map<Boolean, Double> avgSalaryByRange = employees.stream()
                .collect(Collectors.partitioningBy(
                    emp -> emp.salary() > 85000,
                    Collectors.averagingInt(Employee::salary)
                ));
        printResult("Average salary (high earners)", String.format("$%.2f", avgSalaryByRange.get(true)));
        printResult("Average salary (regular)", String.format("$%.2f", avgSalaryByRange.get(false)));
        
        // Custom grouping classifier
        Map<String, List<Employee>> byExperienceLevel = employees.stream()
                .collect(Collectors.groupingBy(emp -> {
                    int yearsExperience = LocalDate.now().getYear() - emp.hireDate().getYear();
                    if (yearsExperience >= 5) return "Senior";
                    else if (yearsExperience >= 2) return "Mid-level";
                    else return "Junior";
                }));
        
        printResult("Employees by experience level", byExperienceLevel.keySet());
        byExperienceLevel.forEach((level, empList) -> 
            System.out.println("  " + level + ": " + empList.stream()
                    .map(Employee::name)
                    .collect(Collectors.joining(", "))));
        
        printTip("Use partitioningBy for boolean-based grouping - it's more efficient than groupingBy");
        printInfo("Combine grouping with downstream collectors for complex aggregations");
    }
    
    /**
     * Demonstrates parallel stream processing
     */
    private void demonstrateParallelStreams() {
        printSubHeader("Parallel Stream Processing");
        
        printInfo("Parallel streams can improve performance for CPU-intensive operations on large datasets");
        printWarning("Not all operations benefit from parallelization - measure performance!");
        
        // Create large dataset for demonstration
        List<Integer> largeDataset = IntStream.range(1, 1000000)
                .boxed()
                .toList();
        
        printResult("Dataset size", formatNumber(largeDataset.size()));
        
        // Sequential vs parallel comparison for CPU-intensive operation
        double sequentialTime = measureTime(() -> {
            @SuppressWarnings("unused")
            long sum = largeDataset.stream()
                    .mapToLong(n -> expensiveComputation(n))
                    .sum();
        });
        
        double parallelTime = measureTime(() -> {
            @SuppressWarnings("unused")
            long sum = largeDataset.parallelStream()
                    .mapToLong(n -> expensiveComputation(n))
                    .sum();
        });
        
        printBenchmark("Sequential expensive computation", sequentialTime);
        printBenchmark("Parallel expensive computation", parallelTime);
        
        double speedup = sequentialTime / parallelTime;
        printResult("Speedup factor", String.format("%.2fx", speedup));
        
        // Demonstrate when parallel streams don't help
        double seqSimpleTime = measureTime(() -> {
            @SuppressWarnings("unused")
            long sum = largeDataset.stream()
                    .mapToLong(Integer::longValue)
                    .sum();
        });
        
        double parSimpleTime = measureTime(() -> {
            @SuppressWarnings("unused")
            long sum = largeDataset.parallelStream()
                    .mapToLong(Integer::longValue)
                    .sum();
        });
        
        printComparison("Simple operation (sequential vs parallel)",
                       parSimpleTime, seqSimpleTime,
                       "Parallel", "Sequential");
        
        // Parallel reduction with custom combiner
        List<String> words = List.of("java", "stream", "parallel", "processing", "performance", 
                                   "optimization", "concurrent", "functional", "programming");
        
        String concatenated = words.parallelStream()
                .reduce("", 
                       (partial, element) -> partial + element, // accumulator
                       (s1, s2) -> s1 + s2); // combiner for parallel execution
        printResult("Parallel string concatenation", concatenated);
        
        // Collecting in parallel
        List<String> processedWords = words.parallelStream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        printResult("Parallel processing to list", processedWords);
        
        // Parallel grouping
        Map<Integer, List<String>> wordsByLength = words.parallelStream()
                .collect(Collectors.groupingBy(String::length));
        printResult("Words grouped by length", wordsByLength);
        
        // Control parallelism
        printInfo("Available processors: " + Runtime.getRuntime().availableProcessors());
        
        // Custom thread pool (advanced)
        try (ForkJoinPool customThreadPool = new ForkJoinPool(2)) {
            List<Integer> result = customThreadPool.submit(() ->
                largeDataset.parallelStream()
                    .filter(n -> n % 2 == 0)
                    .limit(100)
                    .toList()
            ).get();
            printResult("Custom thread pool result size", result.size());
        } catch (Exception e) {
            printInfo("Error with custom thread pool: " + e.getMessage());
        }
        
        printTip("Use parallel streams for CPU-intensive operations on large datasets");
        printWarning("Be careful with stateful operations and shared mutable state in parallel streams");
    }
    
    // Helper method for CPU-intensive computation
    private long expensiveComputation(int n) {
                long result = 0;
                for (int i = 0; i < 1000; i++) {
                        result += (long) Math.sqrt(n * i);
                }
        return result;
    }
    
    /**
     * Demonstrates functional interfaces and lambda expressions
     */
    private void demonstrateFunctionalInterfaces() {
        printSubHeader("Functional Interfaces and Lambda Expressions");
        
        printInfo("Functional interfaces provide the foundation for lambda expressions and method references");
        
        List<String> words = List.of("java", "functional", "programming", "lambda", "stream");
        
        // Predicate<T> - boolean-valued function
        Predicate<String> isLongWord = word -> word.length() > 5;
        Predicate<String> startsWithP = word -> word.startsWith("p");
        
        List<String> longWords = words.stream()
                .filter(isLongWord)
                .toList();
        printResult("Long words (>5 chars)", longWords);
        
        // Combining predicates
        List<String> longWordsStartingWithP = words.stream()
                .filter(isLongWord.and(startsWithP))
                .toList();
        printResult("Long words starting with 'p'", longWordsStartingWithP);
        
        // Function<T, R> - transforms input to output
        Function<String, Integer> wordLength = String::length;
        Function<Integer, String> lengthCategory = len -> {
            if (len <= 4) return "short";
            else if (len <= 7) return "medium";
            else return "long";
        };
        
        List<String> categories = words.stream()
                .map(wordLength.andThen(lengthCategory))
                .toList();
        printResult("Word categories", categories);
        
        // Consumer<T> - accepts input, returns nothing
        Consumer<String> printer = word -> System.out.print(word + " ");
        System.out.print("  Words: ");
        words.forEach(printer);
        System.out.println();
        
        // Supplier<T> - provides values
        Supplier<String> randomWord = () -> words.get(new Random().nextInt(words.size()));
        List<String> randomWords = Stream.generate(randomWord)
                .limit(3)
                .toList();
        printResult("Random words", randomWords);
        
        // BiFunction<T, U, R> - two inputs, one output
        BiFunction<String, String, String> concatenator = (a, b) -> a + "-" + b;
        String result = words.stream()
                .reduce(concatenator::apply)
                .orElse("");
        printResult("Concatenated with BiFunction", result);
        
        // UnaryOperator<T> - special case of Function<T, T>
        UnaryOperator<String> toUpperCase = String::toUpperCase;
        List<String> upperWords = words.stream()
                .map(toUpperCase)
                .toList();
        printResult("Uppercase words", upperWords);
        
        // BinaryOperator<T> - special case of BiFunction<T, T, T>
        BinaryOperator<String> combiner = (s1, s2) -> s1.length() > s2.length() ? s1 : s2;
        Optional<String> longest = words.stream()
                .reduce(combiner);
        printResult("Longest word", longest.orElse("none"));
        
        // Custom functional interface
        @FunctionalInterface
        interface WordProcessor {
            String process(String word, int index);
        }
        
        WordProcessor indexedProcessor = (word, index) -> 
            index + ": " + word.toUpperCase();
        
        List<String> processed = IntStream.range(0, words.size())
                .mapToObj(i -> indexedProcessor.process(words.get(i), i))
                .toList();
        printResult("Processed with custom interface", processed);
        
        printTip("Use built-in functional interfaces when possible - they're well-tested and optimized");
        printInfo("Lambda expressions are just syntactic sugar for functional interfaces");
    }
    
    /**
     * Demonstrates method references and their types
     */
    private void demonstrateMethodReferences() {
        printSubHeader("Method References");
        
        printInfo("Method references provide a more concise way to refer to methods");
        
        List<String> words = List.of("java", "method", "reference", "functional", "programming");
        
        // Static method reference
        List<Integer> lengths1 = words.stream()
                .map(String::length) // equivalent to: word -> word.length()
                .toList();
        printResult("Static method reference (String::length)", lengths1);
        
        // Instance method reference on arbitrary object
        List<String> upperWords = words.stream()
                .map(String::toUpperCase) // equivalent to: word -> word.toUpperCase()
                .toList();
        printResult("Instance method reference (String::toUpperCase)", upperWords);
        
        // Instance method reference on specific object
        String prefix = ">>> ";
        Function<String, String> prefixer = prefix::concat; // equivalent to: word -> prefix.concat(word)
        List<String> prefixedWords = words.stream()
                .map(prefixer)
                .toList();
        printResult("Specific instance method reference", prefixedWords);
        
        // Constructor reference
        Supplier<List<String>> listFactory = ArrayList::new; // equivalent to: () -> new ArrayList<>()
        List<String> newList = listFactory.get();
        newList.addAll(words);
        printResult("Constructor reference result", newList);
        
        // Constructor reference with parameters
        Function<String, StringBuilder> sbFactory = StringBuilder::new; // equivalent to: s -> new StringBuilder(s)
        List<StringBuilder> builders = words.stream()
                .map(sbFactory)
                .toList();
        printResult("Parameterized constructor reference count", builders.size());
        
        // Array constructor reference
        IntFunction<String[]> arrayFactory = String[]::new; // equivalent to: size -> new String[size]
        String[] wordArray = words.stream()
                .toArray(arrayFactory);
        printResult("Array constructor reference length", wordArray.length);
        
        // Method reference in different contexts
        printInfo("Method references in different contexts:");
        
        // As Predicate
        List<String> nonEmpty = words.stream()
                .filter(((Predicate<String>) String::isEmpty).negate())
                .toList();
        printResult("Non-empty words", nonEmpty);
        
        // As Comparator
        List<String> sortedByLength = words.stream()
                .sorted(Comparator.comparing(String::length))
                .toList();
        printResult("Sorted by length", sortedByLength);
        
        // Chaining method references
        List<String> processed = words.stream()
                .map(String::toUpperCase)
                .map(String::trim)
                .sorted(String::compareTo)
                .toList();
        printResult("Chained method references", processed);
        
        // Method reference vs lambda performance
        List<Integer> numbers = IntStream.range(1, 100000).boxed().toList();
        
        double lambdaTime = measureTime(() -> {
            numbers.stream()
                    .map(n -> n.toString())
                    .count();
        });
        
        double methodRefTime = measureTime(() -> {
            numbers.stream()
                    .map(Object::toString)
                    .count();
        });
        
        printBenchmark("Lambda expression", lambdaTime);
        printBenchmark("Method reference", methodRefTime);
        
        printTip("Use method references when they make code more readable");
        printInfo("Method references are often more concise and can be more performant");
    }
    
    /**
     * Demonstrates advanced stream patterns
     */
    private void demonstrateAdvancedStreamPatterns() {
        printSubHeader("Advanced Stream Patterns");
        
        printInfo("Advanced patterns for complex data processing scenarios");
        
        // Nested object processing
        List<Order> orders = List.of(
            new Order("O1", "C1", List.of(
                new Product("Laptop", "Electronics", new BigDecimal("999.99"), 2, true),
                new Product("Mouse", "Electronics", new BigDecimal("29.99"), 5, true)
            ), LocalDate.of(2024, Month.JANUARY, 15)),
            
            new Order("O2", "C2", List.of(
                new Product("Book", "Education", new BigDecimal("19.99"), 3, true),
                new Product("Pen", "Office", new BigDecimal("1.99"), 10, false)
            ), LocalDate.of(2024, Month.FEBRUARY, 20))
        );
        
        // FlatMap for nested collections
        List<Product> allProducts = orders.stream()
                .flatMap(order -> order.products().stream())
                .toList();
        printResult("Total products across all orders", allProducts.size());
        
        // Complex aggregation
        BigDecimal totalValue = orders.stream()
                .flatMap(order -> order.products().stream())
                .filter(Product::available)
                .map(product -> product.price().multiply(BigDecimal.valueOf(product.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        printResult("Total value of available products", "$" + totalValue);
        
        // Multi-level grouping
        Map<String, Map<Boolean, List<Product>>> productsByCategory = orders.stream()
                .flatMap(order -> order.products().stream())
                .collect(Collectors.groupingBy(
                    Product::category,
                    Collectors.groupingBy(Product::available)
                ));
        
        printInfo("Products by category and availability:");
        productsByCategory.forEach((category, availabilityMap) -> {
            System.out.println("  " + category + ":");
            availabilityMap.forEach((available, products) -> 
                System.out.println("    " + (available ? "Available" : "Unavailable") + 
                                 ": " + products.size()));
        });
        
        // Optional handling in streams
        List<Optional<String>> optionalList = List.of(
            Optional.of("Value1"),
            Optional.empty(),
            Optional.of("Value2"),
            Optional.empty(),
            Optional.of("Value3")
        );
        
        List<String> presentValues = optionalList.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        printResult("Present values", presentValues);
        
        // Using flatMap with Optional (Java 9+)
        List<String> flatMappedValues = optionalList.stream()
                .flatMap(Optional::stream)
                .toList();
        printResult("FlatMapped Optional values", flatMappedValues);
        
        // Conditional processing
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        List<String> conditionallyProcessed = numbers.stream()
                .map(n -> {
                    if (n % 2 == 0) {
                        return "Even: " + n;
                    } else if (n % 3 == 0) {
                        return "Odd divisible by 3: " + n;
                    } else {
                        return "Other odd: " + n;
                    }
                })
                .toList();
        printResult("Conditionally processed", conditionallyProcessed);
        
        // Stateful operations (use with caution)
        AtomicInteger counter = new AtomicInteger();
        List<String> numbered = words.stream()
                .map(word -> counter.incrementAndGet() + ": " + word)
                .toList();
        printResult("Numbered words", numbered);
        
        printWarning("Avoid stateful operations in parallel streams - they can cause race conditions");
        printTip("Use indices from IntStream.range() instead of external counters when possible");
    }
    
    // Sample data
    private final List<String> words = List.of("java", "stream", "functional", "programming", "lambda");
    
    /**
     * Demonstrates custom collectors
     */
    private void demonstrateCustomCollectors() {
        printSubHeader("Custom Collectors");
        
        printInfo("Create custom collectors for specialized data collection needs");
        
        List<String> sentences = List.of(
            "Java is a programming language",
            "Streams provide functional programming",
            "Collectors enable data aggregation",
            "Lambda expressions simplify code"
        );
        
        // Custom collector to join with numbering
        Collector<String, ?, String> numberedJoiner = Collector.of(
            StringBuilder::new, // supplier
            (sb, item) -> { // accumulator
                if (sb.length() > 0) sb.append(", ");
                sb.append(sb.toString().split(", ").length).append(". ").append(item);
            },
            (sb1, sb2) -> sb1.append(", ").append(sb2), // combiner
            StringBuilder::toString // finisher
        );
        
        String numberedSentences = sentences.stream()
                .collect(numberedJoiner);
        printResult("Custom numbered collector", numberedSentences);
        
        // Custom collector for statistics
        record WordStats(int totalWords, int totalChars, double avgLength) {}
        
        Collector<String, ?, WordStats> wordStatsCollector = Collector.of(
            () -> new int[]{0, 0}, // supplier: [wordCount, charCount]
            (acc, word) -> { // accumulator
                acc[0]++; // word count
                acc[1] += word.length(); // char count
            },
            (acc1, acc2) -> new int[]{acc1[0] + acc2[0], acc1[1] + acc2[1]}, // combiner
            acc -> new WordStats(acc[0], acc[1], (double) acc[1] / acc[0]) // finisher
        );
        
        WordStats stats = words.stream()
                .collect(wordStatsCollector);
        printResult("Word statistics", 
                   "Words: " + stats.totalWords + ", Chars: " + stats.totalChars + 
                   ", Avg Length: " + String.format("%.2f", stats.avgLength));
        
        // Custom collector using built-in combinators
        Collector<String, ?, Map<Integer, List<String>>> byLengthCollector = 
            Collectors.groupingBy(String::length);
        
        Map<Integer, List<String>> wordsByLength = words.stream()
                .collect(byLengthCollector);
        printResult("Words grouped by length", wordsByLength);
        
        // Collector composition
        Map<Integer, String> lengthToJoinedWords = words.stream()
                .collect(Collectors.groupingBy(
                    String::length,
                    Collectors.joining(", ")
                ));
        printResult("Length to joined words", lengthToJoinedWords);
        
        printTip("Use Collector.of() for completely custom collection logic");
        printInfo("Combine existing collectors with groupingBy and mapping for complex operations");
    }
    
    /**
     * Demonstrates best practices for functional programming with collections
     */
    private void demonstrateBestPractices() {
        printSubHeader("Functional Programming Best Practices");
        
        printInfo("💡 Stream Performance Tips:");
        System.out.println("  • Use primitive streams (IntStream, LongStream, DoubleStream) when possible");
        System.out.println("  • Order operations for early termination (filter before map)");
        System.out.println("  • Use parallel streams only for CPU-intensive operations on large datasets");
        System.out.println("  • Avoid stateful operations in parallel streams");
        System.out.println("  • Use method references when they improve readability");
        
        printSeparator();
        
        printInfo("🎯 Readability Guidelines:");
        System.out.println("  • Keep stream pipelines short and focused");
        System.out.println("  • Extract complex lambdas into named methods");
        System.out.println("  • Use meaningful variable names in lambda parameters");
        System.out.println("  • Break complex operations into multiple steps");
        System.out.println("  • Use intermediate variables for complex predicates/functions");
        
        printSeparator();
        
        printInfo("⚠️  Common Pitfalls to Avoid:");
        System.out.println("  • Don't reuse streams - create new ones for each operation");
        System.out.println("  • Avoid side effects in lambda expressions");
        System.out.println("  • Don't use parallel streams with shared mutable state");
        System.out.println("  • Be careful with infinite streams - always use limit()");
        System.out.println("  • Handle Optional properly - avoid get() without isPresent()");
        
        // Demonstrate good vs bad practices
        printInfo("Good vs Bad practices examples:");
        
        List<String> data = List.of("Java", "Python", "JavaScript", "Kotlin", "Scala");
        
        // Bad: Side effects in lambda
        List<String> badResults = new ArrayList<>();
        data.stream()
                .filter(s -> s.length() > 4)
                .forEach(s -> badResults.add(s.toUpperCase())); // Side effect!
        printResult("Bad: Side effects result", badResults);
        
        // Good: Pure functional approach
        List<String> goodResults = data.stream()
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .toList();
        printResult("Good: Pure functional result", goodResults);
        
        // Bad: Complex lambda
        List<String> complexProcessed = data.stream()
                .map(s -> {
                    if (s.length() > 6) {
                        return s.toUpperCase() + "(" + s.length() + ")";
                    } else if (s.length() > 4) {
                        return s.toLowerCase() + "[" + s.length() + "]";
                    } else {
                        return s + "-short";
                    }
                })
                .toList();
        printResult("Bad: Complex lambda result", complexProcessed);
        
        // Good: Extracted method
        List<String> cleanProcessed = data.stream()
                .map(this::processWord)
                .toList();
        printResult("Good: Extracted method result", cleanProcessed);
        
        // Performance consideration example
        List<Integer> numbers = IntStream.range(1, 1000000).boxed().toList();
        
        // Less efficient: boxing/unboxing
        double avgBoxed = numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        
        // More efficient: use primitive stream
        double avgPrimitive = numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        
        printResult("Both approaches give same result", avgBoxed == avgPrimitive);
        
        printTip("Measure performance before optimizing - premature optimization is often unnecessary");
        printWarning("Readability often trumps minor performance gains - optimize hot paths only");
    }
    
    // Helper method for good practice example
    private String processWord(String word) {
        if (word.length() > 6) {
            return word.toUpperCase() + "(" + word.length() + ")";
        } else if (word.length() > 4) {
            return word.toLowerCase() + "[" + word.length() + "]";
        } else {
            return word + "-short";
        }
    }
}