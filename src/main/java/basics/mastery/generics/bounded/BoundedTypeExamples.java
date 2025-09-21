package basics.mastery.generics.bounded;

import java.io.Serializable;

/**
 * Comprehensive examples of bounded type parameters in Java generics.
 *
 * @author Srineel with Copilot
 * @version 1.0
 */
// ...existing code...
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Comprehensive examples of bounded type parameters in Java generics.
 * 
 * This class demonstrates:
 * - Single type bounds (T extends SomeClass)
 * - Multiple type bounds (T extends Class & Interface1 & Interface2)
 * - Intersection types with classes and interfaces
 * - Practical applications of bounded generics
 * - Complex constraint scenarios
 * - Type parameter inheritance and variance
 *
 * @author Srineel with Copilot
 * @version 1.0
 */
public class BoundedTypeExamples {
    
    // ===== SINGLE UPPER BOUNDS =====
    
    /**
     * Generic class that only accepts Comparable types.
     * Demonstrates single upper bound constraint.
     * 
     * @param <T> type that must implement Comparable
     */
    public static class SortedContainer<T extends Comparable<T>> {
        private final List<T> items = new ArrayList<>();
        
        /**
         * Adds an item and maintains sorted order.
         * 
         * @param item the item to add
         */
        public void add(T item) {
            items.add(item);
            Collections.sort(items);
        }
        
        /**
         * Gets the minimum item.
         * 
         * @return the minimum item, or null if empty
         */
        public T getMin() {
            return items.isEmpty() ? null : items.get(0);
        }
        
        /**
         * Gets the maximum item.
         * 
         * @return the maximum item, or null if empty
         */
        public T getMax() {
            return items.isEmpty() ? null : items.get(items.size() - 1);
        }
        
        /**
         * Finds items within a range.
         * 
         * @param min minimum value (inclusive)
         * @param max maximum value (inclusive)
         * @return list of items in range
         */
        public List<T> findInRange(T min, T max) {
            List<T> result = new ArrayList<>();
            for (T item : items) {
                if (item.compareTo(min) >= 0 && item.compareTo(max) <= 0) {
                    result.add(item);
                }
            }
            return result;
        }
        
        public List<T> getItems() {
            return new ArrayList<>(items);
        }
        
        public int size() {
            return items.size();
        }
        
        public boolean isEmpty() {
            return items.isEmpty();
        }
    }
    
    /**
     * Numeric calculator that works with any Number subtype.
     * Demonstrates bounded generics with Number hierarchy.
     * 
     * @param <T> numeric type extending Number
     */
    public static class NumericProcessor<T extends Number> {
        private final List<T> values = new ArrayList<>();
        
        public void addValue(T value) {
            values.add(value);
        }
        
        /**
         * Calculates sum as double.
         * Uses Number's doubleValue() method.
         * 
         * @return sum of all values
         */
        public double sum() {
            return values.stream()
                    .mapToDouble(Number::doubleValue)
                    .sum();
        }
        
        /**
         * Finds the maximum value.
         * 
         * @return maximum value or null if empty
         */
        public T max() {
            if (values.isEmpty()) return null;
            
            T max = values.get(0);
            for (T value : values) {
                if (value.doubleValue() > max.doubleValue()) {
                    max = value;
                }
            }
            return max;
        }
        
        /**
         * Calculates average.
         * 
         * @return average value
         */
        public double average() {
            return values.isEmpty() ? 0.0 : sum() / values.size();
        }
        
        public List<T> getValues() {
            return new ArrayList<>(values);
        }
    }
    
    // ===== MULTIPLE BOUNDS (INTERSECTION TYPES) =====
    
    /**
     * Demonstrates multiple bounds: class bound + interface bounds.
     * T must extend Number AND implement Comparable AND Serializable.
     * 
     * @param <T> type with multiple constraints
     */
    public static class AdvancedNumericContainer<T extends Number & Comparable<T> & Serializable> {
        private final TreeSet<T> sortedValues = new TreeSet<>();
        
        public void add(T value) {
            sortedValues.add(value);
        }
        
        public T getMedian() {
            if (sortedValues.isEmpty()) return null;
            
            List<T> list = new ArrayList<>(sortedValues);
            int middle = list.size() / 2;
            return list.get(middle);
        }
        
        public Set<T> getSortedValues() {
            return new TreeSet<>(sortedValues);
        }
        
        /**
         * Serializes the container state.
         * Possible because T extends Serializable.
         * 
         * @return serialization info
         */
        public String serializationInfo() {
            return "Container with " + sortedValues.size() + " serializable numeric values";
        }
        
