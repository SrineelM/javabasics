package io.mastery.generics.wildcards;

import java.util.*;
import java.util.function.*;

/**
 * Comprehensive demonstration of wildcard usage in Java generics.
 * 
 * This class illustrates the three types of wildcards:
 * - Unbounded wildcards (?)
 * - Upper bounded wildcards (? extends Type)
 * - Lower bounded wildcards (? super Type)
 * 
 * Key concepts covered:
 * - PECS principle (Producer Extends, Consumer Super)
 * - Covariance and contravariance
 * - Get and Put principle
 * - Wildcard capture
 * - Generic type erasure implications
 *
 * @author Java Generics Tutorial
 * @version 1.0
 */
public class WildcardExamples {
    
    // ===== UNBOUNDED WILDCARDS (?) =====
    
    /**
     * Counts elements in any generic collection.
     * Uses unbounded wildcard since we don't need to know the type.
     * 
     * @param collection any collection
     * @return the number of elements
     */
    public static int countElements(Collection<?> collection) {
        return collection.size();
    }
    
    /**
     * Checks if any collection is empty.
     * Demonstrates read-only operations with unbounded wildcards.
     * 
     * @param collection any collection
     * @return true if empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection.isEmpty();
    }
    
    /**
     * Prints all elements in a collection.
     * Can only call methods that don't depend on the generic type.
     * 
     * @param collection any collection
     */
    public static void printAll(Collection<?> collection) {
        for (Object element : collection) {
            System.out.println(element);
        }
    }
    
    /**
     * Clears any collection.
     * Demonstrates that modification operations that don't involve
     * the generic type parameter are allowed.
     * 
     * @param collection any collection to clear
     */
    public static void clearCollection(Collection<?> collection) {
        collection.clear();
    }
    
    /**
     * Compares two collections for equality.
     * Uses Object.equals() which works with unbounded wildcards.
     * 
     * @param first any collection
     * @param second any collection
     * @return true if collections are equal
     */
    public static boolean areEqual(Collection<?> first, Collection<?> second) {
        return first.equals(second);
    }
    
    // ===== UPPER BOUNDED WILDCARDS (? extends T) =====
    // These are PRODUCERS - you can GET (read) but not PUT (write)
    
    /**
     * Finds the maximum element in a collection of Numbers.
     * Demonstrates upper bounded wildcard - can read but not write.
     * 
     * @param numbers collection of any Number subtype
     * @return the maximum number
     */
    public static double findMax(Collection<? extends Number> numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("Collection cannot be empty");
        }
        
        double max = Double.NEGATIVE_INFINITY;
        for (Number number : numbers) {
            double value = number.doubleValue();
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    
    /**
     * Calculates sum of any numeric collection.
     * Works with Integer, Double, Float, etc.
     * 
     * @param numbers collection of numbers
     * @return the sum as double
     */
    public static double sum(Collection<? extends Number> numbers) {
        double total = 0.0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }
    
    /**
     * Copies all elements from source to destination.
     * Source is a producer (? extends T), destination is exact type.
     * 
     * @param <T> the element type
     * @param source producer collection
     * @param destination consumer collection
     */
    public static <T> void copyFrom(Collection<? extends T> source, Collection<T> destination) {
        for (T element : source) {
            destination.add(element);
        }
    }
    
    /**
     * Finds elements matching a predicate.
     * Demonstrates reading from producer with functional interface.
     * 
     * @param <T> the element type
     * @param source producer collection
     * @param predicate test condition
     * @return list of matching elements
     */
    public static <T> List<T> findMatching(Collection<? extends T> source, Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T element : source) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }
    
