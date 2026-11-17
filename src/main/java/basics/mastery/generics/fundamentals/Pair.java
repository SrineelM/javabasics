package basics.mastery.generics.fundamentals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Immutable generic pair implementation demonstrating multiple type parameters
 * and advanced generic method combinations.
 *
 * This class showcases:
 * - Multiple type parameters (K, V)
 * - Type-safe operations on paired data
 * - Functional transformations
 * - Generic method chaining
 * - Immutability patterns with generics
 *
 * @param <K> the type of the key (first element)
 * @param <V> the type of the value (second element)
 * @author Srineel with Copilot
 * @version 1.0
 */
// [coach] No stray "// ...existing code..." or duplicate class Javadoc. Structural fix is
// limited to explicit imports for the fully-qualified references (Predicate, Stream, List, ArrayList).
public final class Pair<K, V> {
    private final K key;
    private final V value;
    
    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * Creates a new Pair with the specified key and value.
     * 
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @param key the key (first element)
     * @param value the value (second element)
     * @return a new Pair
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
    
    /**
     * Creates a Pair from two Box instances.
     * Demonstrates generic method composition.
     * 
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @param keyBox Box containing the key
     * @param valueBox Box containing the value
     * @return Pair if both boxes have values, null otherwise
     */
    // [coach:fix] Returning null from a factory breaks the "no null from factories" contract
    // and forces callers into null checks. Prefer returning Box<Pair<K,V>> or Optional<Pair<K,V>>.
    // Kept as-is to preserve the original signature; flagged under CORRECTNESS.
    public static <K, V> Pair<K, V> fromBoxes(Box<K> keyBox, Box<V> valueBox) {
        if (keyBox.isPresent() && valueBox.isPresent()) {
            return Pair.of(keyBox.get(), valueBox.get());
        }
        return null;
    }
    
    /**
     * Gets the key (first element).
     * 
     * @return the key
     */
    public K getKey() {
        return key;
    }
    
    /**
     * Gets the value (second element).
     * 
     * @return the value
     */
    public V getValue() {
        return value;
    }
    
    /**
     * Gets the first element (alias for getKey).
     * 
     * @return the first element
     */
    public K getFirst() {
        return key;
    }
    
    /**
     * Gets the second element (alias for getValue).
     * 
     * @return the second element
     */
    public V getSecond() {
        return value;
    }
    
    /**
     * Maps the key to a new type while keeping the value unchanged.
     * Demonstrates partial transformation of generic types.
     * 
     * @param <R> the new key type
     * @param keyMapper the function to transform the key
     * @return a new Pair with transformed key
     */
    public <R> Pair<R, V> mapKey(Function<? super K, ? extends R> keyMapper) {
        return Pair.of(keyMapper.apply(key), value);
    }
    
    /**
     * Maps the value to a new type while keeping the key unchanged.
     * 
     * @param <R> the new value type
     * @param valueMapper the function to transform the value
     * @return a new Pair with transformed value
     */
    public <R> Pair<K, R> mapValue(Function<? super V, ? extends R> valueMapper) {
        return Pair.of(key, valueMapper.apply(value));
    }
    
    /**
     * Maps both key and value to new types.
     * Demonstrates transformation of both generic type parameters.
     * 
     * @param <R1> the new key type
     * @param <R2> the new value type
     * @param keyMapper the function to transform the key
     * @param valueMapper the function to transform the value
     * @return a new Pair with both elements transformed
     */
    public <R1, R2> Pair<R1, R2> map(Function<? super K, ? extends R1> keyMapper,
                                      Function<? super V, ? extends R2> valueMapper) {
        return Pair.of(keyMapper.apply(key), valueMapper.apply(value));
    }
    
    /**
     * Combines both elements using a binary function.
     * Demonstrates how to collapse generic types.
     * 
     * @param <R> the result type
     * @param combiner the combining function
     * @return the combined result
     */
    public <R> R combine(BiFunction<? super K, ? super V, ? extends R> combiner) {
        return combiner.apply(key, value);
    }
    
