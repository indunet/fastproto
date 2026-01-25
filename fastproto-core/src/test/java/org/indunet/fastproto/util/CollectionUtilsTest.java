package org.indunet.fastproto.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionUtilsTest {
    
    @Test
    public void testNewInstanceList() throws Exception {
        Collection<?> collection = CollectionUtils.newInstance(List.class);
        assertNotNull(collection);
        assertTrue(collection instanceof ArrayList);
    }
    
    @Test
    public void testNewInstanceCollection() throws Exception {
        Collection<?> collection = CollectionUtils.newInstance(Collection.class);
        assertNotNull(collection);
        assertTrue(collection instanceof ArrayList);
    }
    
    @Test
    public void testNewInstanceSet() throws Exception {
        Collection<?> collection = CollectionUtils.newInstance(Set.class);
        assertNotNull(collection);
        assertTrue(collection instanceof HashSet);
    }
    
    @Test
    public void testNewInstanceDeque() throws Exception {
        Collection<?> collection = CollectionUtils.newInstance(Deque.class);
        assertNotNull(collection);
        assertTrue(collection instanceof ArrayDeque);
    }
    
    @Test
    public void testNewInstanceArrayList() throws Exception {
        Collection<?> collection = CollectionUtils.newInstance(ArrayList.class);
        assertNotNull(collection);
        assertTrue(collection instanceof ArrayList);
    }
    
    @Test
    public void testNewInstanceHashSet() throws Exception {
        Collection<?> collection = CollectionUtils.newInstance(HashSet.class);
        assertNotNull(collection);
        assertTrue(collection instanceof HashSet);
    }
    
    @Test
    public void testNewInstanceUnsupported() {
        assertThrows(IllegalArgumentException.class, () -> {
            CollectionUtils.newInstance(Queue.class);
        });
    }
    
    @Test
    public void testElementType() {
        Type elementType = CollectionUtils.elementType(StringList.class);
        assertNotNull(elementType);
        assertEquals(String.class, elementType);
    }
    
    @Test
    public void testElementTypeInteger() {
        Type elementType = CollectionUtils.elementType(IntegerSet.class);
        assertNotNull(elementType);
        assertEquals(Integer.class, elementType);
    }
    
    @Test
    public void testElementTypeNoGeneric() {
        // ArrayList doesn't directly implement a parameterized Collection interface,
        // so it will throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            CollectionUtils.elementType(RawList.class);
        });
    }
    
    // Helper classes for testing
    static class StringList implements List<String> {
        @Override public int size() { return 0; }
        @Override public boolean isEmpty() { return false; }
        @Override public boolean contains(Object o) { return false; }
        @Override public Iterator<String> iterator() { return null; }
        @Override public Object[] toArray() { return new Object[0]; }
        @Override public <T> T[] toArray(T[] a) { return null; }
        @Override public boolean add(String s) { return false; }
        @Override public boolean remove(Object o) { return false; }
        @Override public boolean containsAll(Collection<?> c) { return false; }
        @Override public boolean addAll(Collection<? extends String> c) { return false; }
        @Override public boolean addAll(int index, Collection<? extends String> c) { return false; }
        @Override public boolean removeAll(Collection<?> c) { return false; }
        @Override public boolean retainAll(Collection<?> c) { return false; }
        @Override public void clear() { }
        @Override public String get(int index) { return null; }
        @Override public String set(int index, String element) { return null; }
        @Override public void add(int index, String element) { }
        @Override public String remove(int index) { return null; }
        @Override public int indexOf(Object o) { return 0; }
        @Override public int lastIndexOf(Object o) { return 0; }
        @Override public ListIterator<String> listIterator() { return null; }
        @Override public ListIterator<String> listIterator(int index) { return null; }
        @Override public List<String> subList(int fromIndex, int toIndex) { return null; }
    }
    
    static class IntegerSet implements Set<Integer> {
        @Override public int size() { return 0; }
        @Override public boolean isEmpty() { return false; }
        @Override public boolean contains(Object o) { return false; }
        @Override public Iterator<Integer> iterator() { return null; }
        @Override public Object[] toArray() { return new Object[0]; }
        @Override public <T> T[] toArray(T[] a) { return null; }
        @Override public boolean add(Integer integer) { return false; }
        @Override public boolean remove(Object o) { return false; }
        @Override public boolean containsAll(Collection<?> c) { return false; }
        @Override public boolean addAll(Collection<? extends Integer> c) { return false; }
        @Override public boolean retainAll(Collection<?> c) { return false; }
        @Override public boolean removeAll(Collection<?> c) { return false; }
        @Override public void clear() { }
    }
    
    static class RawList extends ArrayList {
    }
}

