package io.mastery.collections;

/**
 * Abstract base class for all collection demonstration classes.
 * 
 * Provides common utility methods for formatting output, timing operations,
 * and organizing demonstrations in a consistent manner.
 * 
 * This class follows the Template Method pattern, allowing subclasses
 * to implement specific demonstration logic while maintaining consistent
 * presentation and structure.
 * 
 * @author Java Collections Tutorial Team
 * @version 2.0
 * @since Java 17
 */
public abstract class CollectionDemo {
    
    // ANSI color codes for enhanced console output
    protected static final String RESET = "\u001B[0m";
    protected static final String BOLD = "\u001B[1m";
    protected static final String GREEN = "\u001B[32m";
    protected static final String BLUE = "\u001B[34m";
    protected static final String YELLOW = "\u001B[33m";
    protected static final String CYAN = "\u001B[36m";
    protected static final String PURPLE = "\u001B[35m";
    
    /**
     * Main demonstration method that subclasses must implement.
     * This method should contain all the demonstration logic for the specific collection type.
     */
    public abstract void demonstrateAll();
    
    /**
     * Prints a formatted header for major sections
     * 
     * @param title The title to display in the header
     */
    protected void printHeader(String title) {
        int totalWidth = 80;
        int titleLength = title.length();
        int padding = (totalWidth - titleLength - 2) / 2;
        
        System.out.println();
        System.out.println(BOLD + BLUE + "╔" + "═".repeat(totalWidth - 2) + "╗" + RESET);
        System.out.println(BOLD + BLUE + "║" + " ".repeat(padding) + CYAN + title + BLUE + 
                          " ".repeat(totalWidth - 2 - padding - titleLength) + "║" + RESET);
        System.out.println(BOLD + BLUE + "╚" + "═".repeat(totalWidth - 2) + "╝" + RESET);
        System.out.println();
    }
    
    /**
     * Prints a formatted sub-header for demonstrations within a section
     * 
     * @param subtitle The subtitle to display
     */
    protected void printSubHeader(String subtitle) {
        System.out.println();
        System.out.println(BOLD + GREEN + "┌─ " + subtitle + " " + "─".repeat(Math.max(0, 70 - subtitle.length())) + RESET);
        System.out.println();
    }
    
    /**
     * Prints an operation result with consistent formatting
     * 
     * @param operation The operation description
     * @param result The result to display
     */
    protected void printResult(String operation, Object result) {
        System.out.println(YELLOW + "  ➤ " + RESET + operation + ": " + CYAN + result + RESET);
    }
    
    /**
     * Prints an informational message with emoji and formatting
     * 
     * @param message The information message
     */
    protected void printInfo(String message) {
        System.out.println(BLUE + "  ℹ️  " + message + RESET);
    }
    
    /**
     * Prints a warning message with formatting
     * 
     * @param message The warning message
     */
    protected void printWarning(String message) {
        System.out.println(YELLOW + "  ⚠️  " + message + RESET);
    }
    
    /**
     * Prints a tip or best practice with formatting
     * 
     * @param tip The tip or best practice
     */
    protected void printTip(String tip) {
        System.out.println(GREEN + "  💡 " + BOLD + "TIP: " + RESET + GREEN + tip + RESET);
    }
    
    /**
     * Prints a benchmark result with performance metrics
     * 
     * @param operation The operation being benchmarked
     * @param timeMs The execution time in milliseconds
     */
    protected void printBenchmark(String operation, double timeMs) {
        System.out.println(PURPLE + "  ⏱️  " + operation + ": " + 
                          String.format("%.2f", timeMs) + " ms" + RESET);
    }
    
    /**
     * Prints a comparison between two performance metrics
     * 
     * @param operation The operation being compared
     * @param baseline The baseline time
     * @param comparison The comparison time
     * @param baselineName The name of the baseline implementation
     * @param comparisonName The name of the comparison implementation
     */
    protected void printComparison(String operation, double baseline, double comparison, 
                                 String baselineName, String comparisonName) {
        double ratio = baseline / comparison;
        String faster = ratio > 1 ? comparisonName : baselineName;
        double speedup = Math.max(ratio, 1.0 / ratio);
        
        System.out.println(PURPLE + "  📊 " + operation + " comparison:" + RESET);
        System.out.println("     " + baselineName + ": " + String.format("%.2f", baseline) + " ms");
        System.out.println("     " + comparisonName + ": " + String.format("%.2f", comparison) + " ms");
        System.out.println(GREEN + "     " + faster + " is " + String.format("%.1f", speedup) + "x faster" + RESET);
    }
    
    /**
     * Measures execution time of a Runnable operation
     * 
     * @param operation The operation to time
     * @return The execution time in milliseconds
     */
    protected double measureTime(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0;
    }
    
    /**
     * Prints a separator line for better visual organization
     */
    protected void printSeparator() {
        System.out.println(BLUE + "  " + "─".repeat(76) + RESET);
    }
    
    /**
     * Prints section completion message
     * 
     * @param sectionName The name of the completed section
     */
    protected void printSectionComplete(String sectionName) {
        System.out.println();
        System.out.println(GREEN + "  ✅ " + sectionName + " demonstration completed!" + RESET);
        System.out.println();
    }
    
    /**
     * Formats large numbers with commas for better readability
     * 
     * @param number The number to format
     * @return Formatted number string
     */
    protected String formatNumber(long number) {
        return String.format("%,d", number);
    }
    
    /**
     * Formats bytes into human-readable format
     * 
     * @param bytes The number of bytes
     * @return Formatted string with appropriate unit
     */
    protected String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        else return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}