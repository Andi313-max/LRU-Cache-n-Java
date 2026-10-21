public final class Main {
    public static void main(String[] args) {
        Cache cache = new LRUCache(3);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        System.out.println("get(1) = " + cache.get(1));
        cache.put(4, 40); // Reading key 1 made key 2 the least recently used.
        System.out.println("get(2) = " + cache.get(2));
        System.out.println("get(4) = " + cache.get(4));
        System.out.println("size = " + cache.size());
    }
}
