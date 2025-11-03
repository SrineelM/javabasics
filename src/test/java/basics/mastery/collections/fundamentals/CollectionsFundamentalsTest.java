package basics.mastery.collections.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for Java Collections fundamentals
 * 
 * Tests cover List, Set, Map, and Queue implementations with various scenarios
 * including edge cases, performance characteristics, and common pitfalls.
 * 
 * @author Srineel with Copilot
 */
@DisplayName("Collections Fundamentals Tests")
class CollectionsFundamentalsTest {

    @Nested
    @DisplayName("List Tests")
    class ListTests {

        @Test
        @DisplayName("ArrayList maintains insertion order")
        void testArrayListOrder() {
            List<String> list = new ArrayList<>();
            list.add("First");
            list.add("Second");
            list.add("Third");
            
            assertThat(list).containsExactly("First", "Second", "Third");
        }

        @Test
        @DisplayName("ArrayList allows duplicates")
        void testArrayListDuplicates() {
            List<String> list = new ArrayList<>();
            list.add("Item");
            list.add("Item");
            
            assertThat(list).hasSize(2);
            assertThat(list).containsExactly("Item", "Item");
        }

        @Test
        @DisplayName("ArrayList allows null elements")
        void testArrayListNulls() {
            List<String> list = new ArrayList<>();
            list.add(null);
            list.add("NotNull");
            list.add(null);
            
            assertThat(list).hasSize(3);
            assertThat(list).containsExactly(null, "NotNull", null);
        }

        @Test
        @DisplayName("LinkedList efficient for insertions")
        void testLinkedListInsertion() {
            LinkedList<String> list = new LinkedList<>();
            list.addFirst("First");
            list.addLast("Last");
            list.add(1, "Middle");
            
            assertThat(list).containsExactly("First", "Middle", "Last");
        }

        @Test
        @DisplayName("List indexOf finds first occurrence")
        void testListIndexOf() {
            List<String> list = new ArrayList<>(List.of("A", "B", "C", "B", "D"));
            
            assertThat(list.indexOf("B")).isEqualTo(1);
            assertThat(list.lastIndexOf("B")).isEqualTo(3);
            assertThat(list.indexOf("Z")).isEqualTo(-1);
        }

