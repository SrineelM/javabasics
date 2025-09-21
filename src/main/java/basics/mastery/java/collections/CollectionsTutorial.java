package basics.mastery.java.collections;

import java.util.Scanner;

import basics.mastery.java.collections.concurrent.ConcurrentCollectionsDemo;
import basics.mastery.java.collections.concurrent.MultithreadingExamplesDemo;
import basics.mastery.java.collections.functional.FunctionalProgrammingDemo;
import basics.mastery.java.collections.fundamentals.ListCollectionsDemo;
import basics.mastery.java.collections.fundamentals.MapCollectionsDemo;
import basics.mastery.java.collections.fundamentals.QueueCollectionsDemo;
import basics.mastery.java.collections.fundamentals.SetCollectionsDemo;
import basics.mastery.java.collections.modern.ModernJavaFeaturesDemo;
import basics.mastery.java.collections.patterns.BestPracticesDemo;
import basics.mastery.java.collections.performance.PerformanceAnalysisDemo;

/**
 * Java Collections Master Tutorial
 * 
 * A comprehensive educational project covering Java Collections Framework
 * from fundamentals to advanced patterns and modern Java features.
 * 
 * Features:
 * - Complete coverage of Java Collections API (Java 17-21)
 * - Interactive examples with detailed explanations
 * - Performance analysis and benchmarking
 * - Concurrent programming patterns
 * - Functional programming with Streams
 * - Modern Java features integration
 * - Best practices and design patterns
 * - Real-world scenarios and use cases
 * 
 * @author Srineel with Copilot
 * @version 2.0
 * @since Java 17
 */
public class CollectionsTutorial {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Main entry point for the Collections Tutorial
     * Provides an interactive menu system for exploring different topics
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
                    case 10 -> new MultithreadingExamplesDemo().demonstrateAll();
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
     * Displays the welcome message and tutorial overview
     */
    private static void printWelcomeMessage() {
        System.out.println("╔" + "═".repeat(80) + "╗");
        System.out.println("║" + " ".repeat(25) + "JAVA COLLECTIONS MASTER TUTORIAL" + " ".repeat(23) + "║");
        System.out.println("║" + " ".repeat(30) + "Java 17-21 Complete Guide" + " ".repeat(24) + "║");
        System.out.println("╚" + "═".repeat(80) + "╝");
        System.out.println();
        System.out.println("🎯 Learning Objectives:");
        System.out.println("   • Master all Java Collections Framework components");
        System.out.println("   • Understand performance characteristics and trade-offs");
        System.out.println("   • Learn concurrent programming with collections");
        System.out.println("   • Apply functional programming patterns");
        System.out.println("   • Explore modern Java features (17-21)");
        System.out.println("   • Follow best practices and design patterns");
        System.out.println();
        System.out.println("💡 Each section includes:");
        System.out.println("   • Interactive code examples");
        System.out.println("   • Detailed explanations and comments");
        System.out.println("   • Performance comparisons");
        System.out.println("   • Real-world use cases");
        System.out.println("   • Common pitfalls and solutions");
        System.out.println();
    }
    
    /**
     * Displays the main navigation menu
     */
    private static void displayMainMenu() {
        System.out.println("┌" + "─".repeat(78) + "┐");
        System.out.println("│" + " ".repeat(30) + "CHOOSE A TOPIC TO EXPLORE" + " ".repeat(23) + "│");
        System.out.println("├" + "─".repeat(78) + "┤");
        System.out.println("│  1. 📋 List Collections (ArrayList, LinkedList, Vector)              │");
        System.out.println("│  2. 🔢 Set Collections (HashSet, TreeSet, LinkedHashSet)             │");
        System.out.println("│  3. 🗺️  Map Collections (HashMap, TreeMap, LinkedHashMap)             │");
        System.out.println("│  4. 📤 Queue & Deque Collections (ArrayDeque, PriorityQueue)        │");
        System.out.println("│  5. 🔒 Concurrent Collections (Thread-safe implementations)          │");
        System.out.println("│  6. 🎯 Functional Programming (Streams, Lambda, Method References)   │");
        System.out.println("│  7. 🚀 Modern Java Features (Records, Pattern Matching, etc.)       │");
        System.out.println("│  8. ⚡ Performance Analysis & Benchmarking                           │");
        System.out.println("│  9. 💎 Best Practices & Design Patterns                             │");
        System.out.println("│ 10. 🧵 Multithreading Examples & Scenarios                          │");
        System.out.println("│                                                                      │");
        System.out.println("│  0. 🚪 Exit Tutorial                                                 │");
        System.out.println("└" + "─".repeat(78) + "┘");
        System.out.print("Enter your choice (0-10): ");
    }
    
    /**
     * Gets user input with error handling
     */
    private static int getChoice() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Waits for user to continue
     */
    private static void waitForContinue() {
        System.out.println();
        System.out.println("📌 Press Enter to return to main menu...");
        scanner.nextLine();
        System.out.println();
    }
    
    /**
     * Displays goodbye message
     */
    private static void printGoodbyeMessage() {
        System.out.println();
        System.out.println("╔" + "═".repeat(60) + "╗");
        System.out.println("║" + " ".repeat(20) + "Thank you for learning!" + " ".repeat(19) + "║");
        System.out.println("║" + " ".repeat(15) + "Java Collections mastery achieved! 🎉" + " ".repeat(7) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
        System.out.println();
        System.out.println("🔗 Additional Resources:");
        System.out.println("   • Oracle Java Documentation: https://docs.oracle.com/javase/");
        System.out.println("   • Java Collections Framework Guide");
        System.out.println("   • OpenJDK Source Code: https://github.com/openjdk/jdk");
        System.out.println();
    }
}