package basics.mastery.advanced;.collections;

import io.mastery.collections.fundamentals.*;
import io.mastery.collections.concurrent.*;
import io.mastery.collections.functional.*;
import io.mastery.collections.modern.*;
import io.mastery.collections.performance.*;
import io.mastery.collections.patterns.*;

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
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public class CollectionsTutorial {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Entry point for the Collections Tutorial
     * Can be run separately from the main concurrency tutorial
     */
    public static void main(String[] args) {
        printWelcomeMessage();
        
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
                        System.exit(0);
                    }
                    default -> System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (Exception e) {
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
        main(new String[]{});
    }
    
    private static void printWelcomeMessage() {
        System.out.println("╔" + "═".repeat(80) + "╗");
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
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void waitForContinue() {
        System.out.println();
        System.out.println("📌 Press Enter to return to menu...");
        scanner.nextLine();
        System.out.println();
    }
    
    private static void printGoodbyeMessage() {
        System.out.println();
        System.out.println("╔" + "═".repeat(60) + "╗");
        System.out.println("║" + " ".repeat(10) + "Collections Framework Tutorial Complete!" + " ".repeat(9) + "║");
        System.out.println("║" + " ".repeat(15) + "Thank you for learning! 🎉" + " ".repeat(19) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
        System.out.println();
        System.out.println("💡 You can also explore the concurrency examples in this project!");
        System.out.println("   Run: java io.mastery.MainApplication");
        System.out.println();
    }
}