        @Test
        @DisplayName("List subList creates view")
        void testSubList() {
            List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
            List<Integer> subList = list.subList(1, 4);
            
            assertThat(subList).containsExactly(2, 3, 4);
            
            // Modifying subList affects original
            subList.set(0, 20);
            assertThat(list.get(1)).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("Set Tests")
    class SetTests {

        @Test
        @DisplayName("HashSet prevents duplicates")
        void testHashSetUniqueness() {
            Set<String> set = new HashSet<>();
            set.add("Item");
            set.add("Item");
            set.add("Item");
            
            assertThat(set).hasSize(1);
            assertThat(set).containsExactly("Item");
        }

        @Test
        @DisplayName("TreeSet maintains sorted order")
        void testTreeSetSorting() {
            Set<Integer> set = new TreeSet<>();
            set.add(5);
            set.add(1);
            set.add(3);
            set.add(2);
            
            assertThat(set).containsExactly(1, 2, 3, 5);
        }

        @Test
        @DisplayName("LinkedHashSet maintains insertion order")
        void testLinkedHashSetOrder() {
            Set<String> set = new LinkedHashSet<>();
            set.add("C");
            set.add("A");
            set.add("B");
            
            assertThat(set).containsExactly("C", "A", "B");
        }

        @Test
        @DisplayName("HashSet allows single null")
        void testHashSetNull() {
            Set<String> set = new HashSet<>();
            set.add(null);
            set.add(null);
            set.add("Item");
            
            assertThat(set).hasSize(2);
            assertThat(set).contains(null, "Item");
        }

        @Test
        @DisplayName("TreeSet does not allow null in Java 7+")
        void testTreeSetNoNull() {
            Set<String> set = new TreeSet<>();
            
            assertThatThrownBy(() -> set.add(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Map Tests")
    class MapTests {

        @Test
        @DisplayName("HashMap stores key-value pairs")
        void testHashMapBasics() {
            Map<String, Integer> map = new HashMap<>();
            map.put("One", 1);
            map.put("Two", 2);
            map.put("Three", 3);
            
            assertThat(map).hasSize(3);
            assertThat(map.get("Two")).isEqualTo(2);
        }

        @Test
        @DisplayName("HashMap allows null key and values")
        void testHashMapNulls() {
            Map<String, String> map = new HashMap<>();
            map.put(null, "NullKey");
            map.put("NullValue", null);
            
            assertThat(map.get(null)).isEqualTo("NullKey");
            assertThat(map.get("NullValue")).isNull();
        }

        @Test
        @DisplayName("TreeMap maintains key order")
        void testTreeMapSorting() {
            Map<Integer, String> map = new TreeMap<>();
            map.put(3, "Three");
            map.put(1, "One");
            map.put(2, "Two");
            
            List<Integer> keys = new ArrayList<>(map.keySet());
            assertThat(keys).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("LinkedHashMap maintains insertion order")
        void testLinkedHashMapOrder() {
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("C", 3);
            map.put("A", 1);
            map.put("B", 2);
            
            List<String> keys = new ArrayList<>(map.keySet());
            assertThat(keys).containsExactly("C", "A", "B");
        }

        @Test
        @DisplayName("Map putIfAbsent doesn't overwrite")
        void testPutIfAbsent() {
            Map<String, Integer> map = new HashMap<>();
            map.put("Key", 1);
            map.putIfAbsent("Key", 2);
            
            assertThat(map.get("Key")).isEqualTo(1);
        }

        @Test
        @DisplayName("Map computeIfAbsent creates entry if missing")
        void testComputeIfAbsent() {
            Map<String, List<String>> map = new HashMap<>();
            
            map.computeIfAbsent("Key", k -> new ArrayList<>()).add("Value");
            
            assertThat(map.get("Key")).containsExactly("Value");
        }

        @Test
        @DisplayName("Map merge combines values")
        void testMapMerge() {
            Map<String, Integer> map = new HashMap<>();
            map.put("Count", 5);
            
            map.merge("Count", 3, Integer::sum);
            
            assertThat(map.get("Count")).isEqualTo(8);
        }
    }

    @Nested
    @DisplayName("Queue Tests")
    class QueueTests {

        @Test
        @DisplayName("Queue follows FIFO order")
        void testQueueFIFO() {
            Queue<String> queue = new LinkedList<>();
            queue.offer("First");
            queue.offer("Second");
            queue.offer("Third");
            
            assertThat(queue.poll()).isEqualTo("First");
            assertThat(queue.poll()).isEqualTo("Second");
            assertThat(queue.poll()).isEqualTo("Third");
        }

        @Test
        @DisplayName("PriorityQueue maintains heap order")
        void testPriorityQueue() {
            Queue<Integer> queue = new PriorityQueue<>();
            queue.offer(5);
            queue.offer(1);
            queue.offer(3);
            
            assertThat(queue.poll()).isEqualTo(1);
            assertThat(queue.poll()).isEqualTo(3);
            assertThat(queue.poll()).isEqualTo(5);
        }

        @Test
        @DisplayName("Deque supports both ends")
        void testDeque() {
            Deque<String> deque = new ArrayDeque<>();
            deque.addFirst("First");
            deque.addLast("Last");
            
            assertThat(deque.pollFirst()).isEqualTo("First");
            assertThat(deque.pollLast()).isEqualTo("Last");
        }

        @Test
        @DisplayName("Queue peek doesn't remove element")
        void testQueuePeek() {
            Queue<String> queue = new LinkedList<>();
            queue.offer("Item");
            
            assertThat(queue.peek()).isEqualTo("Item");
            assertThat(queue).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Collection Operations Tests")
    class CollectionOperationsTests {

        @Test
        @DisplayName("Collections.unmodifiableList prevents modifications")
        void testUnmodifiableList() {
            List<String> list = new ArrayList<>(List.of("A", "B", "C"));
            List<String> unmodifiable = Collections.unmodifiableList(list);
            
            assertThatThrownBy(() -> unmodifiable.add("D"))
                .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("List.copyOf creates immutable copy")
        void testListCopyOf() {
            List<String> list = List.of("A", "B", "C");
            List<String> copy = List.copyOf(list);
            
            assertThat(copy).isEqualTo(list);
            assertThatThrownBy(() -> copy.add("D"))
                .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Collections.frequency counts occurrences")
        void testFrequency() {
            List<String> list = List.of("A", "B", "A", "C", "A");
            
            assertThat(Collections.frequency(list, "A")).isEqualTo(3);
            assertThat(Collections.frequency(list, "B")).isEqualTo(1);
            assertThat(Collections.frequency(list, "Z")).isEqualTo(0);
        }

        @Test
        @DisplayName("Collections.disjoint checks for common elements")
        void testDisjoint() {
            List<Integer> list1 = List.of(1, 2, 3);
            List<Integer> list2 = List.of(4, 5, 6);
            List<Integer> list3 = List.of(3, 4, 5);
            
            assertThat(Collections.disjoint(list1, list2)).isTrue();
            assertThat(Collections.disjoint(list1, list3)).isFalse();
        }
    }
}
