package basics.mastery;

/**
 * Main Application for Java Concurrency Training Program
 *
 * This is the entry point for exploring various concurrency concepts and demonstrations.
 * The program provides an interactive menu to access different modules covering
 * everything from basic threading to modern Java concurrency features.
 *
 * @author Srineel with Copilot
 */
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import basics.mastery.fundamentals.ThreadBasicsDemo;

/**
 * Main Application for Java Concurrency Training Program
 * 
 * This is the entry point for exploring various concurrency concepts and demonstrations.
 * The program provides an interactive menu to access different modules covering
 * everything from basic threading to modern Java concurrency features.
 */
public class MainApplication {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║              🚀 Java Concurrency Mastery Training                ║");
        System.out.println("║              Version 2.0 - Modern Java Edition                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        displaySystemInfo();
        
        boolean running = true;
        while (running) {
            displayMenu();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        System.out.println("📚 Thread Basics Demo");
                        System.out.println("Launching Thread Basics Demo...");
                        try {
                            ThreadBasicsDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found or failed: " + e.getMessage());
                        }
                        break;
                        
                    case 2:
                        System.out.println("🔒 Synchronization Primitives");
                        System.out.println("Launching Synchronization Primitives Demo...");
                        try {
                            basics.mastery.synchronization.SynchronizationPrimitivesDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                        
                    case 3:
                        System.out.println("📁 Parallel File Processing Lab");
                        System.out.println("Launching Parallel File Processing Lab...");
                        try {
                            basics.mastery.fundamentals.lab.ParallelFileProcessor.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Lab class not found: " + e.getMessage());
                        }
                        break;
                        
                    case 4:
                        System.out.println("🚀 Modern Concurrency (Java 21)");
                        System.out.println("Launching Modern Concurrency Demo...");
                        try {
                            basics.mastery.modern.ModernConcurrencyDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                        
                    case 5:
                        System.out.println("⚡ Microservice Gateway Lab");
                        System.out.println("Launching Microservice Gateway Lab...");
                        try {
                            basics.mastery.MicroserviceGatewayLab.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Lab class not found: " + e.getMessage());
                        }
                        break;
                        
                    case 6:
                        System.out.println("📊 Memory Model Demo");
                        System.out.println("Launching Java Memory Model Demo...");
                        try {
                            basics.mastery.JavaMemoryModelDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                        
                    case 7:
                        System.out.println("🔍 Concurrency Pitfalls Demo");
                        System.out.println("Launching Concurrency Pitfalls Demo...");
                        try {
                            basics.mastery.ConcurrencyPitfallsDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                        
                    case 8:
                        System.out.println("⚙️ Counter Service Benchmark");
                        System.out.println("Launching Counter Service Benchmark...");
                        try {
                            basics.mastery.CounterServiceBenchmark.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Benchmark class not found: " + e.getMessage());
                        }
                        break;
                    case 9:
                        System.out.println("🧵 Thread Basics Demo");
                        System.out.println("Launching Thread Basics Demo...");
                        try {
                            basics.mastery.fundamentals.ThreadBasicsDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                    case 10:
                        System.out.println("🪶 Virtual Thread Pinning Demo");
                        System.out.println("Launching Virtual Thread Pinning Demo...");
                        try {
                            basics.mastery.modern.VirtualThreadPinningDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                    case 11:
                        System.out.println("🧩 Fail-Fast vs Fail-Safe Iteration");
                        System.out.println("Launching Fail-Safe Iteration Demo...");
                        try {
                            basics.mastery.collections.patterns.FailSafeIterationDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                    case 12:
                        System.out.println("#️⃣ Hashing & Comparator Pitfalls");
                        System.out.println("Launching Hashing Pitfalls Demo...");
                        try {
                            basics.mastery.collections.fundamentals.HashingPitfallsDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                    case 13:
                        System.out.println("🧠 Generics PECS & Variance");
                        System.out.println("Launching Generics PECS Demo...");
                        try {
                            basics.mastery.generics.advanced.GenericsPecosDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                    case 14:
                        System.out.println("⚙️ Lock-Free Counter (CAS)");
                        System.out.println("Launching Lock-Free Counter Demo...");
                        try {
                            basics.mastery.advanced.LockFreeCounterDemo.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Demo class not found: " + e.getMessage());
                        }
                        break;
                    case 15:
                        System.out.println("🔎 Thread Diagnostics Quickstart");
                        System.out.println("Launching Thread Diagnostics...");
                        try {
                            basics.mastery.tools.ThreadDiagnosticsQuickstart.main(new String[]{});
                        } catch (Exception e) {
                            System.out.println("Tool class not found: " + e.getMessage());
                        }
                        break;
                    case 90:
                        System.out.println("🧰 Supplemental Demos");
                        runSupplementalDemosSubmenu();
                        break;
                        
                    case 0:
                        System.out.println("\n🎓 Thank you for using the Java Concurrency Mastery Training Program!");
                        System.out.println("💡 Continue practicing with the provided labs and benchmarks.");
                        running = false;
                        break;
                        
                    default:
            System.out.println("❌ Invalid choice. Please select a number between 0-15.");
                }
                
                if (running) {
                    pressEnterToContinue();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
                pressEnterToContinue();
            } catch (Exception e) {
                System.out.println("❌ An error occurred: " + e.getMessage());
                e.printStackTrace();
                pressEnterToContinue();
            }
        }
        
        scanner.close();
    }
    
    private static void displaySystemInfo() {
        System.out.println("🖥️  System Information:");
        System.out.printf("   Java Version: %s%n", System.getProperty("java.version"));
        System.out.printf("   Available Processors: %d%n", Runtime.getRuntime().availableProcessors());
        System.out.printf("   Max Memory: %d MB%n", Runtime.getRuntime().maxMemory() / (1024 * 1024));
        System.out.printf("   Current Time: %s%n", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println();
    }
    
    private static void displayMenu() {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        📚 Learning Modules                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║  1️⃣  Thread Fundamentals                                         ║");
        System.out.println("║  2️⃣  Synchronization Primitives                                  ║");
        System.out.println("║  3️⃣  Parallel File Processing Lab                                ║");
        System.out.println("║  4️⃣  Modern Concurrency (Java 21) ⭐                            ║");
        System.out.println("║  5️⃣  Microservice Gateway Lab                                    ║");
        System.out.println("║  6️⃣  Java Memory Model Demo                                      ║");
        System.out.println("║  7️⃣  Concurrency Pitfalls Demo                                  ║");
        System.out.println("║  8️⃣  Counter Service Benchmark                                   ║");
        System.out.println("║  9️⃣  Thread Basics Demo                                          ║");
        System.out.println("║  1️⃣0  Virtual Thread Pinning Demo                                 ║");
        System.out.println("║  1️⃣1  Fail-Safe Iteration Demo                                    ║");
        System.out.println("║  1️⃣2  Hashing & Comparator Pitfalls                               ║");
        System.out.println("║  1️⃣3  Generics: PECS & Variance                                   ║");
        System.out.println("║  1️⃣4  Lock-Free Counter (CAS)                                     ║");
        System.out.println("║  1️⃣5  Thread Diagnostics Quickstart                               ║");
        System.out.println("║  9️⃣0  Supplemental Demos (submenu)                                ║");
        System.out.println("║  0️⃣  Exit Program                                                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.print("Enter your choice (0-15): ");
    }

    // Submenu for supplemental demos (duplicates options 9–15 for convenience)
    private static void runSupplementalDemosSubmenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Supplemental Demos ---");
            System.out.println("  9  Thread Basics Demo");
            System.out.println(" 10  Virtual Thread Pinning Demo");
            System.out.println(" 11  Fail-Safe Iteration Demo");
            System.out.println(" 12  Hashing & Comparator Pitfalls");
            System.out.println(" 13  Generics: PECS & Variance");
            System.out.println(" 14  Lock-Free Counter (CAS)");
            System.out.println(" 15  Thread Diagnostics Quickstart");
            System.out.println("  0  Back to main menu");
            System.out.print("Choose (0 to return): ");
            String line = scanner.nextLine().trim();
            if (line.equals("0")) {
                back = true;
                break;
            }
            try {
                int c = Integer.parseInt(line);
                // Reuse existing handler by simulating main switch
                switch (c) {
                    case 9 -> basics.mastery.fundamentals.ThreadBasicsDemo.main(new String[]{});
                    case 10 -> basics.mastery.modern.VirtualThreadPinningDemo.main(new String[]{});
                    case 11 -> basics.mastery.collections.patterns.FailSafeIterationDemo.main(new String[]{});
                    case 12 -> basics.mastery.collections.fundamentals.HashingPitfallsDemo.main(new String[]{});
                    case 13 -> basics.mastery.generics.advanced.GenericsPecosDemo.main(new String[]{});
                    case 14 -> basics.mastery.advanced.LockFreeCounterDemo.main(new String[]{});
                    case 15 -> basics.mastery.tools.ThreadDiagnosticsQuickstart.main(new String[]{});
                    default -> System.out.println("Invalid choice; use 9-15 or 0 to return.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            pressEnterToContinue();
        }
    }
    
    private static void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }
}

// Added inline comments to explain the purpose of each menu option and the overall structure of the main application.
