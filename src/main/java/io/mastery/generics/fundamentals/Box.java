package io.mastery.generics.fundamentals;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Enhanced generic container demonstrating type-safe storage and operations.
 * Showcases fundamental generic concepts including type parameters,
 * type safety, and functional-style operations.
 *
 * This implementation demonstrates:
 * - Generic type parameters
 * - Type safety at compile time
 * - Functional programming patterns with generics
 * - Optional-like operations
 * - Monadic operations (map, flatMap)
 *
 * @param <T> the type of value stored in this box
 * @author Java Generics Tutorial
 * @version 1.0
 */
public final class Box<T> {
    // STUDENT NOTE: This class demonstrates the use of a single generic type parameter (T) to create a type-safe container.
    // STUDENT NOTE: The content field holds the value of type T. It can be any object or null.
    private T content;
    
    // STUDENT NOTE: The constructor is private to enforce the use of static factory methods for object creation.
    private Box(T content) {
        this.content = content;
    }
    
    /**
     * Creates a new Box containing the specified value.
     * 
     * @param <T> the type of the value
     * @param content the value to store (can be null)
     * @return a new Box containing the value
     */
    // STUDENT NOTE: Use Box.of(value) to create a new Box. This is a generic static factory method.
    public static <T> Box<T> of(T content) {
        return new Box<>(content);
    }
    
    /**
     * Creates an empty Box (containing null).
     * 
     * @param <T> the type parameter for the empty box
     * @return an empty Box
     */
    // STUDENT NOTE: Box.empty() creates a Box with null content. Useful for representing absence of a value.
    public static <T> Box<T> empty() {
        return new Box<>(null);
    }
    
    /**
     * Creates a Box from a Supplier that will be evaluated lazily.
     * 
     * @param <T> the type of the value
     * @param supplier the supplier to evaluate
     * @return a new Box containing the supplied value
     */
    // STUDENT NOTE: fromSupplier allows lazy evaluation. The value is supplied when the Box is created.
    public static <T> Box<T> fromSupplier(Supplier<T> supplier) {
        return new Box<>(supplier.get());
    }
    
    /**
     * Gets the content of this Box.
     * 
     * @return the content (may be null)
     */
    // STUDENT NOTE: get() returns the value inside the Box. It may be null.
    public T get() {
        return content;
    }
    
    /**
     * Sets the content of this Box.
     * 
     * @param content the new content
     */
    // STUDENT NOTE: set() changes the value inside the Box. This makes Box mutable.
    public void set(T content) {
        this.content = content;
    }
    
    /**
     * Returns an Optional containing the content if present.
     * 
     * @return Optional containing the content
     */
    // STUDENT NOTE: getOptional() wraps the content in an Optional for safe handling of null values.
    public Optional<T> getOptional() {
        return Optional.ofNullable(content);
    }
    
    /**
     * Checks if this Box is empty (content is null).
     * 
     * @return true if empty, false otherwise
     */
    // STUDENT NOTE: isEmpty() returns true if the Box contains null.
    public boolean isEmpty() {
        return content == null;
    }
    
    /**
     * Checks if this Box contains a non-null value.
     * 
     * @return true if contains value, false otherwise
     */
    // STUDENT NOTE: isPresent() returns true if the Box contains a non-null value.
    public boolean isPresent() {
        return content != null;
    }
    
    /**
     * Executes the given action if a value is present.
     * 
     * @param action the action to execute
     */
    // STUDENT NOTE: ifPresent() executes the given action if the Box contains a value.
    public void ifPresent(Consumer<? super T> action) {
        if (content != null) {
            action.accept(content);
        }
    }
    
    /**
     * Executes the given action if the Box is empty.
     * 
     * @param action the action to execute
     */
    // STUDENT NOTE: ifEmpty() executes the given action if the Box is empty (null).
    public void ifEmpty(Runnable action) {
        if (content == null) {
            action.run();
        }
    }
    
    /**
     * Tests the content against the given predicate.
     * 
     * @param predicate the predicate to test
     * @return true if content is non-null and passes the test
     */
    // STUDENT NOTE: test() applies a predicate to the content if present.
    public boolean test(Predicate<? super T> predicate) {
        return content != null && predicate.test(content);
    }
    
