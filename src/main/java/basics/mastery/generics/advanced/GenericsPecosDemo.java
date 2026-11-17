package basics.mastery.generics.advanced;

// [coach:fix] Removed stray "// ...existing code..." placeholder (not valid before imports).
// [coach] Imports are explicit; wildcard imports would obscure which types are used.

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
 *
 * @author Srineel with Copilot
 */
// [coach:fix] Second, duplicate class Javadoc was merged into the block above.
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

    // [coach] Small closed hierarchy — a great candidate for sealed types on JDK 17+.
    // Kept as plain nested classes to preserve scope; see VERSION NOTES.
    static class Animal { public String name() { return "animal"; } }
    static class Cat extends Animal { @Override public String name() { return "cat"; } }
    static class Tiger extends Cat { @Override public String name() { return "tiger"; } }

    public static void main(String[] args) {
        System.out.println("=== Generics PECS Demo ===\n");
        // [coach] List.of returns an immutable list — fine here because we only read from it.
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
        // [coach] Function<? super A, ? extends R> matches PECS: input consumed, output produced.
        transformAndPrint(cats, a -> a.name().toUpperCase());
    }

    // PE (Producer Extends): source is a producer -> ? extends T
    // [coach] Signature is correct, but note: any subtype of T in src can be added to dst because
    // the compiler knows each element is at least a T. This is the classic safe-covariant copy.
    static <T> void copyProducerExtends(List<? extends T> src, List<T> dst) {
        for (T item : src) dst.add(item);
    }

    // CS (Consumer Super): target is a consumer -> ? super T
    // [coach:fix] Method name says "addCatsToConsumers" but T is unconstrained; the demo works
    // only because cats is List<Cat> and animals is List<Animal>. Name is misleading, not wrong.
    static <T> void addCatsToConsumers(List<T> src, List<? super T> dst) {
        dst.addAll(src);
    }

    // Reading with upper-bounded wildcard
    static void printNames(List<? extends Animal> src) {
        src.forEach(a -> System.out.println("  " + a.name()));
    }

    // Higher-order generic transformation
    // [coach] PECS applied to Function: parameter is a consumer of A (? super A),
    // return is a producer of R (? extends R). Correct and interview-worthy.
    static <A, R> void transformAndPrint(List<? extends A> src, Function<? super A, ? extends R> fn) {
        for (A a : src) {
            R r = fn.apply(a);
            System.out.println("  Transform: " + r);
        }
    }
}