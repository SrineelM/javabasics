package basics.mastery.generics;

import basics.mastery.generics.fundamentals.Box;
import basics.mastery.generics.fundamentals.Pair;
import basics.mastery.generics.fundamentals.GenericMethods;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for Java Generics
 * 
 * Tests cover generic classes, methods, bounded types, wildcards,
 * type inference, and PECS (Producer Extends Consumer Super) principles.
 * 
 * @author Srineel with Copilot
 */
@DisplayName("Generics Tests")
class GenericsTest {

    @Nested
    @DisplayName("Generic Class Tests")
    class GenericClassTests {

        @Test
        @DisplayName("Box stores and retrieves typed value")
        void testBox() {
            Box<String> stringBox = Box.of("Hello");
            
            assertThat(stringBox.get()).isEqualTo("Hello");
            assertThat(stringBox.get()).isInstanceOf(String.class);
        }

        @Test
        @DisplayName("Box can hold any type")
        void testBoxWithDifferentTypes() {
            Box<Integer> intBox = Box.of(42);
            assertThat(intBox.get()).isEqualTo(42);
            
            Box<Double> doubleBox = Box.of(3.14);
            assertThat(doubleBox.get()).isEqualTo(3.14);
        }

        @Test
        @DisplayName("Pair stores two typed values")
        void testPair() {
            Pair<String, Integer> pair = Pair.of("Age", 30);
            
            assertThat(pair.getFirst()).isEqualTo("Age");
            assertThat(pair.getSecond()).isEqualTo(30);
        }

