package basics.mastery.collections;

import basics.mastery.collections.fundamentals.ListCollectionsDemo;
import basics.mastery.collections.fundamentals.SetCollectionsDemo;
import basics.mastery.collections.fundamentals.MapCollectionsDemo;
import basics.mastery.collections.fundamentals.QueueCollectionsDemo;
import basics.mastery.collections.concurrent.ConcurrentCollectionsDemo;
import basics.mastery.collections.functional.FunctionalProgrammingDemo;
import basics.mastery.collections.modern.ModernJavaFeaturesDemo;
import basics.mastery.collections.performance.PerformanceAnalysisDemo;
import basics.mastery.collections.patterns.BestPracticesDemo;

import java.util.Scanner;

/**
 * Java Collections Framework Tutorial
 * 
 * A comprehensive educational program for mastering Java Collections.
 * This tutorial complements the existing concurrency examples in this project
 * by focusing specifically on the Collections Framework.
 * 
 * Features:
 * - Complete coverage of Java Collections API (Java 17-21)
 * - Interactive examples with detailed explanations
 * - Performance analysis and benchmarking
 * - Functional programming with Streams
 * - Modern Java features integration
 * - Best practices and design patterns
 * 
 * Note: This runs alongside the existing concurrency tutorial
 * in the same project without interference.
 * 
 * @author Srineel with Copilot
 * @version 2.0
 * @since Java 17
 */
public class CollectionsTutorial {
    
