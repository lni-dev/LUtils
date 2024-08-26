package de.linusdev.lutils.result;

/**
 * Method return result interface.
 * @see SingleResult
 * @see BiResult
 * @see TriResult
 */
public interface Result {

    /**
     * Count of available results
     */
    int count();

    /**
     * Get result at a specific index
     * @param index index smaller than {@link #count()}
     * @return result at given {@code index}
     */
    Object get(int index);

}
