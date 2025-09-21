package io.mastery.generics.fundamentals;

import java.util.*;
import java.util.function.*;

/**
 * Comprehensive collection of generic methods demonstrating various
 * generic programming patterns, type parameter usage, and best practices.
 * 
 * This class demonstrates:
 * - Static generic methods
 * - Type parameter bounds
 * - Wildcard usage in method signatures
 * - Variance and contravariance
 * - Method overloading with generics
 * - Generic method composition
 * - Type safety patterns
 *
 * @author Java Generics Tutorial
 * @version 1.0
 */
public final class GenericMethods {
    
    private GenericMethods() {
        // Utility class - prevent instantiation
    }
    
    // ===== BASIC GENERIC METHODS =====
    
    /**
     * Identity function - returns the input unchanged.
     * Demonstrates simplest generic method.
     * 
     * @param <T> the type of the value
     * @param value the input value
     * @return the same value
     */
    public static <T> T identity(T value) {
        return value;
    }
    
    /**
     * Swaps two elements in an array.
     * Demonstrates generic method with array manipulation.
     * 
     * @param <T> the type of array elements
     * @param array the array to modify
     * @param i first index
     * @param j second index
     * @throws IndexOutOfBoundsException if indices are invalid
     */
    public static <T> void swap(T[] array, int i, int j) {
        Objects.checkIndex(i, array.length);
        Objects.checkIndex(j, array.length);
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * Swaps two elements in a list.
     * Demonstrates generic method with collections.
     * 
     * @param <T> the type of list elements
     * @param list the list to modify
     * @param i first index
     * @param j second index
     */
    public static <T> void swap(List<T> list, int i, int j) {
        Collections.swap(list, i, j);
    }
    
    /**
     * Creates a Pair from two values.
     * Demonstrates generic method with multiple type parameters.
     * 
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @param key the key
     * @param value the value
     * @return a new Pair
     */
    public static <K, V> Pair<K, V> createPair(K key, V value) {
        return Pair.of(key, value);
    }
    
    /**
     * Creates a Triple from three values.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <C> the type of the third element
     * @param first the first element
     * @param second the second element
     * @param third the third element
     * @return a new Triple
     */
    public static <A, B, C> Triple<A, B, C> createTriple(A first, B second, C third) {
        return Triple.of(first, second, third);
    }
    
    // ===== VARARGS AND SAFE OPERATIONS =====
    
    /**
     * Creates a List from varargs.
     * Demonstrates generic varargs usage.
     * 
     * @param <T> the type of elements
     * @param elements the elements to include
     * @return a new List containing the elements
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> List<T> listOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }
    
    /**
     * Creates a Set from varargs.
     * 
     * @param <T> the type of elements
     * @param elements the elements to include
     * @return a new Set containing the elements
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Set<T> setOf(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }
    
    /**
     * Creates an immutable List from varargs.
     * 
     * @param <T> the type of elements
     * @param elements the elements to include
     * @return an immutable List
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> List<T> immutableListOf(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
    
    // ===== BOUNDED GENERIC METHODS =====
    
    /**
     * Finds the maximum element in a collection.
     * Demonstrates upper bounded generic method.
     * 
     * @param <T> the type of elements, must be Comparable
     * @param collection the collection to search
     * @return the maximum element
     * @throws NoSuchElementException if collection is empty
     */
    public static <T extends Comparable<? super T>> T max(Collection<T> collection) {
        if (collection.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return Collections.max(collection);
    }
    
    /**
     * Finds the minimum element in a collection.
     * 
     * @param <T> the type of elements, must be Comparable
     * @param collection the collection to search
     * @return the minimum element
     * @throws NoSuchElementException if collection is empty
     */
    public static <T extends Comparable<? super T>> T min(Collection<T> collection) {
        if (collection.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return Collections.min(collection);
    }
    
    /**
     * Sorts a list in-place.
     * Demonstrates generic method with bounded type parameter.
     * 
     * @param <T> the type of elements, must be Comparable
     * @param list the list to sort
     */
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        Collections.sort(list);
    }
    
    /**
     * Returns a sorted copy of the list.
     * 
     * @param <T> the type of elements, must be Comparable
     * @param list the list to copy and sort
     * @return a new sorted list
     */
    public static <T extends Comparable<? super T>> List<T> sorted(List<T> list) {
        List<T> copy = new ArrayList<>(list);
        Collections.sort(copy);
        return copy;
    }
    
    // ===== WILDCARD METHODS =====
    
    /**
     * Copies elements from source to destination.
     * Demonstrates PECS principle (Producer Extends, Consumer Super).
     * 
     * @param <T> the type of elements
     * @param source the source collection (producer)
     * @param destination the destination collection (consumer)
     */
    public static <T> void copy(Collection<? extends T> source, Collection<? super T> destination) {
        for (T item : source) {
            destination.add(item);
        }
    }
    
    /**
     * Adds all elements from source to destination.
     * 
     * @param <T> the type of elements
     * @param destination the destination collection
     * @param source the source collection
     */
    public static <T> void addAll(Collection<? super T> destination, Collection<? extends T> source) {
        destination.addAll(source);
    }
    
    /**
     * Counts elements in any collection.
     * Demonstrates unbounded wildcard.
     * 
     * @param collection the collection to count
     * @return the number of elements
     */
    public static int count(Collection<?> collection) {
        return collection.size();
    }
    
    /**
     * Checks if any collection is empty.
     * 
     * @param collection the collection to check
     * @return true if empty, false otherwise
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection.isEmpty();
    }
    
    /**
     * Clears any collection.
     * 
     * @param collection the collection to clear
     */
    public static void clear(Collection<?> collection) {
        collection.clear();
    }
    
    // ===== FUNCTIONAL STYLE METHODS =====
    
    /**
     * Transforms a list using a function.
     * Demonstrates generic method with Function parameter.
     * 
     * @param <T> the input type
     * @param <R> the result type
     * @param input the input list
     * @param transformer the transformation function
     * @return a new list with transformed elements
     */
    public static <T, R> List<R> map(List<T> input, Function<? super T, ? extends R> transformer) {
        List<R> result = new ArrayList<>(input.size());
        for (T item : input) {
            result.add(transformer.apply(item));
        }
        return result;
    }
    
    /**
     * Filters a list based on a predicate.
     * 
     * @param <T> the type of elements
     * @param input the input list
     * @param predicate the filtering predicate
     * @return a new list with filtered elements
     */
    public static <T> List<T> filter(List<T> input, Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : input) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    /**
     * Reduces a collection to a single value.
     * 
     * @param <T> the type of elements
     * @param <R> the result type
     * @param collection the collection to reduce
     * @param identity the identity value
     * @param accumulator the accumulating function
     * @return the reduced value
     */
    public static <T, R> R fold(Collection<T> collection, R identity, BiFunction<R, T, R> accumulator) {
        R result = identity;
        for (T item : collection) {
            result = accumulator.apply(result, item);
        }
        return result;
    }
    
    /**
     * Reduces a collection with no identity value.
     * 
     * @param <T> the type of elements
     * @param collection the collection to reduce
     * @param accumulator the accumulating function
     * @return an Optional containing the result, or empty if collection is empty
     */
    public static <T> Optional<T> reduce(Collection<T> collection, BinaryOperator<T> accumulator) {
        return collection.stream().reduce(accumulator);
    }
    
    // ===== ADVANCED GENERIC PATTERNS =====
    
    /**
     * Finds the first element matching a predicate.
     * 
     * @param <T> the type of elements
     * @param collection the collection to search
     * @param predicate the search predicate
     * @return an Optional containing the first match, or empty if none found
     */
    public static <T> Optional<T> findFirst(Collection<T> collection, Predicate<? super T> predicate) {
        return collection.stream().filter(predicate).findFirst();
    }
    
    /**
     * Finds the last element in a list.
     * 
     * @param <T> the type of elements
     * @param list the list to search
     * @return an Optional containing the last element, or empty if list is empty
     */
    public static <T> Optional<T> findLast(List<T> list) {
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(list.size() - 1));
    }
    
    /**
     * Reverses a list and returns a new list.
     * 
     * @param <T> the type of elements
     * @param list the list to reverse
     * @return a new reversed list
     */
    public static <T> List<T> reverse(List<T> list) {
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }
    
    /**
     * Shuffles a list and returns a new list.
     * 
     * @param <T> the type of elements
     * @param list the list to shuffle
     * @return a new shuffled list
     */
    public static <T> List<T> shuffle(List<T> list) {
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }
    
    /**
     * Groups elements by a key function.
     * Demonstrates generic method returning complex generic types.
     * 
     * @param <T> the type of elements
     * @param <K> the type of keys
     * @param collection the collection to group
     * @param keyExtractor the key extraction function
     * @return a map of keys to lists of elements
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<? super T, ? extends K> keyExtractor) {
        Map<K, List<T>> groups = new HashMap<>();
        for (T item : collection) {
            K key = keyExtractor.apply(item);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return groups;
    }
    
    /**
     * Partitions a collection based on a predicate.
     * 
     * @param <T> the type of elements
     * @param collection the collection to partition
     * @param predicate the partitioning predicate
     * @return a Pair where the key contains elements matching the predicate,
     *         and the value contains elements not matching
     */
    public static <T> Pair<List<T>, List<T>> partition(Collection<T> collection, Predicate<? super T> predicate) {
        List<T> trueList = new ArrayList<>();
        List<T> falseList = new ArrayList<>();
        
        for (T item : collection) {
            if (predicate.test(item)) {
                trueList.add(item);
            } else {
                falseList.add(item);
            }
        }
        
        return Pair.of(trueList, falseList);
    }
    
    /**
     * Zips two lists together into pairs.
     * Demonstrates generic method with multiple input types.
     * 
     * @param <T> the type of the first list elements
     * @param <U> the type of the second list elements
     * @param first the first list
     * @param second the second list
     * @return a list of pairs, with length equal to the shorter input list
     */
    public static <T, U> List<Pair<T, U>> zip(List<T> first, List<U> second) {
        List<Pair<T, U>> result = new ArrayList<>();
        int minSize = Math.min(first.size(), second.size());
        
        for (int i = 0; i < minSize; i++) {
            result.add(Pair.of(first.get(i), second.get(i)));
        }
        
        return result;
    }
    
    /**
     * Flattens a collection of collections.
     * Demonstrates nested generic types.
     * 
     * @param <T> the type of elements
     * @param collections the collection of collections to flatten
     * @return a flattened list
     */
    public static <T> List<T> flatten(Collection<? extends Collection<? extends T>> collections) {
        List<T> result = new ArrayList<>();
        for (Collection<? extends T> collection : collections) {
            result.addAll(collection);
        }
        return result;
    }
    
    // ===== TYPE WITNESS METHODS =====
    
    /**
     * Creates an empty list with explicit type.
     * Useful when type inference isn't sufficient.
     * 
     * @param <T> the type of elements
     * @param type the class object for type witness (not used at runtime)
     * @return an empty list
     */
    public static <T> List<T> emptyList(Class<T> type) {
        return new ArrayList<>();
    }
    
    /**
     * Creates an empty set with explicit type.
     * 
     * @param <T> the type of elements
     * @param type the class object for type witness
     * @return an empty set
     */
    public static <T> Set<T> emptySet(Class<T> type) {
        return new HashSet<>();
    }
    
    /**
     * Casts an object to a specific type safely.
     * Demonstrates type witness pattern for safe casting.
     * 
     * @param <T> the target type
     * @param obj the object to cast
     * @param type the target class
     * @return the cast object
     * @throws ClassCastException if the cast is invalid
     */
    public static <T> T cast(Object obj, Class<T> type) {
        return type.cast(obj);
    }
    
    // ===== UTILITY METHODS FOR DEMONSTRATION =====
    
    /**
     * Demonstrates method overloading with generics.
     * This version works with any Collection.
     * 
     * @param <T> the type of elements
     * @param collection the collection to process
     * @return a description string
     */
    public static <T> String describe(Collection<T> collection) {
        return String.format("Collection of %s with %d elements", 
                           collection.getClass().getSimpleName(), 
                           collection.size());
    }
    
    /**
     * Overloaded version that works specifically with Lists.
     * Demonstrates how generics work with method overloading.
     * 
     * @param <T> the type of elements
     * @param list the list to process
     * @return a description string
     */
    public static <T> String describe(List<T> list) {
        return String.format("List of %s with %d elements (indexed access available)", 
                           list.getClass().getSimpleName(), 
                           list.size());
    }
    
    /**
     * Demonstrates type parameter naming conventions.
     * Uses descriptive names instead of single letters.
     * 
     * @param <ElementType> the type of elements in the collection
     * @param <ResultType> the type of the transformation result
     * @param collection the input collection
     * @param transformer the transformation function
     * @return the transformed result
     */
    public static <ElementType, ResultType> ResultType transformAndAggregate(
            Collection<ElementType> collection,
            Function<Collection<ElementType>, ResultType> transformer) {
        return transformer.apply(collection);
    }
}