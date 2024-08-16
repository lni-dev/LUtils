package de.linusdev.lutils.interfaces;

@SuppressWarnings("unused")
@FunctionalInterface
public interface TBiConsumer <A, B, E extends Throwable> {

    void consume(A a, B b) throws E;

}
