package io.mastery.generics.fundamentals;

import java.util.Objects;
import java.util.function.Function;

/**
 * Generic triple demonstrating three type parameters and complex generic relationships.
 * 
 * This class showcases:
 * - Three type parameters (A, B, C)
 * - Complex generic transformations
 * - Type-safe tuple operations
 * - Conversion between different generic containers
 * - Advanced generic method signatures
 *
 * @param <A> the type of the first element
 * @param <B> the type of the second element
 * @param <C> the type of the third element
 * @author Java Generics Tutorial
 * @version 1.0
 */
public final class Triple<A, B, C> {
    private final A first;
    private final B second;
    private final C third;
    
    private Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    /**
     * Creates a new Triple with the specified elements.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <C> the type of the third element
     * @param first the first element
     * @param second the second element
     * @param third the third element
     * @return a new Triple
     */
    public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
        return new Triple<>(first, second, third);
    }
    
    /**
     * Creates a Triple from three Box instances.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <C> the type of the third element
     * @param firstBox Box containing the first element
     * @param secondBox Box containing the second element
     * @param thirdBox Box containing the third element
     * @return Triple if all boxes have values, null otherwise
     */
    public static <A, B, C> Triple<A, B, C> fromBoxes(Box<A> firstBox, Box<B> secondBox, Box<C> thirdBox) {
        if (firstBox.isPresent() && secondBox.isPresent() && thirdBox.isPresent()) {
            return Triple.of(firstBox.get(), secondBox.get(), thirdBox.get());
        }
        return null;
    }
    
    /**
     * Creates a Triple where all elements are the same type.
     * 
     * @param <T> the type of all elements
     * @param first the first element
     * @param second the second element
     * @param third the third element
     * @return a homogeneous Triple
     */
    public static <T> Triple<T, T, T> homogeneous(T first, T second, T third) {
        return Triple.of(first, second, third);
    }
    
    /**
     * Gets the first element.
     * 
     * @return the first element
     */
    public A getFirst() { 
        return first; 
    }
    
    /**
     * Gets the second element.
     * 
     * @return the second element
     */
    public B getSecond() { 
        return second; 
    }
    
    /**
     * Gets the third element.
     * 
     * @return the third element
     */
    public C getThird() { 
        return third; 
    }
    
    /**
     * Extracts the first two elements as a Pair.
     * Demonstrates generic type reduction.
     * 
     * @return a Pair containing the first two elements
     */
    public Pair<A, B> getFirstTwo() {
        return Pair.of(first, second);
    }
    
    /**
     * Extracts the last two elements as a Pair.
     * 
     * @return a Pair containing the last two elements
     */
    public Pair<B, C> getLastTwo() {
        return Pair.of(second, third);
    }
    
    /**
     * Extracts the first and third elements as a Pair.
     * 
     * @return a Pair containing the first and third elements
     */
    public Pair<A, C> getFirstAndThird() {
        return Pair.of(first, third);
    }
    
    /**
     * Maps the first element to a new type.
     * 
     * @param <R> the new type for the first element
     * @param mapper the mapping function
     * @return a new Triple with transformed first element
     */
    public <R> Triple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper) {
        return Triple.of(mapper.apply(first), second, third);
    }
    
    /**
     * Maps the second element to a new type.
     * 
     * @param <R> the new type for the second element
     * @param mapper the mapping function
     * @return a new Triple with transformed second element
     */
    public <R> Triple<A, R, C> mapSecond(Function<? super B, ? extends R> mapper) {
        return Triple.of(first, mapper.apply(second), third);
    }
    
    /**
     * Maps the third element to a new type.
     * 
     * @param <R> the new type for the third element
     * @param mapper the mapping function
     * @return a new Triple with transformed third element
     */
    public <R> Triple<A, B, R> mapThird(Function<? super C, ? extends R> mapper) {
        return Triple.of(first, second, mapper.apply(third));
    }
    
    /**
     * Maps all three elements to new types.
     * Demonstrates simultaneous transformation of multiple type parameters.
     * 
     * @param <R1> the new type for the first element
     * @param <R2> the new type for the second element
     * @param <R3> the new type for the third element
     * @param firstMapper mapper for the first element
     * @param secondMapper mapper for the second element
     * @param thirdMapper mapper for the third element
     * @return a new Triple with all elements transformed
     */
    public <R1, R2, R3> Triple<R1, R2, R3> map(Function<? super A, ? extends R1> firstMapper,
                                               Function<? super B, ? extends R2> secondMapper,
                                               Function<? super C, ? extends R3> thirdMapper) {
        return Triple.of(
            firstMapper.apply(first),
            secondMapper.apply(second), 
            thirdMapper.apply(third)
        );
    }
    
    /**
     * Combines all three elements using a ternary function.
     * 
     * @param <R> the result type
     * @param combiner the combining function
     * @return the combined result
     */
    public <R> R combine(TriFunction<? super A, ? super B, ? super C, ? extends R> combiner) {
        return combiner.apply(first, second, third);
    }
    
    /**
     * Rotates the elements: (a,b,c) -> (c,a,b).
     * Demonstrates type parameter reordering.
     * 
     * @return a new Triple with rotated elements
     */
    public Triple<C, A, B> rotateLeft() {
        return Triple.of(third, first, second);
    }
    
    /**
     * Rotates the elements: (a,b,c) -> (b,c,a).
     * 
     * @return a new Triple with rotated elements
     */
    public Triple<B, C, A> rotateRight() {
        return Triple.of(second, third, first);
    }
    
    /**
     * Reverses the order of elements: (a,b,c) -> (c,b,a).
     * 
     * @return a new Triple with reversed elements
     */
    public Triple<C, B, A> reverse() {
        return Triple.of(third, second, first);
    }
    
    /**
     * Converts each element to a Box.
     * 
     * @return a Triple of Boxes
     */
    public Triple<Box<A>, Box<B>, Box<C>> toBoxes() {
        return Triple.of(Box.of(first), Box.of(second), Box.of(third));
    }
    
    /**
     * Creates a stream containing all three elements.
     * Note: Type information is lost due to type erasure.
     * 
     * @return a stream containing all elements as Objects
     */
    public java.util.stream.Stream<Object> stream() {
        return java.util.stream.Stream.of(first, second, third);
    }
    
    /**
     * Tests if all elements match their respective predicates.
     * 
     * @param firstPredicate predicate for the first element
     * @param secondPredicate predicate for the second element
     * @param thirdPredicate predicate for the third element
     * @return true if all predicates pass
     */
    public boolean matchesAll(java.util.function.Predicate<? super A> firstPredicate,
                             java.util.function.Predicate<? super B> secondPredicate,
                             java.util.function.Predicate<? super C> thirdPredicate) {
        return firstPredicate.test(first) && 
               secondPredicate.test(second) && 
               thirdPredicate.test(third);
    }
    
    /**
     * Applies different functions to each element and collects results into a list.
     * Demonstrates heterogeneous transformation with homogeneous result.
     * 
     * @param <R> the common result type
     * @param firstMapper mapper for the first element
     * @param secondMapper mapper for the second element
     * @param thirdMapper mapper for the third element
     * @return a list containing the mapped results
     */
    public <R> java.util.List<R> mapToList(Function<? super A, ? extends R> firstMapper,
                                          Function<? super B, ? extends R> secondMapper,
                                          Function<? super C, ? extends R> thirdMapper) {
        return java.util.Arrays.asList(
            firstMapper.apply(first),
            secondMapper.apply(second),
            thirdMapper.apply(third)
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(first, triple.first) &&
               Objects.equals(second, triple.second) &&
               Objects.equals(third, triple.third);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
    
    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
    
    /**
     * Functional interface for functions that take three arguments.
     * Demonstrates creating custom generic functional interfaces.
     * 
     * @param <T> first argument type
     * @param <U> second argument type
     * @param <V> third argument type
     * @param <R> result type
     */
    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        /**
         * Applies this function to the given arguments.
         * 
         * @param t the first argument
         * @param u the second argument
         * @param v the third argument
         * @return the function result
         */
        R apply(T t, U u, V v);
        
        /**
         * Composes this function with another function.
         * 
         * @param <S> the type of the final result
         * @param after the function to apply after this one
         * @return a composed function
         */
        default <S> TriFunction<T, U, V, S> andThen(Function<? super R, ? extends S> after) {
            Objects.requireNonNull(after);
            return (t, u, v) -> after.apply(apply(t, u, v));
        }
    }
    
    /**
     * Demonstrates a higher-order function working with Triples.
     * Lifts a ternary function to work with Triples.
     * 
     * @param <A> first input type
     * @param <B> second input type
     * @param <C> third input type
     * @param <R> result type
     * @param function the ternary function to lift
     * @return a function that works with Triples
     */
    public static <A, B, C, R> Function<Triple<A, B, C>, R> lift(TriFunction<A, B, C, R> function) {
        return triple -> function.apply(triple.getFirst(), triple.getSecond(), triple.getThird());
    }
    
    /**
     * Creates a Triple by zipping three separate values.
     * 
     * @param <A> type of first value
     * @param <B> type of second value
     * @param <C> type of third value
     * @param first the first value
     * @param second the second value
     * @param third the third value
     * @return a new Triple
     */
    public static <A, B, C> Triple<A, B, C> zip(A first, B second, C third) {
        return Triple.of(first, second, third);
    }
}