        /**
         * Range query using Comparable constraint.
         * 
         * @param from start value
         * @param to end value
         * @return subset in range
         */
        public Set<T> subSet(T from, T to) {
            return sortedValues.subSet(from, to);
        }
    }
    
    /**
     * Generic utility for objects that are both Cloneable and Comparable.
     * Demonstrates intersection with two interfaces.
     * 
     * @param <T> type implementing both Cloneable and Comparable
     */
    public static class CloneableComparableProcessor<T extends Cloneable & Comparable<T>> {
        
        /**
         * Creates a sorted copy of the input array.
         * Uses clone() from Cloneable and compareTo() from Comparable.
         * 
         * @param items array to sort
         * @return new array with sorted cloned elements
         * @throws CloneNotSupportedException if cloning fails
         */
        @SuppressWarnings("unchecked")
        public T[] sortedCopy(T[] items) throws CloneNotSupportedException {
            T[] copy = items.clone();
            
            // Clone each element
            for (int i = 0; i < copy.length; i++) {
                if (copy[i] instanceof Cloneable) {
                    try {
                        // Use reflection to call clone() since it's protected
                        copy[i] = (T) copy[i].getClass().getMethod("clone").invoke(copy[i]);
                    } catch (Exception e) {
                        throw new CloneNotSupportedException("Failed to clone element: " + e.getMessage());
                    }
                }
            }
            
            // Sort using Comparable
            Arrays.sort(copy);
            return copy;
        }
        
        /**
         * Finds the maximum element and returns a clone.
         * 
         * @param items collection to search
         * @return cloned maximum element
         * @throws CloneNotSupportedException if cloning fails
         */
        @SuppressWarnings("unchecked")
        public T maxClone(Collection<T> items) throws CloneNotSupportedException {
            if (items.isEmpty()) return null;
            
            T max = Collections.max(items);
            try {
                return (T) max.getClass().getMethod("clone").invoke(max);
            } catch (Exception e) {
                throw new CloneNotSupportedException("Failed to clone max element: " + e.getMessage());
            }
        }
    }
    
    // ===== BOUNDED WILDCARDS WITH METHODS =====
    
