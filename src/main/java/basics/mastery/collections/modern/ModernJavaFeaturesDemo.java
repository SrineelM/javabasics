package basics.mastery.advanced;.collections.modern;

import io.mastery.collections.CollectionDemo;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Comprehensive demonstration of modern Java features with Collections.
 * 
 * This class showcases Java 8+ features that enhance collection usage:
 * - Java 9: Factory methods, enhanced Stream API
 * - Java 10: Local variable type inference (var)
 * - Java 11: String methods, HTTP Client collections
 * - Java 14+: Records, Pattern Matching, Text Blocks
 * - Java 16+: Pattern matching instanceof, Records
 * - Java 17+: Sealed classes
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class ModernJavaFeaturesDemo extends CollectionDemo {
    
    // Sealed classes defined at class level
    sealed interface Result<T> permits Success, Failure {
        T getValue();
    }
    
    record Success<T>(T value) implements Result<T> {
        @Override
        public T getValue() { return value; }
    }
    
    record Failure<T>(String error, T defaultValue) implements Result<T> {
        @Override
        public T getValue() { return defaultValue; }
    }
    
    sealed interface Data permits TextData, NumericData, CollectionData {}
    record TextData(String content) implements Data {}
    record NumericData(double value) implements Data {}
    record CollectionData(List<Object> items) implements Data {}
    
    sealed interface ProcessingState permits Processing, Completed, Failed {}
    record Processing(int progress) implements ProcessingState {}
    record Completed(String result) implements ProcessingState {}
    record Failed(String error) implements ProcessingState {}
    
    @Override
    public void demonstrateAll() {
        printHeader("MODERN JAVA FEATURES WITH COLLECTIONS");
        
        // Java version features
        demonstrateJava9Features();
        demonstrateJava10Features();
        demonstrateJava11Features();
        demonstrateJava14Features();
        demonstrateJava16Features();
        demonstrateJava17Features();
        
        // Advanced modern patterns
        demonstrateModernPatterns();
        demonstrateImmutableCollections();
        demonstrateModernStreamFeatures();
        
        // Best practices
        demonstrateBestPractices();
        
        printSectionComplete("Modern Java Features");
    }
    
    /**
     * Demonstrates Java 9 collection features
     */
    private void demonstrateJava9Features() {
        printSubHeader("Java 9 Features");
        
        printInfo("Java 9 introduced convenient factory methods and enhanced Stream API");
        
        // Collection factory methods
        printInfo("Factory methods for immutable collections:");
        
        List<String> languages = List.of("Java", "Python", "JavaScript", "Kotlin");
        printResult("List.of()", languages);
        
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        printResult("Set.of()", numbers);
        
        Map<String, Integer> scores = Map.of(
            "Alice", 95,
            "Bob", 87,
            "Charlie", 92
        );
        printResult("Map.of()", scores);
        
        // Map.ofEntries for larger maps
        Map<String, String> countries = Map.ofEntries(
            Map.entry("US", "United States"),
            Map.entry("UK", "United Kingdom"),
            Map.entry("DE", "Germany"),
            Map.entry("FR", "France"),
            Map.entry("JP", "Japan")
        );
        printResult("Map.ofEntries()", countries.keySet());
        
        // Enhanced Stream API
        printInfo("Stream enhancements:");
        
        // takeWhile - take elements while condition is true
        List<Integer> sequence = List.of(1, 2, 3, 4, 5, 4, 3, 2, 1);
        List<Integer> increasing = sequence.stream()
                .takeWhile(n -> n <= 3)
                .toList();
        printResult("takeWhile (n <= 3)", increasing);
        
        // dropWhile - skip elements while condition is true
        List<Integer> afterDrop = sequence.stream()
                .dropWhile(n -> n <= 3)
                .toList();
        printResult("dropWhile (n <= 3)", afterDrop);
        
        // iterate with condition
        List<Integer> fibonacci = Stream.iterate(new int[]{0, 1}, 
                arr -> arr[0] + arr[1] < 100,
                arr -> new int[]{arr[1], arr[0] + arr[1]})
                .map(arr -> arr[0])
                .toList();
        printResult("iterate with condition (Fibonacci < 100)", fibonacci);
        
        // ofNullable - create stream from potentially null value
        String nullableValue = Math.random() > 0.5 ? "Value" : null;
        List<String> fromNullable = Stream.ofNullable(nullableValue)
                .map(String::toUpperCase)
                .toList();
        printResult("Stream.ofNullable()", fromNullable);
        
        // Collectors.filtering (Java 9)
        Map<String, List<String>> longNamesByFirstLetter = languages.stream()
                .collect(Collectors.groupingBy(
                    name -> name.substring(0, 1),
                    Collectors.filtering(name -> name.length() > 4, Collectors.toList())
                ));
        printResult("Collectors.filtering", longNamesByFirstLetter);
        
        // Collectors.flatMapping (Java 9)
        List<List<String>> nestedWords = List.of(
            List.of("Java", "Programming"),
            List.of("Python", "Scripting"),
            List.of("JavaScript", "Web", "Development")
        );
        
        Map<Integer, List<String>> wordsByLength = nestedWords.stream()
                .collect(Collectors.groupingBy(
                    List::size,
                    Collectors.flatMapping(list -> list.stream(), Collectors.toList())
                ));
        printResult("Collectors.flatMapping", wordsByLength);
        
        printTip("Use factory methods for small immutable collections - they're memory efficient");
        printWarning("Collections created with factory methods are immutable!");
    }
    
    /**
     * Demonstrates Java 10 features (var keyword)
     */
    private void demonstrateJava10Features() {
        printSubHeader("Java 10 Features - Local Variable Type Inference");
        
        printInfo("The 'var' keyword reduces boilerplate for local variables");
        
        // Basic var usage with collections
        @SuppressWarnings("unused")
        var stringList = List.of("Java", "10", "Features");
        @SuppressWarnings("unused")
        var numberSet = Set.of(1, 2, 3, 4, 5);
        @SuppressWarnings("unused")
        var scoreMap = Map.of("Alice", 95, "Bob", 87);
        
        printResult("var stringList", stringList);
        printResult("var numberSet", numberSet);
        printResult("var scoreMap", scoreMap);
        
        // var in loops
        printInfo("var in enhanced for loops:");
        for (var item : stringList) {
            System.out.println("  Item: " + item);
        }
        
        // var with complex types
        var complexData = Map.of(
            "users", List.of("Alice", "Bob", "Charlie"),
            "admins", List.of("Admin1", "Admin2"),
            "guests", List.of("Guest1", "Guest2", "Guest3")
        );
        
        printResult("Complex var type", complexData.getClass().getSimpleName());
        
        // var with streams
        var processedData = stringList.stream()
                .filter(s -> s.length() > 2)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        
        printResult("var with streams", processedData);
        
        // var with anonymous types (diamond operator)
        var anonymousList = new ArrayList<String>() {{
            add("Anonymous");
            add("Collection");
            add("Initialization");
        }};
        printResult("var with anonymous type", anonymousList);
        
        // var guidelines demonstration
        printInfo("var guidelines:");
        
        // Good: Type is obvious from right side
        var numbers = List.of(1, 2, 3, 4, 5);
        var upperWords = stringList.stream().map(String::toUpperCase).toList();
        
        // Avoid: Type is not obvious
        // var unclear = getData(); // What type does getData() return?
        // var x = getProcessor().process(); // Too ambiguous
        
        printResult("Clear var usage", numbers.size() + " numbers, " + upperWords.size() + " words");
        
        printTip("Use var when the type is obvious from the right-hand side");
        printWarning("Avoid var when it makes code less readable");
    }
    
    /**
     * Demonstrates Java 11 features
     */
    private void demonstrateJava11Features() {
        printSubHeader("Java 11 Features");
        
        printInfo("Java 11 enhanced String methods and collection processing");
        
        // String methods useful with collections
        List<String> messyStrings = List.of("  Java  ", "", "   ", "Python", "\t\n", "JavaScript   ");
        
        // isBlank() - checks if string is empty or contains only whitespace
        var nonBlankStrings = messyStrings.stream()
                .filter(s -> !s.isBlank())
                .toList();
        printResult("Non-blank strings", nonBlankStrings);
        
        // strip() - removes leading and trailing whitespace (Unicode-aware)
        var trimmedStrings = messyStrings.stream()
                .filter(s -> !s.isBlank())
                .map(String::strip)
                .toList();
        printResult("Stripped strings", trimmedStrings);
        
        // lines() - splits string into stream of lines
        String multilineText = "Line 1\nLine 2\nLine 3\n\nLine 5";
        var lines = multilineText.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());
        printResult("Non-empty lines", lines);
        
        // repeat() - repeats string n times
        var repeatedPatterns = List.of("*", "-", "+").stream()
                .map(pattern -> pattern.repeat(5))
                .collect(Collectors.toList());
        printResult("Repeated patterns", repeatedPatterns);
        
        // Collection.toArray(IntFunction) - type-safe array conversion
        String[] stringArray = trimmedStrings.toArray(String[]::new);
        printResult("Type-safe array conversion", stringArray.length + " elements");
        
        // Predicate.not() - negation method reference
        var nonEmptyTrimmed = trimmedStrings.stream()
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toList());
        printResult("Using Predicate.not()", nonEmptyTrimmed);
        
        // Optional.isEmpty() (Java 11)
        List<Optional<String>> optionals = List.of(
            Optional.of("Present"),
            Optional.empty(),
            Optional.of("Also Present"),
            Optional.empty()
        );
        
        var presentValues = optionals.stream()
                .filter(Predicate.not(Optional::isEmpty))
                .map(Optional::get)
                .collect(Collectors.toList());
        printResult("Present values using isEmpty()", presentValues);
        
        printTip("Use String.strip() instead of trim() for Unicode-aware whitespace removal");
        printInfo("Predicate.not() improves readability for method reference negation");
    }
    
    /**
     * Demonstrates Java 14+ features (Records, Pattern Matching)
     */
    private void demonstrateJava14Features() {
        printSubHeader("Java 14+ Features - Records and Text Blocks");
        
        printInfo("Records provide concise syntax for data classes, Text Blocks for multiline strings");
        
        // Records with collections
        record Person(String name, int age, List<String> hobbies) {
            // Defensive copy in compact constructor
            public Person {
                hobbies = List.copyOf(hobbies);
            }
            
            // Custom methods
            public boolean hasHobby(String hobby) {
                return hobbies.contains(hobby);
            }
            
            public int hobbyCount() {
                return hobbies.size();
            }
        }
        
        List<Person> people = List.of(
            new Person("Alice", 28, List.of("Reading", "Hiking", "Photography")),
            new Person("Bob", 34, List.of("Gaming", "Cooking")),
            new Person("Charlie", 22, List.of("Music", "Sports", "Travel", "Coding"))
        );
        
        printResult("People with records", people.size() + " people");
        
        // Records in stream operations
        var bookLovers = people.stream()
                .filter(person -> person.hasHobby("Reading"))
                .map(Person::name)
                .collect(Collectors.toList());
        printResult("Book lovers", bookLovers);
        
        // Grouping by record methods
        var peopleByHobbyCount = people.stream()
                .collect(Collectors.groupingBy(Person::hobbyCount));
        printResult("People by hobby count", peopleByHobbyCount);
        
        // Text blocks for complex data
        var jsonTemplate = """
                {
                    "users": [
                        {
                            "name": "%s",
                            "age": %d,
                            "hobbies": %s
                        }
                    ]
                }
                """;
        
        // Using text blocks with collections
        List<String> jsonObjects = people.stream()
                .limit(2)
                .map(person -> jsonTemplate.formatted(
                    person.name(), 
                    person.age(), 
                    person.hobbies()))
                .collect(Collectors.toList());
        
        printResult("JSON objects count", jsonObjects.size());
        System.out.println("  Sample JSON:\n" + jsonObjects.get(0));
        
        // Switch expressions with collections (Java 14)
        var categorizedPeople = people.stream()
                .map(person -> switch (person.age() / 10) {
                    case 2 -> "Twenties: " + person.name();
                    case 3 -> "Thirties: " + person.name();
                    case 4 -> "Forties: " + person.name();
                    default -> "Other: " + person.name();
                })
                .collect(Collectors.toList());
        printResult("Categorized people", categorizedPeople);
        
        printTip("Use Records for immutable data objects - they're perfect with collections");
        printInfo("Text blocks eliminate the need for complex string concatenation");
    }
    
    /**
     * Demonstrates Java 16+ features (Pattern Matching, Records enhancements)
     */
    private void demonstrateJava16Features() {
        printSubHeader("Java 16+ Features - Pattern Matching and Stream enhancements");
        
        printInfo("Pattern matching with instanceof and Stream.toList()");
        
        // Pattern matching with instanceof (Java 16)
        List<Object> mixedObjects = List.of(
            "String value",
            42,
            List.of(1, 2, 3),
            Map.of("key", "value"),
            3.14159
        );
        
        List<String> descriptions = mixedObjects.stream()
                .map(obj -> {
                    if (obj instanceof String s) {
                        return "String of length " + s.length() + ": " + s;
                    } else if (obj instanceof Integer i) {
                        return "Integer: " + i + " (squared: " + (i * i) + ")";
                    } else if (obj instanceof List<?> list) {
                        return "List with " + list.size() + " elements";
                    } else if (obj instanceof Map<?, ?> map) {
                        return "Map with " + map.size() + " entries";
                    } else if (obj instanceof Double d) {
                        return "Double: " + String.format("%.2f", d);
                    } else {
                        return "Unknown type: " + obj.getClass().getSimpleName();
                    }
                })
                .collect(Collectors.toList());
        
        printResult("Pattern matching descriptions", descriptions.size() + " descriptions");
        descriptions.forEach(desc -> System.out.println("  " + desc));
        
        // Stream.toList() (Java 16) vs collect(Collectors.toList())
        List<String> strings = List.of("java", "pattern", "matching", "streams");
        
        // Old way
        List<String> upperOld = strings.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        
        // New way (Java 16+)
        List<String> upperNew = strings.stream()
                .map(String::toUpperCase)
                .toList();
        
        printResult("Old collect way", upperOld);
        printResult("New toList() way", upperNew);
        printResult("Both are equal", upperOld.equals(upperNew));
        
        // Records with pattern matching
        record Shape(String type, double area) {}
        
        List<Shape> shapes = List.of(
            new Shape("Circle", 3.14159),
            new Shape("Square", 16.0),
            new Shape("Triangle", 6.0),
            new Shape("Rectangle", 24.0)
        );
        
        // Pattern matching in filter
        var largeShapes = shapes.stream()
                .filter(shape -> {
                    if (shape instanceof Shape s && s.area() > 10.0) {
                        return true;
                    }
                    return false;
                })
                .map(Shape::type)
                .collect(Collectors.toList());
        
        printResult("Large shapes (area > 10)", largeShapes);
        
        printTip("Use toList() instead of collect(Collectors.toList()) for cleaner code");
        printInfo("Pattern matching with instanceof reduces boilerplate and improves type safety");
    }
    
    /**
     * Demonstrates Java 17+ features (Enhanced pattern matching)
     */
    private void demonstrateJava17Features() {
        printSubHeader("Java 17+ Features - Enhanced Switch and Pattern Matching");
        
        printInfo("Enhanced switch expressions with pattern matching");
        
        // Using the class-level sealed classes
        List<Result<String>> results = List.of(
            new Success<>("Operation completed"),
            new Failure<>("Network error", "default"),
            new Success<>("Data processed"),
            new Failure<>("Timeout", "fallback")
        );
        
        // Pattern matching with sealed classes in switch (Java 17+)
        List<String> processedResults = results.stream()
                .map(result -> switch (result) {
                    case Success<String> s -> "✓ " + s.value();
                    case Failure<String> f -> "✗ " + f.error() + " (using: " + f.defaultValue() + ")";
                    // No default needed - compiler knows all cases are covered
                })
                .collect(Collectors.toList());
        
        printResult("Processed results", processedResults.size() + " items");
        processedResults.forEach(result -> System.out.println("  " + result));
        
        // Enhanced switch with multiple values
        List<String> days = List.of("MONDAY", "FRIDAY", "SATURDAY", "WEDNESDAY", "SUNDAY");
        
        Map<String, String> dayCategories = days.stream()
                .collect(Collectors.toMap(
                    day -> day,
                    day -> switch (day) {
                        case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
                        case "SATURDAY", "SUNDAY" -> "Weekend";
                        default -> "Unknown";
                    }
                ));
        
        printResult("Day categories", dayCategories);
        
        // Sealed classes ensure exhaustive pattern matching
        long successCount = results.stream()
                .mapToLong(result -> switch (result) {
                    case Success<String> s -> 1L;
                    case Failure<String> f -> 0L;
                    // No default needed - compiler knows all cases are covered
                })
                .sum();
        
        printResult("Success count", successCount);
        printResult("Total results", results.size());
        
        // Complex pattern matching example using class-level sealed types
        List<Data> mixedData = List.of(
            new TextData("Hello World"),
            new NumericData(42.5),
            new CollectionData(List.of(1, 2, 3)),
            new TextData("Java 17"),
            new NumericData(100.0)
        );
        
        Map<String, Long> dataTypeCounts = mixedData.stream()
                .collect(Collectors.groupingBy(
                    data -> switch (data) {
                        case TextData t -> "Text";
                        case NumericData n -> "Numeric";
                        case CollectionData c -> "Collection";
                        // No default needed - sealed class is exhaustive
                    },
                    Collectors.counting()
                ));
        
        printResult("Data type counts", dataTypeCounts);
        
        printTip("Sealed classes with pattern matching provide type-safe, exhaustive handling");
        printInfo("Use sealed classes to model closed hierarchies with known subtypes");
    }
    
    /**
     * Demonstrates modern patterns combining multiple Java features
     */
    private void demonstrateModernPatterns() {
        printSubHeader("Modern Java Patterns");
        
        printInfo("Combining modern Java features for elegant solutions");
        
        // Builder pattern with records and var
        record Configuration(String name, List<String> features, Map<String, String> properties) {}
        
        // Simple configuration creation
        var config = new Configuration(
            "MyApp",
            List.of("Authentication", "Caching", "Monitoring"),
            Map.of("version", "1.0", "environment", "production")
        );
        
        printResult("Modern configuration", config);
        
        printResult("Modern builder pattern", config);
        
        // Functional error handling with Optional and streams
        List<String> userInputs = List.of("123", "abc", "456", "", "789", "xyz");
        
        var validNumbers = userInputs.stream()
                .map(this::parseIntSafely)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        
        printResult("Valid numbers from input", validNumbers);
        
        // Data transformation pipeline
        record Employee(String name, String department, int salary) {}
        
        var employees = List.of(
            new Employee("Alice", "Engineering", 95000),
            new Employee("Bob", "Engineering", 87000),
            new Employee("Charlie", "Marketing", 72000),
            new Employee("Diana", "Sales", 78000)
        );
        
        // Modern transformation pipeline with var, records, and streams
        var departmentStats = employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::department,
                    Collectors.teeing(
                        Collectors.counting(),
                        Collectors.averagingInt(Employee::salary),
                        (count, avgSalary) -> Map.of(
                            "count", count.intValue(),
                            "avgSalary", avgSalary.intValue()
                        )
                    )
                ));
        
        printResult("Department statistics", departmentStats);
        
        printTip("Combine Records, var, and Streams for clean data processing pipelines");
        printInfo("Modern Java enables more expressive and maintainable code");
    }
    
    // Helper method for safe parsing
    private Optional<Integer> parseIntSafely(String str) {
        try {
            return str.isBlank() ? Optional.empty() : Optional.of(Integer.parseInt(str.strip()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Demonstrates immutable collections and defensive programming
     */
    private void demonstrateImmutableCollections() {
        printSubHeader("Immutable Collections and Defensive Programming");
        
        printInfo("Modern Java provides better support for immutable collections");
        
        // Factory methods create immutable collections
        var immutableList = List.of("Java", "Immutable", "Collections");
        @SuppressWarnings("unused")
        var immutableSet = Set.of(1, 2, 3, 4, 5);
        @SuppressWarnings("unused")
        var immutableMap = Map.of("key1", "value1", "key2", "value2");
        
        printResult("Immutable list", immutableList);
        
        try {
            immutableList.add("This will fail");
        } catch (UnsupportedOperationException e) {
            printInfo("✓ Immutable list correctly prevents modification");
        }
        
        // copyOf methods for defensive copying
        List<String> mutableSource = new ArrayList<>(List.of("Mutable", "Source", "List"));
        List<String> defensiveCopy = List.copyOf(mutableSource);
        
        printResult("Original mutable list", mutableSource);
        printResult("Defensive copy", defensiveCopy);
        
        // Modify original - copy remains unchanged
        mutableSource.add("Modified");
        printResult("After modifying original", mutableSource);
        printResult("Defensive copy unchanged", defensiveCopy);
        
        // Building immutable collections
        var builderList = Stream.of("Stream", "To", "Immutable", "List")
                .collect(Collectors.toUnmodifiableList());
        
        var builderSet = IntStream.range(1, 6)
                .boxed()
                .collect(Collectors.toUnmodifiableSet());
        
        printResult("Stream to unmodifiable list", builderList);
        printResult("Stream to unmodifiable set", builderSet);
        
        // Immutable collections in records
        record ImmutableDataHolder(List<String> items, Set<Integer> numbers) {
            public ImmutableDataHolder {
                items = List.copyOf(items);
                numbers = Set.copyOf(numbers);
            }
        }
        
        var dataHolder = new ImmutableDataHolder(
            List.of("Item1", "Item2", "Item3"),
            Set.of(10, 20, 30)
        );
        
        printResult("Immutable record data", dataHolder);
        
        try {
            dataHolder.items().add("This will fail");
        } catch (UnsupportedOperationException e) {
            printInfo("✓ Record with immutable collections prevents modification");
        }
        
        printTip("Use List.copyOf(), Set.copyOf(), Map.copyOf() for defensive copying");
        printWarning("Factory methods (List.of, etc.) create truly immutable collections");
    }
    
    /**
     * Demonstrates modern Stream API enhancements
     */
    private void demonstrateModernStreamFeatures() {
        printSubHeader("Modern Stream API Enhancements");
        
        printInfo("Recent Java versions added powerful Stream operations");
        
        // Teeing collector (Java 12) - fork stream into two collectors
        List<Integer> numbers = IntStream.range(1, 21).boxed().toList();
        
        var sumAndCount = numbers.stream()
                .collect(Collectors.teeing(
                    Collectors.summingInt(Integer::intValue),
                    Collectors.counting(),
                    (sum, count) -> Map.of("sum", sum, "count", count.intValue())
                ));
        
        printResult("Teeing collector (sum and count)", sumAndCount);
        
        // Collectors.filtering and flatMapping (Java 9)
        List<String> words = List.of("Java", "Stream", "Collectors", "Modern", "Features");
        
        var groupedByLength = words.stream()
                .collect(Collectors.groupingBy(
                    String::length,
                    Collectors.filtering(
                        word -> word.contains("a"),
                        Collectors.toList()
                    )
                ));
        
        printResult("Grouped by length, filtered by 'a'", groupedByLength);
        
        // Stream.concat for combining streams
        var stream1 = List.of("A", "B", "C").stream();
        var stream2 = List.of("D", "E", "F").stream();
        var combined = Stream.concat(stream1, stream2).toList();
        
        printResult("Combined streams", combined);
        
        // Multiple stream operations
        List<List<String>> nestedLists = List.of(
            List.of("Java", "Programming"),
            List.of("Modern", "Features"),
            List.of("Stream", "API", "Enhancements")
        );
        
        var flatMapped = nestedLists.stream()
                .collect(Collectors.flatMapping(
                    list -> list.stream(),
                    Collectors.groupingBy(String::length)
                ));
        
        printResult("Flat mapped and grouped", flatMapped);
        
        // Advanced Optional operations (Java 9+)
        List<Optional<String>> optionals = List.of(
            Optional.of("Present1"),
            Optional.empty(),
            Optional.of("Present2"),
            Optional.empty(),
            Optional.of("Present3")
        );
        
        // flatMap with Optional.stream()
        var presentValues = optionals.stream()
                .flatMap(Optional::stream)
                .toList();
        
        printResult("Present values using flatMap", presentValues);
        
        // ifPresentOrElse (Java 9)
        optionals.forEach(opt -> 
            opt.ifPresentOrElse(
                value -> System.out.println("  Present: " + value),
                () -> System.out.println("  Empty optional")
            )
        );
        
        printTip("Use Collectors.teeing to fork streams into multiple collectors");
        printInfo("Modern Optional methods improve null handling in streams");
    }
    
    /**
     * Demonstrates best practices for modern Java with collections
     */
    private void demonstrateBestPractices() {
        printSubHeader("Modern Java Best Practices with Collections");
        
        printInfo("🎯 Modern Java Collection Guidelines:");
        System.out.println("  • Use var for local variables when type is obvious");
        System.out.println("  • Prefer List.of(), Set.of(), Map.of() for small immutable collections");
        System.out.println("  • Use Records for data transfer objects");
        System.out.println("  • Apply pattern matching for type-safe operations");
        System.out.println("  • Leverage sealed classes for controlled hierarchies");
        
        printSeparator();
        
        printInfo("⚡ Performance Best Practices:");
        System.out.println("  • Use toList() instead of collect(Collectors.toList())");
        System.out.println("  • Prefer immutable collections for thread safety");
        System.out.println("  • Use appropriate Stream operations (takeWhile, dropWhile)");
        System.out.println("  • Apply defensive copying with List.copyOf()");
        System.out.println("  • Consider parallel streams for CPU-intensive operations");
        
        printSeparator();
        
        printInfo("🚨 Common Pitfalls to Avoid:");
        System.out.println("  • Don't overuse var - maintain readability");
        System.out.println("  • Remember that factory method collections are immutable");
        System.out.println("  • Avoid pattern matching when simple instanceof suffices");
        System.out.println("  • Don't mix mutable and immutable collection patterns");
        System.out.println("  • Be aware of null handling with modern Optional methods");
        
        // Demonstrate good practices
        printInfo("Modern code example:");
        
        // Good modern Java style
        record UserData(String username, List<String> roles, Map<String, String> metadata) {
            public UserData {
                roles = List.copyOf(roles);
                metadata = Map.copyOf(metadata);
            }
        }
        
        var users = List.of(
            new UserData("alice", List.of("admin", "user"), Map.of("team", "engineering")),
            new UserData("bob", List.of("user"), Map.of("team", "marketing")),
            new UserData("charlie", List.of("user", "reviewer"), Map.of("team", "engineering"))
        );
        
        var engineeringAdmins = users.stream()
                .filter(user -> user.metadata().get("team").equals("engineering"))
                .filter(user -> user.roles().contains("admin"))
                .map(UserData::username)
                .toList();
        
        printResult("Engineering admins", engineeringAdmins);
        
        // Using the class-level sealed classes for state management
        var states = List.of(
            new Processing(50),
            new Completed("Success"),
            new Failed("Network error"),
            new Processing(75)
        );
        
        var summary = states.stream()
                .collect(Collectors.groupingBy(
                    state -> switch (state) {
                        case Processing p -> "In Progress";
                        case Completed c -> "Completed";
                        case Failed f -> "Failed";
                        default -> "Unknown"; // Add default for safety
                    },
                    Collectors.counting()
                ));
        
        printResult("State summary", summary);
        
        printTip("Combine modern features thoughtfully - don't use them just because they're new");
        printWarning("Always consider team familiarity with newer language features");
    }
}