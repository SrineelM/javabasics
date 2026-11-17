package basics.mastery.java.collections.modern;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.SequencedSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import basics.mastery.collections.CollectionDemo;

/**
 * ============================================================================
 * JAVA COACH TUTORIAL: MODERN JAVA FEATURES MASTERCLASS (JDK 17 - 21)
 * ============================================================================
 * 
 * Target Audience: Intermediate -> Senior -> Staff Java Engineers
 * 
 * Objectives:
 *   1. Understand the landmark LTS milestones: Java 17 LTS -> Java 21 LTS.
 *   2. Master Sequenced Collections (JEP 431) - The biggest Collection API overhaul in 20 years!
 *   3. Master Pattern Matching for Switch (JEP 441) and Record Patterns (JEP 440).
 *   4. Leverage Virtual Threads (JEP 444) with Collections and Executors.
 *   5. Model robust Domain Hierarchies with Sealed Interfaces and Records (Algebraic Data Types).
 *   6. Distinguish True Immutability (List.of, List.copyOf) from Unmodifiable Views.
 *   7. Crack Senior/Staff Java Architecture and Modern Feature interviews.
 * 
 * JDK 17 to JDK 21 Feature Evolution:
 * 
 *   [Java 17 LTS]  • Sealed Classes & Interfaces (`sealed`, `permits`)
 *                  • Pattern Matching for instanceof (`if (obj instanceof String s)`)
 *                  • Enhanced Pseudo-Random Number Generators
 *                  • Strict Floating-Point by default
 *         │
 *   [Java 21 LTS]  • Sequenced Collections (SequencedCollection, SequencedSet, SequencedMap)
 *                  • Pattern Matching for switch (STANDARD - JEP 441)
 *                  • Record Patterns / Deconstruction (STANDARD - JEP 440)
 *                  • Virtual Threads (STANDARD - JEP 444)
 *                  • Scoped Values & Structured Concurrency (Preview)
 * 
 * @author Srineel with Copilot & Expert Java Coach
 * @version 3.0
 * @since Java 17 (Enhanced for Java 21)
 */
public class ModernJavaFeaturesDemo extends CollectionDemo {

    // =========================================================================
    // SEALED DOMAIN HIERARCHIES & RECORDS (ALGEBRAIC DATA TYPES)
    // =========================================================================

    /**
     * Sealed interface modeling financial payment methods.
     * Exhaustive: The compiler knows EXACTLY every permitted implementation!
     */
    public sealed interface PaymentMethod permits CreditCard, BankTransfer, CryptoWallet {}

    public record CreditCard(String cardNumber, String holderName, String expiry) implements PaymentMethod {}
    public record BankTransfer(String iban, String bic, String bankName) implements PaymentMethod {}
    public record CryptoWallet(String walletAddress, String network) implements PaymentMethod {}

    /**
     * Sealed Result monad for functional error handling.
     */
    public sealed interface OperationResult<T> permits OperationResult.Success, OperationResult.Failure {
        record Success<T>(T data, long timestamp) implements OperationResult<T> {}
        record Failure<T>(String errorCode, String errorMessage) implements OperationResult<T> {}
    }

    /**
     * Nested Records modeling e-commerce orders for record pattern deconstruction.
     */
    public record Customer(String id, String name, String email) {}
    public record Product(String sku, String title, double price) {}
    public record Order(String orderId, Customer customer, List<Product> products, PaymentMethod payment) {}

    public static void main(String[] args) {
        new ModernJavaFeaturesDemo().demonstrateAll();
    }