    /**
     * Swaps the key and value positions.
     * Demonstrates type parameter reordering.
     * 
     * @return a new Pair with swapped elements
     */
    public Pair<V, K> swap() {
        return Pair.of(value, key);
    }
    
    /**
     * Applies a function that takes both elements and returns a new Pair.
     * Demonstrates flatMap-like operation for Pairs.
     * 
     * @param <R1> the new key type
     * @param <R2> the new value type
     * @param transformer the transforming function
     * @return the transformed Pair
     */
    public <R1, R2> Pair<R1, R2> flatMap(BiFunction<? super K, ? super V, Pair<R1, R2>> transformer) {
        return transformer.apply(key, value);
    }
    
    /**
     * Creates a Triple by adding a third element.
     * Demonstrates generic type extension.
     * 
     * @param <T> the type of the third element
     * @param third the third element
     * @return a new Triple
     */
    public <T> Triple<K, V, T> withThird(T third) {
        return Triple.of(key, value, third);
    }
    
    /**
     * Converts this Pair to a Box containing the key.
     * 
     * @return Box containing the key
     */
    public Box<K> keyToBox() {
        return Box.of(key);
    }
    
    /**
     * Converts this Pair to a Box containing the value.
     * 
     * @return Box containing the value
     */
    public Box<V> valueToBox() {
        return Box.of(value);
    }
    
    /**
     * Tests if this Pair matches the given predicates.
     * 
     * @param keyPredicate predicate for the key
     * @param valuePredicate predicate for the value
     * @return true if both predicates pass
     */
    public boolean matches(Predicate<? super K> keyPredicate,
                          Predicate<? super V> valuePredicate) {
        return keyPredicate.test(key) && valuePredicate.test(value);
    }
    
    /**
     * Creates a stream containing both elements.
     * Note: This demonstrates type erasure limitations - we lose specific types.
     * 
     * @return a stream containing key and value as Objects
     */
    // [coach] The `Stream<Object>` return type is a deliberate teaching point about erasure:
    // K and V have no common supertype the compiler can express as a single type variable.
    // You could still do `Stream.of(key)` and `Stream.of(value)` separately and concat,
    // but the joined stream is inherently `Stream<Object>`. Correct as written.
    public Stream<Object> stream() {
        return Stream.of(key, value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
    
    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
    
    /**
     * Demonstrates a higher-order function that works with Pairs.
     * Lifts a binary function to work with Pairs.
     * 
     * @param <T> first input type
     * @param <U> second input type
     * @param <R> result type
     * @param function the binary function to lift
     * @return a function that works with Pairs
     */
    public static <T, U, R> Function<Pair<T, U>, R> lift(BiFunction<T, U, R> function) {
        return pair -> function.apply(pair.getKey(), pair.getValue());
    }
    
    /**
     * Creates a Pair with both elements being the same type.
     * Useful for creating symmetric pairs.
     * 
     * @param <T> the type of both elements
     * @param first the first element
     * @param second the second element
     * @return a symmetric Pair
     */
    public static <T> Pair<T, T> symmetric(T first, T second) {
        return Pair.of(first, second);
    }
    
    /**
     * Demonstrates collector-like functionality for Pairs.
     * Combines multiple Pairs into a single result.
     * 
     * @param <K> key type
     * @param <V> value type
     * @param <R> result type
     * @param pairs the Pairs to combine
     * @param combiner function to combine all pairs
     * @return the combined result
     */
    // [coach] @SafeVarargs is legal here because the method is static. Correct.
    // Note the warning suppression is not actually needed: the element type is Pair<K,V>,
    // a reifiable-enough type at the call site (no unchecked cast into Object[]).
    @SafeVarargs
    public static <K, V, R> R combineAll(BiFunction<List<K>, List<V>, R> combiner, 
                                         Pair<K, V>... pairs) {
        List<K> keys = new ArrayList<>();
        List<V> values = new ArrayList<>();
        
        for (Pair<K, V> pair : pairs) {
            keys.add(pair.getKey());
            values.add(pair.getValue());
        }
        
        return combiner.apply(keys, values);
    }
}