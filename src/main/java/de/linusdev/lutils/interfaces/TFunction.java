package de.linusdev.lutils.interfaces;

@SuppressWarnings("unused")
@FunctionalInterface
public interface TFunction<T, R, E extends Throwable> {

    R apply(T param) throws E;

}
