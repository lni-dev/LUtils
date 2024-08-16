package de.linusdev.lutils.interfaces;

@SuppressWarnings("unused")
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void consume(A a, B b, C c);
}