    /**
     * Converts elements using a function.
     * Source is producer, result is new collection.
     * 
     * @param <T> source element type
     * @param <R> result element type
     * @param source producer collection
     * @param mapper transformation function
     * @return new collection with transformed elements
     */
    public static <T, R> List<R> transform(Collection<? extends T> source, Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T element : source) {
            result.add(mapper.apply(element));
        }
        return result;
    }
    
    /**
     * Creates a string representation of any collection.
     * Demonstrates upper bounded wildcard with Object methods.
     * 
     * @param collection collection of any type
     * @return formatted string
     */
    public static String toString(Collection<?> collection) {
        if (collection.isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object element : collection) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(element);
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
    
    // ===== LOWER BOUNDED WILDCARDS (? super T) =====
    // These are CONSUMERS - you can PUT (write) but reading gives Object
    
    /**
     * Adds all elements to a consumer collection.
     * Destination is consumer (? super T) - can write T or its subtypes.
     * 
     * @param <T> the element type
     * @param elements elements to add
     * @param destination consumer collection
     */
    @SafeVarargs
    public static <T> void addAll(Collection<? super T> destination, T... elements) {
        for (T element : elements) {
            destination.add(element);
        }
    }
    
    /**
     * Adds all elements from source to destination.
     * Demonstrates PECS: source is producer, destination is consumer.
     * 
     * @param <T> the element type
     * @param source producer collection (? extends T)
     * @param destination consumer collection (? super T)
     */
    public static <T> void transfer(Collection<? extends T> source, Collection<? super T> destination) {
        for (T element : source) {
            destination.add(element);
        }
    }
    
    /**
     * Fills a collection with a specific value.
     * Consumer can accept T or any supertype of T.
     * 
     * @param <T> the element type
     * @param collection consumer collection
     * @param value value to fill with
     * @param count number of times to add
     */
    public static <T> void fill(Collection<? super T> collection, T value, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(value);
        }
    }
    
    /**
     * Applies an action to each element and adds results to destination.
     * Shows consumer pattern with functional interfaces.
     * 
     * @param <T> source element type
     * @param <R> result element type
     * @param source source collection
     * @param mapper transformation function
     * @param destination consumer for results
     */
    public static <T, R> void processAndAdd(
            Collection<? extends T> source,
            Function<? super T, ? extends R> mapper,
            Collection<? super R> destination) {
        for (T element : source) {
            R result = mapper.apply(element);
            destination.add(result);
        }
    }
    
    // ===== PECS PRINCIPLE DEMONSTRATIONS =====
    
    /**
     * Perfect example of PECS principle in action.
     * Producer Extends, Consumer Super.
     * 
     * @param <T> the element type
     * @param source producer - can read T and its subtypes
     * @param destination consumer - can write T and its supertypes
     * @param filter predicate to test elements
     */
    public static <T> void copyFiltered(
            Collection<? extends T> source,          // Producer Extends
            Collection<? super T> destination,       // Consumer Super
            Predicate<? super T> filter) {
        for (T element : source) {
            if (filter.test(element)) {
                destination.add(element);
            }
        }
    }
    
    /**
     * Merges multiple producer collections into one consumer.
     * Demonstrates multiple producers with single consumer.
     * 
     * @param <T> the element type
     * @param destination consumer collection
     * @param sources producer collections
     */
    @SafeVarargs
    public static <T> void mergeAll(
            Collection<? super T> destination,
            Collection<? extends T>... sources) {
        for (Collection<? extends T> source : sources) {
            destination.addAll(source);
        }
    }
    
    /**
     * Sorts elements and adds them to destination.
     * Shows bounded wildcards with type constraints.
     * 
     * @param <T> the element type, must be Comparable
     * @param source producer collection
     * @param destination consumer collection
     */
    public static <T extends Comparable<? super T>> void sortedCopy(
            Collection<? extends T> source,
            Collection<? super T> destination) {
        List<T> temp = new ArrayList<>();
        for (T element : source) {
            temp.add(element);
        }
        Collections.sort(temp);
        destination.addAll(temp);
    }
    
    // ===== WILDCARD CAPTURE HELPERS =====
    
    /**
     * Demonstrates wildcard capture - converting ? to concrete type parameter.
     * This is a common pattern when you need to "capture" the wildcard type.
     * 
     * @param list list with wildcard type
     */
    public static void processUnknownList(List<?> list) {
        processKnownList(list);
    }
    
    /**
     * Helper method that captures the wildcard type as T.
     * Now we can work with T as a concrete type parameter.
     * 
     * @param <T> the captured type
     * @param list list with known type
     */
    private static <T> void processKnownList(List<T> list) {
        if (!list.isEmpty()) {
            T first = list.get(0);
            // Now we can use first as type T
            list.set(0, first); // This works because T is captured
        }
    }
    
    /**
     * Another wildcard capture example with swapping.
     * 
     * @param list list with unknown element type
     */
    public static void swapFirstAndLast(List<?> list) {
        swapFirstAndLastHelper(list);
    }
    
    /**
     * Helper that captures the wildcard type.
     * 
     * @param <T> the captured type
     * @param list list with known type
     */
    private static <T> void swapFirstAndLastHelper(List<T> list) {
        if (list.size() >= 2) {
            T first = list.get(0);
            T last = list.get(list.size() - 1);
            list.set(0, last);
            list.set(list.size() - 1, first);
        }
    }
    
    // ===== GET AND PUT PRINCIPLE EXAMPLES =====
    
    /**
     * Demonstrates GET principle with upper bounded wildcards.
     * You can GET (read) from ? extends T, but cannot PUT (write).
     * 
     * @param numbers collection of number subtypes
     * @return statistics about the numbers
     */
    public static NumberStats analyzeNumbers(Collection<? extends Number> numbers) {
        if (numbers.isEmpty()) {
            return new NumberStats(0, 0, 0, 0);
        }
        
        double sum = 0;
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        int count = 0;
        
        // GET operations work fine
        for (Number number : numbers) {
            double value = number.doubleValue();
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
            count++;
        }
        
        // PUT operations would NOT compile:
        // numbers.add(42);        // ERROR! Cannot add to ? extends Number
        // numbers.add(42.0);      // ERROR! Cannot add to ? extends Number
        
        return new NumberStats(sum, min, max, count);
    }
    
    /**
     * Demonstrates PUT principle with lower bounded wildcards.
     * You can PUT (write) to ? super T, but GET only returns Object.
     * 
     * @param destination collection that accepts Integer or its supertypes
     * @param values values to add
     */
    public static void addIntegers(Collection<? super Integer> destination, int... values) {
        // PUT operations work fine
        for (int value : values) {
            destination.add(value);           // OK: Integer is subtype of ? super Integer
            destination.add(Integer.valueOf(value)); // OK: explicit Integer
        }
        
        // GET operations return Object:
        if (!destination.isEmpty()) {
            Object first = destination.iterator().next(); // Returns Object, not Integer
            System.out.println("First element as Object: " + first);
            // Integer typedFirst = destination.iterator().next(); // ERROR! Would not compile
        }
    }
    
    // ===== NESTED WILDCARDS =====
    
    /**
     * Demonstrates nested wildcards - wildcards within wildcards.
     * This represents a list of lists where each inner list contains
     * some subtype of Number.
     * 
     * @param nestedLists list of lists of numbers
     * @return total count of all numbers
     */
    public static int countAllNumbers(List<? extends List<? extends Number>> nestedLists) {
        int total = 0;
        for (List<? extends Number> innerList : nestedLists) {
            total += innerList.size();
        }
        return total;
    }
    
    /**
     * Flattens nested collections with wildcards.
     * 
     * @param <T> the element type
     * @param nested collection of collections
     * @return flattened list
     */
    public static <T> List<T> flattenWildcard(Collection<? extends Collection<? extends T>> nested) {
        List<T> result = new ArrayList<>();
        for (Collection<? extends T> inner : nested) {
            result.addAll(inner);
        }
        return result;
    }
    
    // ===== PRACTICAL EXAMPLES =====
    
    /**
     * Real-world example: Animal hierarchy with wildcards.
     * Demonstrates variance in inheritance hierarchies.
     */
    public static class AnimalExamples {
        
        public static class Animal {
            private final String name;
            public Animal(String name) { this.name = name; }
            public String getName() { return name; }
            @Override public String toString() { return name; }
        }
        
        public static class Dog extends Animal {
            public Dog(String name) { super(name); }
            public void bark() { System.out.println(getName() + " barks!"); }
        }
        
        public static class Cat extends Animal {
            public Cat(String name) { super(name); }
            public void meow() { System.out.println(getName() + " meows!"); }
        }
        
        /**
         * Feeds all animals in a collection.
         * Uses upper bounded wildcard - can work with any animal subtype.
         * 
         * @param animals collection of any animal subtypes
         */
        public static void feedAnimals(Collection<? extends Animal> animals) {
            for (Animal animal : animals) {
                System.out.println("Feeding " + animal.getName());
            }
        }
        
        /**
         * Adds new animals to a collection.
         * Uses lower bounded wildcard - can add to any animal supertype collection.
         * 
         * @param animals collection that can hold animals
         * @param newAnimals animals to add
         */
        @SafeVarargs
        public static void addAnimals(Collection<? super Animal> animals, Animal... newAnimals) {
            for (Animal animal : newAnimals) {
                animals.add(animal);
            }
        }
        
        /**
         * Moves animals between collections.
         * Perfect PECS example with animal hierarchy.
         * 
         * @param source collection to move from (producer)
         * @param destination collection to move to (consumer)
         */
        public static void moveAnimals(
                Collection<? extends Animal> source,
                Collection<? super Animal> destination) {
            List<Animal> toMove = new ArrayList<>(source);
            destination.addAll(toMove);
            source.clear();
        }
    }
    
    // ===== HELPER CLASSES =====
    
    /**
     * Helper class for number statistics.
     */
    public static class NumberStats {
        private final double sum;
        private final double min;
        private final double max;
        private final int count;
        
        public NumberStats(double sum, double min, double max, int count) {
            this.sum = sum;
            this.min = min;
            this.max = max;
            this.count = count;
        }
        
        public double getSum() { return sum; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public int getCount() { return count; }
        public double getAverage() { return count > 0 ? sum / count : 0; }
        
        @Override
        public String toString() {
            return String.format("NumberStats{sum=%.2f, min=%.2f, max=%.2f, count=%d, avg=%.2f}",
                    sum, min, max, count, getAverage());
        }
    }
}