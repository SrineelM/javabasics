package basics.mastery.advanced;.generics.interfaces;

import java.util.*;
import java.util.function.*;

/**
 * Comprehensive examples of generic interfaces in Java.
 * 
 * This class demonstrates:
 * - Generic interface design patterns
 * - Variance in generic interfaces
 * - Functional interfaces with generics
 * - Multiple implementations with different generic constraints
 * - Generic interface inheritance
 * - Contravariance and covariance in interfaces
 * - Default methods in generic interfaces
 *
 * @author Java Generics Tutorial
 * @version 1.0
 */
public class GenericInterfaceExamples {
    
    // ===== BASIC GENERIC INTERFACES =====
    
    /**
     * Generic container interface with basic operations.
     * Demonstrates fundamental generic interface design.
     * 
     * @param <T> the type of elements stored
     */
    public interface Container<T> {
        
        /**
         * Adds an element to the container.
         * 
         * @param element the element to add
         */
        void add(T element);
        
        /**
         * Removes an element from the container.
         * 
         * @param element the element to remove
         * @return true if element was removed
         */
        boolean remove(T element);
        
        /**
         * Checks if container contains an element.
         * 
         * @param element the element to check
         * @return true if element is present
         */
        boolean contains(T element);
        
        /**
         * Returns the number of elements.
         * 
         * @return element count
         */
        int size();
        
        /**
         * Checks if container is empty.
         * 
         * @return true if empty
         */
        default boolean isEmpty() {
            return size() == 0;
        }
        
        /**
         * Clears all elements.
         */
        void clear();
        
        /**
         * Returns an iterator over elements.
         * 
         * @return iterator
         */
        Iterator<T> iterator();
        
        /**
         * Applies an action to each element.
         * Default method using generic function.
         * 
         * @param action the action to apply
         */
        default void forEach(java.util.function.Consumer<? super T> action) {
            for (Iterator<T> it = iterator(); it.hasNext();) {
                action.accept(it.next());
            }
        }
        
        /**
         * Transforms elements and collects to a list.
         * Demonstrates generic method in generic interface.
         * 
         * @param <R> the result type
         * @param mapper the transformation function
         * @return list of transformed elements
         */
        default <R> List<R> map(Function<? super T, ? extends R> mapper) {
            List<R> result = new ArrayList<>();
            forEach(element -> result.add(mapper.apply(element)));
            return result;
        }
        
        /**
         * Filters elements based on predicate.
         * 
         * @param predicate the filter condition
         * @return list of matching elements
         */
        default List<T> filter(Predicate<? super T> predicate) {
            List<T> result = new ArrayList<>();
            forEach(element -> {
                if (predicate.test(element)) {
                    result.add(element);
                }
            });
            return result;
        }
    }
    
    /**
     * Simple list-based implementation of Container.
     * 
     * @param <T> element type
     */
    public static class ListContainer<T> implements Container<T> {
        private final List<T> list = new ArrayList<>();
        
        @Override
        public void add(T element) {
            list.add(element);
        }
        
        @Override
        public boolean remove(T element) {
            return list.remove(element);
        }
        
        @Override
        public boolean contains(T element) {
            return list.contains(element);
        }
        
        @Override
        public int size() {
            return list.size();
        }
        
        @Override
        public void clear() {
            list.clear();
        }
        
        @Override
        public Iterator<T> iterator() {
            return list.iterator();
        }
        
        public List<T> toList() {
            return new ArrayList<>(list);
        }
    }
    
    // ===== PROCESSOR INTERFACES =====
    
    /**
     * Generic processor interface with input/output types.
     * Demonstrates interface with multiple type parameters.
     * 
     * @param <Input> input type
     * @param <Output> output type
     */
    public interface Processor<Input, Output> {
        
        /**
         * Processes input and produces output.
         * 
         * @param input the input to process
         * @return the processed output
         */
        Output process(Input input);
        
        /**
         * Processes a collection of inputs.
         * 
         * @param inputs input collection
         * @return collection of outputs
         */
        default Collection<Output> processAll(Collection<? extends Input> inputs) {
            List<Output> results = new ArrayList<>();
            for (Input input : inputs) {
                results.add(process(input));
            }
            return results;
        }
        
        /**
         * Chains this processor with another.
         * Demonstrates generic method composition.
         * 
         * @param <FinalOutput> the final output type
         * @param next the next processor in chain
         * @return composed processor
         */
        default <FinalOutput> Processor<Input, FinalOutput> andThen(
                Processor<? super Output, ? extends FinalOutput> next) {
            return input -> next.process(process(input));
        }
        
        /**
         * Creates a processor that applies a predicate filter.
         * 
         * @param predicate the filter condition
         * @return filtered processor
         */
        default Processor<Input, Optional<Output>> filtered(Predicate<? super Output> predicate) {
            return input -> {
                Output result = process(input);
                return predicate.test(result) ? Optional.of(result) : Optional.empty();
            };
        }
    }
    
