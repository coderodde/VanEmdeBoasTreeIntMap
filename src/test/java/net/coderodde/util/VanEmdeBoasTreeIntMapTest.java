package net.coderodde.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    }

    @Test
    public void testGetNextIntKey() {
        VanEmdeBoasTreeIntMap<Integer> tree = 
                new VanEmdeBoasTreeIntMap<>(-3, 3);
        
        tree.put(-3, -3);
        tree.put(-1, -1);
        tree.put(0, 0);
        tree.put(2, 2);
        
        assertEquals(-3, tree.getNextIntKey(-5));
        assertEquals(-3, tree.getNextIntKey(-4));
        
        assertEquals(-1, tree.getNextIntKey(-3));
        assertEquals(-1, tree.getNextIntKey(-2));
        
        assertEquals(0, tree.getNextIntKey(-1));
        assertEquals(2, tree.getNextIntKey(0));
        assertEquals(2, tree.getNextIntKey(1));
        assertEquals(-4, tree.getNextIntKey(2));
        assertEquals(-4, tree.getNextIntKey(3));
        assertEquals(-4, tree.getNextIntKey(4));
    }

    @Test
    public void testGetPreviousIntKey() {
        
    }

    @Test
    public void testContains() {
        
    }

    @Test
    public void testGet() {
        
    }

    @Test
    public void testPut() {
        
    }

    @Test
    public void testRemove() {
        
    }

    @Test
    public void testKeyIterator() {
        
    }
    
    @Test
    public void clear() {
        
    }
}
