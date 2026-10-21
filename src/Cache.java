/** A fixed-capacity cache with non-negative integer keys and integer values. */
public interface Cache {
    /**
     * Returns the value, or -1 if absent. A hit marks the entry as most recently used.
     * @throws IllegalArgumentException if key is negative
     */
    int get(int key);

    /**
     * Adds or updates an entry and marks it as most recently used.
     * @throws IllegalArgumentException if key is negative
     */
    void put(int key, int value);

    /** Returns the number of stored entries. */
    int size();
}
