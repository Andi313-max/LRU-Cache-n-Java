import java.util.HashMap;
import java.util.Map;

/**
 * Single-threaded LRU cache using a hash index and a doubly linked list.
 * get and put take expected amortized O(1) time; storage is O(capacity).
 */
public final class LRUCache implements Cache {
    private final int capacity;
    private final Map<Integer, Node> entries = new HashMap<>();
    // Sentinels: head.next is most recent; tail.prev is least recent.
    private final Node head = new Node(-1, 0);
    private final Node tail = new Node(-1, 0);

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public int get(int key) {
        validateKey(key);
        Node node = entries.get(key);
        if (node == null) {
            return -1;
        }
        unlink(node);
        addFirst(node);
        return node.value;
    }

    @Override
    public void put(int key, int value) {
        validateKey(key);
        Node node = entries.get(key);
        if (node != null) {
            node.value = value;
            unlink(node);
            addFirst(node);
            return;
        }

        if (entries.size() == capacity) {
            Node oldest = tail.prev;
            unlink(oldest);
            entries.remove(oldest.key);
        }

        node = new Node(key, value);
        entries.put(key, node);
        addFirst(node);
    }

    @Override
    public int size() {
        return entries.size();
    }

    private static void validateKey(int key) {
        if (key < 0) {
            throw new IllegalArgumentException("Key must be non-negative");
        }
    }

    private void unlink(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addFirst(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
}