    // [coach] A static Scanner on System.in is fine for a single-threaded CLI,
    // but note: (1) never close it — closing System.in breaks later reads;
    // (2) it is not thread-safe — do not share across threads.
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Entry point for the Collections Tutorial
     * Can be run separately from the main concurrency tutorial
     */
    public static void main(String[] args) {
        printWelcomeMessage();
        
        // [coach] while(true) + System.exit(0) on case 0 is fine for a CLI, but
        // System.exit bypasses finally blocks and shutdown hooks. For a library-
        // friendly design, prefer a `boolean running` flag and `return` from main.
        // Kept as-is to preserve original behavior; flagged for interview.
        while (true) {
            displayMainMenu();
            int choice = getChoice();
            
            try {
                switch (choice) {
                    case 1 -> new ListCollectionsDemo().demonstrateAll();
                    case 2 -> new SetCollectionsDemo().demonstrateAll();
                    case 3 -> new MapCollectionsDemo().demonstrateAll();
                    case 4 -> new QueueCollectionsDemo().demonstrateAll();
                    case 5 -> new ConcurrentCollectionsDemo().demonstrateAll();
                    case 6 -> new FunctionalProgrammingDemo().demonstrateAll();
                    case 7 -> new ModernJavaFeaturesDemo().demonstrateAll();
                    case 8 -> new PerformanceAnalysisDemo().demonstrateAll();
                    case 9 -> new BestPracticesDemo().demonstrateAll();
                    case 0 -> {
                        printGoodbyeMessage();
                        // [coach] System.exit(0) is redundant here because it is
                        // the only terminal path out of the loop; cleaner to
                        // `return;` from main so finally blocks run. Kept as-is.
                        System.exit(0);
                    }
                    default -> System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                // [coach] Catching Exception here is broad; but for a demo CLI
                // that must not die on one failing demo, it is the correct trade-
                // off. Prefer logging the full stack in production, not printing
                // to stderr and continuing.
                System.err.println("❌ Error executing example: " + e.getMessage());
                e.printStackTrace();
            }
            
            waitForContinue();
        }
    }
    
    /**
     * Utility method to run collections tutorial from main concurrency application
     */
    public static void runCollectionsTutorial() {
        System.out.println("\n🔄 Launching Collections Framework Tutorial...\n");
        // [coach] Re-entering main() from another class's main re-runs welcome,
        // re-enters the menu loop, and blocks until exit — effectively a nested
        // CLI. This is fine for a demo, but do NOT call this from a non-main
        // thread that expects to regain control; System.exit inside will kill
        // the JVM. Also note: calling main() directly (not via new Thread) means
        // the scanner is shared with any outer UI — potential contention.
        main(new String[]{});
    }
    
    private static void printWelcomeMessage() {
        System.out.println("╔" + "═".repeat(80) + "╗");
        // [coach] Hardcoded padding is brittle: changing the title breaks the
        // alignment. For interview-quality output, compute padding from the
        // title length (see CollectionDemo#printHeader). Kept as-is for scope.
        System.out.println("║" + " ".repeat(20) + "JAVA COLLECTIONS FRAMEWORK TUTORIAL" + " ".repeat(24) + "║");
        System.out.println("║" + " ".repeat(25) + "Comprehensive Learning Guide" + " ".repeat(27) + "║");
        System.out.println("╚" + "═".repeat(80) + "╝");
        System.out.println();
        System.out.println("🎯 What You'll Learn:");
        System.out.println("   • Master all Java Collections Framework components");
        System.out.println("   • Understand performance characteristics and trade-offs");
        System.out.println("   • Apply functional programming patterns with collections");
        System.out.println("   • Explore modern Java features (17-21)");
        System.out.println("   • Follow best practices and design patterns");
        System.out.println();
        System.out.println("💡 This tutorial runs alongside the existing concurrency examples");
        System.out.println("   and focuses specifically on Collections Framework mastery.");
        System.out.println();
    }
    
    private static void displayMainMenu() {
        System.out.println("┌" + "─".repeat(78) + "┐");
        System.out.println("│" + " ".repeat(25) + "COLLECTIONS TUTORIAL MENU" + " ".repeat(27) + "│");
        System.out.println("├" + "─".repeat(78) + "┤");
        // [coach] Lines are hand-padded to 70 chars; any edit to text risks
        // misalignment. A small helper `padRight(s, n)` would be cleaner.
        System.out.println("│  1. 📋 List Collections (ArrayList, LinkedList, Vector)              │");
        System.out.println("│  2. 🔢 Set Collections (HashSet, TreeSet, LinkedHashSet)             │");
        System.out.println("│  3. 🗺️  Map Collections (HashMap, TreeMap, LinkedHashMap)             │");
        System.out.println("│  4. 📤 Queue & Deque Collections                                    │");
        System.out.println("│  5. 🔒 Concurrent Collections (Thread-safe implementations)          │");
        System.out.println("│  6. 🎯 Functional Programming (Streams, Lambda, Method References)   │");
        System.out.println("│  7. 🚀 Modern Java Features (Records, Pattern Matching, etc.)       │");
        System.out.println("│  8. ⚡ Performance Analysis & Benchmarking                           │");
        System.out.println("│  9. 💎 Best Practices & Design Patterns                             │");
        System.out.println("│                                                                      │");
        System.out.println("│  0. 🚪 Exit Collections Tutorial                                     │");
        System.out.println("└" + "─".repeat(78) + "┘");
        System.out.print("Enter your choice (0-9): ");
    }
    
    private static int getChoice() {
        try {
            String input = scanner.nextLine().trim();
            // [coach] nextLine() on EOF returns null (e.g., input redirected from
            // an exhausted pipe). Calling trim() on null throws NPE, not
            // NumberFormatException. Guard for null.
            // [coach:fix] guard against null input from exhausted stream.
            if (input == null) {
                return -1;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void waitForContinue() {
        System.out.println();
        System.out.println("📌 Press Enter to return to menu...");
        // [coach] Scanner.nextLine() can also return null on EOF; the previous
        // getChoice() consumed the line, so this is usually fine, but a null
        // return here would silently fall through (no NPE because we discard it).
        scanner.nextLine();
        System.out.println();
    }
    
    private static void printGoodbyeMessage() {
        System.out.println();
        System.out.println("╔" + "═".repeat(60) + "╗");
        // [coach] Emoji width is not 1 char in all terminals; the emoji "🎉"
        // is a surrogate pair (2 UTF-16 chars) and renders as 2 columns wide,
        // so padding math here is approximate. Acceptable for demo output.
        System.out.println("║" + " ".repeat(10) + "Collections Framework Tutorial Complete!" + " ".repeat(9) + "║");
        System.out.println("║" + " ".repeat(15) + "Thank you for learning! 🎉" + " ".repeat(19) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
        System.out.println();
        System.out.println("💡 You can also explore the concurrency examples in this project!");
        System.out.println("   Run: java basics.mastery.MainApplication");
        System.out.println();
    }
}