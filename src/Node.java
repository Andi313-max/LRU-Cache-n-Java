/** Internal entry in the doubly linked recency list. */
final class Node {
    final int key;
    int value;
    Node prev;
    Node next;

    Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
