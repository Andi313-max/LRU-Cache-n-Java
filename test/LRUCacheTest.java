import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/** Dependency-free tests; assertions stay enabled without the JVM -ea option. */
public final class LRUCacheTest {
    public static void main(String[] args) {
        emptyCache();
        evictsLeastRecentlyUsed();
        updatesRefreshRecency();
        missingReadsDoNotRefreshRecency();
        capacityOne();
        rejectsInvalidInputs();
        matchesLinkedHashMap();
        System.out.println("All 7 LRU cache tests passed.");
    }

    private static void emptyCache() {
        Cache cache = new LRUCache(3);
        equal(0, cache.size(), "initial size");
        equal(-1, cache.get(0), "missing key in empty cache");
        equal(0, cache.size(), "miss does not insert");
    }

    private static void evictsLeastRecentlyUsed() {
        Cache cache = new LRUCache(3);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        equal(10, cache.get(1), "read refreshes key 1");
        cache.put(4, 40);
        equal(-1, cache.get(2), "key 2 was least recent");
        equal(30, cache.get(3), "key 3 remains");
        equal(10, cache.get(1), "key 1 remains");
        equal(40, cache.get(4), "new key remains");
        equal(3, cache.size(), "size stays at capacity");
    }

    private static void updatesRefreshRecency() {
        Cache cache = new LRUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(1, 11);
        equal(2, cache.size(), "update does not grow cache");
        cache.put(3, 30);
        equal(-1, cache.get(2), "update refreshes key 1");
        equal(11, cache.get(1), "updated value");
        cache.put(1, 12);
        cache.put(4, 40);
        equal(-1, cache.get(3), "updating most recent entry preserves list");
        equal(12, cache.get(1), "second update");
    }

    private static void missingReadsDoNotRefreshRecency() {
        Cache cache = new LRUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        equal(-1, cache.get(99), "missing read");
        cache.put(3, 30);
        equal(-1, cache.get(1), "miss preserves eviction order");
        equal(20, cache.get(2), "most recent entry survives");
    }

    private static void capacityOne() {
        Cache cache = new LRUCache(1);
        cache.put(0, -1);
        equal(-1, cache.get(0), "negative values are allowed");
        equal(1, cache.size(), "stored -1 occupies an entry");
        cache.put(0, Integer.MIN_VALUE);
        equal(Integer.MIN_VALUE, cache.get(0), "update at capacity one");
        cache.put(Integer.MAX_VALUE, Integer.MAX_VALUE);
        equal(-1, cache.get(0), "old entry evicted");
        equal(Integer.MAX_VALUE, cache.get(Integer.MAX_VALUE), "maximum int key/value");
        equal(1, cache.size(), "capacity one is preserved");
    }

    private static void rejectsInvalidInputs() {
        rejects(() -> new LRUCache(0), "zero capacity");
        rejects(() -> new LRUCache(-1), "negative capacity");
        Cache cache = new LRUCache(1);
        cache.put(1, 10);
        rejects(() -> cache.get(-1), "negative get key");
        rejects(() -> cache.put(-1, 20), "negative put key");
        equal(1, cache.size(), "invalid input preserves size");
        equal(10, cache.get(1), "invalid input preserves entry");
    }

    private static void matchesLinkedHashMap() {
        Random random = new Random(20261021L);
        for (int capacity : new int[] {1, 2, 3, 16, 128}) {
            Cache cache = new LRUCache(capacity);
            Map<Integer, Integer> reference = new LinkedHashMap<Integer, Integer>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > capacity;
                }
            };
            for (int step = 0; step < 10000; step++) {
                int key = random.nextInt(capacity * 2 + 1);
                if (random.nextBoolean()) {
                    int value = random.nextInt();
                    cache.put(key, value);
                    reference.put(key, value);
                } else {
                    Integer expected = reference.get(key);
                    equal(expected == null ? -1 : expected, cache.get(key),
                            "reference read at capacity " + capacity + ", step " + step);
                }
                equal(reference.size(), cache.size(), "reference size");
            }
            for (int key = 0; key <= capacity * 2; key++) {
                Integer expected = reference.get(key);
                equal(expected == null ? -1 : expected, cache.get(key), "final reference contents");
            }
        }
    }

    private static void equal(int expected, int actual, String context) {
        if (expected != actual) {
            throw new AssertionError(context + ": expected " + expected + ", got " + actual);
        }
    }

    private static void rejects(Runnable action, String context) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError(context + ": expected IllegalArgumentException");
    }
}