    // =========================================================================
    // MAIN DEMONSTRATION WORKFLOW
    // =========================================================================
    @Override
    public void demonstrateAll() {
        printHeader("MODERN JAVA FEATURES CAPSTONE MASTERCLASS (JDK 17 - 21)");

        // 1. Sequenced Collections (JEP 431) - The Star Feature of Java 21
        demonstrateSequencedCollectionsJava21();

        // 2. Pattern Matching for Switch & Guards (JEP 441)
        demonstratePatternMatchingForSwitchJava21();

        // 3. Record Patterns & Nested Deconstruction (JEP 440)
        demonstrateRecordPatternsJava21();

        // 4. Virtual Threads (JEP 444) & Throughput Scaling
        demonstrateVirtualThreadsJava21();

        // 5. Sealed Classes & Exhaustive Type Hierarchies (Java 17)
        demonstrateSealedHierarchies();

        // 6. True Immutability vs Unmodifiable Views
        demonstrateImmutableCollections();

        // 7. Text Blocks & String Formatting Idioms
        demonstrateTextBlocksAndFormatting();

        // 8. End-to-End Modern Enterprise Processing Pipeline
        demonstrateEndToEndModernPipeline();

        // 9. Top 20 Modern Java Interview Questions & Answers
        demonstrateInterviewMasterclass();

        printSectionComplete("Modern Java Features Capstone");
    }

    // =========================================================================
    // 1. SEQUENCED COLLECTIONS (JEP 431) - JAVA 21
    // =========================================================================
    /**
     * Demonstrates {@link SequencedCollection}, {@link SequencedSet}, and {@link SequencedMap}.
     * 
     * [COACH: WHY SEQUENCED COLLECTIONS WAS INTRODUCED]
     * For 25 years, Java Collections lacked a unified abstraction for collections with a defined encounter order:
     * ---------------------------------------------------------------------------------
     * Action             List              Deque            LinkedHashSet      SortedSet
     * ---------------------------------------------------------------------------------
     * First element      list.get(0)       deque.getFirst() set.iterator().next() set.first()
     * Last element       list.get(size-1)  deque.getLast()  [iterate to end]   set.last()
     * Reverse iteration  listIterator(size) descendingIterator() [impossible]  descendingSet()
     * ---------------------------------------------------------------------------------
     * JEP 431 in Java 21 fixes this permanently by introducing:
     *   - SequencedCollection: getFirst(), getLast(), addFirst(), addLast(), removeFirst(), removeLast(), reversed()
     *   - SequencedSet: extends Set, SequencedCollection
     *   - SequencedMap: putFirst(), putLast(), firstEntry(), lastEntry(), pollFirstEntry(), pollLastEntry(), reversed()
     */
    private void demonstrateSequencedCollectionsJava21() {
        printSubHeader("1. [JDK 21] Sequenced Collections (JEP 431) - The Unified Order API");

        printInfo("A. SequencedCollection with ArrayList and Deque:");
        List<String> list = new ArrayList<>(List.of("Alpha", "Beta", "Gamma"));
        printResult("List original", list);
        printResult("list.getFirst()", list.getFirst());
        printResult("list.getLast()", list.getLast());

        list.addFirst("Start");
        list.addLast("End");
        printResult("After addFirst('Start') and addLast('End')", list);

        // Live reversed view without cloning:
        SequencedCollection<String> reversedList = list.reversed();
        printResult("list.reversed() [Live reverse view]", reversedList);

        // B. SequencedSet with LinkedHashSet
        printInfo("B. SequencedSet with LinkedHashSet (Encounter-order preserved Set):");
        SequencedSet<String> orderedSet = new LinkedHashSet<>();
        orderedSet.add("Step_1");
        orderedSet.add("Step_2");
        orderedSet.add("Step_3");
        printResult("LinkedHashSet original", orderedSet);
        printResult("orderedSet.getFirst()", orderedSet.getFirst());
        printResult("orderedSet.getLast()", orderedSet.getLast());

        orderedSet.addFirst("Step_0");
        printResult("After orderedSet.addFirst('Step_0')", orderedSet);
        printResult("orderedSet.reversed()", orderedSet.reversed());

        // C. SequencedMap with LinkedHashMap
        printInfo("C. SequencedMap with LinkedHashMap:");
        SequencedMap<Integer, String> milestones = new LinkedHashMap<>();
        milestones.put(1, "Requirement Gathering");
        milestones.put(2, "Architecture Design");
        milestones.put(3, "Implementation");

        printResult("SequencedMap original", milestones);
        printResult("firstEntry()", milestones.firstEntry());
        printResult("lastEntry()", milestones.lastEntry());

        milestones.putFirst(0, "Discovery Call");
        milestones.putLast(4, "Production Deployment");
        printResult("After putFirst and putLast", milestones);

        printResult("milestones.reversed()", milestones.reversed());

        printTip("[COACH: JDK 21] Notice that list.reversed() and map.reversed() do NOT create a copy! They return a live, bidirectional view running in reverse order.");
    }

