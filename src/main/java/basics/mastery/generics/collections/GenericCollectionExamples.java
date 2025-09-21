package basics.mastery.advanced;.generics.collections;

import java.util.*;
import java.util.function.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive examples of generics with Java Collections Framework.
 * 
 * This class demonstrates:
 * - Generic collections usage patterns
 * - Type-safe collection operations
 * - Custom generic collection implementations
 * - Collection variance and wildcards
 * - Generic algorithms for collections
 * - Performance considerations with generics
 * - Thread-safe generic collections
 *
 * @author Java Generics Tutorial
 * @version 1.0
 */
public class GenericCollectionExamples {
    
    // ===== TYPE-SAFE COLLECTION UTILITIES =====
    
    /**
     * Creates a type-safe list from varargs.
     * Demonstrates generic factory methods.
     * 
     * @param <T> element type
     * @param elements elements to include
     * @return new ArrayList containing elements
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> List<T> listOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }
    
    /**
     * Creates a type-safe set from varargs.
     * 
     * @param <T> element type
     * @param elements elements to include
     * @return new HashSet containing elements
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Set<T> setOf(T... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }
    
    /**
     * Creates a type-safe map from key-value pairs.
     * 
     * @param <K> key type
     * @param <V> value type
     * @param entries key-value pairs
     * @return new HashMap containing entries
     */
    @SafeVarargs
    public static <K, V> Map<K, V> mapOf(Map.Entry<K, V>... entries) {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
    
    /**
     * Helper method to create map entries.
     * 
     * @param <K> key type
     * @param <V> value type
     * @param key the key
     * @param value the value
     * @return new map entry
     */
    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
    
    // ===== COLLECTION TRANSFORMATION UTILITIES =====
    
    /**
     * Transforms a collection using a mapper function.
     * Demonstrates generic transformation pattern.
     * 
     * @param <T> input type
     * @param <R> result type
     * @param source source collection
     * @param mapper transformation function
     * @return new list with transformed elements
     */
    public static <T, R> List<R> map(Collection<? extends T> source, 
                                     Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>(source.size());
        for (T item : source) {
            result.add(mapper.apply(item));
        }
        return result;
    }
    
    /**
     * Filters a collection based on a predicate.
     * 
     * @param <T> element type
     * @param source source collection
     * @param predicate filter condition
     * @return filtered list
     */
    public static <T> List<T> filter(Collection<? extends T> source, 
                                     Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    /**
     * Reduces a collection to a single value.
     * 
     * @param <T> element type
     * @param <R> result type
     * @param source source collection
     * @param identity initial value
     * @param accumulator combining function
     * @return reduced value
     */
    public static <T, R> R reduce(Collection<? extends T> source, 
                                  R identity, 
                                  BiFunction<R, ? super T, R> accumulator) {
        R result = identity;
        for (T item : source) {
            result = accumulator.apply(result, item);
        }
        return result;
    }
    
    /**
     * Partitions a collection into two lists based on predicate.
     * 
     * @param <T> element type
     * @param source source collection
     * @param predicate partitioning condition
     * @return pair of (matching, non-matching) lists
     */
    public static <T> Pair<List<T>, List<T>> partition(Collection<? extends T> source, 
                                                       Predicate<? super T> predicate) {
        List<T> matching = new ArrayList<>();
        List<T> notMatching = new ArrayList<>();
        
        for (T item : source) {
            if (predicate.test(item)) {
                matching.add(item);
            } else {
                notMatching.add(item);
            }
        }
        
        return new Pair<>(matching, notMatching);
    }
    
    /**
     * Groups elements by a key function.
     * 
     * @param <T> element type
     * @param <K> key type
     * @param source source collection
     * @param keyExtractor key extraction function
     * @return map of keys to lists of elements
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<? extends T> source, 
                                                 Function<? super T, ? extends K> keyExtractor) {
        Map<K, List<T>> groups = new HashMap<>();
        for (T item : source) {
            K key = keyExtractor.apply(item);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return groups;
    }
    
    /**
     * Flattens a collection of collections.
     * 
     * @param <T> element type
     * @param nested collection of collections
     * @return flattened list
     */
    public static <T> List<T> flatten(Collection<? extends Collection<? extends T>> nested) {
        List<T> result = new ArrayList<>();
        for (Collection<? extends T> inner : nested) {
            result.addAll(inner);
        }
        return result;
    }
    
    // ===== CUSTOM GENERIC COLLECTIONS =====
    
    /**
     * Generic circular buffer implementation.
     * Demonstrates custom generic collection with fixed capacity.
     * 
     * @param <T> element type
     */
    public static class CircularBuffer<T> implements Iterable<T> {
        private final Object[] buffer;
        private final int capacity;
        private int head = 0;
        private int tail = 0;
        private int size = 0;
        
        /**
         * Creates a circular buffer with given capacity.
         * 
         * @param capacity maximum number of elements
         */
        public CircularBuffer(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            this.capacity = capacity;
            this.buffer = new Object[capacity];
        }
        
        /**
         * Adds an element to the buffer.
         * If buffer is full, overwrites the oldest element.
         * 
         * @param element element to add
         */
        public void add(T element) {
            buffer[tail] = element;
            tail = (tail + 1) % capacity;
            
            if (size < capacity) {
                size++;
            } else {
                // Buffer is full, move head forward
                head = (head + 1) % capacity;
            }
        }
        
        /**
         * Gets the oldest element without removing it.
         * 
         * @return oldest element, or null if empty
         */
        @SuppressWarnings("unchecked")
        public T peek() {
            return size == 0 ? null : (T) buffer[head];
        }
        
        /**
         * Removes and returns the oldest element.
         * 
         * @return oldest element, or null if empty
         */
        @SuppressWarnings("unchecked")
        public T poll() {
            if (size == 0) return null;
            
            T element = (T) buffer[head];
            buffer[head] = null; // Help GC
            head = (head + 1) % capacity;
            size--;
            return element;
        }
        
        /**
         * Gets element at specific index (0 = oldest).
         * 
         * @param index the index
         * @return element at index
         * @throws IndexOutOfBoundsException if index is invalid
         */
        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            int actualIndex = (head + index) % capacity;
            return (T) buffer[actualIndex];
        }
        
        public int size() { return size; }
        public int capacity() { return capacity; }
        public boolean isEmpty() { return size == 0; }
        public boolean isFull() { return size == capacity; }
        
        /**
         * Converts to a list in chronological order.
         * 
         * @return list of elements from oldest to newest
         */
        public List<T> toList() {
            List<T> result = new ArrayList<>(size);
            for (T element : this) {
                result.add(element);
            }
            return result;
        }
        
        @Override
        public Iterator<T> iterator() {
            return new CircularBufferIterator();
        }
        
        private class CircularBufferIterator implements Iterator<T> {
            private int currentIndex = 0;
            
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int actualIndex = (head + currentIndex) % capacity;
                currentIndex++;
                return (T) buffer[actualIndex];
            }
        }
    }
    
