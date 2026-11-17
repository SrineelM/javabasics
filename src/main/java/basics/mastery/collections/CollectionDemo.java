package basics.mastery.collections;

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
 * @author Srineel with Copilot
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
        
        // [coach] If title is longer than the box, padding goes negative and the
        // repeat() calls throw IllegalArgumentException. Clamp defensively.
        if (titleLength > totalWidth - 4) {
            title = title.substring(0, totalWidth - 7) + "...";
            titleLength = title.length();
            padding = (totalWidth - titleLength - 2) / 2;
        }
        
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
        // [coach] Math.max guards against negative repeat counts when subtitle
        // is longer than 70 - already present in the original, good.
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
        // [coach] The emoji here is a fixed glyph inside the formatting;
        // it is preserved per "no scope removal" even under a no-emoji rule,
        // because removing it would alter every subclass's output layout.
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
        // [coach] Division by zero or negative time (e.g., a zero-nanos operation
        // because the JIT eliminated the work) produces Infinity or NaN.
        // Clamp comparison to a tiny positive value to keep the ratio finite.
        // Better: only print a comparison when both times exceed a noise floor.
        // [coach:fix] guard against zero / NaN timing values.
        double safeComparison = (comparison > 0 && !Double.isNaN(comparison)) ? comparison : 1e-6;
        double safeBaseline = (baseline > 0 && !Double.isNaN(baseline)) ? baseline : 1e-6;
        double ratio = safeBaseline / safeComparison;
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
        // [coach] This is a wall-clock microbenchmark, not JMH. It is subject
        // to JIT warmup, dead-code elimination (if the operation's result is
        // unused, the JIT may remove it entirely), and single-run variance.
        // Sufficient for didactic side-by-side comparisons; not for claims.
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
        // [coach] Locale-sensitive: String.format uses the default locale.
        // Under Locale.FRANCE the separator is a narrow no-break space, not ",".
        // If the output must be parsed by scripts, use Locale.ROOT explicitly:
        // return String.format(Locale.ROOT, "%,d", number);
        return String.format("%,d", number);
    }
    
    /**
     * Formats bytes into human-readable format
     * 
     * @param bytes The number of bytes
     * @return Formatted string with appropriate unit
     */
    protected String formatBytes(long bytes) {
        // [coach] Negative inputs (e.g., Integer.MIN_VALUE passed as long)
        // fall through to "GB" with a negative number. Inputs are assumed
        // non-negative; callers should validate. Also note this method uses
        // binary units (KiB/MiB/GiB) but labels them KB/MB/GB — common and
        // deliberately user-friendly, not SI-correct.
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        else return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}