    // =========================================================================
    // 2. PATTERN MATCHING FOR SWITCH & GUARDS (JEP 441) - JAVA 21
    // =========================================================================
    /**
     * Demonstrates Pattern Matching for switch (JEP 441 - Standard in Java 21).
     * 
     * Key Innovations:
     * 1. Type Patterns: `case String s -> ...`
     * 2. Guarded Patterns: `case String s when s.length() > 5 -> ...`
     * 3. Null Handling: `case null -> ...` directly in the switch block!
     * 4. Sealed Class Exhaustiveness: Compiler proves all cases are handled. No `default` needed!
     */
    private void demonstratePatternMatchingForSwitchJava21() {
        printSubHeader("2. [JDK 21] Pattern Matching for switch & Guards (JEP 441)");

        printInfo("Evaluating diverse objects using modern pattern matching switch expressions:");

        List<Object> mixedItems = List.of(
            42,
            "Antigravity",
            "Java",
            3.14159,
            new CreditCard("4111-XXXX-XXXX-1111", "Alice Smith", "12/28"),
            Collections.emptyList()
        );

        for (Object item : mixedItems) {
            String description = switch (item) {
                // Type Pattern with 'when' Guard
                case Integer i when i > 0 -> "Positive Integer: " + i + " (Hex: 0x" + Integer.toHexString(i) + ")";
                case Integer i -> "Non-positive Integer: " + i;

                // String pattern with guard
                case String s when s.length() > 5 -> "Long String (" + s.length() + " chars): " + s;
                case String s -> "Short String: " + s;

                // Double pattern
                case Double d -> "Double: " + String.format("%.2f", d);

                // Record pattern inside switch
                case CreditCard cc -> "Payment Card for: " + cc.holderName();

                // Null handling
                case null -> "Explicit Null Object";

                // Fallback default
                default -> "Other type: " + item.getClass().getSimpleName();
            };
            System.out.println("     ➤ " + description);
        }

        printTip("[COACH: JDK 21] Prior to Java 21, switch (null) always threw NullPointerException before evaluating any case. In Java 21, you can write `case null -> ...` directly!");
    }

    // =========================================================================
    // 3. RECORD PATTERNS & NESTED DECONSTRUCTION (JEP 440) - JAVA 21
    // =========================================================================
    /**
     * Demonstrates Record Patterns (JEP 440 - Standard in Java 21).
     * 
     * Deconstructs record components directly in `instanceof` and `switch` statements,
     * including nested records!
     */
    private void demonstrateRecordPatternsJava21() {
        printSubHeader("3. [JDK 21] Record Patterns & Nested Deconstruction (JEP 440)");

        printInfo("Deconstructing nested Record structures directly in pattern matching:");

        Order testOrder = new Order(
            "ORD-90210",
            new Customer("CUST-1", "Alice Johnson", "alice@example.com"),
            List.of(new Product("SKU-LAPTOP", "MacBook Pro", 2499.00)),
            new CreditCard("4111-2222-3333-4444", "Alice Johnson", "05/27")
        );

        // Nested Record Pattern Matching with instanceof!
        if (testOrder instanceof Order(
                var orderId, 
                Customer(var custId, var custName, var email), 
                var products, 
                CreditCard(var cardNum, var holder, var exp)
            )) {
            System.out.println("     [DECONSTRUCTED ORDER]");
            System.out.println("     Order ID:       " + orderId);
            System.out.println("     Customer Name:  " + custName + " (" + email + ")");
            System.out.println("     Card Holder:    " + holder + " (Exp: " + exp + ")");
            System.out.println("     Items Count:    " + products.size());
        }

        printTip("[COACH: JDK 21] Notice you never called order.customer().name() or order.payment()! The nested record pattern deconstructed the entire hierarchy in one elegant statement.");
    }