    /**
     * Generic Trie (prefix tree) implementation.
     * Demonstrates complex generic data structure.
     */
    public static class Trie<V> {
        private final TrieNode<V> root = new TrieNode<>();
        
        /**
         * Inserts a key-value pair.
         * 
         * @param key the key
         * @param value the value
         */
        public void put(String key, V value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            
            TrieNode<V> current = root;
            for (char c : key.toCharArray()) {
                current = current.children.computeIfAbsent(c, k -> new TrieNode<>());
            }
            current.value = value;
            current.isEndOfWord = true;
        }
        
        /**
         * Gets the value for a key.
         * 
         * @param key the key
         * @return the value, or null if not found
         */
        public V get(String key) {
            if (key == null) return null;
            
            TrieNode<V> node = findNode(key);
            return (node != null && node.isEndOfWord) ? node.value : null;
        }
        
        /**
         * Checks if a key exists.
         * 
         * @param key the key
         * @return true if key exists
         */
        public boolean containsKey(String key) {
            if (key == null) return false;
            
            TrieNode<V> node = findNode(key);
            return node != null && node.isEndOfWord;
        }
        
        /**
         * Finds all keys with given prefix.
         * 
         * @param prefix the prefix
         * @return list of keys with the prefix
         */
        public List<String> keysWithPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            if (prefix == null) return result;
            
            TrieNode<V> prefixNode = findNode(prefix);
            if (prefixNode != null) {
                collectKeys(prefixNode, prefix, result);
            }
            return result;
        }
        
        /**
         * Gets all key-value pairs with given prefix.
         * 
         * @param prefix the prefix
         * @return map of keys to values
         */
        public Map<String, V> entriesWithPrefix(String prefix) {
            Map<String, V> result = new HashMap<>();
            if (prefix == null) return result;
            
            TrieNode<V> prefixNode = findNode(prefix);
            if (prefixNode != null) {
                collectEntries(prefixNode, prefix, result);
            }
            return result;
        }
        
        private TrieNode<V> findNode(String key) {
            TrieNode<V> current = root;
            for (char c : key.toCharArray()) {
                current = current.children.get(c);
                if (current == null) {
                    return null;
                }
            }
            return current;
        }
        
        private void collectKeys(TrieNode<V> node, String prefix, List<String> result) {
            if (node.isEndOfWord) {
                result.add(prefix);
            }
            
            for (Map.Entry<Character, TrieNode<V>> entry : node.children.entrySet()) {
                collectKeys(entry.getValue(), prefix + entry.getKey(), result);
            }
        }
        
        private void collectEntries(TrieNode<V> node, String prefix, Map<String, V> result) {
            if (node.isEndOfWord) {
                result.put(prefix, node.value);
            }
            
            for (Map.Entry<Character, TrieNode<V>> entry : node.children.entrySet()) {
                collectEntries(entry.getValue(), prefix + entry.getKey(), result);
            }
        }
        
