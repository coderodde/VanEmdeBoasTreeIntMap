package net.coderodde.util;

import net.coderodde.util.VanEmdeBoasTreeIntMap.KeyValueMapping;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class VanEmdeBoasTreeIntMapTest {

    @Test
    public void testSize() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-10, 10);
        
        for (int i = -10, size = 0; i <= 10; ++i, ++size) {
            assertEquals(size, tree.size());
            tree.put(i, i);
            assertEquals(size + 1, tree.size());
        }
    }

    @Test
    public void testIsEmpty() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-10, 10);
        
        assertTrue(tree.isEmpty());
        tree.put(0, 0);
        assertFalse(tree.isEmpty());
        tree.put(1, 1);
        assertFalse(tree.isEmpty());
        tree.remove(0);
        assertFalse(tree.isEmpty());
        tree.remove(1);
        assertTrue(tree.isEmpty());
    }

    @Test
    public void testGetMinimumKey() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-5, 4);
        
        assertEquals(5, tree.getMinimumKey());
        
        for (int i = 4; i >= -5; --i) {
            tree.put(i, i);
            assertEquals(i, tree.getMinimumKey());
        }
        
        tree.clear();
        
        assertEquals(5, tree.getMinimumKey());
        
        tree = new VanEmdeBoasTreeIntMap<>(Integer.MAX_VALUE - 5,
                                           Integer.MAX_VALUE);
        
        assertEquals(Integer.MIN_VALUE, tree.getMinimumKey());
    }

    @Test
    public void testGetMaximumKey() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-5, 4);
        
        assertEquals(-6, tree.getMaximumKey());
        
        for (int i = -5; i <= 4; ++i) {
            tree.put(i, i);
            assertEquals(i, tree.getMaximumKey());
        }
        
        tree.clear();
        
        assertEquals(-6, tree.getMaximumKey());
        
        tree = new VanEmdeBoasTreeIntMap<>(Integer.MIN_VALUE,
                                           Integer.MIN_VALUE + 5);
        
        assertEquals(Integer.MAX_VALUE, tree.getMaximumKey());
    }

    @Test
    public void testGetNextIntKey() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-3, 3);
        
        tree.put(-3, -3);
        tree.put(-1, -1);
        tree.put(0, 0);
        tree.put(2, 2);
        
        assertEquals(-1, tree.getNextIntKey(-3));
        assertEquals(-1, tree.getNextIntKey(-2));
        
        assertEquals(0, tree.getNextIntKey(-1));
        assertEquals(2, tree.getNextIntKey(0));
        assertEquals(2, tree.getNextIntKey(1));
        assertEquals(-4, tree.getNextIntKey(2));
        assertEquals(-4, tree.getNextIntKey(3));
        
        tree.clear();
        
        for (int i = -3; i <= 3; ++i) {
            assertEquals(-4, tree.getNextIntKey(i));
        }
    }

    @Test
    public void testGetPreviousIntKey() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-3, 3);
        
        tree.put(-3, -3);
        tree.put(-1, -1);
        tree.put(0, 0);
        tree.put(2, 2);
        
        assertEquals(2, tree.getPreviousIntKey(3));
        
        assertEquals(0, tree.getPreviousIntKey(2));
        assertEquals(0, tree.getPreviousIntKey(1));
        
        assertEquals(-1, tree.getPreviousIntKey(0));
        assertEquals(-3, tree.getPreviousIntKey(-1));
        assertEquals(-3, tree.getPreviousIntKey(-2));
        assertEquals(4, tree.getPreviousIntKey(-3));
        
        tree.clear();
        
        for (int i = -3; i <= 3; ++i) {
            assertEquals(4, tree.getPreviousIntKey(i));
        }
    }

    @Test
    public void testContains() {
        VanEmdeBoasTreeIntMap<Integer> tree =
                new VanEmdeBoasTreeIntMap<>(-5, -1);
        
        tree.put(-5, null);
        tree.put(-3, -3);
        tree.put(-1, -1);
        
        assertTrue(tree.contains(-5));
        assertTrue(tree.contains(-3));
        assertTrue(tree.contains(-1));
        
        assertFalse(tree.contains(-4));
        assertFalse(tree.contains(-2));
    }

    @Test
    public void testGet() {
        VanEmdeBoasTreeIntMap<Integer> tree =
                new VanEmdeBoasTreeIntMap<>(-5, -1);
        
        tree.put(-5, null);
        tree.put(-3, -13);
        tree.put(-1, -11);
        
        assertNull(tree.get(-5));
        assertEquals(Integer.valueOf(-11), tree.get(-1));
        assertEquals(Integer.valueOf(-13), tree.get(-3));
    }

    @Test
    public void testPut() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-5, 10);
        
        for (int i = -2; i <= 4; ++i) {
            assertFalse(tree.contains(i));
            assertNull(tree.put(i, 2 * i));
            assertTrue(tree.contains(i));
        }
        
        for (int i = -3; i >= -5; --i) {
            assertFalse(tree.contains(i));
            assertNull(tree.get(i));
        }
        
        assertTrue(tree.contains(0));
        tree.remove(0);
        assertFalse(tree.contains(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLowerBound() {
        VanEmdeBoasTreeIntMap<String> tree = new VanEmdeBoasTreeIntMap<>(-4, 4);
        
        tree.put(-5, "-5");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpperBound() {
        VanEmdeBoasTreeIntMap<String> tree = new VanEmdeBoasTreeIntMap<>(-4, 4);
        
        tree.put(5, "5");
    }
    
    @Test 
    public void testTreeKeyIterator() {
        VanEmdeBoasTreeIntMap<String> tree = new VanEmdeBoasTreeIntMap<>(-4, 4);
        
        for (int i = 4; i >= -4; --i) {
            tree.put(i, null);
        }
        
        VanEmdeBoasTreeIntMap.KeyIterator iterator = tree.treeKeyIterator();
        
        for (int i = -4; i <= 4; ++i) {
            assertTrue(iterator.hasNextKey());
            assertEquals(i, iterator.nextKey());
        }
        
        assertEquals(9, tree.size());
        
        iterator = tree.treeKeyIterator();
        
        assertEquals(-4, iterator.nextKey());
        iterator.removeKey();
        assertEquals(-3, iterator.nextKey());
        assertEquals(-2, iterator.nextKey());
        assertEquals(-1, iterator.nextKey());
        iterator.removeKey();
        
        assertFalse(tree.contains(-4));
        assertTrue(tree.contains(-3));
        assertTrue(tree.contains(-2));
        assertFalse(tree.contains(-1));
        
        assertEquals(7, tree.size());
    }
    
    @Test 
    public void testTreeKeyValueIterator() {
        KeyValueMapping<String> mapping = new KeyValueMapping<>();
        VanEmdeBoasTreeIntMap<String> tree = new VanEmdeBoasTreeIntMap<>(-4, 4);
        
        for (int i = 4; i >= -4; --i) {
            tree.put(i, "" + i);
        }
        
        VanEmdeBoasTreeIntMap.KeyValueIterator<String> iterator =
                tree.get
        
        for (int i = -4; i <= 4; ++i) {
            assertTrue(iterator.hasNextKey());
            assertEquals(i, iterator.nextKey());
        }
        
        assertEquals(9, tree.size());
        
        iterator = tree.treeKeyIterator();
        
        assertEquals(-4, iterator.nextKey());
        iterator.removeKey();
        assertEquals(-3, iterator.nextKey());
        assertEquals(-2, iterator.nextKey());
        assertEquals(-1, iterator.nextKey());
        iterator.removeKey();
        
        assertFalse(tree.contains(-4));
        assertTrue(tree.contains(-3));
        assertTrue(tree.contains(-2));
        assertFalse(tree.contains(-1));
        
        assertEquals(7, tree.size());
    }

    @Test
    public void testRemove() {
        VanEmdeBoasTreeIntMap<Integer> tree = new VanEmdeBoasTreeIntMap<>(1, 5);
        
        tree.put(3, 3);
        tree.put(1, 1);
        tree.put(5, 5);
        
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(5));
        
        tree.put(1, null);
        
        assertTrue(tree.contains(1));
        
        tree.remove(1);
        
        assertFalse(tree.contains(1));
    }

    @Test
    public void clear() {
        VanEmdeBoasTreeIntMap<Integer> tree =
                new VanEmdeBoasTreeIntMap<>(3, 10);
        
        for (int i = 4, sz = 0; i <= 8; ++i, ++sz) {
            assertEquals(sz, tree.size());
            tree.put(i, null);
            assertEquals(sz + 1, tree.size());
        }
        
        assertEquals(5, tree.size());
        tree.clear();
        assertEquals(0, tree.size());
        
        for (int i = 3; i <= 10; ++i) {
            assertFalse(tree.contains(i));
            assertNull(tree.get(i));
            assertNull(tree.remove(i));
        }
    }
}