    /**
     * String to Integer processor implementation.
     */
    public static class StringToIntegerProcessor implements Processor<String, Integer> {
        @Override
        public Integer process(String input) {
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                return 0; // Default value for invalid input
            }
        }
    }
    
    /**
     * Generic math processor for numbers.
     * 
     * @param <T> numeric type
     */
    public static class MathProcessor<T extends Number> implements Processor<T, Double> {
        private final Function<T, Double> operation;
        
        public MathProcessor(Function<T, Double> operation) {
            this.operation = operation;
        }
        
        @Override
        public Double process(T input) {
            return operation.apply(input);
        }
        
        // Factory methods for common operations
        public static <T extends Number> MathProcessor<T> square() {
            return new MathProcessor<>(n -> {
                double value = n.doubleValue();
                return value * value;
            });
        }
        
        public static <T extends Number> MathProcessor<T> sqrt() {
            return new MathProcessor<>(n -> Math.sqrt(n.doubleValue()));
        }
        
        public static <T extends Number> MathProcessor<T> abs() {
            return new MathProcessor<>(n -> Math.abs(n.doubleValue()));
        }
    }
    
    // ===== REPOSITORY INTERFACES =====
    
    /**
     * Generic repository interface for data access.
     * Demonstrates CRUD operations with generics.
     * 
     * @param <T> entity type
     * @param <ID> identifier type
     */
    public interface Repository<T, ID> {
        
        /**
         * Saves an entity.
         * 
         * @param entity the entity to save
         * @return the saved entity
         */
        T save(T entity);
        
        /**
         * Finds entity by ID.
         * 
         * @param id the identifier
         * @return optional entity
         */
        Optional<T> findById(ID id);
        
        /**
         * Finds all entities.
         * 
         * @return all entities
         */
        List<T> findAll();
        
        /**
         * Deletes entity by ID.
         * 
         * @param id the identifier
         * @return true if deleted
         */
        boolean deleteById(ID id);
        
        /**
         * Checks if entity exists.
         * 
         * @param id the identifier
         * @return true if exists
         */
        boolean existsById(ID id);
        
        /**
         * Counts all entities.
         * 
         * @return entity count
         */
        long count();
        
        /**
         * Finds entities matching criteria.
         * 
         * @param predicate search criteria
         * @return matching entities
         */
        default List<T> findBy(Predicate<? super T> predicate) {
            return findAll().stream()
                    .filter(predicate)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
        
        /**
         * Saves multiple entities.
         * 
         * @param entities entities to save
         * @return saved entities
         */
        default List<T> saveAll(Collection<? extends T> entities) {
            List<T> saved = new ArrayList<>();
            for (T entity : entities) {
                saved.add(save(entity));
            }
            return saved;
        }
        
        /**
         * Deletes multiple entities by ID.
         * 
         * @param ids identifiers to delete
         * @return number of deleted entities
         */
        default int deleteAllById(Collection<? extends ID> ids) {
            int count = 0;
            for (ID id : ids) {
                if (deleteById(id)) {
                    count++;
                }
            }
            return count;
        }
    }
    
    /**
     * In-memory implementation of Repository.
     * 
     * @param <T> entity type
     * @param <ID> identifier type
     */
    public static class InMemoryRepository<T extends Identifiable<ID>, ID> 
            implements Repository<T, ID> {
        
        private final Map<ID, T> storage = new HashMap<>();
        
        @Override
        public T save(T entity) {
            storage.put(entity.getId(), entity);
            return entity;
        }
        
        @Override
        public Optional<T> findById(ID id) {
            return Optional.ofNullable(storage.get(id));
        }
        
        @Override
        public List<T> findAll() {
            return new ArrayList<>(storage.values());
        }
        
        @Override
        public boolean deleteById(ID id) {
            return storage.remove(id) != null;
        }
        
        @Override
        public boolean existsById(ID id) {
            return storage.containsKey(id);
        }
        
        @Override
        public long count() {
            return storage.size();
        }
    }
    
    // ===== FUNCTIONAL GENERIC INTERFACES =====
    
    /**
     * Generic transformer interface with exception handling.
     * 
     * @param <T> input type
     * @param <R> result type
     * @param <E> exception type
     */
    @FunctionalInterface
    public interface ThrowingTransformer<T, R, E extends Exception> {
        
        /**
         * Transforms input to result, potentially throwing exception.
         * 
         * @param input the input
         * @return the result
         * @throws E if transformation fails
         */
        R transform(T input) throws E;
        
        /**
         * Transforms input, wrapping exceptions in RuntimeException.
         * 
         * @param input the input
         * @return the result
         */
        default R safeTransform(T input) {
            try {
                return transform(input);
            } catch (Exception e) {
                throw new RuntimeException("Transformation failed", e);
            }
        }
        
        /**
         * Transforms input, returning Optional for exception cases.
         * 
         * @param input the input
         * @return optional result
         */
        default Optional<R> tryTransform(T input) {
            try {
                return Optional.of(transform(input));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        
        /**
         * Chains this transformer with another.
         * 
         * @param <U> next result type
         * @param <F> next exception type
         * @param next the next transformer
         * @return chained transformer
         */
        default <U, F extends Exception> ThrowingTransformer<T, U, Exception> 
                andThen(ThrowingTransformer<? super R, ? extends U, F> next) {
            return input -> next.transform(transform(input));
        }
    }
    
    /**
     * Generic validator interface.
     * 
     * @param <T> type to validate
     */
    @FunctionalInterface
    public interface Validator<T> {
        
        /**
         * Validates an object.
         * 
         * @param object the object to validate
         * @return validation result
         */
        ValidationResult validate(T object);
        
        /**
         * Combines this validator with another using AND logic.
         * 
         * @param other the other validator
         * @return combined validator
         */
        default Validator<T> and(Validator<? super T> other) {
            return object -> {
                ValidationResult first = validate(object);
                if (!first.isValid()) {
                    return first;
                }
                return other.validate(object);
            };
        }
        
        /**
         * Combines this validator with another using OR logic.
         * 
         * @param other the other validator
         * @return combined validator
         */
        default Validator<T> or(Validator<? super T> other) {
            return object -> {
                ValidationResult first = validate(object);
                if (first.isValid()) {
                    return first;
                }
                return other.validate(object);
            };
        }
        
        /**
         * Creates a validator that always passes.
         * 
         * @param <T> any type
         * @return always valid validator
         */
        static <T> Validator<T> alwaysValid() {
            return obj -> ValidationResult.valid();
        }
        
        /**
         * Creates a validator that always fails.
         * 
         * @param <T> any type
         * @param message failure message
         * @return always invalid validator
         */
        static <T> Validator<T> alwaysInvalid(String message) {
            return obj -> ValidationResult.invalid(message);
        }
    }
    
    // ===== BUILDER INTERFACES =====
    
    /**
     * Generic builder interface with fluent API.
     * 
     * @param <T> the type being built
     * @param <B> the builder type (for method chaining)
     */
    public interface Builder<T, B extends Builder<T, B>> {
        
        /**
         * Builds the final object.
         * 
         * @return the built object
         */
        T build();
        
        /**
         * Validates the builder state.
         * 
         * @return validation result
         */
        default ValidationResult validate() {
            return ValidationResult.valid();
        }
        
        /**
         * Builds with validation.
         * 
         * @return the built object
         * @throws IllegalStateException if validation fails
         */
        default T buildWithValidation() {
            ValidationResult result = validate();
            if (!result.isValid()) {
                throw new IllegalStateException("Builder validation failed: " + result.getMessage());
            }
            return build();
        }
        
        /**
         * Returns this builder cast to the concrete type.
         * Enables proper method chaining.
         * 
         * @return this builder
         */
        @SuppressWarnings("unchecked")
        default B self() {
            return (B) this;
        }
    }
    
    /**
     * Concrete builder for creating persons.
     */
    public static class PersonBuilder implements Builder<Person, PersonBuilder> {
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
        public ValidationResult validate() {
            if (name == null || name.trim().isEmpty()) {
                return ValidationResult.invalid("Name is required");
            }
            if (age < 0 || age > 150) {
                return ValidationResult.invalid("Age must be between 0 and 150");
            }
            if (email == null || !email.contains("@")) {
                return ValidationResult.invalid("Valid email is required");
            }
            return ValidationResult.valid();
        }
        
        @Override
        public Person build() {
            return new Person(name, age, email);
        }
    }
    
    // ===== SUPPORTING CLASSES AND INTERFACES =====
    
    /**
     * Interface for objects with identifiers.
     * 
     * @param <ID> identifier type
     */
    public interface Identifiable<ID> {
        ID getId();
    }
    
    /**
     * Simple person class.
     */
    public static class Person implements Identifiable<String> {
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
            return email;
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
     * Validation result class.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
        
        @Override
        public String toString() {
            return valid ? "Valid" : "Invalid: " + message;
        }
    }
    
    // ===== VARIANCE EXAMPLES =====
    
    /**
     * Producer interface demonstrating covariance.
     * Can safely return subtypes.
     * 
     * @param <T> produced type
     */
    public interface Producer<T> {
        T produce();
        
        /**
         * Produces multiple items.
         * 
         * @param count number of items
         * @return list of produced items
         */
        default List<T> produceMany(int count) {
            List<T> items = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                items.add(produce());
            }
            return items;
        }
    }
    
    /**
     * Custom consumer interface demonstrating contravariance.
     * Can safely accept supertypes.
     * 
     * @param <T> consumed type
     */
    public interface CustomConsumer<T> {
        void consume(T item);
        
        /**
         * Consumes multiple items.
         * 
         * @param items items to consume
         */
        default void consumeAll(Collection<? extends T> items) {
            for (T item : items) {
                consume(item);
            }
        }
        
        /**
         * Chains this consumer with another.
         * 
         * @param after consumer to run after this one
         * @return chained consumer
         */
        default CustomConsumer<T> andThen(CustomConsumer<? super T> after) {
            return item -> {
                consume(item);
                after.consume(item);
            };
        }
    }
}