    /**
     * Method with bounded type parameter and bounded wildcard.
     * T must extend Comparable, and collection can contain T subtypes.
     * 
     * @param <T> comparable type
     * @param items collection of T or its subtypes
     * @return maximum element
     */
    public static <T extends Comparable<? super T>> T findMaximum(Collection<? extends T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Collection cannot be empty");
        }
        return Collections.max(items);
    }
    
    /**
     * Sorts a list where elements implement Comparable.
     * Demonstrates self-bounded type parameter.
     * 
     * @param <T> type that can compare to itself or its supertypes
     * @param list list to sort
     */
    public static <T extends Comparable<? super T>> void sortInPlace(List<T> list) {
        Collections.sort(list);
    }
    
    /**
     * Merges two sorted lists into one sorted list.
     * 
     * @param <T> comparable type
     * @param list1 first sorted list
     * @param list2 second sorted list
     * @return merged sorted list
     */
    public static <T extends Comparable<? super T>> List<T> mergeSorted(
            List<? extends T> list1, List<? extends T> list2) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < list1.size() && j < list2.size()) {
            T item1 = list1.get(i);
            T item2 = list2.get(j);
            if (item1.compareTo(item2) <= 0) {
                result.add(item1);
                i++;
            } else {
                result.add(item2);
                j++;
            }
        }
        
        // Add remaining elements
        while (i < list1.size()) {
            result.add(list1.get(i++));
        }
        while (j < list2.size()) {
            result.add(list2.get(j++));
        }
        
        return result;
    }
    
    // ===== COMPLEX BOUNDED SCENARIOS =====
    
    /**
     * Builder pattern with bounded generics.
     * Demonstrates self-referential bounded generics.
     * 
     * @param <T> the concrete builder type
     * @param <R> the type being built
     */
    public static abstract class BoundedBuilder<T extends BoundedBuilder<T, R>, R> {
        
        /**
         * Template method that returns the concrete builder type.
         * Enables method chaining with proper return types.
         * 
         * @return this builder cast to concrete type
         */
        @SuppressWarnings("unchecked")
        protected final T self() {
            return (T) this;
        }
        
        /**
         * Abstract method to build the final object.
         * 
         * @return the built object
         */
        public abstract R build();
    }
    
    /**
     * Concrete implementation of bounded builder for Person objects.
     */
    public static class PersonBuilder extends BoundedBuilder<PersonBuilder, Person> {
        private String name;
        private int age;
        private String email;
        
        public PersonBuilder name(String name) {
            this.name = name;
            return self();
        }
        
        public PersonBuilder age(int age) {
            this.age = age;
            return self();
        }
        
        public PersonBuilder email(String email) {
            this.email = email;
            return self();
        }
        
        @Override
        public Person build() {
            return new Person(name, age, email);
        }
    }
    
    /**
     * Repository pattern with bounded entities.
     * T must extend Entity and implement Serializable.
     * 
     * @param <T> entity type
     * @param <ID> identifier type
     */
    public static class Repository<T extends Entity<ID> & Serializable, ID extends Serializable> {
        private final Map<ID, T> storage = new HashMap<>();
        
        public void save(T entity) {
            storage.put(entity.getId(), entity);
        }
        
        public Optional<T> findById(ID id) {
            return Optional.ofNullable(storage.get(id));
        }
        
        public List<T> findAll() {
            return new ArrayList<>(storage.values());
        }
        
        public boolean deleteById(ID id) {
            return storage.remove(id) != null;
        }
        
        public long count() {
            return storage.size();
        }
        
        /**
         * Find entities matching predicate.
         * Uses bounded type to ensure type safety.
         * 
         * @param predicate filter condition
         * @return matching entities
         */
        public List<T> findByPredicate(Predicate<? super T> predicate) {
            return storage.values().stream()
                    .filter(predicate)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    // ===== FUNCTIONAL INTERFACES WITH BOUNDS =====
    
    /**
     * Function interface that works with comparable types.
     * Input and output must both be comparable.
     * 
     * @param <T> input type
     * @param <R> output type
     */
    @FunctionalInterface
    public interface ComparableFunction<T extends Comparable<T>, R extends Comparable<R>> 
            extends Function<T, R> {
        
        /**
         * Applies this function and compares result with a threshold.
         * 
         * @param input input value
         * @param threshold comparison threshold
         * @return true if result is greater than threshold
         */
        default boolean applyAndCompare(T input, R threshold) {
            R result = apply(input);
            return result.compareTo(threshold) > 0;
        }
    }
    
    /**
     * Processor for numeric transformations.
     * Demonstrates bounded functional interface usage.
     * 
     * @param <T> input numeric type
     * @param <R> output numeric type
     */
    public static class NumericTransformer<T extends Number, R extends Number> {
        private final Function<T, R> transformer;
        
        public NumericTransformer(Function<T, R> transformer) {
            this.transformer = transformer;
        }
        
        /**
         * Transforms a collection of numbers.
         * 
         * @param input input numbers
         * @return transformed numbers
         */
        public List<R> transform(Collection<T> input) {
            return input.stream()
                    .map(transformer)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
        
        /**
         * Transforms and filters based on numeric threshold.
         * 
         * @param input input numbers
         * @param threshold numeric threshold
         * @return transformed numbers above threshold
         */
        public List<R> transformAndFilter(Collection<T> input, double threshold) {
            return input.stream()
                    .map(transformer)
                    .filter(r -> r.doubleValue() > threshold)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    // ===== SUPPORTING CLASSES =====
    
    /**
     * Base entity interface for repository example.
     * 
     * @param <ID> identifier type
     */
    public interface Entity<ID> {
        ID getId();
    }
    
    /**
     * Simple Person class for examples.
     */
    public static class Person implements Entity<String>, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final String name;
        private final int age;
        private final String email;
        
        public Person(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
        
        @Override
        public String getId() {
            return email; // Using email as ID
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getEmail() { return email; }
        
        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d, email='%s'}", name, age, email);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Person person = (Person) obj;
            return age == person.age && 
                   Objects.equals(name, person.name) && 
                   Objects.equals(email, person.email);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, age, email);
        }
    }
    
    /**
     * Example of a class that implements both Cloneable and Comparable.
     */
    public static class ComparableScore implements Cloneable, Comparable<ComparableScore> {
        private final int value;
        private final String description;
        
        public ComparableScore(int value, String description) {
            this.value = value;
            this.description = description;
        }
        
        @Override
        public ComparableScore clone() throws CloneNotSupportedException {
            return new ComparableScore(value, description);
        }
        
        @Override
        public int compareTo(ComparableScore other) {
            return Integer.compare(this.value, other.value);
        }
        
        public int getValue() { return value; }
        public String getDescription() { return description; }
        
        @Override
        public String toString() {
            return String.format("Score{value=%d, description='%s'}", value, description);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ComparableScore that = (ComparableScore) obj;
            return value == that.value && Objects.equals(description, that.description);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value, description);
        }
    }
}