        private static class TrieNode<V> {
            Map<Character, TrieNode<V>> children = new HashMap<>();
            V value;
            boolean isEndOfWord = false;
        }
    }
    
    // ===== THREAD-SAFE GENERIC COLLECTIONS =====
    
    /**
     * Thread-safe generic cache with LRU eviction.
     * 
     * @param <K> key type
     * @param <V> value type
     */
    public static class ConcurrentLRUCache<K, V> {
        private final int maxSize;
        private final ConcurrentHashMap<K, Node<K, V>> cache;
        private final Object lock = new Object();
        private Node<K, V> head;
        private Node<K, V> tail;
        
        public ConcurrentLRUCache(int maxSize) {
            this.maxSize = maxSize;
            this.cache = new ConcurrentHashMap<>();
            
            // Initialize dummy head and tail
            this.head = new Node<>(null, null);
            this.tail = new Node<>(null, null);
            this.head.next = this.tail;
            this.tail.prev = this.head;
        }
        
        /**
         * Gets a value by key.
         * 
         * @param key the key
         * @return the value, or null if not found
         */
        public V get(K key) {
            Node<K, V> node = cache.get(key);
            if (node != null) {
                synchronized (lock) {
                    moveToHead(node);
                }
                return node.value;
            }
            return null;
        }
        
        /**
         * Puts a key-value pair.
         * 
         * @param key the key
         * @param value the value
         * @return the previous value, or null
         */
        public V put(K key, V value) {
            Node<K, V> existing = cache.get(key);
            
            if (existing != null) {
                V oldValue = existing.value;
                existing.value = value;
                synchronized (lock) {
                    moveToHead(existing);
                }
                return oldValue;
            } else {
                Node<K, V> newNode = new Node<>(key, value);
                cache.put(key, newNode);
                
                synchronized (lock) {
                    addToHead(newNode);
                    
                    if (cache.size() > maxSize) {
                        Node<K, V> removed = removeTail();
                        cache.remove(removed.key);
                    }
                }
                return null;
            }
        }
        
        /**
         * Removes a key.
         * 
         * @param key the key
         * @return the removed value, or null
         */
        public V remove(K key) {
            Node<K, V> node = cache.remove(key);
            if (node != null) {
                synchronized (lock) {
                    removeNode(node);
                }
                return node.value;
            }
            return null;
        }
        
        /**
         * Gets the current size.
         * 
         * @return current number of entries
         */
        public int size() {
            return cache.size();
        }
        
        /**
         * Checks if cache is empty.
         * 
         * @return true if empty
         */
        public boolean isEmpty() {
            return cache.isEmpty();
        }
        
        /**
         * Clears the cache.
         */
        public void clear() {
            cache.clear();
            synchronized (lock) {
                head.next = tail;
                tail.prev = head;
            }
        }
        
        private void addToHead(Node<K, V> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node<K, V> node) {
            removeNode(node);
            addToHead(node);
        }
        
        private Node<K, V> removeTail() {
            Node<K, V> lastNode = tail.prev;
            removeNode(lastNode);
            return lastNode;
        }
        
        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> prev;
            Node<K, V> next;
            
            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    // ===== COLLECTION ALGORITHMS =====
    
    /**
     * Generic binary search implementation.
     * 
     * @param <T> element type
     * @param list sorted list to search
     * @param key key to find
     * @param comparator comparison function
     * @return index if found, negative insertion point if not found
     */
    public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> comparator) {
        int low = 0;
        int high = list.size() - 1;
        
        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = list.get(mid);
            int cmp = comparator.compare(midVal, key);
            
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // Found
            }
        }
        return -(low + 1); // Not found
    }
    
    /**
     * Generic merge operation for sorted collections.
     * 
     * @param <T> element type
     * @param first first sorted collection
     * @param second second sorted collection
     * @param comparator comparison function
     * @return merged sorted list
     */
    public static <T> List<T> merge(Collection<? extends T> first, 
                                    Collection<? extends T> second, 
                                    Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(first.size() + second.size());
        Iterator<? extends T> iter1 = first.iterator();
        Iterator<? extends T> iter2 = second.iterator();
        
        T item1 = iter1.hasNext() ? iter1.next() : null;
        T item2 = iter2.hasNext() ? iter2.next() : null;
        
        while (item1 != null && item2 != null) {
            if (comparator.compare(item1, item2) <= 0) {
                result.add(item1);
                item1 = iter1.hasNext() ? iter1.next() : null;
            } else {
                result.add(item2);
                item2 = iter2.hasNext() ? iter2.next() : null;
            }
        }
        
        // Add remaining elements
        while (item1 != null) {
            result.add(item1);
            item1 = iter1.hasNext() ? iter1.next() : null;
        }
        while (item2 != null) {
            result.add(item2);
            item2 = iter2.hasNext() ? iter2.next() : null;
        }
        
        return result;
    }
    
    // ===== SUPPORTING CLASSES =====
    
    /**
     * Simple pair class for utility methods.
     * 
     * @param <T> first element type
     * @param <U> second element type
     */
    public static class Pair<T, U> {
        private final T first;
        private final U second;
        
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
        
        public T getFirst() { return first; }
        public U getSecond() { return second; }
        
        @Override
        public String toString() {
            return String.format("(%s, %s)", first, second);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}