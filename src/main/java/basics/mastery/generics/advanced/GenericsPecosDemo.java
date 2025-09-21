package basics.mastery.generics.advanced;

/**
 * GenericsPecosDemo
 *
 * Addresses common beginner-to-advanced gaps in generics:
 * - PECS (Producer Extends, Consumer Super)
 * - Wildcards vs type parameters
 * - Invariance of generics and safe covariance via wildcards
 * - Higher-order functions with generics
 *
 * Additive educational example; no existing logic is modified.
 *
 * @author Srineel with Copilot
 */
// ...existing code...

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * GenericsPecosDemo
 *
 * Addresses common beginner-to-advanced gaps in generics:
 * - PECS (Producer Extends, Consumer Super)
 * - Wildcards vs type parameters
 * - Invariance of generics and safe covariance via wildcards
 * - Higher-order functions with generics
 *
 * Additive educational example; no existing logic is modified.
 */
public class GenericsPecosDemo {

    static class Animal { public String name() { return "animal"; } }
    static class Cat extends Animal { @Override public String name() { return "cat"; } }
    static class Tiger extends Cat { @Override public String name() { return "tiger"; } }

    public static void main(String[] args) {
        System.out.println("=== Generics PECS Demo ===\n");
        List<Tiger> tigers = List.of(new Tiger(), new Tiger());
        List<Cat> cats = new ArrayList<>();

        // Producer Extends: reading from producer collection
        copyProducerExtends(tigers, cats);
        System.out.println("Cats after copy: " + cats.size());

        // Consumer Super: writing into consumer collection
        List<Animal> animals = new ArrayList<>();
        addCatsToConsumers(cats, animals);
        System.out.println("Animals after add: " + animals.size());

        // Wildcards vs type params
        printNames(cats); // ? extends Animal
        transformAndPrint(cats, a -> a.name().toUpperCase());
    }

    // PE (Producer Extends): source is a producer -> ? extends T
    static <T> void copyProducerExtends(List<? extends T> src, List<T> dst) {
        for (T item : src) dst.add(item);
    }

    // CS (Consumer Super): target is a consumer -> ? super T
    static <T> void addCatsToConsumers(List<T> src, List<? super T> dst) {
        dst.addAll(src);
    }

    // Reading with upper-bounded wildcard
    static void printNames(List<? extends Animal> src) {
        src.forEach(a -> System.out.println("  " + a.name()));
    }

    // Higher-order generic transformation
    static <A, R> void transformAndPrint(List<? extends A> src, Function<? super A, ? extends R> fn) {
        for (A a : src) {
            R r = fn.apply(a);
            System.out.println("  Transform: " + r);
        }
    }
}