        @Test
        @DisplayName("Pair can have different types")
        void testPairDifferentTypes() {
            Pair<String, String> stringPair = Pair.of("Key", "Value");
            assertThat(stringPair.getFirst()).isEqualTo("Key");
            assertThat(stringPair.getSecond()).isEqualTo("Value");
            
            Pair<Integer, Integer> intPair = Pair.of(1, 2);
            assertThat(intPair.getFirst()).isEqualTo(1);
            assertThat(intPair.getSecond()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Generic Method Tests")
    class GenericMethodTests {

        @Test
        @DisplayName("Generic swap method works with any type")
        void testGenericSwap() {
            String[] strings = {"A", "B"};
            GenericMethods.swap(strings, 0, 1);
            assertThat(strings).containsExactly("B", "A");
            
            Integer[] numbers = {1, 2};
            GenericMethods.swap(numbers, 0, 1);
            assertThat(numbers).containsExactly(2, 1);
        }

        @Test
        @DisplayName("Type inference works with generic methods")
        void testTypeInference() {
            // Type is inferred from context
            List<String> strings = GenericMethods.listOf("A", "B", "C");
            assertThat(strings).containsExactly("A", "B", "C");
            
            List<Integer> numbers = GenericMethods.listOf(1, 2, 3);
            assertThat(numbers).containsExactly(1, 2, 3);
        }
    }

    @Nested
    @DisplayName("Bounded Type Tests")
    class BoundedTypeTests {

        @Test
        @DisplayName("Upper bounded type accepts subclasses")
        void testUpperBound() {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5);
            Integer max = findMax(numbers);
            assertThat(max).isEqualTo(5);
        }

        @Test
        @DisplayName("Upper bounded wildcard allows reading")
        void testUpperBoundedWildcard() {
            List<Integer> integers = List.of(1, 2, 3);
            List<? extends Number> numbers = integers;
            
            // Can read as Number
            Number first = numbers.get(0);
            assertThat(first).isEqualTo(1);
            
            // Cannot write (except null)
            // numbers.add(4); // Compilation error
        }

        private <T extends Comparable<T>> T findMax(List<T> list) {
            if (list.isEmpty()) {
                throw new IllegalArgumentException("List cannot be empty");
            }
            T max = list.get(0);
            for (T element : list) {
                if (element.compareTo(max) > 0) {
                    max = element;
                }
            }
            return max;
        }
    }

    @Nested
    @DisplayName("Wildcard Tests")
    class WildcardTests {

        @Test
        @DisplayName("Unbounded wildcard accepts any type")
        void testUnboundedWildcard() {
            List<String> strings = List.of("A", "B", "C");
            List<Integer> integers = List.of(1, 2, 3);
            
            assertThat(printSize(strings)).isEqualTo(3);
            assertThat(printSize(integers)).isEqualTo(3);
        }

        @Test
        @DisplayName("Lower bounded wildcard allows writing")
        void testLowerBoundedWildcard() {
            List<Number> numbers = new ArrayList<>();
            List<? super Integer> superIntegers = numbers;
            
            // Can write Integer or its subclasses
            superIntegers.add(42);
            superIntegers.add(Integer.valueOf(100));
            
            assertThat(numbers).hasSize(2);
        }

        private int printSize(List<?> list) {
            return list.size();
        }
    }

    @Nested
    @DisplayName("PECS (Producer Extends Consumer Super) Tests")
    class PECSTests {

        @Test
        @DisplayName("Producer Extends - reading from source")
        void testProducerExtends() {
            List<Integer> integers = List.of(1, 2, 3);
            List<Double> doubles = List.of(1.1, 2.2, 3.3);
            
            assertThat(sumNumbers(integers)).isEqualTo(6.0);
            assertThat(sumNumbers(doubles)).isEqualTo(6.6, within(0.01));
        }

        @Test
        @DisplayName("Consumer Super - writing to destination")
        void testConsumerSuper() {
            List<Number> numbers = new ArrayList<>();
            addIntegers(numbers, List.of(1, 2, 3));
            
            assertThat(numbers).hasSize(3);
            assertThat(numbers).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("PECS in copy method")
        void testPECSCopy() {
            List<Integer> source = new ArrayList<>(List.of(1, 2, 3));
            List<Number> destination = new ArrayList<>();
            
            copy(source, destination);
            
            assertThat(destination).hasSize(3);
            assertThat(destination).containsExactly(1, 2, 3);
        }

        // Producer - extends (reading)
        private double sumNumbers(List<? extends Number> numbers) {
            double sum = 0;
            for (Number num : numbers) {
                sum += num.doubleValue();
            }
            return sum;
        }

        // Consumer - super (writing)
        private void addIntegers(List<? super Integer> destination, List<Integer> source) {
            destination.addAll(source);
        }

        // Combined PECS
        private <T> void copy(List<? extends T> source, List<? super T> destination) {
            for (T element : source) {
                destination.add(element);
            }
        }
    }

    @Nested
    @DisplayName("Type Erasure Tests")
    class TypeErasureTests {

        @Test
        @DisplayName("Generic type information is erased at runtime")
        void testTypeErasure() {
            List<String> stringList = new ArrayList<>();
            List<Integer> intList = new ArrayList<>();
            
            // At runtime, both are just List
            assertThat(stringList.getClass()).isEqualTo(intList.getClass());
        }

        @Test
        @DisplayName("Cannot create generic array directly")
        void testGenericArrayCreation() {
            // This would cause compilation error:
            // List<String>[] array = new List<String>[10]; // Error
            
            // Workaround using unchecked cast
            @SuppressWarnings("unchecked")
            List<String>[] array = (List<String>[]) new List[10];
            array[0] = new ArrayList<>();
            array[0].add("Item");
            
            assertThat(array[0]).containsExactly("Item");
        }
    }

    @Nested
    @DisplayName("Generic Inheritance Tests")
    class GenericInheritanceTests {

        @Test
        @DisplayName("Generic types are invariant")
        void testInvariance() {
            // List<Integer> is NOT a subtype of List<Number>
            // This would be a compilation error:
            // List<Number> numbers = new ArrayList<Integer>(); // Error
            
            // But wildcards allow covariance
            List<Integer> integers = new ArrayList<>();
            List<? extends Number> numbers = integers; // OK
            
            assertThat(numbers).isNotNull();
        }

        @Test
        @DisplayName("Raw types for backward compatibility")
        void testRawTypes() {
            @SuppressWarnings("rawtypes")
            List rawList = new ArrayList();
            
            @SuppressWarnings("unchecked")
            boolean added = rawList.add("String");
            
            assertThat(added).isTrue();
            assertThat(rawList).hasSize(1);
        }
    }
}