    /**
     * Applies a function to the content if present and returns a new Box.
     * This demonstrates the map operation from functional programming.
     * 
     * @param <R> the type of the result
     * @param mapper the function to apply
     * @return a new Box containing the result, or empty if this Box is empty
     */
    // STUDENT NOTE: map() transforms the content using a function, returning a new Box with the result.
    public <R> Box<R> map(Function<? super T, ? extends R> mapper) {
        return content != null ? Box.of(mapper.apply(content)) : Box.empty();
    }
    
    /**
     * Applies a function that returns a Box and flattens the result.
     * This demonstrates the flatMap operation from functional programming.
     * 
     * @param <R> the type of the result
     * @param mapper the function to apply
     * @return the result Box, or empty if this Box is empty
     */
    // STUDENT NOTE: flatMap() is used for chaining operations that return Box. This is a monadic operation.
    public <R> Box<R> flatMap(Function<? super T, Box<R>> mapper) {
        return content != null ? mapper.apply(content) : Box.empty();
    }
    
    /**
     * Filters the content based on a predicate.
     * 
     * @param predicate the predicate to test
     * @return this Box if predicate passes, empty Box otherwise
     */
    // STUDENT NOTE: filter() returns this Box if the predicate passes, or an empty Box otherwise.
    public Box<T> filter(Predicate<? super T> predicate) {
        return test(predicate) ? this : Box.empty();
    }
    
    /**
     * Returns the content if present, otherwise returns the provided default.
     * 
     * @param defaultValue the default value
     * @return the content or default value
     */
    // STUDENT NOTE: orElse() provides a default value if the Box is empty.
    public T orElse(T defaultValue) {
        return content != null ? content : defaultValue;
    }
    
    /**
     * Returns the content if present, otherwise returns the result of the supplier.
     * 
     * @param supplier the supplier for the default value
     * @return the content or supplied value
     */
    // STUDENT NOTE: orElseGet() provides a default value from a Supplier if the Box is empty.
    public T orElseGet(Supplier<? extends T> supplier) {
        return content != null ? content : supplier.get();
    }
    
    /**
     * Returns the content if present, otherwise throws the provided exception.
     * 
     * @param <X> the type of exception
     * @param exceptionSupplier the supplier for the exception
     * @return the content
     * @throws X if content is null
     */
    // STUDENT NOTE: orElseThrow() throws an exception if the Box is empty. Useful for enforcing non-null values.
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (content != null) {
            return content;
        } else {
            throw exceptionSupplier.get();
        }
    }
    
    /**
     * Combines this Box with another Box using a binary function.
     * 
     * @param <U> the type of the other Box
     * @param <R> the result type
     * @param other the other Box
     * @param combiner the combining function
     * @return a new Box with the combined result, or empty if either Box is empty
     */
    // STUDENT NOTE: combine() merges two Boxes using a function, returning a new Box with the result.
    public <U, R> Box<R> combine(Box<U> other, Function<? super T, Function<? super U, ? extends R>> combiner) {
        return flatMap(t -> other.map(u -> combiner.apply(t).apply(u)));
    }
    
    /**
     * Demonstrates variance - creates a Box containing a list of this Box's content.
     * 
     * @return a new Box containing a singleton list
     */
    // STUDENT NOTE: asList() wraps the content in a singleton list inside a Box.
    public Box<java.util.List<T>> asList() {
        return map(java.util.Collections::singletonList);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box<?> box = (Box<?>) o;
        return Objects.equals(content, box.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
    
    @Override
    public String toString() {
        return "Box[" + content + "]";
    }
    
    /**
     * Demonstrates generic method with multiple type parameters.
     * Combines two Boxes into a Pair.
     * 
     * @param <T> type of first Box
     * @param <U> type of second Box
     * @param first the first Box
     * @param second the second Box
     * @return a Box containing a Pair, or empty if either input is empty
     */
    // STUDENT NOTE: zip() combines two Boxes into a Box of Pair, or returns empty if either is empty.
    public static <T, U> Box<Pair<T, U>> zip(Box<T> first, Box<U> second) {
        return first.combine(second, t -> u -> Pair.of(t, u));
    }
    
    /**
     * Lifts a regular function to work with Boxes.
     * This demonstrates how to make regular functions work with generic containers.
     * 
     * @param <T> input type
     * @param <R> result type
     * @param function the function to lift
     * @return a function that works with Boxes
     */
    // STUDENT NOTE: lift() adapts a function to operate on Boxes, returning a function from Box<T> to Box<R>.
    public static <T, R> Function<Box<T>, Box<R>> lift(Function<T, R> function) {
        return box -> box.map(function);
    }
}