    // =========================================================================
    // 4. VIRTUAL THREADS (JEP 444) - JAVA 21
    // =========================================================================
    /**
     * Demonstrates Virtual Threads (JEP 444 - Standard in Java 21).
     */
    private void demonstrateVirtualThreadsJava21() {
        printSubHeader("4. [JDK 21] Virtual Threads (JEP 444) - High-Throughput Lightweight Threads");

        printInfo("Simulating 50,000 concurrent IO-bound tasks using Virtual Threads:");

        final int totalTasks = 50_000;

        double timeTaken = measureTime(() -> {
            try (ExecutorService vExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < totalTasks; i++) {
                    vExecutor.submit(() -> {
                        try {
                            // Non-blocking sleep on virtual thread (unmounts from carrier thread)
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
            } // Auto-closes and waits for all virtual threads to complete!
        });

        printBenchmark(formatNumber(totalTasks) + " Virtual Threads completed in", timeTaken);
        printTip("[INTERVIEW TIP] Virtual threads solve the thread-per-request scaling bottleneck without forcing developers into complex reactive callback frameworks like WebFlux/RxJava.");
    }

    // =========================================================================
    // 5. SEALED CLASSES & EXHAUSTIVE SWITCH (JAVA 17)
    // =========================================================================
    /**
     * Demonstrates Sealed Hierarchies and compiler-verified exhaustiveness in switch.
     */
    private void demonstrateSealedHierarchies() {
        printSubHeader("5. Sealed Classes & Interfaces - Algebraic Data Types (Java 17)");

        printInfo("Dispatching payment processing across sealed PaymentMethod types:");

        List<PaymentMethod> payments = List.of(
            new CreditCard("4111-XXXX-XXXX-1234", "Bob", "01/29"),
            new BankTransfer("DE89370400440532013000", "DBEEDDFE", "Deutsche Bank"),
            new CryptoWallet("0x71C...352", "Ethereum")
        );

        for (PaymentMethod pm : payments) {
            // Notice: NO DEFAULT CASE IS NEEDED!
            // Because PaymentMethod is sealed permits CreditCard, BankTransfer, CryptoWallet,
            // the compiler mathematically guarantees all subclasses are handled!
            String receipt = switch (pm) {
                case CreditCard cc    -> "Processed Credit Card ending in: " + cc.cardNumber().substring(15);
                case BankTransfer bt  -> "Initiated SEPA transfer to IBAN: " + bt.iban();
                case CryptoWallet cw  -> "Broadcasting on " + cw.network() + " to: " + cw.walletAddress();
            };
            System.out.println("     " + receipt);
        }

        printTip("[COACH BEST PRACTICE] If a new payment type (e.g. PayPal) is added to the sealed interface, the compiler immediately produces a compile-time error at every switch statement that hasn't handled it!");
    }

    // =========================================================================
    // 6. TRUE IMMUTABILITY VS UNMODIFIABLE VIEWS
    // =========================================================================
    /**
     * Demonstrates the vital architectural difference between
     * `Collections.unmodifiableList` and `List.of()` / `List.copyOf()`.
     * 
     * [COACH: SENIOR INTERVIEW MUST-KNOW]
     * - Collections.unmodifiableList(list): Returns a VIEW wrapper around the original list.
     *   If someone modifies the underlying original list, the "unmodifiable" list changes too!
     * - List.of(...) / List.copyOf(...): TRULY IMMUTABLE.
     *   The collection owns its own compact array. Null elements are strictly forbidden.
     */
    private void demonstrateImmutableCollections() {
        printSubHeader("6. Immutability Masterclass: Unmodifiable Views vs Truly Immutable Lists");

        // Part A: The Trap of Collections.unmodifiableList (View)
        List<String> mutableBacking = new ArrayList<>(List.of("Data_A", "Data_B"));
        List<String> unmodifiableView = Collections.unmodifiableList(mutableBacking);

        printResult("Unmodifiable View initial", unmodifiableView);
        // Modifying the backing list mutates the view!
        mutableBacking.add("LEAKED_DATA");
        printResult("Unmodifiable View AFTER modifying original list", unmodifiableView);
        printWarning("[SECURITY RISK] Collections.unmodifiableList does NOT prevent changes if the backing reference is retained!");

        // Part B: Truly Immutable List (List.copyOf)
        List<String> trulyImmutable = List.copyOf(mutableBacking);
        mutableBacking.add("ANOTHER_LEAK");
        printResult("List.copyOf() remains completely unaffected", trulyImmutable);

        // Verification of immutable rejection:
        try {
            trulyImmutable.add("Illegal");
        } catch (UnsupportedOperationException e) {
            printInfo("trulyImmutable.add() correctly threw UnsupportedOperationException.");
        }
    }

    // =========================================================================
    // 7. TEXT BLOCKS & STRING FORMATTING (JAVA 15+)
    // =========================================================================
    /**
     * Demonstrates Text Blocks (`"""`).
     */
    private void demonstrateTextBlocksAndFormatting() {
        printSubHeader("7. Text Blocks & String Formatting Idioms");

        String query = """
                SELECT u.id, u.username, u.email, o.total_amount
                FROM users u
                INNER JOIN orders o ON u.id = o.user_id
                WHERE o.status = '%s'
                  AND o.created_at >= '%s'
                ORDER BY o.total_amount DESC;
                """.formatted("COMPLETED", LocalDate.now().minusDays(30));

        printInfo("Clean SQL query formatted via Text Block:\n" + query);
    }

    // =========================================================================
    // 8. END-TO-END MODERN PIPELINE INTEGRATION
    // =========================================================================
    /**
     * Demonstrates an end-to-end pipeline uniting:
     * - Sealed Result types
     * - Records
     * - Sequenced Collections
     * - Pattern matching switch
     * - Stream processing
     */
    private void demonstrateEndToEndModernPipeline() {
        printSubHeader("8. Capstone Architecture: End-to-End Modern Java Pipeline");

        // Input payload
        List<String> rawTransactions = List.of(
            "TXN_101:500.00:CREDIT",
            "TXN_102:-50.00:DEBIT",
            "INVALID_TRANSACTION",
            "TXN_103:1200.50:CREDIT"
        );

        record Transaction(String id, double amount, String type) {}

        // Step 1: Parse and validate using sealed OperationResult
        List<OperationResult<Transaction>> parsedResults = rawTransactions.stream()
                .map(raw -> {
                    String[] tokens = raw.split(":");
                    if (tokens.length != 3) {
                        return (OperationResult<Transaction>) new OperationResult.Failure<Transaction>("E001", "Malformed payload: " + raw);
                    }
                    try {
                        double amt = Double.parseDouble(tokens[1]);
                        return new OperationResult.Success<>(new Transaction(tokens[0], amt, tokens[2]), System.currentTimeMillis());
                    } catch (NumberFormatException e) {
                        return new OperationResult.Failure<Transaction>("E002", "Invalid number: " + tokens[1]);
                    }
                })
                .toList();

        // Step 2: Process using Pattern Matching switch and collect into a SequencedCollection
        SequencedCollection<Transaction> successfulTransactions = new ArrayDeque<>();

        for (var result : parsedResults) {
            switch (result) {
                case OperationResult.Success<Transaction>(Transaction txn, var time) -> {
                    successfulTransactions.addLast(txn);
                    System.out.println("     [APPROVED] " + txn.id() + " ($" + txn.amount() + ")");
                }
                case OperationResult.Failure<Transaction>(var code, var msg) -> {
                    System.out.println("     [REJECTED] Code: " + code + " -> " + msg);
                }
            }
        }

        printResult("Pipeline processed", successfulTransactions.size() + " valid transactions stored in SequencedCollection.");
        printResult("First approved transaction", successfulTransactions.getFirst());
        printResult("Last approved transaction", successfulTransactions.getLast());
    }

    // =========================================================================
    // 9. SENIOR INTERVIEW MASTERCLASS & PRODUCTION CHECKLIST
    // =========================================================================
    private void demonstrateInterviewMasterclass() {
        printSubHeader("9. Top 20 Modern Java (17-21) Interview Questions & Answers");

        printInfo("======================= TOP MODERN JAVA INTERVIEW Q&A =======================");
        System.out.println("""
          Q1: What problem do Sequenced Collections (JEP 431) solve in Java 21?
          A1: They provide a long-overdue unified interface hierarchy (SequencedCollection, SequencedSet,
              SequencedMap) for collections with encounter order, providing standardized getFirst(),
              getLast(), addFirst(), addLast(), and live reversed() views across List, Deque, LinkedHashSet,
              and LinkedHashMap.

          Q2: What is the difference between a sealed class and a final class?
          A2: A `final` class cannot be extended by ANY class. A `sealed` class CAN be extended, but ONLY
              by the explicit list of permitted subclasses named in its `permits` clause, enabling
              compile-time exhaustive pattern matching.

          Q3: Can a Record extend another class?
          A3: No. All Records in Java implicitly extend `java.lang.Record`. Because Java does not support
              multiple class inheritance, a record cannot extend any other class. However, records CAN
              implement any number of interfaces (including sealed interfaces).

          Q4: How does Pattern Matching for switch in Java 21 handle null?
          A4: In Java 21, `switch` can directly match null with `case null -> ...` or combined `case null, String s -> ...`.
              Previously, passing null to switch always threw NullPointerException immediately.

          Q5: What are Record Patterns (JEP 440) and why are they revolutionary?
          A5: Record patterns allow deconstructing record components directly in `instanceof` and `switch`,
              eliminating defensive getter chains. They support nested deconstruction, extracting deep fields
              in a single expressive statement.

          Q6: How do Virtual Threads differ from traditional Java platform threads?
          A6: Platform threads are 1:1 kernel threads requiring ~1MB stack memory. Virtual Threads are M:N
              user-mode threads managed by the JVM, allocated on the heap (~few hundred bytes). When blocked
              on IO, the JVM unmounts the virtual thread, freeing the carrier OS thread.

          Q7: What is Thread Pinning in Java 21?
          A7: Pinning occurs when a virtual thread cannot unmount from its carrier thread because it is
              executing inside a `synchronized` block/method or native code (JNI). Solution: Replace
              `synchronized` with `ReentrantLock`.

          Q8: What is the difference between Collections.unmodifiableList(list) and List.copyOf(list)?
          A8: `Collections.unmodifiableList` is an unmodifiable VIEW wrapper over the original backing list;
              mutations to the backing list are visible through the view. `List.copyOf` performs a defensive
              copy into a truly immutable collection.

          Q9: What is the purpose of the 'when' clause in a Java 21 switch statement?
          A9: The `when` clause specifies a pattern guard (a boolean condition). The case matches only if
              the pattern type matches AND the guard evaluates to true (e.g. `case String s when s.length() > 5`).

          Q10: Are Records completely immutable?
          A10: Records are shallowly immutable: their fields are private and final, with no setters. However,
               if a record component is a reference to a mutable object (like a java.util.ArrayList), the
               contents of that list can still be mutated unless defensive copying (List.copyOf) is used in the
               compact constructor.
        """);
    }
}
